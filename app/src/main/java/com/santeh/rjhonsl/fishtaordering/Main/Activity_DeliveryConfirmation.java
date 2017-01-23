package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import com.santeh.rjhonsl.fishtaordering.Adapter.DeliveryConfirmationAdapter;
import com.santeh.rjhonsl.fishtaordering.Pojo.DeliveryConfirmationPojo;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

/**
 * Created by rjhonsl on 5/31/2016.
 */
public class Activity_DeliveryConfirmation extends AppCompatActivity {

    Activity activity;
    Context context;
    private static RecyclerView rvdrConf;
    public DeliveryConfirmationAdapter drAdapter;
    LinearLayoutManager mLayoutManager;
    List<DeliveryConfirmationPojo> drList;
    public static boolean isActive = false;
    DBaseQuery db;
    private static LinearLayout llNoHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);
        activity = this;
        context = Activity_DeliveryConfirmation.this;

        isActive = true;

        db = new DBaseQuery(this);
        db.open();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        llNoHistory = (LinearLayout) findViewById(R.id.ll_noHistory);

        rvdrConf = (RecyclerView) findViewById(R.id.rvOrderHistory);
        assert rvdrConf != null;
        rvdrConf.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        rvdrConf.setLayoutManager(mLayoutManager);
        rvdrConf.setItemAnimator(new FadeInRightAnimator(new OvershootInterpolator(2f)));
        registerForContextMenu(rvdrConf);

        populatelist();


        toggleHistoryVisibility(drList.size());

    }

    private void populatelist() {


        drList = new ArrayList<>();
        drList = db.getBatchedDeliverConfirmation();
        if (drList.size() >0){
            drAdapter = new DeliveryConfirmationAdapter(drList, context, activity);
            rvdrConf.setAdapter(drAdapter);
            drAdapter.notifyDataSetChanged();
        }

    }

    public static void toggleHistoryVisibility(int listSize) {
        if (listSize < 1){
            llNoHistory.setVisibility(View.VISIBLE);
            rvdrConf.setVisibility(View.GONE);
        }else {
            llNoHistory.setVisibility(View.GONE);
            rvdrConf.setVisibility(View.VISIBLE);
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
        populatelist();
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
        isActive = false;
    }
}
