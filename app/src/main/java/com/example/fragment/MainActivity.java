package com.example.fragment;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private Button con;
    private EditText emailInput;
    private DatabaseHelper databaseHelper;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    // To track the current orientation
    private String currentOrientation = "Portrait";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        con = findViewById(R.id.btn_con);
        emailInput = findViewById(R.id.email);
        databaseHelper = new DatabaseHelper(this);

        // Initialize Sensor Manager for device orientation detection
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // Set button click listener
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();

                // Check if the email is empty or invalid
                if (email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please insert email", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(MainActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                } else {
                    // Check email in the database and navigate accordingly
                    checkEmailInDatabase(email);
                }
            }
        });
    }

    // Register the sensor listener when the activity resumes
    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // Unregister the sensor listener when the activity pauses
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    // Detect device orientation using accelerometer data
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        String newOrientation = currentOrientation;

        // Determine orientation based on accelerometer data
        if (Math.abs(y) > Math.abs(x)) {
            if (y > 0) {
                newOrientation = "Portrait";
            } else {
                newOrientation = "Portrait Upside Down"; // Flip/Reverse portrait
            }
        } else {
            if (x > 0) {
                newOrientation = "Landscape Left";
            } else {
                newOrientation = "Landscape Right";
            }
        }

        // If the orientation has changed, notify the user with a Toast message
        if (!newOrientation.equals(currentOrientation)) {
            currentOrientation = newOrientation;
            Toast.makeText(this, "Device Orientation: " + currentOrientation, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }

    // Validate email format
    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Check if the email exists in the SQLite database
    private void checkEmailInDatabase(String email) {
        if (databaseHelper.checkUser(email)) {
            // Email exists, move to Login page with the email pre-filled
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.putExtra("email_key", email);
            startActivity(intent);
        } else {
            // New email, move to Register page with the email pre-filled
            Intent intent = new Intent(MainActivity.this, Register.class);
            intent.putExtra("email_key", email);
            startActivity(intent);
        }
    }
}
