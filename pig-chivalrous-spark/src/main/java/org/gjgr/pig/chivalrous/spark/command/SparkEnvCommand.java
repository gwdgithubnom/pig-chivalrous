package org.gjgr.pig.chivalrous.spark.command;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.gjgr.pig.chivalrous.core.lang.CollectionCommand;
import org.gjgr.pig.chivalrous.core.system.JavaSystemCommand;
import org.gjgr.pig.chivalrous.core.util.CodeCommand;

/**
 * @author gongwendong
 * @time 04-09-2021  星期五
 * @description: miparent:
 * @target:
 * @more:
 */
public class SparkEnvCommand {

    public static Map<String,String> ROOT_LOCAL_PATH = ImmutableMap.of("windows","c://tmp/hdfs","linux","/tmp/hdfs","mac","/tmp/hdfs");

    public static String LOCAL_PATH = ROOT_LOCAL_PATH.getOrDefault(JavaSystemCommand.getOsInfo().getOsType(),"/tmp/hdfs");

    public Boolean isLocal(SparkConf sparkConf) {
       return sparkConf.get("spark.master").contains("local");
    }

    public Boolean isLocal(SparkContext sparkContext){
        return isLocal(new JavaSparkContext(sparkContext));
    }

    public Boolean isLocal(JavaSparkContext sparkContext){
        return isLocal(sparkContext.getConf());
    }

    public Boolean isLocal(SparkSession sparkSession){
        return isLocal(sparkSession.sparkContext().getConf());
    }

    public String unescape(SparkConf sparkConf,String path){
        if(isLocal(sparkConf)){
            return LOCAL_PATH+path;
        }else{
            return path;
        }
    }

    public String unescape(SparkContext sparkContext,String path){
        return unescape(sparkContext.getConf(),path);
    }

    public String unescape(SparkSession sparkSession,String path){
        return unescape(sparkSession.sparkContext(),path);
    }

}
