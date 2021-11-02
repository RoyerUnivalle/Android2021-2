package com.example.uceva20212.connection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Connection extends SQLiteOpenHelper {

    public String query = "create table students " +
            "(id INTEGER NOT NULL, name TEXT, last_name TEXT);";

    public Connection(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase objetoManipuladorDB) {
        objetoManipuladorDB.execSQL(query); // <- crear una tabla en la bd de nombre students
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
