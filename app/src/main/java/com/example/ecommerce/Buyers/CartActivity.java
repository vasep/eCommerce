package com.example.ecommerce.Buyers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.CurrentUser.CurrentU;
import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button nextButton;
    TextView totalAmount, txtMsg1;
    int overAllTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        txtMsg1 = findViewById(R.id.msg_1);
        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextButton = findViewById(R.id.btn_next);
        totalAmount = findViewById(R.id.total_price_txt);

        nextButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check oreder state, in case you need to run orders one at a time
//        checkOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(CurrentU.currentOnlineUser.getPhone())
                                .child("Products"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder cardViewHolder, int i, @NonNull final Cart cart) {
                int sum = Integer.parseInt(cart.getPrice()) * Integer.parseInt(cart.getQuantity());
                cardViewHolder.productName.setText(cart.getPname());
                cardViewHolder.productPrice.setText("Price = " + sum + "$");
                cardViewHolder.productQuantity.setText("Quantity = " + cart.getQuantity());

                overAllTotalPrice = overAllTotalPrice + sum;
                totalAmount.setText(String.valueOf(overAllTotalPrice));

                cardViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                                    intent.putExtra("pid", cart.getPid());
                                    startActivity(intent);
                                }
                                if (i == 1) {
                                    cartListRef.child("User View")
                                            .child(CurrentU.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(cart.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    //    private void checkOrderState()
//    {
//        DatabaseReference ref;
//        ref=FirebaseDatabase.getInstance().getReference()
//                .child("Orders")
//                .child(CurrentU.currentOnlineUser.getPhone());
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists())
//                {
//                    String shippingState= dataSnapshot.child("state").getValue().toString();
//                    String userName = dataSnapshot.child("name").getValue().toString();
//
//                    if (shippingState.equals("shipped"))
//                    {
//                        totalAmount.setText("Order is shipped successfully");
//                        recyclerView.setVisibility(View.GONE);
//                        txtMsg1.setVisibility(View.VISIBLE);
//                        nextButton.setVisibility(View.GONE);
//                    }
//                    else if(shippingState.equals("not shipped"))
//                    {
//                        totalAmount.setText("Shipping State = not shipped");
//                        recyclerView.setVisibility(View.GONE);
//                        txtMsg1.setVisibility(View.VISIBLE);
//                        nextButton.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
    @Override
    public void onClick(View view) {
        if (view == nextButton) {
            Intent intent = new Intent(getApplicationContext(), ConfirmFinalOrderActivity.class);
            intent.putExtra("totalPrice", String.valueOf(overAllTotalPrice));
            startActivity(intent);
            finish();
        }
    }
}
