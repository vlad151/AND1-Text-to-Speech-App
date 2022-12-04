package com.example.speechful.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.speechful.repository.UserRepository;
import com.example.speechful.repository.UserRepositoryImpl;
import com.google.firebase.auth.FirebaseUser;

public class MainViewModel extends ViewModel {
    private UserRepository userRepository;

    public MainViewModel() {
        userRepository = UserRepositoryImpl.getInstance();
    }
    public LiveData<FirebaseUser> getUser() {
        return userRepository.getUser();
    }
    public FirebaseUser getCurrentUser() {
        return userRepository.getCurrentUser();
    }

}
