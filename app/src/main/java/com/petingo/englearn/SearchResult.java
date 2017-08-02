package com.petingo.englearn;

// Petingo @ 2016/8/20
class SearchResult {
    private String Eng;
    private String Chi;
    SearchResult(String Eng, String Chi){
        this.Eng = Eng;
        this.Chi = Chi;
    }
    String getEng(){
        return Eng;
    }
    void setEng(String Eng){
        this.Eng = Eng;
    }

    String getChi(){
        return Chi;
    }
    void setChi(String Chi){
        this.Chi = Chi;
    }
}
