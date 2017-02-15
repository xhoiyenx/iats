package com.hoiyen.iats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoiyen.iats.R;
import com.hoiyen.iats.models.CommentModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public final class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.Holder> {

    private Context context;
    private List<CommentModel> comments = new ArrayList<>();

    class Holder extends RecyclerView.ViewHolder {
        private TextView username, caption, date;
        private CircleImageView avatar;
        public Holder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.name_text);
            caption = (TextView) view.findViewById(R.id.comment_text);
            date = (TextView) view.findViewById(R.id.date_text);
            avatar = (CircleImageView) view.findViewById(R.id.avatar_image);
        }
    }

    public CommentListAdapter(Context context) {
        this.context = context;
    }

    public void putDataset(List<CommentModel> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public void putData(CommentModel comment) {
        comments.add(comment);
        notifyItemInserted(comments.size());
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_comment, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final CommentModel comment = comments.get(position);

        holder.username.setText(comment.username);
        holder.caption.setText(comment.comment);
        holder.date.setText(comment.date);
        Picasso.with(context).load(comment.avatar).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
