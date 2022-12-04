package com.example.speechful.ui.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.speechful.R;
import com.example.speechful.model.User;
import com.example.speechful.ui.CreateAudio.CreateAudioFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;


public class LoginTabFragment extends Fragment {

    private EditText email, password;
    private Button loginButton;
    private UserViewModel userViewModel;
    private BottomNavigationView bottomNavigationView;


    public LoginTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_tab, container, false);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        initializeFields(view);
        return view;
    }

    private void initializeFields(View view) {

        email = view.findViewById(R.id.loginEmailAddress);
        password = view.findViewById(R.id.loginPassword);
        loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this::loginUser);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
    }

    private void loginUser(View view) {
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();

        if (email.isEmpty()) {
            this.email.setError("Email is required");
            this.email.requestFocus();
            return;
        }

        validateEmail(email);
        if (password.length() < 6) {
            this.password.setError("Min password length should be 6 characters");
            this.password.requestFocus();
            return;
        }

        if (userViewModel.getUserByEmail(email)==null) {
            this.email.setError("Wrong email or password");
            this.email.requestFocus();
            return;
        }

        if (userViewModel.getUserByEmail(email) != null && (!userViewModel.getUserByEmail(email).getPassword().equals(password) || !userViewModel.getUserByEmail(email).getEmail().equals(email)))  {
            this.password.setError("Wrong email or password");
            this.password.requestFocus();
            return;
        }
        if(userViewModel.getUserByEmail(email).getPassword().equals(password) && userViewModel.getUserByEmail(email).getEmail().equals(email)){
            userViewModel.signInWithEmailAndPassword(email, password);
            bottomNavigationView.setSelectedItemId(R.id.createAudio);
            bottomNavigationView.setVisibility(View.VISIBLE);
            getFragmentManager().beginTransaction().add(R.id.frame_layout, new CreateAudioFragment()).commit();
        }
       return;
    }

    private boolean validateEmail(String val) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

}