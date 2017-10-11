package com.petingo.englearn;

import android.annotation.SuppressLint;
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
import android.widget.Button;
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
    private ArrayList<String> name = new ArrayList<>();
    private SharedPreferences pref;
    private ArrayList<String> tableName;

    MyAdapter(Context c, List<Word> searchResult) {
        myInflater = LayoutInflater.from(c);
        myContext = c;
        pref = myContext.getSharedPreferences(myContext.getPackageName(), Context.MODE_PRIVATE);
        this.searchResults = searchResult;
        tableName = getAllWordListName();
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
        TextView txtWord;
        TextView txtTrans;

        ViewHolder(TextView txtWord, TextView txtTrans) {
            this.txtWord = txtWord;
            this.txtTrans = txtTrans;
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
                    (TextView) convertView.findViewById(R.id.resultWord),
                    (TextView) convertView.findViewById(R.id.resultTrans)
            );
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtWord.setText(searchResult.getWord());
        holder.txtTrans.setText(searchResult.getTranslation().replace("\\n","；"));
        return convertView;
    }


    private void showAddWordDialog(final Context context, final int position) {
        final LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addWordDialogView = inflater.inflate(R.layout.add_word, null);

        final Spinner spinnerTableName = (Spinner) addWordDialogView.findViewById(R.id.spinnerTableName);
        final int[] lastSelectedListSpinnerID = {pref.getInt(myContext.getString(R.string.lastSelectedListSpinnerID), 0)};
        ArrayAdapter<String> TableNameAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tableName);
        spinnerTableName.setAdapter(TableNameAdapter);
        spinnerTableName.setSelection(lastSelectedListSpinnerID[0]);
        spinnerTableName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("select", String.valueOf(i));
                lastSelectedListSpinnerID[0] = i;
                pref.edit().putInt(myContext.getString(R.string.lastSelectedListSpinnerID), i).apply();

                Log.e("pref Written", String.valueOf(pref.getInt(myContext.getString(R.string.lastSelectedListSpinnerID), 0)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        final Button newWordList = (Button) addWordDialogView.findViewById(R.id.newWordList);
        newWordList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View newWordListView = inflater.inflate(R.layout.new_word_list, null);
                newWordListDialog(newWordListView).show();
            }
        });

        addWordDialog(addWordDialogView, position, spinnerTableName).show();
    }

    private AlertDialog.Builder newWordListDialog(final View view){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(myContext);
        dialog.setView(view)
                .setTitle(myContext.getString(R.string.newWordList))
                .setNegativeButton(myContext.getString(R.string.cancel), null)
                .setPositiveButton(myContext.getString((R.string.confirm)), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText wordListName = (EditText) view.findViewById(R.id.newWordListName);
                        String newName = wordListName.getText().toString();
                        if (newName.isEmpty()) {
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd hh:mm:ss");
                            Date date = new Date(System.currentTimeMillis());
                            newName = dateFormat.format(date);
                        }
                        newWordList(newName);
                        tableName.add(newName);
                        Log.e("New Name", wordListName.getText().toString());
                    }
                });
        return dialog;
    }
    private AlertDialog.Builder addWordDialog(View view, final int position, final Spinner spinnerTableName) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(myContext);
        dialog.setView(view)
                .setTitle(myContext.getString(R.string.addNewVoc))
                .setNegativeButton(myContext.getString(R.string.cancel), null)
                .setPositiveButton(myContext.getString((R.string.confirm)), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addWord((Word) getItem(position), (String) spinnerTableName.getSelectedItem());
                    }
                });
        return dialog;
    }

    private ArrayList<String> getAllWordListName() {
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

    private void newWordList(String name) {
        SQLiteDatabase db = UserDataHelper.getWritableDB(myContext);
        UserDataHelper.newWordListName(myContext, name);
    }

    private void addWord(Word word, String selectedListName) {
        SQLiteDatabase db = UserDataHelper.getWritableDB(myContext);
        ContentValues cv = new ContentValues();
        //TODO complete addWord
        cv.put("example", word.getExample());
        cv.put("listName", selectedListName);

        db.insert("WordList", null, cv);
        cv.clear();
        Toast.makeText(myContext, myContext.getString(R.string.newSuccess), Toast.LENGTH_SHORT).show();
        Log.e("addWord", "Success");
    }

}
