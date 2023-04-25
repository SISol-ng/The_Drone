package org.assessment.the_drone.util.validator;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.assessment.the_drone.model.Model;

/**
 *
 * @author Muhammed.Ibrahim
 */
public class ModelValidator implements ConstraintValidator<ValidDroneModel, Model> {
    private Model[] model;
    
    @Override
    public void initialize(ValidDroneModel constraint) {
        this.model = constraint.valid();
        }

    @Override
    public boolean isValid(Model value, ConstraintValidatorContext context) {
        return value != null && Arrays.asList(model).contains(value);
    }
}

