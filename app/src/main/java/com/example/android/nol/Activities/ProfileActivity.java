package com.example.android.nol.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.nol.Model.Users;
import com.example.android.nol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private Button editProfile;
    private CircleImageView profileImage;
    private TextView profileName, name, email, phone, address;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference usersRef;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        user = mUser.getUid();


        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_user_name);
        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        phone = findViewById(R.id.profile_phone);
        address = findViewById(R.id.profile_address);



        editProfile = findViewById(R.id.profile_edit_btn);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfileActivity = new Intent(ProfileActivity.this,EditProfileActivity.class);
                startActivity(editProfileActivity);
            }
        });
    }

    private void getUserDetails(String userID) {

        if(userID != null){
            usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
            usersRef.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Users users = dataSnapshot.getValue(Users.class);
                        profileName.setText(users.getName());
                        name.setText(users.getName());
                        email.setText(users.getEmail());
                        phone.setText(users.getPhone());
                        address.setText(users.getAddress());
                        Picasso.get().load(users.getImage()).into(profileImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        getUserDetails(user);

    }
}
