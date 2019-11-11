package com.example.wordlist10;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class WordsContentActivity extends AppCompatActivity {
    public static void actionStart(Context context, String wordName, String wordMeaning,String wordSample){
        Intent intent = new Intent(context,WordsContentActivity.class);
        intent.putExtra("word_name",wordName);
        intent.putExtra("word_meaning",wordMeaning);
        intent.putExtra("word_sample",wordSample);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_content);
        String wordName = getIntent().getStringExtra("word_name");
        String wordMeaing = getIntent().getStringExtra("word_meaning");
        String wordSample = getIntent().getStringExtra("word_sample");
        WordsContentFragment wordsContentFragment=(WordsContentFragment) getSupportFragmentManager().findFragmentById(R.id.word_content_fragment);
        wordsContentFragment.refresh(wordName,wordMeaing,wordSample);
    }
}
