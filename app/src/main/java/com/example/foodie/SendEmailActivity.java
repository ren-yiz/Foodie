package com.example.foodie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.foodie.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendEmailActivity extends AppCompatActivity {

    DatabaseReference reference;
    TextView text;
    Button sendEmail;
    EditText emailMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        User user = (User) getIntent().getSerializableExtra("to");
        text = findViewById(R.id.text_view);
        sendEmail = findViewById(R.id.send);
//        emailMessage = findViewById(R.id.message);

        text.setText("Cilck send button to send an email to " + user.Email);

        final String[] senderName = {""};
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        senderName[0] = snapshot.getValue(User.class).FullName;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        String title = "You got a greeting from " + senderName[0] + " on Foodie app";
//        String message = emailMessage.getText().toString();

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("mailto:" + user.Email);
                String[] email = {user.Email};
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_CC, email);
                intent.putExtra(Intent.EXTRA_SUBJECT, title);
                intent.putExtra(Intent.EXTRA_TEXT, "Hi!");
                startActivity(Intent.createChooser(intent, "Please choose mail application"));
            }
        });

    }
}