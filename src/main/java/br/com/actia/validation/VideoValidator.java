package br.com.actia.validation;

import br.com.actia.util.CONST;
import br.com.actia.model.Video;
import java.io.File;
import java.util.ResourceBundle;
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
    private ResourceBundle rb;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(Video video, ResourceBundle rb) {
        this.rb = rb;
        StringBuilder sb = new StringBuilder();
        
        if (video != null) {
            String strReturn = videoFormatValidation(video, sb);
            if(!strReturn.isEmpty()) {
                sb.append(rb.getString("FILE_VALIDATION"));
                sb.append(strReturn);
            }
            
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<Video>> constraintViolations = validator.validate(video);

            if (!constraintViolations.isEmpty()) {
                sb.append(rb.getString("ENTITY_VALIDATION"));
                //sb.append("Validação da entidade video\n");
                for (ConstraintViolation<Video> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }

    private String videoFormatValidation(Video video, StringBuilder sb) {
        File file = new File(video.getVideoPath());
        String msg = "";
        
        /*if(mediaFile.getDuration().greaterThan(Duration.millis(CONST.VIDEO_MAX_LENGTH))) {
            return "Tempo do vídeo excede " + CONST.VIDEO_MAX_LENGTH + " ms.";
        }*/
        
        if(file.length() >= CONST.VIDEO_MAX_LENGTH) {
            msg =  rb.getString("FILE_SIZE_ERR") + " " + CONST.VIDEO_MAX_LENGTH + " Bytes.";
        }
        
        return msg;
   }
}
