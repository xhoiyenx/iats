package com.hoiyen.iats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoiyen.iats.R;
import com.hoiyen.iats.models.SearchResultModel;

import java.util.ArrayList;
import java.util.List;

public final class SearchResultsAdaptaer extends RecyclerView.Adapter<SearchResultsAdaptaer.Holder> {

    private Context context;
    private List<SearchResultModel> results = new ArrayList<>();

    class Holder extends RecyclerView.ViewHolder {
        public Holder(View view) {
            super(view);
        }
    }

    public SearchResultsAdaptaer(Context context) {
        this.context = context;
    }

    public void putDataset(List<SearchResultModel> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.list_search_profile, parent, false);
                break;

            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.list_search_tags, parent, false);
                break;

            default:
                view = LayoutInflater.from(context).inflate(R.layout.list_search_products, parent, false);
                break;
        }

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final SearchResultModel result = results.get(position);

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public int getItemViewType(int position) {
        final SearchResultModel result = results.get(position);
        return result.type;
    }
}
