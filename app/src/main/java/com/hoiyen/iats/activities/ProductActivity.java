package com.hoiyen.iats.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.hoiyen.iats.BaseActivity;
import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.ProductGalleryListAdapter;
import com.hoiyen.iats.adapter.ProductSizeListAdapter;
import com.hoiyen.iats.library.ApiRequest;
import com.hoiyen.iats.models.ProductMediaModel;
import com.hoiyen.iats.models.ProductModel;
import com.hoiyen.iats.models.ProductUnitModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductActivity extends Activity implements View.OnClickListener {

    private RecyclerView galleryList, sizeList;
    private ProductGalleryListAdapter galleryListAdapter;
    private ProductSizeListAdapter sizeListAdapter;

    private Toolbar toolbar;
    private ActionBar actionBar;
    private ProgressBar loader;
    private NestedScrollView container;
    private TextView brandText, shortDesc, descriptionText, articleList, colorList, totalAmount;
    private ImageView mainImage;
    private Button submit;

    /*
    private List<String> articleIDList = new ArrayList<>();
    private List<String> articleNameList = new ArrayList<>();
    */
    private String product_id, pid, size_type;
    private int selectedArticle = -1;
    private int selectedColor = -1;
    private String[] articleIDList;
    private String[] articleNameList;
    private String[] colorIDList;
    private String[] colorNameList;
    private String[] productIDList;
    private double totalSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        loader = (ProgressBar) findViewById(R.id.loader);
        container = (NestedScrollView) findViewById(R.id.activity_product);
        brandText = (TextView) findViewById(R.id.brand_text);
        shortDesc = (TextView) findViewById(R.id.short_desc);
        descriptionText = (TextView) findViewById(R.id.description);
        galleryList = (RecyclerView) findViewById(R.id.galleryList);
        sizeList = (RecyclerView) findViewById(R.id.sizeList);
        mainImage = (ImageView) findViewById(R.id.image);
        articleList = (TextView) findViewById(R.id.article_list);
        colorList = (TextView) findViewById(R.id.color_list);
        totalAmount = (TextView) findViewById(R.id.total_amount);
        submit = (Button) findViewById(R.id.cart_button);

        galleryListAdapter = new ProductGalleryListAdapter(this);
        galleryList.setHasFixedSize(true);
        galleryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        galleryList.setAdapter(galleryListAdapter);

        sizeListAdapter = new ProductSizeListAdapter(this, new ProductSizeListAdapter.SizeListener() {
            @Override
            public void onItemSelected(ProductUnitModel model) {
                Log.d("Unit", "selected");
                totalSize += model.size;
                setTotal();
            }

            @Override
            public void onItemDeselected(ProductUnitModel model) {
                Log.d("Unit", "deselected");
                totalSize -= model.size;
                setTotal();
            }
        });

        sizeList.setHasFixedSize(true);
        sizeList.setLayoutManager(new GridLayoutManager(this, 3));
        sizeList.setAdapter(sizeListAdapter);
        submit.setOnClickListener(ProductActivity.this);

        show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setActionBar(toolbar);
        actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void show() {

        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        final String id = bundle.getString("id");
        final String url = getString(R.string.api_product_detail).concat(id);
        product_id = id;

        ApiRequest.SendRequest(url, new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                container.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);

                ProductModel product = ProductModel.parseJSON(response);

                if (actionBar != null) {
                    actionBar.setTitle(product.brand);
                }

                brandText.setText(product.brand);
                shortDesc.setText(product.short_desc);
                descriptionText.setText(product.description);
                size_type = product.unit_type;

                // images
                if (product.media != null) {
                    if (product.media.size() > 0) {
                        ProductMediaModel media = product.media.get(0);
                        Picasso.with(ProductActivity.this).load(media.getThumb()).into(mainImage);
                        galleryListAdapter.putDataset(product.media);
                    }
                }

                // article list
                JSONArray articles = response.optJSONArray("article");
                if (articles != null && articles.length() > 0) {
                    populateArticle(articles);
                    articleList.setOnClickListener(ProductActivity.this);
                }

                setTotal();
            }

            @Override
            public void onErrorResponse(String response) {

            }
        });

    }

    private void populateArticle(JSONArray data) {
        articleNameList = new String[data.length()];
        articleIDList = new String[data.length()];

        for (int i = 0; i < data.length(); i++) {
            JSONObject article = data.optJSONObject(i);
            articleIDList[i] = article.optString("article_id");
            articleNameList[i] = article.optString("name");
        }
    }

    private void populateColor(String article_id) {

        String url = getString(R.string.api_product_color);
        url = url.concat("product_id=" + product_id);
        url = url.concat("&article_id=" + article_id);

        ApiRequest.SendRequest(url, new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray data = response.optJSONArray("data");
                if (data.length() > 0) {
                    colorList.setVisibility(View.VISIBLE);
                    colorIDList = new String[data.length()];
                    colorNameList = new String[data.length()];
                    productIDList = new String[data.length()];

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject color = data.optJSONObject(i);
                        colorIDList[i] = color.optString("id");
                        colorNameList[i] = color.optString("name");
                        productIDList[i] = color.optString("pid");
                    }

                    colorList.setOnClickListener(ProductActivity.this);
                }
            }

            @Override
            public void onErrorResponse(String response) {

            }
        });

    }

    /**
     * Call this when user select color
     */
    private void populateUnitSizes() {

        final String url = getString(R.string.api_product_units).concat("pid=" + pid);
        ApiRequest.SendRequest(url, new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray data = response.optJSONArray("data");
                final List<ProductUnitModel> models = ProductUnitModel.parseJSON(data);
                sizeListAdapter.setUnit_type(size_type);
                sizeListAdapter.putDataset(models);

            }

            @Override
            public void onErrorResponse(String response) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.article_list: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(articleNameList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        articleList.setText(articleNameList[which]);
                        colorList.setText("Select color");
                        selectedArticle = which;
                        selectedColor = -1;

                        // populate color
                        populateColor(articleIDList[selectedArticle]);
                    }
                });
                builder.show();
            }
            break;

            /**
             * Color selected, show availables sizes. Only if product unit_type is Sqf
             */
            case R.id.color_list: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(colorNameList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        colorList.setText(colorNameList[which]);
                        selectedColor = which;
                        pid = productIDList[which];

                        populateUnitSizes();
                        resetTotal();

                    }
                });
                builder.show();
            }
            break;

            /**
             * User select buy
             */
            case R.id.cart_button: {
                if (pid == null || pid.equals("")) {
                    Toast.makeText(this, "Please choose article & color", Toast.LENGTH_SHORT).show();
                }
                else {

                }
            }
            break;

        }
    }

    private void setTotal() {
        totalAmount.setText(String.format(Locale.US, "%.2f", totalSize).concat(" " + size_type));
    }

    private void resetTotal() {
        totalSize = 0;
        setTotal();
    }
}
