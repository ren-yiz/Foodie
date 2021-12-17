package com.example.foodie.model;

import com.example.foodie.userPanel.ItemClickListener;

import java.io.Serializable;

public class Restaurant implements ItemClickListener, Serializable {
    String RestaurantId, RestaurantName, Menu, Phone, ImageURL, Address, rating, Zipcode;

    public Restaurant(){

    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getZipcode() {
        return Zipcode;
    }

    public void setZipcode(String zipcode) {
        Zipcode = zipcode;
    }

    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        RestaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return RestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }

    public String getMenu() {
        return Menu;
    }

    public void setMenu(String menu) {
        Menu = menu;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        Phone = Phone;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }


    @Override
    public void onItemClick(int position) {
        //isChecked = !isChecked;
    }
}
