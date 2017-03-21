package com.hoiyen.iats.activities;

import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hoiyen.iats.BaseActivity;
import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.FeaturedListAdapter;
import com.hoiyen.iats.adapter.ProductListAdapter;
import com.hoiyen.iats.library.ApiRequest;
import com.hoiyen.iats.models.ProductModel;
import com.hoiyen.iats.utils.FunctionHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ShopActivity extends BaseActivity {

    private final String TAG = "Shop";
    private LinearLayout container;
    private String apiUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        setTabPosition(4);

        container = (LinearLayout) findViewById(R.id.container);
        apiUrl = getString(R.string.api_product_list);
        showFeatured();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void showFeatured() {
        ApiRequest.SendRequest(apiUrl.concat("featured"), new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final JSONArray data = response.optJSONArray("data");
                if (data.length() > 0) {

                    // Process data
                    final List<ProductModel> products = ProductModel.parseJSON(data);

                    // Create title
                    container.addView(titleView("Featured"));

                    // Create list
                    ProductListAdapter adapter = new ProductListAdapter(ShopActivity.this, 1);
                    RecyclerView list = listView();
                    list.setAdapter(adapter);
                    container.addView(list);

                    adapter.putDataset(products);

                }

                showPremium();
            }

            @Override
            public void onErrorResponse(String response) {
                Log.e(TAG, "Error");
            }
        });
    }

    private void showPremium() {
        ApiRequest.SendRequest(apiUrl.concat("leather"), new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final JSONArray data = response.optJSONArray("data");
                if (data.length() > 0) {

                    // Process data
                    final List<ProductModel> products = ProductModel.parseJSON(data);

                    // Create title
                    container.addView(titleView("Premium Leather"));

                    // Create list
                    ProductListAdapter adapter = new ProductListAdapter(ShopActivity.this, 0);
                    RecyclerView list = listView();
                    list.setAdapter(adapter);
                    container.addView(list);

                    adapter.putDataset(products);

                }

                showMaterial();
            }

            @Override
            public void onErrorResponse(String response) {

            }
        });
    }


    private void showMaterial() {
        ApiRequest.SendRequest(apiUrl.concat("material"), new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final JSONArray data = response.optJSONArray("data");
                if (data.length() > 0) {

                    // Process data
                    final List<ProductModel> products = ProductModel.parseJSON(data);

                    // Create title
                    container.addView(titleView("Material"));

                    // Create list
                    ProductListAdapter adapter = new ProductListAdapter(ShopActivity.this, 0);
                    RecyclerView list = listView();
                    list.setAdapter(adapter);
                    container.addView(list);

                    adapter.putDataset(products);

                }
                showMerchandise();
            }

            @Override
            public void onErrorResponse(String response) {

            }
        });
    }

    private void showMerchandise() {
        ApiRequest.SendRequest(apiUrl.concat("merchandise"), new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final JSONArray data = response.optJSONArray("data");
                if (data.length() > 0) {

                    // Process data
                    final List<ProductModel> products = ProductModel.parseJSON(data);

                    // Create title
                    container.addView(titleView("Merchandise"));

                    // Create list
                    ProductListAdapter adapter = new ProductListAdapter(ShopActivity.this, 0);
                    RecyclerView list = listView();
                    list.setAdapter(adapter);
                    container.addView(list);

                    adapter.putDataset(products);

                }
            }

            @Override
            public void onErrorResponse(String response) {

            }
        });
    }


    private TextView titleView(String text) {

        int padding = FunctionHelper.dptopx(ShopActivity.this, 8);

        TextView titleView = new TextView(ShopActivity.this);
        titleView.setText(text);
        titleView.setPadding(padding, padding, padding, padding);
        titleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return titleView;

    }

    private RecyclerView listView() {

        RecyclerView listView = new RecyclerView(ShopActivity.this);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(ShopActivity.this, LinearLayoutManager.HORIZONTAL, false));
        listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return listView;

    }
}
