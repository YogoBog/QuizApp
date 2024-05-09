package com.example.quizapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private TextView usernameTextView, userPointsTextView, playAPITextView, seeAllTextView;
    private CircleImageView userImageView;
    private ImageView createImageView, viewAllImageView, playImageView, sportsHomeImageView, historyHomeImageView, enterHomeImageView, scienceHomeImageView;
    private FirebaseAuth auth;
    private Uri uriImage;
    private ProgressBar homeProgressBar;
    private StorageReference storageReference;
    private LinearLayout sports, history, science, enter, play, viewQuizzes, create;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        findViews(view);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
        } else {
            homeProgressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        Log.d("banana","ine");


        storageReference = FirebaseStorage.getInstance().getReference("Pics");

        userImageView.setOnClickListener(v -> choosePic());

        createImageView.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChooseQuizNameActivity.class);
            startActivity(intent);
        });

        seeAllTextView.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChooseCategoryApiActivity.class);
            intent.putExtra("cameFromHome", "yes");
            startActivity(intent);
        });

        viewAllImageView.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ViewYourQuizzesActivity.class);
            startActivity(intent);
        });

        playImageView.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ViewAllQuizzesActivity.class);
            startActivity(intent);
        });

        create.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChooseQuizNameActivity.class);
            startActivity(intent);
        });

        viewQuizzes.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ViewYourQuizzesActivity.class);
            startActivity(intent);
        });

        play.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ViewAllQuizzesActivity.class);
            startActivity(intent);
        });

        sportsHomeImageView.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), ViewQuizzesByCategoryActivity.class);
            intent.putExtra("categoryName", "Sports");
            startActivity(intent);
        });

        sports.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), ViewQuizzesByCategoryActivity.class);
            intent.putExtra("categoryName", "Sports");
            startActivity(intent);
        });

        historyHomeImageView.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), ViewQuizzesByCategoryActivity.class);
            intent.putExtra("categoryName", "History");
            startActivity(intent);
        });

        history.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), ViewQuizzesByCategoryActivity.class);
            intent.putExtra("categoryName", "History");
            startActivity(intent);
        });

        enterHomeImageView.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), ViewQuizzesByCategoryActivity.class);
            intent.putExtra("categoryName", "Movies");
            startActivity(intent);
        });

        enter.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), ViewQuizzesByCategoryActivity.class);
            intent.putExtra("categoryName", "Movies");
            startActivity(intent);
        });

        scienceHomeImageView.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), ViewQuizzesByCategoryActivity.class);
            intent.putExtra("categoryName", "Science");
            startActivity(intent);
        });

        science.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), ViewQuizzesByCategoryActivity.class);
            intent.putExtra("categoryName", "Science");
            startActivity(intent);
        });

        playAPITextView.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChooseCategoryApiActivity.class);
            startActivity(intent);

        });

        return view;
    }

    private void findViews(View view) {
        seeAllTextView = view.findViewById(R.id.seeAllTextView);
        userPointsTextView = view.findViewById(R.id.userPointsTextView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        userImageView = view.findViewById(R.id.userImageView);
        playImageView = view.findViewById(R.id.playImageView);
        viewAllImageView = view.findViewById(R.id.viewAllImageView);
        homeProgressBar = view.findViewById(R.id.homeProgressBar);
        createImageView = view.findViewById(R.id.createImageView);
        sports = view.findViewById(R.id.homeSportsLinearLayout);
        enter = view.findViewById(R.id.homeEnterLinearLayout);
        history = view.findViewById(R.id.homeHistoryLinearLayout);
        science = view.findViewById(R.id.homeScienceLinearLayout);
        play = view.findViewById(R.id.playLinearLayout);
        create = view.findViewById(R.id.createLinearLayout);
        viewQuizzes = view.findViewById(R.id.yourQuizzesLinearLayout);
        sportsHomeImageView = view.findViewById(R.id.sportsHomeImageView);
        scienceHomeImageView = view.findViewById(R.id.scienceHomeImageView);
        enterHomeImageView = view.findViewById(R.id.entHomeImageView);
        historyHomeImageView = view.findViewById(R.id.historyHomeImageView);
        playAPITextView = view.findViewById(R.id.playAPIQuizzesTextView);
    }

    private void choosePic() {
        Intent intent = new Intent();
        intent.setType("image/*"); // any type of image
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1); // true
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == requireActivity().RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            userImageView.setImageURI(uriImage);
            uploadPic();
        }
    }

    private void uploadPic() {
        final ProgressDialog pd = new ProgressDialog(requireContext());
        pd.setTitle("Uploading image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference reference = storageReference.child("Pics/" + randomKey);
        reference.putFile(uriImage)
                .addOnSuccessListener(taskSnapshot -> {
                    pd.dismiss();
                    // Get the download URL of the uploaded image
                    reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save the download URL in Firestore
                        saveImageUriToFirestore(uri.toString());
                    });
                    Toast.makeText(requireContext(), "Image Uploaded!", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                })
                .addOnProgressListener(snapshot -> {
                    double percent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Progress: " + (int) percent + "%");
                });
    }

    private void saveImageUriToFirestore(String imageUrl) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(currentUser.getUid())
                    .update("profilePictureUrl", imageUrl)
                    .addOnSuccessListener(aVoid -> {
                        // Profile picture URL saved successfully
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed to save profile picture URL", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (isAdded()) {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");
                            int points = documentSnapshot.getLong("user Points").intValue();
                            if (username != null) {
                                usernameTextView.setText("Hi, " + username);
                                userPointsTextView.setText(String.valueOf(points));
                                // Load and set profile picture from the saved URL
                                String profilePictureUrl = documentSnapshot.getString("profilePictureUrl");
                                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                                    Picasso.with(requireContext())
                                            .load(profilePictureUrl)
                                            .into(userImageView, new com.squareup.picasso.Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    // Image loaded successfully
                                                    homeProgressBar.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onError() {
                                                    // Error occurred while loading image
                                                    homeProgressBar.setVisibility(View.GONE);
                                                }
                                            });
                                } else {
                                    // If no profile picture URL is available
                                    homeProgressBar.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (isAdded()) { // Check if the fragment is attached to the activity
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        homeProgressBar.setVisibility(View.GONE);
                    }
                });
    }

}
