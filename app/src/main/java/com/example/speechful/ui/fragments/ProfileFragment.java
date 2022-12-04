package com.example.speechful.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speechful.R;
import com.example.speechful.model.User;
import com.example.speechful.ui.CreateAudio.CreateAudioFragment;
import com.example.speechful.ui.login.LoginFragment;
import com.example.speechful.ui.login.LoginTabFragment;
import com.example.speechful.ui.login.UserViewModel;


public class ProfileFragment extends Fragment {

    private Button logoutButton, updateButton;
    private TextView name, email;
    private UserViewModel userViewModel;
    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        userViewModel= new ViewModelProvider(this).get(UserViewModel.class);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view){

        updateButton=view.findViewById(R.id.update_profile_button);
        logoutButton = view.findViewById(R.id.logout_button);
        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        logoutButton.setOnClickListener(this::logoutUser);
        updateButton.setOnClickListener(this::updateUser);
        name.setText(userViewModel.getCurrentUserName());
        email.setText(userViewModel.getCurrentUser().getEmail());
    }

    private void updateUser(View view) {

        User user = userViewModel.getUserByEmail(userViewModel.getCurrentUser().getEmail());
        String newEmail = email.getText().toString();
        String newName = name.getText().toString();
        user.setName(newName);
        user.setEmail(newEmail);
        userViewModel.updateUser(user);
        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();

    }

    private void logoutUser(View view) {
        userViewModel.logout();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, new LoginFragment()).commit();
        fragmentManager.beginTransaction().replace(R.id.view_pager, new LoginTabFragment()).commit();
    }


}