package com.example.foodie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodie.common.Common;
import com.example.foodie.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;

    private EditText editTextEmail;
    private TextView editTextPassword;
    private Button LogIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register =(TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        LogIn= (Button) findViewById(R.id.bt_login);
        LogIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.et_EmailLogin);
        editTextPassword =  (TextView) findViewById(R.id.et_passwordLogin);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(this,RegisterUser.class));
                break;
            case R.id.bt_login:
                userLogin();
                break;
        }

    }


    private void userLogin() {
        String emailEnter = editTextEmail.getText().toString().trim();
        String passwordEnter =editTextPassword.getText().toString().trim();

        if(emailEnter.isEmpty()){
            editTextEmail.setError("Email missing");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailEnter).matches()){
            editTextEmail.setError("Please Enter a valid Email");
            editTextEmail.requestFocus();
            return;
        }

        if(passwordEnter.isEmpty()){
            editTextPassword.setError("Password missing");
            editTextPassword.requestFocus();
            return;
        }
        if(passwordEnter.length()< 6){
            editTextPassword.setError("Password length should be at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(emailEnter,passwordEnter).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // this will go into the new user activity
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(MainActivity.this, UserPanelBottomNavigation.class));
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Login Failed, Please double check your Email and Password", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}