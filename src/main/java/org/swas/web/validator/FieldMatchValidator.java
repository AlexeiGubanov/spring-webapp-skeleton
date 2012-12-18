package org.swas.web.validator;

import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 23.01.12
 */
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String original;

    private String compared;

    public void initialize(FieldMatch constraintAnnotation) {
        this.original = constraintAnnotation.original();
        this.compared = constraintAnnotation.compared();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        ConfigurablePropertyAccessor pa = PropertyAccessorFactory.forDirectFieldAccess(value);
        final Object origObj = pa.getPropertyValue(original);
        final Object compObj = pa.getPropertyValue(compared);

        boolean matches = origObj == null && compObj == null || compObj != null && compObj.equals(origObj);
        if (!matches) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addNode(this.compared)
                    .addConstraintViolation();
        }
        return matches;
    }
}
