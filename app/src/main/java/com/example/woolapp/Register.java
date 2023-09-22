package com.example.woolapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woolapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    FirebaseAuth auth;
    EditText username;
    EditText email;
    EditText password;
    EditText confirm;
    Button register;
    TextView account;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.inputUsername);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        confirm = findViewById(R.id.inputConformPassword);
        register = findViewById(R.id.btnRegister);
        account = findViewById(R.id.alreadyHaveAccount);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, LoginPage.class));
            }
        });
    }

    private void createUser() {
        String userName = username.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userConfirm = confirm.getText().toString();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(userConfirm)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!userConfirm.equals(userPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPassword.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = task.getResult().getUser().getUid();

                            // Create a new document in Firestore with the user's information
                            Map<String, Object> user = new HashMap<>();
                            user.put("username", userName);
                            user.put("email", userEmail);
                            user.put("password", userPassword);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(userId).set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Register.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Register.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
