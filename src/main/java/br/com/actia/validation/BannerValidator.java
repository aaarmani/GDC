package br.com.actia.validation;

import br.com.actia.util.CONST;
import br.com.actia.model.Banner;
import java.io.File;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.scene.image.Image;
import javax.validation.ConstraintViolation;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BannerValidator  implements Validator<Banner> {
    private ResourceBundle rb;
    private static ValidatorFactory factory;

    static {
        factory = buildDefaultValidatorFactory();
    }
    

    @Override
    public String validate(Banner banner, ResourceBundle rb) {
        this.rb = rb;
        StringBuilder sb = new StringBuilder();
        if (banner != null) {
            
            String strReturn = bannerFormatValidation(banner, sb);
            if(!strReturn.isEmpty()) {
                sb.append(rb.getString("FILE_VALIDATION"));
                sb.append(strReturn);
            }
            
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

    
    private String bannerFormatValidation(Banner banner, StringBuilder sb) {
        String msg = "";
        
        msg += imageValidation(banner.getImagePath());
        if(banner.getAudioPath() != null && !banner.getAudioPath().isEmpty())
            msg += audioValidation(banner.getAudioPath());
        
        return msg;
    }
    
    private String imageValidation(String imagePath) {
        File file = new File(imagePath);
        String msg = "";
        
        if(file.length() > CONST.BANNER_MAX_LENGTH) {
            msg +=  rb.getString("FILE_SIZE_ERR") + " " + CONST.BANNER_MAX_LENGTH + " Bytes.";
        }
        
        Image img = new Image("file:///" + file.getPath().toString());
        
        if(img.getHeight() < CONST.BANNER_MIN_HEIGTH || img.getHeight() > CONST.BANNER_MAX_HEIGTH ||
            img.getWidth() < CONST.BANNER_MIN_WIDTH || img.getWidth() > CONST.BANNER_MAX_WIDTH) {
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
