package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseHelper;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;
import com.santeh.rjhonsl.fishtaordering.Util.Keys;
import com.santeh.rjhonsl.fishtaordering.Util.Parser;
import com.santeh.rjhonsl.fishtaordering.Util.SimpleFileDialog;
import com.santeh.rjhonsl.fishtaordering.Util.VarFishtaOrdering;
import com.santeh.rjhonsl.fishtaordering.Util.VolleyAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rjhonsl on 6/1/2016.
 */
public class Activity_Settings extends AppCompatActivity {


    Activity activity;
    Context context;
    DBaseQuery db;

    LinearLayout txtServerNum, txtStoreName, txtBackup, txtRestore, txtAbout, llStores;
    TextView lblServerNum, lblUserName, lblAbout, lblStore;
    ProgressDialog pd;
    private String assignedCustomers;
    private String wholeName;
    private String username;
    private String pass;
    private String serverNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = Activity_Settings.this;
        activity = this;

        db = new DBaseQuery(this);
        db.open();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);


        pd = new ProgressDialog(context);
        Helper.random.createFolderToExternal("/.snt/local", activity);

        txtServerNum = (LinearLayout) findViewById(R.id.txtServerNum);
        txtStoreName = (LinearLayout) findViewById(R.id.txtStoreName);
        txtBackup = (LinearLayout) findViewById(R.id.llBackup);
        txtRestore = (LinearLayout) findViewById(R.id.llRestore);
        txtAbout = (LinearLayout) findViewById(R.id.txtAbout);
        llStores = (LinearLayout) findViewById(R.id.llStores);

        lblServerNum = (TextView) findViewById(R.id.lblServerNum);
        lblUserName = (TextView) findViewById(R.id.lblUserName);
        lblAbout= (TextView) findViewById(R.id.lblAbout);
        lblStore= (TextView) findViewById(R.id.lblstore);


        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int versionCode = pInfo.versionCode;
            lblAbout.setText("Fishta Ordering App " + version + "    rev."+ versionCode );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



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
                startActivity(new Intent(Activity_Settings.this, Activity_Wiz_UserName.class));
            }
        });

        llStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog d = new Dialog(activity);//
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.dialog_pin);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                d.show();

                final EditText edtpin = (EditText) d.findViewById(R.id.dialog_edt_pin);
                edtpin.getBackground().setColorFilter(getApplicationContext().getResources().getColor(R.color.gray_100), PorterDuff.Mode.SRC_IN);

                edtpin.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (edtpin.getText().toString().equalsIgnoreCase("1425")){
                            d.dismiss();
                            startActivity(new Intent(Activity_Settings.this, Activity_Wiz_Stores.class));
                        }
                    }
                });


            }
        });


    }

    public void updateDB(View v){
        if (Helper.getDeviceInfo.isNetworkAvailable(context)){


            final Dialog d = new Dialog(activity);//
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.dialog_login);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            d.show();


            final EditText edtusername = (EditText) d.findViewById(R.id.dialog_edt_username);
            final EditText edtPassword = (EditText) d.findViewById(R.id.dialog_edt_password);
            Button btnLogin = (Button) d.findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edtPassword.getText().toString().equalsIgnoreCase("") || edtusername.getText().toString().equalsIgnoreCase(""))
                    {
                        Helper.toast.short_(activity, "Fill both fields to continue");
                        d.dismiss();
                    }else{
                        login(edtusername.getText().toString(), edtPassword.getText().toString());
                        username = edtusername.getText().toString();
                        pass = edtPassword.getText().toString();
                        d.dismiss();

                    }
                }
            });




