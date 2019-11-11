package com.example.wordlist10;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

//        List<Map<String, String>> items = getAll();

        dbHelper.getWritableDatabase();
    }



    private List<Words> getWords(){
        List<Words> wordsList = new ArrayList<>();

        return wordsList;
    }

//    public List<Map<String, String>> getAll() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Words words = new Words();
//        String[] projection = {
//                "name",
//                "Chinese",
//                "sentence",
//        };
//
//        String sortOrder = words.getWordname() == null ? null : words.getWordname() + "DESC";
//
//        Cursor c;
//        c = db.query(
//                "Word",
//                projection,
//                null,
//                null,
//                null,
//                null,
//                sortOrder
//        );
//        return ConvertCursor2List(c);
//    }



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

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Words words = mWordsList.get(holder.getAdapterPosition());
                    if(isTwoPane){
                        WordsContentFragment wordsContentFragment = (WordsContentFragment) getFragmentManager().findFragmentById(R.id.word_content_fragment);
                        wordsContentFragment.refresh(words.getWordname(),words.getMeaning(),words.getSample());
                    }else{
                        WordsContentActivity.actionStart(getActivity(),words.getWordname(),words.getMeaning(),words.getSample());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            // Words model M
            // holder  ReclyverView V
            Words words = mWordsList.get(position);
            holder.wordsNameText.setText(words.getWordname());
            holder.wordsMeaningText.setText(words.getMeaning());
            holder.wordsSampleText.setText(words.getSample());
        }

        @Override
        public int getItemCount(){
            return mWordsList.size();
        }

    }



}
