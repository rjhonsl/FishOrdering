package com.santeh.rjhonsl.fishtaordering.Main.SetupWizard;

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
public class Activity_Wiz_UserName extends AppCompatActivity {

    DBaseQuery db;
    Button btnSetStoreName;
    EditText edtUserName;
    Activity activity;
    Context context;
    Intent receivedIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setuser_name);

        activity = this;
        context = Activity_Wiz_UserName.this;

        receivedIntent = getIntent();

        btnSetStoreName = (Button) findViewById(R.id.btnSetStoreName);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        db = new DBaseQuery(this);
        db.open();

        btnSetStoreName.setEnabled(false);

        if (!receivedIntent.hasExtra(Keys.IS_STARTUP)){
            edtUserName.setText(db.getUserName());
        }


        btnSetStoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (receivedIntent.hasExtra(Keys.IS_STARTUP)){
//                    db.insertSettings(edtUserName.getText().toString(), , "1", "123456");
                    Intent inte = new Intent(activity, Activity_Wiz_Stores.class);
                    inte.putExtra(Keys.IS_STARTUP, true);
                    inte.putExtra(Keys.SETTINGS_SERVERNUMBER, receivedIntent.getStringExtra(Keys.SETTINGS_SERVERNUMBER));
                    inte.putExtra(Keys.SETTINGS_USERNAME, edtUserName.getText().toString());
//                    inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(inte, ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
                }else{
                    db.updateSettingsStoreName(edtUserName.getText().toString());
                    finish();
                }
            }
        });


        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtUserName.getText().toString().length() > 0){
                    btnSetStoreName.setEnabled(true);
                }else{
                    btnSetStoreName.setEnabled(false);
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
