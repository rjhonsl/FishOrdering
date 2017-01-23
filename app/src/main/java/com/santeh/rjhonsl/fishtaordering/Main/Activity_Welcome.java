package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseHelper;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;
import com.santeh.rjhonsl.fishtaordering.Util.Keys;
import com.santeh.rjhonsl.fishtaordering.Util.Parser;
import com.santeh.rjhonsl.fishtaordering.Util.VarFishtaOrdering;
import com.santeh.rjhonsl.fishtaordering.Util.VolleyAPI;

import java.util.List;

/**
 * Created by rjhonsl on 6/3/2016.
 */
public class Activity_Welcome extends AppCompatActivity {


    Activity activity;
    Context context;
    FloatingActionButton btnStart;
    ProgressDialog pd;
    int retries = 3;
    DBaseQuery db;
    EditText edtPassword, edtUsername;
    String assignedCustomers, wholeName, serverNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        activity = this;
        pd = new ProgressDialog(this);
        db = new DBaseQuery(this);
        db.open();
        context = Activity_Welcome.this;
        btnStart = (FloatingActionButton) findViewById(R.id.btnStart);


        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtUsername = (EditText) findViewById(R.id.edtUserName);
//        edtPin.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                if (edtPin.getText().toString().equalsIgnoreCase("1425")){
//                    Intent intent = new Intent(Activity_Welcome.this, Activity_Wiz_SetServerNum.class);
//                    intent.putExtra(Keys.IS_STARTUP, true);
//                    edtPin.setText("");
//                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();
//                    startActivity(intent, bundle);
//                }else{
//
//                }
//            }
//        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassword.getText().toString().equalsIgnoreCase("") || edtUsername.getText().toString().equalsIgnoreCase("")) {
                    Helper.toast.long_(activity, "Both username and password is needed.");
                }else{
                    login(edtUsername.getText().toString(), edtPassword.getText().toString());
                }
            }
        });

    }


    private void login(final String email, final String password){

        pd.setCancelable(false);
        pd.setMessage("Logging in...");
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
                        Helper.toast.long_(activity, "Login Failed: Connection error.");
                    }
                }){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(2500, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyAPI api = new VolleyAPI();
        api.addToReqQueue(postRequest, context);


    }

    private void getStoresAssigned(String assignedCustomers){
        pd.setCancelable(false);
        pd.setMessage("Getting your assigned customers");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setProgress(100/3);
        pd.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST,
                Helper.variables.getStoresByArrayID+"?" +
                        "username="+edtUsername.getText().toString()+"&" +
                        "password="+edtPassword.getText().toString()+"&" +
                        "storeArray="+assignedCustomers,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {

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

                                db.deleteALL(DBaseHelper.TBL_CUST);
                                cust_query1 = cust_query1 + ";";
                                db.rawQuery(cust_query1);
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

    private void getItems() {
        pd.setCancelable(false);
        pd.setMessage("Getting items...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setProgress(100/2);
        pd.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.selectItems+"?username="+edtUsername.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (!response.substring(0,1).equalsIgnoreCase("0")) {
                            db.deleteALL(DBaseHelper.TBL_ITEMS);
                            final List<VarFishtaOrdering> itemsList = Parser.parseFeed(response, context);
                            if (itemsList != null) {
                                if (itemsList.size()>0){
                                    pd.setMessage("Putting items on local DB.. ");
                                    pd.show();

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
                                    }, 15000);

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
                "username="+edtUsername.getText().toString()+"&" +
                "password="+edtPassword.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (!response.substring(0,1).equalsIgnoreCase("0")) {
                            final List<VarFishtaOrdering> itemsList = Parser.parseFeed(response, context);
                            if (itemsList != null) {
                                if (itemsList.size()>0){
                                    serverNumber = itemsList.get(Keys.SETTINGS_DEFAULT_SERVERNUM).getDs_value();

//                                    Helper.toast.indefinite(activity, response + " " + serverNumber + " " + wholeName + " " + assignedCustomers + " ");

                                    Log.d("ASSIGNED CUSTOMERS", assignedCustomers);

                                    db.insertKeyValSettings(Keys.SETTINGS_STOREIDS, assignedCustomers);
                                    db.insertSettings(
                                    wholeName, //full name
                                    serverNumber, //server number
                                    assignedCustomers, //all customers
                                    "1425");
//
                                    pd.dismiss();
                                    Helper.toast.indefinite(activity, "Success");
                                    Intent intent = new Intent(activity, Activity_SetupSuccess.class);
                                    startActivity(intent);

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
