package com.hoiyen.iats.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.CommentListAdapter;
import com.hoiyen.iats.library.ApiRequest;
import com.hoiyen.iats.models.CommentModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class BlogCaptionActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.commentList)
    RecyclerView commentList;

    @BindView(R.id.name_text)
    TextView username;

    @BindView(R.id.comment_text)
    TextView caption;

    @BindView(R.id.date_text)
    TextView date;

    @BindView(R.id.avatar_image)
    CircleImageView avatar;

    // SEND NEW COMMENT
    @BindView(R.id.imageButton)
    ImageButton submit;

    @BindView(R.id.editText)
    EditText text;

    private String post_id;
    private CommentListAdapter adapter = new CommentListAdapter(this);
    private final LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

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

        Bundle bundle = getIntent().getExtras();
        if (!bundle.containsKey("post_id")) {
            finish();
        }

        post_id = bundle.getString("post_id");
        getDetail();
    }

    @Override
    protected void onStart() {
        super.onStart();


        layout.setStackFromEnd(true);
        commentList.setHasFixedSize(true);
        commentList.setAdapter(adapter);
        commentList.setLayoutManager(layout);

        submit.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (text.getText().toString().equals("")) {
            Toast.makeText(this, "Please insert your message", Toast.LENGTH_SHORT).show();
        }
        else {

            final String url = getString(R.string.api_send_comment);
            final Map<String, String> param = new HashMap<>();
            param.put("post_id", post_id);
            param.put("comment", text.getText().toString());
            ApiRequest.PostRequest(url, param, new ApiRequest.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONObject member = response.optJSONObject("member");
                    CommentModel comment = new CommentModel(
                            member.optString("username"),
                            member.optString("avatar_url"),
                            response.optString("description"),
                            response.optString("date")
                    );
                    adapter.putData(comment);
                    text.setText("");
                    commentList.scrollToPosition(adapter.getItemCount());
                }

                @Override
                public void onErrorResponse(String response) {

                }
            });

        }
    }

    private void getDetail() {
        final String url = getString(R.string.api_caption_detail).concat(post_id);
        ApiRequest.SendRequest(url, new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                username.setText(response.optString("member_username"));
                caption.setText(response.optString("caption"));
                date.setText(response.optString("date"));
                Picasso.with(BlogCaptionActivity.this).load(response.optString("member_avatar")).into(avatar);

                // Process comments
                JSONArray arrayComments = response.optJSONArray("comments");
                if (arrayComments.length() > 0) {
                    final List<CommentModel> comments = new ArrayList<>(arrayComments.length());

                    for (int i = 0; i < arrayComments.length(); i++) {
                        JSONObject comment = arrayComments.optJSONObject(i);
                        JSONObject member = comment.optJSONObject("member");

                        comments.add(new CommentModel(
                                member.optString("username"),
                                member.optString("avatar_url"),
                                comment.optString("description"),
                                comment.optString("date")
                        ));
                    }

                    adapter.putDataset(comments);
                }
            }

            @Override
            public void onErrorResponse(String response) {

            }
        });
    }

}
