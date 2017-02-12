package com.hoiyen.iats.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoiyen.iats.R;
import com.hoiyen.iats.activities.BlogCaptionActivity;
import com.hoiyen.iats.models.PostModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.Holder> {

    private Context context;
    private List<PostModel> posts;

    class Holder extends RecyclerView.ViewHolder {

        private TextView userText, dateText, commentText, shareText, captionText, readmoreText, likeText;
        private ImageView imageView;
        private RecyclerView tagList;

        private TagListAdapter adapter;

        private Holder(View view) {
            super(view);

            userText = (TextView) view.findViewById(R.id.username_text);
            dateText = (TextView) view.findViewById(R.id.date_text);
            commentText = (TextView) view.findViewById(R.id.comment_text);
            shareText = (TextView) view.findViewById(R.id.share_button);
            captionText = (TextView) view.findViewById(R.id.caption_text);
            readmoreText = (TextView) view.findViewById(R.id.readmore_text);
            likeText = (TextView) view.findViewById(R.id.like_count_text);
            imageView = (ImageView) view.findViewById(R.id.image);
            tagList = (RecyclerView) view.findViewById(R.id.tagList);

            captionText.setSelected(true);

            // Readmore listener
            readmoreText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    readmoreText.setVisibility(View.GONE);
                    captionText.setMaxLines(10);
                    captionText.setSingleLine(false);
                }
            });

            // Setup tags
            adapter = new TagListAdapter(context);
            tagList.setHasFixedSize(true);
            tagList.setAdapter(adapter);
            tagList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        }
    }

    public FeedListAdapter(Context context) {
        this.context = context;
    }

    public void putDataset(List<PostModel> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_feed, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        final PostModel post = posts.get(position);
        holder.captionText.setText(post.caption);
        holder.userText.setText(post.username);
        holder.dateText.setText(post.published_at);
        Picasso.with(context).load(post.image).into(holder.imageView);

        // Comments listener
        holder.commentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BlogCaptionActivity.class);
                context.startActivity(intent);
            }
        });

        // Share listener
        holder.shareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Blog subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Blog content");
                context.startActivity(Intent.createChooser(intent, "Share via"));
            }
        });

        // Like count listener
        holder.likeText.setOnClickListener(new View.OnClickListener() {

            int liked = 0;

            @Override
            public void onClick(View v) {

                if (liked == 0) {
                    v.setSelected(true);
                    liked = 1;
                }
                else {
                    v.setSelected(false);
                    liked = 0;
                }

                if (v.isSelected()) {
                    ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_gold, 0, 0, 0);
                }
                else {
                    ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart, 0, 0, 0);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
