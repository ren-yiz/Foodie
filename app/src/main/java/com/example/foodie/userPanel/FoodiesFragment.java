
package com.example.foodie.userPanel;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodie.FoodieProfileActivity;
import com.example.foodie.R;
import com.example.foodie.model.User;
import com.example.foodie.userPanel.adapters.FoodieAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FoodiesFragment extends Fragment  {

    RecyclerView recyclerView;
    private List<User> userList = new ArrayList<>();
    private FoodieAdapter adapter;
    private RecyclerView.LayoutManager rLayoutManger;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_foodies,null);
        getActivity().setTitle("Foodies");

        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //attributions bond to the item has been changed
                //LinkCard currentItem = linkList.get(position);
                User user = userList.get(position);
                Intent newIntent = new Intent(getContext(), FoodieProfileActivity.class);
                newIntent.putExtra("user", user);
                startActivity(newIntent);
            }
        };

        rLayoutManger = new LinearLayoutManager(getActivity());
        recyclerView = v.findViewById(R.id.recycle_foodies);
        recyclerView.setHasFixedSize(true);
        adapter = new FoodieAdapter(userList);
        adapter.setOnItemClickListener(itemClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(rLayoutManger);

        getAllUsers();

        return v;
    }

    public void getAllUsers() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String email = (String) snapshot.child("Email").getValue();
                String fullName = (String) snapshot.child("FullName").getValue();
                userList.add(new User(email, fullName));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }
}


