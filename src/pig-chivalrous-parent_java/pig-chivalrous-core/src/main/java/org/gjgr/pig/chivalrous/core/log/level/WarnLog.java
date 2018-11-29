package org.gjgr.pig.chivalrous.core.log.level;

/**
 * WARN级别日志接口
 *
 * @author Looly
 */
public interface WarnLog {
    /**
     * @return WARN 等级是否开启
     */
    boolean isWarnEnabled();

    /**
     * 打印 WARN 等级的日志
     *
     * @param t 错误对象
     */
    void warn(Throwable t);

    /**
     * 打印 WARN 等级的日志
     *
     * @param format 消息模板
     * @param arguments 参数
     */
    void warn(String format, Object... arguments);

    /**
     * 打印 WARN 等级的日志
     *
     * @param t 错误对象
     * @param format 消息模板
     * @param arguments 参数
     */
    void warn(Throwable t, String format, Object... arguments);
}
