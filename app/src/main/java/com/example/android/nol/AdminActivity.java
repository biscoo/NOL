package com.example.android.nol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {

    private ImageView accecories , embroidery, homeDecoration, bags;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        accecories = findViewById(R.id.accessories);
        embroidery = findViewById(R.id.embroidery);
        homeDecoration = findViewById(R.id.home_decoration);
        bags = findViewById(R.id.bags);

        accecories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","Accessories");
                startActivity(intent);
            }
        });

        bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","Bags");
                startActivity(intent);
            }
        });

        embroidery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","Embroidery");
                startActivity(intent);
            }
        });

        homeDecoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","Home Decoration");
                startActivity(intent);
            }
        });

    }








    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Welcome Admin", Toast.LENGTH_SHORT).show();
    }
}
