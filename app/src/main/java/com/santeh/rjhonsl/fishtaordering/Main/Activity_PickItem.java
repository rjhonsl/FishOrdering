package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;

/**
 * Created by rjhonsl on 5/20/2016.
 */
public class Activity_PickItem extends AppCompatActivity {

    Activity activity;
    Context context;
    ListView lvItems;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickitem);

        activity = this;
        context = Activity_PickItem.this;

        lvItems = (ListView) findViewById(R.id.lvFishes);
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
        final String[] itemsArray = getResources().getStringArray(R.array.fishes);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String[] pax = new String[]{"KGs","PCs"};
                final Dialog d = Helper.dialogBox.numberAndPAXpicker(activity, itemsArray[position], 1, 1000,pax );
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
                        d.dismiss();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });
            }
        });

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
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

}
