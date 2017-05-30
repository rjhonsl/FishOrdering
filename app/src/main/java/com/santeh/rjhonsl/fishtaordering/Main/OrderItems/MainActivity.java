package com.santeh.rjhonsl.fishtaordering.Main.OrderItems;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
//    static FloatingActionButton btnSendOrder;
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
    String selectedStoreName = "";

    public static Button btnSend, btnAddProduct;


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

        // use a linear layout list
        mLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(mLayoutManager);
        rvItems.setItemAnimator(new FadeInRightAnimator(new OvershootInterpolator(2f)));
        registerForContextMenu(rvItems);

        itemsViewAdapter = new ItemsAdapter(orderList, context, activity);
        rvItems.setAdapter(itemsViewAdapter);
        itemsViewAdapter.notifyDataSetChanged();

        imgNoItems = (LinearLayout) findViewById(R.id.img_noitems);
        toggleNoItemImage();

//        Helper.toast.indefinite(activity, db.getKeyVal(Keys.SETTINGS_STOREIDS));

        storeList = db.getFilteredStores(db.getKeyVal(Keys.SETTINGS_STOREIDS));

        if (storeList.size()<=1){
            selectedStoreID = storeList.get(0).getCust_id();
            selectedStoreName = storeList.get(0).getCust_name();
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
                final ListView lvStore = (ListView) d.findViewById(R.id.dialog_list_listview);
                lvStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        btnSelectStore.setText(storeNames[i]);
                        selectedStoreName = storeNames[i];
                        selectedStoreID = storeID[i];
                        orderList = new ArrayList<>();
                        d.dismiss();
                        itemsViewAdapter = new ItemsAdapter(orderList, context, activity);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rvItems.setAdapter(itemsViewAdapter);
                                itemsViewAdapter.notifyDataSetChanged();
                                toggleNoItemImage();
                            }
                        }, 300);
                    }
                });
            }
        });

        btnAddProduct = (Button) findViewById(R.id.btnAddproduct);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedStoreID.equalsIgnoreCase("")) {
                    Helper.toast.short_(activity, "Select a Customer First");
                }else{
                    Intent intent = new Intent(MainActivity.this, Activity_PickItem.class);
                    intent.putExtra("storeid", selectedStoreID);
                    startActivityForResult(intent, INTENT_SELECT_ITEM);
                }
            }
        });

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedStoreID.equalsIgnoreCase("")){
                    Helper.toast.short_(activity, "No store selected.");
                }else if (orderList != null){
                    if (orderList.size() > 0){

                        final Dialog d = new Dialog(activity);//
                        d.requestWindowFeature(Window.FEATURE_NO_TITLE); //notitle
                        d.setContentView(R.layout.dialog_sendingconf);//Set the xml view of the dialog
                        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        Window window = d.getWindow();
                        lp.copyFrom(window.getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        window.setAttributes(lp);
                        d.show();


                        String start =  "#OF-"+selectedStoreID+"";
                        String formattedOrder = start + ";";
                        String fullformatted = "";
                        final List<String> batchedOrderslist = new ArrayList<String>();
                        int batchCounter = 0;
                        for (int i = 0; i <orderList.size() ; i++) {
//                            Log.d("BATCHING", "start: "+formattedOrder.length()+"");
                            if (i==0){
                                formattedOrder = formattedOrder + orderList.get(i).getOrder_code() +","+ orderList.get(i).getOrder_qty() +","+ orderList.get(i).getOrder_unit() +"";
                                fullformatted = formattedOrder;
                                if (orderList.size() > 0 && orderList.size() < 2) {
                                    batchedOrderslist.add(formattedOrder);
                                }
                            }else{
                                String tester = formattedOrder + ";" + orderList.get(i).getOrder_code() + "," + orderList.get(i).getOrder_qty() + "," + orderList.get(i).getOrder_unit() + "";
                                if (tester.length() > 160) {
//                                    Log.d("BATCHING", "greater than: "+formattedOrder.length()+"");
                                    batchCounter++;
                                    batchedOrderslist.add(formattedOrder);
                                    formattedOrder = start + "" + batchCounter + ";";
                                } else {
                                    formattedOrder = formattedOrder + ";" + orderList.get(i).getOrder_code() + "," + orderList.get(i).getOrder_qty() + "," + orderList.get(i).getOrder_unit() + "";
                                    if (i == orderList.size() - 1) {
                                        batchedOrderslist.add(formattedOrder);
                                    }
//                                    Log.d("BATCHING", "less than: "+formattedOrder.length()+"");
                                }
                                fullformatted = fullformatted + ";" + orderList.get(i).getOrder_code() + "," + orderList.get(i).getOrder_qty() + "," + orderList.get(i).getOrder_unit() + "";
                            }
                        }


                        if (batchedOrderslist.size() > 0){
                            String allOrders = "";
                            for (int i = 0; i < batchedOrderslist.size(); i++) {
                                allOrders = allOrders + batchedOrderslist.get(i) + "\n||\n";
                            }
                        }


                        TextView txtstore = (TextView) d.findViewById(R.id.txtStoreName);
                        TextView txtCurrent = (TextView) d.findViewById(R.id.txtcurrentTime);
                        TextView txtItems = (TextView) d.findViewById(R.id.txtItem);
                        TextView txtClose = (TextView) d.findViewById(R.id.txtCloseDialog);
                        ImageView imgButton = (ImageView) d.findViewById(R.id.btnFinalSend);
                        txtItems.setText(db.rearrangeItems(fullformatted));

                        txtstore.setText(selectedStoreName);
                        txtCurrent.setText(Helper.convert.LongToDateTime_Gregorian(System.currentTimeMillis()));


//                        final String finalFormattedOrder = formattedOrder;
                        final String finalFormattedOrder = fullformatted;
                        imgButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Dialog ddd = Helper.dialogBox.yesNo(activity, "Send this order to warehouse?", "Send Order", "YES", "NO");
                                Button yes = (Button) ddd.findViewById(R.id.btn_dialog_yesno_opt1);
                                Button no = (Button) ddd.findViewById(R.id.btn_dialog_yesno_opt2);
                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if (batchedOrderslist.size() > 0){
                                            String allOrders = "";
                                            for (int i = 0; i < batchedOrderslist.size(); i++) {
                                                allOrders = allOrders + batchedOrderslist.get(i) + "\n\n";
                                            }
//                                            Helper.dialogBox.okOnly_Scrolling(activity, "Items", finalFormattedOrder, "OK", R.color.amber_300).show();
                                            Log.d("AllOrders", finalFormattedOrder);
                                        }

                                        ddd.dismiss();
                                        d.dismiss();
                                        SendSMS.sendOrder(activity, context, db.getServerNum(), finalFormattedOrder);
                                    }
                                });

                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ddd.dismiss();
                                    }
                                });
                            }
                        });

                        txtClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                d.dismiss();
                            }
                        });
                    }else{
                        Helper.toast.short_(activity, "No items to send.");
                    }

                }else{
                    Helper.toast.short_(activity, "No items to send.");
                }
            }
        });


