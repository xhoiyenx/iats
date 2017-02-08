package com.hoiyen.iats.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoiyen.iats.R;
import com.hoiyen.iats.activities.BlogCaptionActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.Holder> {

    private Context context;

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.comment_text)
        TextView comments;

        @BindView(R.id.share_button)
        TextView share;

        @BindView(R.id.caption_text)
        TextView caption;

        @BindView(R.id.readmore_text)
        TextView readmore;

        @BindView(R.id.like_count_text)
        TextView like;

        @BindView(R.id.tagList)
        RecyclerView tagList;

        private TagListAdapter adapter;

        private Holder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            caption.setSelected(true);

            // Readmore listener
            readmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    readmore.setVisibility(View.GONE);
                    caption.setMaxLines(10);
                    caption.setSingleLine(false);
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

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_feed, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        // Comments listener
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BlogCaptionActivity.class);
                context.startActivity(intent);
            }
        });

        // Share listener
        holder.share.setOnClickListener(new View.OnClickListener() {
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
        holder.like.setOnClickListener(new View.OnClickListener() {

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
        return 10;
    }
}
