package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;

/**
 * Created by rjhonsl on 5/31/2016.
 */
public class Activity_Home extends AppCompatActivity {

    Activity activity;
    Context context;
    DBaseQuery db;

    Button btnConvert;
    EditText edtToConvert, edtConverted, edtBinary;

    TextView btnSendOrder, btnOrdHistory, btnSettings, btnOrderConfirmation, btnDeliveryConfirmation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        activity = this;
        context = Activity_Home.this;

        db = new DBaseQuery(this);
        db.open();


        btnSendOrder = (TextView) findViewById(R.id.btnSendOrder);
        btnOrdHistory = (TextView) findViewById(R.id.btnOrderHistory);
        btnSettings = (TextView) findViewById(R.id.btnSettings);
        btnDeliveryConfirmation  = (TextView) findViewById(R.id.btnDeliveryConfirmation);
        btnOrderConfirmation = (TextView) findViewById(R.id.btnOrderConfirmation);
        TextView btnBackLoad = (TextView) findViewById(R.id.btnBLBO);

        edtToConvert = (EditText) findViewById(R.id.edtToConvert);
        edtBinary = (EditText) findViewById(R.id.edtBinary);
        edtConverted = (EditText) findViewById(R.id.edtConverted);

        btnConvert = (Button) findViewById(R.id.btnEncrypt);


        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                String hexed = Helper.convert.stringtoHex(edtToConvert.getText().toString());
//                String binned = Helper.convert.StringToBinary(edtToConvert.getText().toString()).toString();
//                String stringFromHexedFromBinned = Helper.convert.stringtoHex(binned);
////                String reversedHex = Helper.convert.toStringBuilder(hexed).reverse().toString();
//
                edtBinary.setText(hexed);
//
////                edtConverted.setText(Helper.convert.HextoString(Helper.convert.stringtoHex(edtToConvert.getText().toString())));
////                edtBinary.setText(convertToBinary(edtToConvert.getText().toString()+""));
////                String reversedbinary = convertToBinary(edtToConvert.getText().toString()+"").reverse().toString();
////                String binaryTOChar = convertBinaryToChar(
//////                        convertToBinary(edtToConvert.getText().toString()).toString()
////                        reversedbinary
////                );
////
////                edtBinary.setText(edtBinary.getText().toString()+"\n\n"+reversedbinary);
//
////                edtConverted.setText(Helper.convert.HextoString(stringFromHexedFromBinned));

            }
        });

        btnSendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });

        btnBackLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Activity_BackLoad_BadOrder.class);
                startActivity(intent);
            }
        });

        btnDeliveryConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Activity_DeliveryConfirmation.class);
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
                Intent intent = new Intent(activity, Activity_Settings.class);
                startActivity(intent);
            }
        });

        btnOrderConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Activity_ConfirmedOrders.class);
                startActivity(intent);
            }
        });


//        if (db.getAllItems().size() < 1){
//            insertItems();
//        }
//
//        if (db.getAllCustomers().size() < 1){
//            insertCustomers();
//        }

    }


    private void checkIfFreshInstall() {
        if (db.getSettingsCount()< 1){
            Helper.ActivityAction.startActivityClearStack(activity, Activity_Welcome.class);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfFreshInstall();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}
