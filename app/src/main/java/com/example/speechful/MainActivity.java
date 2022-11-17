package com.example.speechful;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.example.speechful.databinding.ActivityMainBinding;
import com.example.speechful.ui.fragments.CreateAudioFragment;
import com.example.speechful.ui.fragments.LibraryFragment;
import com.example.speechful.ui.fragments.ProfileFragment;
import com.example.speechful.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       hideTitleBar();
        login();
        initializeViews();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void initializeViews() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new CreateAudioFragment());
        binding.bottomNavigationView.setSelectedItemId(R.id.createAudio);



        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.library:
                    replaceFragment(new LibraryFragment());
                    break;
                case R.id.createAudio:
                    replaceFragment(new CreateAudioFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }

            return true;
        });
    }

    private void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
    private void hideTitleBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
    }
}
