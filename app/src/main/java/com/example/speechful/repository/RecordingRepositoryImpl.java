package com.example.speechful.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.speechful.R;
import com.example.speechful.model.Recording;
import com.example.speechful.model.RecordingLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RecordingRepositoryImpl implements RecordingRepository {

    private static RecordingRepository instance;
    private static Lock lock = new ReentrantLock();
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference databaseReference;
    private RecordingLiveData recordingLiveData;

    public RecordingRepositoryImpl() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void initDatabase(){
        mDatabase = FirebaseDatabase.getInstance("https://speechful-19583-default-rtdb.firebaseio.com/");
        databaseReference = mDatabase.getReference("Recordings");
        recordingLiveData = new RecordingLiveData(databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()));
    }
    public static RecordingRepository getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new RecordingRepositoryImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void insertRecording(Recording recording) {

        if (recording.getText().equals("")) {
            return;
        }
        recording.setSaved(false);
        databaseReference.child(mAuth.getCurrentUser().getUid()).push().setValue(recording);

    }

    @Override
    public void deleteRecording(Recording recording) {
        Query query = databaseReference.child(mAuth.getCurrentUser().getUid()).orderByChild("text").equalTo(recording.getText());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if(childSnapshot.child("language").getValue().equals(recording.getLanguage()) && childSnapshot.child("saved").getValue().equals(recording.isSaved())){
                        childSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void updateRecording(Recording recording) {
    Query query = databaseReference.child(mAuth.getCurrentUser().getUid()).orderByChild("text").equalTo(recording.getText());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if(childSnapshot.child("language").getValue().equals(recording.getLanguage()) && childSnapshot.child("saved").getValue().equals(recording.isSaved())){
                        childSnapshot.getRef().setValue(recording);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public LiveData<List<Recording>> getAllRecordings() {
        return recordingLiveData;
    }
}

