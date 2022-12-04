package com.example.speechful.ui.library;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.speechful.R;
import com.example.speechful.model.Recording;
import com.example.speechful.ui.CreateAudio.RecordingViewModel;
import com.like.LikeButton;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Recording> recordings;
    private RecordingViewModel recordingViewModel;

    public RecyclerAdapter(List<Recording> recordings) {
        recordingViewModel = new RecordingViewModel();
        this.recordings = recordings;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text, voice;
        private ImageView languageFlag;
        private ImageButton deleteButton, saveButton;
        private TextToSpeech textToSpeech;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.audio_text);
            voice = (TextView) itemView.findViewById(R.id.voice_text_field);
            languageFlag = (ImageView) itemView.findViewById(R.id.language_flag);
            deleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
            saveButton = (ImageButton) itemView.findViewById(R.id.download_button);
            initializeTextToSpeech();
            initSaveButton();
            initDeleteButton();

        }

        private void initSaveButton() {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> myHashRender = new HashMap<String, String>();
                    myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text.getText().toString());

                    String exStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File appTmpPath = new File(exStoragePath);
                    appTmpPath.mkdirs();
                    String tempFilename = text.getText().toString().replace(" ", "_");
                    if (tempFilename.length() > 20) {
                        tempFilename = tempFilename.substring(0, 20);
                    }

                    String filename = tempFilename + "_" + voice.getText().toString() + ".wav";

                    String tempDestFile = appTmpPath.getAbsolutePath() + "/" + filename;
                    System.out.println(tempDestFile.toString());
                    setLanguage();
                    textToSpeech.synthesizeToFile(text.getText().toString(), myHashRender, tempDestFile);
                    Toast.makeText(v.getContext(), "Audio saved to " + tempDestFile, Toast.LENGTH_LONG).show();
                    saveButton.setEnabled(false);
                    saveButton.setImageResource(R.drawable.ic_baseline_check_24);
                    recordings.get(getAdapterPosition()).setSaved(true);
                    recordingViewModel.updateRecording(recordings.get(getAdapterPosition()));
                }
            });
        }

        private void initDeleteButton() {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Recording recording = recordings.get(getAdapterPosition());
                    recordings.remove(recording);
                    recordingViewModel.deleteRecording(recording);
                    notifyDataSetChanged();
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), recordings.size());
                    Toast.makeText(v.getContext(), "Recording deleted", Toast.LENGTH_LONG).show();


                }
            });
        }

        private void initializeTextToSpeech() {
            textToSpeech = new TextToSpeech(itemView.getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = textToSpeech.setLanguage(Locale.US);
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("error", "This Language is not supported");
                        } else {
                            deleteButton.setEnabled(true);
                        }
                    } else
                        Log.e("error", "Initialization Failed!");
                }
            });
        }

        private void setLanguage() {
            switch (voice.getText().toString()) {
                case "English":
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    break;
                case "Spanish":
                    textToSpeech.setLanguage(new Locale("spa", "ESP"));
                    break;
                case "French":
                    textToSpeech.setLanguage(Locale.FRANCE);
                    break;
                case "German":
                    textToSpeech.setLanguage(Locale.GERMAN);
                    break;
                case "Italian":
                    textToSpeech.setLanguage(Locale.ITALIAN);
                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.text.setText(recordings.get(position).getText());
        holder.voice.setText(recordings.get(position).getLanguage());
        if (recordings.get(position).isSaved()) {
            holder.saveButton.setEnabled(false);
            holder.saveButton.setImageResource(R.drawable.ic_baseline_check_24);
        } else {
            holder.saveButton.setEnabled(true);
            holder.saveButton.setImageResource(R.drawable.ic_baseline_download_for_offline_24);
        }

        switch (recordings.get(position).getLanguage()) {
            case "English":
                holder.languageFlag.setImageResource(R.drawable.british_flag_small);
                break;
            case "Spanish":
                holder.languageFlag.setImageResource(R.drawable.spanish_flag_small);
                break;
            case "French":
                holder.languageFlag.setImageResource(R.drawable.french_flag_small);
                break;
            case "German":
                holder.languageFlag.setImageResource(R.drawable.german_flag_small);
                break;
            case "Italian":
                holder.languageFlag.setImageResource(R.drawable.italian_flag_small);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }


}


