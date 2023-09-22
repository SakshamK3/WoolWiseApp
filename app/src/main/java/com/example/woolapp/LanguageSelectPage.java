package com.example.woolapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LanguageSelectPage extends AppCompatActivity {
    Button english;
    Button hindi;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select_page);
        english = findViewById(R.id.English_rect);
        hindi = findViewById(R.id.Hindi_rect);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLanguagePreference("English");
            }
        });

        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLanguagePreference("Hindi");
            }
        });
    }

    private void updateLanguagePreference(String selectedLanguage) {
        String userId = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .update("language", selectedLanguage)
                .addOnSuccessListener(aVoid -> {
                    // Data successfully updated
                    checkAndRedirect();
                })
                .addOnFailureListener(e -> {
                    // Error updating data
                });
    }

    private void checkAndRedirect() {
        String userId = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            if (document.contains("language")) {
                                String selectedLanguage = document.getString("language");
                                if (selectedLanguage != null && !selectedLanguage.isEmpty()) {

                                    startActivity(new Intent(LanguageSelectPage.this, RegisterAs.class));
                                    finish();
                                }
                            }
                        }
                    }
                });
    }
}
