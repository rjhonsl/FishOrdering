package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.santeh.rjhonsl.fishtaordering.Adapter.ItemsAdapter;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;
import com.santeh.rjhonsl.fishtaordering.Util.Keys;
import com.santeh.rjhonsl.fishtaordering.Util.SendSMS;
import com.santeh.rjhonsl.fishtaordering.Util.VarFishtaOrdering;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

public class MainActivity extends AppCompatActivity {

    DBaseQuery db;
    FloatingActionButton btnAddITem;
    static FloatingActionButton btnSendOrder;
    Activity activity;
    Context context;
    Button btnSelectStore;
    public static int INTENT_SELECT_ITEM = 0;
    public static ArrayList<VarFishtaOrdering> orderList = new ArrayList<>();

    RecyclerView rvItems;
    Toolbar myToolbar;
    public static ItemsAdapter itemsViewAdapter;
    LinearLayoutManager mLayoutManager;
    static LinearLayout imgNoItems;
    List<VarFishtaOrdering> storeList = new ArrayList<>();
    String selectedStoreID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        context = MainActivity.this;

        db = new DBaseQuery(this);
        db.open();

        btnSelectStore = (Button) findViewById(R.id.btnSelectedStores);
        myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        assert myToolbar != null;
        myToolbar.setBackgroundColor(getResources().getColor(R.color.orange_fishta));
        setSupportActionBar(myToolbar);
//        myToolbar.inflateMenu(R.menu.menu_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        assert rvItems != null;
        rvItems.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(mLayoutManager);
        rvItems.setItemAnimator(new FadeInRightAnimator(new OvershootInterpolator(2f)));
        registerForContextMenu(rvItems);

        itemsViewAdapter = new ItemsAdapter(orderList, context, activity);
        rvItems.setAdapter(itemsViewAdapter);
        itemsViewAdapter.notifyDataSetChanged();


        imgNoItems = (LinearLayout) findViewById(R.id.img_noitems);
        showNoItemImage();



        storeList = db.getFilteredStores(db.getKeyVal(Keys.SETTINGS_STOREIDS));
        if (storeList.size()<2){
            selectedStoreID = storeList.get(0).getCust_id();
            btnSelectStore.setText(storeList.get(0).getCust_name());
        }
        btnSelectStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String[] storeNames = new String[storeList.size()];
                final String[] storeID = new String[storeList.size()];

                for (int i = 0; i < storeList.size(); i++) {
                    storeNames[i] = storeList.get(i).getCust_name();
                    storeID[i] = storeList.get(i).getCust_id();
                }

                final Dialog d = Helper.dialogBox.list(activity, storeNames, "STORES", R.color.orange_fishta_darker);
                ListView lvStore = (ListView) d.findViewById(R.id.dialog_list_listview);
                lvStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        btnSelectStore.setText(storeNames[i]);
                        selectedStoreID = storeID[i];

                        d.dismiss();
                    }
                });
