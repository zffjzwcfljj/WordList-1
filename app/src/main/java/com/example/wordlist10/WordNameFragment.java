package com.example.wordlist10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WordNameFragment extends Fragment {
    private boolean isTwoPane;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.word_list,container,false);
        RecyclerView wordsNameRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        wordsNameRecyclerView.setLayoutManager(layoutManager);
        WordsAdapter adapter = new WordsAdapter(getWords());
        wordsNameRecyclerView.setAdapter(adapter);
        return view;
    }

    private List<Words> getWords(){
        List<Words> wordsList = new ArrayList<>();
        for(int i=1;i<=50;i++){
            Words words = new Words();
            words.setWordname("WordName");
            words.setContent("jieshi");
            wordsList.add(words);
        }
        return wordsList;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.content_layout)!=null){
            isTwoPane = true;
        }else{
            isTwoPane = false;
        }
    }

    class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder>{
        private List<Words> mWordsList;
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView wordsNameText;
            TextView wordsContent;
            public ViewHolder(View v){
                super(v);
                wordsNameText = (TextView) itemView.findViewById(R.id.word_name);
                wordsContent = (TextView) itemView.findViewById(R.id.word_translation);
            }
        }
        public WordsAdapter(List<Words> wordsList){
            mWordsList = wordsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Words words = mWordsList.get(holder.getAdapterPosition());
                    if(isTwoPane){
                        WordsContentFragment wordsContentFragment = (WordsContentFragment) getFragmentManager().findFragmentById(R.id.word_content_fragment);
                        wordsContentFragment.refresh(words.getWordname(),words.getContent());
                    }else{
                        WordsContentActivity.actionStart(getActivity(),words.getWordname(),words.getContent());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            Words words = mWordsList.get(position);
            holder.wordsNameText.setText(words.getWordname());
            holder.wordsContent.setText(words.getContent());
        }

        @Override
        public int getItemCount(){
            return mWordsList.size();
        }

    }

    

}
