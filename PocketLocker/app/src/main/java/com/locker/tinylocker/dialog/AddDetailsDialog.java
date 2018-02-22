package com.locker.tinylocker.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.locker.tinylocker.R;
import com.locker.tinylocker.adapter.ParametersAdapter;
import com.locker.tinylocker.database.DataBaseHelper;
import com.locker.tinylocker.interfaces.iUpdateList;
import com.locker.tinylocker.model.PopulateCategory;
import com.locker.tinylocker.model.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Prasanna on 12/19/2016.
 */
public class AddDetailsDialog extends Dialog {

    public static String[] detailsFromAdapter;
    private Activity activity;
    private Button submit;
    private HashMap<String, ArrayList<String>> wholePopulated;
    private String selectedCategory;
    private EditText uniqueName;
    private iUpdateList updateList;

    public AddDetailsDialog(Activity context) {
        super(context);
        this.activity = context;
        this.updateList = (iUpdateList) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_items_dialog);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        init();

        listener();

        wholePopulated = PopulateCategory.getInstance().getPopulatedMap();
    }

    private void listener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
            }
        });

        uniqueName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    dismissKeyboard();
                }
                return false;
            }
        });
    }

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != this.getCurrentFocus())
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getApplicationWindowToken(), 0);
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

            Log.e("lengthArray","array "+detailsFromAdapter.length);
            if(detailsFromAdapter.length==0)
            {
                Utility.getInstance(activity).showSnackBar(getWindow().findViewById(android.R.id.content),"Please choose a category!");
            }
            else
            {
                if(!uniqueName.getText().toString().trim().equals(""))
                {
                    if(size==detailsFromAdapter.length)
                    {
                        saveIntoDB(s);
                    }
                    else
                    {
                        Utility.getInstance(activity).showSnackBar(getWindow().findViewById(android.R.id.content),"None of the fields can be empty!");
                    }
                }
                else
                {
                    Utility.getInstance(activity).showSnackBar(getWindow().findViewById(android.R.id.content),"Sorry! Unique name cannot be empty!");
                }
            }
        }
    }

    private void saveIntoDB(String s) {
        List<String> savedUniqueNames = DataBaseHelper.getInstance().getUniqueNames();
        String uniqueNameString = uniqueName.getText().toString().trim();
        boolean isUnique = true;
        for (int i = 0; i < savedUniqueNames.size(); i++) {
            if (savedUniqueNames.get(i).equals(uniqueNameString)) {
                isUnique = false;
                break;
            }
        }
        if (isUnique) {
            DataBaseHelper.getInstance().insertDetails(uniqueNameString, s);

            String gotDetails = DataBaseHelper.getInstance().getDETAILS(uniqueNameString);
            Utility.getInstance(activity).showSnackBar(activity.findViewById(android.R.id.content),"Saved successfully!");
            Log.e("GOT DETAILS", gotDetails);
            updateList.updateList();
            dismiss();
        } else {
            Utility.getInstance(activity).showSnackBar(getWindow().findViewById(android.R.id.content),"Unique name should be unique among your data!");
        }
    }

    private void init() {
        submit = (Button) findViewById(R.id.submit_button);
        uniqueName = (EditText) findViewById(R.id.unique_name_edit_text);

        setSpinnerAdapter();
    }

    private void setSpinnerAdapter() {
        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, populateCategoryList());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                setRecyclerParams(parent.getItemAtPosition(position).toString());
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setRecyclerParams(String selectedItem) {
        if (wholePopulated.get(selectedItem.trim()) != null) {

            ArrayList<String> arr = wholePopulated.get(selectedItem.trim());

            detailsFromAdapter = new String[arr.size()];

            (findViewById(R.id.unique_layout)).setVisibility(View.VISIBLE);

            RecyclerView paramRecycler = (RecyclerView) findViewById(R.id.param_recycler);
            ParametersAdapter adapter = new ParametersAdapter(wholePopulated.get(selectedItem.trim()));
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

    private List<String> populateCategoryList() {

        HashMap<String, ArrayList<String>> h;
        List<String> category = new ArrayList<>();
        category.add("Tap to choose...");
        category.add("ATM card");
        category.add("Internet banking");
        category.add("Adhaar card");
        category.add("Passport");
        category.add("PAN card");
        category.add("Driving licence");
        category.add("Home computer");
        category.add("Home laptop");
        category.add("Office computer");
        category.add("Office laptop");
        category.add("Email");
        category.add("IRCTC");
        category.add("Facebook");
        category.add("Skype");
        category.add("Gmail");
        category.add("Broadband");

        category.add("Other login");
        return category;
    }
}