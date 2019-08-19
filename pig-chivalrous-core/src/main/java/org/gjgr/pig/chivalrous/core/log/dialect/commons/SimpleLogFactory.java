package org.gjgr.pig.chivalrous.core.log.dialect.commons;

import org.gjgr.pig.chivalrous.core.log.Log;
import org.gjgr.pig.chivalrous.core.log.LogFactory;

/**
 * Apache Commons Logging
 *
 * @author Looly
 */
public class SimpleLogFactory extends LogFactory {

    public SimpleLogFactory() {
        super("Apache Common Logging");
        checkLogExist(LogFactory.class);
    }

    @Override
    public Log getLog(String name) {
        try {
            return new SimpleLog4jLog(name);
        } catch (Exception e) {
            return new SimpleLog(name);
        }
    }

    @Override
    public Log getLog(Class<?> clazz) {
        try {
            return new SimpleLog4jLog(clazz);
        } catch (Exception e) {
            return new SimpleLog(clazz);
        }
    }

}
