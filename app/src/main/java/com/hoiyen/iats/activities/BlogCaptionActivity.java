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
import com.hoiyen.iats.adapter.CommentListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlogCaptionActivity extends Activity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.commentList)
    RecyclerView commentList;

    private CommentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_caption);
        ButterKnife.bind(this);

        setActionBar(toolbar);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.text_comments);
        }

        adapter = new CommentListAdapter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        commentList.setHasFixedSize(true);
        commentList.setAdapter(adapter);
        commentList.setLayoutManager(new LinearLayoutManager(this));
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
