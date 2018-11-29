package org.gjgr.pig.chivalrous.core.lang;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author gwd
 * @Time 10-29-2018  Monday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class StringCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(StringCommand.class);

    /**
     * Parse {@ value} to given type {@link T}
     *
     * @param <T> Class of return type. can be int/Integer, long/Long, boolean/Boolean and String
     * @param clazz Class of return type.
     * @param value The input value.
     * @param defaultValue default value.
     * @return the converted value, or defaultValue if {@ value} is <code>null</code>, or it's not a well formated
     * value.
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(Class<T> clazz, String value, T defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        if (clazz == String.class) {
            return clazz.cast(value);
        }

        try {
            if (clazz == int.class || clazz == Integer.class) {
                return (T) (Integer) Integer.parseInt(value);
            }

            if (clazz == long.class || clazz == Long.class) {
                return (T) (Long) Long.parseLong(value);
            }
        } catch (NumberFormatException e) {
            return defaultValue;
        }

        if (clazz == boolean.class || clazz == Boolean.class) {
            return (T) Boolean.valueOf(value);
        }

        throw new IllegalArgumentException(String.format("Invalid Class: %s", clazz));
    }

    /**
     * Parse the {@ input} String to int, if the input is null, or not a valid string, return defaultValue;
     *
     * @param input
     * @param defaultValue
     * @return
     */
    public static int safeParseInt(String input, int defaultValue) {
        return parse(int.class, input, defaultValue);
    }

    /**
     * Parse the {@ input} String to long, if the input is null, or not a valid string, return defaultValue;
     *
     * @param input
     * @param defaultValue
     * @return
     */
    public static long safeParseLong(String input, long defaultValue) {
        return parse(long.class, input, defaultValue);
    }

    /**
     * Get the longest substring of {@ src}, the UTF-8 size should be less than or equals to {@ length}. <br>
     * see http://en.wikipedia.org/wiki/UTF-8
     *
     * @param src the input String.
     * @param start start index of src
     * @param length the max UTF-8 length
     * @return
     */
    public static String substringByUTF8Length(String src, int start, int length) {
        return substringByUTF8Length(src, start, length, false);
    }

    /**
     * Get the longest substring of {@ src}, the UTF-8 size should be less than or equals to {@ length}. <br>
     * see http://en.wikipedia.org/wiki/UTF-8
     *
     * @param src the input String.
     * @param start start index of src
     * @param byteLen the max length of bytes from src with UTF-8 encoding
     * @param replaceLastWithDots replace last chars with three dots ...
     * @return
     */
    public static String substringByUTF8Length(String src, int start, int byteLen, boolean replaceLastWithDots) {
        Validate.notNull(src, "src");
        Validate.isTrue(start >= 0 && start < src.length(), "start should be >= 0 and < src.length()");
        Validate.isTrue(byteLen >= 0, "length should be >= 0");

        if (byteLen == 0) {
            return "";
        }

        int count = 0;
        for (int i = start; i < src.length(); ++i) {
            count += getUTF8Bytes(src.charAt(i));

            if (count == byteLen) {
                return (start == 0 && i == src.length() - 1) ? src : replaceLastWithDots(src, start, i + 1, replaceLastWithDots);
            }

            if (count > byteLen) {
                return replaceLastWithDots(src, start, i, replaceLastWithDots);
            }
        }
        return start == 0 ? src : replaceLastWithDots(src, start, src.length(), replaceLastWithDots);
    }

    /**
     * see http://en.wikipedia.org/wiki/UTF-8
     *
     * @param c
     * @return
     */
    public static int getUTF8Bytes(char c) {
        if (0 <= c && c <= 0x7f) {
            return 1;
        }
        if (c <= 0x7ff) {
            return 2;
        }
        if (c <= 0xd7ff) {// excluding the surrogate area
            return 3;
        }
        if (0xdc00 <= c && c <= 0xffff) {
            return 3;
        }
        return 4;
    }

    private static String replaceLastWithDots(String input, int start, int end, boolean replaceLastWithDots) {
        if (!replaceLastWithDots) {
            return input.substring(start, end);
        }
        int newEnd = end;
        int count = 0;
        for (int i = end - 1; i >= start; --i) {
            count += getUTF8Bytes(input.charAt(i));
            newEnd--;
            if (count >= 3) {
                break;
            }
        }
        return input.substring(start, newEnd) + "...";
    }

    /**
     * It's used to get the hash value of a database partition value. <br>
     * This algorithm was first reported by Dan Bernstein many years ago in comp.lang.c
     *
     * @param str
     * @return
     */
    public static long getDbStringHash(String str) {
        long hash = 5381;
        int idx = 0, len = str.length();
        char c;
        while (idx < len) {
            c = str.charAt(idx);
            hash = ((hash << 5) + hash) + c; // hash*33 + c
            idx++;
        }
        if (hash < 0) {
            hash = -hash;
        }
        return hash;
    }

    public static String stripAndEscapeXml(String input) {
        String stripedString = stripNonValidXMLChars(input);
        return StringEscapeUtils.escapeXml(stripedString);
    }

    /**
     * This method ensures that the output String has only valid XML unicode characters as specified by the XML 1.0
     * standard. any Unicode character, excluding the surrogate blocks, FFFE, and FFFF. Char ::= #x9 | #xA | #xD |
     * [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF] For reference, please see <a href
     * http://www.w3.org/TR/xml/#charsets>XML Characters</a> This method will return an empty String if the input is
     * null or empty.
     *
     * @param input The String whose non-valid characters we want to remove.
     * @return The in String, stripped of non-valid characters.
     */
    public static String stripNonValidXMLChars(String input) {
        int len = input.length();
        char[] chars = new char[len];
        int j = 0;
        for (int i = 0; i < len; i++) {
            char current = input.charAt(i);
            if (((current >= 0x20) && (current <= 0xD7FF))
                    || ((current >= 0xE000) && (current <= 0xFFFD))
                    || (current == 0xA)
                    || (current == 0xD)
                    || (current == 0x9)
                    || ((current >= 0x10000) && (current <= 0x10FFFF))) {
                chars[j] = current;
                ++j;
            }
        }

        return new String(chars, 0, j);
    }

    /**
     * Join the array of string, seperated by the seperator. If the element of the array starts or ends with the
     * seperator, duplcated seperators will be removed.
     *
     * @param appendStart add the seperator at the start of the result.
     * @param appendEnd add the seperator at the end of the result.
     * @param seperator seperator for join
     * @param array input string array,.
     * @return
     */
    public static String join(boolean appendStart, boolean appendEnd, String seperator, String... array) {
        if (array == null) {
            return null;
        }

        StringBuilder joinedPath = new StringBuilder(appendStart ? seperator : "");
        for (int i = 0; i < array.length; ++i) {
            if (i != 0) {
                joinedPath.append(seperator);
            }
            joinedPath.append(StringUtils.removeStart(StringUtils.removeEnd(array[i], seperator), seperator));
        }
        if (appendEnd) {
            joinedPath.append(seperator);
        }
        return joinedPath.toString();
    }

    /**
     * Returns a random String of numbers and letters (lower and upper case) of the specified length. The method uses
     * the Random class that is built-in to Java which is suitable for low to medium grade security uses. This means
     * that the output is only pseudo random, i.e., each number is mathematically generated so is not truly random.
     * <p>
     * The specified length must be at least one. If not, the method will return null.
     *
     * @param length the desired length of the random String to return.
     * @return a random String of numbers and letters of the specified length.
     * @deprecated Please use {@ org.apache.commons.lang.RandomStringUtils} instead.
     */
    @Deprecated
    public static String randomString(int length) {
        return RandomStringUtils.random(length, true, true);
    }

    /**
     * @param val
     * @param kvSeparator
     * @param entrySeparator
     * @return "k:v:v,k2:v2"  -> {{k, v:v}, {k2, v2}}
     */
    public static final Map<String, String> convertToMap(String val, char kvSeparator, char entrySeparator) {
        Map<String, String> result = new HashMap<String, String>();

        if (StringUtils.isNotBlank(val)) {
            String[] entries = StringUtils.split(val, entrySeparator);
            for (String entry : entries) {
                if (StringUtils.isBlank(entry)) {
                    continue;
                }

                String[] kv = StringUtils.split(entry, String.valueOf(kvSeparator), 2);
                if (kv.length != 2) {
                    // not log here to avoid log flooding
                    continue;
                }

                result.put(kv[0].trim(), kv[1].trim());
            }
        }

        return result;
    }

    /**
     * 分割字符串, 省略空串, 并对返回值进行trim处理
     *
     * @param data
     * @return
     */
    public static List<String> splitStringAndTrim(String data, Character seperator) {
        List<String> result = new ArrayList<String>();
        if (StringUtils.isBlank(data)) {
            return result;
        }

        if (seperator == null) {
            result.add(data);
            return result;
        }

        for (String cell : StringUtils.split(data.trim(), seperator)) {
            result.add(cell.trim());
        }
        return result;
    }
}

