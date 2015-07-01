package br.com.actia.validation;

import br.com.actia.model.ListBusStop;
import java.util.ResourceBundle;
import static javax.validation.Validation.buildDefaultValidatorFactory;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

public class ListBusStopValidator implements Validator<ListBusStop> {
    private ResourceBundle rb;
    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(ListBusStop listBusStop, ResourceBundle rb) {
        this.rb = rb;
        StringBuilder sb = new StringBuilder();
        if (listBusStop != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<ListBusStop>> constraintViolations = validator.validate(listBusStop);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade ListBusStop\n");
                for (ConstraintViolation<ListBusStop> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
