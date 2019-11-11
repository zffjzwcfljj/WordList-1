package com.example.wordlist10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    public String str1, str2, str3;
    private List<Words> wordList = new ArrayList<>();
    private WordNameFragment wordNameFragment;
    private Words words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(this, "WordList.db", null, 1);
        dbHelper.getWritableDatabase();

        FragmentManager fragmentManager = getSupportFragmentManager();

        wordNameFragment = (WordNameFragment) fragmentManager.findFragmentById(R.id.wordName_fragment);
        wordNameFragment.dbHelper = dbHelper;

//        WordNameFragment wordnameFragment = new WordNameFragment();
//        WordNameFragment.WordsAdapter adapter = wordnameFragment.new WordsAdapter(MainActivity.this,R.layout.word_item,wordList);
//       WordNameFragment.WordsAdapter adapter = new WordNameFragment.WordsAdapter(MainActivity.this,R.layout.word_item,wordList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(wordNameFragment.adapter);
        registerForContextMenu(recyclerView);

        List<Words> items = getAll();
        wordNameFragment.adapter.mWordsList = items;
        wordNameFragment.adapter.notifyDataSetChanged();


    }


    public List<Words> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Words words = new Words();
        String[] projection = {
                "name",
                "Chinese",
                "sentence",
        };

        String sortOrder = words.getWordname() == null ? null : words.getWordname() + "DESC";

        Cursor c;
        c = db.query(
                "Word",
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );


        List<Words> wordsList = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                Words word = new Words();
                word.setWordname(String.valueOf(c.getString(c.getColumnIndex("name"))));
                word.setMeaning(String.valueOf(c.getString(c.getColumnIndex("Chinese"))));
                word.setSample(String.valueOf(c.getString(c.getColumnIndex("sentence"))));

                wordsList.add(word);
            } while (c.moveToNext());
        }
        return wordsList;
    }



    public List<Words> searchUseSql(String str) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Words words = new Words();
        String[] projection = {
                "name",
                "Chinese",
                "sentence",
        };
        String sql = "select * from Word where name like ? order by id desc";
        Cursor c = db.rawQuery(sql,new String[]{"%" + str + "%"});

        List<Words> wordsList = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                Words word = new Words();
                word.setWordname(String.valueOf(c.getString(c.getColumnIndex("name"))));
                word.setMeaning(String.valueOf(c.getString(c.getColumnIndex("Chinese"))));
                word.setSample(String.valueOf(c.getString(c.getColumnIndex("sentence"))));

                wordsList.add(word);
            } while (c.moveToNext());
        }
        return wordsList;
    }


//    public ArrayList<Map<String, String>> ConvertCursor2List(Cursor c) {
//        ArrayList<Map<String, String>> res = new ArrayList<>();
//        Words words = new Words();
//        while (c.moveToNext()) {
//            Map<String, String> map = new HashMap<>();
//
//            map.put(words.getWordname(), String.valueOf(c.getString(1)));
//            map.put(words.getMeaning(), String.valueOf(c.getString(2)));
//            map.put(words.getSample(), String.valueOf(c.getString(3)));
//            res.add(map);
//
//        }return res;
//    }



    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu,menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View viewer = (inflater.inflate(R.layout.add,null));
        final View viewer1 = (inflater.inflate(R.layout.query_main,null));
        switch (item.getItemId()){
            case R.id.help:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Help.class);
                startActivity(intent);
                break;
            case R.id.add:
                builder.setView(viewer);
                builder.setTitle("添加界面");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText input1 = (EditText) viewer.findViewById(R.id.edit1);
                        EditText input2 = (EditText) viewer.findViewById(R.id.edit2);
                        EditText input3 = (EditText) viewer.findViewById(R.id.edit3);
                        str1 = input1.getText().toString();
                        str2 = input2.getText().toString();
                        str3 = input3.getText().toString();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("name",str1);
                        values.put("Chinese",str2);
                        values.put("sentence",str3);
                        db.insert("Word",null,values);
                        values.clear();
                        Toast.makeText(MainActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        List<Words> items = getAll();
                        wordNameFragment.adapter.mWordsList = items;
                        wordNameFragment.adapter.notifyDataSetChanged();
                    }
                });
                builder.show();
                break;

            case R.id.query:
                builder.setView(viewer1);
                builder.setTitle("模糊查询");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText input = (EditText) viewer1.findViewById(R.id.edit);
                        str1 = input.getText().toString();
                        input.setText(str1);

                        ArrayList<Words> items= (ArrayList<Words>) searchUseSql(str1);
                        if(items.size()>0) {
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("result",items);
                            Intent intent = new Intent (MainActivity.this,show.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,"没有找到",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.show();
                break;
        }
        return true;
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MainActivity.this.getMenuInflater().inflate(R.menu.menu1, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View viewer = (inflater.inflate(R.layout.add,null));
        TextView textName = null;
        AdapterView.AdapterContextMenuInfo info=null;
        View itemView=null;

        switch(item.getItemId()){

            case R.id.delete:
               info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        //        System.out.println("XXXXXXX"+info.position);
               itemView=info.targetView;
                textName = (TextView)itemView.findViewById(R.id.textName);
                if(textName !=null){
                     SQLiteDatabase db = dbHelper.getWritableDatabase();
                    String sql = "delete froe Word where name = +'"+textName+"'";
                    SQLiteDatabase db1 = dbHelper.getReadableDatabase();
                    db1.execSQL(sql);
                    Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                }

                List<Words> items = getAll();
                wordNameFragment.adapter.mWordsList = items;
                wordNameFragment.adapter.notifyDataSetChanged();
                break;

            case R.id.change:
                builder.setView(viewer);
                builder.setTitle("修改界面");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText input1 = (EditText) viewer.findViewById(R.id.edit1);
                        EditText input2 = (EditText) viewer.findViewById(R.id.edit2);
                        EditText input3 = (EditText) viewer.findViewById(R.id.edit3);
                        str1 = input1.getText().toString();
                        str2 = input2.getText().toString();
                        str3 = input3.getText().toString();
                        input1.setText(str1);
                        input2.setText(str2);
                        input3.setText(str3);

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("name",str1);
                        values.put("Chinese",str2);
                        values.put("sentence",str3);
                        db.update("Word",values,"name=?",new String[]{str1});
                        values.clear();
                        Toast.makeText(MainActivity.this,"修改成功",Toast.LENGTH_SHORT).show();

                        List<Words> items = getAll();
                        wordNameFragment.adapter.mWordsList = items;
                        wordNameFragment.adapter.notifyDataSetChanged();
                    }
                });
                builder.show();
                break;

        }
        return true;
    }



}
