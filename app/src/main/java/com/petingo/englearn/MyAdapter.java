package com.petingo.englearn;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.NameList;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.StringTokenizer;

import static com.petingo.englearn.R.layout.add_word;

/* Created by Petingo on 2016/8/20. */
public class MyAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<SearchResault> searchResaults;
    private Context myContext;
    public MyAdapter(Context c, List<SearchResault> searchResault){
        myInflater = LayoutInflater.from(c);
        myContext = c;
        this.searchResaults = searchResault;
    }
    @Override
    public int getCount() {
        return searchResaults.size();
    }
    @Override
    public Object getItem(int arg0) {
        return searchResaults.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return searchResaults.indexOf(getItem(position));
    }

    private class ViewHolder{
        TextView txtEng;
        TextView txtChi;
        ViewHolder(TextView txtEng,TextView txtChi){
            this.txtEng = txtEng;
            this.txtChi = txtChi;
        }
    }

    private int selectedItem = 0;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            convertView = myInflater.inflate(R.layout.search_list_item, null);
            ImageButton addTheWord = (ImageButton) convertView.findViewById(R.id.addTheWord);
            addTheWord.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    MyWordDBhelper myWordHelper = new MyWordDBhelper(parent.getContext());
                    final SQLiteDatabase myWord = myWordHelper.getWritableDatabase();
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(parent.getContext());
                    LayoutInflater addWordDialogInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = addWordDialogInflater.inflate(R.layout.add_word,null);
                    final EditText editText_name = (EditText) dialogView.findViewById(R.id.editText_name);
                    dialog.setView(dialogView)
                            .setTitle("新增單字")
                            .setPositiveButton("確認",new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ContentValues cv = new ContentValues();
                                    if (selectedItem == 1) {
                                        String newName = editText_name.getText().toString();
                                        if (!newName.isEmpty()){
                                            cv.put("Name",newName);
                                            myWord.insert("NameList",null,cv);
                                            cv.clear();
                                        }
                                        else{
                                            Log.e("NO","Name");
                                        }
                                    }
                                    else{
                                    }
                        }
                    });
                    Spinner spinnerTableName = (Spinner) dialogView.findViewById(R.id.spinnerTableName);
                    ArrayList<String> TableName = new ArrayList<>();
                    TableName.add("...新增單字庫");

                    Cursor cs = myWord.rawQuery("Select * from NameList", null);
                    int NameListCounter = cs.getCount();
                    if(NameListCounter == 0){
                        ContentValues cv = new ContentValues();
                        cv.put("Name", "預設單字庫");
                        myWord.insert("NameList", null, cv);
                    }
                    cs.moveToFirst();
                    for(int i = 0 ; i < NameListCounter ; i++ ){
                        TableName.add(cs.getString(1));
                        cs.moveToNext();
                    }

                    cs.close();

                    ArrayAdapter<String> TableNameAdapter = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_dropdown_item, TableName);
                    spinnerTableName.setAdapter(TableNameAdapter);
                    spinnerTableName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedItem = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    dialog.show();
                }
            });
            holder = new ViewHolder(
                    (TextView)convertView.findViewById(R.id.resaultEng),
                    (TextView)convertView.findViewById(R.id.resaultChi)
            );
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        SearchResault searchResault = (SearchResault)getItem(position);
        holder.txtEng.setText(searchResault.getEng());
        holder.txtChi.setText(searchResault.getChi());
        return convertView;
    }

}
