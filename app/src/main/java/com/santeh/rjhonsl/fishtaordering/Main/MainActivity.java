package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btnAddITem, btnSendOrder;
    Activity activity;
    Context context;
    public static int INTENT_SELECT_ITEM = 0;

    ArrayList<String> orderList =new ArrayList<String>();
    ListView lvOrders;

    Toolbar myToolbar;
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    LinearLayout imgNoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        context = MainActivity.this;
        final SmsManager sms = SmsManager.getDefault();

        myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        assert myToolbar != null;
        myToolbar.setBackgroundColor(getResources().getColor(R.color.orange_fishta));
        setSupportActionBar(myToolbar);
        myToolbar.inflateMenu(R.menu.menu_search);



        lvOrders = (ListView) findViewById(R.id.lv_orders);
        final String[] sampleItems = getResources().getStringArray(R.array.sampleItems);
        imgNoItems = (LinearLayout) findViewById(R.id.img_noitems);
//        Collections.addAll(orderList, sampleItems);

        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                orderList);
        lvOrders.setAdapter(adapter);
        showNoItemImage();

        btnSendOrder = (FloatingActionButton) findViewById(R.id.btnSendOrder);
        btnSendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (orderList != null){
                    if (orderList.size() > 0){
                        String formattedOrder = "StoreName;", recepient = "+639159231467";
                        for (int i = 0; i <orderList.size() ; i++) {
                            formattedOrder = formattedOrder + "" + orderList.get(i).toString()+"\n";
                        }

                        if (formattedOrder.length() > 159){
                            Helper.toast.short_(activity, "Sending Multipart Message");
                            ArrayList<String> parts = sms.divideMessage(formattedOrder);
                            sms.sendMultipartTextMessage(recepient, recepient,parts, null, null );
                        }else{
                            Helper.toast.short_(activity, "Sending Single Message");
                            sms.sendTextMessage(recepient, recepient, formattedOrder, null, null );}
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
        lvOrders.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvOrders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Helper.dialogBox.okOnly(activity, "Value", "" + orderList.get(position).toString(), "OK");
                return false;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
//            Helper.dialogBox.okOnly(activity, "Result", data.getStringExtra("qty") + "\n" + data.getStringExtra("item") + "\n" + data.getStringExtra("pax"), "OK");
            addItems(data.getStringExtra("item") + " | " + data.getStringExtra("qty") + data.getStringExtra("pax"));
            Helper.toast.short_(activity, "Item has been added.");
            showNoItemImage();
        }
    }


    public void addItems(String item) {
        orderList.add(item);
        adapter.notifyDataSetChanged();
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
}
