package com.hoiyen.iats.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
                }
            });
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
    public void onBindViewHolder(Holder holder, int position) {

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
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
