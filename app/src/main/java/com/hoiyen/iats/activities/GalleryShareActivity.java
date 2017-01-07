package com.hoiyen.iats.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hoiyen.iats.R;

public class GalleryShareActivity extends Activity {

    private String filepath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_share);


        // get parameter
        final Intent intent = getIntent();
        filepath = intent.getStringExtra("media");
        if (filepath == null || "".equals(filepath)) {
            finish();
        }
    }
}
