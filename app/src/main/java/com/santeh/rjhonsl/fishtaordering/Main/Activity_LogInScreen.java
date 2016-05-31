package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;

/**
 * Created by rjhonsl on 5/31/2016.
 */
public class Activity_LogInScreen extends AppCompatActivity {

    Activity activity;
    Context context;

    DBaseQuery db;

    private TextView btnSendOrder, btnOrdHistory, btnSettings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        activity = this;
        context = Activity_LogInScreen.this;

        db = new DBaseQuery(this);
        btnSendOrder = (TextView) findViewById(R.id.btnSendOrder);
        btnOrdHistory = (TextView) findViewById(R.id.btnOrderHistory);
        btnSettings = (TextView) findViewById(R.id.btnSettings);
        db.open();


        btnSendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });


        btnOrdHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Activity_OrderHistory.class);
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        if (db.getAllItems().size() < 1){
            insertItems();
        }
    }


    void insertItems(){

        Log.d("DB", "INSERTING ITEMS");
        String[] itemss = getResources().getStringArray(R.array.fishes);
        for (int i = 0; i < itemss.length; i++) {
            db.insertItems(i + "", itemss[i], "", "KGs,PCs");
        }

    }
}
