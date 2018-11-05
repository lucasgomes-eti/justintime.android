package com.lucasgomes.android.justintime.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lucasgomes.android.justintime.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btn_login).setOnClickListener((v) -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
