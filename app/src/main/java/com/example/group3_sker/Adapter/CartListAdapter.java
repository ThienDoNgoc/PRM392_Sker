package com.example.group3_sker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.group3_sker.Domain.Product;
import com.example.group3_sker.Helper.ChangeNumberItemsListener;
import com.example.group3_sker.Helper.ManagementCart;
import com.example.group3_sker.R;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    private ArrayList<Product> listItemSelected;
    private ManagementCart managementCart;
    private ChangeNumberItemsListener changeNumberItemsListener;

    public CartListAdapter(ArrayList<Product> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listItemSelected = listItemSelected;
        this.managementCart = new ManagementCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product item = listItemSelected.get(position);

        if (item != null) {
            holder.title.setText(item.getName());
            holder.feeEachItem.setText("$" + String.format("%.1f", item.getPrice()));
            holder.totalEachItem.setText("$" + String.format("%.1f", item.getPrice() * item.getNumberInCart()));
            holder.num.setText(String.valueOf(item.getNumberInCart()));

            // Load image using Glide from URL
            Glide.with(holder.itemView.getContext())
                    .load(item.getPicUrl()) // Load image from URL
                    .placeholder(R.drawable.pic1) // Placeholder image while loading
                    .error(R.drawable.pic2) // Error image if loading fails
                    .transform(new GranularRoundedCorners(30, 30, 0, 0)) // Rounded corners
                    .into(holder.pic);

            holder.plusItem.setOnClickListener(v -> {
                managementCart.plusNumberItem(listItemSelected, position, () -> {
                    notifyItemChanged(position);
                    changeNumberItemsListener.change();
                });
            });

            holder.minusItem.setOnClickListener(v -> {
                managementCart.minusNumberItem(listItemSelected, position, () -> {
                    notifyItemChanged(position);
                    changeNumberItemsListener.change();
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, feeEachItem, plusItem, minusItem, totalEachItem, num;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            num = itemView.findViewById(R.id.numberInCartTxt);
        }
    }
}

