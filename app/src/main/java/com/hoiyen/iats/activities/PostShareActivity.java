package com.hoiyen.iats.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.TagListAdapter;
import com.hoiyen.iats.library.ApiRequest;
import com.hoiyen.iats.models.TagModel;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostShareActivity extends Activity {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "Share";
    private Bitmap bitmap;
    private int watermark_position = 0;
    private boolean use_watermark = false;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.add_location_text)
    TextView add_location;

    @BindView(R.id.caption_edit)
    EditText caption;

    @BindView(R.id.image_view)
    ImageView image;

    @BindView(R.id.tag_edit)
    EditText tagEdit;

    @BindView(R.id.tag_list)
    RecyclerView tag_list;

    private TagListAdapter adapter;

    // Params
    private String place_id, place_name, place_address, place_country, place_lat, place_lng, tags = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_share);
        ButterKnife.bind(this);

        // get parameter
        final Intent intent = getIntent();
        final String filepath = intent.getStringExtra("media");
        if (filepath == null || "".equals(filepath)) {
            finish();
            return;
        }

        if (intent.hasExtra("watermark_position")) {
            use_watermark = true;
            watermark_position = intent.getIntExtra("watermark_position", 0);
        }

        final File file = new File(filepath);
        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            image.setImageBitmap(bitmap);
        }

        // Set toolbar
        setActionBar(toolbar);
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(null);
        }

        findLocationListener();

        adapter = new TagListAdapter(this);
        tag_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tag_list.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (dstart == 0 && source.toString().equals(" ")) {
                    return "";
                }

                if (source.toString().equals(" ")) {
                    tagEdit.setText("");
                    adapter.putTag(dest.toString());
                    if ("".equals(tags)) {
                        tags = tags.concat(dest.toString());
                    }
                    else {
                        tags = tags.concat(" ").concat(dest.toString());
                    }
                }
                return null;
            }
        };
        tagEdit.setFilters(new InputFilter[]{filter});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                submit();
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                place_id = place.getId();
                place_name = place.getName().toString();
                place_address = place.getAddress().toString();
                place_country = "ID";

                LatLng coor = place.getLatLng();
                place_lat = String.valueOf(coor.latitude);
                place_lng = String.valueOf(coor.longitude);
                add_location.setText(place.getName());
            }
        }
    }

    private void findLocationListener() {
        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                        .setCountry("ID")
                        .build();
                try {
                    Intent intent = new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(PostShareActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    private void submit()
    {
        // validate
        if ("".equals(place_id) || place_id == null) {
            Toast.makeText(PostShareActivity.this, getString(R.string.error_select_location), Toast.LENGTH_SHORT).show();
            return;
        }

        final String url = getString(R.string.api_share_post);
        final Map<String, String> param = new HashMap<>();

        // Place information
        param.put("google_id", place_id);
        param.put("place_name", place_name);
        param.put("place_address", place_address);
        param.put("place_country", place_country);
        param.put("place_city", "");
        param.put("place_lat", place_lat);
        param.put("place_lng", place_lng);

        // Post information
        param.put("caption", caption.getText().toString());
        param.put("image", getStringImage(bitmap));
        param.put("tags", tags);

        if (use_watermark) {
            param.put("watermark_position", String.valueOf(watermark_position));
        }

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Loading ...", true);
        ApiRequest.PostRequest(url, param, new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                startActivity(new Intent(PostShareActivity.this, BlogActivity.class));
                finish();
            }

            @Override
            public void onErrorResponse(String response) {
                Toast.makeText(PostShareActivity.this, getString(R.string.error_submit_post), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
