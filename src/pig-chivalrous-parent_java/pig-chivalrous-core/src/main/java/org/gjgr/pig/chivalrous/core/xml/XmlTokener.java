package org.gjgr.pig.chivalrous.core.xml;

import org.gjgr.pig.chivalrous.core.json.JsonException;
import org.gjgr.pig.chivalrous.core.json.JsonTokener;

/**
 * XML分析器，继承自JSONTokener，提供XML的语法分析
 *
 * @author Json.org
 */
public class XmlTokener extends JsonTokener {

    /**
     * The table of entity values. It initially contains Character values for amp, apos, gt, lt, quot.
     */
    public static final java.util.HashMap<String, Character> entity;

    static {
        entity = new java.util.HashMap<String, Character>(8);
        entity.put("amp", XmlBetweenJsonObject.AMP);
        entity.put("apos", XmlBetweenJsonObject.APOS);
        entity.put("gt", XmlBetweenJsonObject.GT);
        entity.put("lt", XmlBetweenJsonObject.LT);
        entity.put("quot", XmlBetweenJsonObject.QUOT);
    }

    /**
     * Construct an XmlTokener from a string.
     *
     * @param s A source string.
     */
    public XmlTokener(String s) {
        super(s);
    }

    /**
     * Get the text in the CDATA block.
     *
     * @return The string up to the <code>]]&gt;</code>.
     * @throws JsonException If the <code>]]&gt;</code> is not found.
     */
    public String nextCDATA() throws JsonException {
        char c;
        int i;
        StringBuilder sb = new StringBuilder();
        for (; ; ) {
            c = next();
            if (end()) {
                throw syntaxError("Unclosed CDATA");
            }
            sb.append(c);
            i = sb.length() - 3;
            if (i >= 0 && sb.charAt(i) == ']' && sb.charAt(i + 1) == ']' && sb.charAt(i + 2) == '>') {
                sb.setLength(i);
                return sb.toString();
            }
        }
    }

    /**
     * Get the next XmlBetweenJsonObject outer token, trimming whitespace. There are two kinds of tokens: the '<' character which begins a markup tag, and the content text between markup tags.
     *
     * @return A string, or a '<' Character, or null if there is no more source text.
     * @throws JsonException
     */
    public Object nextContent() throws JsonException {
        char c;
        StringBuilder sb;
        do {
            c = next();
        } while (Character.isWhitespace(c));
        if (c == 0) {
            return null;
        }
        if (c == '<') {
            return XmlBetweenJsonObject.LT;
        }
        sb = new StringBuilder();
        for (; ; ) {
            if (c == '<' || c == 0) {
                back();
                return sb.toString().trim();
            }
            if (c == '&') {
                sb.append(nextEntity(c));
            } else {
                sb.append(c);
            }
            c = next();
        }
    }

    /**
     * Return the next entity. These entities are translated to Characters: <code>&amp;  &apos;  &gt;  &lt;  &quot;</code>.
     *
     * @param ampersand An ampersand character.
     * @return A Character or an entity String if the entity is not recognized.
     * @throws JsonException If missing ';' in XmlBetweenJsonObject entity.
     */
    public Object nextEntity(char ampersand) throws JsonException {
        StringBuilder sb = new StringBuilder();
        for (; ; ) {
            char c = next();
            if (Character.isLetterOrDigit(c) || c == '#') {
                sb.append(Character.toLowerCase(c));
            } else if (c == ';') {
                break;
            } else {
                throw syntaxError("Missing ';' in XmlBetweenJsonObject entity: &" + sb);
            }
        }
        String string = sb.toString();
        Object object = entity.get(string);
        return object != null ? object : ampersand + string + ";";
    }

