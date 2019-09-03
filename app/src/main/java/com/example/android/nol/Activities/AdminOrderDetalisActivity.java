package com.example.android.nol.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.nol.Model.Cart;
import com.example.android.nol.R;
import com.example.android.nol.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminOrderDetalisActivity extends AppCompatActivity {

    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference productsRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userId = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_detalis);



        userId = getIntent().getStringExtra("pid");

        productsList = findViewById(R.id.admin_order_details_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);
        productsRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("Admin View").child(userId).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(productsRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {

                cartViewHolder.quantity.setText("Quantity : "+cart.getQuantity());
                cartViewHolder.productPrice.setText(cart.getPrice()+"LE");
                cartViewHolder.productName.setText(cart.getName());
                Picasso.get().load(cart.getImage()).into(cartViewHolder.productImage);

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder cartViewHolder = new CartViewHolder(view);
                return cartViewHolder;
            }
        };

        productsList.setAdapter(adapter);
        adapter.startListening();

    }
}
