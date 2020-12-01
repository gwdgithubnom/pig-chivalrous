package org.gjgr.pig.chivalrous.core.linux;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShellConfig implements Serializable {
    protected int sleepInterval = 2000;
    protected String workPath;
    protected String taskId;
    protected String shellContent;
    /**
     * shell超时时间
     */
    protected long timeOutSeconds = 24L * 60L * 60L;
    private int exitCode = 0;
    private boolean isForceKill = false;
    private boolean isFinish = false;
    private long startRunTimeSeconds;
    private long endTimeSeconds;
    private String script;
    private String output;

}
