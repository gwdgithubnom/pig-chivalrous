package org.gjgr.pig.chivalrous.spark.command;

import org.gjgr.pig.chivalrous.core.command.JavaCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "spark_app", version = "0.1.0", subcommands = {CommandLine.HelpCommand.class},
    description = "auto default boost spark command cli")
public class SimpleSparkCommand extends JavaCommand {

    private static final Logger logger = LoggerFactory.getLogger(SimpleSparkCommand.class);

    @CommandLine.Command(name = "run", description = "Example reuse with @Mixin annotation.")
    @Override
    public void run() {
        logger.error("could not parse the spark command \"{}\"", StringCommand.join(getSpec().args(), " "));
        throw new CommandLine.ParameterException(getSpec().commandLine(), "Specify a subcommand");
    }
}
