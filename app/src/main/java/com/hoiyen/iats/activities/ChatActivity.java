package com.hoiyen.iats.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.messaging.RemoteMessage;
import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.ChatListAdapter;
import com.hoiyen.iats.adapter.MemberOnlineListAdapter;
import com.hoiyen.iats.library.ApiRequest;
import com.hoiyen.iats.models.ChatModel;
import com.hoiyen.iats.models.UserModel;
import com.hoiyen.iats.utils.AppFirebaseMessagingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.onlineMemberList)
    RecyclerView onlineMemberList;

    @BindView(R.id.chatList)
    RecyclerView chatList;

    @BindView(R.id.loader_chat)
    ProgressBar loaderChat;

    @BindView(R.id.loader)
    ProgressBar loader;

    // SEND NEW COMMENT
    @BindView(R.id.imageButton)
    ImageButton submit;

    @BindView(R.id.editText)
    EditText text;

    private MemberOnlineListAdapter member_adapter = new MemberOnlineListAdapter(this);
    private ChatListAdapter chat_adapter = new ChatListAdapter(this);
    private final LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

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
        submit.setOnClickListener(ChatActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final String url = getString(R.string.api_online);
        ApiRequest.SendRequest(url, new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loaderChat.setVisibility(View.GONE);
                JSONArray data = response.optJSONArray("data");
                List<UserModel> models = new ArrayList<>();
                if (data.length() > 0) {
                    for (int i = 0; i < data.length(); i++) {
                        UserModel user = UserModel.parseJSON(data.optJSONObject(i));
                        if (user != null) {
                            models.add(user);
                        }
                    }
                }
                member_adapter.putDataset(models);
            }

            @Override
            public void onErrorResponse(String response) {

            }
        });

        chatMessage();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("update-chat"));
        super.onResume();
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

    @Override
    public void onClick(View v) {

        if (!text.getText().toString().equals("")) {
            sendMessage();
        }
        else {
            Toast.makeText(this, "Please type your message", Toast.LENGTH_SHORT).show();
        }

    }


    private void initView() {
        // Setup member recycler view
        onlineMemberList.setHasFixedSize(true);
        onlineMemberList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        onlineMemberList.setAdapter(member_adapter);

        // Setup chat recycler view
        layout.setStackFromEnd(true);
        chatList.setHasFixedSize(true);
        chatList.setLayoutManager(layout);
        chatList.setAdapter(chat_adapter);
        chat_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int count = chat_adapter.getItemCount();
                chatList.scrollToPosition(count - 1);
            }
        });
    }

    private void sendMessage() {
        final String url = getString(R.string.api_chat_send);
        Map<String, String> params = new HashMap<>();
        params.put("message", text.getText().toString());
        ApiRequest.PostRequest(url, params, new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ChatModel model = ChatModel.parseJSON(response);
                chat_adapter.putData(model);
                text.setText("");
            }

            @Override
            public void onErrorResponse(String response) {

            }
        });
    }

    private void chatMessage() {
        final String url = getString(R.string.api_chat_list);
        ApiRequest.SendRequest(url, new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray data = response.optJSONArray("data");
                List<ChatModel> models = ChatModel.parseJSON(data);
                chat_adapter.putDataset(models);
                loader.setVisibility(View.VISIBLE);
            }

            @Override
            public void onErrorResponse(String response) {

            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String chat = intent.getStringExtra("model");
            JSONObject chatModel = null;

            try {
                chatModel = new JSONObject(chat);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            if (chatModel != null) {
                ChatModel model = ChatModel.parseJSON(chatModel);
                model.type = 0;
                chat_adapter.putData(model);
            }
        }
    };
}
