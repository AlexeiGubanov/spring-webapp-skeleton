package org.swas.web.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 23.01.12
 */
@Documented
@Constraint(validatedBy = {FieldMatchValidator.class})
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@NotNull
public @interface FieldMatch {
    String message() default "{FieldMatch.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String original();

    String compared();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FieldMatch[] value();
    }
}
