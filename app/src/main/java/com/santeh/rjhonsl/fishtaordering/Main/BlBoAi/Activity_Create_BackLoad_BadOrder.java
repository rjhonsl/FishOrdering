package com.santeh.rjhonsl.fishtaordering.Main.BlBoAi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.santeh.rjhonsl.fishtaordering.Main.OrderItems.Activity_PickItem;
import com.santeh.rjhonsl.fishtaordering.Main.OrderItems.MainActivity;
import com.santeh.rjhonsl.fishtaordering.Pojo.BLBO_itemPojo;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.BR_SMSDelivery;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;
import com.santeh.rjhonsl.fishtaordering.Util.SendSMS;

import java.util.ArrayList;
import java.util.Calendar;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

/**
 * Created by rjhonsl on 5/31/2016.
 */
public class Activity_Create_BackLoad_BadOrder extends AppCompatActivity {

    Activity activity;
    Context context;
    LinearLayoutManager mLayoutManager;
    public static boolean isActive = false;
    AdapterBLBODetails itemsViewAdapter;
    DBaseQuery db;
    Calendar cal;
    String storeID;
    String storeName;
    BroadcastReceiver receiver;

    String createType = "BO";
    String[] createTypeOptions = new String[]{"BAD ORDER", "BACK LOAD", "ACTUAL INVENTORY"};
    String[] createTypeItems = new String[]{"BO", "BL", "AI"};
    RecyclerView rvItems;