//                AlertDialog dialog = new AlertDialog.Builder(context)
//                        .setTitle("CATEGORIES")
//                        .setMultiChoiceItems(storeNames, null, new DialogInterface.OnMultiChoiceClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
//                                if (isChecked) {
//                                    seletedItems.add(indexSelected);
//                                } else if (seletedItems.contains(indexSelected)) {
//                                    seletedItems.remove(Integer.valueOf(indexSelected));
//                                }
//                            }
//                        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//
//                                String[] selectedStores = new String[seletedItems.size()];
//                                String stores = "";
//                                for (int i = 0; i < seletedItems.size(); i++) {
//
////                                    selectedStores[i] = storeNames[Integer.valueOf(seletedItems.get(i)+"")];
//                                    if (i > 1){
//                                        stores = stores + ", " + storeNames[Integer.valueOf(seletedItems.get(i)+"")];
//                                        allitems = allitems + "," + storeID[Integer.valueOf(seletedItems.get(i)+"")];
//                                    }else{
//                                        allitems = storeID[Integer.valueOf(seletedItems.get(i)+"")];
//                                        stores =  storeNames[Integer.valueOf(seletedItems.get(i)+"")];
//                                    }
//
//                                    btnSelectStore.setText(stores);
//                                }
//                            }
//                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                //  Your code when user clicked on Cancel
//
//                            }
//                        }).create();
//                dialog.show();
            }
        });


        btnSendOrder = (FloatingActionButton) findViewById(R.id.btnSendOrder);
        btnSendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedStoreID.equalsIgnoreCase("")){
                    Helper.toast.short_(activity, "No store selected.");
                }else if (orderList != null){

                    if (orderList.size() > 0){
                        String formattedOrder = selectedStoreID+";";
                        for (int i = 0; i <orderList.size() ; i++) {
                            if (i==0){
                                formattedOrder = formattedOrder + orderList.get(i).getOrder_code().toString()+","+orderList.get(i).getOrder_qty().toString()+","+orderList.get(i).getOrder_unit().toString()+"";
                            }else{
                                formattedOrder = formattedOrder + ";" + orderList.get(i).getOrder_code().toString()+","+orderList.get(i).getOrder_qty().toString()+","+orderList.get(i).getOrder_unit().toString()+"";
                            }

                        }
                        SendSMS.sendOrder(activity, context, db.getServerNum(), formattedOrder);
                    }else{
                        Helper.toast.short_(activity, "No items to send.");
                    }

                }else{
                    Helper.toast.short_(activity, "No items to send.");
                }

            }
        });


        btnAddITem = (FloatingActionButton) findViewById(R.id.btnAdditem);
        btnAddITem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity_PickItem.class);
                startActivityForResult(intent, INTENT_SELECT_ITEM);
            }
        });

        toggleSendButtonVisibility();


    }

    public static void toggleSendButtonVisibility() {
        if (orderList.size() > 0){
            btnSendOrder.setVisibility(View.VISIBLE);
        }else{
            btnSendOrder.setVisibility(View.GONE);
        }
    }


    public static void showNoItemImage() {
        if (orderList != null){
            if (orderList.size() <= 0){
                imgNoItems.setVisibility(View.VISIBLE);
            }else{
                imgNoItems.setVisibility(View.GONE);
            }
        }else{
            imgNoItems.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (checkIfITemAlreadyAdded(data.getStringExtra("code"))){
                Helper.toast.short_(activity, "Item already in list.");
            }else{
                VarFishtaOrdering selectedItem = new VarFishtaOrdering();
                selectedItem.setOrder_code(data.getStringExtra("code"));
                selectedItem.setOrder_description(data.getStringExtra("item"));
                selectedItem.setOrder_qty(data.getStringExtra("qty"));
                selectedItem.setOrder_unit(data.getStringExtra("pax"));
                selectedItem.setItem_units(data.getStringExtra("units"));
//            Helper.dialogBox.okOnly(activity, "Result", data.getStringExtra("code") + "\n" + data.getStringExtra("item") + "\n" + data.getStringExtra("pax"), "OK");
                addItems(selectedItem);
                toggleSendButtonVisibility();
                showNoItemImage();
            }
        }
    }


    public void addItems(VarFishtaOrdering item) {
        orderList.add(item);
        itemsViewAdapter.notifyItemInserted(orderList.size());
        itemsViewAdapter.notifyItemRangeInserted(orderList.size(), orderList.size());
    }


    boolean checkIfITemAlreadyAdded(String code){
        int itemcount = 0; boolean isAdded = false;
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getOrder_code().equalsIgnoreCase(code)) {
                itemcount = itemcount + 1;
            }
        }

        if (itemcount > 0){
            isAdded = true;
        }
        return isAdded;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//
//        // Define the listener
//        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                // Do something when action item collapses
//                return true;  // Return true to collapse action view
//            }
//
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                // Do something when expanded
//                return true;  // Return true to expand action view
//            }
//
//        };
//        // Get the MenuItem for the action item
//        MenuItem actionMenuItem = menu.findItem(R.id.action_search);
//        // Assign the listener to that action item
//        MenuItemCompat.setOnActionExpandListener(actionMenuItem, expandListener);
//
//
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) MenuItemCompat.getActionView(actionMenuItem);
////        searchView.setSearchableInfo(
////                searchManager.getSearchableInfo(getComponentName()));
//
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                Helper.toast.long_(activity, query);
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
    }

}

