package com.hoiyen.iats.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hoiyen.iats.BaseActivity;
import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.ProductGalleryListAdapter;
import com.hoiyen.iats.adapter.ProductSizeListAdapter;

public class ProductActivity extends BaseActivity {

    private RecyclerView galleryList, sizeList;
    private ProductGalleryListAdapter galleryListAdapter;
    private ProductSizeListAdapter sizeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        setTabPosition(4);

        galleryListAdapter = new ProductGalleryListAdapter(this);
        sizeListAdapter = new ProductSizeListAdapter(this);

        galleryList = (RecyclerView) findViewById(R.id.galleryList);
        galleryList.setHasFixedSize(true);
        galleryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        galleryList.setAdapter(galleryListAdapter);

        sizeList = (RecyclerView) findViewById(R.id.sizeList);
        sizeList.setHasFixedSize(true);
        sizeList.setLayoutManager(new GridLayoutManager(this, 3));
        sizeList.setAdapter(sizeListAdapter);
    }
}
