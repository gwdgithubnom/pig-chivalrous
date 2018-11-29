package org.gjgr.pig.chivalrous.core.lang;

/**
 * @Author gwd
 * @Time 11-27-2018  Tuesday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;

/**
 * A common Spring annotation to declare that parameters and return values
 * are to be considered as non-nullable by default for a given package.
 *
 * <p>Leverages JSR-305 meta-annotations to indicate nullability in Java to common
 * tools with JSR-305 support and used by Kotlin to infer nullability of Spring API.
 *
 * <p>Should be used at package level in association with {@link Nullable}
 * annotations at parameter and return value level.
 *
 * @author Sebastien Deleuze
 * @author Juergen Hoeller
 * @see NonNullFields
 * @see Nullable
 * @see NonNull
 * @since 5.0
 */
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Nonnull
@TypeQualifierDefault({ElementType.METHOD, ElementType.PARAMETER})
public @interface NonNullApi {
}
