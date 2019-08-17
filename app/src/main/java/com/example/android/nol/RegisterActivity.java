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
import android.widget.ProgressBar;

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

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button signUp;
    private EditText userName, userEmail  , userPassword,userRepeatPassword;
    private ProgressBar loadingBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        signUp = findViewById(R.id.reg_sign_up);
        userName =  findViewById(R.id.regName);
        userEmail =  findViewById(R.id.regEmail);
        userPassword =  findViewById(R.id.regPassword);
        userRepeatPassword =  findViewById(R.id.reg_repeat_Password);
        loadingBar = findViewById(R.id.reg_progressBar);

        loadingBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUp.setVisibility(View.INVISIBLE);
                loadingBar.setVisibility(View.VISIBLE);

                final String name = userName.getText().toString();
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String repeatPassword = userRepeatPassword.getText().toString();

                if(name.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){

                    showMessage("Please Verify all fields.");

                }
                else if(!password.equals(repeatPassword)) {
                    showMessage("The password fields not matching");


                    }
                else {
                    createAccount(name,email,password);

                }

            }

        });

    }

    private void createAccount(final String name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            showMessage("Account created Successfully");
                            Intent homeActivity = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(homeActivity);
                            loadingBar.setVisibility(View.INVISIBLE);
                            signUp.setVisibility(View.VISIBLE);
                        }
                        else{
                            showMessage("Account creation failed" + task.getException().getMessage());
                            signUp.setVisibility(View.VISIBLE);
                            loadingBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }



    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


}
