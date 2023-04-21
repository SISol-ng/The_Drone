package org.assessment.the_drone.util.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author Muhammed.Ibrahim
 */
@Documented
@Constraint(validatedBy = ModelValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMedicationCode {
    String message() default "Medication code must contain only upper case letter, underscore, and numbers";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
