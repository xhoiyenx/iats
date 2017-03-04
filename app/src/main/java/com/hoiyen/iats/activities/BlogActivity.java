package com.hoiyen.iats.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.hoiyen.iats.BaseActivity;
import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.FeedListAdapter;
import com.hoiyen.iats.library.ApiRequest;
import com.hoiyen.iats.models.PostModel;
import com.hoiyen.iats.utils.SampleData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class BlogActivity extends BaseActivity {

    private RecyclerView blogList;
    private ProgressBar loader;
    private FeedListAdapter adapter = new FeedListAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        loader = (ProgressBar)  findViewById(R.id.loader);
        blogList = (RecyclerView) findViewById(R.id.blog_list);
        blogList.setHasFixedSize(true);
        blogList.setAdapter(adapter);
        blogList.setLayoutManager(new LinearLayoutManager(this));

        final String url = getString(R.string.api_post);
        ApiRequest.SendRequest(url, new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loader.setVisibility(View.GONE);
                JSONArray data = response.optJSONArray("data");
                List<PostModel> posts = PostModel.parseList(data);
                adapter.putDataset(posts);

            }

            @Override
            public void onErrorResponse(String response) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTabPosition(0);
    }
}
