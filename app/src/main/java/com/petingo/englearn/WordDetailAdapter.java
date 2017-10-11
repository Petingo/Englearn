package com.petingo.englearn;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by petingo on 2017/9/20.
 */

public class WordDetailAdapter extends RecyclerView.Adapter<WordDetailAdapter.ViewHolder>{
    private List<Pair<String,String >> mData;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView content;
        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.word_detail_item_title);
            content = (TextView) v.findViewById(R.id.word_detail_item_content);
        }
    }

    public WordDetailAdapter(List<Pair<String,String >> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_detail_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(mData.get(position).first);
        holder.content.setText(mData.get(position).second);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
