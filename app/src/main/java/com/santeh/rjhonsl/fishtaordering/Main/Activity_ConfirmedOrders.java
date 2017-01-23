package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import com.santeh.rjhonsl.fishtaordering.Adapter.ORConfirmationAdapter;
import com.santeh.rjhonsl.fishtaordering.Pojo.OrderConfirmationPojo;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

/**
 * Created by rjhonsl on 5/31/2016.
 */
public class Activity_ConfirmedOrders extends AppCompatActivity {

    Activity activity;
    Context context;
    private static RecyclerView rvOrderHistory;
    ORConfirmationAdapter ORAdapter;
    LinearLayoutManager mLayoutManager;
    List<OrderConfirmationPojo> orderHistoryList;
    public static boolean isActive = false;
    DBaseQuery db;
    private static LinearLayout llNoHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);
        activity = this;
        context = Activity_ConfirmedOrders.this;

        isActive = true;

        db = new DBaseQuery(this);
        db.open();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        llNoHistory = (LinearLayout) findViewById(R.id.ll_noHistory);

        orderHistoryList = new ArrayList<>();
        orderHistoryList = db.getbatchedConfirmation();
//        orderHistoryList = groupBatches(db.getAllConfirmation());


        rvOrderHistory = (RecyclerView) findViewById(R.id.rvOrderHistory);
        assert rvOrderHistory != null;
        rvOrderHistory.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        rvOrderHistory.setLayoutManager(mLayoutManager);
        rvOrderHistory.setItemAnimator(new FadeInRightAnimator(new OvershootInterpolator(2f)));
        registerForContextMenu(rvOrderHistory);

        ORAdapter = new ORConfirmationAdapter(orderHistoryList, context, activity);
        rvOrderHistory.setAdapter(ORAdapter);
        ORAdapter.notifyDataSetChanged();


        toggleHistoryVisibility(orderHistoryList.size());

    }


/*
    private List<OrderConfirmationPojo> groupBatches(List<OrderConfirmationPojo> unbatchOFlist) {
        List<OrderConfirmationPojo> batchOfItem = new ArrayList<>();
        String lastorNumber = "";
        String fullitem = "";

        for (int i = 0; i < unbatchOFlist.size(); i++) {

            String[] splitted = unbatchOFlist.get(i).getContent().split(";");
            String allItems = "";
            for (int x = 1; x < splitted.length; x++) {
                if (x == 1) {
                    allItems = allItems + splitted[x];
                } else {
                    allItems = allItems + ";" + splitted[x];
                }
            }

            Log.d("OFNUMBER", "COMPARE " + lastorNumber + " || " + unbatchOFlist.get(i).getORnumber());
            if (lastorNumber.equalsIgnoreCase(unbatchOFlist.get(i).getORnumber())) {
                fullitem = fullitem + ";" + allItems;
                Log.d("OFNUMBER", unbatchOFlist.get(i).getBatchNumber());
                if(i == (unbatchOFlist.size()-1) ){ //if item is on end then
                    Log.d("OFNUMBER", "ADDING "  + unbatchOFlist.get(i).getORnumber() + " " + unbatchOFlist.get(i).getBatchNumber());
                    OrderConfirmationPojo _of = new OrderConfirmationPojo();
                    _of.setId(unbatchOFlist.get(i).getId());
                    _of.setORnumber(lastorNumber);
                    _of.setSender(unbatchOFlist.get(i).getSender());
                    _of.setContent(unbatchOFlist.get(i).getContent());
                    _of.setTimeReceived(unbatchOFlist.get(i).getTimeReceived());
                    _of.setCustID(unbatchOFlist.get(i).getCustID());
                    _of.setArrangedItem(fullitem.substring(1));
                    _of.setIsSent(unbatchOFlist.get(i).getIsSent());
                    _of.setBatchNumber(unbatchOFlist.get(i).getBatchNumber());
                    batchOfItem.add(_of);
                    Log.d("OFNUMBER end", "FULL ITEMX: " + fullitem);
                }
            }else {
                Log.d("OFNUMBER", unbatchOFlist.get(i).getBatchNumber());
                if ( i > 0){ //if 0 then it must be start of loop therefore last or should be always not-equal
                    fullitem = fullitem + ";" + allItems;
                    Log.d("OFNUMBER", "ADDING x "  + unbatchOFlist.get(i).getORnumber() + " " + unbatchOFlist.get(i).getBatchNumber());
                    OrderConfirmationPojo _of = new OrderConfirmationPojo();
                    _of.setId(unbatchOFlist.get(i-1).getId());
                    _of.setORnumber(lastorNumber);
                    _of.setSender(unbatchOFlist.get(i-1).getSender());
                    _of.setContent(unbatchOFlist.get(i-1).getContent());
                    _of.setTimeReceived(unbatchOFlist.get(i-1).getTimeReceived());
                    _of.setCustID(unbatchOFlist.get(i-1).getCustID());
                    _of.setArrangedItem(fullitem.substring(1));
                    _of.setIsSent(unbatchOFlist.get(i-1).getIsSent());
                    _of.setBatchNumber(unbatchOFlist.get(i-1).getBatchNumber());
                    batchOfItem.add(_of);


                    fullitem = "";
                    Log.d("OFNUMBER mid", "FULL ITEM: " + fullitem);
                }
            }

            lastorNumber = unbatchOFlist.get(i).getORnumber();
        } //end of loop


        return batchOfItem;
    }
    */

    public static void toggleHistoryVisibility(int listSize) {
        if (listSize < 1){
            llNoHistory.setVisibility(View.VISIBLE);
            rvOrderHistory.setVisibility(View.GONE);
        }else {
            llNoHistory.setVisibility(View.GONE);
            rvOrderHistory.setVisibility(View.VISIBLE);
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
    protected void onResume() {
        super.onResume();
        db.open();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
        isActive = false;
    }
}
