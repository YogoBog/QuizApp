package com.example.quizapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddQuestionActivity extends AppCompatActivity {


    private Question question;
    private Button uploadButton, finishButton;
    private EditText questionEditText, answerAedt, answerBedt, answerCedt, answerDedt;
    private RadioButton answerA, answerB, answerC, answerD;
    private Button okButton, cancelButton;
    private String correctAnswer;
    private List<Question> questions;
    private Intent intent;
    private boolean isCurrQuestionFinished;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        init();

        intent = getIntent();
        String quizName = intent.getStringExtra("quizName");
        String categoryName = intent.getStringExtra("categoryName");

        questions = new ArrayList<>();

        isCurrQuestionFinished = false;

        uploadButton.setOnClickListener(view -> {

            if (TextUtils.isEmpty(questionEditText.getText().toString())) {
                questionEditText.setError("Write any question");
                questionEditText.requestFocus();
            } else if (TextUtils.isEmpty(answerAedt.getText().toString()) || TextUtils.isEmpty(answerBedt.getText().toString())
                    || TextUtils.isEmpty(answerCedt.getText().toString()) || TextUtils.isEmpty(answerDedt.getText().toString())) {
                Toast.makeText(AddQuestionActivity.this, "Every option needs an answer!", Toast.LENGTH_SHORT).show();
            } else {

                if (answerA.isChecked())
                    correctAnswer = answerAedt.getText().toString();
                else if (answerB.isChecked())
                    correctAnswer = answerBedt.getText().toString();
                else if (answerC.isChecked())
                    correctAnswer = answerCedt.getText().toString();
                else if (answerD.isChecked())
                    correctAnswer = answerDedt.getText().toString();

                isCurrQuestionFinished = true;


                question = new Question(questionEditText.getText().toString(), answerAedt.getText().toString()
                        , answerBedt.getText().toString(), answerCedt.getText().toString(), answerDedt.getText().toString(), correctAnswer);

                questions.add(question);
                emptyEditTexts();
                Toast.makeText(AddQuestionActivity.this, "Question Added", Toast.LENGTH_SHORT).show();
            }

        });

        finishButton.setOnClickListener(view -> {
            if (isCurrQuestionFinished) {
                showConfirmDialog();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("users").document(userId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    // get the username and mail from the user document
                                    String creatorUsername = documentSnapshot.getString("username");
                                    String creatorMail = documentSnapshot.getString("email");

                                    Quiz quiz = new Quiz(categoryName, quizName, creatorUsername, creatorMail, questions);

                                    db.collection("quizzes").document(quizName).set(quiz);


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
            } else
                Toast.makeText(AddQuestionActivity.this, "Need at least 1 question", Toast.LENGTH_SHORT).show();
        });
    }


    private void init() {

        dialog = new Dialog(AddQuestionActivity.this);
        dialog.setContentView(R.layout.finish_quiz_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.end_quiz_dialog_bg));
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        okButton = dialog.findViewById(R.id.okButton);
        cancelButton = dialog.findViewById(R.id.cancelButton);

        uploadButton = findViewById(R.id.uploadQuestionButton);
        finishButton = findViewById(R.id.finishButton);
        questionEditText = findViewById(R.id.questionEditText);
        answerAedt = findViewById(R.id.answerAEditText);
        answerBedt = findViewById(R.id.answerBEditText);
        answerCedt = findViewById(R.id.answerCEditText);
        answerDedt = findViewById(R.id.answerDEditText);
        answerA = findViewById(R.id.answerARadioButton);
        answerB = findViewById(R.id.answerBRadioButton);
        answerC = findViewById(R.id.answerCRadioButton);
        answerD = findViewById(R.id.answerDRadioButton);
    }

    private void emptyEditTexts() {
        questionEditText.setText("");
        answerAedt.setText("");
        answerBedt.setText("");
        answerCedt.setText("");
        answerDedt.setText("");
    }

    private void showConfirmDialog() {
        okButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(AddQuestionActivity.this, ViewQuestionsActivity.class);
            intent1.putExtra("quizName", intent.getStringExtra("quizName"));
            intent1.putExtra("categoryName", intent.getStringExtra("categoryName"));
            startActivity(intent1);
            finish();
        });

        cancelButton.setOnClickListener(v -> {
            dialog.cancel();
        });

        dialog.show();

    }


}