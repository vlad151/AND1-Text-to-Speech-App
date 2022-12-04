package com.example.speechful.ui.library;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import com.example.speechful.R;
import com.example.speechful.model.Recording;
import com.example.speechful.ui.CreateAudio.RecordingViewModel;
import com.example.speechful.ui.login.UserViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class LibraryFragment extends Fragment {

    private RecyclerAdapter recyclerAdapter;
    private View view;
    private RecordingViewModel recordingViewModel;
    private TextToSpeech textToSpeech;


    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_library, container, false);
        recordingViewModel = new ViewModelProvider(this).get(RecordingViewModel.class);
        initializeRecycler();
        return view;

    }


    private void initializeRecycler() {
        RecyclerView recyclerView = view.findViewById(R.id.library_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recordingViewModel.initDatabase();

        recordingViewModel.getAllRecordings().observe(getViewLifecycleOwner(), recordings -> {
            recyclerAdapter = new RecyclerAdapter(recordings);
            recyclerView.setAdapter(recyclerAdapter);
            initializeTextToSpeech();

            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            String language = recordings.get(position).getLanguage();
                            switch (language) {
                                case "English":
                                    textToSpeech.setLanguage(Locale.ENGLISH);

                                    break;
                                case "Spanish":
                                    textToSpeech.setLanguage(new Locale("spa", "MEX"));

                                    break;
                                case "French":
                                    textToSpeech.setLanguage(Locale.FRENCH);

                                    break;
                                case "German":
                                    textToSpeech.setLanguage(Locale.GERMAN);

                                    break;
                                case "Italian":
                                    textToSpeech.setLanguage(Locale.ITALIAN);

                                    break;
                            }

                            textToSpeech.speak(recordings.get(position).getText(), TextToSpeech.QUEUE_FLUSH, null);
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            // do whatever
                        }
                    }));
        });

    }

    private void initializeTextToSpeech() {
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                        Log.i("TTS", "Language not supported");
                    }
                } else {
                    Log.i("TTS", "Initialization failed");
                }
            }
        });
    }

}