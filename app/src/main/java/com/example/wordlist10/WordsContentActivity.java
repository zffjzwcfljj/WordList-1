package com.example.wordlist10;

import android.app.AppComponentFactory;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class WordsContentActivity extends AppCompatActivity {
    public static void actionStart(Context context, String wordName, String wordContent){
        Intent intent = new Intent(context,WordsContentActivity.class);
        intent.putExtra("word",wordName);
        intent.putExtra("wordContent",wordContent);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_content);
        String wordName = getIntent().getStringExtra("word_name");
        String wordContent = getIntent().getStringExtra("word_content");
        WordsContentFragment wordsContentFragment=(WordsContentFragment) getSupportFragmentManager().findFragmentById(R.id.word_content_fragment);
        wordsContentFragment.refresh(wordName,wordContent);
    }
}
