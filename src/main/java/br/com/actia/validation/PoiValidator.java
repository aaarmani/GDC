package br.com.actia.validation;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import br.com.actia.model.Poi;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

public class PoiValidator implements Validator<Poi> {

    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(Poi poi) {
        StringBuilder sb = new StringBuilder();
        if (poi != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<Poi>> constraintViolations = validator.validate(poi);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade POI\n");
                for (ConstraintViolation<Poi> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
