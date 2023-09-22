package com.example.woolapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        ImageView marketplace = findViewById(R.id.marketplace);
        marketplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MarketplaceActivity.class));
            }
        });
        ImageView qr = findViewById(R.id.qr);
        ImageView client = findViewById(R.id.clients_contact);
        ImageView notification = findViewById(R.id.notification);
        ImageView history = findViewById(R.id.history);
        ImageView supply = findViewById(R.id.supply);
        ImageView bills = findViewById(R.id.bills);
        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DueBillsPage.class));
            }
        });
        ImageView revenue = findViewById(R.id.revenue);
        revenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RevenuePage.class));
            }
        });
        TextView usernameText = findViewById(R.id.username_text);
        fetchUsernameFromFirestore(usernameText);

        ImageView menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ClientsPage.class));
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NotificationsPage.class));
            }
        });
    }

    private void fetchUsernameFromFirestore(TextView usernameText) {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String username = document.getString("username");
                                usernameText.setText(username);
                            }
                        }
                    });
        }
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenu().add(0, 1, 1, "Logout"); // Add a menu item
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1: // Check if the selected item is the logout option
                        logout();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(MainActivity.this, LoginPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
