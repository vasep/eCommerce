package com.example.ecommerce.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Buyers.HomeActivity;
import com.example.ecommerce.Buyers.LoginActivity;
import com.example.ecommerce.R;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button checkOrders, maintenanceBtn, logoutBtn, checkApprovedProductsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        checkApprovedProductsBtn = findViewById(R.id.checked_approved_product_btn);
        checkOrders = findViewById(R.id.check_orders_btn);
        maintenanceBtn = findViewById(R.id.maintenance_admin_button);
        logoutBtn = findViewById(R.id.androind_logut_btn);

        logoutBtn.setOnClickListener(this);
        maintenanceBtn.setOnClickListener(this);
        checkOrders.setOnClickListener(this);
        checkApprovedProductsBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == logoutBtn) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
        if (view == maintenanceBtn) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("Admin", "Admin");
            startActivity(intent);
        }
        if (view == checkOrders) {
            startActivity(new Intent(getApplicationContext(), AdminNewOrdersActivity.class));
        }
        if (view == checkApprovedProductsBtn){
            startActivity(new Intent(getApplicationContext(), AdminCheckNewProductActivity.class));
        }
    }
}
