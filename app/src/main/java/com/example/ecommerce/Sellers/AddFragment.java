package com.example.ecommerce.Sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerce.R;

public class AddFragment extends Fragment implements View.OnClickListener {
    private ImageView sweaters, female_dresses, sport_t_shirts, t_shirts, glasses, purses, hats, shoes, head_phones, laptops, watches, mobile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_add_category, container, false);

        sweaters = view.findViewById(R.id.sweaters);
        female_dresses = view.findViewById(R.id.female_dresses);
        sport_t_shirts = view.findViewById(R.id.sport_t_shirts);
        t_shirts = view.findViewById(R.id.t_shirts);

        glasses = view.findViewById(R.id.glasses);
        purses = view.findViewById(R.id.purses);
        hats = view.findViewById(R.id.hats);
        shoes = view.findViewById(R.id.shoes);

        head_phones = view.findViewById(R.id.head_phones);
        laptops = view.findViewById(R.id.laptops);
        watches = view.findViewById(R.id.watches);
        mobile = view.findViewById(R.id.mobile);

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

        return view;
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
        Intent intent = new Intent(getContext(), SellerAddNewProductActivity.class);
        intent.putExtra("category", categoryName);
        startActivity(intent);
    }
}