//            final Dialog d = Helper.dialogBox.yesNo(activity, "Sync now?", "UPDATE", "NO", "YES");
//            Button btnNo = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
//            Button btnYes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
//
//            btnNo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    d.dismiss();
//                }
//            });
//
//            btnYes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    getItems();
////                    getCustomers();
//                    d.dismiss();
//                }
//            });
        }else{
            Helper.toast.long_(activity, "No internet connection...");
        }



    }



    private void refreshValues() {
        lblServerNum.setText(db.getServerNum());
        lblUserName.setText(db.getUserName());
        List<VarFishtaOrdering> storeList1 = db.getFilteredStores(db.getKeyVal(Keys.SETTINGS_STOREIDS));
        final String[] storeNames = new String[storeList1.size()];

        String stores = "";
        for (int i = 0; i < storeList1.size(); i++) {
            storeNames[i] = storeList1.get(i).getCust_name();
            if (i > 0){
                stores = stores + ", " + storeList1.get(i).getCust_name();
            }else{
                stores = storeList1.get(i).getCust_name();
            }

        }

        lblStore.setText(stores);
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








    private void login(final String email, final String password){

        pd.setCancelable(false);
        pd.setMessage("Validating your account...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setProgress(100/4);
        pd.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST,
                Helper.variables.login+"?" +
                        "username="+email+"&" +
                        "password="+password+"",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {

                        if (Parser.parseFeed(response, context) != null) {
                            if (Parser.parseFeed(response,context).size() > 0){
                                VarFishtaOrdering userDetail =  Parser.parseFeed(response,context).get(0);

                                assignedCustomers = userDetail.getUsr_assignedCust();
                                Log.d("ASSIGNED CUSTOMERS", assignedCustomers);
                                wholeName = userDetail.getUsr_fname() + " " + userDetail.getUsr_lname();
                                getStoresAssigned(assignedCustomers);

                            }else{
                                pd.dismiss();
                                Helper.toast.long_(activity, "Wrong username and password.");
                            }
                        }else {
                            pd.dismiss();
                            Helper.toast.long_(activity, "Wrong username and password.");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Helper.toast.long_(activity, "Sync Failed: "+error.toString());
                    }
                });

        postRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyAPI api = new VolleyAPI();
        api.addToReqQueue(postRequest, context);


    }

    private void getStoresAssigned(String assignedCustomers){
        pd.setCancelable(false);
        pd.setMessage("Getting your assigned customers");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setProgress(100/3);
        pd.show();

        Log.d("ASSIGNED", assignedCustomers);


        StringRequest postRequest = new StringRequest(Request.Method.POST,
                Helper.variables.getStoresByArrayID+"?" +
                        "username="+username+"&" +
                        "password="+pass+"&" +
                        "storeArray="+assignedCustomers,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {

                        Log.d("ASSIGNED RESP", response);
                        List<VarFishtaOrdering> custlist = Parser.parseFeed(response, context);
                        if (custlist!=null){
                            if (custlist.size()> 0){
                                String cust_query1 = "INSERT INTO tbl_cust ( cust_id, cust_code, cust_name, cust_type, cust_isactive, "+ DBaseHelper.CL_CUST_assignedItems+") " +
                                        "VALUES ";

                                for (int i = 0; i < custlist.size(); i++) {
                                    if (i > 0){
                                        cust_query1 = cust_query1 + " ,( '"+custlist.get(i).getCust_id()+"', '"+custlist.get(i).getCust_code().replace("\'","")+"', '"+custlist.get(i).getCust_name().replace("\'"," ")+"', '"+custlist.get(i).getCust_type().replace("'","\'")+"', '"+custlist.get(i).getCust_isactive()+"', '"+custlist.get(i).getAp_prodsarray()+"')";
                                    }else{
                                        cust_query1 = cust_query1 + " ( '"+custlist.get(i).getCust_id()+"', '"+custlist.get(i).getCust_code().replace("\'","")+"', '"+custlist.get(i).getCust_name().replace("\'"," ")+"', '"+custlist.get(i).getCust_type().replace("'","\'")+"', '"+custlist.get(i).getCust_isactive()+"', '"+custlist.get(i).getAp_prodsarray()+"')";
                                    }
                                }

                                populateCustomerDB(cust_query1);
                                getItems();

                            }else{
                                pd.dismiss();
                                Helper.toast.long_(activity, "You have no assigned customer. Please contact administrator.");
                            }
                        }else {
                            pd.dismiss();
                            Helper.toast.long_(activity, "You have no assigned customer. Please contact administrator.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Helper.toast.long_(activity, "Sync interrupted. Please logging in again");
                    }
                })
        {
        };

        VolleyAPI api = new VolleyAPI();
        api.addToReqQueue(postRequest, context);

    }

    private void populateCustomerDB(String cust_query1) {
        db.deleteALL(DBaseHelper.TBL_CUST);
        cust_query1 = cust_query1 + ";";
        db.rawQuery(cust_query1);
    }

    private void getItems() {
        pd.setCancelable(false);
        pd.setMessage("Getting items...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setProgress(100/2);
        pd.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.selectItems+"?username="+username,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (!response.substring(0,1).equalsIgnoreCase("0")) {
                            final List<VarFishtaOrdering> itemsList = Parser.parseFeed(response, context);
                            if (itemsList != null) {
                                if (itemsList.size()>0){
                                    pd.setMessage("Putting items on local DB.. ");
                                    pd.show();
                                    db.deleteALL(DBaseHelper.TBL_ITEMS);
                                    AsyncTask.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int i = 0; i < itemsList.size(); i++) {
                                                db.insertItems(itemsList.get(i).getItem_id(), itemsList.get(i).getItem_code().replace("\'", ""), itemsList.get(i).getItem_description().replace("\'", " "), itemsList.get(i).getItem_oldcode().replace("'", "\'"), itemsList.get(i).getItem_units(), itemsList.get(i).getItem_isActive(), itemsList.get(i).getItem_class());
                                            }
                                        }
                                    });
                                    Handler postDelayed = new Handler();
                                    postDelayed.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getDefaultSettings();
                                        }
                                    }, 7000);

                                }else {
                                    pd.dismiss();
                                    Helper.toast.long_(activity, "No items assigned to your customers. Please contact administrator");
                                }
                            }
                        } else {
                            pd.dismiss();
                            Helper.toast.long_(activity, "Server authentication failed. Please contact admin");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Helper.toast.long_(activity, "Sync failed. Please try again...");
                    }
                });


        VolleyAPI api = new VolleyAPI();
        api.addToReqQueue(postRequest, context);
    }

    private void getDefaultSettings() {
        pd.setCancelable(false);
        pd.setMessage("Finalizing setup...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setProgress(100/1);
        pd.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.getDefaultSettings+"?"+
                "username="+username+"&" +
                "password="+pass,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (!response.substring(0,1).equalsIgnoreCase("0")) {
                            final List<VarFishtaOrdering> itemsList = Parser.parseFeed(response, context);
                            if (itemsList != null) {
                                if (itemsList.size()>0){
                                    db.deleteALL(DBaseHelper.TBL_SETINGS_KEYVAL_PAIR);
                                    db.deleteALL(DBaseHelper.TBL_SETTINGS);
                                    serverNumber = itemsList.get(Keys.SETTINGS_DEFAULT_SERVERNUM).getDs_value();

//                                    Helper.toast.indefinite(activity, response + " " + serverNumber + " " + wholeName + " " + assignedCustomers + " ");

                                    db.insertKeyValSettings(Keys.SETTINGS_STOREIDS, assignedCustomers);
                                    lblServerNum.setText(serverNumber);
                                    lblUserName.setText(wholeName);
                                    db.insertSettings(
                                            wholeName, //full name
                                            serverNumber, //server number
                                            assignedCustomers, //all customers
                                            "1425");
//
                                    pd.dismiss();
                                    Helper.toast.indefinite(activity, "Sync Success");

//                                    Intent intent = new Intent(activity, Activity_SetupSuccess.class);
//                                    startActivity(intent);

                                }else {
                                    pd.dismiss();
                                    Helper.toast.long_(activity, "Cant get your settings. Please contact admin for further support");
                                }
                            }
                        } else {
                            pd.dismiss();
                            Helper.toast.long_(activity, "Server authentication failed. Please contact admin");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Helper.toast.long_(activity, "Sync failed. Please try again...");
                    }
                });


        VolleyAPI api = new VolleyAPI();
        api.addToReqQueue(postRequest, context);
    }

}
