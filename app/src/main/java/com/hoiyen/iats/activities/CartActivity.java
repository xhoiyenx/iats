package com.hoiyen.iats.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.CartItemListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends Activity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.cartList)
    RecyclerView cartList;

    private CartItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        setActionBar(toolbar);
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.text_my_orders);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        adapter = new CartItemListAdapter(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        cartList.setLayoutManager(new LinearLayoutManager(this));
        cartList.setHasFixedSize(true);
        cartList.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
