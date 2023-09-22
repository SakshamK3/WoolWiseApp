package com.example.woolapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterAs extends AppCompatActivity {
    View back;
    View vector;
    Button producer;
    Button industrialist;
    Button client;
    TextView learn_more;

    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as);

        back = findViewById(R.id.back);
        vector = findViewById(R.id.vector);
        producer = findViewById(R.id.producer_button);
        industrialist = findViewById(R.id.industrialist_button);
        client = findViewById(R.id.consumer_button);
        learn_more = findViewById(R.id.learn_more_);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Check if type is already present for the user
        String userId = auth.getCurrentUser().getUid();
        db.collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if (document.contains("type")) {
                                String userType = document.getString("type");
                                if (userType != null && !userType.isEmpty()) {
                                    // Type is already set, navigate to MainActivity
                                    startActivity(new Intent(RegisterAs.this, MainActivity.class));
                                    finish(); // Finish current activity to prevent going back to it
                                }
                            }
                        }
                    }
                });

        producer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTraderType("Producer");
            }
        });

        industrialist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTraderType("Industrialist");
            }
        });

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTraderType("Client");
            }
        });
    }

    private void saveTraderType(String type) {
        String userId = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .update("type", type)
                .addOnSuccessListener(aVoid -> {
                    // Data successfully updated
                    startActivity(new Intent(RegisterAs.this, MainActivity.class));
                    finish(); // Finish current activity to prevent going back to it
                })
                .addOnFailureListener(e -> {
                    // Error updating data
                });
    }
}
