package com.hoiyen.iats.utils;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pixplicity.easyprefs.library.Prefs;

public final class IATS extends Application {

    public static final String TAG = IATS.class.getSimpleName();
    private RequestQueue requestQueue;
    private static IATS instance;

    public static synchronized IATS getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        /**
         * Setup preference helpers
         */
        new Prefs.Builder()
                .setContext(this)
                .setMode(MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        FontsOverride.setDefaultFont(this, "SERIF", "Effra-Std-Lt.ttf");
        FontsOverride.setDefaultFont(this, "SANS", "Effra-Std-Rg.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "Effra-Std-Rg.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF_MEDIUM", "Effra-Std-Bd.ttf");
    }

}