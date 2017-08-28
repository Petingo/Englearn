package com.petingo.englearn;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.StringTokenizer;

import static android.provider.BaseColumns._ID;
import static com.petingo.englearn.R.layout.add_word;

/* Created by Petingo on 2016/8/20. */
class MyAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<Word> searchResults;
    private Context myContext;
    private int selectedItem = 0;
    SharedPreferences pref;

    MyAdapter(Context c, List<Word> searchResult) {
        myInflater = LayoutInflater.from(c);
        myContext = c;
        pref = myContext.getSharedPreferences(myContext.getPackageName(), Context.MODE_PRIVATE);
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

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        final Word searchResult = (Word) getItem(position);
        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.search_list_item, null);
            ImageButton addTheWord = (ImageButton) convertView.findViewById(R.id.addTheWord);
            addTheWord.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    showAddWordDialog(parent.getContext(), position);
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


    private void showAddWordDialog(final Context context, int position) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.add_word, null);
        final EditText editName = (EditText) view.findViewById(R.id.editText_name);
        int lastSelectedListSpinnerID = pref.getInt(myContext.getString(R.string.lastSelectedListSpinnerID),1);
        String listName = context.getString(R.string.defaultDatabase);

        //Only when selection = 0 need to add a new word list, so set it GONE for others.
        if(lastSelectedListSpinnerID == 0){
            editName.setVisibility(View.VISIBLE);
        } else {
            editName.setVisibility(View.GONE);
        }
        final Spinner spinnerTableName = (Spinner) view.findViewById(R.id.spinnerTableName);

        ArrayList<String> tableName = getAllWordListName();
        ArrayAdapter<String> TableNameAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tableName);
        spinnerTableName.setAdapter(TableNameAdapter);
        spinnerTableName.setSelection(lastSelectedListSpinnerID);
        spinnerTableName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("select", String.valueOf(i));
                lastSelectedListSpinnerID = i;
                listName = (String) spinnerTableName.getSelectedItem();
                if(i == 0){
                    editName.setVisibility(View.VISIBLE);
                } else {
                    editName.setVisibility(View.GONE);
                }

                pref.edit().putInt(myContext.getString(R.string.lastSelectedListSpinnerID), i).apply();

                Log.e("pref Written", String.valueOf(pref.getInt(myContext.getString(R.string.lastSelectedListSpinnerID),0)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        dialog.setView(view)
                .setTitle(myContext.getString(R.string.addNewVoc))
                .setPositiveButton(myContext.getString((R.string.confirm)), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (lastSelectedListSpinnerID == 0) {
                            EditText editName = (EditText) view.findViewById(R.id.editText_name);
                            String name = editName.getText().toString();
                            if (!name.isEmpty()) {
                                newWordList(name);

                            } else {
                                //TODO name = now
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd hh:mm");
                                Date date = new Date(System.currentTimeMillis());
                                String sDate = dateFormat.format(date);
                                Log.e("date", sDate);
                                newWordList(sDate);
                            }
                        }
                        //TODO find which to insert and insert

//                                    cv.put("Eng", searchResult.getEng());
//                                    myWord.insert("WordList", null, cv);
//                                    cv.clear();
                    }

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
    private void newWordList(String name){
        SQLiteDatabase db = UserDataHelper.getWritableDB(myContext);
        UserDataHelper.newWordListName(myContext, name);

    }
    private void addWord(Word word, String tableName){
        SQLiteDatabase db = UserDataHelper.getWritableDB(myContext);
        ContentValues cv = new ContentValues();
        cv.put("Eng", word.getEng());
        cv.put("KK", word.getKK());
        cv.put("Chi",word.getChi());
        cv.put("example", word.getExample());

        db.insert(tableName, null, cv);
        cv.clear();
        Toast.makeText(myContext, myContext.getString(R.string.newSuccess), Toast.LENGTH_SHORT).show();
        Log.e("addWord","Success");
    }

}
