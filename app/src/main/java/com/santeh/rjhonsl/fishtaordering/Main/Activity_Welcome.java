package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.Keys;

/**
 * Created by rjhonsl on 6/3/2016.
 */
public class Activity_Welcome extends AppCompatActivity {


    Activity activity;
    Context context;
    FloatingActionButton btnStart;
    int retries = 3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        activity = this;
        context = Activity_Welcome.this;
        btnStart = (FloatingActionButton) findViewById(R.id.btnStart);

        final EditText edtPin = (EditText) findViewById(R.id.edtPin);
        edtPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (edtPin.getText().toString().equalsIgnoreCase("1425")){
                    Intent intent = new Intent(Activity_Welcome.this, Activity_Wiz_SetServerNum.class);
                    intent.putExtra(Keys.IS_STARTUP, true);
                    edtPin.setText("");
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();
                    startActivity(intent, bundle);
                }else{

                }
            }
        });
//        btnStart.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View v) {
//
//
//
//            }
//        });

    }
}
