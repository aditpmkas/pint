package com.example.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    private EditText editName, editEmail, editPassword, editPasswordConf;
    private Button btnRegister, btnLogin;
    private ProgressDialog progressDialog;
    private DatabaseHelper databaseHelper; // SQLite database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        editName = findViewById(R.id.name);
        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        editPasswordConf = findViewById(R.id.password_Confirm);
        btnRegister = findViewById(R.id.btn_up_register);
        btnLogin = findViewById(R.id.btn_back_login);

        // Get the email passed from MainActivity and pre-fill it
        String emailFromMain = getIntent().getStringExtra("email_key");
        if (emailFromMain != null) {
            editEmail.setText(emailFromMain);
        }

        // Initialize SQLite helper
        databaseHelper = new DatabaseHelper(this);
        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

        // Login button to go back to login screen
        btnLogin.setOnClickListener(view -> {
            finish(); // Go back to login activity
        });

        // Register button to validate inputs and register user
        btnRegister.setOnClickListener(view -> {
            if (editEmail.getText().length() > 0 && editName.getText().length() > 0 && editPassword.getText().length() > 0) {
                if (editPassword.getText().toString().equals(editPasswordConf.getText().toString())) {
                    // Register user with SQLite
                    registerWithSQLite(editName.getText().toString(), editEmail.getText().toString(), editPassword.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please fill all the information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to register a user with SQLite
    private void registerWithSQLite(String name, String email, String password) {
        progressDialog.show();

        // Check if the user already exists in SQLite
        if (!databaseHelper.checkUser(email)) {
            // Insert user into SQLite database
            boolean insert = databaseHelper.insertUser(name, email, password);
            progressDialog.dismiss();

            if (insert) {
                Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                finish(); // Go back to login after successful registration
            } else {
                Toast.makeText(getApplicationContext(), "Registration Failed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "User already exists. Please use a different email.", Toast.LENGTH_SHORT).show();
        }
    }
}
