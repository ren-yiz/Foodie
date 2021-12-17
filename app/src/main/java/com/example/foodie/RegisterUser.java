package com.example.foodie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodie.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private EditText editTextFullName,editTextEmail,editTextPassword;
    private TextView registerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.bt_Register);
        registerUser.setOnClickListener(this);

        editTextFullName =(EditText) findViewById(R.id.et_FullName);
        editTextEmail =(EditText) findViewById(R.id.et_Email);
        editTextPassword =(EditText) findViewById(R.id.et_Password);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bt_Register:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String fullname = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(fullname.isEmpty()){
            editTextFullName.setError("Please Enter full name");
            editTextFullName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail.setError("Please Enter Email");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("please Enter valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Please Enter Password ");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Password length should be at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(fullname, email,password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "User Successful Registered Now you can login ", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterUser.this,MainActivity.class));

                                    } else {
                                        Toast.makeText(RegisterUser.this, "Fail Register!! try again", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        } else {
                            Toast.makeText(RegisterUser.this, "Fail Register!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
}