//        btnSendOrder = (FloatingActionButton) findViewById(R.id.btnSendOrder);
//        btnSendOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(selectedStoreID.equalsIgnoreCase("")){
//                    Helper.toast.short_(activity, "No store selected.");
//                }else if (itemlist_OUT != null){
//
//                    if (itemlist_OUT.size() > 0){
//                        String formattedOrder = selectedStoreID+";";
//                        for (int i = 0; i <itemlist_OUT.size() ; i++) {
//                            if (i==0){
//                                formattedOrder = formattedOrder + itemlist_OUT.get(i).getOrder_code().toString()+","+itemlist_OUT.get(i).getOrder_qty().toString()+","+itemlist_OUT.get(i).getOrder_unit().toString()+"";
//                            }else{
//                                formattedOrder = formattedOrder + ";" + itemlist_OUT.get(i).getOrder_code().toString()+","+itemlist_OUT.get(i).getOrder_qty().toString()+","+itemlist_OUT.get(i).getOrder_unit().toString()+"";
//                            }
//
//                        }
//                        SendSMS.sendOrder(activity, context, db.getServerNum(), formattedOrder);
//                    }else{
//                        Helper.toast.short_(activity, "No items to send.");
//                    }
//
//                }else{
//                    Helper.toast.short_(activity, "No items to send.");
//                }
//
//            }
//        });


//        btnAddITem = (FloatingActionButton) findViewById(R.id.btnAdditem);
//        btnAddITem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedStoreID.equalsIgnoreCase("")) {
//                    Helper.toast.short_(activity, "Select a Customer First");
//                }else{
//                    Intent intent = new Intent(MainActivity.this, Activity_PickItem.class);
//                    intent.putExtra("storeid", selectedStoreID);
//                    startActivityForResult(intent, INTENT_SELECT_ITEM);
//                }
//
//            }
//        });

        toggleSendButtonVisibility();


    }

    public static void toggleSendButtonVisibility() {
        if (orderList.size() > 0){
            btnSend.setVisibility(View.VISIBLE);
        }else{
            btnSend.setVisibility(View.GONE);
        }
    }


    public static void toggleNoItemImage() {
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
                toggleNoItemImage();
            }
        }
    }


    public void addItems(VarFishtaOrdering item) {
        orderList.add(item);
//        itemsViewAdapter.notifyDataSetChanged();

//        itemsViewAdapter.notifyItemRangeInserted(itemlist_OUT.size(), itemlist_OUT.size());

        Helper.random.delayedTask(new Runnable() {
            @Override
            public void run() {
                itemsViewAdapter.notifyItemInserted(orderList.size());
            }
        }, 250);

        Helper.random.delayedTask(new Runnable() {
            @Override
            public void run() {
                rvItems.smoothScrollToPosition(orderList.size());
            }
        }, 600);
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

