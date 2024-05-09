package com.example.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private List<Quiz> quizzes;
    private static View.OnClickListener onItemClickListener;

    public QuizAdapter(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
        notifyDataSetChanged();
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void removeQuiz(int pos) {
        quizzes.remove(pos);
    }

    public Quiz getQuiz(int pos) {
        return quizzes.get(pos);
    }

    public void addQuiz(int pos, Quiz quiz) {
        quizzes.add(pos, quiz);
        notifyItemInserted(pos);
    }


    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizzes.get(position);
        holder.quizNameTextView.setText("Name: " + quiz.getName());
        holder.categoryTextView.setText("Category: " + quiz.getCategory());
        holder.createdByTextView.setText("Created By: " + quiz.getCreatedBy());
    }

    @Override
    public int getItemCount() {
        return quizzes != null ? quizzes.size() : 0;
    }

    static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView quizNameTextView;
        TextView categoryTextView;
        TextView createdByTextView;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizNameTextView = itemView.findViewById(R.id.quizNameTextView);
            categoryTextView = itemView.findViewById(R.id.categoryNameTextView);
            createdByTextView = itemView.findViewById(R.id.createdByTextView);
            itemView.setOnClickListener(onItemClickListener);

        }
    }

}
