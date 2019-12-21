package com.example.ecommerce.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView maintenanceProductImage;
    Button applyChangesBtn, removeProduct;
    EditText changePriceEdt, changeNameEdt, changeDescriptionEdt;
    String productID = "";
    DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        applyChangesBtn = findViewById(R.id.apply_changes_maintain_btn);
        removeProduct = findViewById(R.id.delete_product_btn);
        maintenanceProductImage = findViewById(R.id.product_image_maintain);
        changePriceEdt = findViewById(R.id.product_price_maintain);
        changeNameEdt = findViewById(R.id.product_name_maintain);
        changeDescriptionEdt = findViewById(R.id.product_description_maintain);

        displaySpecificProductInfo();
        removeProduct.setOnClickListener(this);
        applyChangesBtn.setOnClickListener(this);

    }

    private void displaySpecificProductInfo() {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("productName").getValue().toString();
                    String price = dataSnapshot.child("price").getValue().toString();
                    String description = dataSnapshot.child("description").getValue().toString();
                    String productImage = dataSnapshot.child("image").getValue().toString();

                    changePriceEdt.setText(price);
                    changeNameEdt.setText(name);
                    changeDescriptionEdt.setText(description);
                    Picasso.get().load(productImage).into(maintenanceProductImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == applyChangesBtn) {
            applyChanges();
        }
        if (view == removeProduct) {
            removeProductfromStore();
        }
    }

    private void removeProductfromStore() {

        CharSequence options[] = new CharSequence[]{
                "Cancel",
                "Remove"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMaintainProductsActivity.this);
        builder.setTitle("Are you sure you want to remove the product");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {

                }
                if (i == 1) {
                    productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Product was successfuly remove from the store", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), AdminProductCategoryActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Removing the product failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        builder.show();
    }

    private void applyChanges() {
        String getName = changeNameEdt.getText().toString();
        String getPrice = changePriceEdt.getText().toString();
        String getDesc = changeDescriptionEdt.getText().toString();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we're updating your account info");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (TextUtils.isEmpty(getName)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(String.valueOf(getPrice))) {
            Toast.makeText(this, "Please write your price...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(getDesc)) {
            Toast.makeText(this, "Please write your description...", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("productName", getName);
            userMap.put("price", getPrice);
            userMap.put("description", getDesc);

            productsRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Product details updated successfuly", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), AdminProductCategoryActivity.class));
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Applying changes failed", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                }
            });
        }
    }
}
