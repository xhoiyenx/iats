package com.hoiyen.iats.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hoiyen.iats.BaseActivity;
import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.FeedListAdapter;

public class BlogActivity extends BaseActivity {

    private RecyclerView blogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        blogList = (RecyclerView) findViewById(R.id.blogList);
        blogList.setHasFixedSize(true);

        FeedListAdapter adapter = new FeedListAdapter(this);
        blogList.setAdapter(adapter);
        blogList.setLayoutManager(new LinearLayoutManager(this));
    }
}
