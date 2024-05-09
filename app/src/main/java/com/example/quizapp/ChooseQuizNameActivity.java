package com.example.quizapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChooseQuizNameActivity extends AppCompatActivity {

    private EditText quizNameEditText;
    private ImageView back, next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz_name);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        findViews();

        next.setOnClickListener(v -> {
            String quizName = quizNameEditText.getText().toString();
            if (quizName.length() < 4) {
                Toast.makeText(this, "Quiz name must be at least 4 letters long!", Toast.LENGTH_SHORT).show();
            } else {
                checkIfQuizNameExists(quizName);
            }
        });

        back.setOnClickListener(v -> {
           Intent intent = new Intent(ChooseQuizNameActivity.this, MainActivity.class);
           startActivity(intent);
        });

    }

    private void findViews() {
        quizNameEditText = findViewById(R.id.quizNameEditText);
        next = findViewById(R.id.quizNameNextImageView);
        back = findViewById(R.id.quizNameBackImageView);
    }


    private void checkIfQuizNameExists(String quizName) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {

            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String creatorUsername = documentSnapshot.getString("username");
                            db.collection("quizzes")
                                    .whereEqualTo("name", quizName)
                                    .whereEqualTo("createdBy", creatorUsername)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().isEmpty()) {
                                                Log.d("Banana", "Username available");
                                                Intent intent = new Intent(ChooseQuizNameActivity.this, ChooseCategoryApiActivity.class);
                                                intent.putExtra("quizName", quizName);
                                                startActivity(intent);
                                            } else {
                                                // name is taken, show error message
                                                Toast.makeText(ChooseQuizNameActivity.this, "You already have a quiz with this name", Toast.LENGTH_SHORT).show();

                                            }
                                        } else {
                                            Toast.makeText(ChooseQuizNameActivity.this, "error occurred", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Log.d("Firestore", "User document does not exist");
                            Toast.makeText(this, "Failed to retrieve user data. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w("Firestore", "Error retrieving user document", e);
                        Toast.makeText(this, "Failed to retrieve user data. Please try again.", Toast.LENGTH_SHORT).show();
                    });

        }
    }

}