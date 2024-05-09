package com.example.quizapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class StoreFragment extends Fragment {

    private Button addTimeButton, skipQuestionButton, jokerButton, halfButton, doublePointsButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private int points;
    private TextView userScore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        findViews(view);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        showUserProfile(firebaseUser);

        addTimeButton.setOnClickListener(view1 -> db.collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (isAdded()) {
                        if (documentSnapshot.exists()) {
                            int addTimeValue = documentSnapshot.getLong("addTimeAmount").intValue();
                            points = documentSnapshot.getLong("user Points").intValue();
                            if (points >= 250) {
                                DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());
                                userRef.get().addOnSuccessListener(documentSnapshot1 -> {
                                    userRef.update("addTimeAmount", addTimeValue + 1);
                                    userRef.update("user Points", points - 250);

                                    animatePointsChange(250);

                                    Toast.makeText(requireContext(), "Purchased!", Toast.LENGTH_SHORT).show();

                                });

                            } else {
                                Toast.makeText(requireContext(), "Not Enough Points!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }));


        skipQuestionButton.setOnClickListener(view1 -> db.collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (isAdded()) {
                        if (documentSnapshot.exists()) {
                            int skipQuestionAmount = documentSnapshot.getLong("skipQuestionAmount").intValue();
                            points = documentSnapshot.getLong("user Points").intValue();
                            if (points >= 300) {
                                DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());
                                userRef.get().addOnSuccessListener(documentSnapshot1 -> {
                                    userRef.update("skipQuestionAmount", skipQuestionAmount + 1);
                                    userRef.update("user Points", points - 300);

                                    animatePointsChange(300);


                                    Toast.makeText(requireContext(), "Purchased!", Toast.LENGTH_SHORT).show();

                                });

                            } else {
                                Toast.makeText(requireContext(), "Not Enough Points!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }));

        jokerButton.setOnClickListener(view1 -> db.collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (isAdded()) {
                        if (documentSnapshot.exists()) {
                            int jokerAmount = documentSnapshot.getLong("jokerAmount").intValue();
                            points = documentSnapshot.getLong("user Points").intValue();
                            if (points >= 500) {
                                DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());
                                userRef.get().addOnSuccessListener(documentSnapshot1 -> {
                                    userRef.update("jokerAmount", jokerAmount + 1);
                                    userRef.update("user Points", points - 500);

                                    animatePointsChange(500);

                                    Toast.makeText(requireContext(), "Purchased!", Toast.LENGTH_SHORT).show();

                                });

                            } else {
                                Toast.makeText(requireContext(), "Not Enough Points!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }));

        halfButton.setOnClickListener(view1 -> db.collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (isAdded()) {
                        if (documentSnapshot.exists()) {
                            int halfHalfAmount = documentSnapshot.getLong("halfHalfAmount").intValue();
                            points = documentSnapshot.getLong("user Points").intValue();
                            if (points >= 275) {
                                DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());
                                userRef.get().addOnSuccessListener(documentSnapshot1 -> {
                                    userRef.update("halfHalfAmount", halfHalfAmount + 1);
                                    userRef.update("user Points", points - 275);

                                    animatePointsChange(275);

                                    Toast.makeText(requireContext(), "Purchased!", Toast.LENGTH_SHORT).show();

                                });

                            } else {
                                Toast.makeText(requireContext(), "Not Enough Points!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }));

        doublePointsButton.setOnClickListener(view1 -> db.collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (isAdded()) {
                        if (documentSnapshot.exists()) {
                            int doublePointsAmount = documentSnapshot.getLong("doublePointsAmount").intValue();
                            points = documentSnapshot.getLong("user Points").intValue();
                            if (points >= 350) {
                                DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());
                                userRef.get().addOnSuccessListener(documentSnapshot1 -> {
                                    userRef.update("doublePointsAmount", doublePointsAmount + 1);
                                    userRef.update("user Points", points - 350);

                                    animatePointsChange(350);

                                    Toast.makeText(requireContext(), "Purchased!", Toast.LENGTH_SHORT).show();

                                });

                            } else {
                                Toast.makeText(requireContext(), "Not Enough Points!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }));

        return view;
    }

    private void findViews(View view) {
        addTimeButton = view.findViewById(R.id.addTimeButton);
        skipQuestionButton = view.findViewById(R.id.skipButton);
        jokerButton = view.findViewById(R.id.jokerButton);
        halfButton = view.findViewById(R.id.halfHalfButton);
        doublePointsButton = view.findViewById(R.id.doublePointsButton);
        userScore = view.findViewById(R.id.userPointsStoreTextView);
    }


    private void showUserProfile(FirebaseUser firebaseUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (isAdded()) {
                        if (documentSnapshot.exists()) {
                            int points = documentSnapshot.getLong("user Points").intValue();
                            if (documentSnapshot.getString("username") != null) {
                                userScore.setText(String.valueOf(points));
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (isAdded()) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                        homeProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void updatePointsDisplay(int points) {
        userScore.setText(String.valueOf(points));
    }

    private void animatePointsChange(int pointsDifference) {
        Handler handler = new Handler();
        int animationDuration = 500; // milliseconds
        int animationInterval = animationDuration / pointsDifference;
        Runnable runnable = new Runnable() {
            int currentPointsCount = points;

            @Override
            public void run() {
                if (currentPointsCount > points - 250) {
                    currentPointsCount--;
                    updatePointsDisplay(currentPointsCount);
                    handler.postDelayed(this, animationInterval);
                } else {
                    updatePointsDisplay(points - 250);
                }
            }
        };
        handler.postDelayed(runnable, animationInterval);
    }

}