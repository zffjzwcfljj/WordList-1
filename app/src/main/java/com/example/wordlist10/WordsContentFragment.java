package com.example.wordlist10;

import android.app.AppComponentFactory;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class WordsContentFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.content,container,false);
        return view;
    }

    public void refresh(String word,String content){
        View visiblityLayout = view.findViewById(R.id.visibility_layout);
        visiblityLayout.setVisibility(View.VISIBLE);
        TextView  wordName = (TextView) view.findViewById(R.id.word);
        TextView  wordContent = (TextView) view.findViewById(R.id.word_content);
        wordName.setText(word);
        wordContent.setText(content);
    }
}
