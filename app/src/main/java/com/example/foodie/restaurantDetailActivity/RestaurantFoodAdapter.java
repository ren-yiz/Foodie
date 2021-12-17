package com.example.foodie.restaurantDetailActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.foodie.R;
import com.example.foodie.model.Food;
import com.example.foodie.userPanel.ItemClickListener;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantFoodAdapter extends RecyclerView.Adapter<RestaurantFoodAdapter.ViewHolder> {
    private Context context;
    private List<Food> foods;
    DatabaseReference databaseReference;
    private ItemClickListener listener;

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public List getFoods() {
        return foods;
    }

    public int getFoodCount() {
        return this.foods.size();
    }

    public Food getItem(int position) {
        return foods.get(position);
    }

    public void setFood(Food food, int position) {
        foods.set(position, food);
    }

    public RestaurantFoodAdapter(Context context, List<Food> foods) {
        this.context = context;
        this.foods = foods;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public RestaurantFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_detail_page_food_card,parent,false);
        return new RestaurantFoodAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantFoodAdapter.ViewHolder holder, int position) {
        final Food food = foods.get(position);
        holder.foodName.setText(food.getTitle());
        Picasso.get().load(food.getImage()).into(holder.foodImage);
        holder.foodDescription.setText(food.getDescription());
        holder.foodPrice.setText(food.getPrice());
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView foodImage;
        TextView foodName,foodDescription,foodPrice;
        ElegantNumberButton numberButton;

        public ViewHolder(@NonNull View itemView, final ItemClickListener listener) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_title);
            foodDescription = itemView.findViewById(R.id.food_description);
            foodPrice = itemView.findViewById(R.id.food_price);
            numberButton = itemView.findViewById(R.id.number_button);

            numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    String quantity = numberButton.getNumber();
                    if ( Integer.valueOf(quantity) > 0) {
                        int position = getLayoutPosition();
                        foods.get(position).setQuantity(quantity);
                    }
                }
            });

//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        int position = getLayoutPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            listener.onItemClick(position);
//                        }
//                    }
//                }
//            });
        }
    }
}
