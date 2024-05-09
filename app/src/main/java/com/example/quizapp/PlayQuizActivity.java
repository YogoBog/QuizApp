package com.example.quizapp;

import static com.example.quizapp.ChooseDifActivity.difficulty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PlayQuizActivity extends AppCompatActivity {

    private TextView timerTextView, questNumTextView, currQuestTextView, scoreTextView, pointsTextView;
    private ImageView pointsImageView, speakerImageView;
    private Button opt1Btn, opt2Btn, opt3Btn, opt4Btn, endQuizButton;
    private LinearProgressIndicator timerProgressBar;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis; // Time in milliseconds
    private final long COUNTDOWN_INTERVAL = 1000; // Interval in milliseconds
    private List<Question> questions = new ArrayList<>();
    private List<Button> allOptionsList = new ArrayList<>();
    private String correctAnswer, quizName;
    private int currentQuestionIndex = 0, quizScore = 0;
    private Dialog dialog;
    private FirebaseAuth auth;
    private int userPoints, addTimeAmount, skipQuestionAmount, jokerAmount, halfHalfAmount, doublePointsAmount;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser;
    private TextToSpeech textToSpeech;
    private Animation fadeIn;
    private Animation fadeOut;
    private FloatingActionButton fab, jokerButton, skipButton, doubleButton, halfHalfButton, addTimeButton;
    private boolean isDoubleActive = false, isSkipActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        init();

//        TODO: improve scoring system, improve texttospeech logic, when can be pressed, not overlap, show coin amount in store ....

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        fadeIn = AnimationUtils.loadAnimation(PlayQuizActivity.this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(PlayQuizActivity.this, R.anim.fade_out);

        Intent intent = getIntent();

        quizName = intent.getStringExtra("quizName");

        db.collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userPoints = documentSnapshot.getLong("user Points").intValue();
                        doublePointsAmount = documentSnapshot.getLong("doublePointsAmount").intValue();
                        halfHalfAmount = documentSnapshot.getLong("halfHalfAmount").intValue();
                        jokerAmount = documentSnapshot.getLong("jokerAmount").intValue();
                        skipQuestionAmount = documentSnapshot.getLong("skipQuestionAmount").intValue();
                        addTimeAmount = documentSnapshot.getLong("addTimeAmount").intValue();
                    }
                });

        db.collection("quizzes").document(quizName).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Quiz quiz = documentSnapshot.toObject(Quiz.class);
                        if (quiz != null) {
                            questions = quiz.getQuestions();
                            setNextQuestion();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                });

        fab.setOnClickListener(view -> {

            if (skipButton.isShown()) {
                disablePowerUps();
            } else {
                skipButton.startAnimation(fadeIn);
                skipButton.setVisibility(View.VISIBLE);

                halfHalfButton.startAnimation(fadeIn);
                halfHalfButton.setVisibility(View.VISIBLE);

                addTimeButton.startAnimation(fadeIn);
                addTimeButton.setVisibility(View.VISIBLE);

                doubleButton.startAnimation(fadeIn);
                doubleButton.setVisibility(View.VISIBLE);

                jokerButton.startAnimation(fadeIn);
                jokerButton.setVisibility(View.VISIBLE);
            }

        });

        skipButton.setOnClickListener(view -> {

            if (skipQuestionAmount > 0) {
                DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());
                userRef.get().addOnSuccessListener(documentSnapshot1 -> {
                    userRef.update("skipQuestionAmount", skipQuestionAmount - 1);
                });

                isSkipActive = true;

                scoreTextView.setVisibility(View.INVISIBLE);
                pointsImageView.setVisibility(View.INVISIBLE);
                jokerButton.setEnabled(false);
                skipButton.setEnabled(false);
                halfHalfButton.setEnabled(false);
                addTimeButton.setEnabled(false);
                doubleButton.setEnabled(false);
                fab.setEnabled(false);
                disablePowerUps();


                countDownTimer.cancel(); // Cancel the existing timer
                timeLeftInMillis = 30000;

                new Handler().postDelayed(this::setNextQuestion, 500); //
            } else
                Toast.makeText(PlayQuizActivity.this, "You don't have this item!", Toast.LENGTH_SHORT).show();
        });

        addTimeButton.setOnClickListener(view -> {
            if (addTimeAmount > 0) {
                DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());
                userRef.get().addOnSuccessListener(documentSnapshot1 -> {
                    userRef.update("addTimeAmount", addTimeAmount - 1);
                });

                countDownTimer.cancel();
                timeLeftInMillis = 31000;
                startCountDown();
            } else
                Toast.makeText(PlayQuizActivity.this, "You don't have this item!", Toast.LENGTH_SHORT).show();
        });

        jokerButton.setOnClickListener(view -> {
            if (jokerAmount > 0) {
                DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());
                userRef.get().addOnSuccessListener(documentSnapshot1 -> {
                    userRef.update("jokerAmount", jokerAmount - 1);
                });

                for (Button button : allOptionsList) {
                    if (button.getText().toString().equals(correctAnswer)) {
                        button.setBackgroundColor(ContextCompat.getColor(PlayQuizActivity.this, R.color.green));
                        button.setTextColor(ContextCompat.getColor(PlayQuizActivity.this, R.color.white));

                        break; // Once the correct button is found, exit the loop
                    }
                }
                if (isDoubleActive) {
                    quizScore += 200;
                    userPoints += 200;
                    scoreTextView.setText("+ 200");
                } else {
                    quizScore += 100;
                    userPoints += 100;
                    scoreTextView.setText("+ 100");
                }

                if ((quizName.equals("Open Trivia Quiz"))) {
                    scoreTextView.setVisibility(View.VISIBLE);
                    pointsImageView.setVisibility(View.VISIBLE);
                    scoreTextView.startAnimation(fadeIn);
                    pointsImageView.startAnimation(fadeIn);
                }

                jokerButton.setEnabled(false);
                skipButton.setEnabled(false);
                halfHalfButton.setEnabled(false);
                addTimeButton.setEnabled(false);
                doubleButton.setEnabled(false);
                fab.setEnabled(false);
                for (Button button : allOptionsList) {
                    button.setEnabled(false);
                }
                disablePowerUps();


                countDownTimer.cancel(); // Cancel the existing timer

                // Delay the call to setNextQuestion() by 2 seconds
                new Handler().postDelayed(PlayQuizActivity.this::setNextQuestion, 2000); // 2000 milliseconds = 2 seconds
            } else
                Toast.makeText(PlayQuizActivity.this, "You don't have this item!", Toast.LENGTH_SHORT).show();
        });

        halfHalfButton.setOnClickListener(view -> {
            if (halfHalfAmount > 0) {
                DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());
                userRef.get().addOnSuccessListener(documentSnapshot1 -> {
                    userRef.update("halfHalfAmount", halfHalfAmount - 1);
                });
                List<Button> inCorrect = new ArrayList<>();
                for (Button button : allOptionsList) {
                    if (!(button.getText().toString().equals(correctAnswer))) {
                        inCorrect.add(button);
                    }
                }

                List<Button> throwAwayButtons = getRandomButtons(inCorrect, 2);

                for (Button btn : throwAwayButtons) {
                    // For example, change the color of the selected buttons
                    btn.startAnimation(fadeOut);
                    btn.setVisibility(View.INVISIBLE);

                }
            } else
                Toast.makeText(PlayQuizActivity.this, "You don't have this item!", Toast.LENGTH_SHORT).show();

            halfHalfButton.setEnabled(false);
        });


        doubleButton.setOnClickListener(view -> {
            if (doublePointsAmount > 0) {
                DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());
                userRef.get().addOnSuccessListener(documentSnapshot1 -> {
                    userRef.update("doublePointsAmount", doublePointsAmount - 1);
                });
                doubleButton.setEnabled(false);
                Toast.makeText(PlayQuizActivity.this, "Acquired", Toast.LENGTH_SHORT).show();
                isDoubleActive = true;
            } else
                Toast.makeText(PlayQuizActivity.this, "You don't have this item!", Toast.LENGTH_SHORT).show();
        });

    }


    private void init() {

        dialog = new Dialog(PlayQuizActivity.this);
        dialog.setContentView(R.layout.end_quiz_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.end_quiz_dialog_bg));
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        speakerImageView = findViewById(R.id.speakerImageView);
        timerTextView = findViewById(R.id.timerTextView);
        questNumTextView = findViewById(R.id.questNumTextView);
        currQuestTextView = findViewById(R.id.currQuestTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        pointsTextView = dialog.findViewById(R.id.pointsTextView);
        pointsImageView = findViewById(R.id.pointsImageView);
        opt1Btn = findViewById(R.id.option1button);
        opt2Btn = findViewById(R.id.option2button);
        opt3Btn = findViewById(R.id.option3button);
        opt4Btn = findViewById(R.id.option4button);
        jokerButton = findViewById(R.id.useJokerBtn);
        skipButton = findViewById(R.id.useSkipBtn);
        halfHalfButton = findViewById(R.id.useHalfBtn);
        addTimeButton = findViewById(R.id.useTimeBtn);
        doubleButton = findViewById(R.id.useDoubleBtn);
        fab = findViewById(R.id.usePowersFab);

        endQuizButton = dialog.findViewById(R.id.endQuizButton);
        timerProgressBar = findViewById(R.id.timerProgressBar);

        allOptionsList.add(opt1Btn);
        allOptionsList.add(opt2Btn);
        allOptionsList.add(opt3Btn);
        allOptionsList.add(opt4Btn);


        textToSpeech = new TextToSpeech(PlayQuizActivity.this, i -> {
            if (i != TextToSpeech.ERROR)
                textToSpeech.setLanguage(Locale.ENGLISH);
        });

        setClickListeners();

        // Set the initial time in milliseconds (30 seconds)
        timeLeftInMillis = 30000;
    }

    private void setNextQuestion() {
        // Check if there are more questions
        if (currentQuestionIndex < questions.size()) {

            timeLeftInMillis = 30000;
            updateTimerUI();

            // Get the current question
            Question currentQuestion = questions.get(currentQuestionIndex);

            opt1Btn.setVisibility(View.INVISIBLE);
            opt2Btn.setVisibility(View.INVISIBLE);
            opt3Btn.setVisibility(View.INVISIBLE);
            opt4Btn.setVisibility(View.INVISIBLE);

            isDoubleActive = false;

            speakerImageView.setOnClickListener(view -> {
                String text = currentQuestion.getQuestion();
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                Log.d("speak", text);
            });


            opt1Btn.setVisibility(View.VISIBLE);
            opt1Btn.startAnimation(fadeIn);
            opt1Btn.setText(currentQuestion.getOptionA());
            opt1Btn.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            opt1Btn.setTextColor(ContextCompat.getColor(this, R.color.blue2));


            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                opt2Btn.setVisibility(View.VISIBLE);
                opt2Btn.startAnimation(fadeIn);
                opt2Btn.setText(currentQuestion.getOptionB());
                opt2Btn.setBackgroundColor(ContextCompat.getColor(PlayQuizActivity.this, R.color.white));
                opt2Btn.setTextColor(ContextCompat.getColor(PlayQuizActivity.this, R.color.blue2));
            }, 500);

            handler.postDelayed(() -> {
                opt3Btn.setVisibility(View.VISIBLE);
                opt3Btn.startAnimation(fadeIn);
                opt3Btn.setText(currentQuestion.getOptionC());
                opt3Btn.setBackgroundColor(ContextCompat.getColor(PlayQuizActivity.this, R.color.white));
                opt3Btn.setTextColor(ContextCompat.getColor(PlayQuizActivity.this, R.color.blue2));
            }, 1000);

            handler.postDelayed(() -> {
                opt4Btn.setVisibility(View.VISIBLE);
                opt4Btn.startAnimation(fadeIn);
                opt4Btn.setText(currentQuestion.getOptionD());
                opt4Btn.setBackgroundColor(ContextCompat.getColor(PlayQuizActivity.this, R.color.white));
                opt4Btn.setTextColor(ContextCompat.getColor(PlayQuizActivity.this, R.color.blue2));

                startCountDown();

                for (Button button : allOptionsList) {
                    button.setEnabled(true);
                }

                jokerButton.setEnabled(true);
                skipButton.setEnabled(true);
                halfHalfButton.setEnabled(true);
                addTimeButton.setEnabled(true);
                doubleButton.setEnabled(true);
                fab.setEnabled(true);

            }, 1500);


            correctAnswer = currentQuestion.getCorrectAnswer();


            if (currentQuestionIndex != 0 && !isSkipActive && quizName.equals("Open Trivia Quiz")) {
                scoreTextView.startAnimation(fadeOut);
                pointsImageView.startAnimation(fadeOut);
            }

            isSkipActive = false;

            scoreTextView.setVisibility(View.INVISIBLE);
            pointsImageView.setVisibility(View.INVISIBLE);

            currQuestTextView.setText(currentQuestion.getQuestion());
            questNumTextView.setText(String.valueOf(currentQuestionIndex + 1)); // value starts at 0

            // Increment the question index for the next question
            currentQuestionIndex++;
        } else {
            // Quiz finished

            DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());
            userRef.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            userRef.update("user Points", userPoints);
                        }
                    });

            countDownTimer.cancel();
            updateTimerUI();

            pointsTextView.setText("You Got " + quizScore);

            dialog.show();

            endQuizButton.setOnClickListener(view -> {
                Intent intent = new Intent(PlayQuizActivity.this, MainActivity.class);
                startActivity(intent);
            });
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerUI();
            }

            @Override
            public void onFinish() {
                // Handle timer finish event

                quizScore -= 100;
                userPoints -= 100;

                scoreTextView.setText("- 100");

                if ((quizName.equals("Open Trivia Quiz"))) {
                    scoreTextView.startAnimation(fadeIn);
                    pointsImageView.startAnimation(fadeIn);
                    scoreTextView.setVisibility(View.VISIBLE);
                    pointsImageView.setVisibility(View.VISIBLE);
                }


                for (Button button : allOptionsList) {
                    if (button.getText().toString().equals(correctAnswer)) {
                        button.setBackgroundColor(ContextCompat.getColor(PlayQuizActivity.this, R.color.green));
                        button.setTextColor(ContextCompat.getColor(PlayQuizActivity.this, R.color.white));

                        break; // Once the correct button is found, exit the loop
                    }
                }

                countDownTimer.cancel(); // Cancel the existing timer

                // Delay the call to setNextQuestion() by 2 seconds
                new Handler().postDelayed(PlayQuizActivity.this::setNextQuestion, 2000); // 2000 milliseconds = 2 seconds

            }
        }.start();
    }

    private void updateTimerUI() {
        // Convert timeLeftInMillis to seconds
        int secondsLeft = (int) (timeLeftInMillis / 1000);

        // Update the TextView
        timerTextView.setText(String.valueOf(secondsLeft));

        // Calculate progress percentage
        int progress = (int) ((timeLeftInMillis / 30000.0) * 100); // 30000 milliseconds is the initial time

        // Update the progress indicator
        timerProgressBar.setProgressCompat(progress, true);
    }


    public void checkAnswer(View v) {
        Button b = (Button) v;

        int timeTaken = 30 - Integer.parseInt(timerTextView.getText().toString());

        if (b.getText().toString().equals(correctAnswer)) {
            Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            b.startAnimation(scale);
            b.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            b.setTextColor(ContextCompat.getColor(this, R.color.white));


            quizScore += calculateScore(timeTaken);
            userPoints += calculateScore(timeTaken);
            scoreTextView.setText("+ " + calculateScore(timeTaken));

        } else {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            b.startAnimation(shake);
            b.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            b.setTextColor(ContextCompat.getColor(this, R.color.white));


            quizScore -= calculateScore(timeTaken);
            userPoints -= calculateScore(timeTaken);
            scoreTextView.setText("- " + calculateScore(timeTaken));

            for (Button button : allOptionsList) {
                if (button.getText().toString().equals(correctAnswer)) {
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
                    button.setTextColor(ContextCompat.getColor(this, R.color.white));

                    break; // Once the correct button is found, exit the loop
                }
            }
        }

        for (Button button : allOptionsList) {
            button.setEnabled(false);
        }

        jokerButton.setEnabled(false);
        skipButton.setEnabled(false);
        halfHalfButton.setEnabled(false);
        addTimeButton.setEnabled(false);
        doubleButton.setEnabled(false);
        fab.setEnabled(false);
        if (skipButton.isShown())
            disablePowerUps();


        if ((quizName.equals("Open Trivia Quiz"))) {
            scoreTextView.setVisibility(View.VISIBLE);
            pointsImageView.setVisibility(View.VISIBLE);
            scoreTextView.startAnimation(fadeIn);
            pointsImageView.startAnimation(fadeIn);
        }

        countDownTimer.cancel(); // Cancel the existing timer
        timeLeftInMillis = 30000;

        // Delay the call to setNextQuestion() by 2 seconds
        new Handler().postDelayed(this::setNextQuestion, 2000); // 2000 milliseconds = 2 seconds

    }


    public static List<Button> getRandomButtons(List<Button> buttonList, int numButtons) {
        List<Button> selectedButtons = new ArrayList<>();
        Random random = new Random();

        // Ensure the List has enough buttons
        if (buttonList.size() < numButtons) {
            throw new IllegalArgumentException("Not enough buttons in the list" + buttonList.size());
        }

        // Generate random indices and get corresponding buttons
        for (int i = 0; i < numButtons; i++) {
            int randomIndex = random.nextInt(buttonList.size());
            selectedButtons.add(buttonList.get(randomIndex));
            // Remove the selected button to avoid duplicates
            buttonList.remove(randomIndex);
        }

        return selectedButtons;
    }

    private void setClickListeners() {
        opt1Btn.setOnClickListener(this::checkAnswer);
        opt2Btn.setOnClickListener(this::checkAnswer);
        opt3Btn.setOnClickListener(this::checkAnswer);
        opt4Btn.setOnClickListener(this::checkAnswer);
    }

    private void disablePowerUps() {
        skipButton.startAnimation(fadeOut);
        skipButton.setVisibility(View.INVISIBLE);

        halfHalfButton.startAnimation(fadeOut);
        halfHalfButton.setVisibility(View.INVISIBLE);

        addTimeButton.startAnimation(fadeOut);
        addTimeButton.setVisibility(View.INVISIBLE);

        doubleButton.startAnimation(fadeOut);
        doubleButton.setVisibility(View.INVISIBLE);

        jokerButton.startAnimation(fadeOut);
        jokerButton.setVisibility(View.INVISIBLE);
    }

    private int calculateScore(int timeTaken) {
        int score = 1000 / timeTaken;
        if (difficulty != null) {
            if (difficulty.equals("easy"))
                score *= 0.5;
            else if (difficulty.equals("medium")) {
                score *= 1;
            } else if (difficulty.equals("hard")) {
                score *= 1.5;
            }
        }
        if (!(quizName.equals("Open Trivia Quiz"))) {
            return 0;
        }

        if (isDoubleActive)
            score *= 2;

        return score;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}