package com.santeh.rjhonsl.fishtaordering.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.santeh.rjhonsl.fishtaordering.R;

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
                Intent inte = new Intent(Activity_SetupSuccess.this, Activity_LogInScreen.class);
                startActivity(inte, ActivityOptionsCompat.makeSceneTransitionAnimation(Activity_SetupSuccess.this).toBundle());
                finish();
//                Helper.ActivityAction.startActivityClearStack(Activity_SetupSuccess.this, Activity_LogInScreen.class);
            }
        });
    }
}
