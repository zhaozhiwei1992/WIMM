package com.z.module.system.aop;

import cn.hutool.core.util.StrUtil;
import com.z.framework.common.domain.TenantContext;
import com.z.framework.security.util.SecurityUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.Values;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * @author zhaozhiwei
 * @version V1.0
 * @Title: DataRightInterceptor
 * @Package com/example/aop/DataRightInterceptor.java
 * @Description: 通过实现org.hibernate.resource.jdbc.spi.StatementInspector
 * 动态调整sql, 扩展特殊sql相关功能
 * @date 2022/5/14 下午10:22
 */
public class CustomStatementInspector implements StatementInspector {

    private static final Logger log = LoggerFactory.getLogger(CustomStatementInspector.class);

    public static final ThreadLocal<Map<String, String>> replaceTable = new ThreadLocal<>();

    /**
     * 重写StatementInspector的inspect接口，
     * 参数为hibernate处理后的原始SQL，返回值为我们修改后的SQL
     *
     * @param sql
     * @return
     */
    @Override
    public String inspect(String sql) {
        try {
            // 1. 是否登录状态, 未登录不允许查看数据
            String currentLoginName = SecurityUtils.getCurrentLoginName();
            if (currentLoginName.equals("anonymousUser")) {
                // 可以显示未登录不允许查看任何数据 1<>1
                log.info("当前未登录, 看着办吧");
                return sql;
            }

            // 2. 登录状态下加载权限, 根据类型增加不同sql过滤
            log.info("原SQL：{}", sql);
            Statements statements = CCJSqlParserUtil.parseStatements(sql);
            StringBuilder sqlStringBuilder = new StringBuilder();
            int i = 0;
            for (Statement statement : statements) {
                if (null != statement) {
                    if (i++ > 0) {
                        sqlStringBuilder.append(';');
                    }
                    sqlStringBuilder.append(this.doSqlParser(statement));
                }
            }
            String newSql = sqlStringBuilder.toString();
            log.info("处理后SQL：{}", newSql);
            return newSql;
        } catch (Exception e) {
            log.error("组织筛选解析失败，解析SQL异常", e);
        }
        return null;
    }

    private String doSqlParser(Statement statement) {
        if (statement instanceof Select) {
            this.doSelectParser(((Select) statement).getPlainSelect());
        } else if (statement instanceof Delete) {
            this.doDeleteParser((Delete) statement);
        } else if (statement instanceof Insert) {
//            this.doInsertParser((Insert) statement);
        }
        return statement.toString();
    }

    private void doInsertParser(Insert insert) {

        // 租户处理, 删除字段直接写入值
        String tenantId = SecurityUtils.getTenantId();

        ExpressionList<Column> columns = insert.getColumns();
        ExpressionList<Expression> values = (ExpressionList<Expression>) insert.getValues().getExpressions();
        // 获取tenantId的下标
        int index = -1;
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            if("tenant_id".equals(column.getColumnName())){
                index = i;
            }
        }
        columns.remove(index);
        values.remove(index);

        columns.add(new Column("tenant_id"));
        values.add(new StringValue("99"));

        System.out.println(insert.toString());
    }

    /**
     * @param delete :
     * @Description: 删除时做一些操作，控制误删除
     * @author: zhaozhiwei
     * @data: 2024/10/13-16:10
     * @return: void
     */

    private void doDeleteParser(Delete delete) {
        // 如何根据id判断数据是否能删除
        System.out.println("delete");
    }

    public void doSelectParser(PlainSelect selectBody) {
        processPlainSelect(selectBody);
    }

    /**
     * @param plainSelect :
     * @data: 2022/5/14-下午11:20
     * @User: zhaozhiwei
     * @method: processPlainSelect
     * @return: void
     * @Description: 在这里扩展各种的条件
     * 1. 数据权限扩展
     * 2. 动态表替换, 支持相同domain,不同表结构
     */
    protected void processPlainSelect(PlainSelect plainSelect) {
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table) {
            Table fromTable = (Table) fromItem;

            // 换表
            this.replaceTable(fromTable);
            String tenantId = TenantContext.getTenantId();

            // 多租户，增加tenantId过滤
            String currentLoginName = SecurityUtils.getCurrentLoginName();
            if (!"admin".equals(currentLoginName)) {
                // TODO 根据传入个人或者家庭, 分别拼接创建人或者租户条件
            }

            //TODO  这里可扩展
        }
    }

    private void replaceTable(Table fromTable) {
        Map<String, String> map = replaceTable.get();

        // 必要时才会去替换
        if (!Objects.isNull(map)) {
            String oldName = map.get("oldName");
            String newName = map.get("newName");

            if (!StrUtil.isEmpty(oldName) && !StrUtil.isEmpty(newName)) {
                final String name = fromTable.getName();
                if (name.startsWith(oldName)) {
                    // 如果表名匹配，则将旧表换成新表
                    fromTable.setName(newName);
                    log.info("表替换,将 {} 替换为 {}", oldName, newName);
                }
            }
        }
    }
}
