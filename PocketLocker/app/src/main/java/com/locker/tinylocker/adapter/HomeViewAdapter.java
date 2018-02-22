package com.locker.tinylocker.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.locker.tinylocker.R;
import com.locker.tinylocker.interfaces.iDisplayDetails;

import java.util.List;

/**
 * Created by Prasanna on 12/23/2016.
 */
public class HomeViewAdapter extends RecyclerView.Adapter<HomeViewAdapter.MyViewHolder> {

    private List<String> uniqueNames;
    private Activity activity;
    private iDisplayDetails displayDetails;

    public HomeViewAdapter(List<String> uniqueNames, Activity activity)
    {
        this.uniqueNames=uniqueNames;
        this.activity=activity;
        this.displayDetails=(iDisplayDetails) activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_home_details,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.uniqueName.setText(uniqueNames.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDetails.displayDetails(uniqueNames.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uniqueNames.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView uniqueName;
        public MyViewHolder(View itemView) {
            super(itemView);
            uniqueName=(TextView) itemView.findViewById(R.id.unique_name);
        }
    }
}