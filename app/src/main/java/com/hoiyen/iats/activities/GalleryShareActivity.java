package com.hoiyen.iats.activities;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.NearbyPlacesAdapter;
import com.hoiyen.iats.models.PlaceModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hoiyen.iats.R.id.image;

public class GalleryShareActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private String filepath = "";
    private GoogleApiClient mGoogleApiClient;
    private boolean placeCalled = false;
    private NearbyPlacesAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nearby_place_list)
    RecyclerView nearby_place_recyclerview;

    @BindView(R.id.loader)
    ProgressBar loader;

    @BindView(R.id.add_location_text)
    TextView add_location;

    @BindView(R.id.image_view)
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_share);
        ButterKnife.bind(this);

        // get parameter
        final Intent intent = getIntent();
        filepath = intent.getStringExtra("media");
        if (filepath == null || "".equals(filepath)) {
            finish();
        }

        setActionBar(toolbar);

        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(null);
        }

        adapter = new NearbyPlacesAdapter(this);
        nearby_place_recyclerview.setAdapter(adapter);
        nearby_place_recyclerview.setHasFixedSize(true);
        nearby_place_recyclerview.setNestedScrollingEnabled(true);
        nearby_place_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        findLocationListener();

        // 1. Build google client connection
        apiGoogleClient();

        // 2. Connect to google client
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();

        final File file = new File(filepath);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            image.setImageBitmap(bitmap);
        }

        // 3. Check permissions
        // check permission for access location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        // 4. Get places data
        // If Google Client is not connected yet, call places data from OnConnected callback
        getCurrentPlaces();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loader.setVisibility(View.VISIBLE);
        nearby_place_recyclerview.setVisibility(View.GONE);
        getCurrentPlaces();
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
                break;
        }

        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        getCurrentPlaces();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // 4.1 get Places, only if Places not called yet
        if (!placeCalled) {
            getCurrentPlaces();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                add_location.setText(place.getName());
            }
        }
    }

    private synchronized void apiGoogleClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this, this, this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

    private void getCurrentPlaces() {
        // Only get places if permission granted and connected to google client
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && mGoogleApiClient.isConnected()) {
            placeCalled = true;

            // Get place result
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                    List<PlaceModel> places = new ArrayList<>();
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        places.add(new PlaceModel(placeLikelihood.getPlace().getId(), placeLikelihood.getPlace().getName().toString()));
                    }
                    likelyPlaces.release();

                    // If we get the places, put it to adapter
                    if (places.size() > 0) {
                        loader.setVisibility(View.GONE);
                        adapter.putDataset(places);
                        nearby_place_recyclerview.setVisibility(View.VISIBLE);
                    }
                }
            });
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
                            .build(GalleryShareActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
    }

}
