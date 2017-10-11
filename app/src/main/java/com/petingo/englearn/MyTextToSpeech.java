package com.petingo.englearn;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Locale;

/**
 * Created by petingo on 2017/9/22.
 */

public class MyTextToSpeech extends Activity implements android.speech.tts.TextToSpeech.OnInitListener{
    public GoogleApiClient client;
    public android.speech.tts.TextToSpeech TTS;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        int TTS_CHECK_CODE = 9527;
        if (requestCode == TTS_CHECK_CODE) {
            if (resultCode == android.speech.tts.TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) //如果TTS Engine有成功找到的話
            {
                TTS = new android.speech.tts.TextToSpeech(this, this);
                //宣告一個 TextToSpeech instance
                //並註冊android.speech.tts.TextToSpeech.OnInitListener
                //當TTS Engine 初始完畢之後會呼叫 onInit(int status)
                Log.d("onActivityResult", "onInit");
            } else {
                //如果TTS Engine 沒有安裝的話 , 要求API安裝
                Intent installIntent = new Intent();
                installIntent.setAction(android.speech.tts.TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("WordDetail Page")// TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onInit(int status) {
        if (status == android.speech.tts.TextToSpeech.SUCCESS) {
            //設定語言為英文
            int result = TTS.setLanguage(Locale.US);
            //如果該語言資料不見了或沒有支援則無法使用
            if (result == android.speech.tts.TextToSpeech.LANG_MISSING_DATA || result == android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                //語調(1為正常語調；0.5比正常語調低一倍；2比正常語調高一倍)
                TTS.setPitch(1);
                //速度(1為正常速度；0.5比正常速度慢一倍；2比正常速度快一倍)
                TTS.setSpeechRate((float) 1);
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

}
