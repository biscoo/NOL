package com.example.android.nol.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.nol.R;

public class AdminActivity extends AppCompatActivity {

    private ImageView accecories , embroidery, homeDecoration, bags;
    private Button adminLogOut , checkNewOrders, adminModify;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toast.makeText(this, "Welcome Admin", Toast.LENGTH_SHORT).show();



        accecories = findViewById(R.id.accessories);
        embroidery = findViewById(R.id.embroidery);
        homeDecoration = findViewById(R.id.home_decoration);
        bags = findViewById(R.id.bags);

        adminLogOut = findViewById(R.id.admin_log_out);
        checkNewOrders = findViewById(R.id.check_orders_btn);
        /*adminModify = findViewById(R.id.admin_modify_product);*/

        adminLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkNewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);

            }
        });

        /*adminModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        accecories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminAddProductActivity.class);
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
