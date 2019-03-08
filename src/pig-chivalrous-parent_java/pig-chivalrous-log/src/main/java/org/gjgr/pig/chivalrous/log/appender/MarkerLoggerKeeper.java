package org.gjgr.pig.chivalrous.log.appender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.OutputStreamAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.MarkerFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Marker;

public class MarkerLoggerKeeper {

    private Set<String> markerSet = new HashSet<>();

    private String loggerName = LoggerConfig.ROOT;

    private MarkerLoggerKeeper() {

    }

    public static MarkerLoggerKeeper getInstance() {
        return new MarkerLoggerKeeper();
    }

    public void start() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        List<AppenderRef> appenderRefs = new ArrayList<>();
        Configuration configuration = context.getConfiguration();
        LoggerConfig oldLogger = configuration.getLoggerConfig(loggerName);
        if (oldLogger != null) {
            appenderRefs.addAll(oldLogger.getAppenderRefs());
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(new File("/tmp/test.log"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamAppender.Builder appenderBuilder = OutputStreamAppender.newBuilder();
        appenderBuilder.setTarget(out).setName("OutputStreamAppender:" + hashCode());
        appenderBuilder.setLayout(PatternLayout.createDefaultLayout(configuration));
        OutputStreamAppender appender = appenderBuilder.build();
        List<MarkerFilter> filters = new ArrayList<>();
        for (String marker : markerSet) {
            appender.addFilter(MarkerFilter.createFilter(marker, Filter.Result.DENY, Filter.Result.NEUTRAL));
        }
        appender.addFilter(MarkerFilter.createFilter("NONE_REG", Filter.Result.ACCEPT, Filter.Result.ACCEPT));
        appender.start();
        configuration.addAppender(appender);
        AppenderRef appenderRef = AppenderRef.createAppenderRef(appender.getName(), null, null);
        appenderRefs.add(appenderRef);
        LoggerConfig logger = LoggerConfig.createLogger(false, Level.INFO, loggerName, null,
                appenderRefs.toArray(new AppenderRef[0]), null, configuration, null);
        // logger.addAppender(appender, null, null);
        configuration.addLogger(loggerName, logger);
        context.updateLoggers();
    }

    public void regMarker(String marker) {
        markerSet.add(marker);
    }

    public void regMarker(Marker marker) {
        markerSet.add(marker.getName());
    }
}
