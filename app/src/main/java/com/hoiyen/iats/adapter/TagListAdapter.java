package com.hoiyen.iats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoiyen.iats.R;
import com.hoiyen.iats.models.TagModel;

import java.util.ArrayList;
import java.util.List;

public final class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.Holder> {

    private Context context;
    private List<TagModel> tags = new ArrayList<>();
    private boolean editable = false;

    class Holder extends RecyclerView.ViewHolder {
        private TextView tagView;
        public Holder(View view) {
            super(view);
            tagView = (TextView) view.findViewById(R.id.name_text);
        }
    }

    public TagListAdapter(Context context) {
        this.context = context;
    }

    public void putTag(String tag) {
        tags.add(new TagModel(tag));
        notifyItemInserted(tags.size());
    }

    public List<TagModel> getTags() {
        return tags;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_tags, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final TagModel tag = tags.get(position);
        holder.tagView.setText(tag.name);
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

}
