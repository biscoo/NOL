package com.example.android.nol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {

    private Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        editProfile = findViewById(R.id.profile_edit_btn);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfileActivity = new Intent(ProfileActivity.this,EditProfileActivity.class);
                startActivity(editProfileActivity);
            }
        });
    }
}
