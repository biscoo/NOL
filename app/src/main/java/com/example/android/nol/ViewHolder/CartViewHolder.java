package com.example.android.nol.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.nol.Interface.ItemClickListner;
import com.example.android.nol.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName, quantity, productPrice;
    public ImageView productImage;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        productName = itemView.findViewById(R.id.cart_product_name);
        quantity = itemView.findViewById(R.id.cart_product_quantity);
        productPrice = itemView.findViewById(R.id.cart_product_price);
        productImage = itemView.findViewById(R.id.cart_product_image);

    }

    @Override
    public void onClick(View v) {

        itemClickListner.onClick(v,getAdapterPosition(),false);

    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
