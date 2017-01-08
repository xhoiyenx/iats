package com.hoiyen.iats.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.hoiyen.iats.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryProcessActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.image_view)
    ImageView image;

    @BindView(R.id.watermark_image)
    ImageView watermark_image;

    @BindView(R.id.watermark)
    ToggleButton watermark;

    @BindView(R.id.position_button)
    Button position_button;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String filepath;
    private int selectedPosition = 1;
    private static final int POSITION_BOTTOM = 1;
    private static final int POSITION_BOTTOM_RIGHT = 2;
    private static final int POSITION_RIGHT = 3;
    private static final int POSITION_TOP_RIGHT = 4;
    private static final int POSITION_TOP = 5;
    private static final int POSITION_TOP_LEFT = 6;
    private static final int POSITION_LEFT = 7;
    private static final int POSITION_LEFT_BOTTOM = 8;

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
        position_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) watermark_image.getLayoutParams();

        if (selectedPosition == 8) {
            selectedPosition = 0;
        }

        selectedPosition++;

        switch (selectedPosition) {
            case POSITION_BOTTOM: {
                layout.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                layout.removeRule(RelativeLayout.ALIGN_PARENT_START);
                layout.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
                layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            break;

            case POSITION_BOTTOM_RIGHT: {
                layout.removeRule(RelativeLayout.CENTER_HORIZONTAL);
                layout.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            break;

            case POSITION_RIGHT: {
                layout.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layout.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                layout.addRule(RelativeLayout.CENTER_VERTICAL);
                layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            break;

            case POSITION_TOP_RIGHT: {
                layout.removeRule(RelativeLayout.CENTER_VERTICAL);
                layout.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                layout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            break;

            case POSITION_TOP: {
                layout.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                layout.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                layout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
            }
            break;

            case POSITION_TOP_LEFT: {
                layout.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                layout.removeRule(RelativeLayout.CENTER_HORIZONTAL);

                layout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                layout.addRule(RelativeLayout.ALIGN_PARENT_START);
            }
            break;

            case POSITION_LEFT: {
                layout.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                layout.removeRule(RelativeLayout.ALIGN_PARENT_START);

                layout.addRule(RelativeLayout.CENTER_VERTICAL);
                layout.addRule(RelativeLayout.ALIGN_PARENT_START);
            }
            break;

            case POSITION_LEFT_BOTTOM: {
                layout.removeRule(RelativeLayout.CENTER_VERTICAL);
                layout.removeRule(RelativeLayout.ALIGN_PARENT_START);

                layout.addRule(RelativeLayout.ALIGN_PARENT_START);
                layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            break;
        }

        watermark_image.setLayoutParams(layout);

    }
}
