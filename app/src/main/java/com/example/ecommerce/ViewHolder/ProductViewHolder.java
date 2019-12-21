package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView productImageView;
    ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productImageView = itemView.findViewById(R.id.product_image);
        txtProductName = itemView.findViewById(R.id.product_name);
        txtProductDescription = itemView.findViewById(R.id.product_description);
        txtProductPrice = itemView.findViewById(R.id.product_price);
    }

    @Override
    public void onClick(View view) {
        listener.OnClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

}
