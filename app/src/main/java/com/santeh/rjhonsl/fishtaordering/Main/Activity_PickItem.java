package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;
import com.santeh.rjhonsl.fishtaordering.Util.VarFishtaOrdering;

import java.util.List;

/**
 * Created by rjhonsl on 5/20/2016.
 */
public class Activity_PickItem extends AppCompatActivity {

    Activity activity;
    Context context;
    ListView lvItems, lvFavorites;
    DBaseQuery db;
    ArrayAdapter<String> adapter, favAdapter;
    FloatingActionButton btnShowFav;
    BottomSheetBehavior mBottomSheetBehavior;
    boolean isBOttomShow = false;

    String[] itemsArray;
    List<VarFishtaOrdering> itemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickitem);

        activity = this;
        context = Activity_PickItem.this;

        db = new DBaseQuery(this);
        db.open();

        lvItems = (ListView) findViewById(R.id.lvFishes);
        lvFavorites = (ListView) findViewById(R.id.lvFavorites);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        assert myToolbar != null;
        myToolbar.setBackgroundColor(getResources().getColor(R.color.orange_fishta));
        setSupportActionBar(myToolbar);
        myToolbar.inflateMenu(R.menu.menu_search);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
//        ab.setDefaultDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.show();
        final View bottomSheet = findViewById( R.id.bottom_sheet );
        btnShowFav = (FloatingActionButton) findViewById(R.id.btnFav);



        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(Helper.convert.dpToPixels(context, 35));
        mBottomSheetBehavior.setHideable(false);


        itemList = db.getAllItems();
        final List<VarFishtaOrdering> favList = db.getTopTenFav();

        itemsArray = new String[itemList.size()];
        final String[] favesArray = new String[favList.size()];

        for (int i = 0; i < itemList.size(); i++) {
            itemsArray[i] = itemList.get(i).getItem_description();
        }

        for (int i = 0; i < favList.size(); i++) {
            favesArray[i] = favList.get(i).getItem_description();
        }

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING && isBOttomShow){
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    isBOttomShow = true;
                    btnShowFav.setImageResource(R.drawable.ic_arrow_downward_white_24dp);
                    mBottomSheetBehavior.setHideable(false);
                }

                if (newState == BottomSheetBehavior.STATE_COLLAPSED){
                    Log.d("BOTTOM", "collapsed");
                    isBOttomShow = false;
                    btnShowFav.setImageResource(R.drawable.ic_arrow_upward_white_24dp);
                }else if (newState == BottomSheetBehavior.STATE_DRAGGING){
                    Log.d("BOTTOM", "dragging");
                }else if (newState == BottomSheetBehavior.STATE_EXPANDED){
                    Log.d("BOTTOM", "expanded");
                    btnShowFav.setImageResource(R.drawable.ic_arrow_downward_white_24dp);
                    isBOttomShow = true;
                }else if (newState == BottomSheetBehavior.STATE_HIDDEN){
                    Log.d("BOTTOM", "hidden");
                }else if (newState == BottomSheetBehavior.STATE_SETTLING){
                    Log.d("BOTTOM", "settling");
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        btnShowFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBottomSheet();

            }
        });

        if (favesArray.length > 0){
            isBOttomShow =false;
        }else{
            isBOttomShow =true;
        }

        toggleBottomSheet();


        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, itemsArray);
        favAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, favesArray);

        lvItems.setAdapter(adapter);
        lvFavorites.setAdapter(favAdapter);

        lvFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final String units = itemList.get(i).getItem_units();
                final String[] pax = units.split(",");

                final Dialog d = Helper.dialogBox.numberAndPAXpicker(activity, favesArray[i], 1, 1000, pax);
                Button btnOk = (Button) d.findViewById(R.id.btn_numberAndPaxPicekr_set);
                final NumberPicker value = (NumberPicker) d.findViewById(R.id.dialog_numandpax_value);
                final NumberPicker pax1 = (NumberPicker) d.findViewById(R.id.dialog_numandpax_pax);
                pax1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                value.clearFocus();
                pax1.clearFocus();

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent returnIntent = new Intent();
                        value.clearFocus();
                        returnIntent.putExtra("qty",  value.getValue()+"");
                        returnIntent.putExtra("item",  favesArray[i]+"");
                        returnIntent.putExtra("pax", pax[pax1.getValue()]+"");
                        returnIntent.putExtra("units", units);
                        returnIntent.putExtra("code", itemList.get(i).getItem_code()+"");
                        d.dismiss();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });

            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String units = itemList.get(position).getItem_units();
                final String[] pax = units.split(",");

                final Dialog d = Helper.dialogBox.numberAndPAXpicker(activity, itemsArray[position], 1, 1000, pax);
                Button btnOk = (Button) d.findViewById(R.id.btn_numberAndPaxPicekr_set);
                final NumberPicker value = (NumberPicker) d.findViewById(R.id.dialog_numandpax_value);
                final NumberPicker pax1 = (NumberPicker) d.findViewById(R.id.dialog_numandpax_pax);
                pax1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                value.clearFocus();
                pax1.clearFocus();

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent returnIntent = new Intent();
                        value.clearFocus();
                        returnIntent.putExtra("qty",  value.getValue()+"");
                        returnIntent.putExtra("item",  itemsArray[position]+"");
                        returnIntent.putExtra("pax", pax[pax1.getValue()]+"");
                        returnIntent.putExtra("units", units);
                        returnIntent.putExtra("code", itemList.get(position).getItem_code()+"");
                        d.dismiss();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });
            }
        });

    }

    private void toggleBottomSheet() {
        if (!isBOttomShow){
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            btnShowFav.setImageResource(R.drawable.ic_arrow_downward_white_24dp);
            isBOttomShow = true;
        }else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            isBOttomShow = false;
            btnShowFav.setImageResource(R.drawable.ic_arrow_upward_white_24dp);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

            // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                itemList = db.getAllItems();
                itemsArray = new String[itemList.size()];

                for (int i = 0; i < itemList.size(); i++) {
                    itemsArray[i] = itemList.get(i).getItem_description();
                }

                adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, itemsArray);
                lvItems.setAdapter(adapter);
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                isBOttomShow = true;
                toggleBottomSheet();
                return true;  // Return true to expand action view
            }

        };

        // Get the MenuItem for the action item
        final MenuItem actionMenuItem = menu.findItem(R.id.action_search);
        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(actionMenuItem, expandListener);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(actionMenuItem);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                itemList = db.getSearchItems(query);
                itemsArray = new String[itemList.size()];

                for (int i = 0; i < itemList.size(); i++) {
                    itemsArray[i] = itemList.get(i).getItem_description();
                }

                adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, itemsArray);
                lvItems.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
    protected void onResume() {
        super.onResume();
        db.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }
}
