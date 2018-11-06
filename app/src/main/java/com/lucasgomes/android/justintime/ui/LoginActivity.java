package com.lucasgomes.android.justintime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.lucasgomes.android.justintime.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.btn_login).setOnClickListener((v) -> {
            signIn(((TextView)findViewById(R.id.tiet_email)).getText().toString(),
                    ((TextView)findViewById(R.id.tiet_password)).getText().toString());
        });
    }

    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startMainActivity();
                    }
                })
        .addOnFailureListener(this, exception -> {
            if (exception instanceof FirebaseAuthInvalidUserException) {
                signUp(email, password);
            } else {
                showError(exception.getMessage());
            }
        });
    }

    private void signUp(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startMainActivity();
                    }
                })
                .addOnFailureListener(this, exception -> {
                    showError(exception.getMessage());
                });
    }

    private void startMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void showError(String message) {
        if (getCurrentFocus() != null) {
            Snackbar snackbar = Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight));
            snackbar.setAction(R.string.dismiss, (v) -> snackbar.dismiss()).show();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
}
