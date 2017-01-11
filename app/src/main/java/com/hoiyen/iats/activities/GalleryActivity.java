package com.hoiyen.iats.activities;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.ImageGalleryListAdapter;
import com.hoiyen.iats.models.ImageModel;
import com.hoiyen.iats.utils.GalleryHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab_menu)
    TabLayout tabs;

    @BindView(R.id.image_list)
    RecyclerView image_list;

    ImageGalleryListAdapter adapter;

    private List<ImageModel> images = new ArrayList<>();
    private int offset = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        setActionBar(toolbar);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(null);
        }

        adapter = new ImageGalleryListAdapter(this);

        image_list.setHasFixedSize(true);
        image_list.setLayoutManager(new GridLayoutManager(this, 2));
        image_list.setAdapter(adapter);
        setListener();

        // Check if have permission to read storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestImagePermission(this);
        } else {
            images = GalleryHelper.getImages(this, offset);
            adapter.putDataset(images);
        }

        Toast.makeText(this, "Loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabs.setScrollPosition(0, 0, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, BlogActivity.class));
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, BlogActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                images = GalleryHelper.getImages(this, offset);
                adapter.putDataset(images);
            }
        }
    }

    private void requestImagePermission(Activity activity) {
        // We dont have permission to read storage, now we need to request the permission
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    private void setListener() {

        // Listener for gallery list
        image_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                boolean atBottom = recyclerView.canScrollVertically(1);

                // arrived at bottom of recyclerview
                if (!atBottom) {
                    // load more images
                    offset++;
                    adapter.update(GalleryHelper.getImages(GalleryActivity.this, offset));
                }

            }
        });

        // Listener for tabs
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Intent intent;
                switch (tab.getPosition()) {

                    // call camera
                    case 1:
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                        }
                        break;

                    // call video
                    case 2:
                        intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                        }
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() != 1) {
                    tabs.setScrollPosition(0, 0, false);
                }
            }
        });

    }
}
