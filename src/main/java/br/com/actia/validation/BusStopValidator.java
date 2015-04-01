/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.actia.validation;

import br.com.actia.model.BusStop;
import java.util.Set;
import javax.validation.ConstraintViolation;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BusStopValidator  implements Validator<BusStop> {

    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(BusStop busStop) {
        StringBuilder sb = new StringBuilder();
        if (busStop != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<BusStop>> constraintViolations = validator.validate(busStop);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade POI\n");
                for (ConstraintViolation<BusStop> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
