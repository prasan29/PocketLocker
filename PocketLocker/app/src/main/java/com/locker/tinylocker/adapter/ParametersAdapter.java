package com.locker.tinylocker.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.locker.tinylocker.R;
import com.locker.tinylocker.dialog.AddDetailsDialog;

import java.util.ArrayList;

/**
 * Created by Prasanna on 12/21/2016.
 */
public class ParametersAdapter extends RecyclerView.Adapter<ParametersAdapter.MyViewHolder> {

    private ArrayList<String> paramArray;

    public ParametersAdapter(ArrayList<String> paramArray) {
        this.paramArray = paramArray;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_params, parent, false);
        return new MyViewHolder(view, new CustomEtListener());
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

//        holder.paramName.setText(paramArray.get(position));
        holder.paramEditText.setHint(Html.fromHtml("<font size=\"14\">"+"Please enter a valid "+paramArray.get(position)+"</font>"));

        holder.myCustomEditTextListener.updatePosition(position);
    }

    @Override
    public int getItemCount() {
        return paramArray.size();
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
            AddDetailsDialog.detailsFromAdapter[position] = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}