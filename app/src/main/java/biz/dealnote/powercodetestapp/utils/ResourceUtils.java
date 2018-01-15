package biz.dealnote.powercodetestapp.utils;

import android.graphics.Bitmap;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by admin on 12/30/2017.
 * PowercodeTestApp
 */
public class ResourceUtils {
    public static void safelyClose(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException ignored) {

            }
        }
    }

    public static void safelyRecycle(Bitmap bitmap){
        if(bitmap != null){
            bitmap.recycle();
        }
    }
}