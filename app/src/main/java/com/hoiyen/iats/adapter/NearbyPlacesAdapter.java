package com.hoiyen.iats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoiyen.iats.R;
import com.hoiyen.iats.models.PlaceModel;

import java.util.ArrayList;
import java.util.List;

public final class NearbyPlacesAdapter extends RecyclerView.Adapter<NearbyPlacesAdapter.Holder> {

    private Context context;
    private List<PlaceModel> places = new ArrayList<>();

    class Holder extends RecyclerView.ViewHolder {
        private TextView name;
        private Holder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_text);
        }
    }

    public NearbyPlacesAdapter(Context context) {
        this.context = context;
    }

    public void putDataset(List<PlaceModel> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_nearby_places, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        PlaceModel place = places.get(position);
        holder.name.setText(place.name);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

}
