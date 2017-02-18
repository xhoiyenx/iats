package com.hoiyen.iats.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.hoiyen.iats.BuildConfig;
import com.hoiyen.iats.R;
import com.hoiyen.iats.utils.ExifUtil;
import com.hoiyen.iats.utils.FunctionHelper;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
    private boolean useWatermark = false;
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

            // Change orientation
            bitmap = ExifUtil.rotateBitmap(file.getAbsolutePath(), bitmap);

            // Resize
            bitmap = FunctionHelper.resizeImage(bitmap, null);

            // Replace the image
            try {
                final FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
            }
            catch ( IOException e) {
                Log.e("Gallery Process", e.getMessage());
            }

            image.setImageBitmap(bitmap);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Set user avatar as watermark image
        final String avatar_url = Prefs.getString("avatar", "");
        if (!"".equals(avatar_url)) {
            Picasso.with(this).load(avatar_url).resize(200, 200).into(watermark_image);
        }

        // Set listener
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
                Intent intent = new Intent(this, PostShareActivity.class);

                if (useWatermark) {
                    intent.putExtra("watermark_position", selectedPosition);
                }

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
                    useWatermark = true;
                }
                else {
                    watermark_image.setVisibility(View.GONE);
                    useWatermark = false;
                }
            }
        });
        position_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) watermark_image.getLayoutParams();

        if (selectedPosition == POSITION_LEFT_BOTTOM) {
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
