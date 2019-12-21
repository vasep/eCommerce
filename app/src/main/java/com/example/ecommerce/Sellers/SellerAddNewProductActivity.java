package com.example.ecommerce.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Admin.AdminNewOrdersActivity;
import com.example.ecommerce.Buyers.MainActivity;
import com.example.ecommerce.CurrentUser.CurrentU;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductActivity extends AppCompatActivity implements View.OnClickListener {

    private String catergoryName, descriptionProduct, priceProduct, productName, saveCurrentDate, saveCurrentTime;
    Button completeAddProduct, logoutBtn, checkOrdersBtn;
    EditText editProductname, editProductDescription, editProductPrice;
    ImageView imputProduct;
    ProgressDialog loadingBar;
    private static int galleryPick = 1;
    private Uri imageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef, sellersRef;

    private String sName, sAddress, sPhone, sEmail, sID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        sellersRef = FirebaseDatabase.getInstance().getReference().child("Sellers");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        sID = getIntent().getStringExtra("sid");
        sPhone = getIntent().getStringExtra("phone");

        catergoryName = getIntent().getExtras().get("category").toString();
        loadingBar = new ProgressDialog(this);
//        checkOrdersBtn=findViewById(R.id.check_orders_btn);
//        logoutBtn=findViewById(R.id.androind_logut_btn);
        Toast.makeText(getApplicationContext(), catergoryName, Toast.LENGTH_SHORT).show();
        completeAddProduct = findViewById(R.id.complete_btn);
        editProductDescription = findViewById(R.id.product_description);
        editProductname = findViewById(R.id.product_name);
        editProductPrice = findViewById(R.id.product_price);
        imputProduct = findViewById(R.id.select_product_image);

        imputProduct.setOnClickListener(this);
        completeAddProduct.setOnClickListener(this);
//        checkOrdersBtn.setOnClickListener(this);
//        logoutBtn.setOnClickListener(this);

        sellersRef.child(CurrentU.currentOnlinerSeller.getPhone())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            sName = dataSnapshot.child("name").getValue().toString();
                            sAddress = dataSnapshot.child("address").getValue().toString();
                            sEmail = dataSnapshot.child("email").getValue().toString();
                            sPhone = dataSnapshot.child("phone").getValue().toString();
                            sID = dataSnapshot.child("sid").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == completeAddProduct) {
            validateProductData();
        }
        if (view == imputProduct) {
            openGallery();
        }
        if (view == checkOrdersBtn) {
            Intent intent = new Intent(getApplicationContext(), AdminNewOrdersActivity.class);
            startActivity(intent);
        }
        if (view == logoutBtn) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
            //set the imageUri by data on activity result
            imageUri = data.getData();
            imputProduct.setImageURI(imageUri);
        }
    }

    private void validateProductData() {
        //Getting Imageinfo from UI
        descriptionProduct = editProductDescription.getText().toString();
        priceProduct = editProductPrice.getText().toString();
        productName = editProductname.getText().toString();

        if (imageUri == null) {
            Toast.makeText(getApplicationContext(), "Product Image is missing ", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(productName)) {
            Toast.makeText(getApplicationContext(), "Product Name is missing ", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(descriptionProduct)) {
            Toast.makeText(getApplicationContext(), "Product Price is missing ", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(priceProduct)) {
            Toast.makeText(getApplicationContext(), "Product Description is missing ", Toast.LENGTH_LONG).show();

        } else {
            storeImageInfo();
        }

    }

    private void storeImageInfo() {
        //Storing Image Info to FirebaseSotrage
        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Please wait while we're adding a new product...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, YYYY");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;
        //Creating a file path for child element random key + imageUri, which will be saved under Storage reference key "Product Image"
        final StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        //Uploading the images
        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_LONG).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            //TODO see what this returns
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(getApplicationContext(), "getting Product Image URL Successfully", Toast.LENGTH_LONG).show();
                            saveProductInfoTodatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoTodatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", descriptionProduct);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", catergoryName);
        productMap.put("price", priceProduct);
        productMap.put("productName", productName);

        productMap.put("sellerName", sName);
        productMap.put("sellerAddress", sAddress);
        productMap.put("sellerEmail", sEmail);
        productMap.put("sellerPhone", sPhone);
        productMap.put("sid", sID);
        productMap.put("productState", "Not Approved");


        productsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    startActivity(new Intent(getApplicationContext(), SellersHomeActivity.class));
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Product is added successfully", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(getApplicationContext(), "Error" + message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sellersInformation() {
    }
}
