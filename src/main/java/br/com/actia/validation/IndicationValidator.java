package br.com.actia.validation;

import br.com.actia.model.Indication;
import java.util.Set;
import javax.validation.ConstraintViolation;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import javax.validation.ValidatorFactory;

public class IndicationValidator  implements Validator<Indication> {

    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(Indication indication) {
        StringBuilder sb = new StringBuilder();
        if (indication != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<Indication>> constraintViolations = validator.validate(indication);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade indication\n");
                for (ConstraintViolation<Indication> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
