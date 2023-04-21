package org.assessment.the_drone.util.validator;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Muhammed.Ibrahim
 */
public class ModelValidator implements ConstraintValidator<ValidDroneModel, String> {
    String[] modelArray = new String[] { "Lightweight", "Middleweight", "Cruiserweight", "Heavyweight" };

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            return value != null && Arrays.asList(modelArray).contains(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

