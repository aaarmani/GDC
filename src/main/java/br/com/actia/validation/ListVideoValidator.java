package br.com.actia.validation;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import br.com.actia.model.ListVideo;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

public class ListVideoValidator implements Validator<ListVideo> {

    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(ListVideo listVideo) {
        StringBuilder sb = new StringBuilder();
        if (listVideo != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<ListVideo>> constraintViolations = validator.validate(listVideo);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade ListVideo\n");
                for (ConstraintViolation<ListVideo> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