/**
 * A helper class can be used to replace tokens in a string with other values. <br>
 * For example, you have a replacement map: <br> <pre>
 * <code>
 *  Map<String,String> map = new HashMap<String, String>();
 *  map.put("{name}", "Mike");
 *  map.put("{phone}", "123456");
 * </code>
 * And you have an input like "{name}'s phone is {phone}."
 * Then after replaced it, it's:
 *      Mike's phone is 123456.
 * </pre>
 */
class StringTokenReplacer {
    private Pattern pattern;
    private Map<String, String> replacement;

    public StringTokenReplacer(Pattern pattern) {
        Validate.notNull(pattern);
        this.pattern = pattern;
    }

    /**
     * Create a StringTokenReplacer with a tokens collection, you must provide replacement map when calling the replace method,
     *
     * @param tokens
     */
    public StringTokenReplacer(Collection<String> tokens) {
        Validate.notNull(tokens, "token");
        this.pattern = buildConverterPattern(tokens);
    }

    public StringTokenReplacer(Map<String, String> replacement) {
        Validate.notNull(replacement, "replacement");
        this.pattern = buildConverterPattern(replacement.keySet());
        this.replacement = replacement;
    }

    private static Pattern buildConverterPattern(Collection<String> keys) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            sb.append(Pattern.quote(iter.next()));
            if (!iter.hasNext()) {
                break;
            }
            sb.append("|");
        }

        return Pattern.compile(sb.toString(), Pattern.MULTILINE);
    }

    /**
     * replace the input using the initialized replacement map.
     *
     * @param input
     * @return
     */
    public String replace(String input) {
        if (replacement == null) {
            throw new IllegalStateException("Replacement map was not initialized.");
        }

        return replace(input, replacement);
    }

    /**
     * replace the input with the given replacement.
     * the keys of the map must in the tokens list of this StringTokenReplacer instance
     *
     * @param input
     * @param replacement
     * @return
     */
    public String replace(String input, Map<String, String> replacement) {
        Validate.notNull(input, "input");
        Validate.notNull(replacement, "replacement");

        StringBuilder sb = new StringBuilder(input.length());
        Matcher matcher = pattern.matcher(input);
        int index = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            sb.append(input.substring(index, start));

            String group = matcher.group();
            String text = replacement.get(group);

            sb.append(text == null ? group : text);
            index = end;
        }
        sb.append(input.substring(index));

        return sb.toString();
    }
}
