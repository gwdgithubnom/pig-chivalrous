package org.gjgr.pig.chivalrous.core.linux;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @Author gwd
 * @Time 11-29-2018 Thursday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public abstract class LinuxCommand implements Serializable {

    protected Logger logger = LoggerFactory.getLogger(LinuxCommand.class);
    protected String command;
    protected StringBuffer more = new StringBuffer();
    private String help;

    public static WgetCommand wget() {
        return new WgetCommand();
    }

    public static LinuxShellCommand base(String command) {
        return new LinuxShellCommand(command);
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public LinuxCommand append(String param) {
        more.append(param);
        return this;
    }

    public boolean command() {
        command = toString();
        if (command == null) {
            logger.warn("could not init command");
            return false;
        } else {
            String s;
            Process p;
            try {
                p = Runtime.getRuntime().exec(command);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                while ((s = br.readLine()) != null) {
                    System.out.println(s);
                }
                p.waitFor();
                logger.debug("run command:" + command + ". exitValue:" + p.exitValue() + "");
                p.destroy();
            } catch (Exception e) {
                logger.error("Exception: " + e.getMessage());
            }
            return true;
        }
    }

    @Override
    public String toString() {
        return null;
    }
}
