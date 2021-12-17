package com.example.foodie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.foodie.userPanel.FoodiesFragment;
import com.example.foodie.userPanel.HomeFragment;
import com.example.foodie.userPanel.OrdersFragment;
import com.example.foodie.userPanel.ProfileFragment;
import com.example.foodie.userPanel.ShoppingCartFragment;
import com.google.android.gms.location.places.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class UserPanelBottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        String name = getIntent().getStringExtra("PAGE");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (name!= null) {
            if (name.equalsIgnoreCase("homepage")) {
                loadfragment(new HomeFragment());
            } else if (name.equalsIgnoreCase("shoppingCartPage")) {
                loadfragment(new ShoppingCartFragment());
            } else if (name.equalsIgnoreCase("ordersPage")) {
                loadfragment(new OrdersFragment());
            } else if (name.equalsIgnoreCase("foodiesPage")) {
                loadfragment(new FoodiesFragment());
            } else if (name.equalsIgnoreCase("profilePage")) {
                loadfragment(new ProfileFragment());
            } else {
                loadfragment(new HomeFragment());
            }
        }
        else {
            loadfragment(new HomeFragment());
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.home:
                fragment=new HomeFragment();
                break;
            case R.id.shoppingCart:
                fragment=new ShoppingCartFragment();
                break;
            case R.id.orders:
                fragment=new OrdersFragment();
                break;
            case R.id.foodies:
                fragment=new FoodiesFragment();
                break;
            case R.id.userProfile:
                fragment=new ProfileFragment();
                break;
        }
        return loadfragment(fragment);
    }

    private boolean loadfragment(Fragment fragment) {

        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();
            return true;
        }
        return false;
    }
}
