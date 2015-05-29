package br.com.actia.validation;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import br.com.actia.model.ListBanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

public class ListBannerValidator implements Validator<ListBanner> {

    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(ListBanner listBanner) {
        StringBuilder sb = new StringBuilder();
        if (listBanner != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<ListBanner>> constraintViolations = validator.validate(listBanner);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade ListBanner\n");
                for (ConstraintViolation<ListBanner> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
