package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ViewQuestionsActivity extends AppCompatActivity {

    private Button returnToHomeButton;
    private RecyclerView recyclerView;
    private QuestionAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_questions);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        findViews();

        Intent intent = new Intent(ViewQuestionsActivity.this, MainActivity.class);

        Intent intent1 = getIntent();

        returnToHomeButton.setOnClickListener(view -> {
            intent.putExtra("quizName", intent1.getStringExtra("quizName"));
            intent.putExtra("categoryName", intent1.getStringExtra("categoryName"));
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuestionAdapter();
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("quizzes").document(intent1.getStringExtra("quizName")).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Quiz quiz = documentSnapshot.toObject(Quiz.class);
                        if (quiz != null) {
                            List<Question> questions = quiz.getQuestions();
                            adapter.setQuestions(questions);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                });
    }


    private void findViews() {
        returnToHomeButton = findViewById(R.id.returnToHomeButton);
        recyclerView = findViewById(R.id.questionsRecyclerView);
    }
}