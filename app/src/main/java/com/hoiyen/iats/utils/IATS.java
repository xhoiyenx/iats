package com.hoiyen.iats.utils;

import android.app.Application;

public final class IATS extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "SERIF", "Effra-Std-Lt.ttf");
        FontsOverride.setDefaultFont(this, "SANS", "Effra-Std-Rg.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "Effra-Std-Rg.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF_MEDIUM", "Effra-Std-Bd.ttf");
        //FontsOverride.setDefaultFont(this, "SANS_SERIF", "Effra-Std-Rg.ttf");
    }

}