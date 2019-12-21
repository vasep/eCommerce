package com.example.ecommerce.Buyers;

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

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button registerButton;
    EditText inputName, inputPhoneNumber, inputPassword;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = findViewById(R.id.register_btn);
        inputName = findViewById(R.id.registern_name_input);
        inputPhoneNumber = findViewById(R.id.register_phone_number);
        inputPassword = findViewById(R.id.registern_password_input);

        registerButton.setOnClickListener(this);
        loadingBar = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        if (v == registerButton) {
            createAnAccount();
        }
    }

    private void createAnAccount() {
        String getName = inputName.getText().toString();
        String getPhoneNum = inputPhoneNumber.getText().toString();
        String getPasswprd = inputPassword.getText().toString();

        if (TextUtils.isEmpty(getName)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(String.valueOf(getPhoneNum))) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(getPasswprd)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validatePhoneNumber(getName, getPhoneNum, getPasswprd);
        }
    }

    private void validatePhoneNumber(final String getName, final String getPhoneNum, final String getPasswprd) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((!dataSnapshot.child("Users").child(getPhoneNum).exists())) {

                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone", getPhoneNum);
                    userDataMap.put("password", getPasswprd);
                    userDataMap.put("name", getName);

                    rootRef.child("Users").child(getPhoneNum).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Congratulations your account has been created", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

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
