package com.petingo.englearn;

import android.widget.TextView;

/**
 * Created by Petingo on 2016/8/20.
 */
public class SearchResault {
    private String Eng;
    private String Chi;
    public SearchResault(String Eng, String Chi){
        this.Eng = Eng;
        this.Chi = Chi;
    }
    public String getEng(){
        return Eng;
    }
    public void setEng(String Eng){
        this.Eng = Eng;
    }
    public String getChi(){
        return Chi;
    }
    public void setChi(String Chi){
        this.Chi = Chi;
    }
}
