package com.example.woolapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.woolapp.databinding.ActivityMarketplaceBinding;
import com.example.woolapp.models.WoolProduct;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MarketplaceActivity extends AppCompatActivity {

    private ActivityMarketplaceBinding binding;
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private FirebaseFirestore db;
    private List<WoolProduct> productList;
    private TextInputLayout searchInputLayout;
    private TextInputEditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMarketplaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        binding.toolbarLayout.setTitle(getTitle());


        db = FirebaseFirestore.getInstance();


        productRecyclerView = binding.productRecyclerView;
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        productRecyclerView.setAdapter(productAdapter);

        // Initialize search components
        searchInputLayout = binding.searchInputLayout;
        searchEditText = binding.searchEditText;


        loadProducts();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterProducts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void loadProducts() {

        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            WoolProduct product = document.toObject(WoolProduct.class);
                            productList.add(product);
                        }


                        productAdapter.notifyDataSetChanged();
                    } else {
                        // Handle error
                    }
                });
    }

    private void filterProducts(String query) {
        List<WoolProduct> filteredList = new ArrayList<>();
        for (WoolProduct product : productList) {
            if (product.getProductCode().toLowerCase().contains(query.toLowerCase()) ||
                    product.getName().toLowerCase().contains(query.toLowerCase()) ||
                    product.getCategory().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productAdapter.setProductList(filteredList);
        productAdapter.notifyDataSetChanged();
    }
}