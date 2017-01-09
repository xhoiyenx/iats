package com.hoiyen.iats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoiyen.iats.R;
import com.hoiyen.iats.models.MemberModel;

import java.util.ArrayList;
import java.util.List;

public final class MemberOnlineListAdapter extends RecyclerView.Adapter<MemberOnlineListAdapter.Holder> {

    private Context context;
    private List<MemberModel> members = new ArrayList<>();

    class Holder extends RecyclerView.ViewHolder {
        private Holder(View view) {
            super(view);
        }
    }

    public MemberOnlineListAdapter(Context context) {
        this.context = context;
    }

    public void putDataset(List<MemberModel> members) {
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

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
