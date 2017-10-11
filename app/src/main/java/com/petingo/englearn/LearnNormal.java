package com.petingo.englearn;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by petingo on 2017/8/23.
 */

public class LearnNormal extends Activity {
    private int func = 0;
    private int total;

    private TextView eng;
    private TextView chi;
    private TextView counter;
    private Button last;
    private Button next;

    private SQLiteDatabase myWord;
    private Cursor cs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_normal);
        final Bundle bundle = getIntent().getExtras();
        final String selectedListName = bundle.getString("selectedListName");
        final int mode = bundle.getInt("mode"); // 0 = 顯英  1 = 顯中
        final String query = "Select * from WordList where listName = '" + selectedListName + "'";

        eng = (TextView) findViewById(R.id.learn_normal_eng);
        chi = (TextView) findViewById(R.id.learn_normal_chi);
        counter = (TextView) findViewById(R.id.learn_normal_counter);
        last = (Button) findViewById(R.id.learn_normal_last);
        next = (Button) findViewById(R.id.learn_normal_next);
        myWord = UserDataHelper.getReadableDB(this);

        cs = myWord.rawQuery(query, null);
        cs.moveToFirst();
        total = cs.getCount();

        setText();

        if (mode == 0) {
            chiInv();
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCounter();
                    if (func == 0) {
                        chi.setVisibility(View.VISIBLE);
                        next.setText(LearnNormal.this.getString(R.string.next));
                        func = 1;
                    } else if (func == 1) {
                        if (cs.isLast()) {
                            end();
                        } else {
                            moveToNext();
                        }
                    } else {
                        finish();
                    }
                }
            });
            last.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCounter();
                    moveToLast();
                    if(cs.isFirst()){
                        last.setClickable(true);
                    }
                }
            });
        }

    }
    private void updateCounter(){
        counter.setText(cs.getPosition() + "/" + total);
    }
    private void chiInv(){
        chi.setVisibility(View.INVISIBLE);
        next.setText(LearnNormal.this.getString(R.string.showTranslation));
    }
    private void setText(){
        eng.setText(cs.getString(1));
        chi.setText(cs.getString(3));
    }
    private void moveToNext(){
        last.setClickable(true);
        cs.moveToNext();
        setText();
        chiInv();
        func = 0;
    }
    private void moveToLast(){
        cs.moveToLast();
        setText();
        chiInv();
        func = 0;
    }
    private void end(){
        Toast.makeText(LearnNormal.this, "已經到底囉！", Toast.LENGTH_SHORT).show();
        last.setText(LearnNormal.this.getString(R.string.again));
        next.setText(LearnNormal.this.getString(R.string.exit));
        eng.setText("結束囉！");
        chi.setText("");
        counter.setText("");
        func = 2;
    }
}
