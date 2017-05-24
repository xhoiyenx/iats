package com.hoiyen.iats.activities;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import com.hoiyen.iats.utils.FunctionHelper;
import com.hoiyen.iats.utils.GalleryHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;
    static final int REQUEST_GALLERY = 3;

    String capturedImage = null;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //@BindView(R.id.tab_menu)
    //TabLayout tabs;

    //@BindView(R.id.image_list)
    //RecyclerView image_list;

    //ImageGalleryListAdapter adapter;

    //private List<ImageModel> images = new ArrayList<>();
    //private int offset = 1;

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

        // Check if have permission to read storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestImagePermission(this);
        }

        //setListener();
        //tabs.setSelected(false);

        /*
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
        */

    }

    @Override
    protected void onResume() {
        super.onResume();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private void requestImagePermission(Activity activity) {
        // We dont have permission to read storage, now we need to request the permission
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
    }

    /*
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
        */

        /*
        // Listener for tabs
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabListener(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() != 1) {
                    tabs.setScrollPosition(0, 0, false);
                }

                tabListener(tab);
            }
        });


    }
    */


    public void showGallery(View view) {
        /* OLD WAYS
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        startActivityForResult(intent, REQUEST_GALLERY);
        */

        // NEW WAYS
        Intent intent = new Intent();
        intent.setType("image/* video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY);
    }

    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Log.e("Gallery", ex.getMessage());
        }

        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "com.hoiyen.iats.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void takeVideo(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String filepath = getRealPathFromURI(uri);
                    String new_file = copyGalleryImage(filepath);

                    final Intent intent = new Intent(this, GalleryProcessActivity.class);
                    intent.putExtra("media", new_file);
                    startActivity(intent);
                }
                break;

            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    if (capturedImage != null) {
                        final Intent intent = new Intent(this, GalleryProcessActivity.class);
                        intent.putExtra("media", capturedImage);
                        startActivity(intent);
                    }
                }
                break;
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private File createImageFile() throws IOException {
        String filename = "IMG_" + System.currentTimeMillis();
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(filename, ".jpg", dir);
        capturedImage = image.getAbsolutePath();
        return image;
    }

    /**
     * Copy image from gallery to app storage
     *
     * @param filepath
     */
    private String copyGalleryImage(String filepath) {

        // 1. Convert source path to File
        final File file = new File(filepath);

        // 2. Create destination file
        //final File dest = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_" + System.currentTimeMillis() + ".jpg");

        try {
            final File dest = File.createTempFile("IMG_" + System.currentTimeMillis(), ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            FunctionHelper.copyFile(file, dest);

            if (dest.exists()) {
                return dest.getAbsolutePath();
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            Log.e("Copy", e.getMessage());
        }

        return null;
    }

}
