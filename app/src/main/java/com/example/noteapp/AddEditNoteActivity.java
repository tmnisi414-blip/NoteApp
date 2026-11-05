package com.example.noteapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditNoteActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextContent;
    private Button buttonSave;
    private Button buttonDelete;
    private DatabaseHelper databaseHelper;

    private Long noteId;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        initializeViews();
        setupDatabase();
        loadNoteData();
        setupButtons();
    }

    private void initializeViews() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);
    }

    private void setupDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }

    private void loadNoteData() {
        noteId = getIntent().getLongExtra("NOTE_ID", -1L);
        if (noteId != -1L) {
            isEditMode = true;
            editTextTitle.setText(getIntent().getStringExtra("NOTE_TITLE"));
            editTextContent.setText(getIntent().getStringExtra("NOTE_CONTENT"));
            buttonDelete.setVisibility(View.VISIBLE);
            setTitle("Edit Note");
        } else {
            isEditMode = false;
            buttonDelete.setVisibility(View.GONE);
            setTitle("New Note");
        }
    }

    private void setupButtons() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (title.isEmpty()) {
            editTextTitle.setError("Title is required");
            return;
        }

        Note note;
        if (isEditMode) {
            note = new Note(noteId, title, content, System.currentTimeMillis());
        } else {
            note = new Note(title, content, System.currentTimeMillis());
        }

        boolean success;
        if (isEditMode) {
            success = databaseHelper.updateNote(note);
        } else {
            success = databaseHelper.addNote(note) != -1L;
        }

        if (success) {
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving note", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> deleteNote())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteNote() {
        if (noteId != null) {
            boolean deleted = databaseHelper.deleteNote(noteId);
            if (deleted) {
                Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error deleting note", Toast.LENGTH_SHORT).show();
            }
        }
    }
}