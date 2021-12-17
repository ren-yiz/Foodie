package com.example.foodie.userPanel;

import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodie.ForGotPassword;
import com.example.foodie.MainActivity;
import com.example.foodie.R;
import com.example.foodie.model.User;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;


public class ProfileFragment extends Fragment {
    private TextView FullNameTextView;
    private ImageView profilePictureImageView;
    private Button ForGotPassword;
    private Button LogOut;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    public String fullName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_user_page,null);
        getActivity().setTitle("My Profile");

        // forgot password reset
        ForGotPassword = v.findViewById(R.id.bt_ForGotPassWord);
        ForGotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(v.getContext(), ForGotPassword.class));
            }
        });

        //Log out Start here
        LogOut = v.findViewById(R.id.bt_signOut);

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(v.getContext(), "Log Out Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(v.getContext(), MainActivity.class));
            }
        });


        // display the name of User
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        FullNameTextView = v.findViewById(R.id.username);
        profilePictureImageView = v.findViewById(R.id.IV_photo);
        profilePictureImageView.setImageResource(R.drawable.profilepic);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    fullName = userProfile.FullName;

                    FullNameTextView.setText("Hi "+ fullName);
                }else{
                    Toast.makeText(v.getContext(), "Something Wrong, couldn't load the name ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(v.getContext(), "Something Wrong !!!", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

}
