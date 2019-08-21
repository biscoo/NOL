package com.example.android.nol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.android.nol.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private FloatingActionButton cartBtn;
    private TextView productName, productPrice,productDescription;
    private Button addToCart;
    private ImageView productImage;
    private String productID;
    private ElegantNumberButton quantity;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        productID = getIntent().getStringExtra("pID");
        mAuth = FirebaseAuth.getInstance();


        productImage = findViewById(R.id.product_details_image);
        productName = findViewById(R.id.product_details_name);
        productPrice = findViewById(R.id.product_details_price);
        addToCart = findViewById(R.id.profile_details_add_to_cart);
        productDescription = findViewById(R.id.product_details_description);
        quantity = findViewById(R.id.product_quantity);
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


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToProductCart();
            }
        });
    }

    private void addToProductCart() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, YYYY");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("name",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",quantity.getNumber());
        cartMap.put("discount","");


        mUser = mAuth.getCurrentUser();
        if(mUser != null) {
             userID = mUser.getUid();


        cartRef.child("User View").child(userID).child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    cartRef.child("Admin View").child(userID).child("Products").child(productID)
                            .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProductDetailsActivity.this, "Cart Updated Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

                }
            }
        });
        }else {
            Toast.makeText(this, "User not logged in yet", Toast.LENGTH_SHORT).show();
        }





    }

    private void getProductDetails(String productID) {

        if(productID != null){
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);
                    productName.setText(products.getName());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
    }
}
