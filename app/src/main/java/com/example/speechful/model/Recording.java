package com.example.speechful.model;

public class Recording {

    private String text, language;
    private boolean saved;

    public Recording() {
    }

    public Recording( String text, String language, boolean saved) {
        this.language = language;
        this.text = text;
        this.saved = saved;
        setSaved(false);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String voice) {
        this.language = voice;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Recording{" +
                ", text='" + text + '\'' +
                ", voice='" + language + '\'' +
                ", saved=" + saved +
                '}';
    }


}
