package com.santeh.rjhonsl.fishtaordering.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;

/**
 * Created by rjhonsl on 6/3/2016.
 */
public class Activity_SetupSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setupsuccess);

        FloatingActionButton start = (FloatingActionButton) findViewById(R.id.btnStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.ActivityAction.startActivityClearStack(Activity_SetupSuccess.this, Activity_Home.class);
                DBaseQuery db = new DBaseQuery(Activity_SetupSuccess.this);
                db.open();


//                startActivity(inte, ActivityOptionsCompat.makeSceneTransitionAnimation(Activity_SetupSuccess.this).toBundle());
//                finish();
//                Helper.ActivityAction.startActivityClearStack(Activity_SetupSuccess.this, Activity_Home.class);
            }
        });
    }
}
