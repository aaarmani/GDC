package br.com.actia.validation;

import br.com.actia.model.Route;
import java.util.ArrayList;
import java.util.Set;
import javax.validation.ConstraintViolation;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import javax.validation.ValidatorFactory;

public class RoutesToGenerateValidator  implements Validator<ArrayList<Route>> {

    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(ArrayList<Route> routes) {
        StringBuilder sb = new StringBuilder();
        if (routes != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<ArrayList<Route>>> constraintViolations = validator.validate(routes);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade routes\n");
                for (ConstraintViolation<ArrayList<Route>> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
