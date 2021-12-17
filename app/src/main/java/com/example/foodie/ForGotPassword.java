package com.example.foodie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForGotPassword extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetPassWordButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_got_password);

        emailEditText = (EditText) findViewById(R.id.EnterEmailForReset);
        resetPassWordButton = (Button) findViewById(R.id.bt_ResetPassWord);

        auth = FirebaseAuth.getInstance();
        resetPassWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassWord();
            }
        });

    }

    private void resetPassWord() {
        String email = emailEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            return;
        }
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForGotPassword.this, "Check your email to reset your PassWord!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForGotPassword.this,MainActivity.class));
                }else{
                    Toast.makeText(ForGotPassword.this, "Try again, Something Went Wrong Or check Email if Match login email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}