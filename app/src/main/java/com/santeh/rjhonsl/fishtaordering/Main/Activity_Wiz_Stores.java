package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;
import com.santeh.rjhonsl.fishtaordering.Util.Keys;
import com.santeh.rjhonsl.fishtaordering.Util.VarFishtaOrdering;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjhonsl on 6/3/2016.
 */
public class Activity_Wiz_Stores extends AppCompatActivity {

    DBaseQuery db;
    Activity activity;
    Context context;
    Intent receivedIntent;
    ListView lvStores;
    ArrayAdapter<String> adapter;
    List<VarFishtaOrdering> storeList = new ArrayList<>();
    List<VarFishtaOrdering> selectedstoreList = new ArrayList<>();
    String allitems = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_stores);

        activity = this;
        context = Activity_Wiz_Stores.this;

        receivedIntent = getIntent();

        db = new DBaseQuery(this);
        db.open();

        lvStores = (ListView) findViewById(R.id.rvStores);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        assert myToolbar != null;
        myToolbar.setBackgroundColor(getResources().getColor(R.color.orange_fishta));
        setSupportActionBar(myToolbar);
        storeList = db.getCust_OUTLET();

        if (!receivedIntent.hasExtra(Keys.IS_STARTUP)){
            selectedstoreList = db.getFilteredStores(db.getKeyVal(Keys.SETTINGS_STOREIDS));
            final String[] storeNames = new String[selectedstoreList.size()];
            final String[] storeID = new String[selectedstoreList.size()];

            for (int i = 0; i < selectedstoreList.size(); i++) {
                storeNames[i] = storeList.get(i).getCust_name();
                storeID[i] = storeList.get(i).getCust_id();
            }

            adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, storeNames);
            lvStores.setAdapter(adapter);
        }

        Button btnSetStore = (Button) findViewById(R.id.btnSetSTore);
        btnSetStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!receivedIntent.hasExtra(Keys.IS_STARTUP)){
                    if (allitems.equalsIgnoreCase("")){
                        Helper.toast.short_(activity, "No changes has been made.");
                    }else{
                        db.updateKeyVal(Keys.SETTINGS_STOREIDS, allitems);
                        finish();
                    }
                }else{
                    if (lvStores.getCount()>0){
                        db.insertSettings(
                                receivedIntent.getStringExtra(Keys.SETTINGS_USERNAME),
                                receivedIntent.getStringExtra(Keys.SETTINGS_SERVERNUMBER),
                                allitems,
                                "1425"
                        );
                        db.insertKeyValSettings(Keys.SETTINGS_STOREIDS, allitems);
                        Intent intent = new Intent(activity, Activity_SetupSuccess.class);
                        startActivity(intent);
                    }else{
                        Helper.toast.short_(activity, "No stores to set");
                    }
                }
            }
        });



//        Helper.toast.indefinite(activity, storeList.size()+"");


        ActionBar ab = getSupportActionBar();
        assert ab != null;
//        ab.setDefaultDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.show();





//
//        btnSetStoreName.setEnabled(false);
//
//        if (!receivedIntent.hasExtra(Keys.IS_STARTUP)){
//            edtStorename.setText(db.getUserName());
//        }
//
//
//        btnSetStoreName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (receivedIntent.hasExtra(Keys.IS_STARTUP)){
//                    db.insertSettings(edtStorename.getText().toString(), receivedIntent.getStringExtra(Keys.SETTINGS_SERVERNUMBER), "1", "123456");
//                    Intent inte = new Intent(activity, Activity_SetupSuccess.class);
////                    inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                    startActivity(inte, ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
//                }else{
//                    db.updateSettingsStoreName(edtStorename.getText().toString());
//                    finish();
//                }
//            }
//        });
//
//
//        edtStorename.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (edtStorename.getText().toString().length() > 0){
//                    btnSetStoreName.setEnabled(true);
//                }else{
//                    btnSetStoreName.setEnabled(false);
//                }
//            }
//        });
    }

    private void toastingNumAndUsername(String str) {
        Helper.toast.indefinite(activity,
                receivedIntent.getStringExtra(Keys.SETTINGS_SERVERNUMBER)+ " " + receivedIntent.getStringExtra(Keys.SETTINGS_USERNAME)+" "+ str
            );
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);

        final MenuItem actionFilter = menu.findItem(R.id.action_add);
        actionFilter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                 menuItem.collapseActionView();

                final String[] storeNames = new String[storeList.size()];
                final String[] storeID = new String[storeList.size()];
                final ArrayList seletedItems=new ArrayList();

                for (int i = 0; i < storeList.size(); i++) {
                    storeNames[i] = storeList.get(i).getCust_name();
                    storeID[i] = storeList.get(i).getCust_id();
                }


                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("CATEGORIES")
                        .setMultiChoiceItems(storeNames, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                if (isChecked) {
                                    seletedItems.add(indexSelected);
                                } else if (seletedItems.contains(indexSelected)) {
                                    seletedItems.remove(Integer.valueOf(indexSelected));
                                }
                            }
                        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                String[] selectedStores = new String[seletedItems.size()];
                                for (int i = 0; i < seletedItems.size(); i++) {

                                    selectedStores[i] = storeNames[Integer.valueOf(seletedItems.get(i)+"")];
                                    if (i > 0){
                                        allitems = allitems + "," + storeID[Integer.valueOf(seletedItems.get(i)+"")];
                                    }else{
                                        allitems = storeID[Integer.valueOf(seletedItems.get(i)+"")];
                                    }


                                }

//                                Helper.toast.indefinite(activity, allitems);

                                adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, selectedStores);
                                lvStores.setAdapter(adapter);

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //  Your code when user clicked on Cancel

                            }
                        }).create();
                dialog.show();

                return false;
            }
        });


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
    }
}
