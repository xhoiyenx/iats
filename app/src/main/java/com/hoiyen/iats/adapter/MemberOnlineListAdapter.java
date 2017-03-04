package com.hoiyen.iats.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoiyen.iats.R;
import com.hoiyen.iats.models.MemberModel;
import com.hoiyen.iats.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public final class MemberOnlineListAdapter extends RecyclerView.Adapter<MemberOnlineListAdapter.Holder> {

    private Activity context;
    private List<UserModel> members = new ArrayList<>();

    class Holder extends RecyclerView.ViewHolder {
        private TextView name;
        private CircleImageView avatar;
        private Holder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_text);
            avatar = (CircleImageView) view.findViewById(R.id.avatar_image);
        }
    }

    public MemberOnlineListAdapter(Activity context) {
        this.context = context;
    }

    public void putDataset(List<UserModel> members) {
        this.members = members;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_chat_member, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        UserModel model = members.get(position);
        holder.name.setText(model.username);
        Picasso.with(context).load(model.avatar_url).resize(200, 200).centerCrop().placeholder(R.drawable.avatar_placeholder).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }
}
