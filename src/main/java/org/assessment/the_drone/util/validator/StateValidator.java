package org.assessment.the_drone.util.validator;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Muhammed.Ibrahim
 */
public class StateValidator implements ConstraintValidator<ValidDroneState, String> {
    String[] stateArray = new String[] { "IDLE", "LOADING", "LOADED", "DELIVERING", "DELIVERED", "RETURNING" };

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            return value != null && Arrays.asList(stateArray).contains(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

