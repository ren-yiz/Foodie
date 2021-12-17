package com.example.foodie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodie.model.Request;
import com.example.foodie.model.Restaurant;
import com.example.foodie.model.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FoodieProfileActivity extends AppCompatActivity {
    TextView foodieName;
    TextView foodieEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_profile);

        User user = (User) getIntent().getSerializableExtra("user");

        foodieName = (TextView) findViewById(R.id.foodie_email);
        foodieEmail = (TextView) findViewById(R.id.foodie_profile_name);
        View emailBar = findViewById(R.id.email_bar);

        foodieName.setText(user.FullName);
        foodieEmail.setText(user.Email);
        emailBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getApplicationContext(), SendEmailActivity.class);
                newIntent.putExtra("to", user);
                startActivity(newIntent);
            }
        });

    }
}
