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
import android.widget.Toast;

import org.w3c.dom.NameList;

import java.util.ArrayList;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.StringTokenizer;

import static com.petingo.englearn.R.layout.add_word;

/* Created by Petingo on 2016/8/20. */
class MyAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<SearchResult> searchResults;
    private Context myContext;

    MyAdapter(Context c, List<SearchResult> searchResult) {
        myInflater = LayoutInflater.from(c);
        myContext = c;
        this.searchResults = searchResult;
    }

    @Override
    public int getCount() {
        return searchResults.size();
    }

    @Override
    public Object getItem(int arg0) {
        return searchResults.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return searchResults.indexOf(getItem(position));
    }

    private class ViewHolder {
        TextView txtEng;
        TextView txtChi;

        ViewHolder(TextView txtEng, TextView txtChi) {
            this.txtEng = txtEng;
            this.txtChi = txtChi;
        }
    }

    private int selectedItem = 0;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        final SearchResult searchResult = (SearchResult) getItem(position);
        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.search_list_item, null);
            ImageButton addTheWord = (ImageButton) convertView.findViewById(R.id.addTheWord);
            addTheWord.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    //TODO test if it's need!
                    showAddWordDialog(parent.getContext());
                }
            });
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.resultEng),
                    (TextView) convertView.findViewById(R.id.resultChi)
            );
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtEng.setText(searchResult.getEng());
        holder.txtChi.setText(searchResult.getChi());
        return convertView;
    }

    private void showAddWordDialog(final Context context) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.add_word, null);
        dialog.setView(view)
                .setTitle(myContext.getString(R.string.addNewVoc))
                .setPositiveButton(myContext.getString((R.string.confirm)), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedItem == 0) {
                            EditText editName = (EditText) view.findViewById(R.id.editText_name);
                            String name = editName.getText().toString();
                            if (!name.isEmpty()) {
                                UserDataHelper.newWordListName(context, name);
                            } else {
                                //TODO name = now
                                Date date = new Date(System.currentTimeMillis());
                            }
                        }
                        //TODO find which to insert and insert
//                                    cv.put("Eng", searchResult.getEng());
//                                    myWord.insert("WordList", null, cv);
//                                    cv.clear();
                    }

                });
        Spinner spinnerTableName = (Spinner) view.findViewById(R.id.spinnerTableName);
        ArrayList<String> tableName = getAllWordListName();
        ArrayAdapter<String> TableNameAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tableName);
        spinnerTableName.setAdapter(TableNameAdapter);
//        spinnerTableName.setSelection(NameListCounter);
        spinnerTableName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { ;}
        });
        dialog.show();
    }

    private ArrayList<String> getAllWordListName(){
        ArrayList<String> name = new ArrayList<>();
        name.add(myContext.getString(R.string.selectHereToAddNewList));
        Cursor cs = UserDataHelper.getReadableDB(myContext).rawQuery("Select * from NameList", null);
        int NameListCounter = cs.getCount();
        cs.moveToFirst();
        for (int i = 0; i < NameListCounter; i++) {
            name.add(cs.getString(1));
            cs.moveToNext();
        }
        cs.close();

        return name;
    }
}
