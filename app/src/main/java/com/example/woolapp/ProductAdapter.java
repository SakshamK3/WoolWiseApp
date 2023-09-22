package com.example.woolapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.woolapp.models.WoolProduct;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<WoolProduct> productList;

    public ProductAdapter(List<WoolProduct> productList) {
        this.productList = productList;
    }

    public void setProductList(List<WoolProduct> productList) {
        this.productList = productList;
    }

    public List<WoolProduct> getProductList() {
        return productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WoolProduct product = productList.get(position);


        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String productId = product.getProductCode();
        StorageReference imageRef = storageRef.child("product/" + productId + ".jpeg");

        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {

            Picasso.get().load(uri).into(holder.productImageView);
        }).addOnFailureListener(e -> {

            holder.productImageView.setImageResource(R.drawable.placeholder_image);
        });

        holder.productNameTextView.setText(product.getName());
        holder.productDescriptionTextView.setText(product.getDescription());
        holder.productPriceTextView.setText(String.format("$%.2f", product.getPrice()));
        holder.productCategoryTextView.setText(product.getCategory());
        holder.productStockQuantityTextView.setText(String.valueOf(product.getStockQuantity()));
        holder.productCodeTextView.setText(product.getProductCode());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productDescriptionTextView;
        TextView productPriceTextView;
        TextView productCategoryTextView;
        TextView productStockQuantityTextView;
        TextView productCodeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productDescriptionTextView = itemView.findViewById(R.id.productDescriptionTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            productCategoryTextView = itemView.findViewById(R.id.productCategoryTextView);
            productStockQuantityTextView = itemView.findViewById(R.id.productStockQuantityTextView);
            productCodeTextView = itemView.findViewById(R.id.productCodeTextView);
        }
    }
}

