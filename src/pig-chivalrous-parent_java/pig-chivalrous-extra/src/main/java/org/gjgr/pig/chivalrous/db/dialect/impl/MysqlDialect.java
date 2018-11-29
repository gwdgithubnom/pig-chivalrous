package org.gjgr.pig.chivalrous.db.dialect.impl;

import org.gjgr.pig.chivalrous.core.util.StrUtil;
import org.gjgr.pig.chivalrous.db.DbRuntimeException;
import org.gjgr.pig.chivalrous.db.DbUtil;
import org.gjgr.pig.chivalrous.db.Page;
import org.gjgr.pig.chivalrous.db.dialect.DialectName;
import org.gjgr.pig.chivalrous.db.sql.LogicalOperator;
import org.gjgr.pig.chivalrous.db.sql.Order;
import org.gjgr.pig.chivalrous.db.sql.Query;
import org.gjgr.pig.chivalrous.db.sql.SqlBuilder;
import org.gjgr.pig.chivalrous.db.sql.Wrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * MySQL方言
 *
 * @author loolly
 */
public class MysqlDialect extends AnsiSqlDialect {

    public MysqlDialect() {
        wrapper = new Wrapper('`');
    }

    @Override
    public PreparedStatement psForPage(Connection conn, Query query) throws SQLException {
        //验证
        if (query == null || StrUtil.hasBlank(query.getTableNames())) {
            throw new DbRuntimeException("Table name is null !");
        }

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

        find.append(" LIMIT ").append(page.getStartPosition()).append(", ").append(page.getNumPerPage());

        final PreparedStatement ps = conn.prepareStatement(find.build());
        DbUtil.fillParams(ps, find.getParamValueArray());
        return ps;
    }

    @Override
    public DialectName dialectName() {
        return DialectName.MYSQL;
    }
}
