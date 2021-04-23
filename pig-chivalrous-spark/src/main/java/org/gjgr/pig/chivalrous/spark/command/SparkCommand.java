package org.gjgr.pig.chivalrous.spark.command;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Consumer;
import org.apache.spark.SparkConf;
import org.gjgr.pig.chivalrous.core.command.JavaCommand;
import org.gjgr.pig.chivalrous.core.io.IoCommand;
import org.gjgr.pig.chivalrous.core.io.file.FileCommand;
import org.gjgr.pig.chivalrous.core.io.resource.LocationCommand;
import picocli.CommandLine;

abstract class SparkCommand extends JavaCommand {

    public static Properties theProperties = initProperties();
    public static SparkConf theSparkConf = initSparkConf();

    static {
        commandLine = new CommandLine(SimpleSparkCommand.class);
    }

    public static SparkConf initSparkConf() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(commandLine.getCommandName());
        sparkConf.setIfMissing("spark.master", "local[4]");
        theProperties.stringPropertyNames().forEach(new Consumer<String>() {
            @Override
            public void accept(String key) {
                sparkConf.set(key, theProperties.get(key).toString());
            }
        });
        return sparkConf;
    }

    public static Properties initProperties() {
        String filePath = LocationCommand.path("app.properties");
        Properties properties = new Properties();
        if (filePath != null) {
            try {
                InputStream inputStream = JavaCommand.class.getClassLoader().getResourceAsStream("app.properties");
                properties.load(inputStream);
            } catch (Error | Exception e) {
                try {
                    properties.load(IoCommand.inputStream(filePath));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                return properties;
            }
        }
        return properties;
    }

}
