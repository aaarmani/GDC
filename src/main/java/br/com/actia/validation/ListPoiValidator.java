package br.com.actia.validation;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import br.com.actia.model.ListPoi;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

public class ListPoiValidator implements Validator<ListPoi> {

    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(ListPoi listPoi) {
        StringBuilder sb = new StringBuilder();
        if (listPoi != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<ListPoi>> constraintViolations = validator.validate(listPoi);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade ListPoi\n");
                for (ConstraintViolation<ListPoi> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
