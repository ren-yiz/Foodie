package com.example.foodie.userPanel;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodie.R;
import com.example.foodie.common.Common;
import com.example.foodie.model.Request;
import com.example.foodie.userPanel.adapters.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrdersFragment extends Fragment {
    RecyclerView recyclerView;
    TextView textViewId;
    TextView textViewStatus;
    TextView textViewTotal;
    TextView textViewAddress;
    List<Request> ordersRequested;
    FirebaseRecyclerAdapter <Request, OrderViewHolder> adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference requests;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user_orders,null);
        getActivity().setTitle("Orders");

        firebaseDatabase = FirebaseDatabase.getInstance();
        requests = firebaseDatabase.getReference("Request");

        textViewId = v.findViewById(R.id.order_id);
        textViewStatus = v.findViewById(R.id.order_status);
        textViewTotal = v.findViewById(R.id.order_total);
        textViewAddress = v.findViewById(R.id.order_address);

        recyclerView = v.findViewById(R.id.recycler_order_status);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        loadOrdersRequested(user.getEmail());
        adapter.startListening();
        return v;
    }

    //Helper Method
    private void loadOrdersRequested(String email)
    {
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(
                requests.orderByChild("email").equalTo(email), Request.class).build();
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Request model) {
                TextView textViewId = holder.itemView.findViewById(R.id.order_id);
                textViewId.setText(adapter.getRef(position).getKey());

                TextView textViewTotal = holder.itemView.findViewById(R.id.order_total);
                textViewTotal.setText(model.getTotal());

                TextView textViewAddress = holder.itemView.findViewById(R.id.order_address);
                textViewAddress.setText(model.getAddress());

                TextView textViewStatus = holder.itemView.findViewById(R.id.order_status);
                textViewStatus.setText(Common.getStatus(model.getStatus()));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}
