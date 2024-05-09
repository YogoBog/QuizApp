package com.example.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;

    public LeaderboardAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leader_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user, position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profileImageView;
        private TextView usernameTextView;
        private TextView scoreTextView;
        private TextView rowTextview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            usernameTextView = itemView.findViewById(R.id.titleTextView);
            scoreTextView = itemView.findViewById(R.id.amountTextView);
            rowTextview = itemView.findViewById(R.id.rowTextView);
        }

        public void bind(User user, int position) {
            Glide.with(context).load(user.getProfilePictureUrl()).into(profileImageView);
            usernameTextView.setText(user.getUsername());
            scoreTextView.setText(String.valueOf(user.getUserPoints()));
            int rowNumber = position + 4;
            rowTextview.setText(rowNumber + ".");
        }
    }
}
