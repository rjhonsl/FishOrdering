package com.santeh.rjhonsl.fishtaordering.Main.Reprocess;

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

import com.santeh.rjhonsl.fishtaordering.Main.BlBoAi.AdapterBLBODetails;
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
public class Activity_Create_Reprocess extends AppCompatActivity {

    Activity activity;
    Context context;
    LinearLayoutManager mLayoutManager;
    LinearLayoutManager mLayoutManager2;
    public static boolean isActive = false;
    AdapterBLBODetails itemsViewAdapter_IN;
    AdapterBLBODetails itemsViewAdapter_OUT;
    DBaseQuery db;
    Calendar cal;
    String storeID;
    String storeName;
    BroadcastReceiver receiver;

    String createType = "BO";
    String[] reprocessOptions = new String[]{"IN", "OUT"};
    String reprocessType = "";
    RecyclerView rvIn;
    RecyclerView rvOut;

    public static ArrayList<BLBO_itemPojo> itemlist_OUT = new ArrayList<>();
    public static ArrayList<BLBO_itemPojo> itemlist_IN = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reprocess);
        activity = this;
        context = Activity_Create_Reprocess.this;

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
        final Button btnSendReprocess = (Button) findViewById(R.id.btnSendReprocess);
        rvIn = (RecyclerView) findViewById(R.id.rv_reprocess_in);
        rvOut = (RecyclerView) findViewById(R.id.rv_reprocess_out);

        rvIn.setHasFixedSize(true);
        rvIn.setItemViewCacheSize(0);

        rvOut.setHasFixedSize(true);
        rvOut.setItemViewCacheSize(0);

        // use a linear layout list
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager2 = new LinearLayoutManager(this);

        rvIn.setLayoutManager(mLayoutManager);
        rvIn.setItemAnimator(new FadeInRightAnimator(new OvershootInterpolator(2f)));
        registerForContextMenu(rvIn);

        rvOut.setLayoutManager(mLayoutManager2);
        rvOut.setItemAnimator(new FadeInRightAnimator(new OvershootInterpolator(2f)));
        registerForContextMenu(rvOut);



        itemsViewAdapter_IN = new AdapterBLBODetails(itemlist_IN, context, activity);
        rvIn.setAdapter(itemsViewAdapter_IN);
        itemsViewAdapter_IN.notifyDataSetChanged();


        itemsViewAdapter_OUT = new AdapterBLBODetails(itemlist_OUT, context, activity);
        rvOut.setAdapter(itemsViewAdapter_OUT);
        itemsViewAdapter_OUT.notifyDataSetChanged();


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




        Button btnAddProduct = (Button) findViewById(R.id.btnAdditem);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storeName.equalsIgnoreCase("")) {
                    Helper.toast.short_(activity, "Select a Customer First");
                }else{
                    final Dialog d = Helper.dialogBox.list(activity, reprocessOptions, "REPROCESS TYPE", R.color.teal_600);
                    ListView lv = (ListView) d.findViewById(R.id.dialog_list_listview);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(activity, Activity_PickItem.class);
                            intent.putExtra("storeid", storeID);
                            reprocessType = reprocessOptions[i];
                            startActivityForResult(intent, MainActivity.INTENT_SELECT_ITEM);
                            d.dismiss();
                        }
                    });

                }
            }
        });


        btnSendReprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemlist_OUT !=null || itemlist_IN != null){
                    if (itemlist_OUT.size()>0 || itemlist_IN.size()>0 ){

                        String start = "RP" + "-" + storeID + "-" + Helper.convert.LongtoDate_DBFormatSlashes(cal.getTimeInMillis());
                        String formattedOrder = start ;
                        String fullItem  = "";

                        for (int i = 0; i < itemlist_OUT.size(); i++) {
                            formattedOrder = formattedOrder +";" + itemlist_OUT.get(i).getActualItemID() +","+ itemlist_OUT.get(i).getActualQty() +","+ itemlist_OUT.get(i).getActualItemUnit()  + ",o";
                            fullItem = fullItem +";" + itemlist_OUT.get(i).getActualItemID() +","+ itemlist_OUT.get(i).getActualQty() +","+ itemlist_OUT.get(i).getActualItemUnit() +",o" ;
                        }

                        for (int i = 0; i < itemlist_IN.size(); i++) {
                            formattedOrder = formattedOrder +";" + itemlist_IN.get(i).getActualItemID() +","+ itemlist_IN.get(i).getActualQty() +","+ itemlist_IN.get(i).getActualItemUnit() + ",i" ;
                            fullItem = fullItem +";" + itemlist_IN.get(i).getActualItemID() +","+ itemlist_IN.get(i).getActualQty() +","+ itemlist_IN.get(i).getActualItemUnit() + ",i" ;
                        }

                        fullItem = fullItem.substring(1, fullItem.length());

//                        Helper.dialogBox.okOnly_Scrolling(activity, "Items", formattedOrder + " || " + formattedOrder.length() , "OK", R.color.amber_300).show();
                        SendSMS.sendReprocess(context, db.getServerNum(), formattedOrder, fullItem, createType);



                    }else{
                        Toast.makeText(activity, "No items selected", Toast.LENGTH_SHORT).show();}
                }else{Toast.makeText(activity, "No items selected", Toast.LENGTH_SHORT).show();}
            }
        });

        txtDate.callOnClick();


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
                    Helper.toast.short_(activity, db.getitemDescription(data.getStringExtra("code")) + " already in list.");
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
        if (reprocessType.equalsIgnoreCase(reprocessOptions[0])) {
            itemlist_IN.add(item);
            itemsViewAdapter_IN.notifyItemInserted(itemlist_IN.size());
            Helper.random.delayedTask(new Runnable() {
                @Override
                public void run() {
                    rvIn.smoothScrollToPosition(itemlist_IN.size());
                }
            }, 250);
        } else {
            itemlist_OUT.add(item);
            itemsViewAdapter_OUT.notifyItemInserted(itemlist_OUT.size());
            Helper.random.delayedTask(new Runnable() {
                @Override
                public void run() {
                    rvOut.smoothScrollToPosition(itemlist_OUT.size());
                }
            }, 250);
        }
    }

    boolean checkIfITemAlreadyAdded(String code){
        int itemcount = 0; boolean isAdded = false;
        for (int i = 0; i < itemlist_OUT.size(); i++) {
            if (itemlist_OUT.get(i).getActualItemID().equalsIgnoreCase(code)) {
                itemcount = itemcount + 1;
            }
        }

        for (int i = 0; i < itemlist_IN.size(); i++) {
            if (itemlist_IN.get(i).getActualItemID().equalsIgnoreCase(code)) {
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
