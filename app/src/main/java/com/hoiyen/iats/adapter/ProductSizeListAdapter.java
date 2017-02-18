package com.hoiyen.iats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoiyen.iats.R;
import com.hoiyen.iats.models.ProductUnitModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ProductSizeListAdapter extends RecyclerView.Adapter<ProductSizeListAdapter.Holder> {

    private Context context;
    private List<ProductUnitModel> models = new ArrayList<>();
    private SizeListener listener;
    private String unit_type = "";

    public interface SizeListener {
        void onItemSelected(ProductUnitModel model);
        void onItemDeselected(ProductUnitModel model);
    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView size;
        public Holder(View itemView) {
            super(itemView);
            size = (TextView) itemView.findViewById(R.id.sizeText);
        }
    }

    public ProductSizeListAdapter(Context context, SizeListener listener) {
        this.context = context;
        this.listener = listener;

    }

    public void putDataset(List<ProductUnitModel> models) {
        this.models = models;
        notifyDataSetChanged();
        //notifyItemRangeChanged(0, models.size());
    }

    public void setUnit_type(String unit_type) {
        this.unit_type = unit_type;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_product_size, parent, false);
        return new Holder(view);
    }

    @Override
    public void onViewRecycled(Holder holder) {
        super.onViewRecycled(holder);
        holder.size.setSelected(false);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final ProductUnitModel model = models.get(position);
        holder.size.setText(String.format(Locale.US, "%.2f", model.size).concat(" " + unit_type));
        holder.size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( view.isSelected() ) {
                    view.setSelected(false);
                    listener.onItemDeselected(model);
                }
                else {
                    view.setSelected(true);
                    listener.onItemSelected(model);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
