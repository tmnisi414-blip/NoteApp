package com.example.noteapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Note {
    private Long id;
    private String title;
    private String content;
    private long dateCreated;

    // Constructor for new notes
    public Note(String title, String content, long dateCreated) {
        this.title = title;
        this.content = content;
        this.dateCreated = dateCreated;
    }

    // Constructor for existing notes (with ID)
    public Note(Long id, String title, String content, long dateCreated) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateCreated = dateCreated;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date(dateCreated));
    }
}