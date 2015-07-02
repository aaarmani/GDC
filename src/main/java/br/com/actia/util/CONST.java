package br.com.actia.util;

import java.io.File;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public final class CONST {
    //#Audio
    public static long AUDIO_MAX_LENGTH = 100000; //Maximum audio lenght in bytes
    //#video
    public static long VIDEO_MAX_TIME = 100000; //in milliseconds
    public static long VIDEO_MAX_LENGTH = 500000; //500 KB
    //#Banner
    public static long BANNER_MAX_HEIGTH = 130;
    public static long BANNER_MIN_HEIGTH = 100;
    public static long BANNER_MAX_WIDTH = 1200;
    public static long BANNER_MIN_WIDTH = 1000;
    public static long BANNER_MAX_LENGTH = 100000; //Maximum lenght in bytes
    //#Indication Image
    public static long IMAGE_MAX_HEIGTH = 1080;
    public static long IMAGE_MIN_HEIGTH = 360;
    public static long IMAGE_MAX_WIDTH = 1920;
    public static long IMAGE_MIN_WIDTH = 640;
    public static long IMAGE_MAX_LENGTH = 400000; //Maximum lenght in bytes
    
    public static String getContentFolder(){
        return System.getProperty("user.dir") + File.separatorChar + "CONTENT_FILES";
    }
    
    public static String getAudiosFolder(){
        return getContentFolder() + File.separatorChar + "audios";
    }
    
    public static String getBannersFolder(){
        return getContentFolder() + File.separatorChar + "banners";
    }
    
    public static String getImagesFolder(){
        return getContentFolder() + File.separatorChar + "images";
    }
    
    public static String getVideosFolder(){
        return getContentFolder() + File.separatorChar + "videos";
    }
}
