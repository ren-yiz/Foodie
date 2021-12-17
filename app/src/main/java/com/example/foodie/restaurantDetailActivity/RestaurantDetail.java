package com.example.foodie.restaurantDetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.foodie.R;
import com.example.foodie.UserPanelBottomNavigation;
import com.example.foodie.database.Database;
import com.example.foodie.model.Food;
import com.example.foodie.model.Order;
import com.example.foodie.model.Restaurant;
import com.example.foodie.userPanel.ShoppingCartFragment;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RestaurantDetail extends AppCompatActivity {
    private ArrayList<Food> foods = new ArrayList<>();
    private ImageView imgRestaurant;
    private TextView restaurantName;
    RecyclerView recyclerView;
    private RestaurantFoodAdapter adapter;
    private Button addToCartButton;
    private ArrayList<Food> addedFoods = new ArrayList<>();
    private FloatingActionButton cartPicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Food food1 = new Food("1", "Lasagna", "With butter lettuce, tomato and sauce bechamel", "15","https://www.modernhoney.com/wp-content/uploads/2019/08/Classic-Lasagna-14-scaled.jpg");
        Food food2 = new Food("2", "Tandoori Chicken", "Amazing Indian dish with tenderloin chicken off the sizzles 🔥", "19","https://i.ytimg.com/vi/BKxGodX9NGg/maxresdefault.jpg");
        Food food3 = new Food("3", "Chilaquiles", "Chilaquiles with cheese and sauce. A delicious mexican dish 🇲🇽", "14","https://i2.wp.com/chilipeppermadness.com/wp-content/uploads/2020/11/Chilaquales-Recipe-Chilaquiles-Rojos-1.jpg");
        Food food4 = new Food("4", "Chicken Caesar Salad", "One can never go wrong with a chicken caesar salad. Healthy option with greens and proteins!", "21","https://images.themodernproper.com/billowy-turkey/production/posts/2019/Easy-italian-salad-recipe-10.jpg?w=1200&h=1200&q=82&fm=jpg&fit=crop&fp-x=0.5&fp-y=0.5&dm=1614096227&s=c0f63a30cef3334d97f9ecad14be51da");
        Food food5 = new Food("5", "Amazing Lasagna",  "With butter lettuce, tomato and sauce bechamel", "13", "https://thestayathomechef.com/wp-content/uploads/2017/08/Most-Amazing-Lasagna-2-e1574792735811.jpg");
        foods.add(food1);
        foods.add(food2);
        foods.add(food3);
        foods.add(food4);
        foods.add(food5);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        Restaurant restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
        imgRestaurant = (ImageView) findViewById(R.id.img_restaurant);
        restaurantName = (TextView) findViewById(R.id.restaurant_name);
        addToCartButton = (Button) findViewById(R.id.add_to_cart_button);
        cartPicBtn = (FloatingActionButton) findViewById(R.id.btnCart);
        ElegantNumberButton numberButton = (ElegantNumberButton) findViewById(R.id.number_button);

        Picasso.get().load(restaurant.getImageURL()).into(imgRestaurant);
        restaurantName.setText(restaurant.getRestaurantName());

        recyclerView = findViewById(R.id.recycle_foods);
        recyclerView.setHasFixedSize(true);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.move);
        recyclerView.startAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RestaurantFoodAdapter(this, foods);
        recyclerView.setAdapter(adapter);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database database = new Database(getBaseContext());
                int totalQuantity = 0;

                List<Food> foods = ((RestaurantFoodAdapter)recyclerView.getAdapter()).getFoods();
                for (Food food: foods) {
                    if (food.getQuantity() != "0") {
                        Order order = new Order(food.getFoodId(), food.getTitle(), food.getQuantity(), food.getPrice());
                        database.addToCart(order);
                        totalQuantity += Integer.valueOf(food.getQuantity());
                    }
                }
                if (totalQuantity == 0) {
                    Toast.makeText(v.getContext(), "No food is chosen", Toast.LENGTH_SHORT).show();
                }
                else {
                Toast.makeText(v.getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();}
            }
        });

        cartPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), UserPanelBottomNavigation.class);
                i.putExtra("PAGE", "shoppingCartPage");
                startActivity(i);
            }
        });


//        ItemClickListener itemClickListener = new ItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                //attributions bond to the item has been changed
//                //LinkCard currentItem = linkList.get(position);
//                Food food = foods.get(position);
////                Intent intent = new Intent(this, .class);
////                intent.putExtra("restaurant", restaurant);
////                startActivity(intent);
//            }
//        };
//        adapter.setOnItemClickListener(itemClickListener);

    }


}