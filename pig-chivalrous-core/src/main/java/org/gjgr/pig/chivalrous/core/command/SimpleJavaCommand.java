package org.gjgr.pig.chivalrous.core.command;

import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "java_app", version = "0.1.0", subcommands = {CommandLine.HelpCommand.class}, mixinStandardHelpOptions = true,
    description = "default java runner command")
public class SimpleJavaCommand extends JavaCommand {

    private static final Logger logger = LoggerFactory.getLogger(SimpleJavaCommand.class);

    @CommandLine.Command(name = "run", description = "Example reuse with @Mixin annotation.")
    @Override
    public void run() {
        logger.error("could not parse the java command \"{}\"", StringCommand.join(spec.args(), " "));
        throw new CommandLine.ParameterException(spec.commandLine(), "Specify a subcommand");
    }

}
