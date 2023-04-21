package org.assessment.the_drone.util.validator;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Muhammed.Ibrahim
 */
public class MedicationNameValidator implements ConstraintValidator<ValidMedicationName, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            return value != null && Pattern.compile("^[a-zA-Z0-9_-]*$").matcher(value).matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

