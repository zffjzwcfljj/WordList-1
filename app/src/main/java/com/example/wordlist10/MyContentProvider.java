package com.example.wordlist10;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    public static final int WORD_DIR=0;
    public static final int WORD_ITEM=1;

    public static final String AUTHORITY="com.example.wordlist10.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"word",WORD_DIR);
        uriMatcher.addURI(AUTHORITY,"word/#",WORD_ITEM);
    }

    @Override
    public boolean onCreate(){
        dbHelper = new MyDatabaseHelper(getContext(),"WordList.db",null,2);
        return true;
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = 0;
        deletedRows = db.delete("Word",selection,selectionArgs);

        return deletedRows;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case WORD_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.wordlist10.provider.word";
            case WORD_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.wordlist10.provider.1word";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        long newWordId = db.insert("Word",null,values);
        uriReturn = Uri.parse("content://"+AUTHORITY+"/word/"+newWordId);
        return uriReturn;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.query("Word",projection,selection,selectionArgs,null,null,sortOrder);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updatedRows = 0;
        updatedRows = db.update("Word",values,selection,selectionArgs);

        return updatedRows;
    }

}
