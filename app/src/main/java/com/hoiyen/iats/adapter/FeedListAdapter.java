package com.hoiyen.iats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoiyen.iats.R;

public final class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.Holder> {

    private Context context;

    public FeedListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.list_feed_gallery, parent, false);
        View view = LayoutInflater.from(context).inflate(R.layout.list_feed, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    protected class Holder extends RecyclerView.ViewHolder {

        TextView caption;

        private Holder(View view) {
            super(view);

            caption = (TextView) view.findViewById(R.id.caption_text);
            caption.setSelected(true);

        }
    }

}
