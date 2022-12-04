package com.example.speechful.ui.login;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.speechful.model.User;
import com.example.speechful.repository.UserRepository;
import com.example.speechful.repository.UserRepositoryImpl;
import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends ViewModel {

    private UserRepository userRepository;

    public UserViewModel() {
       userRepository = new UserRepositoryImpl().getInstance();

    }
    public void addUser(String name, String email, String password) {
        userRepository.addUser(name, email, password);
    }
    public void signInWithEmailAndPassword(String email, String password) {
        userRepository.signInWithEmailAndPassword(email, password);
    }
    public LiveData<FirebaseUser> getUser() {
        return userRepository.getUser();
    }

    public FirebaseUser getCurrentUser() {
        return userRepository.getCurrentUser();
    }
    public void logout() {
        userRepository.signOut();
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }
    public String getCurrentUserName() {
        return userRepository.getCurrentUserNickname();
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
}
