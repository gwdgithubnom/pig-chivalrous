package org.gjgr.pig.chivalrous.core.util;

import org.gjgr.pig.chivalrous.core.lang.Nullable;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;

import java.util.Locale;

/**
 * @Author gwd
 * @Time 01-22-2019 Tuesday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class LocaleCommand {
    /**
     * Parse the given {@code String} value into a {@link Locale}, accepting the {@link Locale#toString} format as well
     * as BCP 47 language tags.
     *
     * @param localeValue the locale value: following either {@code Locale's} {@code toString()} format ("en", "en_UK",
     *                    etc), also accepting spaces as separators (as an alternative to underscores), or BCP 47 (e.g. "en-UK")
     *                    as specified by {@link Locale#forLanguageTag} on Java 7+
     * @return a corresponding {@code Locale} instance, or {@code null} if none
     * @throws IllegalArgumentException in case of an invalid locale specification
     * @see #parseLocaleString
     * @see Locale#forLanguageTag
     * @since 5.0.4
     */
    @Nullable
    public static Locale parseLocale(String localeValue) {
        String[] tokens = tokenizeLocaleSource(localeValue);
        if (tokens.length == 1) {
            return Locale.forLanguageTag(localeValue);
        }
        return parseLocaleTokens(localeValue, tokens);
    }

    /**
     * Parse the given {@code String} representation into a {@link Locale}.
     * <p>
     * This is the inverse operation of {@link Locale#toString Locale's toString}.
     *
     * @param localeString the locale {@code String}: following {@code Locale's} {@code toString()} format ("en",
     *                     "en_UK", etc), also accepting spaces as separators (as an alternative to underscores)
     *                     <p>
     *                     Note: This variant does not accept the BCP 47 language tag format. Please use {@link #parseLocale} for
     *                     lenient parsing of both formats.
     * @return a corresponding {@code Locale} instance, or {@code null} if none
     * @throws IllegalArgumentException in case of an invalid locale specification
     */
    @Nullable
    public static Locale parseLocaleString(String localeString) {
        return parseLocaleTokens(localeString, tokenizeLocaleSource(localeString));
    }

    private static String[] tokenizeLocaleSource(String localeSource) {
        return StringCommand.tokenizeToStringArray(localeSource, "_ ", false, false);
    }

    @Nullable
    private static Locale parseLocaleTokens(String localeString, String[] tokens) {
        String language = (tokens.length > 0 ? tokens[0] : "");
        String country = (tokens.length > 1 ? tokens[1] : "");
        validateLocalePart(language);
        validateLocalePart(country);

        String variant = "";
        if (tokens.length > 2) {
            // There is definitely a variant, and it is everything after the country
            // code sans the separator between the country code and the variant.
            int endIndexOfCountryCode = localeString.indexOf(country, language.length()) + country.length();
            // Strip off any leading '_' and whitespace, what's left is the variant.
            variant = StringCommand.trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
            if (variant.startsWith("_")) {
                variant = StringCommand.trimLeadingCharacter(variant, '_');
            }
        }
        return (language.length() > 0 ? new Locale(language, country, variant) : null);
    }

    private static void validateLocalePart(String localePart) {
        for (int i = 0; i < localePart.length(); i++) {
            char ch = localePart.charAt(i);
            if (ch != ' ' && ch != '_' && ch != '#' && !Character.isLetterOrDigit(ch)) {
                throw new IllegalArgumentException(
                        "Locale part \"" + localePart + "\" contains invalid characters");
            }
        }
    }
}
