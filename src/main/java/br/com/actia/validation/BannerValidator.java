package br.com.actia.validation;

import br.com.actia.model.Banner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BannerValidator  implements Validator<Banner> {

    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(Banner banner) {
        StringBuilder sb = new StringBuilder();
        if (banner != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<Banner>> constraintViolations = validator.validate(banner);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade banner\n");
                for (ConstraintViolation<Banner> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
