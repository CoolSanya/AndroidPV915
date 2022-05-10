package com.example.shop.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import  com.example.shop.R;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle b = getIntent().getExtras();
        long id = b.getLong("id");
        TextView textView = findViewById(R.id.txtUserEmail);
        textView.setText(String.valueOf(id));
    }
}