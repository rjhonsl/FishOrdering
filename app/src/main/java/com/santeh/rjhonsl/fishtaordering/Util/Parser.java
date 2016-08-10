package com.santeh.rjhonsl.fishtaordering.Util;

import android.content.Context;

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




                feedlist.add(fstObj);

            }

            return feedlist;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
