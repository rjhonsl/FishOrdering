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
        values.put(DBaseHelper.CL_ITEMS_GROUP_CODE, oldCOde);
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


    public long insertSettings(String storeName, String serverNum, String increCount, String pin){

        ContentValues values = new ContentValues();
        values.put(DBaseHelper.CL_SET_STORENAME, storeName);
        values.put(DBaseHelper.CL_SET_SERVERNUM, serverNum);
        values.put(DBaseHelper.CL_SET_INCRECOUNT, increCount);
        values.put(DBaseHelper.CL_SET_PIN, pin);

        return  db.insert(DBaseHelper.TBL_SETTINGS, null, values);
    }


    public long insertOrderedItems(String itemID, String orderId){

        ContentValues values = new ContentValues();
        values.put(DBaseHelper.CL_OI_ITEMID, itemID);
        values.put(DBaseHelper.CL_OI_ORDERID, orderId);

        return  db.insert(DBaseHelper.TBL_ORDEREDITEMS, null, values);
    }



    /**
     * SELECTSS
     */
    public List<VarFishtaOrdering> getAllItems(){
        List<VarFishtaOrdering> itemsList = new ArrayList<>();

        String query = "SELECT * FROM "+DBaseHelper.TBL_ITEMS+" " +
                "ORDER BY " + DBaseHelper.CL_ITEMS_DESCRIPTION + " ASC";

        String[] params = new String[] {};

        Cursor cur =  db.rawQuery(query, params);
//        Log.d("COUNTER", "getAllItems: "+cur.getCount());

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                VarFishtaOrdering queriedItem = new VarFishtaOrdering();
                queriedItem.setItem_id(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_ID)));
                queriedItem.setItem_code(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_CODE)));
                queriedItem.setItem_description(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_DESCRIPTION)));
                queriedItem.setItem_oldcode(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_GROUP_CODE)));
                queriedItem.setItem_units(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_UNITS)));
                itemsList.add(queriedItem);
            }while (cur.moveToNext());
        }

        return itemsList;
    }


    public List<VarFishtaOrdering> getTopTenFav(){
        List<VarFishtaOrdering> itemsList = new ArrayList<>();

        String query = "SELECT *, COUNT(*) as cc from tbl_ordereditems " +
                " " +
                "INNER JOIN tbl_items on tbl_items.itm_code = tbl_ordereditems.oi_itemId " +
                " " +
                "GRoup by tbl_ordereditems.oi_itemId " +
                " " +
                "Order by cc DESC " +
                "LIMIT 10";

        String[] params = new String[] {};

        Cursor cur =  db.rawQuery(query, params);

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                VarFishtaOrdering queriedItem = new VarFishtaOrdering();
                queriedItem.setOi_id(cur.getString(cur.getColumnIndex(DBaseHelper.CL_OI_ID)));
                queriedItem.setOi_itemid(cur.getString(cur.getColumnIndex(DBaseHelper.CL_OI_ITEMID)));
                queriedItem.setOi_orderid(cur.getString(cur.getColumnIndex(DBaseHelper.CL_OI_ORDERID)));

                queriedItem.setItem_id(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_ID)));
                queriedItem.setItem_code(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_CODE)));
                queriedItem.setItem_description(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_DESCRIPTION)));
                queriedItem.setItem_oldcode(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_GROUP_CODE)));
                queriedItem.setItem_units(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_UNITS)));

                itemsList.add(queriedItem);
            }while (cur.moveToNext());
        }

        return itemsList;
    }


    public VarFishtaOrdering getSettings(){

        String query = "SELECT * FROM "+DBaseHelper.TBL_SETTINGS;
        String[] params = new String[] {};

        Cursor cur =  db.rawQuery(query, params);
        VarFishtaOrdering queriedItem = new VarFishtaOrdering();

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                queriedItem.setSet_id(cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_ID)));
                queriedItem.setSet_servernum(cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_SERVERNUM)));
                queriedItem.setSet_increCount(cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_INCRECOUNT)));
                queriedItem.setSet_setPin(cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_PIN)));
                queriedItem.setSet_storeName(cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_STORENAME)));
            } while ((cur.moveToNext()));
        }

        return queriedItem;
    }


    public String getServerNum(){
        String query = "SELECT * FROM "+DBaseHelper.TBL_SETTINGS;
        String[] params = new String[] {};
        String serverNum = "";
        Cursor cur =  db.rawQuery(query, params);

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                serverNum = cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_SERVERNUM));
            } while ((cur.moveToNext()));
        }

        return serverNum;
    }

    public String getTableList(){
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        String tables = "";
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                tables =  " " + c.getString(0);
                c.moveToNext();
            }
        }
        return tables;
    }



    public List<VarFishtaOrdering> getSearchItems(String keyword){
        List<VarFishtaOrdering> itemsList = new ArrayList<>();

        String query = "SELECT * from tbl_items WHERE " +
                "tbl_items.itm_desc LIKE '%"+keyword+"%'";

        String[] params = new String[] {};

        Cursor cur =  db.rawQuery(query, params);

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                VarFishtaOrdering queriedItem = new VarFishtaOrdering();
                queriedItem.setItem_id(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_ID)));
                queriedItem.setItem_code(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_CODE)));
                queriedItem.setItem_description(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_DESCRIPTION)));
                queriedItem.setItem_oldcode(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_GROUP_CODE)));
                queriedItem.setItem_units(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_UNITS)));
                itemsList.add(queriedItem);
            }while (cur.moveToNext());
        }

        return itemsList;
    }

    public String getStoreName(){
        String query = "SELECT * FROM "+DBaseHelper.TBL_SETTINGS;
        String[] params = new String[] {};
        String storeName = "";
        Cursor cur =  db.rawQuery(query, params);

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                storeName = cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_STORENAME));
            } while ((cur.moveToNext()));
        }

        return storeName;
    }

    public int getSettingsCount(){
        String query = "SELECT * FROM "+DBaseHelper.TBL_SETTINGS;
        String[] params = new String[] {};

       return db.rawQuery(query, params).getCount();
    }

    public String getitemDescription(String id){

        String query = "SELECT * from tbl_items WHERE " +
                "tbl_items.itm_code = '"+id+"'";

        String[] params = new String[] {};
        String itemDEsc = "";

        Cursor cur =  db.rawQuery(query, params);

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                itemDEsc = cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_DESCRIPTION));

            }while (cur.moveToNext());
        }
        return itemDEsc;
    }


    public List<VarFishtaOrdering> getAllOrderHistory(){
        List<VarFishtaOrdering> orderHistoryList = new ArrayList<>();
        String query = "SELECT * FROM "+DBaseHelper.TBL_SENTHISTORY+" " +
                "ORDER BY " + DBaseHelper.CL_HST_TIMESENT + " DESC";
        String[] params = new String[] {};

        Cursor cur =  db.rawQuery(query, params);

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                VarFishtaOrdering queriedItem = new VarFishtaOrdering();
                queriedItem.setHst_id(cur.getString(cur.getColumnIndex(DBaseHelper.CL_HST_ID)));
                queriedItem.setHst_sendTo(cur.getString(cur.getColumnIndex(DBaseHelper.CL_HST_SENTTO)));
                queriedItem.setHst_message(cur.getString(cur.getColumnIndex(DBaseHelper.CL_HST_MESSAGE)));
                queriedItem.setHst_timesent(cur.getString(cur.getColumnIndex(DBaseHelper.CL_HST_TIMESENT)));
                queriedItem.setHst_isMultipart(cur.getString(cur.getColumnIndex(DBaseHelper.CL_HST_ISMULTIPART)));
                queriedItem.setHst_isSent(cur.getString(cur.getColumnIndex(DBaseHelper.CL_HST_ISSENT)));
                queriedItem.setHst_isStillSending(cur.getString(cur.getColumnIndex(DBaseHelper.CL_HST_ISSTILLSENDING)));
                orderHistoryList.add(queriedItem);
            } while (cur.moveToNext());
        }

        return orderHistoryList;
    }

    public String rearrangeItems(String raw){

        String[] splitted = raw.split(";");
        String arranged = "";
        for (int i = 0; i < splitted.length; i++) {
            if (i==0){
                arranged = getitemDescription(splitted[i].split(",")[0]) + " "+ splitted[i].split(",")[1]+""+ splitted[i].split(",")[2];
            }else{
                arranged = arranged + ",\n" + getitemDescription(splitted[i].split(",")[0]) + " "+ splitted[i].split(",")[1]+""+ splitted[i].split(",")[2];;
            }
        }
        return arranged;
    }



    /**
     * UPDATESS
     */

    public int updateSentHistory(String hstid, String sendto, String message, String timeSent, String isMultipart, String isSent, String isStillSending){
        String where = DBaseHelper.CL_HST_ID + " = '" + hstid+"'";
        ContentValues newValues = new ContentValues();
        newValues.put(DBaseHelper.CL_HST_SENTTO, sendto);
        newValues.put(DBaseHelper.CL_HST_MESSAGE, message);
        newValues.put(DBaseHelper.CL_HST_TIMESENT, timeSent);
        newValues.put(DBaseHelper.CL_HST_ISMULTIPART, isMultipart);
        newValues.put(DBaseHelper.CL_HST_ISSENT, isSent);
        newValues.put(DBaseHelper.CL_HST_ISSTILLSENDING, isStillSending);

        return 	db.update(DBaseHelper.TBL_SENTHISTORY, newValues, where, null);
    }


    public int updateSettingsServerNum(String serverNum){
        ContentValues newValues = new ContentValues();
        String where = DBaseHelper.CL_SET_ID = "1";
        newValues.put(DBaseHelper.CL_SET_SERVERNUM, serverNum);

        return 	db.update(DBaseHelper.TBL_SETTINGS, newValues, where, null);
    }

    public int updateSettingsStoreName(String storeName){
        ContentValues newValues = new ContentValues();
        String where = DBaseHelper.CL_SET_ID = "1";
        newValues.put(DBaseHelper.CL_SET_STORENAME, storeName);

        return 	db.update(DBaseHelper.TBL_SETTINGS, newValues, where, null);
    }

    public int updateSettingsPin(String loolcount){
        ContentValues newValues = new ContentValues();
        String where = DBaseHelper.CL_SET_ID = "1";
        newValues.put(DBaseHelper.CL_SET_INCRECOUNT, loolcount);

        return 	db.update(DBaseHelper.TBL_SETTINGS, newValues, where, null);
    }

    public int updateSettingsLoopCount(String pin){
        ContentValues newValues = new ContentValues();
        String where = DBaseHelper.CL_SET_ID = "1";
        newValues.put(DBaseHelper.CL_SET_PIN, pin);

        return 	db.update(DBaseHelper.TBL_SETTINGS, newValues, where, null);
    }


    /**
     * DELETES
     * */

    public boolean deleteHistory(String rowId) {
        String where = DBaseHelper.CL_HST_ID + "= " + rowId;
        return db.delete(DBaseHelper.TBL_SENTHISTORY, where, null) != 0;
    }

}
