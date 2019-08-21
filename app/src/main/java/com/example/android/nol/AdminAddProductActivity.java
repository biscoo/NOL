package com.example.android.nol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import android.app.ProgressDialog;

public class AdminAddProductActivity extends AppCompatActivity {

    private String  categoryName, description, price, pName, saveCurrentDate, saveCurrentTime;
    private Button addProduct;
    private EditText productName, productDescription, productPrice;
    private ImageView productImage;
    private int RESULT_LOAD_IMAGE= 100;
    private String productRanKey; 
    private String downloadImageUrl;
    private StorageReference productImageRef;
    private DatabaseReference productRef;
    private ProgressDialog loadingBar;

    Uri newProductImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);



        categoryName = getIntent().getExtras().get("Category").toString();
        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        loadingBar = new ProgressDialog(this);


        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();

        addProduct= findViewById(R.id.add_product);
        productName= findViewById(R.id.write_product_name);
        productDescription= findViewById(R.id.write_product_description);
        productPrice= findViewById(R.id.write_product_price);
        productImage= findViewById(R.id.select_product_image);


        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });

    }

    private void validateProductData() {

        description = productDescription.getText().toString();
        price = productPrice.getText().toString();
        pName = productName.getText().toString();

        if(newProductImage == null){
            Toast.makeText(this, "Please choose product image. ", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(pName)){
            Toast.makeText(this, "Please enter a product name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(description)){
            Toast.makeText(this, "Please enter the product description", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(price)){
            Toast.makeText(this, "Please enter the product price", Toast.LENGTH_SHORT).show();
        }else {
            storeProductInfo();
        }

    }

    private void storeProductInfo() {

        loadingBar.setTitle("Adding new Product");
        loadingBar.setMessage("Please wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, YYYY");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRanKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImageRef.child(newProductImage.getLastPathSegment() + productRanKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(newProductImage);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              String message = e.toString();
                Toast.makeText(AdminAddProductActivity.this, "Error:"+message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminAddProductActivity.this, "Product Image upload Successful..", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();

                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                downloadImageUrl = task.getResult().toString();
                                Toast.makeText(AdminAddProductActivity.this, "Got the product image URL successfully ", Toast.LENGTH_SHORT).show();

                                saveProductInfoToDataBase();
                            }
                    }
                });
            }
        });

    }

    private void saveProductInfoToDataBase() {

        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pID",productRanKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("Time",saveCurrentTime);
        productMap.put("description",description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",price);
        productMap.put("name",pName);

        productRef.child(productRanKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){


                            loadingBar.dismiss();
                            Toast.makeText(AdminAddProductActivity.this, "Product added Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AdminAddProductActivity.this,AdminActivity.class);
                            startActivity(intent);
                        }
                        else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddProductActivity.this, "Error: "+message  , Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE && data!=null){
            //user successfully picked an image
            //we need to save its reference to Uri variable
            newProductImage=data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(newProductImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap yourSelectedImage = BitmapFactory.decodeFile(picturePath);
            ImageView imageView = (ImageView) findViewById(R.id.select_product_image);
            imageView.setImageBitmap(yourSelectedImage);


        }
    }
}
