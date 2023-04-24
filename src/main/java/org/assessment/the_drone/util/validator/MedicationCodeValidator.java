package org.assessment.the_drone.util.validator;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Muhammed.Ibrahim
 */
public class MedicationCodeValidator implements ConstraintValidator<ValidMedicationCode, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            return value != null && Pattern.compile("^[A-Z0-9_]*$").matcher(value).matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

