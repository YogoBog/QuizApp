package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private Button signInButton;
    private TextView loginTextView;
    private EditText emailSignInEditText, usernameSignInEditText, passwordSignInEditText;
    private ProgressBar signInProgressBar;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        findViews();

        signInButton.setOnClickListener(v -> {

            String emailText = emailSignInEditText.getText().toString();
            String usernameText = usernameSignInEditText.getText().toString();
            String passwordText = passwordSignInEditText.getText().toString();

            if (TextUtils.isEmpty(emailText)) {

                Toast.makeText(SignInActivity.this, "Please enter an Email", Toast.LENGTH_LONG).show();
                emailSignInEditText.setError("Email is required");
                emailSignInEditText.requestFocus();

            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {

                Toast.makeText(SignInActivity.this, "Please re-enter an Email", Toast.LENGTH_LONG).show();
                emailSignInEditText.setError("Valid Email is required");
                emailSignInEditText.requestFocus();

            } else if (TextUtils.isEmpty(usernameText)) {

                Toast.makeText(SignInActivity.this, "Please enter a username", Toast.LENGTH_LONG).show();
                usernameSignInEditText.setError("Username is required");
                usernameSignInEditText.requestFocus();

            } else if (TextUtils.isEmpty(passwordText)) {

                Toast.makeText(SignInActivity.this, "Please enter a password", Toast.LENGTH_LONG).show();
                passwordSignInEditText.setError("Password is required");
                passwordSignInEditText.requestFocus();

            } else if (passwordText.length() < 6) {

                Toast.makeText(SignInActivity.this, "Password should be at least 6 digits", Toast.LENGTH_LONG).show();
                passwordSignInEditText.setError("Password is weak");
                passwordSignInEditText.requestFocus();

            } else {

                signInProgressBar.setVisibility(View.VISIBLE);
                registerUser(usernameText, passwordText, emailText);

            }
        });

        loginTextView.setPaintFlags(loginTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //underline

        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        passwordSignInEditText.setOnTouchListener((v, event) -> {
            // Check if the event is within the drawable end bounds
            if (event.getAction() == MotionEvent.ACTION_UP && event.getRawX() >= (passwordSignInEditText.getRight() - passwordSignInEditText.getCompoundDrawables()[2].getBounds().width())) {
                // Toggle password visibility
                togglePasswordVisibility(passwordSignInEditText);
                return true;
            }
            return false;
        });
    }

    private void authenticateUser(String passwordText, String emailText, String usernameText) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(SignInActivity.this,
                (OnCompleteListener<AuthResult>) task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            // Save user data (email, username) to Firestore
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", emailText);
                            userData.put("username", usernameText);
                            userData.put("user Points", 0);
                            userData.put("addTimeAmount", 0);
                            userData.put("skipQuestionAmount", 0);
                            userData.put("jokerAmount", 0);
                            userData.put("halfHalfAmount", 0);
                            userData.put("doublePointsAmount", 0);

                            userData.put("profilePictureUrl", "https://www.shutterstock.com/image-vector/blank-avatar-photo-place-holder-600nw-1095249842.jpg");
                            db.collection("users")
                                    .document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        // User data saved successfully
//                                        Toast.makeText(SignInActivity.this, "User created successfully", Toast.LENGTH_LONG).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            // User registration failed
                            Toast.makeText(SignInActivity.this, "Failed to create user, try again later", Toast.LENGTH_LONG).show();

                        }

                        Toast.makeText(SignInActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthUserCollisionException e) {
                            emailSignInEditText.setError("A user with this email already exists");
                            emailSignInEditText.requestFocus();
                        } catch (Exception e) {
                            Log.e("Banana", e.getMessage());
                            Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        signInProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void findViews() {
        signInButton = findViewById(R.id.signInButton);
        loginTextView = findViewById(R.id.loginTextView);
        emailSignInEditText = findViewById(R.id.emailSignInEditText);
        usernameSignInEditText = findViewById(R.id.usernameSignInEditText);
        passwordSignInEditText = findViewById(R.id.passwordSignInEditText);
        signInProgressBar = findViewById(R.id.signInProgressBar);
    }

    public static void togglePasswordVisibility(EditText passwordEditText) {
        // Store current cursor position
        int selection = passwordEditText.getSelectionEnd();

        // Toggle password visibility
        if (passwordEditText.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            passwordEditText.setTransformationMethod(null); // Show password
        } else {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance()); // Hide password
        }

        // Restore cursor position
        passwordEditText.setSelection(selection);
    }

    private void registerUser(String username, String passwordText, String emailText) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            Log.d("Banana", "Username available");
                            authenticateUser(passwordText, emailText, username);
                        } else {
                            // Username is taken, show error message
                            usernameSignInEditText.setError("Username exists, choose another one");
                            usernameSignInEditText.requestFocus();
                            signInProgressBar.setVisibility(View.GONE);

                        }
                    } else {
                        Toast.makeText(SignInActivity.this, "error occurred", Toast.LENGTH_LONG).show();
                        signInProgressBar.setVisibility(View.GONE);
                    }
                });
    }


}