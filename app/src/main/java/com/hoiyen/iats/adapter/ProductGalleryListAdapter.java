package com.hoiyen.iats.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hoiyen.iats.R;
import com.hoiyen.iats.models.ProductMediaModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public final class ProductGalleryListAdapter extends RecyclerView.Adapter<ProductGalleryListAdapter.Holder> {

    private Context context;
    private List<ProductMediaModel> medias = new ArrayList<>();

    class Holder extends RecyclerView.ViewHolder {
        private ImageView image;
        public Holder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public ProductGalleryListAdapter(Context context) {
        this.context = context;
    }

    public void putDataset(List<ProductMediaModel> medias) {
        this.medias = medias;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_product_gallery, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final ProductMediaModel media = medias.get(position);
        final String image = media.getThumb();
        Picasso.with(context).load(image).resize(640, 360).centerCrop().into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (media.type.equals("image"))
                    return;
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + media.name));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(media.path));
                try {
                    context.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return medias.size();
    }
}
