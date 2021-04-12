package org.gjgr.pig.chivalrous.core.command;

import java.util.List;
import picocli.CommandLine;

public class CommandPackageCovert {

    @CommandLine.Option(names = "--packages", required = false, description = "need to scan")
    List<String> pagckages;

    @CommandLine.Parameters(/* type = File.class, */ description = "other request parm")
    String[] args; // picocli infers type from the generic type
}
