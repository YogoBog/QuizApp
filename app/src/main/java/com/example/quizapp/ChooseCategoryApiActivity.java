package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ChooseCategoryApiActivity extends AppCompatActivity {

    private ImageView sportsImg, historyImg, scienceImg, moviesImg, tvImg, booksImg, musicImg, gamesImg, geoImg,
            artImg, celebsImg, vehiclesImg, backImageView;
    private LinearLayout sports, history, science, movies, tv, books, music, games, geo, art, celebs, vehicles;
    private String quiz, cameFromHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category_api);

        findViews();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        Intent intent = getIntent();
        quiz = intent.getStringExtra("quizName");
        cameFromHome = intent.getStringExtra("cameFromHome");


        if (quiz != null) {

            backImageView.setOnClickListener(v -> {
                Intent intent1 = new Intent(ChooseCategoryApiActivity.this, ChooseQuizNameActivity.class);
                startActivity(intent1);
            });

            sports.setOnClickListener(v -> startAddActivity("Sports"));
            sportsImg.setOnClickListener(v -> startAddActivity("Sports"));

            history.setOnClickListener(v -> startAddActivity("History"));
            historyImg.setOnClickListener(v -> startAddActivity("History"));

            science.setOnClickListener(v -> startAddActivity("Science"));
            scienceImg.setOnClickListener(v -> startAddActivity("Science"));

            movies.setOnClickListener(v -> startAddActivity("Movies"));
            moviesImg.setOnClickListener(v -> startAddActivity("Movies"));

            tv.setOnClickListener(v -> startAddActivity("TV - Shows"));
            tvImg.setOnClickListener(v -> startAddActivity("TV - Shows"));

            books.setOnClickListener(v -> startAddActivity("Books"));
            booksImg.setOnClickListener(v -> startAddActivity("Books"));

            music.setOnClickListener(v -> startAddActivity("Music"));
            musicImg.setOnClickListener(v -> startAddActivity("Music"));

            games.setOnClickListener(v -> startAddActivity("Video Games"));
            gamesImg.setOnClickListener(v -> startAddActivity("Video Games"));

            geo.setOnClickListener(v -> startAddActivity("Geography"));
            geoImg.setOnClickListener(v -> startAddActivity("Geography"));

            art.setOnClickListener(v -> startAddActivity("Art"));
            artImg.setOnClickListener(v -> startAddActivity("Art"));

            celebs.setOnClickListener(v -> startAddActivity("Celebrities"));
            celebsImg.setOnClickListener(v -> startAddActivity("Celebrities"));

            vehicles.setOnClickListener(v -> startAddActivity("Vehicles"));
            vehiclesImg.setOnClickListener(v -> startAddActivity("Vehicles"));

        }
        if (cameFromHome != null) {

            backImageView.setOnClickListener(v -> {
                Intent intent1 = new Intent(ChooseCategoryApiActivity.this, MainActivity.class);
                startActivity(intent1);
            });

            sports.setOnClickListener(v -> startViewQuizzesActivity("Sports"));
            sportsImg.setOnClickListener(v -> startViewQuizzesActivity("Sports"));

            history.setOnClickListener(v -> startViewQuizzesActivity("History"));
            historyImg.setOnClickListener(v -> startViewQuizzesActivity("History"));

            science.setOnClickListener(v -> startViewQuizzesActivity("Science"));
            scienceImg.setOnClickListener(v -> startViewQuizzesActivity("Science"));

            movies.setOnClickListener(v -> startViewQuizzesActivity("Movies"));
            moviesImg.setOnClickListener(v -> startViewQuizzesActivity("Movies"));

            tv.setOnClickListener(v -> startViewQuizzesActivity("TV - Shows"));
            tvImg.setOnClickListener(v -> startViewQuizzesActivity("TV - Shows"));

            books.setOnClickListener(v -> startViewQuizzesActivity("Books"));
            booksImg.setOnClickListener(v -> startViewQuizzesActivity("Books"));

            music.setOnClickListener(v -> startViewQuizzesActivity("Music"));
            musicImg.setOnClickListener(v -> startViewQuizzesActivity("Music"));

            games.setOnClickListener(v -> startViewQuizzesActivity("Video Games"));
            gamesImg.setOnClickListener(v -> startViewQuizzesActivity("Video Games"));

            geo.setOnClickListener(v -> startViewQuizzesActivity("Geography"));
            geoImg.setOnClickListener(v -> startViewQuizzesActivity("Geography"));

            art.setOnClickListener(v -> startViewQuizzesActivity("Art"));
            artImg.setOnClickListener(v -> startViewQuizzesActivity("Art"));

            celebs.setOnClickListener(v -> startViewQuizzesActivity("Celebrities"));
            celebsImg.setOnClickListener(v -> startViewQuizzesActivity("Celebrities"));

            vehicles.setOnClickListener(v -> startViewQuizzesActivity("Vehicles"));
            vehiclesImg.setOnClickListener(v -> startViewQuizzesActivity("Vehicles"));

        }
        if (quiz == null && cameFromHome == null) {

            backImageView.setOnClickListener(v -> {
                Intent intent1 = new Intent(ChooseCategoryApiActivity.this, MainActivity.class);
                startActivity(intent1);
            });

            sports.setOnClickListener(v -> startChooseDifActivity("Sports"));
            sportsImg.setOnClickListener(v -> startChooseDifActivity("Sports"));

            history.setOnClickListener(v -> startChooseDifActivity("History"));
            historyImg.setOnClickListener(v -> startChooseDifActivity("History"));

            science.setOnClickListener(v -> startChooseDifActivity("Science"));
            scienceImg.setOnClickListener(v -> startChooseDifActivity("Science"));

            movies.setOnClickListener(v -> startChooseDifActivity("Movies"));
            moviesImg.setOnClickListener(v -> startChooseDifActivity("Movies"));

            tv.setOnClickListener(v -> startChooseDifActivity("TV - Shows"));
            tvImg.setOnClickListener(v -> startChooseDifActivity("TV - Shows"));

            books.setOnClickListener(v -> startChooseDifActivity("Books"));
            booksImg.setOnClickListener(v -> startChooseDifActivity("Books"));

            music.setOnClickListener(v -> startChooseDifActivity("Music"));
            musicImg.setOnClickListener(v -> startChooseDifActivity("Music"));

            games.setOnClickListener(v -> startChooseDifActivity("Video Games"));
            gamesImg.setOnClickListener(v -> startChooseDifActivity("Video Games"));

            geo.setOnClickListener(v -> startChooseDifActivity("Geography"));
            geoImg.setOnClickListener(v -> startChooseDifActivity("Geography"));

            art.setOnClickListener(v -> startChooseDifActivity("Art"));
            artImg.setOnClickListener(v -> startChooseDifActivity("Art"));

            celebs.setOnClickListener(v -> startChooseDifActivity("Celebrities"));
            celebsImg.setOnClickListener(v -> startChooseDifActivity("Celebrities"));

            vehicles.setOnClickListener(v -> startChooseDifActivity("Vehicles"));
            vehiclesImg.setOnClickListener(v -> startChooseDifActivity("Vehicles"));
        }


    }


    private void startChooseDifActivity(String categoryName) {
        Intent intent = new Intent(ChooseCategoryApiActivity.this, ChooseDifActivity.class);
        intent.putExtra("categoryName", categoryName);
        startActivity(intent);
    }

    private void startViewQuizzesActivity(String categoryName) {
        Intent intent = new Intent(ChooseCategoryApiActivity.this, ViewQuizzesByCategoryActivity.class);
        intent.putExtra("categoryName", categoryName);
        startActivity(intent);
    }

    private void startAddActivity(String categoryName) {
        Intent intent = new Intent(ChooseCategoryApiActivity.this, AddQuestionActivity.class);
        intent.putExtra("categoryName", categoryName);
        intent.putExtra("quizName", quiz);
        startActivity(intent);
    }

    private void findViews() {
        sportsImg = findViewById(R.id.sportsApiImageView);
        historyImg = findViewById(R.id.historyApiImageView);
        scienceImg = findViewById(R.id.scienceApiImageView);
        moviesImg = findViewById(R.id.moviesApiImageView);
        tvImg = findViewById(R.id.tvApiImageView);
        booksImg = findViewById(R.id.booksApiImageView);
        musicImg = findViewById(R.id.musicApiImageView);
        gamesImg = findViewById(R.id.gamesApiImageView);
        geoImg = findViewById(R.id.geographyApiImageView);
        artImg = findViewById(R.id.artImageView);
        celebsImg = findViewById(R.id.celebsApiImageView);
        vehiclesImg = findViewById(R.id.vehiclesApiImageView);

        sports = findViewById(R.id.sportsApiLinearLayout);
        history = findViewById(R.id.historyApiLinearLayout);
        science = findViewById(R.id.scienceApiLinearLayout);
        movies = findViewById(R.id.moviesApiLinearLayout);
        tv = findViewById(R.id.tvApiLinearLayout);
        books = findViewById(R.id.booksApiLinearLayout);
        music = findViewById(R.id.musicApiLinearLayout);
        games = findViewById(R.id.gamesApiLinearLayout);
        geo = findViewById(R.id.geographyApiLinearLayout);
        art = findViewById(R.id.artApiLinearLayout);
        celebs = findViewById(R.id.celebsApiLinearLayout);
        vehicles = findViewById(R.id.vehiclesApiLinearLayout);

        backImageView = findViewById(R.id.chooseCatBackImg);
    }

}