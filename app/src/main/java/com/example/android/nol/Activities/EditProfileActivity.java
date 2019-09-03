package com.example.android.nol.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nol.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {


    private TextView userNametxt;
    private EditText name, email, phone, address;
    private Button updateProfile;
    Uri pickedImage;

    static int PReqCode = 1;
    static int RESULT_LOAD_IMAGE = 100;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference usersRef;
    private StorageReference usersImageRef;
    private String userName, userEmail, userPhone,userAddress;
    private String user, downloadImageUri;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        user = mUser.getUid();


        usersImageRef = FirebaseStorage.getInstance().getReference().child("Users Image");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        loadingBar = new ProgressDialog(this);

        final CircleImageView profileImage;

        profileImage = findViewById(R.id.edit_profile_image);
        userNametxt = findViewById(R.id.profile_user_name);
        name = findViewById(R.id.edit_profile_name);
        email = findViewById(R.id.edit_profile_email);
        phone = findViewById(R.id.edit_profile_phone);
        address = findViewById(R.id.edit_profile_address);
        updateProfile = findViewById(R.id.edit_profile_update_btn);



        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }

            }
        });



        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userUpdateInfo();
            }
        });


    }

    private void userUpdateInfo() {

        loadingBar.setTitle("Updating Profile");
        loadingBar.setMessage("Please wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        userName = name.getText().toString();
        userEmail = email.getText().toString();
        userPhone = phone.getText().toString();
        userAddress = address.getText().toString();


        final StorageReference filePath = usersImageRef.child(pickedImage.getLastPathSegment()+ user +".jpg");
        final UploadTask uploadTask = filePath.putFile(pickedImage);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(EditProfileActivity.this, "Error1: "+message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(EditProfileActivity.this, "Profile Image Uploaded successfully.", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUri = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){

                            downloadImageUri = task.getResult().toString();

                            Toast.makeText(EditProfileActivity.this, "Profile Image uri Saved to database Successfully.", Toast.LENGTH_SHORT).show();

                            saveProfileInfoToDatabase();
                        }
                    }
                });
            }
        });


    }

    private void saveProfileInfoToDatabase(){
        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("pid",user);
        userMap.put("name",userName);
        userMap.put("phone",userPhone);
        userMap.put("address",userAddress);
        userMap.put("email",userEmail);
        userMap.put("image",downloadImageUri);

        usersRef.child(user).updateChildren(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EditProfileActivity.this, "User updates Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
                            startActivity(intent);
                            finish();

                        }else {
                            String message = task.getException().toString();
                            Toast.makeText(EditProfileActivity.this, "Error2: "+message , Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(EditProfileActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }
        else{
            openGallery();

        }

    }
    private void  openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE && data!=null){
            //user successfully picked an image
            //we need to save its reference to Uri variable
            pickedImage=data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(pickedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap yourSelectedImage = BitmapFactory.decodeFile(picturePath);
            ImageView imageView = (ImageView) findViewById(R.id.edit_profile_image);
            imageView.setImageBitmap(yourSelectedImage);


        }
    }
}
