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

import com.example.ecommerce.CurrentUser.CurrentU;
import com.example.ecommerce.Model.SellersModel;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerLoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button sellerLoginBtn;
    EditText name, password;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        sellerLoginBtn = findViewById(R.id.seller_login_btn);
        name = findViewById(R.id.seller_login_email);
        password = findViewById(R.id.seller_login_password);

        loadingBar = new ProgressDialog(this);

        sellerLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == sellerLoginBtn) {
            loginSeller();
        }
    }

    private void loginSeller() {
        String getPhoneNum = name.getText().toString();
        String getPassword = password.getText().toString();

        if (TextUtils.isEmpty(String.valueOf(getPhoneNum))) {
            Toast.makeText(getApplicationContext(), "Please write your name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(String.valueOf(getPassword))) {
            Toast.makeText(getApplicationContext(), "Please write your name...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(getPhoneNum, getPassword);
        }
    }

    private void allowAccessToAccount(final String getPhoneNum, final String getPassword) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference().child("Sellers").child(getPhoneNum);

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    SellersModel sellersModel = dataSnapshot.getValue(SellersModel.class);

                    if (dataSnapshot.child("phone").getValue().equals(getPhoneNum) && dataSnapshot.child("password").getValue().equals(getPassword)) {
                        String sid = dataSnapshot.child("sid").getValue().toString();
                        sellersModel.setId(sid);
//                        startActivity(new Intent(getApplicationContext(), SellersHo
//                        meActivity.class).putExtra("sid", sid).putExtra("phone",getPhoneNum));
                        CurrentU.currentOnlinerSeller = sellersModel;
                        loadingBar.dismiss();
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), SellersHomeActivity.class));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Password or username was incorrect", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
