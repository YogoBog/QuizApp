package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nonnull;

public class ViewYourQuizzesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuizAdapter adapter;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_your_quizzes);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        init();

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
                                    .whereEqualTo("createdBy", creatorUsername)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            List<Quiz> quizzes = task.getResult().toObjects(Quiz.class);
                                            adapter.setQuizzes(quizzes);
                                        } else {
                                            Toast.makeText(ViewYourQuizzesActivity.this, "Error occurred", Toast.LENGTH_LONG).show();
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


            ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@Nonnull RecyclerView recyclerView, @Nonnull RecyclerView.ViewHolder viewHolder, @Nonnull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@Nonnull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    Quiz deletedQuiz = adapter.getQuiz(position);

                    db.getInstance().collection("quizzes").document(adapter.getQuiz(position).getName()).delete();
                    adapter.removeQuiz(position);
                    adapter.notifyItemRemoved(position);
//                    Toast.makeText(ViewYourQuizzesActivity.this, "Quiz Deleted", Toast.LENGTH_SHORT).show();
                    Snackbar.make(recyclerView, "Quiz Deleted", Snackbar.LENGTH_LONG).setAction("UNDO", view -> {
                        db.collection("quizzes").document(deletedQuiz.getName()).set(deletedQuiz);
                        adapter.addQuiz(position, deletedQuiz);
                        recyclerView.scrollToPosition(position);
                    }).show();


                }
            };

            ItemTouchHelper helper = new ItemTouchHelper(callback);
            helper.attachToRecyclerView(recyclerView);

        }




    }

    private void init() {
        back = findViewById(R.id.yourQuizzesBackImg);
        recyclerView = findViewById(R.id.quizzesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new QuizAdapter(view -> {
            int position = recyclerView.getChildAdapterPosition(view);
            Quiz clickedQuiz = adapter.getQuizzes().get(position);
            Intent intent = new Intent(ViewYourQuizzesActivity.this, ViewQuestionsActivity.class);
            intent.putExtra("quizName", clickedQuiz.getName());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(ViewYourQuizzesActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

}