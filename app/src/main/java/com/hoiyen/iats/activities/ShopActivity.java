package com.hoiyen.iats.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hoiyen.iats.BaseActivity;
import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.FeaturedListAdapter;
import com.hoiyen.iats.adapter.ProductListAdapter;

public class ShopActivity extends BaseActivity {

    private RecyclerView featuredList;
    private RecyclerView materialList;
    private RecyclerView merchandiseList;
    private FeaturedListAdapter featuredListAdapter;
    private ProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        setTabPosition(4);

        featuredListAdapter = new FeaturedListAdapter(this);
        productListAdapter = new ProductListAdapter(this);

        featuredList = (RecyclerView) findViewById(R.id.featuredList);
        featuredList.setNestedScrollingEnabled(false);
        featuredList.setHasFixedSize(true);
        featuredList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        featuredList.setAdapter(featuredListAdapter);

        materialList = (RecyclerView) findViewById(R.id.materialList);
        materialList.setNestedScrollingEnabled(false);
        materialList.setHasFixedSize(true);
        materialList.setLayoutManager(new GridLayoutManager(this, 2));
        materialList.setAdapter(productListAdapter);

        merchandiseList = (RecyclerView) findViewById(R.id.merchandiseList);
        merchandiseList.setNestedScrollingEnabled(false);
        merchandiseList.setHasFixedSize(true);
        merchandiseList.setLayoutManager(new GridLayoutManager(this, 2));
        merchandiseList.setAdapter(productListAdapter);
    }
}
