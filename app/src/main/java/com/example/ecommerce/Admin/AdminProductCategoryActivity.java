package com.example.ecommerce.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.example.ecommerce.Sellers.SellerAddNewProductActivity;

public class AdminProductCategoryActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView sweaters, female_dresses, sport_t_shirts, t_shirts, glasses, purses, hats, shoes, head_phones, laptops, watches, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_add_category);
        sweaters = findViewById(R.id.sweaters);
        female_dresses = findViewById(R.id.female_dresses);
        sport_t_shirts = findViewById(R.id.sport_t_shirts);
        t_shirts = findViewById(R.id.t_shirts);

        glasses = findViewById(R.id.glasses);
        purses = findViewById(R.id.purses);
        hats = findViewById(R.id.hats);
        shoes = findViewById(R.id.shoes);

        head_phones = findViewById(R.id.head_phones);
        laptops = findViewById(R.id.laptops);
        watches = findViewById(R.id.watches);
        mobile = findViewById(R.id.mobile);

        sweaters.setOnClickListener(this);
        female_dresses.setOnClickListener(this);
        sport_t_shirts.setOnClickListener(this);
        t_shirts.setOnClickListener(this);
        purses.setOnClickListener(this);
        hats.setOnClickListener(this);
        shoes.setOnClickListener(this);
        head_phones.setOnClickListener(this);
        laptops.setOnClickListener(this);
        watches.setOnClickListener(this);
        mobile.setOnClickListener(this);
        glasses.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == sweaters) {
            goToActivityCategory("sweaters");
        }
        if (view == female_dresses) {
            goToActivityCategory("female_dresses");
        }
        if (view == sport_t_shirts) {
            goToActivityCategory("sport_t_shirts");
        }
        if (view == t_shirts) {
            goToActivityCategory("t_shirts");
        }
        if (view == purses) {
            goToActivityCategory("purses");
        }
        if (view == hats) {
            goToActivityCategory("hats");
        }
        if (view == shoes) {
            goToActivityCategory("shoes");
        }
        if (view == head_phones) {
            goToActivityCategory("head_phones");
        }
        if (view == laptops) {
            goToActivityCategory("laptops");
        }
        if (view == watches) {
            goToActivityCategory("watches");
        }
        if (view == mobile) {
            goToActivityCategory("mobile");
        }
        if (view == glasses) {
            goToActivityCategory("glasses");
        }
    }

    private void goToActivityCategory(String categoryName) {
        Intent intent = new Intent(getApplicationContext(), SellerAddNewProductActivity.class);
        intent.putExtra("category", categoryName);
        startActivity(intent);
    }
}
