package com.example.wordlist10;

import android.provider.BaseColumns;

public class Words {
    public Words(){

    }

    private String id;
    private String wordname;
    private String meaning;
    private String sample;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getWordname(){
        return wordname;
    }

    public void setWordname(String wordname){
        this.wordname=wordname;
    }

    public String getMeaning(){
        return meaning;
    }

    public void setMeaning(String meaning){
        this.meaning=meaning;
    }

    public String getSample(){
        return sample;
    }

    public void setSample(String sample){
        this.sample=sample;
    }


}
