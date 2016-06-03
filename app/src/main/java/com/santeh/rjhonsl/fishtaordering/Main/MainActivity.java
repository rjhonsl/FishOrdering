package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import com.santeh.rjhonsl.fishtaordering.Adapter.ItemsViewAdapter;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;
import com.santeh.rjhonsl.fishtaordering.Util.SendSMS;
import com.santeh.rjhonsl.fishtaordering.Util.VarFishtaOrdering;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

public class MainActivity extends AppCompatActivity {

    DBaseQuery db;
    FloatingActionButton btnAddITem, btnSendOrder;
    Activity activity;
    Context context;
    public static int INTENT_SELECT_ITEM = 0;

    public static ArrayList<VarFishtaOrdering> orderList = new ArrayList<>();

    RecyclerView rvItems;
    Toolbar myToolbar;
    public static ItemsViewAdapter itemsViewAdapter;
    LinearLayoutManager mLayoutManager;
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    LinearLayout imgNoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        context = MainActivity.this;


        db = new DBaseQuery(this);
        db.open();

        myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        assert myToolbar != null;
        myToolbar.setBackgroundColor(getResources().getColor(R.color.orange_fishta));
        setSupportActionBar(myToolbar);
        myToolbar.inflateMenu(R.menu.menu_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        assert rvItems != null;
        rvItems.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(mLayoutManager);
        rvItems.setItemAnimator(new FadeInRightAnimator(new OvershootInterpolator(2f)));
        registerForContextMenu(rvItems);

        itemsViewAdapter = new ItemsViewAdapter(orderList, context, activity);
        rvItems.setAdapter(itemsViewAdapter);
        itemsViewAdapter.notifyDataSetChanged();


        imgNoItems = (LinearLayout) findViewById(R.id.img_noitems);
        showNoItemImage();

        btnSendOrder = (FloatingActionButton) findViewById(R.id.btnSendOrder);
        btnSendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderList != null){
                    if (orderList.size() > 0){
                        String formattedOrder = "STORENAME", recipient = "09159231467";
                        for (int i = 0; i <orderList.size() ; i++) {
                            formattedOrder = formattedOrder + ";" + orderList.get(i).getOrder_code().toString()+","+orderList.get(i).getOrder_qty().toString()+","+orderList.get(i).getOrder_unit().toString()+"";
                        }
                        SendSMS.sendOrder(activity, context, recipient, formattedOrder);
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


    }


    private void showNoItemImage() {
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

                showNoItemImage();
            }
        }
    }


    public void addItems(VarFishtaOrdering item) {
        orderList.add(item);
//        itemsViewAdapter.notifyDataSetChanged();

        itemsViewAdapter.notifyItemInserted(orderList.size());
        itemsViewAdapter.notifyItemRangeInserted(orderList.size(), orderList.size());

//        refreshView();
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
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }

        };

        // Get the MenuItem for the action item
        MenuItem actionMenuItem = menu.findItem(R.id.action_search);
        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(actionMenuItem, expandListener);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
    }

}

