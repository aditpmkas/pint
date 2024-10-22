package com.example.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private Button btnLogin, btnRegister;
    private EditText editEmail, editPassword;
    private ProgressDialog progressDialog;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize EditText and Buttons
        editEmail = findViewById(R.id.email_login);
        editPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_Register);

        // Get the email passed from MainActivity and pre-fill it
        String emailFromMain = getIntent().getStringExtra("email_key");
        if (emailFromMain != null) {
            editEmail.setText(emailFromMain);
        }

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

        // Initialize SQLite database helper and open writable database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Set login button click listener
        btnLogin.setOnClickListener(view -> {
            if (editEmail.getText().length() > 0 && editPassword.getText().length() > 0) {
                login(editEmail.getText().toString(), editPassword.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), "Please fill Email and Password", Toast.LENGTH_SHORT).show();
            }
        });

        // Set register button click listener
        btnRegister.setOnClickListener(v -> {
            Intent intentRegister = new Intent(Login.this, Register.class);
            startActivity(intentRegister);
        });
    }

    // Login function with SQLite query
    private void login(String email, String password) {
        progressDialog.show();

        try {
            // Query the database to check if the email and password exist
            Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?",
                    new String[]{email, password});

            if (cursor != null && cursor.moveToFirst()) {
                // Login successful, navigate to Navbar which will handle fragment transactions
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                navigateToNavbar();
            } else {
                // Login failed
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void navigateToNavbar() {
        // Redirect to Navbar after successful login
        Intent intent = new Intent(getApplicationContext(), Navbar.class);
        startActivity(intent);
        finish(); // Close login activity
    }
}
