package com.example.speechful;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.speechful.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        startActivity(new Intent(getApplicationContext(),RegisterUser.class));
        setContentView(binding.getRoot());

        //sets the initial fragment
        replaceFragment(new LibraryFragment());

        //switches the fragment based on the id of the selected option in the bottom navigation menu
        binding.bottomNavigationView.setOnItemSelectedListener(item->{


            switch (item.getItemId()){
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

    //Replaces the current fragment with the fragment that is passed as an argument
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}