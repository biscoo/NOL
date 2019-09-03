package com.example.android.nol.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.nol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity {

    private EditText fullName, address, phoneNumber, city, zipCode;
    private String userID;

    private String total = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();

        total = getIntent().getStringExtra("Total Price");

        fullName = findViewById(R.id.confirm_name);
        address = findViewById(R.id.confirm_address);
        phoneNumber = findViewById(R.id.confirm_phone);
        city = findViewById(R.id.confirm_city);
        zipCode = findViewById(R.id.confirm_zip_code);
        Button confirmBtn = findViewById(R.id.confirm_btn);




        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check();

            }
        });
    }

    private void check() {
        if (fullName.getText().length() == 0) {
            fullName.setError("Field cannot be left blank!");
            if (address.getText().length() == 0) {
                address.setError("Field cannot be left blank!");
                if (phoneNumber.getText().length() == 0) {
                    phoneNumber.setError("Field cannot be left blank!");
                    if (city.getText().length() == 0) {
                        city.setError("Field cannot be left blank!");
                        if (zipCode.getText().length() == 0) {
                            zipCode.setError("Field cannot be left blank!");

                        }

                    }

                }

            }
        }else {
            confirmOrder();
        }
    }

    private void confirmOrder() {
        final String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, YYYY");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(userID);

        final HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("client name",fullName.getText().toString());
        ordersMap.put("address",address.getText().toString());
        ordersMap.put("phone number",phoneNumber.getText().toString());
        ordersMap.put("city",city.getText().toString());
        ordersMap.put("zip code",zipCode.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("total amount",total);
        ordersMap.put("state","not shipped");
        ordersMap.put("pid",userID);

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View").child(userID)
                            .child("Products")
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ConfirmOrderActivity.this, "Congratulation! Your Order has been placed successfully. ", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmOrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }

            }
        });


        


    }

}
