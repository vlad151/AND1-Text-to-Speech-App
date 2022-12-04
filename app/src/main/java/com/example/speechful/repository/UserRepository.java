package com.example.speechful.repository;

import androidx.lifecycle.LiveData;

import com.example.speechful.model.User;
import com.google.firebase.auth.FirebaseUser;

public interface UserRepository {
    void addUser(String name,String email, String password);
    LiveData<FirebaseUser> getUser();

    void signOut();

    void signInWithEmailAndPassword(String email, String password);

    FirebaseUser getCurrentUser();

    void updateUser(User user);

    String getCurrentUserNickname();

    void setCurrentUser(User user);

   User getUserByEmail(String email);
}
