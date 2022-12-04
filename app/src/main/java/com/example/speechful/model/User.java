package com.example.speechful.model;

import java.util.ArrayList;

public class User {
    private String email, name, password;
    private ArrayList<Recording> recordings;
    public User() {

    }

    public User(String name, String email, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        recordings = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Recording> getRecordings() {
        return recordings;
    }

    public void setRecordings(ArrayList<Recording> recordings) {
        this.recordings = recordings;
    }
}