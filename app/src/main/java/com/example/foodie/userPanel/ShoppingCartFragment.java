package com.example.foodie.userPanel;

import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodie.R;
import com.example.foodie.common.Common;
import com.example.foodie.database.Database;
import com.example.foodie.model.Order;
import com.example.foodie.model.Request;
import com.example.foodie.userPanel.adapters.ShoppingCartAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartFragment extends Fragment {
    RecyclerView recyclerView;
    public static TextView textViewPrice;
    Button buttonOrder;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference request;
    ShoppingCartAdapter shoppingCartAdapter;
    List<Order> orders;
    TextView emptyMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_shopping_cart,null);
        getActivity().setTitle("Cart");

        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");

        orders = new ArrayList<>();
        textViewPrice = v.findViewById(R.id.order_price);
        buttonOrder = v.findViewById(R.id.btnPlaceOrder);
        emptyMessage = v.findViewById(R.id.empty_message);


        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textViewPrice.getText().toString().equals(" $0.00")) {
                    Toast.makeText(v.getContext(), "Cart is empty", Toast.LENGTH_SHORT).show(); }
                else
                    showDialog();
            }
        });

        recyclerView = v.findViewById(R.id.RecycleListOfCart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadCart();

        if(textViewPrice.getText().toString().equals(" $0.00")) {
            emptyMessage.setText("Add food to your cart!");
        }

        return v;
    }


    //Helper Methods
    private void loadCart() {
//        new Database(getContext()).cleanCart();
        orders = new Database(getContext()).getCarts();
//        cartAdapter = new CartAdapter(this, orders, new ItemClickListener() {
//            @Override
//            public void onclick(View view, int position, boolean isLongClick) {
//                Toast.makeText(CartActivity.this, orders.get(position).getProductName(), Toast.LENGTH_SHORT).show();
//            }
//        });
        shoppingCartAdapter = new ShoppingCartAdapter(getContext(), orders);
        recyclerView.setAdapter(shoppingCartAdapter);

        float total = 0;
        //Calculating total price
        for(Order order: orders)
        {
            total += (Float.parseFloat(order.getPrice()) * Float.parseFloat(order.getQuantity()));
        }
        if ((int) total == 0) {
            textViewPrice.setText(String.format(" $0.00"));
        }
        else {
        textViewPrice.setText(String.format(" $%s", total));}
    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("One more step!");
        builder.setMessage("Enter your Address: ");
        final EditText editText = new EditText(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        editText.setLayoutParams(layoutParams);
        builder.setView(editText);
        builder.setIcon(R.drawable.ic_baseline_shopping_cart_24);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Request req = new Request(user.getDisplayName(),
                        user.getEmail(),
                        editText.getText().toString(),
                        textViewPrice.getText().toString(),
                        orders);

                //sending to firebase
                request.child(String.valueOf(System.currentTimeMillis())).setValue(req);

                new Database(getContext()).cleanCart();
                Toast.makeText(getContext(), "Order is placed. Thank You!", Toast.LENGTH_SHORT).show();
                textViewPrice.setText(String.format(" $0.00"));
                orders = new Database(getContext()).getCarts();
                shoppingCartAdapter = new ShoppingCartAdapter(getContext(), orders);
                recyclerView.setAdapter(shoppingCartAdapter);
            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

}
