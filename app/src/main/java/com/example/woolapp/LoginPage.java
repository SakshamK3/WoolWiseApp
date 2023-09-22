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

public class LoginPage extends AppCompatActivity {
    TextView btn;
    TextView forgotPassword;
    EditText username;
    EditText email;
    EditText password;
    Button login;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        auth = FirebaseAuth.getInstance();
        btn = findViewById(R.id.textViewSignUp);
        forgotPassword = findViewById(R.id.forgotPassword);
        username = findViewById(R.id.inUsername);
        email = findViewById(R.id.inEmail);
        password = findViewById(R.id.inPassword);
        login = findViewById(R.id.btnlogin);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        if(auth.getCurrentUser() != null){
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(this, "please wait you are already logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginPage.this, MainActivity.class));
        }

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, ForgotPassword.class));
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, Register.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loginUser() {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginPage.this, LanguageSelectPage.class));
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginPage.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
