package org.gjgr.pig.chivalrous.core.xml;

import java.util.Iterator;

import org.gjgr.pig.chivalrous.core.json.InternalJsonUtil;
import org.gjgr.pig.chivalrous.core.json.JsonException;
import org.gjgr.pig.chivalrous.core.json.bean.JsonArray;
import org.gjgr.pig.chivalrous.core.json.bean.JsonObject;

/**
 * 提供静态方法在XML和JSONObject之间转换
 *
 * @author Json.org
 */
public class XmlBetweenJsonObject {

    /**
     * The Character '&amp;'.
     */
    public static final Character AMP = '&';

    /**
     * The Character '''.
     */
    public static final Character APOS = '\'';

    /**
     * The Character '!'.
     */
    public static final Character BANG = '!';

    /**
     * The Character '='.
     */
    public static final Character EQ = '=';

    /**
     * The Character '>'.
     */
    public static final Character GT = '>';

    /**
     * The Character '&lt;'.
     */
    public static final Character LT = '<';

    /**
     * The Character '?'.
     */
    public static final Character QUEST = '?';

    /**
     * The Character '"'.
     */
    public static final Character QUOT = '"';

    /**
     * The Character '/'.
     */
    public static final Character SLASH = '/';

    /**
     * Replace special characters with XmlBetweenJsonObject escapes:
     * <p>
     * 
     * <pre>
     * &amp; <small>(ampersand)</small> is replaced by &amp;amp;
     * &lt; <small>(less than)</small> is replaced by &amp;lt;
     * &gt; <small>(greater than)</small> is replaced by &amp;gt;
     * &quot; <small>(double quote)</small> is replaced by &amp;quot;
     * </pre>
     *
     * @param string The string to be escaped.
     * @return The escaped string.
     */
    public static String escape(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        for (int i = 0, length = string.length(); i < length; i++) {
            char c = string.charAt(i);
            switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Throw an exception if the string contains whitespace. Whitespace is not allowed in tagNames and attributes.
     *
     * @param string A string.
     * @throws JsonException Thrown if the string contains whitespace or is empty.
     */
    public static void noSpace(String string) throws JsonException {
        int i;
        int length = string.length();
        if (length == 0) {
            throw new JsonException("Empty string.");
        }
        for (i = 0; i < length; i += 1) {
            if (Character.isWhitespace(string.charAt(i))) {
                throw new JsonException("'" + string + "' contains a space character.");
            }
        }
    }

    /**
     * Scan the content following the named tag, attaching it to the context.
     *
     * @param x The XmlTokener containing the source string.
     * @param context The JsonObject that will include the new material.
     * @param name The tag name.
     * @return true if the close tag is processed.
     * @throws JsonException
     */
    private static boolean parse(XmlTokener x, JsonObject context, String name, boolean keepStrings)
            throws JsonException {
        char c;
        int i;
        JsonObject jsonobject = null;
        String string;
        String tagName;
        Object token;

        // Test for and skip past these forms:
        // <!-- ... -->
        // <! ... >
        // <![ ... ]]>
        // <? ... ?>
        // Report errors for these forms:
        // <>
        // <=
        // <<

        token = x.nextToken();

        // <!

        if (token == BANG) {
            c = x.next();
            if (c == '-') {
                if (x.next() == '-') {
                    x.skipPast("-->");
                    return false;
                }
                x.back();
            } else if (c == '[') {
                token = x.nextToken();
                if ("CDATA".equals(token)) {
                    if (x.next() == '[') {
                        string = x.nextCDATA();
                        if (string.length() > 0) {
                            context.accumulate("content", string);
                        }
                        return false;
                    }
                }
                throw x.syntaxError("Expected 'CDATA['");
            }
            i = 1;
            do {
                token = x.nextMeta();
                if (token == null) {
                    throw x.syntaxError("Missing '>' after '<!'.");
                } else if (token == LT) {
                    i += 1;
                } else if (token == GT) {
                    i -= 1;
                }
            } while (i > 0);
            return false;
        } else if (token == QUEST) {

            // <?
            x.skipPast("?>");
            return false;
        } else if (token == SLASH) {

            // Close tag </

            token = x.nextToken();
            if (name == null) {
                throw x.syntaxError("Mismatched close tag " + token);
            }
            if (!token.equals(name)) {
                throw x.syntaxError("Mismatched " + name + " and " + token);
            }
            if (x.nextToken() != GT) {
                throw x.syntaxError("Misshaped close tag");
            }
            return true;

        } else if (token instanceof Character) {
            throw x.syntaxError("Misshaped tag");

            // Open tag <

        } else {
            tagName = (String) token;
            token = null;
            jsonobject = new JsonObject();
            for (;;) {
                if (token == null) {
                    token = x.nextToken();
                }

                // attribute = value
                if (token instanceof String) {
                    string = (String) token;
                    token = x.nextToken();
                    if (token == EQ) {
                        token = x.nextToken();
                        if (!(token instanceof String)) {
                            throw x.syntaxError("Missing value");
                        }
                        jsonobject.accumulate(string,
                                keepStrings ? token : InternalJsonUtil.stringToValue((String) token));
                        token = null;
                    } else {
                        jsonobject.accumulate(string, "");
                    }

                } else if (token == SLASH) {
                    // Empty tag <.../>
                    if (x.nextToken() != GT) {
                        throw x.syntaxError("Misshaped tag");
                    }
                    if (jsonobject.size() > 0) {
                        context.accumulate(tagName, jsonobject);
                    } else {
                        context.accumulate(tagName, "");
                    }
                    return false;

                } else if (token == GT) {
                    // Content, between <...> and </...>
                    for (;;) {
                        token = x.nextContent();
                        if (token == null) {
                            if (tagName != null) {
                                throw x.syntaxError("Unclosed tag " + tagName);
                            }
                            return false;
                        } else if (token instanceof String) {
                            string = (String) token;
                            if (string.length() > 0) {
                                jsonobject.accumulate("content",
                                        keepStrings ? token : InternalJsonUtil.stringToValue(string));
                            }

                        } else if (token == LT) {
                            // Nested element
                            if (parse(x, jsonobject, tagName, keepStrings)) {
                                if (jsonobject.size() == 0) {
                                    context.accumulate(tagName, "");
                                } else if (jsonobject.size() == 1 && jsonobject.get("content") != null) {
                                    context.accumulate(tagName, jsonobject.get("content"));
                                } else {
                                    context.accumulate(tagName, jsonobject);
                                }
                                return false;
                            }
                        }
                    }
                } else {
                    throw x.syntaxError("Misshaped tag");
                }
            }
        }
    }

    /**
     * Convert a well-formed (but not necessarily valid) XmlBetweenJsonObject string into a JsonObject. Some information
     * may be lost in this transformation because Json is a data format and XmlBetweenJsonObject is a document format.
     * XmlBetweenJsonObject uses elements, attributes, and content text, while Json uses unordered collections of
     * name/value pairs and arrays of values. Json does not does not like to distinguish between elements and
     * attributes. Sequences of similar elements are represented as JSONArrays. Content text may be placed in a
     * "content" member. Comments, prologs, DTDs, and <code>&lt;[ [ ]]></code> are ignored.
     *
     * @param string The source string.
     * @return A JsonObject containing the structured data from the XmlBetweenJsonObject string.
     * @throws JsonException Thrown if there is an errors while parsing the string
     */
    public static JsonObject toJSONObject(String string) throws JsonException {
        return toJSONObject(string, false);
    }

    /**
     * Convert a well-formed (but not necessarily valid) XmlBetweenJsonObject string into a JsonObject. Some information
     * may be lost in this transformation because Json is a data format and XmlBetweenJsonObject is a document format.
     * XmlBetweenJsonObject uses elements, attributes, and content text, while Json uses unordered collections of
     * name/value pairs and arrays of values. Json does not does not like to distinguish between elements and
     * attributes. Sequences of similar elements are represented as JSONArrays. Content text may be placed in a
     * "content" member. Comments, prologs, DTDs, and <code>&lt;[ [ ]]></code> are ignored.
     * <p>
     * All values are converted as strings, for 1, 01, 29.0 will not be coerced to numbers but will instead be the exact
     * value as seen in the XmlBetweenJsonObject document.
     *
     * @param string The source string.
     * @param keepStrings If true, then values will not be coerced into boolean or numeric values and will instead be
     *            left as strings
     * @return A JsonObject containing the structured data from the XmlBetweenJsonObject string.
     * @throws JsonException Thrown if there is an errors while parsing the string
     */
    public static JsonObject toJSONObject(String string, boolean keepStrings) throws JsonException {
        JsonObject jo = new JsonObject();
        XmlTokener x = new XmlTokener(string);
        while (x.more() && x.skipPast("<")) {
            parse(x, jo, null, keepStrings);
        }
        return jo;
    }

    /**
     * Convert a JsonObject into a well-formed, element-normal XmlBetweenJsonObject string.
     *
     * @param object A JsonObject.
     * @return A string.
     * @throws JsonException Thrown if there is an error parsing the string
     */
    public static String toString(Object object) throws JsonException {
        return toString(object, null);
    }

    /**
     * Convert a JsonObject into a well-formed, element-normal XmlBetweenJsonObject string.
     *
     * @param object A JsonObject.
     * @param tagName The optional name of the enclosing tag.
     * @return A string.
     * @throws JsonException Thrown if there is an error parsing the string
     */
    public static String toString(Object object, String tagName) throws JsonException {
        StringBuilder sb = new StringBuilder();
        JsonArray ja;
        JsonObject jo;
        String key;
        Iterator<String> keys;
        String string;
        Object value;

        if (object instanceof JsonObject) {

            // Emit <tagName>
            if (tagName != null) {
                sb.append('<');
                sb.append(tagName);
                sb.append('>');
            }

            // Loop thru the keys.
            jo = (JsonObject) object;
            keys = jo.keySet().iterator();
            while (keys.hasNext()) {
                key = keys.next();
                value = jo.get(key);
                if (value == null) {
                    value = "";
                } else if (value.getClass().isArray()) {
                    value = new JsonArray(value);
                }
                string = value instanceof String ? (String) value : null;

                // Emit content in body
                if ("content".equals(key)) {
                    if (value instanceof JsonArray) {
                        ja = (JsonArray) value;
                        int i = 0;
                        for (Object val : ja) {
                            if (i > 0) {
                                sb.append('\n');
                            }
                            sb.append(escape(val.toString()));
                            i++;
                        }
                    } else {
                        sb.append(escape(value.toString()));
                    }

                    // Emit an array of similar keys

                } else if (value instanceof JsonArray) {
                    ja = (JsonArray) value;
                    for (Object val : ja) {
                        if (val instanceof JsonArray) {
                            sb.append('<');
                            sb.append(key);
                            sb.append('>');
                            sb.append(toString(val));
                            sb.append("</");
                            sb.append(key);
                            sb.append('>');
                        } else {
                            sb.append(toString(val, key));
                        }
                    }
                } else if ("".equals(value)) {
                    sb.append('<');
                    sb.append(key);
                    sb.append("/>");

                    // Emit a new tag <k>

                } else {
                    sb.append(toString(value, key));
                }
            }
            if (tagName != null) {

                // Emit the </tagname> close tag
                sb.append("</");
                sb.append(tagName);
                sb.append('>');
            }
            return sb.toString();

        }

        if (object != null) {
            if (object.getClass().isArray()) {
                object = new JsonArray(object);
            }

            if (object instanceof JsonArray) {
                ja = (JsonArray) object;
                for (Object val : ja) {
                    // XmlBetweenJsonObject does not have good support for arrays. If an array
                    // appears in a place where XmlBetweenJsonObject is lacking, synthesize an
                    // <array> element.
                    sb.append(toString(val, tagName == null ? "array" : tagName));
                }
                return sb.toString();
            }
        }

        string = (object == null) ? "null" : escape(object.toString());
        return (tagName == null) ? "\"" + string + "\""
                : (string.length() == 0) ? "<" + tagName + "/>" : "<" + tagName + ">" + string + "</" + tagName + ">";

    }
}
