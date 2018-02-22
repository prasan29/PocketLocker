package com.locker.tinylocker.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.locker.tinylocker.R;
import com.locker.tinylocker.adapter.HomeViewAdapter;
import com.locker.tinylocker.database.DataBaseHelper;
import com.locker.tinylocker.dialog.AddDetailsDialog;
import com.locker.tinylocker.dialog.ResetPatternDialog;
import com.locker.tinylocker.fragment.FragmentDetailedView;
import com.locker.tinylocker.interfaces.iDisplayDetails;
import com.locker.tinylocker.interfaces.iUpdateList;
import com.locker.tinylocker.model.SharedPref;
import com.locker.tinylocker.model.Utility;

import java.util.List;

/**
 * Created by Prasanna on 12/19/2016.
 */
public class TheDetailsActivity extends AppCompatActivity implements iUpdateList, iDisplayDetails {

    List<String> uniqueNames;
    private FloatingActionButton addDetails;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_details_activity);

        init();

        gatherData();

//        skyGear();
    }

    private void skyGear() {

        pDialog.show();

        String pattern = new SharedPref(TheDetailsActivity.this).getPattern();

        /*SkyGearOperations.getInstance(TheDetailsActivity.this).setRecord("pattern", pattern);

        SkyGearOperations.getInstance(TheDetailsActivity.this).saveToSkyGear();*/

        pDialog.dismiss();
    }

    private void gatherData() {
        uniqueNames = DataBaseHelper.getInstance().getUniqueNames();

        if (uniqueNames != null && uniqueNames.size() > 0) {
            (findViewById(R.id.empty_view)).setVisibility(View.GONE);
            setHomeRecycler();
        } else {
            (findViewById(R.id.empty_view)).setVisibility(View.VISIBLE);
            (findViewById(R.id.add_details)).setVisibility(View.GONE);
        }
    }

    private void init() {

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        pDialog = Utility.getInstance(this).onProgressDialog();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Pocket Locker");
        }

        addDetails = (FloatingActionButton) findViewById(R.id.add_details);

        addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDetailsDialogShow();
            }
        });

        if ((findViewById(R.id.empty_view)) != null) {
            (findViewById(R.id.empty_view)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDetailsDialogShow();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inflate_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.forgot_password) {
            resetDialogShow();
        } else if (id == R.id.logout) {

            new AlertDialog.Builder(this)
                    .setTitle("Exit application")
                    .setMessage("Do you want to lock and exit Pocket Locker?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .setCancelable(true)
                    .setIcon(R.mipmap.close_dialog)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void resetDialogShow() {
        ResetPatternDialog resetPatternDialog = new ResetPatternDialog(TheDetailsActivity.this);
        resetPatternDialog.setCancelable(true);
        resetPatternDialog.show();
    }

    public void addDetailsDialogShow() {
        AddDetailsDialog addDialog = new AddDetailsDialog(TheDetailsActivity.this);
        addDialog.setCancelable(true);
        addDialog.show();
    }

    @Override
    public void updateList() {
        uniqueNames = DataBaseHelper.getInstance().getUniqueNames();
        if (uniqueNames != null) {
            (findViewById(R.id.empty_view)).setVisibility(View.GONE);
            (findViewById(R.id.add_details)).setVisibility(View.VISIBLE);

            setHomeRecycler();
        } else {
            (findViewById(R.id.empty_view)).setVisibility(View.VISIBLE);
            (findViewById(R.id.add_details)).setVisibility(View.GONE);
        }
    }

    public void setHomeRecycler() {
        RecyclerView homeRecycler = (RecyclerView) findViewById(R.id.home_recycler);
        homeRecycler.setVisibility(View.VISIBLE);
        HomeViewAdapter adapter = new HomeViewAdapter(uniqueNames, this);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        homeRecycler.setLayoutManager(layout);
        homeRecycler.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        Log.e("popBack", "" + count);

        if (count == 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Exit application")
                    .setMessage("Do you want to lock and exit Pocket Locker?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .setCancelable(true)
                    .setIcon(R.mipmap.close_dialog)
                    .show();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void displayDetails(String clickedItem, int position) {
        addDetails.setVisibility(View.GONE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        callDetailsFragment(position);
    }

    public void callDetailsFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("unique_name", uniqueNames.get(position));
        FragmentDetailedView fragment = FragmentDetailedView.newInstance();
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.root_layout, fragment, "FragmentDetailedView")
                .addToBackStack("FragmentDetailedView")
                .commit();
    }
}