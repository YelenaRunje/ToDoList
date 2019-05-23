package com.example.todolist1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todolist1.R;
import com.example.todolist1.models.Note;

import java.util.ArrayList;
import java.util.List;

;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    Context context;
    List<Note> noteList = new ArrayList<>();

    public NotesAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.row_note,parent,false);
        NotesViewHolder nvh = new NotesViewHolder(v);
        return nvh;

    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        holder.tvTitle.setText(noteList.get(position).getTitle());
        holder.tvNote.setText(noteList.get(position).getNote());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle,tvNote;
        public NotesViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvNote = itemView.findViewById(R.id.tvNoteText);

        }
    }

}