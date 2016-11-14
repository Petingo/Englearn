package com.petingo.englearn;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeechService;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

/* Created by Petingo on 2016/8/29. */
public class WordDetail extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_detail);
        TextView tvEng = (TextView) findViewById(R.id.word_detail_Eng);
        TextView tvChi = (TextView) findViewById(R.id.word_detail_Chi);

        Bundle bundle = getIntent().getExtras();
        String Eng = bundle.getString("Eng");
        String Chi = bundle.getString("Chi");

        tvEng.setText(Eng);
        tvChi.setText(WordDetailParser.ParserChinese(Chi));

        TextToSpeech TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                //TODO TTS!!!!
            }
        });

        FloatingActionButton fab_PlaySound =
                (FloatingActionButton) findViewById(R.id.floatingActionButton_PlaySound);
        fab_PlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
