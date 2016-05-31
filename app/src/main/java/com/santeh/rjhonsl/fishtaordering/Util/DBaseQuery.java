package com.santeh.rjhonsl.fishtaordering.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjhonsl on 5/30/2016.
 */
public class DBaseQuery {

    private static final String LOGTAG = "GPS";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase db;

    /********************************************
     * 				DEFAULTS					*
     ********************************************/
    public DBaseQuery(Context context){
        //Log.d("DBSource", "db connect");
        dbhelper = new DBaseHelper(context);
    }

    public  void open(){
        //Log.d("DBSource", "db open");
        db = dbhelper.getWritableDatabase();
    }
    public  void close(){
        //Log.d("DBSource", "db close");
        dbhelper.close();
    }



    /**
     * INSERTS
    **/
    public long insertItems(String code, String description, String oldCOde, String units ){

        ContentValues values = new ContentValues();
        values.put(DBaseHelper.CL_ITEMS_CODE, code);
        values.put(DBaseHelper.CL_ITEMS_DESCRIPTION, description);
        values.put(DBaseHelper.CL_ITEMS_OLD_CODE, oldCOde);
        values.put(DBaseHelper.CL_ITEMS_UNITS, units);

        return  db.insert(DBaseHelper.TBL_ITEMS, null, values);
    }

    public long insertOrderHistory(String sendto, String message, String timeSent, String isMultipart, String isSent, String isStillSending){

        ContentValues values = new ContentValues();
        values.put(DBaseHelper.CL_HST_SENTTO, sendto);
        values.put(DBaseHelper.CL_HST_MESSAGE, message);
        values.put(DBaseHelper.CL_HST_TIMESENT, timeSent);
        values.put(DBaseHelper.CL_HST_ISMULTIPART, isMultipart);
        values.put(DBaseHelper.CL_HST_ISSENT, isSent);
        values.put(DBaseHelper.CL_HST_ISSTILLSENDING, isStillSending);

        return  db.insert(DBaseHelper.TBL_SENTHISTORY, null, values);
    }


    /**
     *
     * @param activity
     * @return List<VarFishtaOrdering>
     */
    public List<VarFishtaOrdering> getAllItems(){
        List<VarFishtaOrdering> itemsList = new ArrayList<>();

        String query = "SELECT * FROM "+DBaseHelper.TBL_ITEMS+" " +
                "ORDER BY " + DBaseHelper.CL_ITEMS_ID + " ASC";
        String[] params = new String[] {};

        Cursor cur =  db.rawQuery(query, params);


        if (cur != null && cur.getCount() > 0) {
            while (cur.moveToNext()) {
                VarFishtaOrdering queriedItem = new VarFishtaOrdering();
                queriedItem.setItem_id(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_ID)));
                queriedItem.setItem_code(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_CODE)));
                queriedItem.setItem_description(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_DESCRIPTION)));
                queriedItem.setItem_oldcode(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_OLD_CODE)));
                queriedItem.setItem_units(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_UNITS)));
                itemsList.add(queriedItem);
            }
        }

        return itemsList;
    }




}
