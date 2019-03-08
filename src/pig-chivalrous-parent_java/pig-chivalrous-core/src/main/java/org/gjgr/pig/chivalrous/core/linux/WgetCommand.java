package org.gjgr.pig.chivalrous.core.linux;

/**
 * @Author gwd
 * @Time 11-29-2018 Thursday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class WgetCommand extends LinuxShellCommand {

    private String toDiretory = "-P ";
    private String toFile = "-O ";
    private String targetFile;

    public WgetCommand() {
        super("wget");
    }

    public WgetCommand(String targetFile) {
        super("wget " + targetFile);
        this.targetFile = targetFile;
    }

    public WgetCommand(String command, String...params) {
        super("wget ", params);
    }

    public WgetCommand toDiretory(String diretory) {
        this.toDiretory = this.toDiretory + diretory;
        return this;
    }

    public WgetCommand toFile(String toFile) {
        this.toFile = this.toFile + toFile;
        return this;
    }

    public WgetCommand wget(String targetFile, String filename) {
        this.targetFile = targetFile;
        this.toFile = toFile + filename;
        return this;
    }

    @Override
    public String toString() {
        String string;
        if (targetFile == null) {
            return null;
        }
        if (toDiretory.equals("-P ") && toFile.equals("-O ")) {
            logger.warn("did not define the target directory, command will use default directory. ");
            string = command + " " + targetFile;
        } else {
            String param = null;
            if (!toFile.equals("-O ")) {
                param = param + " " + toFile;
            }
            if (!toDiretory.equals("-P ")) {
                param = param + " " + toDiretory;
            }
            string = command + " " + targetFile + param;
        }
        return string;
    }

}
