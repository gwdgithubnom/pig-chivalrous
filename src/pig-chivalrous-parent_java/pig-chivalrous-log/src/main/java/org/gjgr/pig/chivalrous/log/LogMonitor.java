package org.gjgr.pig.chivalrous.log;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;

public interface LogMonitor {
    public void proccess(LogEvent event);

    public Marker getMarker();
}
