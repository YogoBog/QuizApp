package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ViewQuizzesByCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuizAdapter adapter;
    private AutoCompleteTextView searchedQuiz;
    private List<Quiz> quizzes = new ArrayList<>();
    private ArrayAdapter<String> quizAdapter;
    private List<String> quizzesNames = new ArrayList<>();
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quizzes_by_category);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        init();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("categoryName");

        if (currentUser != null) {

            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            db.collection("quizzes")
                                    .whereEqualTo("category", categoryName)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            quizzes = task.getResult().toObjects(Quiz.class);
                                            for (Quiz quiz : quizzes) {
                                                quizzesNames.add(quiz.getName());
                                            }
                                            adapter.setQuizzes(quizzes);


                                            quizAdapter = new ArrayAdapter<>(ViewQuizzesByCategoryActivity.this, android.R.layout.simple_dropdown_item_1line, quizzesNames);
                                            searchedQuiz.setAdapter(quizAdapter);

                                            searchedQuiz.setOnEditorActionListener((textView, i, keyEvent) -> {
                                                String input = textView.getText().toString();
                                                quizAdapter.getFilter().filter(input);
                                                return true;
                                            });

                                            searchedQuiz.setOnItemClickListener((adapterView, view, i, l) -> {
                                                Intent intent1 = new Intent(ViewQuizzesByCategoryActivity.this, PlayQuizActivity.class);
                                                intent1.putExtra("quizName", searchedQuiz.getText().toString());
                                                Log.d("TAG", searchedQuiz.getText().toString());
                                                startActivity(intent1);
                                            });

                                        } else {
                                            Toast.makeText(ViewQuizzesByCategoryActivity.this, "Error occurred", Toast.LENGTH_LONG).show();
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

    private void init() {
        back = findViewById(R.id.viewCatBackButton);
        searchedQuiz = findViewById(R.id.searchCategoryQuizzesTextView);
        recyclerView = findViewById(R.id.categoryQuizzesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new QuizAdapter(view -> {
            int position = recyclerView.getChildAdapterPosition(view);
            Quiz clickedQuiz = adapter.getQuizzes().get(position);
            Intent intent = new Intent(ViewQuizzesByCategoryActivity.this, PlayQuizActivity.class);
            intent.putExtra("quizName", clickedQuiz.getName());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(ViewQuizzesByCategoryActivity.this, MainActivity.class);
            startActivity(intent);
        });


    }
}