package com.hoiyen.iats.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoiyen.iats.R;
import com.hoiyen.iats.models.ChatModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public final class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {

    private Activity context;
    private List<ChatModel> models = new ArrayList<>();

    class Holder extends RecyclerView.ViewHolder {
        private TextView message, time, date;
        private ImageView avatar;
        public Holder(View view) {
            super(view);
            message = (TextView) view.findViewById(R.id.message_text);
            time = (TextView) view.findViewById(R.id.time_text);
            date = (TextView) view.findViewById(R.id.date_text);
            avatar = (ImageView) view.findViewById(R.id.avatar_image);
        }
    }

    public ChatListAdapter(Activity context) {
        this.context = context;
    }

    public void putDataset(List<ChatModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    public void putData(ChatModel model) {
        models.add(model);
        notifyItemInserted(models.size());
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.list_chat_response, parent, false);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.list_chat_request, parent, false);
        }

        return new Holder(view);
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel model = models.get(position);
        return model.type;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ChatModel prevModel = null;

        if (position > 0) {
            prevModel = models.get(position - 1);
        }

        if (position == 0) {
            holder.date.setVisibility(View.VISIBLE);
        }

        ChatModel model = models.get(position);
        holder.message.setText(model.message);
        holder.time.setText(model.time);
        holder.date.setText(model.date);
        Picasso.with(context).load(model.avatar).into(holder.avatar);

        if (prevModel != null) {
            if (prevModel.member_id.equals(model.member_id)) {
                holder.avatar.setVisibility(View.INVISIBLE);
            }

            if (!prevModel.date.equals(model.date)) {
                holder.date.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (models != null) {
            return models.size();
        }
        return 0;
    }
}
