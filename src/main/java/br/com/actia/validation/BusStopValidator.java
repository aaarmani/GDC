package br.com.actia.validation;

import br.com.actia.model.BusStop;
import java.util.ResourceBundle;
import java.util.Set;
import javax.validation.ConstraintViolation;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BusStopValidator  implements Validator<BusStop> {
    private ResourceBundle rb;
    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(BusStop busStop, ResourceBundle rb) {
        this.rb = rb;
        StringBuilder sb = new StringBuilder();
        if (busStop != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<BusStop>> constraintViolations = validator.validate(busStop);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade bus_stop\n");
                for (ConstraintViolation<BusStop> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
