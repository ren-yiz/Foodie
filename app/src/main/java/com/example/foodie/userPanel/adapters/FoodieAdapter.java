
package com.example.foodie.userPanel.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodie.FoodieProfileActivity;
import com.example.foodie.R;
import com.example.foodie.RegisterUser;
import com.example.foodie.model.User;
import com.example.foodie.userPanel.ItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.List;

public class FoodieAdapter extends RecyclerView.Adapter<FoodieAdapter.ViewHolder> {
    private List<User> userList;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();;
    private ItemClickListener listener;

    public FoodieAdapter(List<User> userList) {
        this.userList = userList;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public FoodieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodie_card,parent,false);
        return new FoodieAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodieAdapter.ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.profilePicture.setImageResource(R.drawable.profilepic);
        holder.fullName.setText(user.FullName);
        holder.email.setText(user.Email);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profilePicture;
        TextView fullName,email;

        public ViewHolder(@NonNull View itemView, final ItemClickListener listener) {
            super(itemView);

            profilePicture = itemView.findViewById(R.id.profile_picture);
            fullName = itemView.findViewById(R.id.full_name);
            email = itemView.findViewById(R.id.email);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}