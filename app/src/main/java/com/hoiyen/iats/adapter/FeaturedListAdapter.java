package com.hoiyen.iats.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoiyen.iats.activities.ProductActivity;
import com.hoiyen.iats.R;

public final class FeaturedListAdapter extends RecyclerView.Adapter<FeaturedListAdapter.Holder> {

    private Context context;

    class Holder extends RecyclerView.ViewHolder {
        private Holder(View itemView) {
            super(itemView);

            CardView container = (CardView) itemView.findViewById(R.id.container);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    public FeaturedListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_shop_featured, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
