package service;

import br.com.actia.model.AbstractEntity;
import java.io.File;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class FileToCopy implements AbstractEntity {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_AUDIO = 1;
    public static final int TYPE_VIDEO = 2;
    private static final String CONTENT_FOLDER_NAME = "CONTENT_FILES";
    
    private File origFile;
    private File destFile;

    public FileToCopy(Integer type, String name, String fileSourcePath) {
        if((name == null || name.isEmpty()) || (fileSourcePath == null || fileSourcePath.isEmpty()))
            throw new IllegalArgumentException();
        
        origFile = new File(fileSourcePath);
        
        if(!origFile.exists() || !origFile.isFile()) {
            throw new IllegalArgumentException();
        }
        
        destFile = new File(getFullPath(type, name));
    }
    
    private String getFullPath(Integer type, String name) {
        char FSC = File.separatorChar;
        String path = System.getProperty("user.dir") + FSC + CONTENT_FOLDER_NAME + FSC;
        
        switch(type) {
            case TYPE_IMAGE:
                path += "banners";
                break;
            case TYPE_AUDIO:
                path += "audios";
                break;
            case TYPE_VIDEO:
                path += "videos";
                break;
            default:
                path += "defauls";
                break;
        }
        path += FSC + name;
        return path;
    }
     
    @Override
    public Number getId() {
        return null;
    }

    public File getOrigFile() {
        return origFile;
    }

    public void setOrigFile(File origFile) {
        this.origFile = origFile;
    }

    public File getDestFile() {
        return destFile;
    }

    public void setDestFile(File destFile) {
        this.destFile = destFile;
    }
}
