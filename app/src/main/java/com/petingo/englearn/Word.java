package com.petingo.englearn;

/**
 * Created by petingo on 2017/8/20.
 */

public class Word {
    private String Eng;
    private String KK;
    private String Chi;
    private String example;
    Word(String E, String K, String C, String e){
        this.Eng = E;
        this.KK = K;
        this.Chi = C;
        this.example = e;
    }
    Word(){}
    public String getEng(){
        return this.Eng;
    }
    public String getKK(){
        return this.KK;
    }
    public String getChi(){
        return this.Chi;
    }
    public String getExample(){
        return this.example;
    }
    public void setEng(String t){
        this.Eng = t;
    }
    public void setKK(String t){
        this.KK = t;
    }
    public void setChi(String t){
        this.Chi = t;
    }
    public void setExample(String t){
        this.example = t;
    }

}
