package org.gjgr.pig.chivalrous.db.dialect.impl;

import org.gjgr.pig.chivalrous.db.dialect.DialectName;
import org.gjgr.pig.chivalrous.db.sql.Wrapper;

/**
 * Postgree方言
 *
 * @author loolly
 */
public class PostgresqlDialect extends AnsiSqlDialect {
    public PostgresqlDialect() {
        wrapper = new Wrapper('"');
    }

    @Override
    public DialectName dialectName() {
        return DialectName.POSTGREESQL;
    }
}
