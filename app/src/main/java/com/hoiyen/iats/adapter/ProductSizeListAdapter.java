package com.hoiyen.iats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoiyen.iats.R;

public final class ProductSizeListAdapter extends RecyclerView.Adapter<ProductSizeListAdapter.Holder> {

    private Context context;

    class Holder extends RecyclerView.ViewHolder {
        private TextView size;
        public Holder(View itemView) {
            super(itemView);
            size = (TextView) itemView.findViewById(R.id.sizeText);
            size.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( view.isSelected() ) {
                        view.setSelected(false);
                    }
                    else {
                        view.setSelected(true);
                    }
                }
            });
        }
    }

    public ProductSizeListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_product_size, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 18;
    }
}
