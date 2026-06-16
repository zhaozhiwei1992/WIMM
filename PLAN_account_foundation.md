# 科目体系地基改造方案（v2 — 基于真实 init 状态）

> 目标：接入 AI 录入前，把"按家庭（租户）独立维护科目"地基打牢。
> 已定决策：① 每家庭独立一份科目（注册时复制预设）② 单表 acct_account_cls，程序区分账户/科目 ③ 信用卡放负债(贷) ④ default 数据清空重初始化 ⑤ 家庭号 = fam_<userId> ⑥ AI 录入（spring-ai-alibaba + OpenAI兼容 + 解析确认）为下一步独立计划。

---

## 关键事实（审查真实 SQL 后确认）

初始化实际由 `z-module-system/.../liquibase/system/changelog/00000_init.sql` 的**单一 changeset `zzw:00000`** 完成，它每次启动会 `delete from acct_account_cls` 后重新 INSERT 27 个预设科目（幂等重置）。

**严重不一致（必须先消除）：**

| 项 | `00000_init.sql`（实际执行） | `z-module-acct/00001_*.sql`（死文件） | 实体 |
|---|---|---|---|
| 执行 | ✅ | ❌ 从未被 include | — |
| `tenant_id` 列 | **❌ 缺失** | ✅ 有 | ✅ 有字段 |
| `balance_dir` 列 | **❌ 缺失** | ✅ 有 | ✅ 有字段 |
| 预设科目数 | 27（文档版） | 11（废弃） | — |
| `acct_vou_detail` 建表 | ❌ 不在此 | ✅ 但没执行 | ✅ |

> 即：现在库里 `acct_account_cls` 实际**没有 `tenant_id` / `balance_dir` 列**；`acct_vou_detail` 可能根本没被 liquibase 建过（若能记账，是手动建的）。两份 changelog（acct 模块的 00001/00002）是**死代码**，从未被 include，应清理。

`CustomStatementInspector` 对 `acct_vou_detail` 的 tenant 过滤、`AbstractAuditingEntity` 的 `tenantId` 审计写入——都因物理列缺失而**实际失效**。这是租户隔离没生效的根因。

---

## 改造方案（4 批）

### 批 1：消除初始化不一致（纯 SQL/liquibase）

1. **统一到 `00000_init.sql`，删除死文件**
   - 删除 `z-module-acct/src/main/resources/liquibase/changelog/00001_init_account_cls.sql` 和 `00002_init_voucher_detail.sql`（从没执行过，留着误导）。
   - 在 `00000_init.sql` 的 `acct_account_cls` 建表语句补 `balance_dir int` 和 `tenant_id varchar(50)` 两列（对齐实体）。
   - 把 `acct_vou_detail` 建表也并进 `00000_init.sql`（或新建独立 changeset 并加进 master.xml include），确保真正被执行。

2. **唯一约束改 `(tenant_id, code)`**
   - `00000_init.sql` 里 `acct_account_cls` 目前**没有** `uk_acct_cls_code`（因为死文件里的约束从没生效）。
   - 新增 `CONSTRAINT uk_acct_cls_tenant_code UNIQUE (tenant_id, code)`。

3. **预设科目补 `balance_dir` 与 `tenant_id`，并补全账户类科目**
   - 27 个预设 INSERT 加上 `balance_dir`（资产1/负债-1/收入-1/支出1；二级沿用父方向）。
   - 加 AI 要用的账户类科目：微信、支付宝、花呗（资产类借方）；信用卡已存在（负债贷方，符合决策）。
   - 清空重初始化（决策④）：保持现有 `delete + INSERT` 幂等模式即可。

> changeset 处理：`zzw:00000` 是已执行过的 changeset，**直接改它不会重跑**（liquibase 按 md5sum 校验，改了会报 checksum mismatch）。所以批 1 要么(a)删库重来（决策④本就要清空，可接受），要么(b)新建一个 `00003_fix_acct_columns.sql` changeset 做 `ALTER TABLE ADD COLUMN` + 数据修补。**推荐 (b) + 配合清空。**

### 批 2：查询隔离（Service 层显式按 tenantId）

在 `AccountClsService` 所有查询注入 `SecurityUtils.getTenantId()` 过滤（参考 `VoucherDetailService` 用 Example 的写法，把 `createdBy` 换成 `tenantId`）：
- `getAllAccountClsTree` / `getAccountClsSelect` / `getAllRootAccountCls` / `getAccountCls` / `findAllByCodeIn` / 增删改 → 都限本家庭。
- 不用 `CustomStatementInspector`（避免再踩 SQL 重写/占位符坑）。

### 批 3：注册初始化（每家庭复制预设）

- `LoginResource.register()`：给新用户 `tenantId = "fam_" + userId`（决策⑤），写回 `sys_user.tenant_id`。
- 用 Spring 事件 `TenantCreatedEvent` 解耦（system 发、acct 监听），避免 system 反向依赖 acct。
- acct 监听器：从"预设模板"复制一份科目到该 tenantId。模板来源=一份只读的 code/name/balance_dir/层级 常量（Java enum 或单独 template 表，**推荐 Java 常量**，省一张表）。

### 批 4：账户区分（程序级）

- `AccountMatcher`：账户 = 资产/负债下、code 命中白名单（现金/银行/微信/支付宝/花呗/信用卡…）的末级科目。
- 预留风险：纯靠 code 区分，用户误建在账户 code 段会乱。若批 1~3 跑通后觉得不稳，再回头加 `is_account int` 字段（成本低）。

---

## 待确认（1 个，不阻塞）
- 现有库里 `acct_vou_detail` 是手动建的吗？批 1 会重新用 liquibase 建一次，若已有同名表会冲突 → 需确认是否清库重来。基于决策④（清空重初始化），倾向**直接 drop 两张 acct 表 + DATABASECHANGELOG 里 acct 记录，让 liquibase 干净重建**。
