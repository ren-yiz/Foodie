package com.example.foodie.userPanel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodie.R;
import com.example.foodie.model.Restaurant;
import com.example.foodie.userPanel.ItemClickListener;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    private List<Restaurant> restaurantModelsList;
    DatabaseReference databaseReference;
    private ItemClickListener listener;

    public HomeAdapter(Context context, List<Restaurant> restaurantModelsList) {
        this.context = context;
        this.restaurantModelsList = restaurantModelsList;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.homepage_restaurant_card,parent,false);
        return new HomeAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        final Restaurant restaurant = restaurantModelsList.get(position);
        Glide.with(context).load(restaurant.getImageURL()).into(holder.imageView);
        holder.RestaurantName.setText(restaurant.getRestaurantName());
        Picasso.get().load(restaurant.getImageURL()).into(holder.imageView);
        holder.Rating.setText(restaurant.getRestaurantId());
        holder.RestaurantAddress.setText(restaurant.getAddress());
    }

    @Override
    public int getItemCount() {
        return restaurantModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView RestaurantName,Rating,RestaurantAddress;

        public ViewHolder(@NonNull View itemView, final ItemClickListener listener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.restaurant_image);
            RestaurantName = itemView.findViewById(R.id.restaurant_name);
            Rating = itemView.findViewById(R.id.restaurant_rating);
            RestaurantAddress = itemView.findViewById(R.id.restaurant_address);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
