package com.cursfundacionesplai.restasearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cursfundacionesplai.restasearch.EstablimentDetailActivity;
import com.cursfundacionesplai.restasearch.R;
import com.cursfundacionesplai.restasearch.models.Restaurant;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Restaurant> restaurants;

    public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_restaurant_item, parent, false);
        RestaurantAdapter.ViewHolder holder = new RestaurantAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RestaurantAdapter.ViewHolder holder, int position) {

        Restaurant r = restaurants.get(position);

        holder.name.setText(r.getName());

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;

        TextView name;
        TextView address;
        TextView rating;
        TextView reviews;

        public ViewHolder(View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.rest_item_photo);

            name = itemView.findViewById(R.id.rest_item_name);
            address = itemView.findViewById(R.id.rest_item_address);
            rating = itemView.findViewById(R.id.rest_item_rating);
            reviews = itemView.findViewById(R.id.rest_item_num_reviews);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EstablimentDetailActivity.class);
                    intent.putExtra("place_id", restaurants.get(getAdapterPosition()).getPlace_id());

                    context.startActivity(intent);
                }
            });
        }
    }
}