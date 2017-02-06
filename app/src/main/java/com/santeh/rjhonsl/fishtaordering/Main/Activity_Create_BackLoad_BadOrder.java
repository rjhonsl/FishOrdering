package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.santeh.rjhonsl.fishtaordering.Adapter.DeliveryConfirmationAdapter;
import com.santeh.rjhonsl.fishtaordering.Pojo.DeliveryConfirmationPojo;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

/**
 * Created by rjhonsl on 5/31/2016.
 */
public class Activity_Create_BackLoad_BadOrder extends AppCompatActivity {

    Activity activity;
    Context context;
    private static RecyclerView rvdrConf;
    public DeliveryConfirmationAdapter drAdapter;
    LinearLayoutManager mLayoutManager;
    List<DeliveryConfirmationPojo> drList;
    public static boolean isActive = false;
    DBaseQuery db;
    private static LinearLayout llNoHistory;
    Calendar cal;

    String createType = "BO";
    String[] createTypeOptions = new String[]{"BAD ORDER", "BACK LOAD", "ACTUAL INVENTORY"};
    String[] createTypeItems = new String[]{"BO", "BL", "AI"};

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

        cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        final TextView txtDate = (TextView) findViewById(R.id.txtDate);
        final TextView txtCreateType = (TextView) findViewById(R.id.txtCreateType);
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
                    }
                });
            }
        });


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
