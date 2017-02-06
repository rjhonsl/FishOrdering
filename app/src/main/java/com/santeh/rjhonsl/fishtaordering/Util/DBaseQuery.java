package com.santeh.rjhonsl.fishtaordering.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.santeh.rjhonsl.fishtaordering.Pojo.DeliveryConfirmationPojo;
import com.santeh.rjhonsl.fishtaordering.Pojo.OrderConfirmationPojo;

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
     * DEFAULTS					*
     ********************************************/
    public DBaseQuery(Context context) {
        //Log.d("DBSource", "db connect");
        dbhelper = new DBaseHelper(context);
    }

    public void open() {
        //Log.d("DBSource", "db open");
        db = dbhelper.getWritableDatabase();
    }

    public void close() {
        //Log.d("DBSource", "db close");
        dbhelper.close();
    }


    /**
     * INSERTS
     **/



    public long insertCustomer(String id, String code, String name, String type, String isActive) {

        ContentValues values = new ContentValues();
        values.put(DBaseHelper.CL_CUST_ID, id);
        values.put(DBaseHelper.CL_CUST_CODE, code);
        values.put(DBaseHelper.CL_CUST_NAME, name);
        values.put(DBaseHelper.CL_CUST_TYPE, type);
        values.put(DBaseHelper.CL_CUST_isActive, isActive);

        return db.insert(DBaseHelper.TBL_CUST, null, values);
    }

    public long insertORConfirmation(String id, String ORnumber, String sender, String content, String timeReceived, String custID, String isSent, String batchnumber, String items) {

        ContentValues values = new ContentValues();
        if (id.length()>0){
            values.put(DBConstants.OrderConfirmation.id, id);
        }
        values.put(DBConstants.OrderConfirmation.ORnumber, ORnumber);
        values.put(DBConstants.OrderConfirmation.sender, sender);
        values.put(DBConstants.OrderConfirmation.content, content);
        values.put(DBConstants.OrderConfirmation.timeReceived, timeReceived);
        values.put(DBConstants.OrderConfirmation.custID, custID);
        values.put(DBConstants.OrderConfirmation.isSent, isSent);
        values.put(DBConstants.OrderConfirmation.batchnumber, batchnumber);
        values.put(DBConstants.OrderConfirmation.items, items);

        return db.insert(DBConstants.OrderConfirmation.tableName, null, values);
    }
    public long insertDeliveryConfirmation(String drNumber, String sender, String items_received, String items_sent, String content_received, String content_sent, String timeReceived, String timeSent, String custID,
                                           String isSent, String isRead, String batchnumber) {

        ContentValues values = new ContentValues();
        values.put(DBConstants.DeliveryConfirmation.drNumber, drNumber);
        values.put(DBConstants.DeliveryConfirmation.sender, sender);
        values.put(DBConstants.DeliveryConfirmation.items_received, items_received);
        values.put(DBConstants.DeliveryConfirmation.items_sent, items_sent);
        values.put(DBConstants.DeliveryConfirmation.content_received, content_received);
        values.put(DBConstants.DeliveryConfirmation.content_sent, content_sent);
        values.put(DBConstants.DeliveryConfirmation.timeReceived, timeReceived);
        values.put(DBConstants.DeliveryConfirmation.timeSent, timeSent);
        values.put(DBConstants.DeliveryConfirmation.custID, custID);
        values.put(DBConstants.DeliveryConfirmation.isSent, isSent);
        values.put(DBConstants.DeliveryConfirmation.isRead, isRead);
        values.put(DBConstants.DeliveryConfirmation.item_batchNumber, batchnumber);

        return db.insert(DBConstants.DeliveryConfirmation.tableName, null, values);
    }


    public long insertItems(String id, String code, String description, String oldCOde, String units, String isactive, String itemclass) {

        ContentValues values = new ContentValues();
        values.put(DBaseHelper.CL_ITEMS_ID, id);
        values.put(DBaseHelper.CL_ITEMS_CODE, code);
        values.put(DBaseHelper.CL_ITEMS_DESCRIPTION, description);
        values.put(DBaseHelper.CL_ITEMS_GROUP_CODE, oldCOde);
        values.put(DBaseHelper.CL_ITEMS_UNITS, units);
        values.put(DBaseHelper.CL_ITEMS_isActive, isactive);
        values.put(DBaseHelper.CL_ITEMS_classification, itemclass);

        return db.insert(DBaseHelper.TBL_ITEMS, null, values);
    }

    public void rawQuery(String sqlString) {
        db.execSQL(sqlString);
    }

    public long insertOrderHistory(String sendto, String message, String timeSent, String isMultipart, String isSent, String isStillSending) {

        ContentValues values = new ContentValues();
        values.put(DBaseHelper.CL_HST_SENTTO, sendto);
        values.put(DBaseHelper.CL_HST_MESSAGE, message);
        values.put(DBaseHelper.CL_HST_TIMESENT, timeSent);
        values.put(DBaseHelper.CL_HST_ISMULTIPART, isMultipart);
        values.put(DBaseHelper.CL_HST_ISSENT, isSent);
        values.put(DBaseHelper.CL_HST_ISSTILLSENDING, isStillSending);

        return db.insert(DBaseHelper.TBL_SENTHISTORY, null, values);
    }


    public long insertSettings(String userName, String serverNum, String storesOfUser, String pin) {

        ContentValues values = new ContentValues();
        values.put(DBaseHelper.CL_SET_UserName, userName);
        values.put(DBaseHelper.CL_SET_SERVERNUM, serverNum);
        values.put(DBaseHelper.CL_SET_INCRECOUNT, storesOfUser);
        values.put(DBaseHelper.CL_SET_PIN, pin);

        return db.insert(DBaseHelper.TBL_SETTINGS, null, values);
    }


    public long insertKeyValSettings(String key, String value) {

        ContentValues values = new ContentValues();
        values.put(DBaseHelper.CL_KV_KEY, key);
        values.put(DBaseHelper.CL_KV_VALUE, value);

        return db.insert(DBaseHelper.TBL_SETINGS_KEYVAL_PAIR, null, values);
    }


    public long insertOrderedItems(String itemID, String orderId) {

        ContentValues values = new ContentValues();
        values.put(DBaseHelper.CL_OI_ITEMID, itemID);
        values.put(DBaseHelper.CL_OI_ORDERID, orderId);

        return db.insert(DBaseHelper.TBL_ORDEREDITEMS, null, values);
    }


    /**
     * SELECTSS
     */
    public List<VarFishtaOrdering> getAllItems() {
        List<VarFishtaOrdering> itemsList = new ArrayList<>();

        String query = "SELECT * FROM " + DBaseHelper.TBL_ITEMS + " " +
                " WHERE " + DBaseHelper.CL_ITEMS_isActive + " = '1' "+
                "ORDER BY " + DBaseHelper.CL_ITEMS_DESCRIPTION + " ASC";

        String[] params = new String[]{};

        Cursor cur = db.rawQuery(query, params);
        try {
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
                } while (cur.moveToNext());
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }




        return itemsList;
    }


    public List<VarFishtaOrdering> getStoreItems(String storeID) {
        List<VarFishtaOrdering> itemsList = new ArrayList<>();
        String[] params = new String[]{};
        String itemArray;
        String where1 = "";
        String query_itemArray = "SELECT cust_assigneditems FROM tbl_cust where cust_id='"+storeID+"';";
        Cursor cur_custitem = db.rawQuery(query_itemArray, params);
        if (cur_custitem!=null){
            if (cur_custitem.getCount()>0){
                cur_custitem.moveToFirst();
                do {
                    itemArray = cur_custitem.getString(cur_custitem.getColumnIndex(DBaseHelper.CL_CUST_assignedItems));
                } while (cur_custitem.moveToNext());
                String[] splitteditems = itemArray.split(",");
                for (int i = 0; i < splitteditems.length; i++) {
                    if (i==0){
                        where1 = where1 + " "+DBaseHelper.CL_ITEMS_ID + " = '" + splitteditems[i] + "' ";
                    }else{
                        where1 = where1 + " OR "+DBaseHelper.CL_ITEMS_ID + " = '" + splitteditems[i] + "' ";
                    }
                }
            }
        }


        String query = "SELECT * FROM " + DBaseHelper.TBL_ITEMS + " " +
                " WHERE " + DBaseHelper.CL_ITEMS_isActive + " = '1' AND "+
                where1 +
                " ORDER BY " + DBaseHelper.CL_ITEMS_DESCRIPTION + " ASC";
        Log.d(LOGTAG, query);

        Cursor cur = db.rawQuery(query, params);
        try {
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
                } while (cur.moveToNext());
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }




        return itemsList;
    }



    public List<VarFishtaOrdering> getAllCustomers() {
        List<VarFishtaOrdering> itemsList = new ArrayList<>();

        String query = "SELECT * FROM " + DBaseHelper.TBL_CUST + " " +
                "ORDER BY " + DBaseHelper.CL_CUST_NAME + " ASC";

        String[] params = new String[]{};

        Cursor cur = db.rawQuery(query, params);
//        Log.d("COUNTER", "getAllItems: "+cur.getCount());
        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    VarFishtaOrdering queriedItem = new VarFishtaOrdering();
                    queriedItem.setCust_id(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_ID)));
                    queriedItem.setCust_code(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_CODE)));
                    queriedItem.setCust_name(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_NAME)));
                    queriedItem.setCust_type(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_TYPE)));
                    queriedItem.setCust_isactive(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_isActive)));
                    queriedItem.setCust_assignedItems(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_assignedItems)));
                    itemsList.add(queriedItem);
                } while (cur.moveToNext());
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }
        return itemsList;
    }


    public List<VarFishtaOrdering> getCust_OUTLET() {
        List<VarFishtaOrdering> itemsList = new ArrayList<>();

        String query = "SELECT * FROM " + DBaseHelper.TBL_CUST + " " +
                "WHERE " + DBaseHelper.CL_CUST_TYPE + " = '" + Keys.CUST_TYPE_OUTLET + "' AND " + DBaseHelper.CL_CUST_isActive + " = '1' "+
                "ORDER BY " + DBaseHelper.CL_CUST_NAME + " ASC";

        String[] params = new String[]{};

        Cursor cur = db.rawQuery(query, params);
        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    VarFishtaOrdering queriedItem = new VarFishtaOrdering();
                    queriedItem.setCust_id(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_ID)));
                    queriedItem.setCust_code(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_CODE)));
                    queriedItem.setCust_name(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_NAME)));
                    queriedItem.setCust_type(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_TYPE)));
                    queriedItem.setCust_isactive(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_isActive)));
                    queriedItem.setCust_assignedItems(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_assignedItems)));
                    itemsList.add(queriedItem);
                } while (cur.moveToNext());
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }

        return itemsList;
    }


    public List<VarFishtaOrdering> getFilteredStores(String stores) {
        List<VarFishtaOrdering> itemsList = new ArrayList<>();

        String[] splitted = stores.split(",");
        String condition = "";
        for (int i = 0; i < splitted.length; i++) {
            if (i > 0) {
                condition = condition + " OR " + DBaseHelper.CL_CUST_ID + " = '" + splitted[i] + "' ";
            } else {
                condition = condition + DBaseHelper.CL_CUST_ID + " = '" + splitted[i] + "' ";
            }
        }

        String query = "SELECT * FROM " + DBaseHelper.TBL_CUST + " " +
                "WHERE " +
                condition
                + "ORDER BY " + DBaseHelper.CL_CUST_NAME + " ASC";

        String[] params = new String[]{};

        Cursor cur = db.rawQuery(query, params);
//        Log.d("COUNTER", "getAllItems: "+cur.getCount());
        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    VarFishtaOrdering queriedItem = new VarFishtaOrdering();
                    queriedItem.setCust_id(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_ID)));
                    queriedItem.setCust_code(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_CODE)));
                    queriedItem.setCust_name(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_NAME)));
                    queriedItem.setCust_type(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_TYPE)));
                    queriedItem.setCust_isactive(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_isActive)));
                    queriedItem.setCust_assignedItems(cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_assignedItems)));
                    itemsList.add(queriedItem);
                } while (cur.moveToNext());
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }
        return itemsList;
    }


    public List<VarFishtaOrdering> getTopTenFav() {
        List<VarFishtaOrdering> itemsList = new ArrayList<>();

        String query = "SELECT *, COUNT(*) as cc from tbl_ordereditems " +
                " " +
                "INNER JOIN tbl_items on tbl_items.itm_code = tbl_ordereditems.oi_itemId " +
                " " +
                "GRoup by tbl_ordereditems.oi_itemId " +
                " " +
                "Order by cc DESC " +
                "LIMIT 10";

        String[] params = new String[]{};

        Cursor cur = db.rawQuery(query, params);
        try {
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
                } while (cur.moveToNext());
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }



        return itemsList;
    }


    public VarFishtaOrdering getSettings() {

        String query = "SELECT * FROM " + DBaseHelper.TBL_SETTINGS;
        String[] params = new String[]{};

        Cursor cur = db.rawQuery(query, params);
        VarFishtaOrdering queriedItem = new VarFishtaOrdering();
        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    queriedItem.setSet_id(cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_ID)));
                    queriedItem.setSet_servernum(cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_SERVERNUM)));
                    queriedItem.setSet_increCount(cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_INCRECOUNT)));
                    queriedItem.setSet_setPin(cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_PIN)));
                    queriedItem.setSet_storeName(cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_UserName)));
                } while ((cur.moveToNext()));
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }
        return queriedItem;
    }


    public String getKeyVal(String keyval_id) {

        String query = "SELECT * FROM " + DBaseHelper.TBL_SETINGS_KEYVAL_PAIR + " WHERE " + DBaseHelper.CL_KV_KEY + "" +
                "='" + keyval_id + "'";
        String[] params = new String[]{};
        String values = "";
        Cursor cur = db.rawQuery(query, params);

        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {

                    values = values + cur.getString(cur.getColumnIndex(DBaseHelper.CL_KV_VALUE));
                } while (cur.moveToNext());
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }
        return values;
    }

    public String getStoreName(String storeID) {

        String query = "SELECT * FROM " + DBaseHelper.TBL_CUST + " WHERE " + DBaseHelper.CL_CUST_ID + "" +
                "='" + storeID + "'";
        String[] params = new String[]{};
        String values = "";
        Cursor cur = db.rawQuery(query, params);

        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    values = values + cur.getString(cur.getColumnIndex(DBaseHelper.CL_CUST_NAME));
                } while (cur.moveToNext());
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }




        return values;
    }


    public String getServerNum() {
        String query = "SELECT * FROM " + DBaseHelper.TBL_SETTINGS;
        String[] params = new String[]{};
        String serverNum = "";
        Cursor cur = db.rawQuery(query, params);

        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    serverNum = cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_SERVERNUM));
                } while ((cur.moveToNext()));
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }


        return serverNum;
    }


    public String isDRExisting(String content) {
        String query = "select * from "+DBConstants.DeliveryConfirmation.tableName+" where "+DBConstants.DeliveryConfirmation.content_received +" = '" + content + "'";
        String[] params = new String[]{};
        String id = "";
        Cursor cur = db.rawQuery(query, params);

        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    id = cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.id));
                } while (cur.moveToNext());
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }
        return id;
    }

    public String isOFExisting(String content) {
        String query = "select * from tbl_orconfirmation where oc_content = '" + content + "'";
        String[] params = new String[]{};
        String id = "";
        Cursor cur = db.rawQuery(query, params);

        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    id = cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.id));
                } while (cur.moveToNext());
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }
        return id;
    }

    public String isOFBatchExisting(String ofnumber, String custid, String batchnumber) {
        Log.d("ORDERCONF", ofnumber + " " + custid + " " + batchnumber);
        String query = "select * from tbl_orconfirmation where oc_ornumber = '"+ofnumber+"' AND oc_timeopened = '"+custid+"' AND oc_isread  = "+batchnumber+ "";
        String[] params = new String[]{};
        String id = "";
        Cursor cur = db.rawQuery(query, params);

        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    id = cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.id));
                } while (cur.moveToNext());
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }
        return id;
    }

    public String isDRBatchExisting(String drnumber, String custid, String batchnumber) {
        Log.d("ORDERCONF", drnumber + " " + custid + " " + batchnumber);
        String query = "select * from " +DBConstants.DeliveryConfirmation.tableName + " where " +
                ""+ DBConstants.DeliveryConfirmation.drNumber+" = '"+drnumber+"' AND " +
                " "+ DBConstants.DeliveryConfirmation.custID+" = '"+custid+"' AND " +
                " "+ DBConstants.DeliveryConfirmation.item_batchNumber+"  = "+batchnumber+ "";
        String[] params = new String[]{};
        String id = "";
        Cursor cur = db.rawQuery(query, params);

        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    id = cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.id));
                } while (cur.moveToNext());
            }
        }catch (Exception e){
            Log.e("DB ERROR", e.toString());
        }finally {
            if (cur!=null){
                cur.close();
            }
        }
        return id;
    }

    public String getTableList() {
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        String tables = "";
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                tables = " " + c.getString(0);
                c.moveToNext();
            }
            c.close();
        }
        return tables;
    }


    public List<VarFishtaOrdering> getSearchItems(String keyword) {
        List<VarFishtaOrdering> itemsList = new ArrayList<>();

        String query = "SELECT * from tbl_items WHERE " +
                "tbl_items.itm_isactive = '1' AND tbl_items.itm_desc LIKE '%" + keyword + "%'";

        String[] params = new String[]{};

        Cursor cur = db.rawQuery(query, params);

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
            } while (cur.moveToNext());
            cur.close();
        }

        return itemsList;
    }

    public List<VarFishtaOrdering> getSearchItems(String keyword, String where_group) {
        List<VarFishtaOrdering> itemsList = new ArrayList<>();

        String query = "SELECT * from tbl_items WHERE " +
                "tbl_items.itm_isactive = '1' AND tbl_items.itm_desc LIKE '%" + keyword + "%' AND " + where_group;

        Log.d("QUERY", query);
        String[] params = new String[]{};

        Cursor cur = db.rawQuery(query, params);

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                VarFishtaOrdering queriedItem = new VarFishtaOrdering();
                queriedItem.setItem_id(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_ID)));
                queriedItem.setItem_code(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_CODE)));
                queriedItem.setItem_description(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_DESCRIPTION)));
                queriedItem.setItem_oldcode(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_GROUP_CODE)));
                queriedItem.setItem_units(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_UNITS)));
                queriedItem.setItem_class(cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_classification)));
                itemsList.add(queriedItem);
            } while (cur.moveToNext());
            cur.close();
        }

        return itemsList;
    }

    public String getUserName() {
        String query = "SELECT * FROM " + DBaseHelper.TBL_SETTINGS;
        String[] params = new String[]{};
        String storeName = "";
        Cursor cur = db.rawQuery(query, params);

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                storeName = cur.getString(cur.getColumnIndex(DBaseHelper.CL_SET_UserName));
            } while ((cur.moveToNext()));

            cur.close();
        }


        return storeName;
    }

    public int getSettingsCount() {
        String query = "SELECT * FROM " + DBaseHelper.TBL_SETTINGS;
        String[] params = new String[]{};

        return db.rawQuery(query, params).getCount();
    }

    public String getitemDescription(String id) {

        String query = "SELECT * from tbl_items WHERE " +
                "tbl_items.itm_code = '" + id + "'";

        String[] params = new String[]{};
        String itemDEsc = "Item not in database";

        Cursor cur = db.rawQuery(query, params);

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                itemDEsc = cur.getString(cur.getColumnIndex(DBaseHelper.CL_ITEMS_DESCRIPTION));

            } while (cur.moveToNext());
            cur.close();
        }
        return itemDEsc;
    }


    public List<VarFishtaOrdering> getAllOrderHistory() {
        List<VarFishtaOrdering> orderHistoryList = new ArrayList<>();
        String query = "SELECT * FROM " + DBaseHelper.TBL_SENTHISTORY + " " +
                "ORDER BY " + DBaseHelper.CL_HST_TIMESENT + " DESC";
        String[] params = new String[]{};

        Cursor cur = db.rawQuery(query, params);

        try {
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
        }finally {
            if (cur != null) {
                cur.close();
            }
        }


        return orderHistoryList;
    }


    public List<OrderConfirmationPojo> getAllConfirmation() {
        List<OrderConfirmationPojo> orlist = new ArrayList<>();
        String query = "SELECT * FROM " + DBConstants.OrderConfirmation.tableName + " " +
                "ORDER BY " + DBConstants.OrderConfirmation.ORnumber + " DESC, oc_isread";

        String[] params = new String[]{};
        Cursor cur = db.rawQuery(query, params);

        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    OrderConfirmationPojo queriedItem = new OrderConfirmationPojo();
                    queriedItem.setId(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.id)));
                    queriedItem.setORnumber(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.ORnumber)));
                    queriedItem.setSender(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.sender)));
                    queriedItem.setContent(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.content)));
                    queriedItem.setTimeReceived(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.timeReceived)));
                    queriedItem.setCustID(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.custID)));
                    queriedItem.setIsSent(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.isSent)));
                    queriedItem.setBatchNumber(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.batchnumber)));
                    orlist.add(queriedItem);
                } while (cur.moveToNext());
            }
        }finally {
            if (cur != null) {
                cur.close();
            }
        }
        return orlist;
    }


    public List<OrderConfirmationPojo> getbatchedConfirmation() {
        List<OrderConfirmationPojo> orlist = new ArrayList<>();
        String query = "select *, group_concat(oc_items, ';') as allitems, " +
                "count(oc_ornumber) as batchcounter from tbl_orconfirmation " +
                "group by oc_ornumber " +
                "order by oc_ornumber desc, oc_isread";

        String[] params = new String[]{};
        Cursor cur = db.rawQuery(query, params);

        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    OrderConfirmationPojo queriedItem = new OrderConfirmationPojo();
                    queriedItem.setId(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.id)));
                    queriedItem.setORnumber(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.ORnumber)));
                    queriedItem.setSender(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.sender)));
                    queriedItem.setContent(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.content)));
                    queriedItem.setTimeReceived(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.timeReceived)));
                    queriedItem.setCustID(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.custID)));
                    queriedItem.setIsSent(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.isSent)));
                    queriedItem.setBatchNumber(cur.getString(cur.getColumnIndex(DBConstants.OrderConfirmation.batchnumber)));
                    queriedItem.setAllitems(cur.getString(cur.getColumnIndex("allitems")));
                    orlist.add(queriedItem);
                } while (cur.moveToNext());
            }
        }finally {
            if (cur != null) {
                cur.close();
            }
        }
        return orlist;
    }

    public List<DeliveryConfirmationPojo> getDRAllDRConfirmation() {
        List<DeliveryConfirmationPojo> drList = new ArrayList<>();
        String query = "SELECT * FROM " + DBConstants.DeliveryConfirmation.tableName + " " +
                "ORDER BY " + DBConstants.DeliveryConfirmation.timeReceived + " DESC";

        String[] params = new String[]{};

        Cursor cur = db.rawQuery(query, params);

        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    DeliveryConfirmationPojo queriedItem = new DeliveryConfirmationPojo();
                    queriedItem.setId(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.id)));
                    queriedItem.setDrNumber(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.drNumber)));
                    queriedItem.setSender(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.sender)));
                    queriedItem.setItems_received(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.items_received)));
                    queriedItem.setItems_sent(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.items_sent)));
                    queriedItem.setContent_received(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.content_received)));
                    queriedItem.setContent_sent(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.content_sent)));
                    queriedItem.setTimeReceived(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.timeReceived)));
                    queriedItem.setTimeSent(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.timeSent)));
                    queriedItem.setCustID(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.custID)));
                    queriedItem.setIsSent(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.isSent)));
                    queriedItem.setIsRead(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.isRead)));

                    drList.add(queriedItem);
                } while (cur.moveToNext());
            }
        }finally {
            if (cur != null) {
                cur.close();
            }
        }

        return drList;
    }


    public List<DeliveryConfirmationPojo> getBatchedDeliverConfirmation() {
        List<DeliveryConfirmationPojo> drList = new ArrayList<>();
        String query = "\n" +
                "SELECT *, GROUP_CONCAT(dr_items_received, ';') as allitems\n" +
                "FROM (\n"  +
                "   SELECT *\n" +
                "   FROM tbl_drconfirmation\n" +
                "   ORDER BY dr_batch_number\n" +
                "   )\n" +
                "\n" +
                "group by dc_drnumber \n" +
                "order by "+DBConstants.DeliveryConfirmation.timeReceived+" DESC, dc_drnumber  ;";

        String[] params = new String[]{};

        Cursor cur = db.rawQuery(query, params);

        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    DeliveryConfirmationPojo queriedItem = new DeliveryConfirmationPojo();
                    queriedItem.setId(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.id)));
                    queriedItem.setDrNumber(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.drNumber)));
                    queriedItem.setAllitems(cur.getString(cur.getColumnIndex("allitems")));
                    queriedItem.setSender(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.sender)));
                    queriedItem.setItems_received(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.items_received)));
                    queriedItem.setItems_sent(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.items_sent)));
                    queriedItem.setContent_received(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.content_received)));
                    queriedItem.setContent_sent(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.content_sent)));
                    queriedItem.setTimeReceived(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.timeReceived)));
                    queriedItem.setTimeSent(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.timeSent)));
                    queriedItem.setCustID(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.custID)));
                    queriedItem.setIsSent(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.isSent)));
                    queriedItem.setIsRead(cur.getString(cur.getColumnIndex(DBConstants.DeliveryConfirmation.isRead)));

                    drList.add(queriedItem);
                } while (cur.moveToNext());
            }
        }finally {
            if (cur != null) {
                cur.close();
            }
        }

        return drList;
    }


    public String rearrangeItems(String raw) {

        String[] splitted = raw.split(";");
        String arranged = "";
        for (int i = 1; i < splitted.length; i++) {
            if (i == 1) {
                arranged = arranged +  getitemDescription(splitted[i].split(",")[0]) + " " + splitted[i].split(",")[1] + "" + splitted[i].split(",")[2];
            } else {
                arranged = arranged + ",\n" + getitemDescription(splitted[i].split(",")[0]) + " " + splitted[i].split(",")[1] + "" + splitted[i].split(",")[2];
            }
        }
        return arranged;
    }


    public String rearrangeBatchItems(String raw) {

        String[] splitted = raw.split(";");
        String arranged = "";
        for (int i = 0; i < splitted.length; i++) {
            if (i == 0) {
                arranged = arranged +  getitemDescription(splitted[i].split(",")[0]) + " " + splitted[i].split(",")[1] + "" + splitted[i].split(",")[2];
            } else {
                arranged = arranged + ",\n" + getitemDescription(splitted[i].split(",")[0]) + " " + splitted[i].split(",")[1] + "" + splitted[i].split(",")[2];
            }
        }
        return arranged;
    }


    /**
     * UPDATESS
     */

    public int updateSentHistory(String hstid, String sendto, String message, String timeSent, String isMultipart, String isSent, String isStillSending) {
        String where = DBaseHelper.CL_HST_ID + " = '" + hstid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put(DBaseHelper.CL_HST_SENTTO, sendto);
        newValues.put(DBaseHelper.CL_HST_MESSAGE, message);
        newValues.put(DBaseHelper.CL_HST_TIMESENT, timeSent);
        newValues.put(DBaseHelper.CL_HST_ISMULTIPART, isMultipart);
        newValues.put(DBaseHelper.CL_HST_ISSENT, isSent);
        newValues.put(DBaseHelper.CL_HST_ISSTILLSENDING, isStillSending);

        return db.update(DBaseHelper.TBL_SENTHISTORY, newValues, where, null);
    }


    public int updateSettingsServerNum(String serverNum) {
        ContentValues newValues = new ContentValues();
        String where = DBaseHelper.CL_SET_ID = "1";
        newValues.put(DBaseHelper.CL_SET_SERVERNUM, serverNum);

        return db.update(DBaseHelper.TBL_SETTINGS, newValues, where, null);
    }

    public int updateSettingsStoreName(String storeName) {
        ContentValues newValues = new ContentValues();
        String where = DBaseHelper.CL_SET_ID = "1";
        newValues.put(DBaseHelper.CL_SET_UserName, storeName);

        return db.update(DBaseHelper.TBL_SETTINGS, newValues, where, null);
    }

    public int updateSettingsPin(String loolcount) {
        ContentValues newValues = new ContentValues();
        String where = DBaseHelper.CL_SET_ID = "1";
        newValues.put(DBaseHelper.CL_SET_INCRECOUNT, loolcount);

        return db.update(DBaseHelper.TBL_SETTINGS, newValues, where, null);
    }


    public int updateKeyVal(String key, String value) {
        ContentValues newValues = new ContentValues();
        String where = DBaseHelper.CL_KV_KEY + " = '" + key + "'";
        newValues.put(DBaseHelper.CL_KV_VALUE, value);

        return db.update(DBaseHelper.TBL_SETINGS_KEYVAL_PAIR, newValues, where, null);
    }

    public int updateOF(String id, String content, String allitems) {
        ContentValues newValues = new ContentValues();
        String where = DBConstants.OrderConfirmation.id + " = '" + id + "'";
        newValues.put(DBConstants.OrderConfirmation.content, content);
        newValues.put(DBConstants.OrderConfirmation.items, allitems);
        newValues.put(DBConstants.OrderConfirmation.timeReceived, System.currentTimeMillis() + "");

        return db.update(DBConstants.OrderConfirmation.tableName, newValues, where, null);
    }



    public int updateDRITems(String id, String content, String allitems) {
        ContentValues newValues = new ContentValues();
        String where = DBConstants.DeliveryConfirmation.id + " = '" + id + "'";
        newValues.put(DBConstants.DeliveryConfirmation.content_received, content);
        newValues.put(DBConstants.DeliveryConfirmation.items_received, allitems);
        newValues.put(DBConstants.DeliveryConfirmation.timeReceived, System.currentTimeMillis() + "");

        return db.update(DBConstants.DeliveryConfirmation.tableName, newValues, where, null);
    }

    public int updateDRSentItems(String drNumber, String content, String allitems) {
        ContentValues newValues = new ContentValues();
        String where = DBConstants.DeliveryConfirmation.drNumber + " = '" + drNumber + "'";
        newValues.put(DBConstants.DeliveryConfirmation.content_sent, content);
        newValues.put(DBConstants.DeliveryConfirmation.items_sent, allitems);
        newValues.put(DBConstants.DeliveryConfirmation.timeSent, System.currentTimeMillis() + "");
        newValues.put(DBConstants.DeliveryConfirmation.isSent,  "1");

        return db.update(DBConstants.DeliveryConfirmation.tableName, newValues, where, null);
    }

    public int updateSettingsLoopCount(String pin) {
        ContentValues newValues = new ContentValues();
        String where = DBaseHelper.CL_SET_ID = "1";
        newValues.put(DBaseHelper.CL_SET_PIN, pin);

        return db.update(DBaseHelper.TBL_SETTINGS, newValues, where, null);
    }


    /**
     * DELETES
     */

    public boolean deleteHistory(String rowId) {
        String where = DBaseHelper.CL_HST_ID + "= " + rowId;
        return db.delete(DBaseHelper.TBL_SENTHISTORY, where, null) != 0;
    }

    public void deleteALL(String tableName) {
        db.execSQL("delete from " + tableName);
    }

    public void deleteOne(String tableName, String where){
        db.delete(tableName, where, null);
    }

}