package com.petingo.englearn;

/**
 * Created by petingo on 2017/8/20.
 */

public class Word {
    private String word, phonetic, definition, translation, pos, tag, exchange, detail, audio, example;
    private int collins, oxford, bnc, frq;

    public Word(String word, String phonetic, String definition, String translation, String pos, String tag, String exchange, String detail, String audio, String example, int collins, int oxford, int bnc, int frq) {
        this.word = word;
        this.phonetic = phonetic;
        this.definition = definition;
        this.translation = translation;
        this.pos = pos;
        this.tag = tag;
        this.exchange = exchange;
        this.detail = detail;
        this.audio = audio;
        this.example = example;
        this.collins = collins;
        this.oxford = oxford;
        this.bnc = bnc;
        this.frq = frq;
    }
    public Word(){}

    public String getWord() {
        return word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public String getDefinition() {
        return definition;
    }

    public String getTranslation() {
        return translation;
    }

    public String getPos() {
        return pos;
    }

    public String getTag() {
        return tag;
    }

    public String getExchange() {
        return exchange;
    }

    public String getDetail() {
        return detail;
    }

    public String getAudio() {
        return audio;
    }

    public String getExample() {
        return example;
    }

    public int getCollins() {
        return collins;
    }

    public int getOxford() {
        return oxford;
    }

    public int getBnc() {
        return bnc;
    }

    public int getFrq() {
        return frq;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public void setCollins(int collins) {
        this.collins = collins;
    }

    public void setOxford(int oxford) {
        this.oxford = oxford;
    }

    public void setBnc(int bnc) {
        this.bnc = bnc;
    }

    public void setFrq(int frq) {
        this.frq = frq;
    }

}
