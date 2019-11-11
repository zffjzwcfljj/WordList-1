package com.example.wordlist10;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class show extends AppCompatActivity {
    public String str;
    private ListView re;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);
        re = (ListView)findViewById(R.id.re);

        Intent intent = getIntent();
        str = intent.getStringExtra("result");


    }


}