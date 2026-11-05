package com.example.noteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NotesAdapter extends BaseAdapter {
    private Context context;
    private List<Note> notes;
    private LayoutInflater inflater;

    public NotesAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        Note note = notes.get(position);
        return note.getId() != null ? note.getId() : 0L;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_note, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = convertView.findViewById(R.id.textViewTitle);
            holder.contentTextView = convertView.findViewById(R.id.textViewContent);
            holder.dateTextView = convertView.findViewById(R.id.textViewDate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Note note = notes.get(position);

        holder.titleTextView.setText(note.getTitle());

        String content = note.getContent();
        if (content.length() > 100) {
            content = content.substring(0, 100) + "...";
        }
        holder.contentTextView.setText(content);

        holder.dateTextView.setText(note.getFormattedDate());

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        TextView dateTextView;
    }
}