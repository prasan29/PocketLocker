package com.locker.tinylocker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.locker.tinylocker.R;

import java.util.ArrayList;

/**
 * Created by Prasanna on 12/24/2016.
 */
public class DetailValuesAdapter extends RecyclerView.Adapter<DetailValuesAdapter.MyViewHolder>
{
    ArrayList<String> detailValues;
    ArrayList<String> detailKeys;

    public DetailValuesAdapter(ArrayList<String> detailValues, ArrayList<String> detailKeys)
    {
        this.detailValues=detailValues;
        this.detailKeys=detailKeys;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_detail_values,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.categoryKey.setText(detailValues.get(position));
        holder.categoryValue.setText(detailKeys.get(position));
    }

    @Override
    public int getItemCount() {
        return detailValues.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView categoryKey;
        private TextView categoryValue;

        public MyViewHolder(View itemView) {
            super(itemView);
            categoryKey = (TextView) itemView.findViewById(R.id.category_value);
            categoryValue = (TextView) itemView.findViewById(R.id.category_key);
        }
    }
}
