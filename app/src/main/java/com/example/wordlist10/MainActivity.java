package com.example.wordlist10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(wordNameFragment.adapter);
        registerForContextMenu(recyclerView);

        List<Words> items = getAll();
        wordNameFragment.adapter.mWordsList = items;
        wordNameFragment.adapter.notifyDataSetChanged();

        wordNameFragment.adapter.setOnClickListener(new OnClickListener(){

            // 判断横屏竖屏

            Configuration configuration = MainActivity.this.getResources().getConfiguration();

            boolean isTwoPane = (configuration.orientation == configuration.ORIENTATION_LANDSCAPE ? true : false);

            public void onClick(View v, int pos){
                Words words = getAll().get(pos);
                if(isTwoPane){
                    WordsContentFragment wordsContentFragment = (WordsContentFragment) getSupportFragmentManager().getFragments().get(1);
                    wordsContentFragment.refresh(words.getWordname(),words.getMeaning(),words.getSample());
                }else{
                    WordsContentActivity.actionStart(MainActivity.this,words.getWordname(),words.getMeaning(),words.getSample());
                }

            }


        });
        wordNameFragment.adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemLongClick(View view, final int pos) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View viewer = (inflater.inflate(R.layout.add,null));

                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu1, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        switch (itemId) {
                            case R.id.delete:
                                Words w = getAll().get(pos);
                                if(w !=null){
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    String sql = "delete from Word where id=" + w.getId();
                                    SQLiteDatabase db1 = dbHelper.getReadableDatabase();
                                    db1.execSQL(sql);
                                    System.out.println(sql);
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

                        return false;
                    }
                });

                popupMenu.show();
            }


        });

    }


    public List<Words> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Words words = new Words();
        String[] projection = {
                "id",
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
                word.setId(String.valueOf(c.getString(c.getColumnIndex("id"))));
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
                            wordNameFragment.adapter.mWordsList = items;
                            wordNameFragment.adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(MainActivity.this,"没有找到",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.show();
                break;

            case R.id.refresh:
                List<Words> items = getAll();
                wordNameFragment.adapter.mWordsList = items;
                wordNameFragment.adapter.notifyDataSetChanged();
                break;
        }
        return true;
    }


}
