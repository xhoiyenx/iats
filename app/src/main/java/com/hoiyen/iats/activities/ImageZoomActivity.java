package com.hoiyen.iats.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.hoiyen.iats.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

public class ImageZoomActivity extends Activity {

    private ZoomageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        imageView = (ZoomageView) findViewById(R.id.imageView);

        final Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        if (image == null) {
            finish();
        }

        Picasso.with(this).load(image).into(imageView);
    }
}
