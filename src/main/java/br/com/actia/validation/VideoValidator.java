package br.com.actia.validation;

import br.com.actia.model.Video;
import java.util.Set;
import javax.validation.ConstraintViolation;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class VideoValidator implements Validator<Video> {

    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(Video video) {
        StringBuilder sb = new StringBuilder();
        if (video != null) {
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<Video>> constraintViolations = validator.validate(video);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade video\n");
                for (ConstraintViolation<Video> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
}
