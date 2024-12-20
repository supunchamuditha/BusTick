package com.example.proj;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RegisterActivity extends AppCompatActivity {
    private ImageView imageView;
    private FloatingActionButton button;
    private EditText etUsername, etEmail, etMobile, etPassword;
    private DatabaseHelper databaseHelper;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        imageView = findViewById(R.id.image_view1);
        button = findViewById(R.id.floatingActionButton);
        etUsername = findViewById(R.id.editTextName);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        etMobile = findViewById(R.id.editTextPhone);
        spinner = findViewById(R.id.spinner);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Spinner Setup
        String[] roles = {"Passenger", "Bus Owner", "Driver"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(RegisterActivity.this)
                        .crop() // Crop image(Optional), Check Customization for more option
                        .compress(1024) // Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080) // Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate input fields
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String mobile = etMobile.getText().toString().trim();
                String role = spinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(username)) {
                    etUsername.setError("Username cannot be empty");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Email cannot be empty");
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Invalid email format");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Password cannot be empty");
                    return;
                }
                if (password.length() < 6) {
                    etPassword.setError("Password must be at least 6 characters");
                    return;
                }
                if (TextUtils.isEmpty(mobile)) {
                    etMobile.setError("Mobile number cannot be empty");
                    return;
                }
                if (mobile.length() != 10 || !mobile.matches("\\d+")) {
                    etMobile.setError("Enter a valid 10-digit mobile number");
                    return;
                }

                // Register user in the database
                long isInserted = databaseHelper.insertUser(username, email, password, email, role);
                if (isInserted != 1) {
                    // Show success message
                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                    // Navigate to LoginActivity
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration Failed. User may already exist.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView textView7 = findViewById(R.id.textView7);
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri uri = data.getData();
            imageView.setImageURI(uri);
        }
    }
}
