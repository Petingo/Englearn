package com.petingo.englearn;

import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class WordMemorize extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View myView = inflater.inflate(R.layout.word_memorize, container, false);
        final SQLiteDatabase myWord = UserDataHelper.getReadableDB(getActivity());
        final Cursor cs = myWord.rawQuery("Select * from NameList", null);
        cs.moveToFirst();
        int n = cs.getCount();
        ArrayList<String> TableName = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            TableName.add(cs.getString(1));
            cs.moveToNext();
        }
        ArrayAdapter<String> TableNameAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, TableName);
        final ListView lvTableName = (ListView) myView.findViewById(R.id.lvTableName);
        lvTableName.setAdapter(TableNameAdapter);
        lvTableName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedListName = lvTableName.getItemAtPosition(i).toString();

                final String query = "Select * from WordList where listName = '" + selectedListName + "'";
                final SQLiteDatabase myWord = UserDataHelper.getReadableDB(getActivity());
                final Cursor cs_t = myWord.rawQuery(query, null);
                final int c = cs_t.getCount();
                if (c == 0) {
                    Toast.makeText(getActivity(), getString(R.string.empty), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LearnNormal.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("selectedListName", selectedListName);
                    bundle.putInt("mode", 0);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    Log.e("Clicked", selectedListName);
                }
            }
        });
        cs.close();
        return myView;
    }
}