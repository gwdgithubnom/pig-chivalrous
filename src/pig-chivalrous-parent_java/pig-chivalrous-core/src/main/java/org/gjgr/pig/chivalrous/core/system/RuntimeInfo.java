package org.gjgr.pig.chivalrous.core.system;

import org.gjgr.pig.chivalrous.core.io.FileCommand;

public class RuntimeInfo {

    private Runtime currentRuntime = Runtime.getRuntime();

    /**
     * 获得运行时对象
     *
     * @return {@link Runtime}
     */
    public final Runtime getRuntime() {
        return currentRuntime;
    }

    /**
     * 获得JVM最大可用内存
     *
     * @return 最大可用内存
     */
    public final long getMaxMemory() {
        return currentRuntime.maxMemory();
    }

    /**
     * 获得JVM已分配内存
     *
     * @return 已分配内存
     */
    public final long getTotalMemory() {
        return currentRuntime.totalMemory();
    }

    /**
     * 获得JVM已分配内存中的剩余空间
     *
     * @return 已分配内存中的剩余空间
     */
    public final long getFreeMemory() {
        return currentRuntime.freeMemory();
    }

    /**
     * 获得JVM最大可用内存
     *
     * @return 最大可用内存
     */
    public final long getUsableMemory() {
        return currentRuntime.maxMemory() - currentRuntime.totalMemory() + currentRuntime.freeMemory();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        JavaSystemCommand.append(builder, "Max Memory:    ", FileCommand.readableFileSize(getMaxMemory()));
        JavaSystemCommand.append(builder, "Total Memory:     ", FileCommand.readableFileSize(getTotalMemory()));
        JavaSystemCommand.append(builder, "Free Memory:     ", FileCommand.readableFileSize(getFreeMemory()));
        JavaSystemCommand.append(builder, "Usable Memory:     ", FileCommand.readableFileSize(getUsableMemory()));

        return builder.toString();
    }
}
