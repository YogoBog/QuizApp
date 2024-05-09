package com.example.quizapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {

    private Button logout, changeUsername, changePassword, viewPowerUps, help, cancelPass, confirmPass, confirmName, cancelName, confirmLogout, cancelLogout;
    private Dialog changePassDialog, changeNameDialog, logoutDialog, powerUpsDialog;
    private EditText oldPass, newPass, newName;
    private FirebaseAuth auth;
    private int timeAmount, jokerAmount, halfHalfAmount, skipQuestionAmount, doublePointsAmount;
    private TextView timeTxt, jokerTxt, halfTxt, skipTxt, doubleTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        findViews(view);

        auth = FirebaseAuth.getInstance();

        logout.setOnClickListener(v -> logout());

        changePassword.setOnClickListener(v -> showPasswordDialog());

        changeUsername.setOnClickListener(v -> showUsernameDialog());

        viewPowerUps.setOnClickListener(v -> {
            getPowersAmount();
//            showPowersDialog();
        });

        return view;
    }

    private void findViews(View view) {
        logout = view.findViewById(R.id.logoutButton);
        changePassword = view.findViewById(R.id.changePasswordButton);
        changeUsername = view.findViewById(R.id.changeUsernameButton);
        viewPowerUps = view.findViewById(R.id.viewPowerUpsButton);
        help = view.findViewById(R.id.helpButton);

        changePassDialog = new Dialog(requireContext());
        changePassDialog.setContentView(R.layout.switch_password_dialog);
        changePassDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.end_quiz_dialog_bg));
        changePassDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        changePassDialog.setCancelable(false);
        changePassDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        oldPass = changePassDialog.findViewById(R.id.oldPassEditText);
        newPass = changePassDialog.findViewById(R.id.newPassEditText);
        cancelPass = changePassDialog.findViewById(R.id.cancelPassButton);
        confirmPass = changePassDialog.findViewById(R.id.confirmPassButton);

        changeNameDialog = new Dialog(requireContext());
        changeNameDialog.setContentView(R.layout.switch_username_dialog);
        changeNameDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.end_quiz_dialog_bg));
        changeNameDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        changeNameDialog.setCancelable(false);
        changeNameDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        newName = changeNameDialog.findViewById(R.id.newNameEditText);
        cancelName = changeNameDialog.findViewById(R.id.cancelNameButton);
        confirmName = changeNameDialog.findViewById(R.id.confirmNameButton);

        logoutDialog = new Dialog(requireContext());
        logoutDialog.setContentView(R.layout.confirm_logout_dialog);
        logoutDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.end_quiz_dialog_bg));
        logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        logoutDialog.setCancelable(false);
        logoutDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        cancelLogout = logoutDialog.findViewById(R.id.cancelLogoutButton);
        confirmLogout = logoutDialog.findViewById(R.id.confirmLogoutButton);

        powerUpsDialog = new Dialog(requireContext());
        powerUpsDialog.setContentView(R.layout.power_ups_dialog);
        powerUpsDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.end_quiz_dialog_bg));
        powerUpsDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        powerUpsDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        jokerTxt = powerUpsDialog.findViewById(R.id.jokerDialogTextView);
        timeTxt = powerUpsDialog.findViewById(R.id.timeDialogTextView);
        halfTxt = powerUpsDialog.findViewById(R.id.halfDialogTextView);
        skipTxt = powerUpsDialog.findViewById(R.id.skipDialogTextView);
        doubleTxt = powerUpsDialog.findViewById(R.id.doubleDialogTextView);

    }


    private void logout() {
        cancelLogout.setOnClickListener(view -> logoutDialog.cancel());

        confirmLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_LONG).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });

        logoutDialog.show();

    }

    private void showPasswordDialog() {
        cancelPass.setOnClickListener(v -> {
            oldPass.setText("");
            newPass.setText("");
            changePassDialog.cancel();
        });

        confirmPass.setOnClickListener(v -> {

            String oldPasswordText = oldPass.getText().toString();
            String newPasswordText = newPass.getText().toString();

            if (TextUtils.isEmpty(oldPasswordText) || TextUtils.isEmpty(newPasswordText)) {
                Toast.makeText(requireContext(), "Both Fields Are Required", Toast.LENGTH_LONG).show();
                return;
            }

            if (oldPasswordText.equals(newPasswordText)) {
                Toast.makeText(requireContext(), "New password must be different from the old one", Toast.LENGTH_LONG).show();
                return;
            }

            AuthCredential credential = EmailAuthProvider.getCredential(auth.getCurrentUser().getEmail(), oldPasswordText);
            auth.getCurrentUser().reauthenticate(credential)
                    .addOnSuccessListener(aVoid -> {
                        // Password re-authenticated, now update the password
                        auth.getCurrentUser().updatePassword(newPasswordText)
                                .addOnSuccessListener(aVoid1 -> {
                                    Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_LONG).show();
                                    oldPass.setText("");
                                    newPass.setText("");
                                    changePassDialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Failed to update password: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed to authenticate: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        changePassDialog.show();
    }

    private void showUsernameDialog() {

        cancelName.setOnClickListener(v -> {
            newName.setText("");
            changeNameDialog.cancel();
        });

        confirmName.setOnClickListener(v -> {

            String usernameText = newName.getText().toString().trim();

            if (TextUtils.isEmpty(usernameText)) {
                Toast.makeText(requireContext(), "Username is required", Toast.LENGTH_LONG).show();
                return;
            }

            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users")
                        .whereEqualTo("username", usernameText)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (queryDocumentSnapshots.isEmpty()) {
                                // Username is available, proceed with the update
                                db.collection("users")
                                        .document(user.getUid())
                                        .update("username", usernameText)
                                        .addOnSuccessListener(aVoid -> {
                                            // Username updated successfully in Firestore
                                            Toast.makeText(requireContext(), "Username updated successfully", Toast.LENGTH_LONG).show();
                                            newName.setText("");
                                            changeNameDialog.dismiss();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Failed to update username in Firestore
                                            Toast.makeText(requireContext(), "Failed to update username in Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        });
                            } else {
                                // Username is already in use
                                Toast.makeText(requireContext(), "Username is already taken", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(requireContext(), "Failed to check username availability: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });

            }
        });
        changeNameDialog.show();
    }

    private void getPowersAmount() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        db.collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        doublePointsAmount = documentSnapshot.getLong("doublePointsAmount").intValue();
                        halfHalfAmount = documentSnapshot.getLong("halfHalfAmount").intValue();
                        jokerAmount = documentSnapshot.getLong("jokerAmount").intValue();
                        skipQuestionAmount = documentSnapshot.getLong("skipQuestionAmount").intValue();
                        timeAmount = documentSnapshot.getLong("addTimeAmount").intValue();
                        Log.d("time amount", "" + timeAmount);

                        timeTxt.setText("Reset Time Amount: " + timeAmount);
                        jokerTxt.setText("Joker Amount: " + jokerAmount);
                        skipTxt.setText("Skip Amount: " + skipQuestionAmount);
                        halfTxt.setText("50/50 Amount: " + halfHalfAmount);
                        doubleTxt.setText("Double Points Amount: " + doublePointsAmount);

                        powerUpsDialog.show();
                    }
                });


    }

    private void showPowersDialog() {

    }


}