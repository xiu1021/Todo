package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteOperator {
    private DBHelper dbHelper;

    public NoteOperator(Context context) {

        dbHelper = new DBHelper(context);
    }


    public boolean insert(Note note) {
        //与数据库建立连接
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Note.KEY_title, note.title);
        contentValues.put(Note.KEY_context, note.context);
        //插入每一行数据
        long note_id = db.insert(Note.TABLE, null, contentValues);
        db.close();
        if (note_id != -1)
            return true;
        else
            return false;
    }


    public void delete(int note_id) {
        //与数据库建立连接
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Note.TABLE, Note.KEY_id + "=?", new String[]{String.valueOf(note_id)});
        db.close();
    }


    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> getNoteList() {
        //与数据库建立连接
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select " + Note.KEY_id + "," + Note.KEY_title + "," + Note.KEY_context +
                " from " + Note.TABLE;
        //通过游标将每一条数据放进ArrayList中
        ArrayList<HashMap<String, String>> noteList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> note = new HashMap<String, String>();
            note.put("id", cursor.getString(cursor.getColumnIndex(Note.KEY_id)));
            note.put("title", cursor.getString(cursor.getColumnIndex(Note.KEY_title)));
            noteList.add(note);
        }
        cursor.close();
        db.close();
        return noteList;
    }


    @SuppressLint("Range")
    public Note getNoteById(int id) {
        //与数据库建立连接
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select " + Note.KEY_title + "," + Note.KEY_context +
                " from " + Note.TABLE + " where " + Note.KEY_id + "=?";
        Note note = new Note();
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        while (cursor.moveToNext()) {
            note.title = cursor.getString(cursor.getColumnIndex(Note.KEY_title));
            note.context = cursor.getString(cursor.getColumnIndex(Note.KEY_context));
        }
        cursor.close();
        db.close();
        return note;
    }


    public void update(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Note.KEY_title, note.title);
        contentValues.put(Note.KEY_context, note.context);

        db.update(Note.TABLE, contentValues, Note.KEY_id + "=?", new String[]{String.valueOf(note.note_id)});
        db.close();
    }
}