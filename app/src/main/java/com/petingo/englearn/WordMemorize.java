package com.petingo.englearn;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class WordMemorize extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.word_memorize, container, false);
        MyWordDBhelper myWordHelper = new MyWordDBhelper(getActivity());
        final SQLiteDatabase myWord = myWordHelper.getReadableDatabase();
        Cursor cs = myWord.rawQuery("Select * from NameList", null);
        cs.moveToFirst();
        int n = cs.getCount();
        ArrayList<String> TableName = new ArrayList<>();
        for(int i = 0 ; i < n ; i++ ){
            TableName.add(cs.getString(1));
            cs.moveToNext();
        }
        ArrayAdapter<String> TableNameAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, TableName);
        ListView lvTableName = (ListView) myView.findViewById(R.id.lvTableName);
        lvTableName.setAdapter(TableNameAdapter);
        lvTableName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("Clicked",String.valueOf(i));
            }
        });
        cs.close();
        return myView;
    }}