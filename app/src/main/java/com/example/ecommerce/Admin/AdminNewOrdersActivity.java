package com.example.ecommerce.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Model.Orders;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {
    RecyclerView ordersList;
    DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Orders> options =
                new FirebaseRecyclerOptions.Builder<Orders>()
                        .setQuery(ordersRef, Orders.class)
                        .build();

        FirebaseRecyclerAdapter<Orders, AminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<Orders, AminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AminOrdersViewHolder holder, final int i, @NonNull final Orders orders) {
                        holder.userName.setText("Name: " + orders.getName());
                        holder.userPhone.setText("Phone: " + orders.getPhone());
                        holder.userAddress.setText("Shipped to address: " + orders.getAddress() + "," + orders.getCity());
                        holder.totalPrice.setText("Total Price: " + orders.getOrderAmount());
                        holder.orderDate.setText("Ordered at: " + orders.getDate());

                        holder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), AdminUserProductsActivity.class);
                                intent.putExtra("name", orders.getUserID());
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]{
                                        "Yes",
                                        "No"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("have you shipped this order products?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == 0) {
                                            String orderID = getRef(i).getKey();
                                            removeOrder(orderID);
                                        } else {
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.orders_layout, parent, false);
                        return new AminOrdersViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeOrder(String userID) {
        ordersRef.child(userID).removeValue();
    }

    public static class AminOrdersViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userPhone, userAddress, totalPrice, orderDate;
        Button showOrdersBtn;

        public AminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.order_user_name);
            userPhone = itemView.findViewById(R.id.order_phone_number);
            userAddress = itemView.findViewById(R.id.order_address_city);
            totalPrice = itemView.findViewById(R.id.order_total_price);
            orderDate = itemView.findViewById(R.id.order_date_time);
            showOrdersBtn = itemView.findViewById(R.id.show_all_products);

        }
    }
}
