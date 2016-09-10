package com.santeh.rjhonsl.fishtaordering.Util;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjhonsl on 5/17/2016.
 */
public class Parser {

    public static List<VarFishtaOrdering> parseFeed(String content, Context context) {

        try {
            JSONArray ar = new JSONArray(content);
            List<VarFishtaOrdering> feedlist = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {

                JSONObject obj = ar.getJSONObject(i);
                VarFishtaOrdering fstObj = new VarFishtaOrdering();


                /**
                 * CONDITIONS FOR ITEMS
                 * */
                if (obj.has("itm_id")){
                    fstObj.setItem_id(obj.getString("itm_id"));
                }

                if (obj.has("itm_code")){
                    fstObj.setItem_code(obj.getString("itm_code"));
                }

                if (obj.has("itm_desc")){
                    fstObj.setItem_description(obj.getString("itm_desc"));
                }

                if (obj.has("itm_oldcode")){
                    fstObj.setItem_oldcode(obj.getString("itm_oldcode"));
                }

                if (obj.has("itm_units")){
                    fstObj.setItem_units(obj.getString("itm_units"));
                }

                if (obj.has("itm_isactive")){
                    fstObj.setItem_isActive(obj.getString("itm_isactive"));
                }


                if (obj.has("itm_class")){
                    fstObj.setItem_class(obj.getString("itm_class"));
                    Log.d("QUERY 2", obj.getString("itm_class"));
                }




                /**************
                 * Customers
                 * */
                if (obj.has("cust_id")){
                    fstObj.setCust_id(obj.getString("cust_id"));
                }

                if (obj.has("cust_code")){
                    fstObj.setCust_code(obj.getString("cust_code"));
                }

                if (obj.has("cust_name")){
                    fstObj.setCust_name(obj.getString("cust_name"));
                }

                if (obj.has("cust_type")){
                    fstObj.setCust_type(obj.getString("cust_type"));
                }

                if (obj.has("cust_isactive")){
                    fstObj.setCust_isactive(obj.getString("cust_isactive"));
                }


                //FOR SUER DETAILS
                if (obj.has("usr_id")){
                    fstObj.setUsr_id(obj.getString("usr_id"));
                }
                if (obj.has("usr_fname")){
                    fstObj.setUsr_fname(obj.getString("usr_fname"));
                }
                if (obj.has("usr_lname")){
                    fstObj.setUsr_lname(obj.getString("usr_lname"));
                }
                if (obj.has("usr_deviceid")){
                    fstObj.setUsr_deviceid(obj.getString("usr_deviceid"));
                }
                if (obj.has("usr_devicename")){
                    fstObj.setUsr_devicename(obj.getString("usr_devicename"));
                }
                if (obj.has("usr_isactive")){
                    fstObj.setUsr_isactive(obj.getString("usr_isactive"));
                }
                if (obj.has("usr_username")){
                    fstObj.setUsr_username(obj.getString("usr_username"));
                }
                if (obj.has("usr_password")){
                    fstObj.setUsr_password(obj.getString("usr_password"));
                }
                if (obj.has("usr_assignedCust")){
                    fstObj.setUsr_assignedCust(obj.getString("usr_assignedCust"));
                }

                //ASSIGNED PRODUCT
                if (obj.has("ap_id")){
                    fstObj.setAp_id(obj.getString("ap_id"));
                }
                if (obj.has("ap_custidFk")){
                    fstObj.setAp_custidFk(obj.getString("ap_custidFk"));
                }
                if (obj.has("ap_prodsarray")){
                    fstObj.setAp_prodsarray(obj.getString("ap_prodsarray"));
                }



                //DEFAULT SETTINGS
                if (obj.has("ds_key")){
                    fstObj.setDs_skey(obj.getString("ds_key"));
                }

                if (obj.has("ds_value")){
                    fstObj.setDs_value(obj.getString("ds_value"));
                }



                feedlist.add(fstObj);

            }

            return feedlist;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