    /**
     * Returns the next XmlBetweenJsonObject meta token. This is used for skipping over <!...> and <?...?> structures.
     *
     * @return Syntax characters (<code>< > / = ! ?</code>) are returned as Character, and strings and names are returned as Boolean. We don't care what the values actually are.
     * @throws JsonException If a string is not properly closed or if the XmlBetweenJsonObject is badly structured.
     */
    public Object nextMeta() throws JsonException {
        char c;
        char q;
        do {
            c = next();
        } while (Character.isWhitespace(c));
        switch (c) {
            case 0:
                throw syntaxError("Misshaped meta tag");
            case '<':
                return XmlBetweenJsonObject.LT;
            case '>':
                return XmlBetweenJsonObject.GT;
            case '/':
                return XmlBetweenJsonObject.SLASH;
            case '=':
                return XmlBetweenJsonObject.EQ;
            case '!':
                return XmlBetweenJsonObject.BANG;
            case '?':
                return XmlBetweenJsonObject.QUEST;
            case '"':
            case '\'':
                q = c;
                for (; ; ) {
                    c = next();
                    if (c == 0) {
                        throw syntaxError("Unterminated string");
                    }
                    if (c == q) {
                        return Boolean.TRUE;
                    }
                }
            default:
                for (; ; ) {
                    c = next();
                    if (Character.isWhitespace(c)) {
                        return Boolean.TRUE;
                    }
                    switch (c) {
                        case 0:
                        case '<':
                        case '>':
                        case '/':
                        case '=':
                        case '!':
                        case '?':
                        case '"':
                        case '\'':
                            back();
                            return Boolean.TRUE;
                    }
                }
        }
    }

    /**
     * Get the next XmlBetweenJsonObject Token. These tokens are found inside of angle brackets. It may be one of these characters: <code>/ > = ! ?</code> or it may be a string wrapped in single quotes or double
     * quotes, or it may be a name.
     *
     * @return a String or a Character.
     * @throws JsonException If the XmlBetweenJsonObject is not well formed.
     */
    public Object nextToken() throws JsonException {
        char c;
        char q;
        StringBuilder sb;
        do {
            c = next();
        } while (Character.isWhitespace(c));
        switch (c) {
            case 0:
                throw syntaxError("Misshaped element");
            case '<':
                throw syntaxError("Misplaced '<'");
            case '>':
                return XmlBetweenJsonObject.GT;
            case '/':
                return XmlBetweenJsonObject.SLASH;
            case '=':
                return XmlBetweenJsonObject.EQ;
            case '!':
                return XmlBetweenJsonObject.BANG;
            case '?':
                return XmlBetweenJsonObject.QUEST;

            // Quoted string

            case '"':
            case '\'':
                q = c;
                sb = new StringBuilder();
                for (; ; ) {
                    c = next();
                    if (c == 0) {
                        throw syntaxError("Unterminated string");
                    }
                    if (c == q) {
                        return sb.toString();
                    }
                    if (c == '&') {
                        sb.append(nextEntity(c));
                    } else {
                        sb.append(c);
                    }
                }
            default:

                // Name

                sb = new StringBuilder();
                for (; ; ) {
                    sb.append(c);
                    c = next();
                    if (Character.isWhitespace(c)) {
                        return sb.toString();
                    }
                    switch (c) {
                        case 0:
                            return sb.toString();
                        case '>':
                        case '/':
                        case '=':
                        case '!':
                        case '?':
                        case '[':
                        case ']':
                            back();
                            return sb.toString();
                        case '<':
                        case '"':
                        case '\'':
                            throw syntaxError("Bad character in a name");
                    }
                }
        }
    }

    /**
     * Skip characters until past the requested string. If it is not found, we are left at the end of the source with a result of false.
     *
     * @param to A string to skip past.
     * @throws JsonException
     */
    public boolean skipPast(String to) throws JsonException {
        boolean b;
        char c;
        int i;
        int j;
        int offset = 0;
        int length = to.length();
        char[] circle = new char[length];

        /*
         * First fill the circle buffer with as many characters as are in the to string. If we reach an early end, bail.
         */

        for (i = 0; i < length; i += 1) {
            c = next();
            if (c == 0) {
                return false;
            }
            circle[i] = c;
        }

        /* We will loop, possibly for all of the remaining characters. */

        for (; ; ) {
            j = offset;
            b = true;

            /* Compare the circle buffer with the to string. */

            for (i = 0; i < length; i += 1) {
                if (circle[j] != to.charAt(i)) {
                    b = false;
                    break;
                }
                j += 1;
                if (j >= length) {
                    j -= length;
                }
            }

            /* If we exit the loop with b intact, then victory is ours. */

            if (b) {
                return true;
            }

            /* Get the next character. If there isn't one, then defeat is ours. */

            c = next();
            if (c == 0) {
                return false;
            }
            /*
             * Shove the character in the circle buffer and advance the circle offset. The offset is mod n.
             */
            circle[offset] = c;
            offset += 1;
            if (offset >= length) {
                offset -= length;
            }
        }
    }
}
