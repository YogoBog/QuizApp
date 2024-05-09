package com.example.quizapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class LeaderboardFragment extends Fragment {

    private ImageView numberOneImg, numberTwoImg, numberThreeImg;
    private TextView topOneTxt, topOneScoreTxt, topTwoTxt, topTwoScoreTxt, topThreeTxt, topThreeScoreTxt;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private List<User> userList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        init(view);

        db = FirebaseFirestore.getInstance();

        fetchUsers();

        return view;
    }

    private void init(View view) {
        
        numberOneImg = view.findViewById(R.id.number1ImageView);
        numberTwoImg = view.findViewById(R.id.number2ImageView);
        numberThreeImg = view.findViewById(R.id.number3ImageView);

        topOneTxt = view.findViewById(R.id.top1TextView);
        topTwoTxt = view.findViewById(R.id.top2TextView);
        topThreeTxt = view.findViewById(R.id.top3TextView);

        topOneScoreTxt = view.findViewById(R.id.top1scoreTextView);
        topTwoScoreTxt = view.findViewById(R.id.top2scoreTextView);
        topThreeScoreTxt = view.findViewById(R.id.top3scoreTextView);

        progressBar = view.findViewById(R.id.progressBar2);
        recyclerView = view.findViewById(R.id.leaderRecyclerView);

        userList = new ArrayList<>();

        adapter = new LeaderboardAdapter(getContext(), userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }

    private void fetchUsers() {
        db.collection("users")
                .orderBy("user Points", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            String username = document.getString("username");
                            String profilePictureUrl = document.getString("profilePictureUrl");
                            long userPointsLong = document.getLong("user Points");
                            int userPoints = (int) userPointsLong;
                            User user = new User(username, profilePictureUrl, userPoints);
                            userList.add(user);
                        }
                        displayUsers(userList);
                    } else {
                        Log.d("LeaderboardFragment", "Error getting documents: ", task.getException());
                    }
                });
    }


    private void displayUsers(List<User> topUsers) {
        if (isAdded() && topUsers.size() >= 3) {
            // Display data for first user
            com.example.quizapp.User user1 = topUsers.get(0);
            String username1 = user1.getUsername();
            String profileImageUrl1 = user1.getProfilePictureUrl();
            int score1 = user1.getUserPoints();

            Glide.with(requireContext()).load(profileImageUrl1).into(numberOneImg);
            topOneTxt.setText(username1);
            topOneScoreTxt.setText(String.valueOf(score1));

            // Display data for second user
            com.example.quizapp.User user2 = topUsers.get(1);
            String username2 = user2.getUsername();
            String profileImageUrl2 = user2.getProfilePictureUrl();
            int score2 = user2.getUserPoints();

            Glide.with(requireContext()).load(profileImageUrl2).into(numberTwoImg);
            topTwoTxt.setText(username2);
            topTwoScoreTxt.setText(String.valueOf(score2));

            // Display data for third user
            com.example.quizapp.User user3 = topUsers.get(2);
            String username3 = user3.getUsername();
            String profileImageUrl3 = user3.getProfilePictureUrl();
            int score3 = user3.getUserPoints();

            Glide.with(requireContext()).load(profileImageUrl3).into(numberThreeImg);
            topThreeTxt.setText(username3);
            topThreeScoreTxt.setText(String.valueOf(score3));

            // Remove top 3 users from the list
            topUsers.remove(0);
            topUsers.remove(0);
            topUsers.remove(0);

            // Now topUsers contains users from the fourth position onwards
            adapter.setUserList(topUsers);
            progressBar.setVisibility(View.INVISIBLE);

        }
    }

}