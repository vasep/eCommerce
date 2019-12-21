package com.example.ecommerce.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Buyers.MainActivity;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    Button alreadyHaveAnAcc, registerBtn;
    EditText sellerName, sellerPhone, sellerEmail, sellerAddress, sellerPasoword;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);
        loadingBar = new ProgressDialog(this);

        alreadyHaveAnAcc = findViewById(R.id.seller_already_have_acc_btn);
        registerBtn = findViewById(R.id.seller_register_btn);
        sellerName = findViewById(R.id.seller_name);
        sellerPhone = findViewById(R.id.seller_phone);
        sellerEmail = findViewById(R.id.seller_email);
        sellerAddress = findViewById(R.id.seller_address);
        sellerPasoword = findViewById(R.id.seller_passowrd);

        alreadyHaveAnAcc.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == alreadyHaveAnAcc) {
            startActivity(new Intent(getApplicationContext(), SellerLoginActivity.class));
        }
        if (view == registerBtn) {
            registerSeller();

        }
    }

    private void registerSeller() {
        String getName = sellerName.getText().toString();
        String getPhoneNum = sellerPhone.getText().toString();
        String getPasswprd = sellerPasoword.getText().toString();
        String getAddress = sellerAddress.getText().toString();
        String getEmail = sellerEmail.getText().toString();
        String generatedID = generatedSellerID();


        if (TextUtils.isEmpty(getName)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(String.valueOf(getPhoneNum))) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(getPasswprd)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(getAddress)) {
            Toast.makeText(this, "Please write your address...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(getEmail)) {
            Toast.makeText(this, "Please write your email...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validateUserRegistration(getName, getPhoneNum, getPasswprd, getAddress, getEmail, generatedID);
        }
    }

    private String generatedSellerID() {
        String saveCurrentTime, saveCurrentDate, sid;

        Calendar callFordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callFordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callFordate.getTime());

        return sid = saveCurrentTime + saveCurrentDate;
    }

    private void validateUserRegistration(final String getName, final String getPhoneNum, final String getPasswprd, final String getAddress, final String getEmail, final String getID) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((!dataSnapshot.child("Sellers").child(getPhoneNum).exists())) {

                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone", getPhoneNum);
                    userDataMap.put("password", getPasswprd);
                    userDataMap.put("name", getName);
                    userDataMap.put("email", getEmail);
                    userDataMap.put("address", getAddress);
                    userDataMap.put("sid", getID);

                    rootRef.child("Sellers").child(getPhoneNum).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Congratulations your account has been created", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                startActivity(new Intent(getApplicationContext(), SellerLoginActivity.class));

                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(getApplicationContext(), "Network Error: Please try again..", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "This " + getPhoneNum + "already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Please try again using another number", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
