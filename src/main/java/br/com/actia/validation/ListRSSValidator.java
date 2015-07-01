package br.com.actia.validation;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import br.com.actia.model.ListRSS;
import java.util.ResourceBundle;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

public class ListRSSValidator implements Validator<ListRSS> {
    private ResourceBundle rb;
    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(ListRSS listRSS, ResourceBundle rb) {
        this.rb = rb;
        StringBuilder sb = new StringBuilder();
        if (listRSS != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<ListRSS>> constraintViolations = validator.validate(listRSS);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade ListRSS\n");
                for (ConstraintViolation<ListRSS> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
