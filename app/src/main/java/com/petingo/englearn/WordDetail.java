package com.petingo.englearn;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

/* Created by Petingo on 2016/8/29. */
public class WordDetail extends com.petingo.englearn.MyTextToSpeech {
    private List<Pair<String, String>> detail = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_detail);

        final String word = Search.selectedWord.getWord();
        final String translation = Search.selectedWord.getTranslation();
        final String exchange = Search.selectedWord.getExchange();

        String phonetic = Search.selectedWord.getPhonetic();
        String example = Search.selectedWord.getExample();

        if (Util.notNullOrEmpty(phonetic)){
            Log.e("phonetic",phonetic);
            phonetic = "[" + phonetic + "]";
        }

        TextView txtWord = (TextView) findViewById(R.id.word_detail_word);
        TextView txtKK = (TextView) findViewById(R.id.word_detail_kk);

        txtWord.setText(word);
        txtKK.setText(phonetic);

        detailParser(translation);
        exampleParser(example);
        exchangeParser(exchange);

        WordDetailAdapter WordDetailAdapter = new WordDetailAdapter(detail);
        RecyclerView mList = (RecyclerView) findViewById(R.id.word_detail_list_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(WordDetailAdapter);

        TTS = new TextToSpeech(this, this);
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        //startActivityForResult(checkIntent, TTS_CHECK_CODE);

        FloatingActionButton fab_PlaySound =
                (FloatingActionButton) findViewById(R.id.floatingActionButton_PlaySound);
        fab_PlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String utteranceId = this.hashCode() + "";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    TTS.speak(word, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                } else {
                    TTS.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.word_detail_appbar);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void exampleParser(String example){
        if (Util.notNullOrEmpty(example)) {
            example = example.replace("*", "\n");
            detail.add(Pair.create("例句", example));
        }
    }

    private void detailParser(String resource) {
        resource = resource.replace("a.", "adj.");
        resource = resource.replace("ad.", "adv.");

        resource = resource.replace("n.", "名詞:");
        resource = resource.replace("adj.", "形容詞:");
        resource = resource.replace("adv.", "副詞:");
        resource = resource.replace("vt.", "及物動詞:");
        resource = resource.replace("vi.", "不及物動詞:");
        resource = resource.replace("v.", "動詞:");
        resource = resource.replace("vbl.", "動詞變化:");
        resource = resource.replace("int.", "感嘆詞:");
        resource = resource.replace("prep.", "介繫詞:");
        resource = resource.replace("abbr.", "縮寫:");
        resource = resource.replace("conj.", "連接詞:");
        resource = resource.replace("art.", "冠詞:");
        resource = resource.replace("pron.", "代名詞:");
        resource = resource.replace("pl.", "複數:");
        resource = resource.replace("num.", "數量詞:");

        resource = resource.replace(",", "，");
        resource = resource.replace("\\n","---");
        String[] trans = resource.split("---");

        for (String t : trans) {
            if (!t.isEmpty()) {
                String[] tmp;
                if(t.contains(":")){
                    tmp = t.split(":");
                    detail.add(Pair.create(tmp[0], tmp[1]));
                } else {
                    detail.add(Pair.create("釋意", t));
                }
            }
        }
    }

    private void exchangeParser(String exchange){
        if(Util.notNullOrEmpty(exchange)){
            exchange = exchange.replace("p:","過去式：");
            exchange = exchange.replace("d:","過去分詞：");
            exchange = exchange.replace("i:","現在分詞：");
            exchange = exchange.replace("3:","第三人稱：");
            exchange = exchange.replace("r:","比較級：");
            exchange = exchange.replace("t:","最高級：");
            exchange = exchange.replace("s:","複數形式：");
            exchange = exchange.replace("0:","原形：");

            String output = "";
            if (exchange.contains("/")) {
                String ex[] = exchange.split("/");
                String nextLine = "";
                for (String t : ex){
                    if(t.startsWith("1")){
                        continue;
                    }
                    output += nextLine;
                    output += t;
                    nextLine = "\n";
                }

            } else {
                output = exchange;
            }

            detail.add(Pair.create("詞態變化" ,output));

        }
    }
}
