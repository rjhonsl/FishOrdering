package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;

/**
 * Created by rjhonsl on 6/1/2016.
 */
public class Activity_Settings extends AppCompatActivity {


    Activity activity;
    Context context;
    DBaseQuery db;

    LinearLayout txtServerNum, txtStoreName, txtBackup, txtRestore, txtAbout;
    TextView lblServerNum, lblStoreName, lblAbout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = Activity_Settings.this;
        activity = this;

        db = new DBaseQuery(this);
        db.open();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.gray_50), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        txtServerNum = (LinearLayout) findViewById(R.id.txtServerNum);
        txtStoreName = (LinearLayout) findViewById(R.id.txtStoreName);
        txtBackup = (LinearLayout) findViewById(R.id.txtBackup);
        txtRestore = (LinearLayout) findViewById(R.id.txtRestore);
        txtAbout = (LinearLayout) findViewById(R.id.txtAbout);

        lblServerNum = (TextView) findViewById(R.id.lblServerNum);
        lblStoreName= (TextView) findViewById(R.id.lblStoreName);
        lblAbout= (TextView) findViewById(R.id.lblAbout);

        refreshValues();

        txtServerNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Settings.this, Activity_Wiz_SetServerNum.class));
            }
        });

        txtStoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Settings.this, Activity_Wiz_StoreName.class));
            }
        });




    }

    private void refreshValues() {
        lblServerNum.setText(db.getServerNum());
        lblStoreName.setText(db.getStoreName());
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
        refreshValues();
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }
}
