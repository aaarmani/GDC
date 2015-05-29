package br.com.actia.validation;

import br.com.actia.model.RSS;
import java.util.Set;
import javax.validation.ConstraintViolation;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import javax.validation.ValidatorFactory;

public class RSSValidator  implements Validator<RSS> {

    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(RSS RSS) {
        StringBuilder sb = new StringBuilder();
        if (RSS != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<RSS>> constraintViolations = validator.validate(RSS);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade RSS\n");
                for (ConstraintViolation<RSS> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
