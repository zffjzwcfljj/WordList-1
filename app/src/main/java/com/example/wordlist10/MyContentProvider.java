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
        switch (uriMatcher.match(uri)){
            case WORD_DIR:
                deletedRows = db.delete("Word",selection,selectionArgs);
                break;
            case WORD_ITEM:
                String wordId = uri.getPathSegments().get(1);
                deletedRows = db.delete("Word","id = ?",new String[] {wordId});
                break;
            default:
                break;
        }
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
//        switch (uriMatcher.match(uri)){
//            case WORD_DIR:
//            case WORD_ITEM:
//            default:
//
//                break;
//        }
        long newWordId = db.insert("Word",null,values);
        uriReturn = Uri.parse("content://"+AUTHORITY+"/word/"+newWordId+newWordId);
        return uriReturn;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch(uriMatcher.match(uri)){
            case WORD_DIR:
                cursor = db.query("Word",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case WORD_ITEM:
                String wordId=uri.getPathSegments().get(1);
                cursor = db.query("Word",projection,"id=?",new String[]{wordId},null,null,sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updatedRows = 0;
        switch (uriMatcher.match(uri)){
            case WORD_DIR:
                updatedRows = db.update("Word",values,selection,selectionArgs);
                break;
            case WORD_ITEM:
                String wordId = uri.getPathSegments().get(1);
                updatedRows = db.update("Word",values,"id = ?",new String[] {wordId});
                break;
                default:
                    break;
        }
        return updatedRows;
    }

}
