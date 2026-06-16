# AI 记账功能实现方案

> 目标：用户在小程序输入自然语言（"买衣服花了200，微信付的"），AI 解析为借贷分录结构，用户确认后复用现有记账接口入库。
> 已定决策：① AI 代码放进 z-module-acct（不新建模块）② 解析不出用兜底科目 ③ OpenAI base-url 写死默认值、key 走环境变量 OPENAI_API_KEY ④ 用户确认后再入库（方案1）。

---

## 核心流程

```
小程序输入文本
  → POST /acct/account/ai-parse
  → 后端取当前家庭科目列表作为上下文 + 系统 prompt 喂给 OpenAI
  → AI 返回 JSON {type, bizAccountCode, payAccountCode, amt, remark}
  → 后端校验/兜底, 转成 AiParseResultVO 返回
  → 小程序展示预览(支出·衣服 ¥200 ·微信) + 用户确认/微调
  → 用户确认 → POST /acct/account (复用现有记账接口, 零改动)
```

借贷拆分逻辑：复用现有 AccountResource.acct()，它已能按 creditAccount/debitAccount + amt 自动拆两条分录。AI 只负责把文本翻译成"两个科目代码 + 金额"，不碰借贷。

---

## 后端实现（z-module-acct）

### 1. 依赖与配置

**pom.xml（z-module-acct）**新增：
- `spring-ai-bom`（版本 1.0.1，本地 m2 已有，Spring Boot 3.3.3 兼容）
- `spring-ai-starter-model-openai`

**application.yml** 新增（替换原悬空的 tongyi 配置）：
```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}        # 环境变量
      base-url: https://api.openai.com  # 写死默认(可被环境变量覆盖)
      chat:
        options:
          model: gpt-4o-mini             # 写死默认
          temperature: 0                 # 结构化解析要确定性
```
> base-url 写死默认值，但 `${OPENAI_API_KEY}` 缺失时让 ChatClient 调用失败报错（不静默）。

### 2. 核心 Service：AiBookkeepingService

职责：构造 prompt（含科目上下文）→ 调 ChatClient → 解析返回 JSON → 校验科目存在性 → 兜底。

关键点：
- **科目上下文**：只取**本家庭**的末级科目（用 AccountMatcher 区分账户/费用），格式化成 code→名称列表喂给 AI，让它返回 code。
- **结构化输出**：用 spring-ai 的 `BeanOutputConverter` + system prompt 约束 AI 返回固定 JSON schema（type/bizAccountCode/payAccountCode/amt/remark）。
- **校验**：AI 返回的 code 必须在本家庭科目中存在；不存在→兜底。
- **兜底规则**：
  - 支出解析不出业务科目 → `004010` 其他费用
  - 收入解析不出业务科目 → `003003` 其他收入
  - 收付款账户解析不出 → 用该家庭的默认账户（优先取 `001001` 现金，没有则取任意资产末级）
- **type 推导**：收入/支出/转账。转账=两个都是账户科目（如微信→银行卡）。

### 3. 接口：AccountResource 新增

```java
@PostMapping("/account/ai-parse")
public AiParseResultVO parseByAi(@RequestBody AiParseRequest req) // {text: "..."}
```
返回 AiParseResultVO（含解析出的 creditAccount/debitAccount + 名称 + 金额 + 备注 + 可信度提示），前端确认后直接拿去调 `/acct/account`。

### 4. 复用而非重写
- 记账落库 100% 复用 `AccountResource.acct()`，不改。
- 科目查询复用 `AccountClsService`（批2 已按 tenantId 隔离）。
- 账户识别复用 `AccountMatcher`（批4 已建）。

---

## 前端实现（小程序 z-ui-uniapp）

### 1. 新增页面 pages/acct/AiBookkeeping.vue
- 顶部：文本输入框（"说点什么...，如：午饭花了35"）+ "解析"按钮
- 解析后：卡片展示预览（类型 + 业务科目 + 金额 + 账户 + 备注），每项可点改（弹科目 picker 复用 AddAccount 的 columns）
- 底部："确认记账"按钮 → 调 saveApi（复用 account/index.ts 的 saveApi）

### 2. 入口
work/index.vue 工作台加一个"AI记账"按钮，跳转 AiBookkeeping 页面。
pages.json 注册新页面。

### 3. API 封装
api/acct/account/index.ts 加 `aiParseApi(text)` → POST /acct/account/ai-parse。

---

## 文档

新建 `~/workspace/项目管理/开发文档/家庭记账软件/04_开发实现/AI记账/01_AI记账-架构需求.org`，内容对标其他模块的"架构需求"文档格式（业务需求/用户故事/数据流/接口/技术实现/非功能）。涵盖：
- 业务需求与用户故事
- 解析流程时序
- prompt 设计（系统 prompt + 科目上下文注入 + JSON schema）
- 兜底与降级策略
- 接口契约（ai-parse 请求/响应）
- 配置说明（环境变量 OPENAI_API_KEY）
- 复式记账如何被 AI 透明化（不暴露借贷给用户）

---

## 实施顺序

1. **后端地基**：pom + yml 配置 + AiBookkeepingService + 接口 + VO
2. **后端联调**：本地起服务，curl 测 /account/ai-parse（需设 OPENAI_API_KEY）
3. **前端页面**：AiBookkeeping.vue + 工作台入口 + pages.json
4. **文档**：架构需求 .org

---

## 风险与边界
- **网络/费用**：每次解析一次 OpenAI 调用，记一笔≈一次 API。需控制 prompt 长度（科目列表可能大，只喂末级 + 启用的）。
- **无 key 环境**：OPENAI_API_KEY 未设时，ai-parse 接口返回明确错误提示，不崩。
- **中文科目匹配**：AI 返回的是 code 不是名称，避免中英文/别名歧义。
- **多语言**：先只做中文 prompt。
