package com.santeh.rjhonsl.fishtaordering.Main.BlBoAi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.santeh.rjhonsl.fishtaordering.Pojo.BLBO_recordPojo;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;
import com.santeh.rjhonsl.fishtaordering.Util.Keys;
import com.santeh.rjhonsl.fishtaordering.Util.VarFishtaOrdering;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

/**
 * Created by rjhonsl on 5/31/2016.
 */
public class Activity_BackLoad_BadOrder extends AppCompatActivity {

    Activity activity;
    Context context;
    private static RecyclerView rvdrConf;
    public BLBODetailsAdapter blboAdapter;
    LinearLayoutManager mLayoutManager;
    List<BLBO_recordPojo> blboList;
    public static boolean isActive = false;
    DBaseQuery db;
    private static LinearLayout llNoHistory;
    List<VarFishtaOrdering> storeList = new ArrayList<>();
    String selectedStoreID, selectedStoreName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backload_badorder);
        activity = this;
        context = Activity_BackLoad_BadOrder.this;

        isActive = true;

        db = new DBaseQuery(this);
        db.open();


        ActionBar ab = getSupportActionBar();
        if (ab!=null){
            ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        storeList = db.getFilteredStores(db.getKeyVal(Keys.SETTINGS_STOREIDS));

        llNoHistory = (LinearLayout) findViewById(R.id.ll_noHistory);
        final LinearLayout llCreateBo = (LinearLayout) findViewById(R.id.llCreateNewBO);

        rvdrConf = (RecyclerView) findViewById(R.id.rvOrderHistory);
        assert rvdrConf != null;
        rvdrConf.setHasFixedSize(true);

        ImageButton btnCreate = (ImageButton) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llCreateBo.callOnClick();
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        rvdrConf.setLayoutManager(mLayoutManager);
        rvdrConf.setItemAnimator(new FadeInRightAnimator(new OvershootInterpolator(2f)));
        registerForContextMenu(rvdrConf);


        if (storeList.size()<=1){
            selectedStoreID = storeList.get(0).getCust_id();
            selectedStoreName = storeList.get(0).getCust_name();
        }
        llCreateBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String[] storeNames = new String[storeList.size()];
                final String[] storeID = new String[storeList.size()];

                for (int i = 0; i < storeList.size(); i++) {
                    storeNames[i] = storeList.get(i).getCust_name();
                    storeID[i] = storeList.get(i).getCust_id();
                }

                final Dialog d = Helper.dialogBox.list(activity, storeNames, "STORES", R.color.orange_fishta_darker);
                final ListView lvStore = (ListView) d.findViewById(R.id.dialog_list_listview);
                lvStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedStoreName = storeNames[i];
                        selectedStoreID = storeID[i];
//                        itemlist_OUT = new ArrayList<>();
                        Intent intent = new Intent(activity, Activity_Create_BackLoad_BadOrder.class);
                        intent.putExtra("storeName", selectedStoreName);
                        intent.putExtra("storeId", selectedStoreID);
                        startActivityForResult(intent, 20);
                        d.dismiss();
//                        itemsViewAdapter = new ItemsAdapter(itemlist_OUT, context, activity);
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                rvItems.setAdapter(itemsViewAdapter);
//                                itemsViewAdapter.notifyDataSetChanged();
//                                toggleNoItemImage();
//                            }
//                        }, 300);
                    }
                });
            }
        });


        populatelist();
        toggleHistoryVisibility(blboList.size());

    }

    private void populatelist() {

        blboList = new ArrayList<>();
        blboList = db.getAllBLBO_RECORDS();
        if (blboList.size() >0){
            blboAdapter = new BLBODetailsAdapter(blboList, context, activity);
            rvdrConf.setAdapter(blboAdapter);
            blboAdapter.notifyDataSetChanged();
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
