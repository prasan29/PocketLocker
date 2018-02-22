package com.locker.tinylocker.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.locker.tinylocker.R;
import com.locker.tinylocker.dialog.EditDetailsDialog;

import java.util.ArrayList;

/**
 * Created by Prasanna on 12/26/2016.
 */
public class EditParamAdapter extends RecyclerView.Adapter<EditParamAdapter.MyViewHolder>{

    private ArrayList<String> paramValues;

    public EditParamAdapter(ArrayList<String> paramValues)
    {
        this.paramValues=paramValues;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_params, parent, false);

        return new MyViewHolder(view,new CustomEtListener());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.paramEditText.setText(paramValues.get(position));

        holder.myCustomEditTextListener.updatePosition(position);

//        EditDetailsDialog.detailsFromAdapter[position] = paramValues.get(position);
    }

    @Override
    public int getItemCount() {
        return paramValues.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomEtListener myCustomEditTextListener;
        //        private TextView paramName;
        private EditText paramEditText;

        public MyViewHolder(View itemView, CustomEtListener listener) {
            super(itemView);

//            paramName = (TextView) itemView.findViewById(R.id.param_name);
            paramEditText = (EditText) itemView.findViewById(R.id.param_edit);
            myCustomEditTextListener = listener;

            paramEditText.addTextChangedListener(myCustomEditTextListener);
        }
    }

    private class CustomEtListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            EditDetailsDialog.detailsFromAdapter[position] = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}