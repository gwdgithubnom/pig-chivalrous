package org.gjgr.pig.chivalrous.db.dialect.impl;

import org.gjgr.pig.chivalrous.core.util.ArrayUtil;
import org.gjgr.pig.chivalrous.core.util.CollectionUtil;
import org.gjgr.pig.chivalrous.db.DbUtil;
import org.gjgr.pig.chivalrous.db.Entity;
import org.gjgr.pig.chivalrous.db.Page;
import org.gjgr.pig.chivalrous.db.dialect.Dialect;
import org.gjgr.pig.chivalrous.db.dialect.DialectName;
import org.gjgr.pig.chivalrous.db.sql.Condition;
import org.gjgr.pig.chivalrous.db.sql.LogicalOperator;
import org.gjgr.pig.chivalrous.db.sql.Order;
import org.gjgr.pig.chivalrous.db.sql.Query;
import org.gjgr.pig.chivalrous.db.sql.SqlBuilder;
import org.gjgr.pig.chivalrous.db.sql.Wrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ANSI SQL 方言
 *
 * @author loolly
 */
public class AnsiSqlDialect implements Dialect {

    protected Wrapper wrapper = new Wrapper();

    @Override
    public Wrapper getWrapper() {
        return this.wrapper;
    }

    @Override
    public void setWrapper(Wrapper wrapper) {
        this.wrapper = wrapper;
    }


    @Override
    public PreparedStatement psForInsert(Connection conn, Entity entity) throws SQLException {
        final SqlBuilder insert = SqlBuilder.create(wrapper).insert(entity, this.dialectName());

        final PreparedStatement ps = conn.prepareStatement(insert.build(), Statement.RETURN_GENERATED_KEYS);
        DbUtil.fillParams(ps, insert.getParamValues());
        return ps;
    }

    @Override
    public PreparedStatement psForDelete(Connection conn, Query query) throws SQLException {
        if (null == query) {
            throw new NullPointerException("query is null !");
        }

        Condition[] where = query.getWhere();
        if (ArrayUtil.isEmpty(where)) {
            // 对于无条件的删除语句直接抛出异常禁止，防止误删除
            throw new SQLException("No 'WHERE' condition, we can't prepared statement for delete everything.");
        }
        final SqlBuilder delete = SqlBuilder.create(wrapper)
                .delete(query.getFirstTableName())
                .where(LogicalOperator.AND, where);

        final PreparedStatement ps = conn.prepareStatement(delete.build());
        DbUtil.fillParams(ps, delete.getParamValues());
        return ps;
    }

    @Override
    public PreparedStatement psForUpdate(Connection conn, Entity entity, Query query) throws SQLException {
        if (null == query) {
            throw new NullPointerException("query is null !");
        }

        Condition[] where = query.getWhere();
        if (ArrayUtil.isEmpty(where)) {
            // 对于无条件的删除语句直接抛出异常禁止，防止误删除
            throw new SQLException("No 'WHERE' condition, we can't prepared statement for update everything.");
        }

        final SqlBuilder update = SqlBuilder.create(wrapper)
                .update(entity)
                .where(LogicalOperator.AND, where);

        final PreparedStatement ps = conn.prepareStatement(update.build());
        DbUtil.fillParams(ps, update.getParamValues());
        return ps;
    }

    @Override
    public PreparedStatement psForFind(Connection conn, Query query) throws SQLException {
        //验证
        if (null == query) {
            throw new NullPointerException("query is null !");
        }

        final SqlBuilder find = SqlBuilder.create(wrapper).query(query);
        final PreparedStatement ps = conn.prepareStatement(find.build());
        DbUtil.fillParams(ps, find.getParamValues());
        return ps;
    }

    @Override
    public PreparedStatement psForPage(Connection conn, Query query) throws SQLException {
        final SqlBuilder find = SqlBuilder.create(wrapper)
                .select(query.getFields())
                .from(query.getTableNames())
                .where(LogicalOperator.AND, query.getWhere());

        final Page page = query.getPage();
        if (null != page) {
            final Order[] orders = page.getOrders();
            if (null != orders) {
                find.orderBy(orders);
            }
        }

        //limit  A  offset  B 表示：A就是你需要多少行，B就是查询的起点位置。
        find.append(" limit ").append(page.getNumPerPage()).append(" offset ").append(page.getStartPosition());

        final PreparedStatement ps = conn.prepareStatement(find.build());
        DbUtil.fillParams(ps, find.getParamValues());
        return ps;
    }

    @Override
    public PreparedStatement psForCount(Connection conn, Query query) throws SQLException {
        query.setFields(CollectionUtil.newArrayList("count(1)"));
        return psForFind(conn, query);
    }

    @Override
    public DialectName dialectName() {
        return DialectName.ANSI;
    }

    // ---------------------------------------------------------------------------- Protected method start
    // ---------------------------------------------------------------------------- Protected method end

}
