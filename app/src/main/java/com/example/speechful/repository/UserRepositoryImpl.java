package com.example.speechful.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.speechful.model.LiveUser;
import com.example.speechful.model.Recording;
import com.example.speechful.model.User;
import com.example.speechful.ui.login.RegisterFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserRepositoryImpl implements UserRepository {

    private LiveUser liveUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private static UserRepository instance;
    private static Lock lock = new ReentrantLock();
    private DatabaseReference databaseReference;
    private String userId;
    private String nickname;
    private User currentUser;

    public UserRepositoryImpl() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://speechful-19583-default-rtdb.firebaseio.com/");
        databaseReference = mDatabase.getReference("Users");
        liveUser = new LiveUser();

    }

    public static UserRepository getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new UserRepositoryImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void addUser(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = new User(name, email, password);
                addUserToDatabase(user);
            } else {
                Log.d("UserRepository", "addUser: " + task.getException().getMessage());
            }
        });
    }

    private void addUserToDatabase(User user) {
        databaseReference.child(mAuth.getCurrentUser().getUid()).setValue(user);
    }

    @Override
    public LiveData<FirebaseUser> getUser() {
        return liveUser;
    }

    @Override
    public void signOut() {
        // mAuth.signOut();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void signInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password);

    }

    @Override
    public FirebaseUser getCurrentUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        return user;
    }

    @Override
    public void updateUser(User user) {
        databaseReference.child(mAuth.getCurrentUser().getUid()).setValue(user);
    }

    @Override
    public String getCurrentUserNickname() {

        userId = getCurrentUser().getUid();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    nickname = user.getName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("UserRepositoryImpl", "Failed to read value.", error.toException());
            }
        });

        return nickname;
    }

    @Override
    public void setCurrentUser(User user) {
        currentUser = user;
    }

    @Override
    public User getUserByEmail(String email) {
       Query query = databaseReference.orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        currentUser = dataSnapshot.getValue(User.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("UserRepositoryImpl", "Failed to read value.", error.toException());
                }
            });
            return currentUser;
    }
}
