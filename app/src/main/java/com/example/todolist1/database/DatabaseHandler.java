package com.example.todolist1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.todolist1.models.Note;

import java.util.ArrayList;
import java.util.List;



public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes_manager";
    private static final String TABLE_NAME = "notes";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_NOTE = "note";

    private static final String[] COLS_ID_TITLE_NOTE = new String[] {COLUMN_ID,COLUMN_NOTE,COLUMN_TITLE};


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"+", "
                + COLUMN_TITLE + " TEXT NOT NULL"+ ", "
                + COLUMN_NOTE + " TEXT"
                + ")";

        Log.d(TAG,CREATE_NOTES_TABLE);

        db.execSQL(CREATE_NOTES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;

        Log.d(TAG,DROP_TABLE);

        db.execSQL(DROP_TABLE);

        onCreate(db);

    }

    public void addNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_NOTE, note.getNote());


        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public Note getNote(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_NAME,COLS_ID_TITLE_NOTE,COLUMN_ID+"=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(c != null){
            c.moveToFirst();
        }
        db.close();

        Log.d(TAG,"Get Note Result "+ c.getString(0)+","+c.getString(1)+","+c.getString(2));
        Note note = new Note(Integer.parseInt(c.getString(0)),c.getString(1),c.getString(2));
        return note;
    }

    public List<Note> getAllNotes(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Note> noteList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME,COLS_ID_TITLE_NOTE,null,null,null,null,null);


        if(cursor!= null && cursor.moveToFirst()){

            do{
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setNote(cursor.getString(2));
                noteList.add(note);

            }while (cursor.moveToNext());


        }
        db.close();
        return noteList;

    }
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_NOTE, note.getNote());

        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        return count;
    }


}