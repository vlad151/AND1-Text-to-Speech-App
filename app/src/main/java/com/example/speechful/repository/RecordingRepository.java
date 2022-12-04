package com.example.speechful.repository;

import androidx.lifecycle.LiveData;

import com.example.speechful.model.Recording;
import com.example.speechful.model.RecordingLiveData;

import java.util.ArrayList;
import java.util.List;

public interface RecordingRepository {
    void insertRecording(Recording recording);
    void deleteRecording(Recording recording);
    void updateRecording(Recording recording);
    LiveData<List<Recording>> getAllRecordings();

    void initDatabase();
}