    public static ArrayList<BLBO_itemPojo> orderList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bo);
        activity = this;
        context = Activity_Create_BackLoad_BadOrder.this;

        isActive = true;
        db = new DBaseQuery(this);
        db.open();

        ActionBar ab = getSupportActionBar();
        if (ab!=null){
            ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent().hasExtra("storeId") && getIntent().hasExtra("storeName")){
            storeID = getIntent().getStringExtra("storeId");
            storeName = getIntent().getStringExtra("storeName");

            getSupportActionBar().setTitle(storeName);
        }


        cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        final TextView txtDate = (TextView) findViewById(R.id.txtDate);
        final TextView txtCreateType = (TextView) findViewById(R.id.txtCreateType);
        final Button btnSendBLBO = (Button) findViewById(R.id.btnSendBLBO);
        rvItems = (RecyclerView) findViewById(R.id.rv_drContents);
        rvItems.setHasFixedSize(true);
        rvItems.setItemViewCacheSize(0);

        // use a linear layout list
        mLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(mLayoutManager);
        rvItems.setItemAnimator(new FadeInRightAnimator(new OvershootInterpolator(2f)));
        registerForContextMenu(rvItems);

        itemsViewAdapter = new AdapterBLBODetails(orderList, context, activity);
        rvItems.setAdapter(itemsViewAdapter);
        itemsViewAdapter.notifyDataSetChanged();


        txtDate.setText(Helper.convert.LongToDate_Gregorian(cal.getTimeInMillis()));

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar cal1 = Calendar.getInstance();
                        cal1.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                        cal = cal1;
                        txtDate.setText(Helper.convert.LongToDate_Gregorian(cal1.getTimeInMillis()));
                    }
                }, year, month, day);

                dpd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dpd.show();
            }
        });

        txtCreateType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = Helper.dialogBox.list(activity, createTypeOptions, "SELECT A TRANSACTION TYPE", R.color.orange_fishta);
                ListView lv = (ListView) d.findViewById(R.id.dialog_list_listview);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        d.dismiss();
                        txtCreateType.setText(createTypeOptions[i]);
                        createType = createTypeItems[i];
                        txtDate.callOnClick();
                    }
                });
            }
        });


        Button btnAddProduct = (Button) findViewById(R.id.btnAdditem);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storeName.equalsIgnoreCase("")) {
                    Helper.toast.short_(activity, "Select a Customer First");
                }else{
                    Intent intent = new Intent(activity, Activity_PickItem.class);
                    intent.putExtra("storeid", storeID);
                    startActivityForResult(intent, MainActivity.INTENT_SELECT_ITEM);
                }
            }
        });


        btnSendBLBO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderList!=null){
                    if (orderList.size()>0){

                        String start = createType + "-" + storeID + "-" + Helper.convert.LongtoDate_DBFormatSlashes(cal.getTimeInMillis());
                        String formattedOrder = start ;
                        String fullItem  = "";

                        for (int i = 0; i < orderList.size(); i++) {
                            formattedOrder = formattedOrder +";" + orderList.get(i).getActualItemID() +","+ orderList.get(i).getActualQty() +","+ orderList.get(i).getActualItemUnit() ;
                            fullItem = fullItem +";" + orderList.get(i).getActualItemID() +","+ orderList.get(i).getActualQty() +","+ orderList.get(i).getActualItemUnit() ;
                        }
                        fullItem = fullItem.substring(1, fullItem.length());

//                        Helper.dialogBox.okOnly_Scrolling(activity, "Items", formattedOrder + " || " + formattedOrder.length() , "OK", R.color.amber_300).show();
                        SendSMS.sendBLBO(activity, context, db.getServerNum(), formattedOrder, fullItem, createType);


//                        final List<String> batchedOrderslist = new ArrayList<String>();
//                        int batchCounter = 0;
//                        for (int i = 0; i <itemlist_OUT.size() ; i++) {
//                            if (i==0){
//                                formattedOrder = formattedOrder + itemlist_OUT.get(i).getActualItemID() +","+ itemlist_OUT.get(i).getActualQty() +","+ itemlist_OUT.get(i).getActualItemUnit() +"";
//                                fullformatted = formattedOrder;
//                                Log.d("BATCHING", "start: "+formattedOrder.length()+"");
//                                if (itemlist_OUT.size()>0 && itemlist_OUT.size()<2){
//                                    batchedOrderslist.add(formattedOrder);
//                                }
//                            }else{
//                                String tester =  formattedOrder + ";" + itemlist_OUT.get(i).getActualItemID() +","+ itemlist_OUT.get(i).getActualQty() +","+ itemlist_OUT.get(i).getActualItemUnit() +"";
//                                if (tester.length()>160){
//                                    batchCounter++;
//                                    batchedOrderslist.add(formattedOrder);
//                                    formattedOrder = start + "" + batchCounter+";";
//                                    Log.d("BATCHING", ">160: "+formattedOrder.length()+" || " + fullformatted);
//                                }else{
//                                    formattedOrder = formattedOrder + ";" + itemlist_OUT.get(i).getActualItemID() +","+ itemlist_OUT.get(i).getActualQty() +","+ itemlist_OUT.get(i).getActualItemUnit() +"";
//                                    if (i==itemlist_OUT.size()-1){
//                                        batchedOrderslist.add(formattedOrder);
//                                        Log.d("BATCHING", "if not over 160: "+formattedOrder.length()+" || " + fullformatted);
//                                    }
//                                }
//                                fullformatted = fullformatted  + ";" + itemlist_OUT.get(i).getActualItemID() +","+ itemlist_OUT.get(i).getActualQty() +","+ itemlist_OUT.get(i).getActualItemUnit() +"";
//                            }
//
//                        }
//                        Log.d("BATCHING", "end of loop: "+formattedOrder.length()+" || " + fullformatted);
//
//                        if (batchedOrderslist.size() > 0){
//                            String allOrders = "";
//                            for (int i = 0; i < batchedOrderslist.size(); i++) {
//                                allOrders = allOrders + batchedOrderslist.get(i) + "\n||\n";
//                            }
//                            Helper.dialogBox.okOnly_Scrolling(activity, "Items", allOrders, "OK", R.color.amber_300).show();
//                        }


                    }else{Toast.makeText(activity, "No items selected", Toast.LENGTH_SHORT).show();}
                }else{Toast.makeText(activity, "No items selected", Toast.LENGTH_SHORT).show();}
            }
        });

        txtCreateType.callOnClick();


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                close();
            }
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MainActivity.INTENT_SELECT_ITEM) {
                if (checkIfITemAlreadyAdded(data.getStringExtra("code"))){
                    Helper.toast.short_(activity, "Item already in list.");
                }else{
                    BLBO_itemPojo  selectedItem = new BLBO_itemPojo();
                    selectedItem.setActualItemID(data.getStringExtra("code"));
                    selectedItem.setActualDescription(data.getStringExtra("item"));
                    selectedItem.setActualQty(data.getStringExtra("qty"));
                    selectedItem.setActualItemUnit(data.getStringExtra("pax"));
                    selectedItem.setActualItemUnitOptions(data.getStringExtra("units"));
                    selectedItem.setExpectedQTY("0");
                    addItems(selectedItem);
                }
            }

        }
    }


    public void addItems(BLBO_itemPojo item) {
        orderList.add(item);
//        itemsViewAdapter.notifyDataSetChanged();
        itemsViewAdapter.notifyItemInserted(orderList.size());
//        itemsViewAdapter.notifyItemRangeInserted(itemlist_OUT.size(), itemlist_OUT.size());

        Helper.random.delayedTask(new Runnable() {
            @Override
            public void run() {
                rvItems.smoothScrollToPosition(orderList.size());
            }
        }, 250);
    }

    boolean checkIfITemAlreadyAdded(String code){
        int itemcount = 0; boolean isAdded = false;
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getActualItemID().equalsIgnoreCase(code)) {
                itemcount = itemcount + 1;
            }
        }

        if (itemcount > 0){
            isAdded = true;
        }
        return isAdded;
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


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(BR_SMSDelivery.FCM_RESULT));
    }

    public void close(){
        finish();
    }
}
