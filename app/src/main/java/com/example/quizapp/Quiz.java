package com.example.quizapp;

import java.util.List;

public class Quiz {

    private String category;
    private String name;
    private String createdBy;
    private String userMail;
    private List<Question> questions;

    public Quiz(String category, String name, String createdBy, String userMail, List<Question> questions) {
        this.category = category;
        this.name = name;
        this.createdBy = createdBy;
        this.userMail = userMail;
        this.questions = questions;
    }

    public Quiz() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
