package br.com.actia.validation;

import br.com.actia.model.Indication;
import br.com.actia.util.CONST;
import java.io.File;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.scene.image.Image;
import javax.validation.ConstraintViolation;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import javax.validation.ValidatorFactory;

public class IndicationValidator  implements Validator<Indication> {
    private ResourceBundle rb;
    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }

    @Override
    public String validate(Indication indication, ResourceBundle rb) {
        this.rb = rb;
        StringBuilder sb = new StringBuilder();
        if (indication != null) {
            String strReturn = indicationFormatValidation(indication, sb);
            if(!strReturn.isEmpty()) {
                sb.append(rb.getString("FILE_VALIDATION"));
                sb.append(strReturn);
            }
            
            javax.validation.Validator validator = factory.getValidator();
            Set<ConstraintViolation<Indication>> constraintViolations = validator.validate(indication);

            if (!constraintViolations.isEmpty()) {
                sb.append("Validação da entidade indication\n");
                for (ConstraintViolation<Indication> constraint : constraintViolations) {
                    sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
                }
            }
        }
        return sb.toString();
    }
    
    private String indicationFormatValidation(Indication indication, StringBuilder sb) {
        String msg = "";
        
        if(indication.getImagePath() != null && !indication.getImagePath().isEmpty())
            msg += imageValidation(indication.getImagePath());
        
        if(indication.getAudioPath() != null && !indication.getAudioPath().isEmpty())
            msg += audioValidation(indication.getAudioPath());
        
        return msg;
    }
    
    private String imageValidation(String imagePath) {
        File file = new File(imagePath);
        String msg = "";
        
        if(file.length() > CONST.IMAGE_MAX_LENGTH) {
            msg +=  rb.getString("FILE_SIZE_ERR") + " " + CONST.IMAGE_MAX_LENGTH + " Bytes.";
        }
        
        Image img = new Image("file:///" + file.getPath().toString());
        
        if(img.getHeight() < CONST.IMAGE_MIN_HEIGTH || img.getHeight() > CONST.IMAGE_MAX_HEIGTH ||
            img.getWidth() < CONST.IMAGE_MIN_WIDTH || img.getWidth() > CONST.IMAGE_MAX_WIDTH) {
            System.out.println("HEIGTH = " + img.getHeight() + " WIDTH = " + img.getWidth());
            msg += rb.getString("FILE_FORMAT_ERR");
        }

        return msg;
    }
    
    private String audioValidation(String audioPath) {
        File file = new File(audioPath);
        String msg = "";
        
        /*if(mediaFile.getDuration().greaterThan(Duration.millis(MAX_VIDEO_TIME))) {
            return "Tempo do vídeo excede " + MAX_VIDEO_TIME + " ms.";
        }*/
        
        if(file.length() > CONST.AUDIO_MAX_LENGTH) {
            msg +=  rb.getString("FILE_SIZE_ERR") + " " + CONST.AUDIO_MAX_LENGTH + " Bytes.";
        }
        
        return msg;
    }
}
