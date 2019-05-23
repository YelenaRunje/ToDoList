package com.example.todolist1;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist1.adapters.NotesAdapter;
import com.example.todolist1.database.DatabaseHandler;
import com.example.todolist1.models.Note;
import com.example.todolist1.utils.RecyclerTouchListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rvNotes)
    RecyclerView rvNotes;

    RecyclerView.Adapter adapter;
    List<Note> notesList;

    DatabaseHandler db = new DatabaseHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        initViews();
        loadNotes();

        rvNotes.addOnItemTouchListener(new RecyclerTouchListener(this,
                rvNotes, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }


    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, notesList.get(position), position);
                } else {
                    deleteNote(position);
                }
            }
        });
        builder.show();
    }

    private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputTitle = view.findViewById(R.id.in_dialog_title);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        final EditText inputNote = view.findViewById(R.id.in_dialog_note);
        TextView dialogNote = view.findViewById(R.id.dialog_note);

        dialogTitle.setText(!shouldUpdate ? getString(R.string.new_note) : getString(R.string.edit_note));

        if (shouldUpdate && note != null) {
            inputTitle.setText(note.getTitle());
            inputNote.setText(note.getNote());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (shouldUpdate && note != null) {
                    updateNote(inputNote.getText().toString(), position);
                }

            }
        });
    }


    private void initViews() {

        rvNotes.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadNotes() {
        DatabaseHandler db = new DatabaseHandler(this);

        notesList = db.getAllNotes();
        if (notesList.size() != 0) {
            adapter = new NotesAdapter(this, notesList);
            rvNotes.setAdapter(adapter);
        }
    }

    private void updateNote(String note, int position) {

        Note n = notesList.get(position);
        n.setNote(note);
        db.updateNote(n);

        notesList.set(position, n);
        adapter.notifyItemChanged(position);
    }

    private void deleteNote(int position) {

        db.deleteNote(notesList.get(position));

        notesList.remove(position);
        adapter.notifyItemRemoved(position);

    }

    @OnClick(R.id.fab_btn)
    public void addNote() {
        Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
        startActivity(i);
    }
}