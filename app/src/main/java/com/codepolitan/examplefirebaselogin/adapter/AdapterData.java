package com.codepolitan.examplefirebaselogin.adapter;

import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepolitan.examplefirebaselogin.R;
import com.codepolitan.examplefirebaselogin.model.Data;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 03/03/17.
 */

public class AdapterData extends RecyclerView.Adapter<AdapterData.ViewHolder> {

    private List<Data> listData;

    public AdapterData(List<Data> listData) {
        this.listData = listData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Data data = listData.get(position);
        holder.textViewDateTime.setText(data.getDatetime());
        holder.textViewTitle.setText(data.getTitle());
        holder.textViewContent.setText(data.getContent());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_date_time_item_data)
        TextView textViewDateTime;
        @BindView(R.id.text_view_title_item_data)
        TextView textViewTitle;
        @BindView(R.id.text_view_content_item_data)
        TextView textViewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
