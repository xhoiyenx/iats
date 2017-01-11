package com.hoiyen.iats;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toolbar;

import com.hoiyen.iats.activities.BlogActivity;
import com.hoiyen.iats.activities.ChatActivity;
import com.hoiyen.iats.activities.GalleryActivity;
import com.hoiyen.iats.activities.ShopActivity;

public class BaseActivity extends Activity implements TabLayout.OnTabSelectedListener {

    private DrawerLayout menuDrawer;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        menuDrawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        final FrameLayout container = (FrameLayout) menuDrawer.findViewById(R.id.activityContainer);

        getLayoutInflater().inflate(layoutResID, container);
        super.setContentView(menuDrawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(null);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, menuDrawer, R.string.text_open, R.string.text_close);
        toggle.setDrawerIndicatorEnabled(true);
        menuDrawer.addDrawerListener(toggle);
        toggle.syncState();

        tabLayout = (TabLayout) findViewById(R.id.tab_menu);
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (menuDrawer.isDrawerOpen(GravityCompat.START)) {
                    menuDrawer.closeDrawer(GravityCompat.START);
                } else {
                    menuDrawer.openDrawer(GravityCompat.START);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Intent intent;
        switch (tab.getPosition()) {
            case 0:
                intent = new Intent(this, BlogActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case 1:
                intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                finish();
                break;

            case 2:
                intent = new Intent(this, GalleryActivity.class);
                startActivity(intent);
                finish();
                break;

            case 4:
                intent = new Intent(this, ShopActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Intent intent;
        switch (tab.getPosition()) {
            case 0:
                intent = new Intent(this, BlogActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            // if media reselected, go to home
            case 2:
                tabLayout.setScrollPosition(0, 0, false);
                break;
        }
    }

    /**
     * Get bottom tab object
     *
     * @return TabLayout
     */
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public void setTabPosition(final int position) {
        if (tabLayout != null) {
            tabLayout.setScrollPosition(position, 0, false);
        }
    }
}
