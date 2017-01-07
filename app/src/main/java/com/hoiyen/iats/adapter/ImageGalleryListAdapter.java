package com.hoiyen.iats.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hoiyen.iats.R;
import com.hoiyen.iats.activities.GalleryProcessActivity;
import com.hoiyen.iats.models.ImageModel;
import com.hoiyen.iats.utils.FunctionHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ImageGalleryListAdapter extends RecyclerView.Adapter<ImageGalleryListAdapter.Holder> {

    private Context context;
    private List<ImageModel> images = new ArrayList<>();

    class Holder extends RecyclerView.ViewHolder {
        private ImageView image;
        private Holder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.photo_image);
        }
    }

    public ImageGalleryListAdapter(Context context) {
        this.context = context;
    }

    public void putDataset(List<ImageModel> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public void update(List<ImageModel> images) {
        this.images.addAll(images);
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_image_gallery, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final ImageModel image = images.get(position);
        final Point size = FunctionHelper.getDisplaySize(context);
        int dimension = size.x / 2;
        Picasso.with(context).load(new File(image.filepath)).resize(dimension, dimension).centerCrop().into(holder.image, new Callback() {
            @Override
            public void onSuccess() {
                // Bind listener
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(context, GalleryProcessActivity.class);
                        intent.putExtra("media", image.filepath);
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onError() {
                Log.e("failed", image.filepath);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
