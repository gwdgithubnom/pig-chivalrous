package org.gjgr.pig.chivalrous.log;

import org.slf4j.event.Level;

public class LogCommandTest {
    public static void main(String[] args) {
        LogCommand.ignoreReflectionLog(LogCommandTest.class, Level.WARN);
    }
}
