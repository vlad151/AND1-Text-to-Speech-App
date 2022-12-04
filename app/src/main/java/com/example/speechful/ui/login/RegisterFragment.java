package com.example.speechful.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.speechful.R;
import com.example.speechful.ui.CreateAudio.CreateAudioFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class RegisterFragment extends Fragment {

    Button registerButton;
    EditText name, email, password, repeatPassword;
    private UserViewModel userViewModel;
    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_tab, container, false);

        initializeViews(view);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        registerButton.setOnClickListener(this::registerUser);

        return view;
    }

    private void initializeViews(View view) {
        registerButton = view.findViewById(R.id.registerButton);
        email = view.findViewById(R.id.editTextEmailAddress);
        name = view.findViewById(R.id.editTextPersonName);
        password = view.findViewById(R.id.editTextPassword);
        repeatPassword = view.findViewById(R.id.editTextRepeatPassword);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);


    }

    private void registerUser(View view) {
        boolean validName = validateName();
        boolean validEmail = validateEmail();
        boolean validPassword = validatePassword();
        boolean validRepeatPassword = validateRepeatPassword();

        if (!(validName && validEmail && validPassword && validRepeatPassword)) {
            return;
        }
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String name = this.name.getText().toString().trim();
        userViewModel.addUser(name, email, password);
        navigateToMain();

    }

    private void navigateToMain() {
        bottomNavigationView.setSelectedItemId(R.id.createAudio);
        bottomNavigationView.setVisibility(View.VISIBLE);
        getFragmentManager().beginTransaction().add(R.id.frame_layout, new CreateAudioFragment()).commit();


    }


    private boolean validateName() {
        String val = name.getText().toString();
        if (val.isEmpty()) {
            name.setError("Field cannot be empty");
            return false;
        } else {
            name.setError(null);
            return true;
        }

    }

    private boolean validateEmail() {
        String val = email.getText().toString();
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

    private boolean validatePassword() {

        String val = password.getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    private boolean validateRepeatPassword() {
        String val = repeatPassword.getText().toString();
        String passwordVal = password.getText().toString();

        if (val.isEmpty()) {
            repeatPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.equals(passwordVal)) {
            repeatPassword.setError("Password does not match");
            return false;
        } else {
            repeatPassword.setError(null);
            return true;
        }
    }


}