package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button addButton;
    private DatabaseHelper databaseHelper;
    private NotesAdapter notesAdapter;
    private List<Note> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupDatabase();
        setupListView();
        loadNotes();
    }

    private void initializeViews() {
        listView = findViewById(R.id.listViewNotes);
        addButton = findViewById(R.id.buttonAddNote);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupDatabase() {
        databaseHelper = new DatabaseHelper(this);
        notesList = new ArrayList<>();
    }

    private void setupListView() {
        notesAdapter = new NotesAdapter(this, notesList);
        listView.setAdapter(notesAdapter);

        // Handle item clicks for editing
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = notesList.get(position);
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra("NOTE_ID", note.getId());
                intent.putExtra("NOTE_TITLE", note.getTitle());
                intent.putExtra("NOTE_CONTENT", note.getContent());
                startActivity(intent);
            }
        });

        // Handle long clicks for deletion
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(notesList.get(position));
                return true;
            }
        });
    }

    private void showDeleteDialog(final Note note) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete \"" + note.getTitle() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> deleteNote(note))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteNote(Note note) {
        boolean deleted = databaseHelper.deleteNote(note.getId());
        if (deleted) {
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
            loadNotes();
        } else {
            Toast.makeText(this, "Error deleting note", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNotes() {
        notesList.clear();
        notesList.addAll(databaseHelper.getAllNotes());
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes(); // Refresh notes when returning from edit activity
    }
}