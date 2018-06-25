package com.wuyson.blurbyrenderscript;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import java.io.FileDescriptor;

public class BitmapUtils {
    public static Bitmap decodeSampleBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        // 如果在 Honeycomb 或更新版本系统中运行，尝试使用 inBitmap
        //使用 inBitmap 重复利用内存空间，避免重复开辟新内存
//        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB) {
//            addInBitmapOptions(options, cache);
//        }
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampleBitmapFromResource(FileDescriptor fd, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }


//    private static void addInBitmapOptions(BitmapFactory.Options options,
//                                           ImageCache cache) {
//        options.inMutable = true;
//        if (cache != null) {
//            Bitmap inBitmap = cache.getBitmapFromReusableSet(options);
//            if (inBitmap != null) {
//                options.inBitmap = inBitmap;
//            }
//        }
//    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 图片转出 RGB_565
     * @param context 上下文
     * @param id    图片Id
     * @param reqWidth  宽度
     * @param reqHeight 高度
     * @return Bitmap
     */
    public static Bitmap bitmap2RGB_565(Context context, @DrawableRes int id, int reqWidth, int reqHeight) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        if (drawable != null) {
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 :
                    Bitmap.Config.RGB_565;
            return Bitmap.createBitmap(reqWidth, reqHeight, config);
        }
        return null;
    }

    public static BitmapDrawable bitmap2Drawable(Resources resources,Bitmap bitmap){
        return new BitmapDrawable(resources,bitmap);
    }
}
