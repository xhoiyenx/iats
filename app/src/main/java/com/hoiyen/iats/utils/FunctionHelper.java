package com.hoiyen.iats.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;

public class FunctionHelper {

    public static Point getDisplaySize(Context context) {
        return getDisplaySize( (Activity) context );
    }

    public static Point getDisplaySize(Activity context) {
        final Display display = context.getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        return size;
    }

}
