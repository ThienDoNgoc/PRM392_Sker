package com.example.group3_sker.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.group3_sker.Activity.DetailActivity;
import com.example.group3_sker.Model.Product;
import com.example.group3_sker.R;

import java.util.List;

public class PopularListAdapter extends RecyclerView.Adapter<PopularListAdapter.ViewHolder> {
    private List<Product> items;

    public PopularListAdapter(List<Product> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_pop_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = items.get(position);

        holder.titleTxt.setText(product.getName());
        holder.feeTxt.setText("$" + String.format("%.1f", product.getPrice()));
        holder.review1Txt.setText(String.valueOf(product.getReview()));
        holder.scoreTxt.setText(String.format("%.1f", product.getScore())); // Format score to 1 decimal place

        // Load image using Glide from URL
        Glide.with(holder.itemView.getContext())
                .load(product.getPicUrl()) // Load image from URL
                .placeholder(R.drawable.pic1) // Placeholder image while loading
                .error(R.drawable.pic2) // Error image if loading fails
                .transform(new GranularRoundedCorners(30, 30, 0, 0)) // Rounded corners
                .into(holder.pic);

        // Handle item click to open DetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("product", product); // Assuming Product implements Serializable or Parcelable
            holder.itemView.getContext().startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<Product> productList) {
        items.clear(); // Clear existing items
        items.addAll(productList); // Add new items
        notifyDataSetChanged(); // Notify RecyclerView of data change
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, feeTxt, scoreTxt, review1Txt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            feeTxt = itemView.findViewById(R.id.feeTxt);
            scoreTxt = itemView.findViewById(R.id.scoreTxt);
            pic = itemView.findViewById(R.id.pic);
            review1Txt = itemView.findViewById(R.id.review1Txt);
        }
    }
}
