package org.gjgr.pig.chivalrous.core.lang.replacer;

import org.apache.commons.lang3.Validate;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
