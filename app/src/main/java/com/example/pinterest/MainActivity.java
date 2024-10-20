package com.example.pinterest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQ_ONE_TAP = 2;
    private boolean showOneTapUI = true;

    private SignInClient oneTapClient;  // One Tap client
    private BeginSignInRequest signInRequest;

    FirebaseDatabase database;
    private Button con, btngoogle;
    private EditText emailInput;
    private FirebaseAuth auth;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        con = findViewById(R.id.btn_con);
        emailInput = findViewById(R.id.email);
        auth = FirebaseAuth.getInstance();
        databaseHelper = new DatabaseHelper(this);
        btngoogle = findViewById(R.id.btn_google);
        database = FirebaseDatabase.getInstance();

        // Configure One Tap client
        oneTapClient = Identity.getSignInClient(this);

        // Create One Tap sign-in request
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))  // Server client ID from Google Cloud Console
                        .setFilterByAuthorizedAccounts(true)  // Only show accounts previously signed in
                        .build())
                .build();

        btngoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapSignIn();
            }
        });

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please insert email", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(MainActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                } else {
                    checkEmailInFirebase(email);
                }
            }
        });
    }

    // Method to initiate One Tap Sign-In
    private void oneTapSignIn() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, result -> {
                    try {
                        startIntentSenderForResult(result.getPendingIntent().getIntentSender(), REQ_ONE_TAP, null, 0, 0, 0);
                    } catch (Exception e) {
                        Log.e(TAG, "Could not start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(MainActivity.this, "One Tap sign-in failed", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();

                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken);
                } else {
                    Log.d(TAG, "No ID token found.");
                }
            } catch (ApiException e) {
                Log.e(TAG, "One Tap sign-in failed: " + e.getLocalizedMessage());
            }
        }
    }

    // Method to authenticate with Firebase using Google ID token
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Sign-in successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            saveUserToFirebase(user);
                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Save user information to Firebase Database
    private void saveUserToFirebase(FirebaseUser user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", Objects.requireNonNull(user).getDisplayName());
        map.put("email", user.getEmail());
        map.put("profile", user.getPhotoUrl().toString());

        database.getReference().child("googleAuthUsers").child(user.getUid()).setValue(map)
                .addOnCompleteListener(task -> {
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                    finish();
                });
    }

    private void checkEmailInFirebase(String email) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    intent.putExtra("email_key", email);
                    startActivity(intent);
                } else {
                    if (databaseHelper.checkUser(email)) {
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        intent.putExtra("email_key", email);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, Register.class);
                        intent.putExtra("email_key", email);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
