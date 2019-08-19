package com.example.android.nol;


import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.nol.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText userEmail, userPassword;
    private Button loginButton;
    private ProgressBar loadingBar;
    private String adminEmail = "admin@gmail.com";
    private String adminPassword = "admin";
    private  String parentDbase = "Admins";
    private FirebaseAuth mAuth;
    private Intent HomeActivity;
    private Intent AdminActivity;
    private FirebaseDatabase mDataB;
    private DatabaseReference databaseReference;
    private String childDbase = "3280451693";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginButton = findViewById(R.id.login_btn);
        userEmail =  findViewById(R.id.login_email_input);
        userPassword = findViewById(R.id.login_password_input);
        loadingBar = findViewById(R.id.login_progressBar);
        mAuth = FirebaseAuth.getInstance();
        HomeActivity = new Intent(this,com.example.android.nol.HomeActivity.class);
        AdminActivity = new Intent(this,com.example.android.nol.AdminActivity.class);




        loginButton.setVisibility(View.VISIBLE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.INVISIBLE);

                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    showMessage("Verify all fields");
                    loginButton.setVisibility(View.VISIBLE);
                    loadingBar.setVisibility(View.INVISIBLE);
                }
                else if(email.equals(adminEmail) && password.equals(adminPassword)){
                    startAdminActivity();
                }

                else {loginUser(email,password);
                }
            }
        });


    }

    private void startAdminActivity() {
        startActivity(AdminActivity);
        finish();
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
             if(task.isSuccessful()){

                 loadingBar.setVisibility(View.INVISIBLE);
                 loginButton.setVisibility(View.VISIBLE);
                 updateUI();
                 finish();
             }
             else {
                 showMessage(task.getException().getMessage());
                 loadingBar.setVisibility(View.INVISIBLE);
                 loginButton.setVisibility(View.VISIBLE);
             }
            }
        });
    }



    private void updateUI() {
        startActivity(HomeActivity);
        finish();
    }

    private void showMessage(String s) {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            updateUI();
        }

    }

    public void basicRead(final String email, final String password) {
        // [START write_message]
        // Write a message to the database
        mDataB = FirebaseDatabase.getInstance();
        databaseReference = mDataB.getReference("Admins/3280451693/");
        // [END write_message]

        // [START read_message]
        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.child(parentDbase).child(childDbase).exists()){
                Users users = dataSnapshot.child(parentDbase).child(childDbase).getValue(Users.class);
                if(users.getEmail().equals(email) && users.getPassword().equals(password)){
                    startAdminActivity();
                }

                Log.d(TAG, "Value is: " + users);
            }}

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}

