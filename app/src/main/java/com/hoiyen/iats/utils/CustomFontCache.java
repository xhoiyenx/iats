package com.hoiyen.iats.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

public final class CustomFontCache {
    private static final Hashtable<String, Typeface> fCache = new Hashtable<>();
    public static Typeface get(String tfn, Context ctx) {
        Typeface tf = fCache.get(tfn);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(ctx.getAssets(), tfn);
                if (tf != null) {
                    fCache.put(tfn, tf);
                }

                return tf;
            } catch (Exception e) {
                return null;
            }
        } else {
            return tf;
        }
    }
}
