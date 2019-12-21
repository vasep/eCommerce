package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductDescription, txtProductPrice, txtProductState;
    public ImageView productImageView;
    ItemClickListener listener;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductState = itemView.findViewById(R.id.seller_product_seller_state);
        productImageView = itemView.findViewById(R.id.seller_product_image);
        txtProductName = itemView.findViewById(R.id.seller_product_name);
        txtProductDescription = itemView.findViewById(R.id.seller_product_description);
        txtProductPrice = itemView.findViewById(R.id.seller_product_price);
    }

    @Override
    public void onClick(View view) {
        listener.OnClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

}
