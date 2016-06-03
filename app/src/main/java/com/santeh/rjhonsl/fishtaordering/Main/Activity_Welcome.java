package com.santeh.rjhonsl.fishtaordering.Main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.Keys;

/**
 * Created by rjhonsl on 6/3/2016.
 */
public class Activity_Welcome extends AppCompatActivity {


    Activity activity;
    Context context;
    FloatingActionButton btnStart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        activity = this;
        context = Activity_Welcome.this;
        btnStart = (FloatingActionButton) findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Welcome.this, Activity_Wiz_SetServerNum.class);
                intent.putExtra(Keys.IS_STARTUP, true);
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();
                startActivity(intent, bundle);
            }
        });

    }
}
