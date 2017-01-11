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
import com.hoiyen.iats.adapter.MemberOnlineListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends Activity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.member_list)
    RecyclerView member_rv;

    @BindView(R.id.chat_list)
    RecyclerView chat_rv;

    private MemberOnlineListAdapter member_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setActionBar(toolbar);
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.text_chat);
        }

        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, BlogActivity.class));
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, BlogActivity.class));
        finish();
    }

    private void initView() {
        member_adapter = new MemberOnlineListAdapter(this);

        // Setup member recycler view
        member_rv.setHasFixedSize(true);
        member_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        member_rv.setAdapter(member_adapter);
    }
}
