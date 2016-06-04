package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Keys;

/**
 * Created by rjhonsl on 6/3/2016.
 */
public class Activity_Wiz_StoreName extends AppCompatActivity {

    DBaseQuery db;
    Button betSetSToreName;
    EditText edtStorename;
    Activity activity;
    Context context;
    Intent receivedIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setstore_name);

        activity = this;
        context = Activity_Wiz_StoreName.this;

        receivedIntent = getIntent();

        betSetSToreName = (Button) findViewById(R.id.btnSetStoreName);
        edtStorename = (EditText) findViewById(R.id.edtStoreName);
        db = new DBaseQuery(this);
        db.open();

        betSetSToreName.setEnabled(false);

        if (!receivedIntent.hasExtra(Keys.IS_STARTUP)){
            edtStorename.setText(db.getStoreName());
        }


        betSetSToreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (receivedIntent.hasExtra(Keys.IS_STARTUP)){
                    db.insertSettings(edtStorename.getText().toString(), receivedIntent.getStringExtra(Keys.SETTINGS_SERVERNUMBER), "1", "123456");
                    Intent inte = new Intent(activity, Activity_SetupSuccess.class);
//                    inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(inte, ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
                }else{
                    db.updateSettingsStoreName(edtStorename.getText().toString());
                    finish();
                }
            }
        });


        edtStorename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtStorename.getText().toString().length() > 0){
                    betSetSToreName.setEnabled(true);
                }else{
                    betSetSToreName.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
    }
}
