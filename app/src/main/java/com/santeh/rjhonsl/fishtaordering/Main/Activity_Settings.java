package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;
import com.santeh.rjhonsl.fishtaordering.Util.SimpleFileDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;

/**
 * Created by rjhonsl on 6/1/2016.
 */
public class Activity_Settings extends AppCompatActivity {


    Activity activity;
    Context context;
    DBaseQuery db;

    LinearLayout txtServerNum, txtStoreName, txtBackup, txtRestore, txtAbout;
    TextView lblServerNum, lblStoreName, lblAbout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = Activity_Settings.this;
        activity = this;

        db = new DBaseQuery(this);
        db.open();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.gray_50), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        Helper.random.createFolderToExternal("/.snt/local", activity);

        txtServerNum = (LinearLayout) findViewById(R.id.txtServerNum);
        txtStoreName = (LinearLayout) findViewById(R.id.txtStoreName);
        txtBackup = (LinearLayout) findViewById(R.id.llBackup);
        txtRestore = (LinearLayout) findViewById(R.id.llRestore);
        txtAbout = (LinearLayout) findViewById(R.id.txtAbout);

        lblServerNum = (TextView) findViewById(R.id.lblServerNum);
        lblStoreName= (TextView) findViewById(R.id.lblStoreName);
        lblAbout= (TextView) findViewById(R.id.lblAbout);



        txtRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog d = Helper.dialogBox.yesNo(activity,
                        "All data will be restored to the state of when the backup was done. All existing data after the date will not be restored.\n\nAre you sure you want to retore db? ",
                        "Restore", "NO", "YES");
                Button btnyes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                Button btnno  = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);

                btnno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                    }
                });

                btnyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                        if (Helper.random.checkSD(activity)) {
                            /////////////////////////////////////////////////////////////////////////////////////////////////
                            //Create FileOpenDialog and register a callback
                            /////////////////////////////////////////////////////////////////////////////////////////////////
                            SimpleFileDialog FileOpenDialog = new SimpleFileDialog(activity, ".db",
                                    new SimpleFileDialog.SimpleFileDialogListener() {
                                        String m_chosen;

                                        @Override
                                        public void onChosenDir(String chosenDir) {
                                            // The code in this function will be executed when the dialog OK button is pushed
                                            m_chosen = chosenDir;
                                            try {
                                                File sd = Environment.getExternalStorageDirectory();//gets external Directory/address
                                                if (sd.canWrite()) {
                                                    String backupDBPath = "/data/data/com.santeh.rjhonsl.fishtaordering/databases/fishta_ordering.db";//database internal storage path
                                                    File currentDB = new File(backupDBPath);
                                                    File backupDB = new File(m_chosen);

                                                    if (currentDB.exists()) {
                                                        FileChannel src = new FileInputStream(backupDB).getChannel();
                                                        FileChannel dst = new FileOutputStream(currentDB).getChannel();
                                                        dst.transferFrom(src, 0, src.size());
                                                        src.close();
                                                        dst.close();
                                                        Toast.makeText(getApplicationContext(), "Restore was successful", Toast.LENGTH_LONG).show();
                                                        Intent i = getBaseContext().getPackageManager()
                                                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(i);
                                                    }
                                                }
                                            } catch (Exception e) {
                                                Helper.toast.long_(activity, "Failed to Restore: " + String.valueOf(e));
                                            }

                                        }
                                    });

                            //You can change the default filename using the public variable "Default_File_Name"
                            FileOpenDialog.Default_File_Name = "";
                            FileOpenDialog.chooseFile_or_Dir();
                            /////////////////////////////////////////////////////////////////////////////////////////////////
                        } else {
                            Helper.toast.long_(activity, "External storage not available");
                        }
                    }
                });

            }
        });

        txtBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = Helper.dialogBox.yesNo(activity,
                        "Create local backup? ",
                        "Backup", "NO", "YES");
                Button btnyes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                Button btnno  = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);

                btnno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                    }
                });

                btnyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();

                        if(Helper.random.checkSD(activity)){
                            final String inFileName = "/data/data/com.santeh.rjhonsl.fishtaordering/databases/fishta_ordering.db";//current database to be exported
                            File dbFile = new File(inFileName);
                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(dbFile);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            //gets time for naming sequence
                            Date d = new Date();
                            CharSequence s  = DateFormat.format("MMM-dd-yyyy hhmmAA", d.getTime());
                            String curDate = String.valueOf(s);

                            String outFileName = Environment.getExternalStorageDirectory()+"/.snt/local/" + curDate+".db";//output file name

                            // Open the empty db as the output stream
                            OutputStream output = null;
                            try {
                                output = new FileOutputStream(outFileName);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            // Transfer bytes from the inputfile to the outputfile
                            byte[] buffer = new byte[1024];
                            int length;
                            try {
                                while ((length = fis.read(buffer))>0){
                                    output.write(buffer, 0, length);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // Close the streams
                            try {
                                output.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Helper.toast.long_(activity, "Back up Successfull: \n" + curDate);
                        }
                        else{
                            Helper.toast.long_(activity, "External Storage not available!");
                        }

                    }
                });

            }
        });

        refreshValues();

        txtServerNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Settings.this, Activity_Wiz_SetServerNum.class));
            }
        });

        txtStoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Settings.this, Activity_Wiz_StoreName.class));
            }
        });




    }

    private void refreshValues() {
        lblServerNum.setText(db.getServerNum());
        lblStoreName.setText(db.getStoreName());
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
        refreshValues();
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

}
