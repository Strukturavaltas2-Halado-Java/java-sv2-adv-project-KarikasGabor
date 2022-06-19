package tabletennis.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<Name, String> {

    int maxLength;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Boolean result =
                !s.isEmpty() &&
                        !s.isBlank() &&
                        s.length() > 2 &&
                        s.length() < maxLength &&
                        Character.isUpperCase(s.charAt(0)) &&
                        s.contains(" ");
        return result;
    }

    @Override
    public void initialize(Name constraintAnnotation) {
        maxLength = constraintAnnotation.maxLength();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
