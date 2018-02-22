package com.locker.tinylocker.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.locker.tinylocker.R;
import com.locker.tinylocker.activity.TheDetailsActivity;
import com.locker.tinylocker.adapter.EditParamAdapter;
import com.locker.tinylocker.database.DataBaseHelper;
import com.locker.tinylocker.interfaces.iUpdateList;
import com.locker.tinylocker.model.PopulateCategory;
import com.locker.tinylocker.model.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Prasanna on 12/19/2016.
 */
public class EditDetailsDialog extends Dialog {

    public static String[] detailsFromAdapter;
    private Activity activity;
    private Button submit;
    private HashMap<String, ArrayList<String>> wholePopulated;
    private String selectedCategory;

    private String uniqueNameString;
    private EditText uniqueName;
    private iUpdateList updateList;

    public EditDetailsDialog(Activity context, String uniqueNameString, String selectedCategory) {
        super(context);
        this.activity = context;
        this.selectedCategory = selectedCategory;
        this.uniqueNameString = uniqueNameString;
        this.updateList = (iUpdateList) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_items_dialog);


        wholePopulated = PopulateCategory.getInstance().getPopulatedMap();

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        init();

        listener();

    }

    private void listener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
            }
        });

        uniqueName.setText(uniqueNameString);
        uniqueName.setClickable(false);
        uniqueName.setFocusable(false);

        setSpinnerAdapter();
    }

    private void getValue() {
        if (detailsFromAdapter != null) {
            int size = 0;
            String s = selectedCategory + ",";
            for (int i = 0; i < detailsFromAdapter.length; i++) {
                if (detailsFromAdapter[i] != null) {
                    String aDetailsFromAdapter = detailsFromAdapter[i].trim();
                    if (!aDetailsFromAdapter.trim().equals("")) {
                        if (i == detailsFromAdapter.length - 1) {
                            s += aDetailsFromAdapter;
                        } else {
                            s += aDetailsFromAdapter + ",";
                        }
                        size++;
                    }
                }
            }

            Log.e("lengthArray", "array " + detailsFromAdapter.length);

            if (detailsFromAdapter.length > 0) {
                if (size == detailsFromAdapter.length) {
                    updateIntoDB(s);
                } else {
                    Utility.getInstance(activity).showSnackBar(getWindow().findViewById(android.R.id.content), "None of the fields can be empty!");
                }
            }
        }
    }

    private void updateIntoDB(String s) {
        DataBaseHelper.getInstance().updateDetails(uniqueNameString, s);

        Toast.makeText(activity, "Updated successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(activity, TheDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    private void init() {
        submit = (Button) findViewById(R.id.submit_button);
        uniqueName = (EditText) findViewById(R.id.unique_name_edit_text);
    }

    private void setSpinnerAdapter() {
        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        categorySpinner.setClickable(false);
        categorySpinner.setFocusable(false);

        List<String> selectedSpinner = new ArrayList<>();
        selectedSpinner.add(selectedCategory);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, selectedSpinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter);

        setRecyclerParams(selectedCategory);
    }

    private void setRecyclerParams(String selectedItem) {
        if (wholePopulated.get(selectedItem.trim()) != null) {

            ArrayList<String> arr = wholePopulated.get(selectedItem.trim());

            detailsFromAdapter = new String[arr.size()];

            (findViewById(R.id.unique_layout)).setVisibility(View.VISIBLE);

            String[] detailParams=DataBaseHelper.getInstance().getDETAILS(uniqueNameString).split(",");

            System.arraycopy(detailParams,1,detailsFromAdapter,0,arr.size());

            ArrayList<String> detailValuesList=new ArrayList<>();

            detailValuesList.addAll(Arrays.asList(detailParams).subList(1,detailParams.length));

            RecyclerView paramRecycler = (RecyclerView) findViewById(R.id.param_recycler);
            EditParamAdapter adapter=new EditParamAdapter(detailValuesList);
            LinearLayoutManager layout = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            paramRecycler.setLayoutManager(layout);
            paramRecycler.setAdapter(adapter);
        } else {
            (findViewById(R.id.unique_layout)).setVisibility(View.GONE);
            detailsFromAdapter = new String[0];
            RecyclerView paramRecycler = (RecyclerView) findViewById(R.id.param_recycler);
            paramRecycler.setAdapter(null);
        }
    }
}