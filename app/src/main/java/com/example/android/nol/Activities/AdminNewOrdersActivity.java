package com.example.android.nol.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.nol.Model.AdminOrders;
import com.example.android.nol.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private DatabaseReference ordersRef;
    private RecyclerView ordersList;
    private DatabaseReference adminViewRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        adminViewRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");

        ordersList = findViewById(R.id.admin_orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, int i, @NonNull final AdminOrders adminOrders) {
                            adminOrdersViewHolder.clientName.setText("Name: "+adminOrders.getClientName());
                            adminOrdersViewHolder.clientPhone.setText("Phone No.: "+adminOrders.getPhoneNumber());
                            adminOrdersViewHolder.clientAddress.setText("Address: "+adminOrders.getAddress()+", "+adminOrders.getCity()+", "+adminOrders.getZipCode());
                            adminOrdersViewHolder.clientTotal.setText("Total: "+adminOrders.getTotalAmount()+"LE");
                            adminOrdersViewHolder.orderDateTime.setText("Date: "+adminOrders.getDate()+", "+adminOrders.getTime());
                            adminOrdersViewHolder.productDetails.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(AdminNewOrdersActivity.this,AdminOrderDetalisActivity.class);
                                    intent.putExtra("pid",adminOrders.getPid());
                                    startActivity(intent);
                                }
                            });

                            adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CharSequence adminOptions[] = { "yes", "No"};
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                    builder.setTitle("Order Shipped?");

                                    builder.setItems(adminOptions, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(which == 0){
                                                removeOrder(adminOrders.getPid());


                                            }
                                            if(which ==1){
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
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView clientName, clientAddress, clientPhone, clientTotal, orderDateTime;
        public Button productDetails;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            clientName = itemView.findViewById(R.id.order_client_name);
            clientAddress = itemView.findViewById(R.id.order_client_address);
            clientPhone = itemView.findViewById(R.id.order_client_number);
            clientTotal = itemView.findViewById(R.id.order_total);
            orderDateTime = itemView.findViewById(R.id.order_date);
            productDetails = itemView.findViewById(R.id.admin_product_details_btn);



        }
    }

    private void removeOrder(String userID){

        ordersRef.child(userID).removeValue();
        adminViewRef.child(userID).removeValue();
    }
}
