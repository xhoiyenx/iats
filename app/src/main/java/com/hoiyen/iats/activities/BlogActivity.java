package com.hoiyen.iats.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hoiyen.iats.BaseActivity;
import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.FeedListAdapter;
import com.hoiyen.iats.utils.SampleData;

public class BlogActivity extends BaseActivity {

    private RecyclerView blogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        blogList = (RecyclerView) findViewById(R.id.blog_list);
        blogList.setHasFixedSize(true);

        FeedListAdapter adapter = new FeedListAdapter(this);
        blogList.setAdapter(adapter);
        blogList.setLayoutManager(new LinearLayoutManager(this));
        adapter.putDataset(SampleData.loadPosts());

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTabPosition(0);
    }
}
