package com.example.speechful.ui.CreateAudio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.speechful.model.Recording;
import com.example.speechful.repository.RecordingRepository;
import com.example.speechful.repository.RecordingRepositoryImpl;
import com.example.speechful.repository.UserRepository;
import com.example.speechful.repository.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class RecordingViewModel extends ViewModel {
    private RecordingRepository repository;
    private UserRepository userRepository;

    public RecordingViewModel() {
        repository= RecordingRepositoryImpl.getInstance();
        userRepository = UserRepositoryImpl.getInstance();
    }

    public LiveData<List<Recording>> getAllRecordings() {
        return repository.getAllRecordings();
    }

    void insertRecording(Recording recording){

        initDatabase();
        repository.insertRecording(recording);
    }

    public void deleteRecording(Recording recording){
        repository.deleteRecording(recording);
    }

    public void updateRecording(Recording recording){
        repository.updateRecording(recording);
    }

    public void getCurrentUser(){
        userRepository.getCurrentUser();
    }

    public void initDatabase(){
        repository.initDatabase();
    }

}
