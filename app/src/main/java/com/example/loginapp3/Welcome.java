package com.example.loginapp3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.os.Bundle;

public class Welcome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Retrieve the email passed from LoginActivity
        String email = getIntent().getStringExtra("email");

        // Find the TextView in your layout
        TextView textView2 = findViewById(R.id.textView2);

        // Set the email to the TextView
        if (email != null) {
            textView2.setText("Welcome, " + email);
        }
    }
}