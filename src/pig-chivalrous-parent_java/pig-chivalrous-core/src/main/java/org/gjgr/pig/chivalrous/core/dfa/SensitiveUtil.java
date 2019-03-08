package org.gjgr.pig.chivalrous.core.dfa;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import org.gjgr.pig.chivalrous.core.json.JsonCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.util.ThreadUtil;

/**
 * 敏感词工具类
 *
 * @author Looly
 */
public final class SensitiveUtil {
    // private static final Log log = LogFactory.get();

    public static final char DEFAULT_SEPARATOR = StringCommand.C_COMMA;
    private static WordTree sensitiveTree = new WordTree();

    private SensitiveUtil() {
    }

    /**
     * @return 是否已经被初始化
     */
    public static boolean isInited() {
        return !sensitiveTree.isEmpty();
    }

    /**
     * 初始化敏感词树
     *
     * @param isAsync 是否异步初始化
     * @param sensitiveWords 敏感词列表
     */
    public static void init(final Collection<String> sensitiveWords, boolean isAsync) {
        if (isAsync) {
            ThreadUtil.execAsync(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    init(sensitiveWords);
                    return true;
                }

            });
        } else {
            init(sensitiveWords);
        }
    }

    /**
     * 初始化敏感词树
     *
     * @param sensitiveWords 敏感词列表
     */
    public static void init(Collection<String> sensitiveWords) {
        sensitiveTree.clear();
        sensitiveTree.addWords(sensitiveWords);
        // log.debug("Sensitive init finished, sensitives: {}", sensitiveWords);
    }

    /**
     * 初始化敏感词树
     *
     * @param sensitiveWords 敏感词列表组成的字符串
     * @param isAsync 是否异步初始化
     * @param separator 分隔符
     */
    public static void init(String sensitiveWords, char separator, boolean isAsync) {
        if (StringCommand.isNotBlank(sensitiveWords)) {
            init(StringCommand.split(sensitiveWords, separator), isAsync);
        }
    }

    /**
     * 初始化敏感词树，使用逗号分隔每个单词
     *
     * @param sensitiveWords 敏感词列表组成的字符串
     * @param isAsync 是否异步初始化
     */
    public static void init(String sensitiveWords, boolean isAsync) {
        init(sensitiveWords, DEFAULT_SEPARATOR, isAsync);
    }

    /**
     * 是否包含敏感词
     *
     * @param text 文本
     * @return 是否包含
     */
    public static boolean containsSensitive(String text) {
        return sensitiveTree.isMatch(text);
    }

    /**
     * 是否包含敏感词
     *
     * @param obj bean，会被转为JSON字符串
     * @return 是否包含
     */
    public static boolean containsSensitive(Object obj) {
        return sensitiveTree.isMatch(JsonCommand.string(obj));
    }

    /**
     * 查找敏感词，返回找到的第一个敏感词
     *
     * @param text 文本
     * @return 敏感词
     */
    public static String getFindedFirstSensitive(String text) {
        return sensitiveTree.match(text);
    }

    /**
     * 查找敏感词，返回找到的第一个敏感词
     *
     * @param obj bean，会被转为JSON字符串
     * @return 敏感词
     */
    public static String getFindedFirstSensitive(Object obj) {
        return sensitiveTree.match(JsonCommand.string(obj));
    }

    /**
     * 查找敏感词，返回找到的所有敏感词
     *
     * @param text 文本
     * @return 敏感词
     */
    public static List<String> getFindedAllSensitive(String text) {
        return sensitiveTree.matchAll(text);
    }

    /**
     * 查找敏感词，返回找到的所有敏感词<br>
     * 密集匹配原则：假如关键词有 ab,b，文本是abab，将匹配 [ab,b,ab]<br>
     * 贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
     *
     * @param text 文本
     * @param isDensityMatch 是否使用密集匹配原则
     * @param isGreedMatch 是否使用贪婪匹配（最长匹配）原则
     * @return 敏感词
     */
    public static List<String> getFindedAllSensitive(String text, boolean isDensityMatch, boolean isGreedMatch) {
        return sensitiveTree.matchAll(text, -1, isDensityMatch, isGreedMatch);
    }

    /**
     * 查找敏感词，返回找到的所有敏感词
     *
     * @param bean 对象，会被转为JSON
     * @return 敏感词
     */
    public static List<String> getFindedAllSensitive(Object bean) {
        return sensitiveTree.matchAll(JsonCommand.string(bean));
    }

    /**
     * 查找敏感词，返回找到的所有敏感词<br>
     * 密集匹配原则：假如关键词有 ab,b，文本是abab，将匹配 [ab,b,ab]<br>
     * 贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
     *
     * @param bean 对象，会被转为JSON
     * @param isDensityMatch 是否使用密集匹配原则
     * @param isGreedMatch 是否使用贪婪匹配（最长匹配）原则
     * @return 敏感词
     */
    public static List<String> getFindedAllSensitive(Object bean, boolean isDensityMatch, boolean isGreedMatch) {
        return getFindedAllSensitive(JsonCommand.string(bean), isDensityMatch, isGreedMatch);
    }
}
