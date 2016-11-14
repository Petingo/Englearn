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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {
    private EditText searchText;
    private ImageButton clearText;
    private ListView searchResaultList;
    List<SearchResault> searchResault_list = new ArrayList<>();

    public String[] Eng = new String[20];
    public String[] Chi = new String[20];
    Context search_Context;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_frag, container, false);
        search_Context = container.getContext();
        return view;
    }
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState){
        ECdicDBhelper echelper = new ECdicDBhelper(getActivity());
        final SQLiteDatabase ecdict = echelper.getReadableDatabase();
        searchText = (EditText) view.findViewById(R.id.searchText);
        clearText = (ImageButton) view.findViewById(R.id.clearText);
            clearText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchText.getText().clear();
                }
            });
            searchResaultList = (ListView) view.findViewById(R.id.searchResultList);
            searchResaultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showWD = new Intent();
                showWD.setClass(search_Context, WordDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("Eng",Eng[position]);
                bundle.putString("Chi",Chi[position]);
                showWD.putExtras(bundle);

                startActivity(showWD);
        }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchResault_list.clear();
                String inputText = searchText.getText().toString();
                if (!inputText.isEmpty() && inputText.matches("[a-zA-Z]+")) {
                    char firstChar = inputText.charAt(0);
                    Cursor cs = ecdict.rawQuery("Select * from list_" + Character.toUpperCase(firstChar), null);
                    int counterResault = 0, counterTotal = 0;

                    int dbmount = cs.getCount();

                    int up=dbmount,down=0;
                    cs.moveToFirst();
                    while(inputText.compareTo(cs.getString(1))!=0 && (up-down)<=1) {
                        Log.e("Search",String.valueOf((up)+"*"+String.valueOf(down)));
                        cs.moveToPosition((up+down)/2);
                        if(inputText.compareTo(cs.getString(1))>0){
                            down = (up+down)/2;
                        }
                        else{
                            up = (up+down)/2;
                        }
                    }

                    while (counterResault < 20 && counterTotal < dbmount) {
                        if (cs.getString(1).startsWith(inputText)) {
                            Eng[counterResault] = cs.getString(1);
                            Chi[counterResault] = cs.getString(3);
                            if(Chi[counterResault].length()>20){
                                char[] tmpChi = Chi[counterResault].toCharArray();
                                Chi[counterResault] = String.copyValueOf(tmpChi,0,20) + "...";
                            }
                            searchResault_list.add(new SearchResault(Eng[counterResault],Chi[counterResault]));
                            counterResault++;
                        }
                        cs.moveToNext();
                        counterTotal++;
                    }
                    cs.close();
                }
                else{//中文search

                }
                MyAdapter adapter = new MyAdapter(getActivity(),searchResault_list);
                searchResaultList.setAdapter(adapter);
            }
        });
        //List View -- http://givemepass.blogspot.tw/2011/11/listview.html
    }

}