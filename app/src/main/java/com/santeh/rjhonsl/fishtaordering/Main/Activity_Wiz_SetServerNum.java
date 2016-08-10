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
import android.widget.TextView;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Keys;

/**
 * Created by rjhonsl on 6/3/2016.
 */
public class Activity_Wiz_SetServerNum extends AppCompatActivity {

    DBaseQuery db;
    Button btnSetServerNum;
    EditText edtServerNum;
    TextView txtprompt;
    Activity activity;
    Context context;
    Intent receivedIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setservernum);

        activity = this;
        context = Activity_Wiz_SetServerNum.this;

        receivedIntent = getIntent();

        btnSetServerNum = (Button) findViewById(R.id.btnSetServerNum);
        edtServerNum = (EditText) findViewById(R.id.edtServerNum);
        txtprompt = (TextView) findViewById(R.id.txtServerNumPrompt);
        db = new DBaseQuery(this);
        db.open();

        btnSetServerNum.setEnabled(false);

        if (!receivedIntent.hasExtra(Keys.IS_STARTUP)){
            edtServerNum.setText(db.getServerNum());
        }
        btnSetServerNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtServerNum.getText().toString().length() < 11){
                    txtprompt.setTextColor(getResources().getColor(R.color.red_800));
                    txtprompt.setText("Invalid Number");
                }else if (receivedIntent.hasExtra(Keys.IS_STARTUP)){
                    Intent intent = new Intent(activity, Activity_Wiz_UserName.class);
                    intent.putExtra(Keys.IS_STARTUP, true);
                    intent.putExtra(Keys.SETTINGS_SERVERNUMBER, edtServerNum.getText().toString());
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
                }else{
                    db.updateSettingsServerNum(edtServerNum.getText().toString());
                    finish();
                }
            }
        });


        edtServerNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtServerNum.getText().toString().length() > 10){
                    btnSetServerNum.setEnabled(true);
                }else{
                    btnSetServerNum.setEnabled(false);


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
