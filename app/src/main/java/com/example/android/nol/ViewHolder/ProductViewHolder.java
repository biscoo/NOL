package com.example.android.nol.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.nol.Interface.ItemClickListner;
import com.example.android.nol.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView productName, description, productPrice;
    public ImageView productImage;
    //access our listner
    public ItemClickListner listener;




    public ProductViewHolder(@NonNull View itemView)
    {
        super(itemView);

        productImage = itemView.findViewById(R.id.product_image);
        productName  = itemView.findViewById(R.id.product_name);
        description  = itemView.findViewById(R.id.product_description);
        productPrice = itemView.findViewById(R.id.product_price);

    }

    public void setItemClickListner (ItemClickListner listner){
        this.listener = listner;

    }

    @Override
    public void onClick(View v) {

        listener.onClick(v, getAdapterPosition(), false);

    }
}
