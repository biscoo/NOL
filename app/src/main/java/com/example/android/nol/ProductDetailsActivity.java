package com.example.android.nol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nol.Model.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {

    private FloatingActionButton cartBtn;
    private TextView productname, productPrice,productDescription;
    private Button addToCart;
    private ImageView productImage;
    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pID");


        productImage = findViewById(R.id.product_details_image);
        productname = findViewById(R.id.product_details_name);
        productPrice = findViewById(R.id.product_details_price);
        addToCart = findViewById(R.id.profile_details_add_to_cart);
        productDescription = findViewById(R.id.product_details_description);
        cartBtn = findViewById(R.id.fab);


        getProductDetails(productID);

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this,CartActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void getProductDetails(String productID) {

        if(productID != null){
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);
                    productname.setText(products.getProductName());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).centerCrop().into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
    }
}
