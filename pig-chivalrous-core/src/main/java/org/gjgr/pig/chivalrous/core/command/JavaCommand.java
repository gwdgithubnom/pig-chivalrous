package org.gjgr.pig.chivalrous.core.command;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

public abstract class JavaCommand implements Runnable, CommandLine.IExitCodeExceptionMapper {

    public static CommandLine commandLine = new CommandLine(SimpleJavaCommand.class);
    public static HashMap<String, CommandLine> commandLines = new HashMap<>();

    static {
        commandLines.put(commandLine.getCommandName(), commandLine);
    }

    @CommandLine.Option(names = {"-d", "--debug"}, required = false, defaultValue = "false", description = "debug status")
    String debug;

    @CommandLine.Option(names = {"-i", "--input"}, paramLabel = "INPUT", description = "spark input path")
    String input;

    @CommandLine.Option(names = {"-o", "--output"}, paramLabel = "OUTPUT", description = "spark output path")
    String output;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec = null;

    private final Logger logger = LoggerFactory.getLogger(JavaCommand.class);

    public JavaCommand() {
    }

    protected CommandLine.Model.CommandSpec getSpec(){
        return spec;
    }

    public Map<String, Class<?>> commandLoader(String packages) {
        Map<String, Class<?>> classMap = null;
        if (packages != null) {
            classMap = scanCommand(packages, CommandLine.Command.class);
        } else {
            logger.info("used default creat instance");
            classMap = new HashMap<>();
        }
        classMap.putAll(loadCommand());
        return classMap;
    }

    public Map<String, Class<?>> loadCommand() {
        Map<String, Class<?>> clazzMap = new HashMap<>();
        Reflections reflections = new Reflections("com.xiaomi.beelab");
        Set<Class<? extends JavaCommand>> sets = reflections.getSubTypesOf(JavaCommand.class);
        for (Class<?> clazz : sets) {
            CommandLine.Command command = clazz.getAnnotation(CommandLine.Command.class);
            if (command != null && command.subcommands().length > 0) {
                clazzMap.put(command.name(), clazz);
                if (StringCommand.join(Arrays.asList(command.description()), " ").contains("auto")) {
                    commandLines.put(command.name(), new CommandLine(clazz));
                }
            }
        }
        return clazzMap;
    }

    public <A extends Annotation> Map<String, Class<?>> scanCommand(String packages, Class<A> theClass) {
        Map<String, Class<?>> clazzMap = new HashMap<>();
        Reflections reflections = new Reflections(packages);
        Set<Class<?>> sets = reflections.getTypesAnnotatedWith(theClass);
        for (Class<?> clazz : sets) {
            A command = clazz.getAnnotation(theClass);
            if (command.annotationType() == CommandLine.Command.class) {
                CommandLine.Command command1 = (CommandLine.Command) command;
                if (command1.subcommands().length > 0) {
                    clazzMap.put(command1.name(), clazz);
                } else {
                    logger.info("found method Annotation class {}, Annotation command name {}", theClass, command1.name());
                }
            } else {
                logger.info("need to do");
            }
        }
        return clazzMap;
    }

    public Map<String, Class<?>> scanCommand(String packages) {
        Map<String, Class<?>> clazzMap = new HashMap<>();
        Reflections reflections = new Reflections(packages);
        Set<Class<?>> sets = reflections.getTypesAnnotatedWith(CommandLine.Command.class);
        for (Class<?> clazz : sets) {
            CommandLine.Command command = clazz.getAnnotation(CommandLine.Command.class);
            clazzMap.put(command.name(), clazz);
        }
        Set<String> subs = new HashSet<>();
        for (Map.Entry<String, Class<?>> entry : clazzMap.entrySet()) {
            CommandLine.Command command = entry.getValue().getAnnotation(CommandLine.Command.class);
            for (Class<?> sub : command.subcommands()) {
                CommandLine.Command subCommand = sub.getAnnotation(CommandLine.Command.class);
                subs.add(subCommand.name());
            }
        }
        for (String sub : subs) {
            clazzMap.remove(sub);
        }
        return clazzMap;
    }

    public void launch(String packages, String[] args) {
        int code = -1;
        String[] inputArgs = args.clone();
        try {
            Map<String, Class<?>> map = commandLoader(packages);
            if (commandLines.containsKey(inputArgs[0])) {
                commandLine = commandLines.get(inputArgs[0]);
                List<String> list = new ArrayList<String>(Arrays.asList(args));
                list.remove(inputArgs[0]);
                inputArgs = list.toArray(new String[0]);
            } else {
                if (!map.containsKey(inputArgs[0])) {
                    commandLine.execute(args);
                    System.exit(101);
                } else {
                    commandLine = new CommandLine(map.get(inputArgs[0]));
                    List<String> list = new ArrayList<String>(Arrays.asList(args));
                    list.remove(inputArgs[0]);
                    inputArgs = list.toArray(new String[0]);
                }
            }
            code = commandLine.execute(inputArgs);
        } catch (Error e) {
            logger.error("throw a Error {}", e);
            code = -1024;
        } catch (Exception e) {
            logger.error("throw a Exception {}", e);
            code = -2048;
        } finally {
            if (code == -1) {
                logger.error("application cli exit code: {}", code);
                throw new RuntimeException("task error exception");
            } else if (code != 0) {
                logger.error("found the task faild {}", commandLine.getErr());
                throw new RuntimeException("task error code not 0 exception");
            } else {
                logger.info("task work finished code: {}", code);
            }
        }

    }

    public void launch(String[] args) {
        CommandPackageCovert convert = null;
        try {
            convert = CommandLine.populateCommand(new CommandPackageCovert(), args);
        } catch (Exception e) {
            convert = new CommandPackageCovert();
        } finally {
            if (convert.pagckages != null) {
                for (String s : convert.pagckages) {
                    launch(s, convert.args);
                }
            } else {
                launch(null, args);
            }
        }
    }

    @Override
    public int getExitCode(Throwable throwable) {
        if (throwable instanceof Exception) {
            return -1;
        }
        return 1;
    }

}

