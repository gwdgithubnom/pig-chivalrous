package org.gjgr.pig.chivalrous.core.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.gjgr.pig.chivalrous.core.json.JsonCommand;
import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author gwd
 * @Time 10-29-2018 Monday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public final class StringCommand {

    public static final char C_SPACE = ' ';
    public static final char C_TAB = (char) 8;
    public static final char C_DOT = '.';
    public static final char C_SLASH = '/';
    public static final char C_BACKSLASH = '\\';
    public static final char C_CR = '\r';
    public static final char C_LF = '\n';
    public static final char C_UNDERLINE = '_';
    public static final char C_COMMA = ',';
    public static final char C_DELIM_START = '{';
    public static final char C_DELIM_END = '}';
    public static final char C_COLON = ':';
    public static final String SPACE = " ";
    public static final String TAB = C_TAB + "";
    public static final String DOT = ".";
    public static final String DOUBLE_DOT = "..";
    public static final String SLASH = "/";
    public static final String BACKSLASH = "\\";
    public static final String EMPTY = "";
    public static final String CR = "\r";
    public static final String LF = "\n";
    public static final String CRLF = "\r\n";
    public static final String UNDERLINE = "_";
    public static final String COMMA = ",";
    public static final String DELIM_START = "{";
    public static final String DELIM_END = "}";
    public static final String COLON = ":";
    public static final String HTML_NBSP = "&nbsp;";
    public static final String HTML_AMP = "&amp";
    public static final String HTML_QUOTE = "&quot;";
    public static final String HTML_LT = "&lt;";
    public static final String HTML_GT = "&gt;";
    public static final String EMPTY_JSON = "{}";
    private static final Logger LOGGER = LoggerFactory.getLogger(StringCommand.class);

    // ------------------------------------------------------------------------ Blank

    /**
     * Constructor of utility class should be private.
     */
    private StringCommand() {
    }

    /**
     * 字符串是否为空白 空白的定义如下： <br>
     * 1、为null <br>
     * 2、为不可见字符（如空格）<br>
     * 3、""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isBlank(CharSequence str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (false == Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Trim <i>all</i> whitespace from the given {@code String}: leading, trailing, and in between characters.
     * 
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see java.lang.Character#isWhitespace
     */
    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Trim leading whitespace from the given {@code String}.
     * 
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see java.lang.Character#isWhitespace
     */
    public static String trimLeadingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /**
     * Trim trailing whitespace from the given {@code String}.
     * 
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see java.lang.Character#isWhitespace
     */
    public static String trimTrailingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Trim all occurrences of the supplied leading character from the given {@code String}.
     * 
     * @param str the {@code String} to check
     * @param leadingCharacter the leading character to be trimmed
     * @return the trimmed {@code String}
     */
    public static String trimLeadingCharacter(String str, char leadingCharacter) {
        if (!hasLength(str)) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && sb.charAt(0) == leadingCharacter) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /**
     * Trim all occurrences of the supplied trailing character from the given {@code String}.
     * 
     * @param str the {@code String} to check
     * @param trailingCharacter the trailing character to be trimmed
     * @return the trimmed {@code String}
     */
    public static String trimTrailingCharacter(String str, char trailingCharacter) {
        if (!hasLength(str)) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && sb.charAt(sb.length() - 1) == trailingCharacter) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 字符串是否为非空白 空白的定义如下： <br>
     * 1、不为null <br>
     * 2、不为不可见字符（如空格）<br>
     * 3、不为""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为非空
     */
    public static boolean isNotBlank(CharSequence str) {
        return false == isBlank(str);
    }

    /**
     * 是否包含空字符串
     *
     * @param strs 字符串列表
     * @return 是否包含空字符串
     */
    public static boolean hasBlank(CharSequence...strs) {
        if (ArrayCommand.isEmpty(strs)) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check that the given {@code CharSequence} is neither {@code null} nor of length 0.
     * <p>
     * Note: this method returns {@code true} for a {@code CharSequence} that purely consists of whitespace.
     * <p>
     * 
     * <pre class="code">
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     * 
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null} and has length
     * @see #hasText(String)
     */
    public static boolean hasLength(@Nullable CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Check that the given {@code String} is neither {@code null} nor of length 0.
     * <p>
     * Note: this method returns {@code true} for a {@code String} that purely consists of whitespace.
     * 
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null} and has length
     * @see #hasLength(CharSequence)
     * @see #hasText(String)
     */
    public static boolean hasLength(@Nullable String str) {
        return (str != null && !str.isEmpty());
    }

    /**
     * Check whether the given {@code CharSequence} contains actual <em>text</em>.
     * <p>
     * More specifically, this method returns {@code true} if the {@code CharSequence} is not {@code null}, its length
     * is greater than 0, and it contains at least one non-whitespace character.
     * <p>
     * 
     * <pre class="code">
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     * 
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null}, its length is greater than 0, and it does
     *         not contain whitespace only
     * @see Character#isWhitespace
     */
    public static boolean hasText(@Nullable CharSequence str) {
        return (str != null && str.length() > 0 && containsText(str));
    }

    /**
     * Check whether the given {@code String} contains actual <em>text</em>.
     * <p>
     * More specifically, this method returns {@code true} if the {@code String} is not {@code null}, its length is
     * greater than 0, and it contains at least one non-whitespace character.
     * 
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null}, its length is greater than 0, and it does not
     *         contain whitespace only
     * @see #hasText(CharSequence)
     */
    public static boolean hasText(@Nullable String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given {@code CharSequence} contains any whitespace characters.
     * 
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not empty and contains at least 1 whitespace character
     * @see Character#isWhitespace
     */
    public static boolean containsWhitespace(@Nullable CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }

        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given {@code String} contains any whitespace characters.
     * 
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not empty and contains at least 1 whitespace character
     * @see #containsWhitespace(CharSequence)
     */
    public static boolean containsWhitespace(@Nullable String str) {
        return containsWhitespace((CharSequence) str);
    }

    /**
     * Replace all occurrences of a substring within a string with another string.
     * 
     * @param inString {@code String} to examine
     * @param oldPattern {@code String} to replace
     * @param newPattern {@code String} to insert
     * @return a {@code String} with the replacements
     */
    public static String replace(String inString, String oldPattern, @Nullable String newPattern) {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString;
        }
        int index = inString.indexOf(oldPattern);
        if (index == -1) {
            // no occurrence -> can return input as-is
            return inString;
        }

        int capacity = inString.length();
        if (newPattern.length() > oldPattern.length()) {
            capacity += 16;
        }
        StringBuilder sb = new StringBuilder(capacity);

        int pos = 0; // our position in the old string
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }

        // append any characters to the right of a match
        sb.append(inString.substring(pos));
        return sb.toString();
    }

    /**
     * Delete all occurrences of the given substring.
     * 
     * @param inString the original {@code String}
     * @param pattern the pattern to delete all occurrences of
     * @return the resulting {@code String}
     */
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    /**
     * Convert a {@link Collection} to a delimited {@code String} (e.g. CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     * 
     * @param coll the {@code Collection} to convert
     * @param delim the delimiter to use (typically a ",")
     * @param prefix the {@code String} to start each element with
     * @param suffix the {@code String} to end each element with
     * @return the delimited {@code String}
     */
    public static String collectionToDelimitedString(
            @Nullable Collection<?> coll, String delim, String prefix, String suffix) {

        if (CollectionUtils.isEmpty(coll)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        Iterator<?> it = coll.iterator();
        while (it.hasNext()) {
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    /**
     * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     * 
     * @param coll the {@code Collection} to convert
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String collectionToDelimitedString(@Nullable Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    /**
     * Convert a {@code Collection} into a delimited {@code String} (e.g., CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     * 
     * @param coll the {@code Collection} to convert
     * @return the delimited {@code String}
     */
    public static String collectionToCommaDelimitedString(Collection<?> coll) {
        return collectionToDelimitedString(coll, ",");
    }

    /**
     * Convert a {@code String} array into a delimited {@code String} (e.g. CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     * 
     * @param arr the array to display
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String arrayToDelimitedString(@Nullable Object[] arr, String delim) {
        if (ObjectCommand.isEmpty(arr)) {
            return "";
        }
        if (arr.length == 1) {
            return ObjectCommand.nullSafeToString(arr[0]);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(delim);
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * Append the given {@code String} to the given {@code String} array, returning a new array consisting of the input
     * array contents plus the given {@code String}.
     * 
     * @param array the array to append to (can be {@code null})
     * @param str the {@code String} to append
     * @return the new array (never {@code null})
     */
    public static String[] addStringToArray(@Nullable String[] array, String str) {
        if (ObjectCommand.isEmpty(array)) {
            return new String[] { str };
        }

        String[] newArr = new String[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = str;
        return newArr;
    }

    /**
     * Concatenate the given {@code String} arrays into one, with overlapping array elements included twice.
     * <p>
     * The order of elements in the original arrays is preserved.
     * 
     * @param array1 the first array (can be {@code null})
     * @param array2 the second array (can be {@code null})
     * @return the new array ({@code null} if both given arrays were {@code null})
     */
    @Nullable
    public static String[] concatenateStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
        if (ObjectCommand.isEmpty(array1)) {
            return array2;
        }
        if (ObjectCommand.isEmpty(array2)) {
            return array1;
        }

        String[] newArr = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, newArr, 0, array1.length);
        System.arraycopy(array2, 0, newArr, array1.length, array2.length);
        return newArr;
    }

    /**
     * Merge the given {@code String} arrays into one, with overlapping array elements only included once.
     * <p>
     * The order of elements in the original arrays is preserved (with the exception of overlapping elements, which are
     * only included on their first occurrence).
     * 
     * @param array1 the first array (can be {@code null})
     * @param array2 the second array (can be {@code null})
     * @return the new array ({@code null} if both given arrays were {@code null})
     * @deprecated as of 4.3.15, in favor of manual merging via {@link LinkedHashSet} (with every entry included at most
     *             once, even entries within the first array)
     */
    @Deprecated
    @Nullable
    public static String[] mergeStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
        if (ObjectCommand.isEmpty(array1)) {
            return array2;
        }
        if (ObjectCommand.isEmpty(array2)) {
            return array1;
        }

        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList(array1));
        for (String str : array2) {
            if (!result.contains(str)) {
                result.add(str);
            }
        }
        return toStringArray(result);
    }

    /**
     * Turn given source {@code String} array into sorted array.
     * 
     * @param array the source array
     * @return the sorted array (never {@code null})
     */
    public static String[] sortStringArray(String[] array) {
        if (ObjectCommand.isEmpty(array)) {
            return new String[0];
        }

        Arrays.sort(array);
        return array;
    }

    /**
     * Trim the elements of the given {@code String} array, calling {@code String.trim()} on each of them.
     * 
     * @param array the original {@code String} array
     * @return the resulting array (of the same size) with trimmed elements
     */
    public static String[] trimArrayElements(@Nullable String[] array) {
        if (ObjectCommand.isEmpty(array)) {
            return new String[0];
        }

        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            String element = array[i];
            result[i] = (element != null ? element.trim() : null);
        }
        return result;
    }

    /**
     * Remove duplicate strings from the given array.
     * <p>
     * As of 4.2, it preserves the original order, as it uses a {@link LinkedHashSet}.
     * 
     * @param array the {@code String} array
     * @return an array without duplicates, in natural sort order
     */
    public static String[] removeDuplicateStrings(String[] array) {
        if (ObjectCommand.isEmpty(array)) {
            return array;
        }

        Set<String> set = new LinkedHashSet<>();
        for (String element : array) {
            set.add(element);
        }
        return toStringArray(set);
    }

    /**
     * Take an array of strings and split each element based on the given delimiter. A {@code Properties} instance is
     * then generated, with the left of the delimiter providing the key, and the right of the delimiter providing the
     * value.
     * <p>
     * Will trim both the key and value before adding them to the {@code Properties}.
     * 
     * @param array the array to process
     * @param delimiter to split each element using (typically the equals symbol)
     * @return a {@code Properties} instance representing the array contents, or {@code null} if the array to process
     *         was {@code null} or empty
     */
    @Nullable
    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
        return splitArrayElementsIntoProperties(array, delimiter, null);
    }

    /**
     * Take an array of strings and split each element based on the given delimiter. A {@code Properties} instance is
     * then generated, with the left of the delimiter providing the key, and the right of the delimiter providing the
     * value.
     * <p>
     * Will trim both the key and value before adding them to the {@code Properties} instance.
     * 
     * @param array the array to process
     * @param delimiter to split each element using (typically the equals symbol)
     * @param charsToDelete one or more characters to remove from each element prior to attempting the split operation
     *            (typically the quotation mark symbol), or {@code null} if no removal should occur
     * @return a {@code Properties} instance representing the array contents, or {@code null} if the array to process
     *         was {@code null} or empty
     */
    @Nullable
    public static Properties splitArrayElementsIntoProperties(
            String[] array, String delimiter, @Nullable String charsToDelete) {

        if (ObjectCommand.isEmpty(array)) {
            return null;
        }

        Properties result = new Properties();
        for (String element : array) {
            if (charsToDelete != null) {
                element = deleteAny(element, charsToDelete);
            }
            String[] splittedElement = split(element, delimiter);
            if (splittedElement == null) {
                continue;
            }
            result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
        }
        return result;
    }

    /**
     * Tokenize the given {@code String} into a {@code String} array via a {@link StringTokenizer}.
     * <p>
     * Trims tokens and omits empty tokens.
     * <p>
     * The given {@code delimiters} string can consist of any number of delimiter characters. Each of those characters
     * can be used to separate tokens. A delimiter is always a single character; for multi-character delimiters,
     * consider using {@link #delimitedListToStringArray}.
     * 
     * @param str the {@code String} to tokenize
     * @param delimiters the delimiter characters, assembled as a {@code String} (each of the characters is individually
     *            considered as a delimiter)
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(@Nullable String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * Tokenize the given {@code String} into a {@code String} array via a {@link StringTokenizer}.
     * <p>
     * The given {@code delimiters} string can consist of any number of delimiter characters. Each of those characters
     * can be used to separate tokens. A delimiter is always a single character; for multi-character delimiters,
     * consider using {@link #delimitedListToStringArray}.
     * 
     * @param str the {@code String} to tokenize
     * @param delimiters the delimiter characters, assembled as a {@code String} (each of the characters is individually
     *            considered as a delimiter)
     * @param trimTokens trim the tokens via {@link String#trim()}
     * @param ignoreEmptyTokens omit empty tokens from the result array (only applies to tokens that are empty after
     *            trimming; StringTokenizer will not consider subsequent delimiters as token in the first place).
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(
            @Nullable String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return new String[0];
        }

        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    /**
     * Convert a comma delimited list (e.g., a row from a CSV file) into an array of strings.
     * 
     * @param str the input {@code String}
     * @return an array of strings, or the empty array in case of empty input
     */
    public static String[] commaDelimitedListToStringArray(@Nullable String str) {
        return delimitedListToStringArray(str, ",");
    }

    /**
     * Convert a comma delimited list (e.g., a row from a CSV file) into a set.
     * <p>
     * Note that this will suppress duplicates, and as of 4.2, the elements in the returned set will preserve the
     * original order in a {@link LinkedHashSet}.
     * 
     * @param str the input {@code String}
     * @return a set of {@code String} entries in the list
     * @see #removeDuplicateStrings(String[])
     */
    public static Set<String> commaDelimitedListToSet(@Nullable String str) {
        Set<String> set = new LinkedHashSet<>();
        String[] tokens = commaDelimitedListToStringArray(str);
        for (String token : tokens) {
            set.add(token);
        }
        return set;
    }

    /**
     * Convert a {@code String} array into a comma delimited {@code String} (i.e., CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     * 
     * @param arr the array to display
     * @return the delimited {@code String}
     */
    public static String arrayToCommaDelimitedString(@Nullable Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }

    /**
     * Take a {@code String} that is a delimited list and convert it into a {@code String} array.
     * <p>
     * A single {@code delimiter} may consist of more than one character, but it will still be considered as a single
     * delimiter string, rather than as bunch of potential delimiter characters, in contrast to
     * {@link %tokenizeToStringArray }.
     * 
     * @param str the input {@code String}
     * @param delimiter the delimiter between elements (this is a single delimiter, rather than a bunch individual
     *            delimiter characters)
     * @param charsToDelete a set of characters to delete; useful for deleting unwanted line breaks: e.g. "\r\n\f" will
     *            delete all new lines and line feeds in a {@code String}
     * @return an array of the tokens in the list
     * @see %tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(
            @Nullable String str, @Nullable String delimiter, @Nullable String charsToDelete) {

        if (str == null) {
            return new String[0];
        }
        if (delimiter == null) {
            return new String[] { str };
        }

        List<String> result = new ArrayList<>();
        if ("".equals(delimiter)) {
            for (int i = 0; i < str.length(); i++) {
                result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
            }
        } else {
            int pos = 0;
            int delPos;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                // Add rest of String, but not in case of empty input.
                result.add(deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return toStringArray(result);
    }

    /**
     * Copy the given {@code Collection} into a {@code String} array.
     * <p>
     * The {@code Collection} must contain {@code String} elements only.
     * 
     * @param collection the {@code Collection} to copy
     * @return the {@code String} array
     */
    public static String[] toStringArray(Collection<String> collection) {
        return collection.toArray(new String[0]);
    }

    /**
     * Copy the given Enumeration into a {@code String} array. The Enumeration must contain {@code String} elements
     * only.
     * 
     * @param enumeration the Enumeration to copy
     * @return the {@code String} array
     */
    public static String[] toStringArray(Enumeration<String> enumeration) {
        return toStringArray(Collections.list(enumeration));
    }

    /**
     * Take a {@code String} that is a delimited list and convert it into a {@code String} array.
     * <p>
     * A single {@code delimiter} may consist of more than one character, but it will still be considered as a single
     * delimiter string, rather than as bunch of potential delimiter characters, in contrast to
     * {@link %tokenizeToStringArray}.
     * 
     * @param str the input {@code String}
     * @param delimiter the delimiter between elements (this is a single delimiter, rather than a bunch individual
     *            delimiter characters)
     * @return an array of the tokens in the list
     * @see %tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(@Nullable String str, @Nullable String delimiter) {
        return delimitedListToStringArray(str, delimiter, null);
    }

    /**
     * Delete any character in a given {@code String}.
     * 
     * @param inString the original {@code String}
     * @param charsToDelete a set of characters to delete. E.g. "az\n" will delete 'a's, 'z's and new lines.
     * @return the resulting {@code String}
     */
    public static String deleteAny(String inString, @Nullable String charsToDelete) {
        if (!hasLength(inString) || !hasLength(charsToDelete)) {
            return inString;
        }

        StringBuilder sb = new StringBuilder(inString.length());
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------------ Empty

    /**
     * 给定所有字符串是否为空白
     *
     * @param strs 字符串
     * @return 所有字符串是否为空白
     */
    public static boolean isAllBlank(CharSequence...strs) {
        if (ArrayCommand.isEmpty(strs)) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isNotBlank(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串是否为空，空的定义如下 1、为null <br>
     * 2、为""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 字符串是否为非空白 空白的定义如下： <br>
     * 1、不为null <br>
     * 2、不为""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为非空
     */
    public static boolean isNotEmpty(CharSequence str) {
        return false == isEmpty(str);
    }

    /**
     * 当给定字符串为null时，转换为Empty
     *
     * @param str 被转换的字符串
     * @return 转换后的字符串
     */
    public static String nullToEmpty(String str) {
        return nullToDefault(str, EMPTY);
    }

    /**
     * 如果字符串是<code>null</code>，则返回指定默认字符串，否则返回字符串本身。
     * <p>
     *
     * <pre>
     * nullToDefault(null, &quot;default&quot;)  = &quot;default&quot;
     * nullToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
     * nullToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
     * nullToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     * </pre>
     *
     * @param str 要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String nullToDefault(String str, String defaultStr) {
        return (str == null) ? defaultStr : str;
    }

    /**
     * 当给定字符串为空字符串时，转换为<code>null</code>
     *
     * @param str 被转换的字符串
     * @return 转换后的字符串
     */
    public static String emptyToNull(String str) {
        return isEmpty(str) ? null : str;
    }

    /**
     * 是否包含空字符串
     *
     * @param strs 字符串列表
     * @return 是否包含空字符串
     */
    public static boolean hasEmpty(CharSequence...strs) {
        if (ArrayCommand.isEmpty(strs)) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    // ------------------------------------------------------------------------ Trim

    /**
     * 是否全部为空字符串
     *
     * @param strs 字符串列表
     * @return 是否全部为空字符串
     */
    public static boolean isAllEmpty(CharSequence...strs) {
        if (ArrayCommand.isEmpty(strs)) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isNotEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 除去字符串头尾部的空白，如果字符串是<code>null</code>，依然返回<code>null</code>。
     * <p>
     * <p>
     * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p>
     *
     * <pre>
     * trim(null)          = null
     * trim(&quot;&quot;)            = &quot;&quot;
     * trim(&quot;     &quot;)       = &quot;&quot;
     * trim(&quot;abc&quot;)         = &quot;abc&quot;
     * trim(&quot;    abc    &quot;) = &quot;abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
     */
    public static String trim(String str) {
        return (null == str) ? null : trim(str, 0);
    }

    /**
     * 给定字符串数组全部做去首尾空格
     *
     * @param strs 字符串数组
     */
    public static void trim(String[] strs) {
        if (null == strs) {
            return;
        }
        String str;
        for (int i = 0; i < strs.length; i++) {
            str = strs[i];
            if (null != str) {
                strs[i] = str.trim();
            }
        }
    }

    /**
     * 除去字符串头部的空白，如果字符串是<code>null</code>，则返回<code>null</code>。
     * <p>
     * <p>
     * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p>
     *
     * <pre>
     * trimStart(null)         = null
     * trimStart(&quot;&quot;)           = &quot;&quot;
     * trimStart(&quot;abc&quot;)        = &quot;abc&quot;
     * trimStart(&quot;  abc&quot;)      = &quot;abc&quot;
     * trimStart(&quot;abc  &quot;)      = &quot;abc  &quot;
     * trimStart(&quot; abc &quot;)      = &quot;abc &quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回 <code>null</code>
     */
    public static String trimStart(String str) {
        return trim(str, -1);
    }

    /**
     * 除去字符串尾部的空白，如果字符串是<code>null</code>，则返回<code>null</code>。
     * <p>
     * <p>
     * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p>
     *
     * <pre>
     * trimEnd(null)       = null
     * trimEnd(&quot;&quot;)         = &quot;&quot;
     * trimEnd(&quot;abc&quot;)      = &quot;abc&quot;
     * trimEnd(&quot;  abc&quot;)    = &quot;  abc&quot;
     * trimEnd(&quot;abc  &quot;)    = &quot;abc&quot;
     * trimEnd(&quot; abc &quot;)    = &quot; abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回 <code>null</code>
     */
    public static String trimEnd(String str) {
        return trim(str, 1);
    }

    /**
     * 除去字符串头尾部的空白符，如果字符串是<code>null</code>，依然返回<code>null</code>。
     *
     * @param str 要处理的字符串
     * @param mode <code>-1</code>表示trimStart，<code>0</code>表示trim全部， <code>1</code>表示trimEnd
     * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
     */
    public static String trim(String str, int mode) {
        if (str == null) {
            return null;
        }

        int length = str.length();
        int start = 0;
        int end = length;

        // 扫描字符串头部
        if (mode <= 0) {
            while ((start < end) && (Character.isWhitespace(str.charAt(start)))) {
                start++;
            }
        }

        // 扫描字符串尾部
        if (mode >= 0) {
            while ((start < end) && (Character.isWhitespace(str.charAt(end - 1)))) {
                end--;
            }
        }

        if ((start > 0) || (end < length)) {
            return str.substring(start, end);
        }

        return str;
    }

    /**
     * 是否以指定字符串开头
     *
     * @param str 被监测字符串
     * @param prefix 开头字符串
     * @param isIgnoreCase 是否忽略大小写
     * @return 是否以指定字符串开头
     */
    public static boolean startWith(String str, String prefix, boolean isIgnoreCase) {
        if (isIgnoreCase) {
            return str.toLowerCase().startsWith(prefix.toLowerCase());
        } else {
            return str.startsWith(prefix);
        }
    }

    /**
     * 是否以指定字符串开头，忽略大小写
     *
     * @param str 被监测字符串
     * @param prefix 开头字符串
     * @return 是否以指定字符串开头
     */
    public static boolean startWithIgnoreCase(String str, String prefix) {
        return startWith(str, prefix, true);
    }

    /**
     * 是否以指定字符串结尾
     *
     * @param str 被监测字符串
     * @param suffix 结尾字符串
     * @param isIgnoreCase 是否忽略大小写
     * @return 是否以指定字符串结尾
     */
    public static boolean endWith(String str, String suffix, boolean isIgnoreCase) {
        if (isIgnoreCase) {
            return str.toLowerCase().endsWith(suffix.toLowerCase());
        } else {
            return str.endsWith(suffix);
        }
    }

    /**
     * 是否以指定字符串结尾，忽略大小写
     *
     * @param str 被监测字符串
     * @param suffix 结尾字符串
     * @return 是否以指定字符串结尾
     */
    public static boolean endWithIgnoreCase(String str, String suffix) {
        return endWith(str, suffix, true);
    }

    /**
     * 获得set或get方法对应的标准属性名<br/>
     * 例如：setName 返回 name
     *
     * @param getOrSetMethodName
     * @return 如果是set或get方法名，返回field， 否则null
     */
    public static String getGeneralField(String getOrSetMethodName) {
        if (getOrSetMethodName.startsWith("get") || getOrSetMethodName.startsWith("set")) {
            return removePreAndLowerFirst(getOrSetMethodName, 3);
        }
        return null;
    }

    /**
     * 生成set方法名<br/>
     * 例如：name 返回 setName
     *
     * @param fieldName 属性名
     * @return setXxx
     */
    public static String genSetter(String fieldName) {
        return upperFirstAndAddPre(fieldName, "set");
    }

    /**
     * 生成get方法名
     *
     * @param fieldName 属性名
     * @return getXxx
     */
    public static String genGetter(String fieldName) {
        return upperFirstAndAddPre(fieldName, "get");
    }

    /**
     * 移除字符串中所有给定字符串<br>
     * 例：removeAll("aa-bb-cc-dd", "-") -> aabbccdd
     *
     * @param str 字符串
     * @param strToRemove 被移除的字符串
     * @return 移除后的字符串
     */
    public static String removeAll(String str, CharSequence strToRemove) {
        return str.replace(strToRemove, EMPTY);
    }

    /**
     * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br/>
     * 例如：str=setName, preLength=3 -> return name
     *
     * @param str 被处理的字符串
     * @param preLength 去掉的长度
     * @return 处理后的字符串，不符合规范返回null
     */
    public static String removePreAndLowerFirst(String str, int preLength) {
        if (str == null) {
            return null;
        }
        if (str.length() > preLength) {
            char first = Character.toLowerCase(str.charAt(preLength));
            if (str.length() > preLength + 1) {
                return first + str.substring(preLength + 1);
            }
            return String.valueOf(first);
        } else {
            return str;
        }
    }

    /**
     * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br/>
     * 例如：str=setName, prefix=set -> return name
     *
     * @param str 被处理的字符串
     * @param prefix 前缀
     * @return 处理后的字符串，不符合规范返回null
     */
    public static String removePreAndLowerFirst(String str, String prefix) {
        return lowerFirst(removePrefix(str, prefix));
    }

    /**
     * 原字符串首字母大写并在其首部添加指定字符串 例如：str=name, preString=get -> return getName
     *
     * @param str 被处理的字符串
     * @param preString 添加的首部
     * @return 处理后的字符串
     */
    public static String upperFirstAndAddPre(String str, String preString) {
        if (str == null || preString == null) {
            return null;
        }
        return preString + upperFirst(str);
    }

    /**
     * 大写首字母<br>
     * 例如：str = name, return Name
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String upperFirst(String str) {
        if (StringCommand.isBlank(str)) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + subSuf(str, 1);
    }

    /**
     * 小写首字母<br>
     * 例如：str = Name, return name
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String lowerFirst(String str) {
        if (isBlank(str)) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + subSuf(str, 1);
    }

    /**
     * 去掉指定前缀
     *
     * @param str 字符串
     * @param prefix 前缀
     * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
     */
    public static String removePrefix(String str, String prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str;
        }

        if (str.startsWith(prefix)) {
            return subSuf(str, prefix.length());// 截取后半段
        }
        return str;
    }

    /**
     * 忽略大小写去掉指定前缀
     *
     * @param str 字符串
     * @param prefix 前缀
     * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
     */
    public static String removePrefixIgnoreCase(String str, String prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str;
        }

        if (str.toLowerCase().startsWith(prefix.toLowerCase())) {
            return subSuf(str, prefix.length());// 截取后半段
        }
        return str;
    }

    /**
     * 去掉指定后缀
     *
     * @param str 字符串
     * @param suffix 后缀
     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
     */
    public static String removeSuffix(String str, String suffix) {
        if (isEmpty(str) || isEmpty(suffix)) {
            return str;
        }

        if (str.endsWith(suffix)) {
            return subPre(str, str.length() - suffix.length());// 截取前半段
        }
        return str;
    }

    /**
     * 去掉指定后缀，并小写首字母
     *
     * @param str 字符串
     * @param suffix 后缀
     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
     */
    public static String removeSufAndLowerFirst(String str, String suffix) {
        return lowerFirst(removeSuffix(str, suffix));
    }

    /**
     * 忽略大小写去掉指定后缀
     *
     * @param str 字符串
     * @param suffix 后缀
     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
     */
    public static String removeSuffixIgnoreCase(String str, String suffix) {
        if (isEmpty(str) || isEmpty(suffix)) {
            return str;
        }

        if (str.toLowerCase().endsWith(suffix.toLowerCase())) {
            return subPre(str, str.length() - suffix.length());
        }
        return str;
    }

    /**
     * 如果给定字符串不是以prefix开头的，在开头补充 prefix
     *
     * @param str 字符串
     * @param prefix 前缀
     * @return 补充后的字符串
     */
    public static String addPrefixIfNot(String str, String prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str;
        }
        if (false == str.startsWith(prefix)) {
            str = prefix + str;
        }
        return str;
    }

    /**
     * 如果给定字符串不是以suffix结尾的，在尾部补充 suffix
     *
     * @param str 字符串
     * @param suffix 后缀
     * @return 补充后的字符串
     */
    public static String addSuffixIfNot(String str, String suffix) {
        if (isEmpty(str) || isEmpty(suffix)) {
            return str;
        }
        if (false == str.endsWith(suffix)) {
            str += suffix;
        }
        return str;
    }

    /**
     * 清理空白字符
     *
     * @param str 被清理的字符串
     * @return 清理后的字符串
     */
    public static String cleanBlank(String str) {
        if (str == null) {
            return null;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        char c;
        for (int i = 0; i < len; i++) {
            c = str.charAt(i);
            if (false == Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 切分字符串
     *
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的集合
     */
    public static String[] splitToArray(String str, char separator) {
        List<String> result = split(str, separator);
        return result.toArray(new String[result.size()]);
    }

    /**
     * 切分字符串<br>
     * a#b#c -> [a,b,c] <br>
     * a##b#c -> [a,"",b,c]
     *
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的集合
     */
    public static List<String> split(String str, char separator) {
        return split(str, separator, 0);
    }

    /**
     * 切分字符串
     *
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @param limit 限制分片数
     * @return 切分后的集合
     */
    public static String[] splitToArray(String str, char separator, int limit) {
        List<String> result = split(str, separator, limit);
        return result.toArray(new String[result.size()]);
    }

    /**
     * 切分字符串
     *
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @param limit 限制分片数
     * @return 切分后的集合
     */
    public static List<String> split(String str, char separator, int limit) {
        if (str == null) {
            return null;
        }
        List<String> list = new ArrayList<String>(limit == 0 ? 16 : limit);
        if (limit == 1) {
            list.add(str);
            return list;
        }

        boolean isNotEnd = true; // 未结束切分的标志
        int strLen = str.length();
        StringBuilder sb = new StringBuilder(strLen);
        for (int i = 0; i < strLen; i++) {
            char c = str.charAt(i);
            if (isNotEnd && c == separator) {
                list.add(sb.toString());
                // 清空StringBuilder
                sb.delete(0, sb.length());

                // 当达到切分上限-1的量时，将所剩字符全部作为最后一个串
                if (limit != 0 && list.size() == limit - 1) {
                    isNotEnd = false;
                }
            } else {
                sb.append(c);
            }
        }
        list.add(sb.toString());// 加入尾串
        return list;
    }

    /**
     * Split a {@code String} at the first occurrence of the delimiter. Does not include the delimiter in the result.
     * 
     * @param toSplit the string to split
     * @param delimiter to split the string up with
     * @return a two element array with index 0 being before the delimiter, and index 1 being after the delimiter
     *         (neither element includes the delimiter); or {@code null} if the delimiter wasn't found in the given
     *         input {@code String}
     */
    public static String[] split(String toSplit, String delimiter) {
        if (!hasLength(toSplit) || !hasLength(delimiter)) {
            return null;
        }
        int offset = toSplit.indexOf(delimiter);
        if (offset < 0) {
            return null;
        }

        String beforeDelimiter = toSplit.substring(0, offset);
        String afterDelimiter = toSplit.substring(offset + delimiter.length());
        return new String[] { beforeDelimiter, afterDelimiter };

    }

    /**
     * 切分字符串<br>
     * from jodd
     *
     * @param str 被切分的字符串
     * @param delimiter 分隔符
     * @return 字符串
     */
    public static String[] spliter(String str, String delimiter) {
        if (str == null) {
            return null;
        }
        if (str.trim().length() == 0) {
            return new String[] { str };
        }
        /**
         * del length
         */
        int dellen = delimiter.length();
        /**
         * one more for the last
         */
        int maxparts = (str.length() / dellen) + 2;
        int[] positions = new int[maxparts];

        int i;
        int j = 0;
        int count = 0;
        positions[0] = -dellen;
        while ((i = str.indexOf(delimiter, j)) != -1) {
            count++;
            positions[count] = i;
            j = i + dellen;
        }
        count++;
        positions[count] = str.length();

        String[] result = new String[count];

        for (i = 0; i < count; i++) {
            result[i] = str.substring(positions[i] + dellen, positions[i + 1]);
        }
        return result;
    }

    /**
     * 根据给定长度，将给定字符串截取为多个部分
     *
     * @param str 字符串
     * @param len 每一个小节的长度
     * @return 截取后的字符串数组
     */
    public static String[] split(String str, int len) {
        int partCount = str.length() / len;
        int lastPartCount = str.length() % len;
        int fixPart = 0;
        if (lastPartCount != 0) {
            fixPart = 1;
        }
        final String[] strs = new String[partCount + fixPart];
        for (int i = 0; i < partCount + fixPart; i++) {
            if (i == partCount + fixPart - 1 && lastPartCount != 0) {
                strs[i] = str.substring(i * len, i * len + lastPartCount);
            } else {
                strs[i] = str.substring(i * len, i * len + len);
            }
        }
        return strs;
    }

    /**
     * 改进JDK subString<br>
     * index从0开始计算，最后一个字符为-1<br>
     * 如果from和to位置一样，返回 "" <br>
     * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
     * 如果经过修正的index中from大于to，则互换from和to example: <br>
     * abcdefgh 2 3 -> c <br>
     * abcdefgh 2 -3 -> cde <br>
     *
     * @param string String
     * @param fromIndex 开始的index（包括）
     * @param toIndex 结束的index（不包括）
     * @return 字串
     */
    public static String sub(String string, int fromIndex, int toIndex) {
        int len = string.length();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        } else if (fromIndex > len) {
            fromIndex = len;
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len) {
            toIndex = len;
        }

        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }

        if (fromIndex == toIndex) {
            return EMPTY;
        }

        return string.substring(fromIndex, toIndex);
    }

    /**
     * 切割前部分
     *
     * @param string 字符串
     * @param toIndex 切割到的位置（不包括）
     * @return 切割后的字符串
     */
    public static String subPre(String string, int toIndex) {
        return sub(string, 0, toIndex);
    }

    /**
     * 切割后部分
     *
     * @param string 字符串
     * @param fromIndex 切割开始的位置（包括）
     * @return 切割后的字符串
     */
    public static String subSuf(String string, int fromIndex) {
        if (isEmpty(string)) {
            return null;
        }
        return sub(string, fromIndex, string.length());
    }

    /**
     * 给定字符串是否被字符包围
     *
     * @param str 字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 是否包围，空串不包围
     */
    public static boolean isSurround(String str, String prefix, String suffix) {
        if (StringCommand.isBlank(str)) {
            return false;
        }
        if (str.length() < (prefix.length() + suffix.length())) {
            return false;
        }

        return str.startsWith(prefix) && str.endsWith(suffix);
    }

    /**
     * 给定字符串是否被字符包围
     *
     * @param str 字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 是否包围，空串不包围
     */
    public static boolean isSurround(CharSequence str, char prefix, char suffix) {
        if (StringCommand.isBlank(str)) {
            return false;
        }
        if (str.length() < 2) {
            return false;
        }

        return str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix;
    }

    /**
     * 重复某个字符
     *
     * @param c 被重复的字符
     * @param count 重复的数目
     * @return 重复字符字符串
     */
    public static String repeat(char c, int count) {
        char[] result = new char[count];
        for (int i = 0; i < count; i++) {
            result[i] = c;
        }
        return new String(result);
    }

    /**
     * 重复某个字符串
     *
     * @param str 被重复的字符
     * @param count 重复的数目
     * @return 重复字符字符串
     */
    public static String repeat(String str, int count) {

        // 检查
        final int len = str.length();
        final long longSize = (long) len * (long) count;
        final int size = (int) longSize;
        if (size != longSize) {
            throw new ArrayIndexOutOfBoundsException("Required String length is too large: " + longSize);
        }

        final char[] array = new char[size];
        str.getChars(0, len, array, 0);
        int n;
        for (n = len; n < size - n; n <<= 1) {
            System.arraycopy(array, 0, array, n, n);// n <<= 1相当于n *2
        }
        System.arraycopy(array, 0, array, n, size - n);
        return new String(array);
    }

    /**
     * 比较两个字符串（大小写敏感）。
     * <p>
     *
     * <pre>
     * equals(null, null)   = true
     * equals(null, &quot;abc&quot;)  = false
     * equals(&quot;abc&quot;, null)  = false
     * equals(&quot;abc&quot;, &quot;abc&quot;) = true
     * equals(&quot;abc&quot;, &quot;ABC&quot;) = false
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equals(str2);
    }

    /**
     * 比较两个字符串（大小写不敏感）。
     * <p>
     *
     * <pre>
     * equalsIgnoreCase(null, null)   = true
     * equalsIgnoreCase(null, &quot;abc&quot;)  = false
     * equalsIgnoreCase(&quot;abc&quot;, null)  = false
     * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
     * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equalsIgnoreCase(str2);
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params 参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object...params) {
        if (ArrayCommand.isEmpty(params) || isBlank(template)) {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /**
     * 有序的格式化文本，使用{number}做为占位符<br>
     * 例：<br>
     * 通常使用：format("this is {0} for {1}", "a", "b") -> this is a for b<br>
     *
     * @param pattern 文本格式
     * @param arguments 参数
     * @return 格式化后的文本
     */
    public static String indexedFormat(String pattern, Object...arguments) {
        return MessageFormat.format(pattern, arguments);
    }

    /**
     * 格式化文本，使用 {varName} 占位<br>
     * beanMapWithHashSetValue = {a: "aValue", b: "bValue"} format("{a} and {b}", beanMapWithHashSetValue) ----> aValue
     * and bValue
     *
     * @param template 文本模板，被替换的部分用 {key} 表示
     * @param map 参数值对
     * @return 格式化后的文本
     */
    public static String format(String template, Map<?, ?> map) {
        if (null == map || map.isEmpty()) {
            return template;
        }

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", utf8Str(entry.getValue()));
        }
        return template;
    }

    /**
     * 编码字符串，编码为UTF-8
     *
     * @param str 字符串
     * @return 编码后的字节码
     */
    public static byte[] utf8Bytes(String str) {
        return bytes(str, CharsetCommand.CHARSET_UTF_8);
    }

    /**
     * 编码字符串<br>
     * 使用系统默认编码
     *
     * @param str 字符串
     * @return 编码后的字节码
     */
    public static byte[] bytes(String str) {
        return bytes(str, Charset.defaultCharset());
    }

    /**
     * 编码字符串
     *
     * @param str 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 编码后的字节码
     */
    public static byte[] bytes(String str, String charset) {
        return bytes(str, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * 编码字符串
     *
     * @param str 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 编码后的字节码
     */
    public static byte[] bytes(String str, Charset charset) {
        if (str == null) {
            return null;
        }

        if (null == charset) {
            return str.getBytes();
        }
        return str.getBytes(charset);
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @return 字符串
     */
    public static String utf8Str(Object obj) {
        return str(obj, CharsetCommand.CHARSET_UTF_8);
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @param charsetName 字符集
     * @return 字符串
     */
    public static String str(Object obj, String charsetName) {
        return str(obj, Charset.forName(charsetName));
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @param charset 字符集
     * @return 字符串
     */
    public static String str(Object obj, Charset charset) {
        if (null == obj) {
            return null;
        }

        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof byte[]) {
            return str((byte[]) obj, charset);
        } else if (obj instanceof Byte[]) {
            return str((Byte[]) obj, charset);
        } else if (obj instanceof ByteBuffer) {
            return str((ByteBuffer) obj, charset);
        } else if (ArrayCommand.isArray(obj)) {
            return ArrayCommand.toString(obj);
        }

        return obj.toString();
    }

    /**
     * 将byte数组转为字符串
     *
     * @param bytes byte数组
     * @param charset 字符集
     * @return 字符串
     */
    public static String str(byte[] bytes, String charset) {
        return str(bytes, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * 解码字节码
     *
     * @param data 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }

        if (null == charset) {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     * 将Byte数组转为字符串
     *
     * @param bytes byte数组
     * @param charset 字符集
     * @return 字符串
     */
    public static String str(Byte[] bytes, String charset) {
        return str(bytes, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * 解码字节码
     *
     * @param data 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String str(Byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }

        byte[] bytes = new byte[data.length];
        Byte dataByte;
        for (int i = 0; i < data.length; i++) {
            dataByte = data[i];
            bytes[i] = (null == dataByte) ? -1 : dataByte.byteValue();
        }

        return str(bytes, charset);
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data 数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String str(ByteBuffer data, String charset) {
        if (data == null) {
            return null;
        }

        return str(data, Charset.forName(charset));
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data 数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String str(ByteBuffer data, Charset charset) {
        if (null == charset) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }

    /**
     * 字符串转换为byteBuffer
     *
     * @param str 字符串
     * @param charset 编码
     * @return byteBuffer
     */
    public static ByteBuffer byteBuffer(String str, String charset) {
        return ByteBuffer.wrap(StringCommand.bytes(str, charset));
    }

    /**
     * 以 conjunction 为分隔符将多个对象转换为字符串
     *
     * @param conjunction 分隔符
     * @param objs 数组
     * @return 连接后的字符串
     * @see ArrayCommand#join(Object[], String)
     */
    public static String join(String conjunction, Object...objs) {
        return ArrayCommand.join(objs, conjunction);
    }

    /**
     * 将驼峰式命名的字符串转换为下划线方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
     * 例如：HelloWorld->hello_world
     *
     * @param camelCaseStr 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public static String toUnderlineCase(String camelCaseStr) {
        if (camelCaseStr == null) {
            return null;
        }

        final int length = camelCaseStr.length();
        StringBuilder sb = new StringBuilder();
        char c;
        boolean isPreUpperCase = false;
        for (int i = 0; i < length; i++) {
            c = camelCaseStr.charAt(i);
            boolean isNextUpperCase = true;
            if (i < (length - 1)) {
                isNextUpperCase = Character.isUpperCase(camelCaseStr.charAt(i + 1));
            }
            if (Character.isUpperCase(c)) {
                if (!isPreUpperCase || !isNextUpperCase) {
                    if (i > 0) {
                        sb.append(UNDERLINE);
                    }
                }
                isPreUpperCase = true;
            } else {
                isPreUpperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * 将下划线方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
     * 例如：hello_world->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String toCamelCase(String name) {
        if (name == null) {
            return null;
        }
        if (name.contains(UNDERLINE)) {
            name = name.toLowerCase();

            StringBuilder sb = new StringBuilder(name.length());
            boolean upperCase = false;
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);

                if (c == '_') {
                    upperCase = true;
                } else if (upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else {
            return name;
        }
    }

    /**
     * 包装指定字符串
     *
     * @param str 被包装的字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 包装后的字符串
     */
    public static String wrap(String str, String prefix, String suffix) {
        return format("{}{}{}", prefix, str, suffix);
    }

    /**
     * 指定字符串是否被包装
     *
     * @param str 字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 是否被包装
     */
    public static boolean isWrap(String str, String prefix, String suffix) {
        return str.startsWith(prefix) && str.endsWith(suffix);
    }

    /**
     * 指定字符串是否被同一字符包装（前后都有这些字符串）
     *
     * @param str 字符串
     * @param wrapper 包装字符串
     * @return 是否被包装
     */
    public static boolean isWrap(String str, String wrapper) {
        return isWrap(str, wrapper, wrapper);
    }

    /**
     * 指定字符串是否被同一字符包装（前后都有这些字符串）
     *
     * @param str 字符串
     * @param wrapper 包装字符
     * @return 是否被包装
     */
    public static boolean isWrap(String str, char wrapper) {
        return isWrap(str, wrapper, wrapper);
    }

    /**
     * 指定字符串是否被包装
     *
     * @param str 字符串
     * @param prefixChar 前缀
     * @param suffixChar 后缀
     * @return 是否被包装
     */
    public static boolean isWrap(String str, char prefixChar, char suffixChar) {
        return str.charAt(0) == prefixChar && str.charAt(str.length() - 1) == suffixChar;
    }

    /**
     * 补充字符串以满足最小长度 StrUtil.padPre("1", 3, '0');//"001"
     *
     * @param str 字符串
     * @param minLength 最小长度
     * @param padChar 补充的字符
     * @return 补充后的字符串
     */
    public static String padPre(String str, int minLength, char padChar) {
        if (str.length() >= minLength) {
            return str;
        }
        StringBuilder sb = new StringBuilder(minLength);
        for (int i = str.length(); i < minLength; i++) {
            sb.append(padChar);
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 补充字符串以满足最小长度 StrUtil.padEnd("1", 3, '0');//"100"
     *
     * @param str 字符串
     * @param minLength 最小长度
     * @param padChar 补充的字符
     * @return 补充后的字符串
     */
    public static String padEnd(String str, int minLength, char padChar) {
        if (str.length() >= minLength) {
            return str;
        }
        StringBuilder sb = new StringBuilder(minLength);
        sb.append(str);
        for (int i = str.length(); i < minLength; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder对象
     */
    public static StringBuilder builder() {
        return new StringBuilder();
    }

    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder对象
     */
    public static StringBuilder builder(int capacity) {
        return new StringBuilder(capacity);
    }

    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder对象
     */
    public static StringBuilder builder(String...strs) {
        final StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            sb.append(str);
        }
        return sb;
    }

    /**
     * 获得StringReader
     *
     * @param str 字符串
     * @return StringReader
     */
    public static StringReader getReader(String str) {
        return new StringReader(str);
    }

    /**
     * 获得StringWriter
     *
     * @return StringWriter
     */
    public static StringWriter getWriter() {
        return new StringWriter();
    }

    /**
     * 统计指定内容中包含指定字符串的数量<br>
     * 参数为 {@code null} 或者 "" 返回 {@code 0}.
     * <p>
     *
     * <pre>
     * StrUtil.count(null, *)       = 0
     * StrUtil.count("", *)         = 0
     * StrUtil.count("abba", null)  = 0
     * StrUtil.count("abba", "")    = 0
     * StrUtil.count("abba", "a")   = 2
     * StrUtil.count("abba", "ab")  = 1
     * StrUtil.count("abba", "xxx") = 0
     * </pre>
     *
     * @param content 被查找的字符串
     * @param strForSearch 需要查找的字符串
     * @return 查找到的个数
     */
    public static int count(final String content, final String strForSearch) {
        if (hasEmpty(content, strForSearch) || strForSearch.length() > content.length()) {
            return 0;
        }

        int count = 0;
        int idx = 0;
        while ((idx = content.indexOf(strForSearch, idx)) > -1) {
            count++;
            idx += strForSearch.length();
        }
        return count;
    }

    /**
     * 统计指定内容中包含指定字符的数量
     *
     * @param content 内容
     * @param charForSearch 被统计的字符
     * @return 包含数量
     */
    public static int count(String content, char charForSearch) {
        int count = 0;
        if (isEmpty(content)) {
            return 0;
        }
        int contentLength = content.length();
        for (int i = 0; i < contentLength; i++) {
            if (charForSearch == content.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Ensures that <code>s</code> is friendly for a URL or file system.
     *
     * @param s String to be sanitized.
     * @return sanitized version of <code>s</code>.
     * @throws NullPointerException if <code>s</code> is <code>null</code>.
     */
    public static String sanitize(String s) {
        return s
                .replace(':', '-')
                .replace('_', '-')
                .replace('.', '-')
                .replace('/', '-')
                .replace('\\', '-');
    }

    /**
     * Counts the number of times the given char is in the string
     *
     * @param s the string
     * @param ch the char
     * @return number of times char is located in the string
     */
    public static int countChar(String s, char ch) {
        if (ObjectCommand.isEmpty(s)) {
            return 0;
        }

        int matches = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (ch == c) {
                matches++;
            }
        }

        return matches;
    }

    /**
     * Limits the length of a string
     *
     * @param s the string
     * @param maxLength the maximum length of the returned string
     * @return s if the length of s is less than maxLength or the first maxLength characters of s
     * @deprecated use {@link #limitLength(String, int)}
     */
    @Deprecated
    public static String limitLenght(String s, int maxLength) {
        return limitLength(s, maxLength);
    }

    /**
     * Limits the length of a string
     *
     * @param s the string
     * @param maxLength the maximum length of the returned string
     * @return s if the length of s is less than maxLength or the first maxLength characters of s
     */
    public static String limitLength(String s, int maxLength) {
        if (ObjectCommand.isEmpty(s)) {
            return s;
        }
        return s.length() <= maxLength ? s : s.substring(0, maxLength);
    }

    /**
     * Removes all quotes (single and double) from the string
     *
     * @param s the string
     * @return the string without quotes (single and double)
     */
    public static String removeQuotes(String s) {
        if (ObjectCommand.isEmpty(s)) {
            return s;
        }

        s = replaceAll(s, "'", "");
        s = replaceAll(s, "\"", "");
        return s;
    }

    /**
     * Removes all leading and ending quotes (single and double) from the string
     *
     * @param s the string
     * @return the string without leading and ending quotes (single and double)
     */
    public static String removeLeadingAndEndingQuotes(String s) {
        if (ObjectCommand.isEmpty(s)) {
            return s;
        }

        String copy = s.trim();
        if (copy.startsWith("'") && copy.endsWith("'")) {
            return copy.substring(1, copy.length() - 1);
        }
        if (copy.startsWith("\"") && copy.endsWith("\"")) {
            return copy.substring(1, copy.length() - 1);
        }

        // no quotes, so return as-is
        return s;
    }

    /**
     * Whether the string starts and ends with either single or double quotes.
     *
     * @param s the string
     * @return <tt>true</tt> if the string starts and ends with either single or double quotes.
     */
    public static boolean isQuoted(String s) {
        if (ObjectCommand.isEmpty(s)) {
            return false;
        }

        if (s.startsWith("'") && s.endsWith("'")) {
            return true;
        }
        if (s.startsWith("\"") && s.endsWith("\"")) {
            return true;
        }

        return false;
    }

    /**
     * Encodes the text into safe XML by replacing < > and & with XML tokens
     *
     * @param text the text
     * @return the encoded text
     */
    public static String xmlEncode(String text) {
        if (text == null) {
            return "";
        }
        // must replace amp first, so we dont replace &lt; to amp later
        text = replaceAll(text, "&", "&amp;");
        text = replaceAll(text, "\"", "&quot;");
        text = replaceAll(text, "<", "&lt;");
        text = replaceAll(text, ">", "&gt;");
        return text;
    }

    /**
     * Determines if the string has at least one letter in upper case
     *
     * @param text the text
     * @return <tt>true</tt> if at least one letter is upper case, <tt>false</tt> otherwise
     */
    public static boolean hasUpperCase(String text) {
        if (text == null) {
            return false;
        }

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the string is a fully qualified class name
     */
    public static boolean isClassName(String text) {
        boolean result = false;
        if (text != null) {
            String[] split = text.split("\\.");
            if (split.length > 0) {
                String lastToken = split[split.length - 1];
                if (lastToken.length() > 0) {
                    result = Character.isUpperCase(lastToken.charAt(0));
                }
            }
        }
        return result;
    }

    /**
     * Does the expression have the language start token?
     *
     * @param expression the expression
     * @param language the name of the language, such as simple
     * @return <tt>true</tt> if the expression contains the start token, <tt>false</tt> otherwise
     */
    public static boolean hasStartToken(String expression, String language) {
        if (expression == null) {
            return false;
        }

        // for the simple language the expression start token could be "${"
        if ("simple".equalsIgnoreCase(language) && expression.contains("${")) {
            return true;
        }

        if (language != null && expression.contains("$" + language + "{")) {
            return true;
        }

        return false;
    }

    /**
     * Replaces all the from tokens in the given input string.
     * <p/>
     * This implementation is not recursive, not does it check for tokens in the replacement string.
     *
     * @param input the input string
     * @param from the from string, must <b>not</b> be <tt>null</tt> or empty
     * @param to the replacement string, must <b>not</b> be empty
     * @return the replaced string, or the input string if no replacement was needed
     * @throws IllegalArgumentException if the input arguments is invalid
     */
    public static String replaceAll(String input, String from, String to) {
        if (ObjectCommand.isEmpty(input)) {
            return input;
        }
        if (from == null) {
            throw new IllegalArgumentException("from cannot be null");
        }
        if (to == null) {
            // to can be empty, so only check for null
            throw new IllegalArgumentException("to cannot be null");
        }

        // fast check if there is any from at all
        if (!input.contains(from)) {
            return input;
        }

        final int len = from.length();
        final int max = input.length();
        StringBuilder sb = new StringBuilder(max);
        for (int i = 0; i < max;) {
            if (i + len <= max) {
                String token = input.substring(i, i + len);
                if (from.equals(token)) {
                    sb.append(to);
                    // fast forward
                    i = i + len;
                    continue;
                }
            }

            // append single char
            sb.append(input.charAt(i));
            // forward to next
            i++;
        }
        return sb.toString();
    }

    /**
     * Creates a json tuple with the given name/value pair.
     *
     * @param name the name
     * @param value the value
     * @param isMap whether the tuple should be map
     * @return the json
     */
    public static String toJson(String name, String value, boolean isMap) {
        if (isMap) {
            return "{ " + doubleQuote(name) + ": " + doubleQuote(value) + " }";
        } else {
            return doubleQuote(name) + ": " + doubleQuote(value);
        }
    }

    /**
     * Returns the text wrapped double quotes
     */
    public static String doubleQuote(String text) {
        return quote(text, "\"");
    }

    /**
     * Returns the text wrapped single quotes
     */
    public static String singleQuote(String text) {
        return quote(text, "'");
    }

    /**
     * Wraps the text in the given quote text
     *
     * @param text the text to wrap in quotes
     * @param quote the quote text added to the prefix and postfix of the text
     * @return the text wrapped in the given quotes
     */
    public static String quote(String text, String quote) {
        return quote + text + quote;
    }

    /**
     * Splits the input safely honoring if values is enclosed in quotes.
     * <p/>
     * Though this method does not support double quoting values. A quoted value must start with the same start and
     * ending quote, which is either a single quote or double quote value.
     * <p/>
     * Will <i>trim</i> each splitted value by default.
     *
     * @param input the input
     * @param separator the separator char to split the input, for example a comma.
     * @return the input splitted, or <tt>null</tt> if the input is null.
     */
    public static String[] splitSafeQuote(String input, char separator) {
        return splitSafeQuote(input, separator, true);
    }

    /**
     * Splits the input safely honoring if values is enclosed in quotes.
     * <p/>
     * Though this method does not support double quoting values. A quoted value must start with the same start and
     * ending quote, which is either a single quote or double quote value. \
     *
     * @param input the input
     * @param separator the separator char to split the input, for example a comma.
     * @param trim whether to trim each splitted value
     * @return the input splitted, or <tt>null</tt> if the input is null.
     */
    public static String[] splitSafeQuote(String input, char separator, boolean trim) {
        if (input == null) {
            return null;
        }

        if (input.indexOf(separator) == -1) {
            // no separator in data, so return single string with input as is
            return new String[] { trim ? input.trim() : input };
        }

        List<String> answer = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        boolean singleQuoted = false;
        boolean doubleQuoted = false;
        boolean skipLeadingWhitespace = true;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            char prev = i > 0 ? input.charAt(i - 1) : 0;
            boolean isQuoting = singleQuoted || doubleQuoted;

            if (!doubleQuoted && ch == '\'') {
                if (singleQuoted && prev == ch && sb.length() == 0) {
                    // its an empty quote so add empty text
                    answer.add("");
                }
                // special logic needed if this quote is the end
                if (i == input.length() - 1) {
                    if (singleQuoted && sb.length() > 0) {
                        String text = sb.toString();
                        // do not trim a quoted string
                        answer.add(text);
                        sb.setLength(0);
                    }
                }
                singleQuoted = !singleQuoted;
                continue;
            } else if (!singleQuoted && ch == '"') {
                if (doubleQuoted && prev == ch && sb.length() == 0) {
                    // its an empty quote so add empty text
                    answer.add("");
                }
                // special logic needed if this quote is the end
                if (i == input.length() - 1) {
                    if (doubleQuoted && sb.length() > 0) {
                        String text = sb.toString();
                        // do not trim a quoted string
                        answer.add(text);
                        sb.setLength(0);
                    }
                }
                doubleQuoted = !doubleQuoted;
                continue;
            } else if (!isQuoting && ch == ' ') {
                if (skipLeadingWhitespace) {
                    continue;
                }
            } else if (!isQuoting && ch == separator) {
                // add as answer if we are not in a quote
                if (sb.length() > 0) {
                    String text = sb.toString();
                    if (trim) {
                        text = text.trim();
                    }
                    answer.add(text);
                    sb.setLength(0);
                }
                // we should avoid adding the separator
                continue;
            }

            sb.append(ch);
        }

        // any leftover
        if (sb.length() > 0) {
            String text = sb.toString();
            if (trim) {
                text = text.trim();
            }
            answer.add(text);
        }

        return answer.toArray(new String[answer.size()]);
    }

    /**
     * Asserts whether the string is <b>not</b> empty.
     *
     * @param value the string to test
     * @param name the key that resolved the value
     * @return the passed {@code value} as is
     * @throws IllegalArgumentException is thrown if assertion fails
     */
    public static String notEmpty(String value, String name) {
        if (ObjectCommand.isEmpty(value)) {
            throw new IllegalArgumentException(name + " must be specified and not empty");
        }

        return value;
    }

    /**
     * Asserts whether the string is <b>not</b> empty.
     *
     * @param value the string to test
     * @param on additional description to indicate where this problem occurred (appended as toString())
     * @param name the key that resolved the value
     * @return the passed {@code value} as is
     * @throws IllegalArgumentException is thrown if assertion fails
     */
    public static String notEmpty(String value, String name, Object on) {
        if (on == null) {
            ObjectCommand.notNull(value, name);
        } else if (ObjectCommand.isEmpty(value)) {
            throw new IllegalArgumentException(name + " must be specified and not empty on: " + on);
        }

        return value;
    }

    public static String[] splitOnCharacter(String value, String needle, int count) {
        String[] rc = new String[count];
        rc[0] = value;
        for (int i = 1; i < count; i++) {
            String v = rc[i - 1];
            int p = v.indexOf(needle);
            if (p < 0) {
                return rc;
            }
            rc[i - 1] = v.substring(0, p);
            rc[i] = v.substring(p + 1);
        }
        return rc;
    }

    /**
     * Removes any starting characters on the given text which match the given character
     *
     * @param text the string
     * @param ch the initial characters to remove
     * @return either the original string or the new substring
     */
    public static String removeStartingCharacters(String text, char ch) {
        int idx = 0;
        while (text.charAt(idx) == ch) {
            idx++;
        }
        if (idx > 0) {
            return text.substring(idx);
        }
        return text;
    }

    /**
     * Capitalize the string (upper case first character)
     *
     * @param text the string
     * @return the string capitalized (upper case first character)
     */
    public static String capitalize(String text) {
        return capitalize(text, false);
    }

    /**
     * Capitalize the string (upper case first character)
     *
     * @param text the string
     * @param dashToCamelCase whether to also convert dash format into camel case (hello-great-world -> helloGreatWorld)
     * @return the string capitalized (upper case first character)
     */
    public static String capitalize(String text, boolean dashToCamelCase) {
        if (dashToCamelCase) {
            text = dashToCamelCase(text);
        }
        if (text == null) {
            return null;
        }
        int length = text.length();
        if (length == 0) {
            return text;
        }
        String answer = text.substring(0, 1).toUpperCase(Locale.ENGLISH);
        if (length > 1) {
            answer += text.substring(1, length);
        }
        return answer;
    }

    /**
     * Converts the string from dash format into camel case (hello-great-world -> helloGreatWorld)
     *
     * @param text the string
     * @return the string camel cased
     */
    public static String dashToCamelCase(String text) {
        if (text == null) {
            return null;
        }
        int length = text.length();
        if (length == 0) {
            return text;
        }
        if (text.indexOf('-') == -1) {
            return text;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '-') {
                i++;
                sb.append(Character.toUpperCase(text.charAt(i)));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Returns the string after the given token
     *
     * @param text the text
     * @param after the token
     * @return the text after the token, or <tt>null</tt> if text does not contain the token
     */
    public static String after(String text, String after) {
        if (!text.contains(after)) {
            return null;
        }
        return text.substring(text.indexOf(after) + after.length());
    }

    /**
     * Returns an object after the given token
     *
     * @param text the text
     * @param after the token
     * @param mapper a mapping function to convert the string after the token to type T
     * @return an Optional describing the result of applying a mapping function to the text after the token.
     */
    public static <T> Optional<T> after(String text, String after, Function<String, T> mapper) {
        String result = after(text, after);
        if (result == null) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(result));
        }
    }

    /**
     * Returns the string before the given token
     *
     * @param text the text
     * @param before the token
     * @return the text before the token, or <tt>null</tt> if text does not contain the token
     */
    public static String before(String text, String before) {
        if (!text.contains(before)) {
            return null;
        }
        return text.substring(0, text.indexOf(before));
    }

    /**
     * Returns an object before the given token
     *
     * @param text the text
     * @param before the token
     * @param mapper a mapping function to convert the string before the token to type T
     * @return an Optional describing the result of applying a mapping function to the text before the token.
     */
    public static <T> Optional<T> before(String text, String before, Function<String, T> mapper) {
        String result = before(text, before);
        if (result == null) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(result));
        }
    }

    /**
     * Returns the string between the given tokens
     *
     * @param text the text
     * @param after the before token
     * @param before the after token
     * @return the text between the tokens, or <tt>null</tt> if text does not contain the tokens
     */
    public static String between(String text, String after, String before) {
        text = after(text, after);
        if (text == null) {
            return null;
        }
        return before(text, before);
    }

    /**
     * Returns an object between the given token
     *
     * @param text the text
     * @param after the before token
     * @param before the after token
     * @param mapper a mapping function to convert the string between the token to type T
     * @return an Optional describing the result of applying a mapping function to the text between the token.
     */
    public static <T> Optional<T> between(String text, String after, String before, Function<String, T> mapper) {
        String result = between(text, after, before);
        if (result == null) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(result));
        }
    }

    /**
     * Returns the string between the most outer pair of tokens
     * <p/>
     * The number of token pairs must be evenly, eg there must be same number of before and after tokens, otherwise
     * <tt>null</tt> is returned
     * <p/>
     * This implementation skips matching when the text is either single or double quoted. For example:
     * <tt>${body.matches("foo('bar')")</tt> Will not match the parenthesis from the quoted text.
     *
     * @param text the text
     * @param after the before token
     * @param before the after token
     * @return the text between the outer most tokens, or <tt>null</tt> if text does not contain the tokens
     */
    public static String betweenOuterPair(String text, char before, char after) {
        if (text == null) {
            return null;
        }

        int pos = -1;
        int pos2 = -1;
        int count = 0;
        int count2 = 0;

        boolean singleQuoted = false;
        boolean doubleQuoted = false;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (!doubleQuoted && ch == '\'') {
                singleQuoted = !singleQuoted;
            } else if (!singleQuoted && ch == '\"') {
                doubleQuoted = !doubleQuoted;
            }
            if (singleQuoted || doubleQuoted) {
                continue;
            }

            if (ch == before) {
                count++;
            } else if (ch == after) {
                count2++;
            }

            if (ch == before && pos == -1) {
                pos = i;
            } else if (ch == after) {
                pos2 = i;
            }
        }

        if (pos == -1 || pos2 == -1) {
            return null;
        }

        // must be even paris
        if (count != count2) {
            return null;
        }

        return text.substring(pos + 1, pos2);
    }

    /**
     * Returns an object between the most outer pair of tokens
     *
     * @param text the text
     * @param after the before token
     * @param before the after token
     * @param mapper a mapping function to convert the string between the most outer pair of tokens to type T
     * @return an Optional describing the result of applying a mapping function to the text between the most outer pair
     *         of tokens.
     */
    public static <T> Optional<T> betweenOuterPair(String text, char before, char after, Function<String, T> mapper) {
        String result = betweenOuterPair(text, before, after);
        if (result == null) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(result));
        }
    }

    /**
     * Returns true if the given name is a valid java identifier
     */
    public static boolean isJavaIdentifier(String name) {
        if (name == null) {
            return false;
        }
        int size = name.length();
        if (size < 1) {
            return false;
        }
        if (Character.isJavaIdentifierStart(name.charAt(0))) {
            for (int i = 1; i < size; i++) {
                if (!Character.isJavaIdentifierPart(name.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Cleans the string to a pure Java identifier so we can use it for loading class names.
     * <p/>
     * Especially from Spring DSL people can have \n \t or other characters that otherwise would result in
     * ClassNotFoundException
     *
     * @param name the class name
     * @return normalized classname that can be load by a class loader.
     */
    public static String normalizeClassName(String name) {
        StringBuilder sb = new StringBuilder(name.length());
        for (char ch : name.toCharArray()) {
            if (ch == '.' || ch == '[' || ch == ']' || ch == '-' || Character.isJavaIdentifierPart(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * Compares old and new text content and report back which lines are changed
     *
     * @param oldText the old text
     * @param newText the new text
     * @return a list of line numbers that are changed in the new text
     */
    public static List<Integer> changedLines(String oldText, String newText) {
        if (oldText == null || oldText.equals(newText)) {
            return Collections.emptyList();
        }

        List<Integer> changed = new ArrayList<>();

        String[] oldLines = oldText.split("\n");
        String[] newLines = newText.split("\n");

        for (int i = 0; i < newLines.length; i++) {
            String newLine = newLines[i];
            String oldLine = i < oldLines.length ? oldLines[i] : null;
            if (oldLine == null) {
                changed.add(i);
            } else if (!newLine.equals(oldLine)) {
                changed.add(i);
            }
        }

        return changed;
    }

    /**
     * Removes the leading and trailing whitespace and if the resulting string is empty returns {@code null}. Examples:
     * <p>
     * Examples: <blockquote>
     * 
     * <pre>
     * trimToNull("abc") -> "abc"
     * trimToNull(" abc") -> "abc"
     * trimToNull(" abc ") -> "abc"
     * trimToNull(" ") -> null
     * trimToNull("") -> null
     * </pre>
     * 
     * </blockquote>
     */
    public static String trimToNull(final String given) {
        if (given == null) {
            return null;
        }

        final String trimmed = given.trim();

        if (trimmed.isEmpty()) {
            return null;
        }

        return trimmed;
    }

    /**
     * Checks if the src string contains what 是否包含特定字符，忽略大小写，如果给定两个参数都为<code>null</code>，返回true
     * 
     * @param src is the source string to be checked
     * @param what is the string which will be looked up in the src argument
     * @return true/false
     */
    public static boolean containsIgnoreCase(String src, String what) {
        if (src == null || what == null) {
            return false;
        }

        final int length = what.length();
        if (length == 0) {
            return true; // Empty string is contained
        }

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp) {
                continue;
            }

            if (src.regionMatches(true, i, what, 0, length)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Outputs the bytes in human readable format in units of KB,MB,GB etc.
     *
     * @param locale The locale to apply during formatting. If l is {@code null} then no localization is applied.
     * @param bytes number of bytes
     * @return human readable output
     * @see java.lang.String#format(Locale, String, Object...)
     */
    public static String humanReadableBytes(Locale locale, long bytes) {
        int unit = 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format(locale, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * Outputs the bytes in human readable format in units of KB,MB,GB etc.
     *
     * The locale always used is the one returned by {@link java.util.Locale#getDefault()}.
     *
     * @param bytes number of bytes
     * @return human readable output
     * @sees org.apache.camel.util.StringHelper#humanReadableBytes(Locale, long)
     */
    public static String humanReadableBytes(long bytes) {
        return humanReadableBytes(Locale.getDefault(), bytes);
    }

    /**
     * Check for string pattern matching with a number of strategies in the following order:
     *
     * - equals - null pattern always matches - * always matches - Ant style matching - Regexp
     *
     * @param patter the pattern
     * @param target the string to test
     * @return true if target matches the pattern
     */
    public static boolean matches(String patter, String target) {
        if (Objects.equals(patter, target)) {
            return true;
        }

        if (Objects.isNull(patter)) {
            return true;
        }

        if (Objects.equals("*", patter)) {
            return true;
        }

        if (AntPathMatcher.INSTANCE.match(patter, target)) {
            return true;
        }

        Pattern p = Pattern.compile(patter);
        Matcher m = p.matcher(target);

        return m.matches();
    }

    /**
     * Parse {@ value} to given type {@link T}
     *
     * @param <T> Class of return type. can be int/Integer, long/Long, boolean/Boolean and String
     * @param clazz Class of return type.
     * @param value The input value.
     * @param defaultValue default value.
     * @return the converted value, or defaultValue if {@ value} is <code>null</code>, or it's not a well formated
     *         value.
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
                return (start == 0 && i == src.length() - 1) ? src
                        : replaceLastWithDots(src, start, i + 1, replaceLastWithDots);
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
        if (c <= 0xd7ff) {
            // excluding the surrogate area
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
        int idx = 0;
        int len = str.length();
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
    public static String join(boolean appendStart, boolean appendEnd, String seperator, String...array) {
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
     * @return "k:v:v,k2:v2" -> {{k, v:v}, {k2, v2}}
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

    public static String[] array(String string) {
        String[] strings = null;
        if (string != null && string.startsWith(ConstEnumStringValue.LeftBracket.getValue())
                && string.endsWith(ConstEnumStringValue.RightBracket.getValue())) {
            try {
                strings = JsonCommand.fromJson(string, String[].class);
            } catch (Exception e) {
                strings = new String[1];
                string = string.replace(ConstEnumStringValue.LeftBracket.getValue(), "");
                string = string.replace(ConstEnumStringValue.RightBracket.getValue(), "");
                strings[0] = string;
            }
        } else {
            strings = new String[1];
            strings[0] = string;
        }
        return strings;
    }

    public static String string(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
