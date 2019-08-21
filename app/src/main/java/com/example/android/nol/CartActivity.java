package com.example.android.nol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nol.Model.Cart;
import com.example.android.nol.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartList;
    RecyclerView.LayoutManager layoutManager;
    private TextView totalValue;
    private Button checkOut;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();



        cartList = findViewById(R.id.cart_list);
        cartList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        cartList.setLayoutManager(layoutManager);

        totalValue = findViewById(R.id.cart_total_value);
        checkOut = findViewById(R.id.checkout_btn);

    }

    @Override
    protected void onStart() {
        super.onStart();


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(userID).child("Products"),Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, final int i, @NonNull  final Cart cart) {

                cartViewHolder.quantity.setText("Quantity : "+cart.getQuantity());
                cartViewHolder.productPrice.setText(cart.getPrice()+"LE");
                cartViewHolder.productName.setText(cart.getName());


                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{"Edit","Remove"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 if( which == 1 ){
                                     cartListRef.child("User View").child(userID)
                                             .child("Products")
                                             .child(cart.getPid())
                                             .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {
                                             if(task.isSuccessful()){
                                                 Toast.makeText(CartActivity.this, "Item removed Successfully", Toast.LENGTH_SHORT).show();

                                                 Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                                                 startActivity(intent);
                                             }

                                         }
                                     });

                                 }
                                 if( which == 0){
                                     Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                     intent.putExtra("pID", cart.getPid());
                                     startActivity(intent);


                                 }
                            }
                        });
                        builder.show();

                    }
                });


            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder cartViewHolder = new CartViewHolder(view);
                return cartViewHolder;
            }
        };

        cartList.setAdapter(adapter);
        adapter.startListening();

    }
}
