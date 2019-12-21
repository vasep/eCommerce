package com.example.ecommerce.Buyers;

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
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity implements View.OnClickListener {
    EditText confirmName, confirmPNumber, confirmAddress, confirmCity;
    Button confirmBtn;
    String totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("totalPrice");
        Toast.makeText(getApplicationContext(), "Total amount: " + totalAmount + "$", Toast.LENGTH_LONG).show();

        confirmName = findViewById(R.id.enter_shipment_name);
        confirmPNumber = findViewById(R.id.enter_shipment_phone);
        confirmAddress = findViewById(R.id.enter_shipment_address);
        confirmCity = findViewById(R.id.enter_shipment_city);
        confirmBtn = findViewById(R.id.confirm_btn);

        confirmBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == confirmBtn) {
            checkForEmptyForm();
        }
    }

    private void checkForEmptyForm() {
        if (TextUtils.isEmpty(confirmName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please provide your full name: ", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(confirmPNumber.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please provide the phone number: ", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(confirmCity.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please provide the city name: ", Toast.LENGTH_LONG).show();

        } else {
            confirmOrder();
        }
    }

    private void confirmOrder() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar callFordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callFordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callFordate.getTime());
        String orderRandomkey = saveCurrentDate + saveCurrentTime;
        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
//                .child(CurrentU.currentOnlineUser.getPhone())
                //This allows the user to place multiple orders before they've been confirmed
                //exaple of user placing only one order at a time before confirmation is below
                .child(orderRandomkey);


        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderAmount", totalAmount);
        orderMap.put("userID", CurrentU.currentOnlineUser.getPhone());
        orderMap.put("name", confirmName.getText().toString());
        orderMap.put("phone", confirmPNumber.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("address", confirmAddress.getText().toString());
        orderMap.put("city", confirmCity.getText().toString());
        orderMap.put("state", "not shipped");

        ordersRef.updateChildren(orderMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Once the user confirms the product, CART (in the Database) needs to be emptied
                            Toast.makeText(getApplicationContext(), "Your order has been place successfully ", Toast.LENGTH_LONG).show();

                            FirebaseDatabase.getInstance().getReference()
                                    .child("Cart List")
                                    .child("User View")
                                    .child(CurrentU.currentOnlineUser.getPhone())
                                    .removeValue();

                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            //So the user cant go back to this activity
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
