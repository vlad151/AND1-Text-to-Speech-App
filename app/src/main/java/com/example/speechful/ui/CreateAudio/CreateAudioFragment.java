package com.example.speechful.ui.CreateAudio;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.speechful.R;
import com.example.speechful.model.Recording;
import com.example.speechful.repository.RecordingRepository;
import com.example.speechful.repository.RecordingRepositoryImpl;
import com.example.speechful.ui.login.UserViewModel;

import java.util.Locale;


public class CreateAudioFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private static TextToSpeech textToSpeech;
    private EditText audioText;
    private Button playButton, saveButton;
    private Spinner languageSpinner;
    private ImageView imageView;
    private RecordingViewModel recordingViewModel;


    public CreateAudioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_audio, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        recordingViewModel = new ViewModelProvider(this).get(RecordingViewModel.class);
        audioText = (EditText) view.findViewById(R.id.createAudioText);
        playButton = (Button) view.findViewById(R.id.playButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        imageView = (ImageView) view.findViewById(R.id.imageView);

        initializeSpinner();
        initializeTextToSpeech();
    }

    private void initializeSpinner() {
        languageSpinner = (Spinner) view.findViewById(R.id.languageSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);
        languageSpinner.setOnItemSelectedListener(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageSpinner.performClick();
            }
        });
    }

    private void initializeTextToSpeech() {

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        playButton.setEnabled(false);
                        saveButton.setEnabled(false);
                        Log.i("TTS", "Language not supported");
                    } else {
                        playButton.setEnabled(true);
                        saveButton.setEnabled(true);
                    }
                } else {
                    Log.i("TTS", "Initialization failed");
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Recording recording = new Recording(audioText.getText().toString(), languageSpinner.getSelectedItem().toString(), false);
                speak();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recording recording = new Recording(audioText.getText().toString(), languageSpinner.getSelectedItem().toString(), true);
                recordingViewModel.insertRecording(recording);
            }
        });
    }

    private void speak() {
        String text = audioText.getText().toString();
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        switch (text) {
            case "English":
                textToSpeech.setLanguage(Locale.ENGLISH);
                imageView.setImageResource(R.drawable.british_flag);
                break;
            case "Spanish":
                textToSpeech.setLanguage(new Locale("spa", "MEX"));
                imageView.setImageResource(R.drawable.spanish_flag);
                break;
            case "French":
                textToSpeech.setLanguage(Locale.FRENCH);
                imageView.setImageResource(R.drawable.french_flag);
                break;
            case "German":
                textToSpeech.setLanguage(Locale.GERMAN);
                imageView.setImageResource(R.drawable.german_flag);
                break;
            case "Italian":
                textToSpeech.setLanguage(Locale.ITALIAN);
                imageView.setImageResource(R.drawable.italian_flag);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}