package com.example.wordlist10;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNameFragment extends Fragment {
    private WordsAdapter mAdapter;
    private boolean isTwoPane;
    public MyDatabaseHelper dbHelper;
    public WordsAdapter adapter;
    public String str1, str2, str3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.word_list,container,false);
        RecyclerView wordsNameRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        wordsNameRecyclerView.setLayoutManager(layoutManager);
        adapter = new WordsAdapter(getWords());
        wordsNameRecyclerView.setAdapter(adapter);

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        dbHelper.getWritableDatabase();
    }



    private List<Words> getWords(){
        List<Words> wordsList = new ArrayList<>();

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
        public List<Words> mWordsList;
        private int mPosition = -1;

        public OnItemClickListener onItemClickListener;
        public OnClickListener onClickListener;

        public int getPosition() {
            return mPosition;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView wordsNameText;
            TextView wordsMeaningText;
            TextView wordsSampleText;

            public ViewHolder(View v){
                super(v);
                wordsNameText = (TextView) itemView.findViewById(R.id.word_name);
                wordsMeaningText = (TextView) itemView.findViewById(R.id.word_meaning);
                wordsSampleText = (TextView) itemView.findViewById(R.id.word_sample);
            }
        }
        public WordsAdapter(List<Words> wordsList){
            mWordsList = wordsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            view.setLongClickable(true);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mPosition = getPosition();
                    return false;
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position){
            // Words model M
            // holder  ReclyverView V
            Words words = mWordsList.get(position);
            holder.wordsNameText.setText(words.getWordname());
            holder.wordsMeaningText.setText(words.getMeaning());
            holder.wordsSampleText.setText(words.getSample());

            if (onItemClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onItemClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }

            if (onClickListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickListener.onClick(view, position);
                    }
                });
            }
        }

        @Override
        public int getItemCount(){
            return mWordsList.size();
        }

        public OnItemClickListener getOnItemClickListener() {
            return onItemClickListener;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public OnClickListener getOnClickListener() {
            return onClickListener;
        }

        public void setOnClickListener(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }
    }



}

interface OnItemClickListener {
    void onItemLongClick(View view, int pos);
}

interface OnClickListener {
    void onClick(View view, int pos);
}