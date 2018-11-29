package org.gjgr.pig.chivalrous.db;

import org.gjgr.pig.chivalrous.core.lang.Console;
import org.gjgr.pig.chivalrous.db.ds.DSFactory;
import org.gjgr.pig.chivalrous.db.ds.c3p0.C3p0DSFactory;
import org.gjgr.pig.chivalrous.db.ds.druid.DruidDSFactory;
import org.gjgr.pig.chivalrous.db.ds.hikari.HikariDSFactory;
import org.gjgr.pig.chivalrous.db.ds.tomcat.TomcatDSFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据源单元测试
 *
 * @author Looly
 */
public class DsTest {

    @Test
    public void DefaultDsTest() throws SQLException {
        DataSource ds = DSFactory.get();
        SqlRunner runner = SqlRunner.create(ds);
        List<Entity> all = runner.findAll("user");
        for (Entity entity : all) {
            Console.log(entity);
        }
    }

    @Test
    public void HikariDsTest() throws SQLException {
        DSFactory.setCurrentDSFactory(new HikariDSFactory());
        DataSource ds = DSFactory.get();
        SqlRunner runner = SqlRunner.create(ds);
        List<Entity> all = runner.findAll("user");
        for (Entity entity : all) {
            Console.log(entity);
        }
    }

    @Test
    public void DruidDsTest() throws SQLException {
        DSFactory.setCurrentDSFactory(new DruidDSFactory());
        DataSource ds = DSFactory.get();
        SqlRunner runner = SqlRunner.create(ds);
        List<Entity> all = runner.findAll("user");
        for (Entity entity : all) {
            Console.log(entity);
        }
    }

    @Test
    public void TomcatDsTest() throws SQLException {
        DSFactory.setCurrentDSFactory(new TomcatDSFactory());
        DataSource ds = DSFactory.get();
        SqlRunner runner = SqlRunner.create(ds);
        List<Entity> all = runner.findAll("user");
        for (Entity entity : all) {
            Console.log(entity);
        }
    }

    /*@Test
    public void DbcpDsTest() throws SQLException {
        DSFactory.setCurrentDSFactory(new DbcpDSFactory());
        DataSource ds = DSFactory.get();
        SqlRunner runner = SqlRunner.create(ds);
        List<Entity> all = runner.findAll("user");
        for (Entity entity : all) {
            Console.log(entity);
        }
    }*/

    @Test
    public void C3p0DsTest() throws SQLException {
        DSFactory.setCurrentDSFactory(new C3p0DSFactory());
        DataSource ds = DSFactory.get();
        SqlRunner runner = SqlRunner.create(ds);
        List<Entity> all = runner.findAll("user");
        for (Entity entity : all) {
            Console.log(entity);
        }
    }
}
