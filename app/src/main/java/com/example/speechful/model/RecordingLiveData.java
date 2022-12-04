package com.example.speechful.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecordingLiveData extends LiveData<List<Recording>> {
    DatabaseReference databaseReference;

    public RecordingLiveData(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    private final ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<Recording> recordings = new ArrayList<>();
            snapshot.getChildren().forEach(dataSnapshot ->

                    recordings.add(dataSnapshot.getValue(Recording.class)));
            setValue(recordings);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        databaseReference.addValueEventListener(listener);
    }
    protected void onInactive() {
        super.onInactive();
        databaseReference.removeEventListener(listener);
    }
}
