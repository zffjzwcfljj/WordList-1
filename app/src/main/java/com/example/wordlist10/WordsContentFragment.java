package com.example.wordlist10;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class WordsContentFragment extends Fragment {
    private View view;
    private Words words;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.content,container,false);
        return view;
    }

    public void refresh(String word,String meaning,String sample){
        View visibilityLayout = view.findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        TextView  wordName = (TextView) view.findViewById(R.id.word);
        TextView  wordMeaning = (TextView) view.findViewById(R.id.word_meaning);
        TextView  wordSample = (TextView) view.findViewById(R.id.word_sample);
        wordName.setText(word);
        wordMeaning.setText(meaning);
        wordSample.setText(sample);
    }
}
