package com.hoiyen.iats.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FunctionHelper {

    public static Point getDisplaySize(Context context) {
        return getDisplaySize((Activity) context);
    }

    public static Point getDisplaySize(Activity context) {
        final Display display = context.getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Bitmap resizeImage(Bitmap sourceImage, @Nullable Integer max_size) {

        int newH, newW;

        // Resize image
        int oldH = sourceImage.getHeight();
        int oldW = sourceImage.getWidth();

        // max width or height size
        if (max_size == null) {
            max_size = 1500;
        }

        // set default value
        newH = max_size;
        newW = max_size;

        // height is bigger than width, maybe portrait mode
        if (oldH > oldW) {
            newH = max_size;
            float newSize = ((float) max_size / oldH);
            newSize = newSize * oldW;
            newW = (int) newSize;
        }

        // width is bigger than height, landscape mode
        if (oldW > oldH) {
            newW = max_size;
            float newSize = ((float) max_size / oldW);
            newSize = newSize * oldH;
            newH = (int) newSize;
        }

        return Bitmap.createScaledBitmap(sourceImage, newW, newH, false);

    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    public static void watermarkImage(String sourcePath, String watermarkPath, int position) {

        final Bitmap sourceBitmap = BitmapFactory.decodeFile(sourcePath);
        final Bitmap watermarkBitmap = BitmapFactory.decodeFile(watermarkPath);

        final Bitmap resizedWatermark = resizeImage(watermarkBitmap, 200);

        Canvas canvas = new Canvas(sourceBitmap);
        canvas.drawBitmap(resizedWatermark, 0, 0, null);

        try {
            final FileOutputStream fOut = new FileOutputStream(sourcePath);
            sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Gallery Process", e.getMessage());
        }

    }

    public static int dptopx(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
