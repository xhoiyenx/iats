package com.hoiyen.iats.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.hoiyen.iats.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryProcessActivity extends Activity {



    @BindView(R.id.image_view)
    ImageView image;

    @BindView(R.id.watermark_image)
    ImageView watermark_image;

    @BindView(R.id.watermark)
    ToggleButton watermark;

    @BindView(R.id.position_button)
    Button position;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_process);
        ButterKnife.bind(this);

        // get parameter
        final Intent intent = getIntent();
        filepath = intent.getStringExtra("media");
        if (filepath == null || "".equals(filepath)) {
            finish();
        }

        setActionBar(toolbar);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(null);
        }

        final File file = new File(filepath);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            image.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_process, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                Intent intent = new Intent(this, GalleryShareActivity.class);
                intent.putExtra("media", filepath);
                startActivity(intent);
                break;
        }

        return true;

    }

    private void setListener() {
        watermark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    watermark_image.setVisibility(View.VISIBLE);
                }
                else {
                    watermark_image.setVisibility(View.GONE);
                }
            }
        });
    }
}
