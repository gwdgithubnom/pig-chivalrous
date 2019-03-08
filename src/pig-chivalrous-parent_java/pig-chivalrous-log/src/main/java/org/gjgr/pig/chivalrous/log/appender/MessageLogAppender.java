package org.gjgr.pig.chivalrous.log.appender;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.message.Message;

/**
 * 自定义Appender，继承 AbstractAppender 只需要覆盖自已想要的方法即可<br>
 * 类上面的注解是用来设置配置文件中的标签。
 */
@Plugin(name = "MessageLog",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE,
        printObject = true)
public class MessageLogAppender extends AbstractAppender {
    private static Map<String, MessageLogAppender> instanceMap = new HashMap<>();

    private static Integer MAX_CACHED_SIZE = 5000;

    private BlockingQueue<LogEvent> queue = new LinkedBlockingQueue<>();

    private Integer maxCachedSize;

    private MessageLogAppender(String name, Filter filter, Layout<? extends Serializable> layout,
            Integer maxCachedMarker, Integer maxCachedSize) {
        this(name, filter, layout, true, maxCachedSize);
    }

    public MessageLogAppender(String name, Filter filter, Layout<? extends Serializable> layout,
            boolean ignoreExceptions, Integer maxCachedSize) {
        super(name, filter, layout, ignoreExceptions);
        if (maxCachedSize == null || maxCachedSize <= 0) {
            this.maxCachedSize = MAX_CACHED_SIZE;
        } else {
            this.maxCachedSize = maxCachedSize;
        }
    }

    // 下面这个方法可以接收配置文件中的参数信息
    @PluginFactory
    public static MessageLogAppender createAppender(
            @PluginAttribute("name") @Required String name,
            @PluginElement("Filter") final Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
            @PluginAttribute("maxSize") Integer maxCachedSize) {
        if (name == null) {
            LOGGER.error("No name provided for MyCustomAppenderImpl");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        LOGGER.debug("add filter {} for appender {}", filter, name);
        if (!instanceMap.containsKey(name)) {
            synchronized (instanceMap) {
                if (!instanceMap.containsKey(name)) {
                    MessageLogAppender appender =
                            new MessageLogAppender(name, filter, layout, ignoreExceptions, maxCachedSize);
                    instanceMap.put(name, appender);
                }
            }
        }
        return instanceMap.get(name);
    }

    public static MessageLogAppender getInstance(String name) {
        return instanceMap.get(name);
    }

    @Override
    protected Object clone() {
        return this;
    }

    public LogEvent pop(Long time, TimeUnit timeUnit) throws InterruptedException {
        return queue.poll(time, timeUnit);
    }

    public String popMessage(String marker, Long time, TimeUnit timeUnit) throws InterruptedException {
        return pop(time, timeUnit).getMessage().getFormattedMessage();
    }

    @Override
    public void append(LogEvent event) {
        System.err.println("############appener##################");
        if (event == null) {
            return;
        }
        Message message = event.getMessage();
        LogEvent eventImmutable = event.toImmutable();
        if (eventImmutable.getMarker() == null) {
            LOGGER.debug("logevent {} marker is null", event);
            return;
        }
        // must put the immutable event in order to avoid thread unsafe change
        while (queue.size() > maxCachedSize) {
            LogEvent eventPop = queue.poll();
            System.err.printf("pop log data %s%n", queue.poll().getMessage().getFormattedMessage());
            LOGGER.debug("pop log data {}", queue.poll());
        }
        queue.offer(eventImmutable.toImmutable());
    }
}
