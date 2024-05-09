package com.example.quizapp;

import static com.example.quizapp.SignInActivity.togglePasswordVisibility;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView signInTextView;
    private EditText emailLoginEditText, passwordLoginEditText;
    private ProgressBar loginProgressBar;
    private FirebaseAuth auth;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        findViews();

        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> {

            String emailText = emailLoginEditText.getText().toString();
            String passwordText = passwordLoginEditText.getText().toString();

            if (TextUtils.isEmpty(emailText)) {

                Toast.makeText(LoginActivity.this, "Please enter an Email", Toast.LENGTH_LONG).show();
                emailLoginEditText.setError("Email is required");
                emailLoginEditText.requestFocus();

            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {

                Toast.makeText(LoginActivity.this, "Please re-enter an Email", Toast.LENGTH_LONG).show();
                emailLoginEditText.setError("Valid Email is required");
                emailLoginEditText.requestFocus();

            } else if (TextUtils.isEmpty(passwordText)) {

                Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_LONG).show();
                passwordLoginEditText.setError("Password is required");
                passwordLoginEditText.requestFocus();

            } else {
                loginProgressBar.setVisibility(View.VISIBLE);
                loginUser(emailText, passwordText);


            }
        });

        signInTextView.setPaintFlags(signInTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //underline

        signInTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
            startActivity(intent);
        });

        passwordLoginEditText.setOnTouchListener((v, event) -> {
            // Check if the event is within the drawable end bounds
            if (event.getAction() == MotionEvent.ACTION_UP && event.getRawX() >= (passwordLoginEditText.getRight() - passwordLoginEditText.getCompoundDrawables()[2].getBounds().width())) {
                // Toggle password visibility
                togglePasswordVisibility(passwordLoginEditText);
                return true;
            }
            return false;
        });

    }

    // check if user already logged in
    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }


    private void loginUser(String emailText, String passwordText) {
        auth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(LoginActivity.this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                    emailLoginEditText.setError("User doesn't exists. Please register again.");
                    emailLoginEditText.requestFocus();
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    emailLoginEditText.setError("Invalid credentials, Please check again.");
                    emailLoginEditText.requestFocus();
                } catch (Exception e) {
                    Log.e("Banana", e.getMessage());
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            loginProgressBar.setVisibility(View.GONE);
        });
    }

    private void findViews() {
        loginButton = findViewById(R.id.loginButton);
        signInTextView = findViewById(R.id.signInTextView);
        emailLoginEditText = findViewById(R.id.emailLoginEditText);
        passwordLoginEditText = findViewById(R.id.passwordLoginEditText);
        loginProgressBar = findViewById(R.id.loginProgressBar);
    }
}