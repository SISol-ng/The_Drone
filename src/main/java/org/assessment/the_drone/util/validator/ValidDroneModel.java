package org.assessment.the_drone.util.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import org.assessment.the_drone.model.Model;

/**
 *
 * @author Muhammed.Ibrahim
 */
@Documented
@Constraint(validatedBy = ModelValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDroneModel {
    Model[] valid();
    String message() default "Drone model must be any of {valid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
