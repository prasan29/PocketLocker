package com.locker.tinylocker.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.locker.tinylocker.R;
import com.locker.tinylocker.activity.TheDetailsActivity;
import com.locker.tinylocker.adapter.DetailValuesAdapter;
import com.locker.tinylocker.database.DataBaseHelper;
import com.locker.tinylocker.dialog.EditDetailsDialog;
import com.locker.tinylocker.dialog.PatternDialog;
import com.locker.tinylocker.interfaces.iDeleteEntry;
import com.locker.tinylocker.model.PopulateCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Prasanna on 12/23/2016.
 */
public class FragmentDetailedView extends android.support.v4.app.Fragment implements iDeleteEntry {

    private Activity activity;
    private TextView categoryHeader;
    private TextView uniqueTextView;
    private RelativeLayout relative;
    private String uniqueName;
    private ImageView edit, delete;
    private RecyclerView detailValuesRecycler;
    private String categoryHeaderString;
    private Switch locker;
    private View lockView;

    public FragmentDetailedView() {
    }

    public static FragmentDetailedView newInstance() {
        return new FragmentDetailedView();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_details, container, false);
        uniqueTextView = (TextView) view.findViewById(R.id.fragment_header);
        categoryHeader = (TextView) view.findViewById(R.id.category_name);

        edit = (ImageView) view.findViewById(R.id.edit_entry);
        delete = (ImageView) view.findViewById(R.id.delete_entry);

        locker = (Switch) view.findViewById(R.id.locker);
        lockView = view.findViewById(R.id.lock_view);

        relative = (RelativeLayout) view.findViewById(R.id.relative);

        locker.setChecked(false);

        detailValuesRecycler = (RecyclerView) view.findViewById(R.id.detail_values_recycler);

        listeners();

        uniqueName = getArguments().getString("unique_name");

        if (uniqueName != null) {
            uniqueTextView.setText(uniqueName);
        }

        String details = DataBaseHelper.getInstance().getDETAILS(uniqueName);

        if (details != null) {
            setRecycler(details);
        }

        return view;
    }

    private void listeners() {

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteEntry(uniqueName);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditEntry(uniqueName);
            }
        });

        locker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    lockView.setVisibility(View.VISIBLE);
                    locker.setText("Show    ");
                } else {
                    lockView.setVisibility(View.GONE);
                    locker.setText("Hide     ");
                }
            }
        });

        categoryHeader.setOnClickListener(null);
        uniqueTextView.setOnClickListener(null);
        relative.setOnClickListener(null);
    }

    private void onEditEntry(String uniqueName) {
        EditDetailsDialog eDialog = new EditDetailsDialog(activity, uniqueName, categoryHeaderString);
        eDialog.setCancelable(true);
        eDialog.show();
    }

    private void onDeleteEntry(final String uniqueName) {
        new AlertDialog.Builder(activity)
                .setTitle("Delete")
                .setMessage("Are you sure in deleting this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PatternDialog pDialog = new PatternDialog(activity, FragmentDetailedView.this);
                        pDialog.setCancelable(true);
                        pDialog.show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void setRecycler(String details) {

        String detailsArray[] = details.split(",");

        categoryHeaderString = detailsArray[0];

        categoryHeader.setText(detailsArray[0]);

        ArrayList<String> detailValues = new ArrayList<>();

        detailValues.addAll(Arrays.asList(detailsArray).subList(1, detailsArray.length));

        HashMap<String, ArrayList<String>> wholeData = PopulateCategory.getInstance().getPopulatedMap();
        ArrayList<String> detailsKeys = wholeData.get(detailsArray[0]);

        DetailValuesAdapter adapter = new DetailValuesAdapter(detailValues, detailsKeys);
        LinearLayoutManager layout = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        detailValuesRecycler.setLayoutManager(layout);
        detailValuesRecycler.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Pocket locker");
        }
        activity.getWindow().findViewById(R.id.add_details).setVisibility(View.VISIBLE);
    }

    @Override
    public void deleteEntry() {

        DataBaseHelper.getInstance().deleteEntry(uniqueName);
        Toast.makeText(activity, "Deleted successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(activity, TheDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }
}