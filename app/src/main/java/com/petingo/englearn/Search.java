package com.petingo.englearn;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {
    private EditText searchText;
    private SQLiteDatabase ecDict;

    List<Word> searchResultList = new ArrayList<>();
    public static Word selectedWord;
    ImageView clearText;
    Context search_Context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_frag, container, false);
        search_Context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        DictHelper ecHelper = new DictHelper(getActivity());
        ecDict = ecHelper.getReadableDatabase();
        searchText = (EditText) view.findViewById(R.id.searchText);
        clearText = (ImageView) view.findViewById(R.id.clearText);
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.getText().clear();
            }
        });
        final ListView searchResultListView = (ListView) view.findViewById(R.id.searchResultList);
        searchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showWordDetail = new Intent();
                showWordDetail.setClass(search_Context, WordDetail.class);
                selectedWord = searchResultList.get(position);
                startActivity(showWordDetail);
            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ;
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchResultList.clear();
                String inputText = searchText.getText().toString();
                if (!inputText.isEmpty() && inputText.matches("[a-zA-Z]+")) {
                    ecSearch(inputText);
                } else {
                    //TODO 中文search

                }
                MyAdapter adapter = new MyAdapter(getActivity(), searchResultList);
                searchResultListView.setAdapter(adapter);
            }
        });
    }

    private void ecSearch_old(String inputText) {
        char firstChar = inputText.charAt(0);
        Cursor cs = ecDict.rawQuery("Select * from list_" + Character.toUpperCase(firstChar), null);
        int counterResult = 0, counterTotal = 0;
        int dbMount = cs.getCount();
        int up = dbMount, down = 0;
        cs.moveToFirst();
        while (inputText.compareTo(cs.getString(1)) != 0 && (up - down) <= 1) {
            cs.moveToPosition((up + down) >> 1);
            if (inputText.compareTo(cs.getString(1)) > 0) {
                down = (up + down) >> 1;
            } else {
                up = (up + down) >> 1;
            }
        }
        while (counterResult < 20 && counterTotal < dbMount) {
            if (cs.getString(1).startsWith(inputText)) {
                Word tmp = new Word();
                tmp.setExample(cs.getString(4));
                searchResultList.add(tmp);
                counterResult++;
            }
            cs.moveToNext();
            counterTotal++;
        }
        cs.close();
    }

    private void ecSearch_old2(String inputText) {
        Cursor cs = ecDict.rawQuery("SELECT * FROM ec WHERE eng LIKE '"
                + inputText + "%' limit 50", null);
        cs.moveToFirst();
        if (cs.getCount() != 0) {
            int i = 0;
            while (!cs.isAfterLast() && i < 50) {
                String tEng = cs.getString(1);
                String tKK = cs.getString(2);
                String tChi = cs.getString(3);
                String tExp = cs.getString(4);
                Word tmp = new Word();
                tmp.setExample(tExp); //can be ignore
                searchResultList.add(tmp);
                i++;
                cs.moveToNext();
            }
        }
        cs.close();
    }

    private void ecSearch(String inputText) {
        String query = "Select * from ec_" + Character.toLowerCase(inputText.charAt(0)) +
                " WHERE word LIKE '" + inputText + "%' limit 50";
        Cursor cs = ecDict.rawQuery(query, null);
        cs.moveToFirst();
        if (cs.getCount() != 0) {
            while (!cs.isAfterLast()) {
                String word = cs.getString(1);
                String phonetic = cs.getString(2);
                String definition = cs.getString(3);
                String translation = cs.getString(4);
                String pos = cs.getString(5);
                String tag = cs.getString(8);
                String exchange = cs.getString(11);
                String detail = cs.getString(12);
                String audio = cs.getString(13);
                String example = cs.getString(14);
                int collins = cs.getInt(6);
                int oxford = cs.getInt(7);
                int bnc = cs.getInt(9);
                int frq = cs.getInt(10);
                Word tmp = new Word(word, phonetic, definition, translation, pos, tag, exchange,
                        detail, audio, example, collins, oxford, bnc, frq);
                searchResultList.add(tmp);
                cs.moveToNext();
            }
        }

    }

}