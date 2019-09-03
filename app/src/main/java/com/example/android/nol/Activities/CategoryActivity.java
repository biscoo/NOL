package com.example.android.nol.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nol.Model.Products;
import com.example.android.nol.R;
import com.example.android.nol.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CategoryActivity extends AppCompatActivity {

    private TextView categoryNameTxt;
    private RecyclerView categoryList;
    private String  categoryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryName = getIntent().getExtras().get("Category").toString();
        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();

        categoryNameTxt = findViewById(R.id.category_txt);
        categoryList = findViewById(R.id.category_list);
        categoryList.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));

        categoryNameTxt.setText(categoryName);

        onStart();



    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("category").equalTo(categoryName),Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                        productViewHolder.productName.setText(products.getName());
                        productViewHolder.productPrice.setText("Price:"+products.getPrice());
                        productViewHolder.description.setText(products.getDescription());
                        Picasso.get().load(products.getImage()).into(productViewHolder.productImage);

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("pID",products.getpId());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;

                    }
                };
        categoryList.setAdapter(adapter);
        adapter.startListening();
    }
}
