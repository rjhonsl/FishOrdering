package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import com.santeh.rjhonsl.fishtaordering.Adapter.OrderHistoryAdapter;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.VarFishtaOrdering;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

/**
 * Created by rjhonsl on 5/31/2016.
 */
public class Activity_OrderHistory extends AppCompatActivity {

    Activity activity;
    Context context;
    private static RecyclerView rvOrderHistory;
    public OrderHistoryAdapter orderHistoryAdapter;
    LinearLayoutManager mLayoutManager;
    List<VarFishtaOrdering> orderHistoryList;
    public static boolean isActive = false;
    DBaseQuery db;
    private static LinearLayout llNoHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);
        activity = this;
        context = Activity_OrderHistory.this;

        isActive = true;

        db = new DBaseQuery(this);
        db.open();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.gray_50), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        llNoHistory = (LinearLayout) findViewById(R.id.ll_noHistory);

        orderHistoryList = new ArrayList<>();
        orderHistoryList = db.getAllOrderHistory();

        rvOrderHistory  = (RecyclerView) findViewById(R.id.rvOrderHistory);
        assert rvOrderHistory != null;
        rvOrderHistory.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        rvOrderHistory.setLayoutManager(mLayoutManager);
        rvOrderHistory.setItemAnimator(new FadeInRightAnimator(new OvershootInterpolator(2f)));
        registerForContextMenu(rvOrderHistory);

        orderHistoryAdapter = new OrderHistoryAdapter(orderHistoryList, context, activity);
        rvOrderHistory.setAdapter(orderHistoryAdapter);
        orderHistoryAdapter.notifyDataSetChanged();

        toggleHistoryVisibility(orderHistoryList.size());

    }

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
