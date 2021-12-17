package com.example.foodie.model;

import android.net.Uri;

import java.io.Serializable;

public class User implements Serializable {

    public String FullName,Email,password;

    public User(){
    }

    public User(String fullName, String email) {
        FullName = fullName;
        Email = email;
    }

    public User(String fullName, String email, String password) {
        FullName = fullName;
        Email = email;
        this.password = password;
    }

}