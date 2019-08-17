package com.example.android.nol;


import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
    private EditText userEmail, userPassword;
    private Button loginButton;
    private ProgressBar loadingBar;

    private FirebaseAuth mAuth;
    private Intent HomeActivity;

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
                else{
                    loginUser(email,password);
                }
            }
        });


    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
             if(task.isSuccessful()){

                 loadingBar.setVisibility(View.INVISIBLE);
                 loginButton.setVisibility(View.VISIBLE);
                 updateUI();
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
}

