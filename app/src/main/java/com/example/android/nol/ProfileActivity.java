package com.example.android.nol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView userName;
    private EditText name, lastName, phone, address;
    private Button updateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.profile_user_name);
        name = findViewById(R.id.profile_name);
        lastName = findViewById(R.id.profile_last_name);
        phone = findViewById(R.id.profile_phone);
        address = findViewById(R.id.profile_address);
        updateProfile = findViewById(R.id.profile_update_btn);

        userDispalyInfo(profileImage,name,lastName,phone,address);


    }

    private void userDispalyInfo(CircleImageView profileImage, EditText name, EditText lastName, EditText phone, EditText address) {

        DatabaseReference userInfoRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Users Info");
    }
}
