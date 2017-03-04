package com.hoiyen.iats.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoiyen.iats.R;
import com.hoiyen.iats.library.ApiRequest;
import com.hoiyen.iats.models.CartModel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class CartItemListAdapter extends RecyclerView.Adapter<CartItemListAdapter.Holder> {

    private Activity context;
    private List<CartModel> models = new ArrayList<>();

    class Holder extends RecyclerView.ViewHolder {

        private TextView brand, article, color, price, button;
        private ImageView image;

        public Holder(View view) {
            super(view);
            brand = (TextView) view.findViewById(R.id.brand_text);
            article = (TextView) view.findViewById(R.id.article_text);
            color = (TextView) view.findViewById(R.id.color_text);
            price = (TextView) view.findViewById(R.id.price_text);
            image = (ImageView) view.findViewById(R.id.image);
            button = (TextView) view.findViewById(R.id.cart_delete_item);
        }
    }

    public CartItemListAdapter(Activity context) {
        this.context = context;
    }

    public void putDataset(List<CartModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_cart, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final CartModel model = models.get(position);

        holder.brand.setText(model.brand);
        holder.article.setText(model.article);
        holder.color.setText(model.color);
        holder.price.setText("Rp. " + model.formattedPrice + ",-");
        if (!model.image.equals("")) {
            Picasso.with(context).load(model.image).into(holder.image);
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Send request to delete cart
                new AlertDialog.Builder(context)
                        .setTitle(R.string.text_confirm_delete_cart)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final String url = context.getString(R.string.api_cart_delete).concat(model.id);
                                ApiRequest.SendRequest(url, new ApiRequest.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        context.finish();
                                        context.startActivity(context.getIntent());
                                    }

                                    @Override
                                    public void onErrorResponse(String response) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
