package br.com.actia.validation;

import br.com.actia.model.Route;
import java.util.Set;
import javax.validation.ConstraintViolation;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import javax.validation.ValidatorFactory;

public class RouteValidator  implements Validator<Route> {

    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(Route route) {
        StringBuilder sb = new StringBuilder();
        if (route != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<Route>> constraintViolations = validator.validate(route);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade route\n");
                for (ConstraintViolation<Route> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
