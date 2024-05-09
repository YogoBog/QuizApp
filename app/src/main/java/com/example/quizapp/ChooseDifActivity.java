package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChooseDifActivity extends AppCompatActivity {

    private Button easyButton, hardButton, midButton;
    private String categoryName, apiUrl;
    private ProgressBar progressBar;
    private ImageView back;
    public static String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dif);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");

        findViews();

        easyButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            intent.putExtra("dif", "Easy");
            if (categoryName.equals("Sports")) {
                apiUrl = "https://opentdb.com/api.php?amount=10&category=21&difficulty=easy&type=multiple";
                loadQuiz(apiUrl);
            } else if (categoryName.equals("History")) {
                apiUrl = "https://opentdb.com/api.php?amount=10&category=23&difficulty=easy&type=multiple";
                loadQuiz(apiUrl);
            } else if (categoryName.equals("Movies")) {
                apiUrl = "https://opentdb.com/api.php?amount=10&category=11&difficulty=easy&type=multiple";
                loadQuiz(apiUrl);
            } else if (categoryName.equals("Science")) {
                apiUrl = "https://opentdb.com/api.php?amount=10&category=17&difficulty=easy&type=multiple";
                loadQuiz(apiUrl);
            } else if (categoryName.equals("TV - Shows")) {
                apiUrl = "https://opentdb.com/api.php?amount=10&category=14&difficulty=easy&type=multiple";
                loadQuiz(apiUrl);
            } else if (categoryName.equals("Books")) {
                apiUrl = "https://opentdb.com/api.php?amount=10&category=10&difficulty=easy&type=multiple";
                loadQuiz(apiUrl);
            } else if (categoryName.equals("Music")) {
                apiUrl = "https://opentdb.com/api.php?amount=10&category=12&difficulty=easy&type=multiple";
                loadQuiz(apiUrl);
            } else if (categoryName.equals("Video Games")) {
                apiUrl = "https://opentdb.com/api.php?amount=10&category=15&difficulty=easy&type=multiple";
                loadQuiz(apiUrl);
            } else if (categoryName.equals("Geography")) {
                apiUrl = "https://opentdb.com/api.php?amount=10&category=22&difficulty=easy&type=multiple";
                loadQuiz(apiUrl);
            } else if (categoryName.equals("Art")) {
                apiUrl = "https://opentdb.com/api.php?amount=10&category=25&difficulty=easy&type=multiple";
                loadQuiz(apiUrl);
            } else if (categoryName.equals("Celebrities")) {
                apiUrl = "https://opentdb.com/api.php?amount=10&category=26&difficulty=easy&type=multiple";
                loadQuiz(apiUrl);
            } else if (categoryName.equals("Vehicles")) {
                apiUrl = "https://opentdb.com/api.php?amount=10&category=28&difficulty=easy&type=multiple";
                loadQuiz(apiUrl);
            }
        });

        midButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            intent.putExtra("dif", "Medium");
            switch (categoryName) {
                case "Sports":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=21&difficulty=medium&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "History":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=23&difficulty=medium&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Movies":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=11&difficulty=medium&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Science":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=17&difficulty=medium&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "TV - Shows":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=14&difficulty=medium&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Books":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=10&difficulty=medium&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Music":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=12&difficulty=medium&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Video Games":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=15&difficulty=medium&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Geography":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=22&difficulty=medium&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Art":
                case "Celebrities":
//                    apiUrl = "https://opentdb.com/api.php?amount=10&category=26&difficulty=medium&type=multiple";
//                    apiUrl = "https://opentdb.com/api.php?amount=10&category=25&difficulty=medium&type=multiple";
//                    loadQuiz(apiUrl);
                    Toast.makeText(ChooseDifActivity.this, "Not Available Right Now", Toast.LENGTH_SHORT).show();
                    break;
                case "Vehicles":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=28&difficulty=medium&type=multiple";
                    loadQuiz(apiUrl);
                    break;
            }
        });


        hardButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            intent.putExtra("dif", "Hard");
            switch (categoryName) {
                case "Sports":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=21&difficulty=hard&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "History":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=23&difficulty=hard&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Movies":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=11&difficulty=hard&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Science":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=17&difficulty=hard&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "TV - Shows":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=14&difficulty=hard&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Books":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=10&difficulty=hard&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Music":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=12&difficulty=hard&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Video Games":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=15&difficulty=hard&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Geography":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=22&difficulty=hard&type=multiple";
                    loadQuiz(apiUrl);
                    break;
                case "Art":
                case "Celebrities":
//                apiUrl = "https://opentdb.com/api.php?amount=10&category=26&difficulty=hard&type=multiple";
//                apiUrl = "https://opentdb.com/api.php?amount=10&category=25&difficulty=hard&type=multiple";
//                loadQuiz(apiUrl);
                    Toast.makeText(ChooseDifActivity.this, "Not Available Right Now", Toast.LENGTH_SHORT).show();
                    break;
                case "Vehicles":
                    apiUrl = "https://opentdb.com/api.php?amount=10&category=28&difficulty=hard&type=multiple";
                    loadQuiz(apiUrl);
                    break;
            }
        });

        back.setOnClickListener(v -> {
            Intent intent1 = new Intent(ChooseDifActivity.this, ChooseCategoryApiActivity.class);
            startActivity(intent1);
        });

    }

    private void findViews() {
        easyButton = findViewById(R.id.easyButton);
        midButton = findViewById(R.id.midButton);
        hardButton = findViewById(R.id.hardButton);
        back = findViewById(R.id.chooseDifBackImageView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void loadQuiz(String apiUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Handle failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Get the response body as a string
                String responseData = response.body().string();
                Log.d("responseData", " " + responseData);

                // Parse JSON response
                try {
                    Log.d("try", "1 ");


                    JSONObject json = new JSONObject(responseData);
                    JSONArray results = json.getJSONArray("results");

                    // Extract quiz questions and options
                    List<Question> questions = new ArrayList<>();
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject questionObj = results.getJSONObject(i);
                        String questionText = Html.fromHtml(questionObj.getString("question")).toString();
                        String correctAnswer = Html.fromHtml(questionObj.getString("correct_answer")).toString();
                        difficulty = Html.fromHtml(questionObj.getString("difficulty")).toString();
                        JSONArray incorrectAnswers = questionObj.getJSONArray("incorrect_answers");

                        // Add correct answer to the list of incorrect answers to shuffle them together
                        List<String> options = new ArrayList<>();
                        options.add(correctAnswer);
                        for (int j = 0; j < incorrectAnswers.length(); j++) {
                            options.add(Html.fromHtml(incorrectAnswers.getString(j)).toString());
                        }

                        // Shuffle options to randomize their order
                        Collections.shuffle(options);

                        // Create Question object
                        Question question = new Question(questionText, options.get(0), options.get(1), options.get(2), options.get(3), correctAnswer);
                        questions.add(question);
                    }

                    // Add quiz to Firebase
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        db.collection("users").document(userId)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {

                                        // Create Quiz object
                                        Quiz quiz = new Quiz(categoryName, "Open Trivia Quiz", "Open Trivia", "eMail", questions);

                                        // Save quiz to Firebase
                                        db.collection("quizzes").document("Open Trivia Quiz").set(quiz)
                                                .addOnSuccessListener(aVoid -> {
                                                    // Start PlayQuizActivity after quiz is added
                                                    Intent intent = new Intent(ChooseDifActivity.this, PlayQuizActivity.class);
                                                    intent.putExtra("quizName", "Open Trivia Quiz");
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    startActivity(intent);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e("Firestore", "Error adding quiz", e);
                                                    Toast.makeText(ChooseDifActivity.this, "Failed to add quiz", Toast.LENGTH_SHORT).show();
                                                });

                                    } else {
                                        Log.d("Firestore", "User document does not exist");
                                        Toast.makeText(ChooseDifActivity.this, "Failed to retrieve user data. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error retrieving user document", e);
                                    Toast.makeText(ChooseDifActivity.this, "Failed to retrieve user data. Please try again.", Toast.LENGTH_SHORT).show();
                                });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("try", "22 ");

                }
            }
        });


    }

}