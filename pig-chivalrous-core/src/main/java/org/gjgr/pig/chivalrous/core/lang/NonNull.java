package org.gjgr.pig.chivalrous.core.lang;

/**
 * @Author gwd
 * @Time 11-27-2018  Tuesday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierNickname;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A common Spring annotation to declare that annotated elements cannot be {@code null}. Leverages JSR 305
 * meta-annotations to indicate nullability in Java to common tools with JSR 305 support and used by Kotlin to infer
 * nullability of Spring API.
 *
 * <p>
 * Should be used at parameter, return value, and field level. Method overrides should repeat parent {@code @NonNull}
 * annotations unless they behave differently.
 *
 * <p>
 * Use {@code @NonNullApi} (scope = parameters + return values) and/or {@code @NonNullFields} (scope = fields) to set
 * the default behavior to non-nullable in order to avoid annotating your whole codebase with {@code @NonNull}.
 *
 * @author Sebastien Deleuze
 * @author Juergen Hoeller
 * @see NonNullApi
 * @see NonNullFields
 * @see Nullable
 * @since 5.0
 */
@Target( {ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Nonnull
@TypeQualifierNickname
public @interface NonNull {
}
