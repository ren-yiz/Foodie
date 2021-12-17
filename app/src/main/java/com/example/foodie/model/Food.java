package com.example.foodie.model;

public class Food {
    private String foodId;
    private String title;
    private String description;
    private String price;
    private String image;
    private String quantity;

    public Food(String foodId, String title, String description, String price, String image) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.image = image;
        this.quantity = "0";
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getQuantity() {
        return quantity;
    }

    public String getFoodId() {
        return foodId;
    }
    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

}
