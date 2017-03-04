package com.hoiyen.iats.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoiyen.iats.R;
import com.hoiyen.iats.activities.ProductActivity;
import com.hoiyen.iats.models.ProductMediaModel;
import com.hoiyen.iats.models.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public final class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.Holder> {

    private Context context;
    private List<ProductModel> products = new ArrayList<>();
    private int featured = 0;

    class Holder extends RecyclerView.ViewHolder {

        private TextView priceText, brandText, descText;
        private ImageView imageView;
        private CardView container;

        public Holder(View view) {
            super(view);

            priceText = (TextView) view.findViewById(R.id.price_text);
            brandText = (TextView) view.findViewById(R.id.brand_text);
            descText = (TextView) view.findViewById(R.id.short_desc);
            imageView = (ImageView) view.findViewById(R.id.image);
            container = (CardView) view.findViewById(R.id.container);

            descText.setSelected(true);
        }
    }

    public ProductListAdapter(Context context, int featured) {
        this.context = context;
        this.featured = featured;
    }

    public void putDataset(List<ProductModel> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (featured == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.list_shop_featured, parent, false);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.list_shop_product, parent, false);
        }
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final ProductModel product = products.get(position);

        holder.brandText.setText(product.brand);
        holder.priceText.setText(product.formattedPrice());
        holder.descText.setText(product.short_desc);

        if (product.media != null && product.media.size() > 0) {
            ProductMediaModel media = product.media.get(0);
            Picasso.with(context).load(media.getThumb()).into(holder.imageView);
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("id", product.id);
                //intent.putExtra("product", product);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
