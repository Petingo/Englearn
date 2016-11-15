package com.petingo.englearn;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(parent.getContext());
                    //View dialogView = myInflater.inflate(R.layout.add_word,dialog.);
                    LayoutInflater addWordDialogInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = addWordDialogInflater.inflate(R.layout.add_word,null);
                    final EditText editText_name = (EditText) dialogView.findViewById(R.id.editText_name);
                    dialog.setView(add_word)
                            .setTitle("新增單字")
                            .setPositiveButton("確認",new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.e("press","OK");
                                    String a = editText_name.getText().toString();

                                    Log.e("text",a);
                        }
                    });
                    Spinner spinnerTableName = (Spinner) dialogView.findViewById(R.id.spinnerTableName);
                    ArrayList<String> TableName = new ArrayList<String>();
                    TableName.add(String.valueOf(R.string.createNewWordList));
                    ArrayAdapter<String> TableNameAdapter;

                    MyWordDBhelper myWordHelper = new MyWordDBhelper(parent.getContext());
                    SQLiteDatabase myWord = myWordHelper.getReadableDatabase();
                    Cursor cs = myWord.rawQuery("Select * from NameList", null);
                    cs.moveToFirst();
                    int NameListCounter = cs.getColumnCount();
                    for(int i = 0 ; i < NameListCounter ; i++ ){
                        TableName.add(cs.getString(1));
                    }
                    //
                    cs.close();

                    TableNameAdapter = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_item, TableName);
                    spinnerTableName.setAdapter(TableNameAdapter);
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
