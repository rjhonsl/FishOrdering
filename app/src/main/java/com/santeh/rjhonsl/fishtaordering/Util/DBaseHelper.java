package com.santeh.rjhonsl.fishtaordering.Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rjhonsl on 5/24/2016.
 */
public class DBaseHelper extends SQLiteOpenHelper{

    private static String DATABASE_NAME = "fishta_ordering.db";
    private static int DATABASE_VERSION = 2;
    private static String LOGTAG = "DB_FishtaOrdering";

    public static String DATE = "DATE", TEXT = "TEXT", INTEGER = "INTEGER", DOUBLE = "DOUBLE", DATETIME = "DATETIME",
            PRIMARY_AUTOINCRE = "PRIMARY KEY AUTOINCREMENT", BOOLEAN = "BOOLEAN", TEMP = "TMP", ROWID_AUTOINCRE = INTEGER + " " + PRIMARY_AUTOINCRE;


    //TABLE FOR ITEMS
    public static String TBL_ITEMS              = "tbl_items";
    public static String CL_ITEMS_ID            = "itm_id";
    public static String CL_ITEMS_CODE          = "itm_code";
    public static String CL_ITEMS_DESCRIPTION   = "itm_desc";
    public static String CL_ITEMS_GROUP_CODE    = "itm_oldcode";
    public static String CL_ITEMS_UNITS = "itm_units";
    public static final String[] ALL_KEY_ITEMS      = new String[]{CL_ITEMS_ID, CL_ITEMS_CODE, CL_ITEMS_DESCRIPTION, CL_ITEMS_GROUP_CODE, CL_ITEMS_UNITS};
    public static final String[] ALL_DATATYPE_ITEMS = new String[]{ROWID_AUTOINCRE, TEXT, TEXT, TEXT, TEXT};



    //TABLE FOR ORDER HISTORY
    public static String TBL_SENTHISTORY    = "tbl_senthistory";
    public static String CL_HST_ID              = "hst_id";
    public static String CL_HST_SENTTO          = "hst_sentTo";
    public static String CL_HST_MESSAGE         = "hst_message";
    public static String CL_HST_TIMESENT        = "hst_timesent";
    public static String CL_HST_ISMULTIPART     = "hst_ismultipart";
    public static String CL_HST_ISSENT          = "hst_issent";
    public static String CL_HST_ISSTILLSENDING  = "hst_isstillsending";
    public static final String[] ALL_KEY_HISTORY = new String[]{CL_HST_ID, CL_HST_SENTTO, CL_HST_MESSAGE, CL_HST_TIMESENT, CL_HST_ISMULTIPART, CL_HST_ISSENT, CL_HST_ISSTILLSENDING};
    public static final String[] ALL_DATATYPE_HISTORY = new String[]{ROWID_AUTOINCRE, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT};


    //TABLE FOR SETTINGS
    public static String TBL_SETTINGS = "tbl_settings";
    public static String CL_SET_ID = "set_id";
    public static String CL_SET_SERVERNUM   = "set_servernum";
    public static String CL_SET_INCRECOUNT  = "set_increcount";
    public static String CL_SET_STORENAME   = "set_storeName";
    public static String CL_SET_PIN         = "set_pin";
    public static final String[] ALL_KEY_SETTINGS = new String[]{CL_SET_ID, CL_SET_SERVERNUM, CL_SET_INCRECOUNT, CL_SET_STORENAME, CL_SET_PIN};
    public static final String[] ALL_DATATYPE_SETTINGS = new String[]{ROWID_AUTOINCRE, TEXT, TEXT, TEXT, TEXT};

    ///TBL for ITEMS ORDERED
    public static String TBL_ORDEREDITEMS   = "tbl_ordereditems";
    public static String CL_OI_ID           = "oi_id";
    public static String CL_OI_ITEMID       = "oi_itemId";
    public static String CL_OI_ORDERID      = "oi_orderId";
    public static final String[] ALL_KEY_ORDERED_ITEMS = new String[]{CL_OI_ID, CL_OI_ITEMID, CL_OI_ORDERID};
    public static final String[] ALL_DATATYPE_ORDERED_ITEMS = new String[]{ROWID_AUTOINCRE, TEXT, TEXT};


    //connects db
    public DBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        Log.d(LOGTAG, "table " + DATABASE_NAME + " has been opened: " + String.valueOf(context));
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE TABLE FOR ITEMS
        createTablesIfNotExists(db);
    }
    private void createTablesIfNotExists(SQLiteDatabase db) {
        db.execSQL(createTableString(TBL_ITEMS, ALL_KEY_ITEMS, ALL_DATATYPE_ITEMS));
        db.execSQL(createTableString(TBL_SENTHISTORY, ALL_KEY_HISTORY, ALL_DATATYPE_HISTORY));
        db.execSQL(createTableString(TBL_SETTINGS, ALL_KEY_SETTINGS, ALL_DATATYPE_SETTINGS));
        db.execSQL(createTableString(TBL_ORDEREDITEMS, ALL_KEY_ORDERED_ITEMS, ALL_DATATYPE_ORDERED_ITEMS));
    }

    /**
    1   beginTransaction
    2   run a table creation with if not exists (we are doing an upgrade, so the table might not exists yet, it will fail alter and drop)
    3   put in a list the existing columns List<String> columns = DBUtils.GetColumns(db, TableName);
    4   backup table (ALTER table " + TableName + " RENAME TO 'temp_" + TableName)
            create new table (the newest table creation schema)
    5   get the intersection with the new columns, this time columns taken from the upgraded table (columns.retainAll(DBUtils.GetColumns(db, TableName));)
    6   restore data (String cols = StringUtils.join(columns, ",");
    7   db.execSQL(String.format(
            "INSERT INTO %s (%s) SELECT %s from temp_%s", TableName, cols, cols, TableName));)
    8   remove backup table (DROP table 'temp_" + TableName)
            setTransactionSuccessful
    */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        createTablesIfNotExists(db); //step2

//        Log.d("DB", "tables updated");
//        //step3 - list existing columns
//        List<String> oldItem = GetColumns(db, TBL_ITEMS);
//        List<String> oldSentHistory = GetColumns(db, TBL_SENTHISTORY);
//        List<String> oldSettings = GetColumns(db, TBL_SETTINGS);
//        List<String> oldOrderedItems = GetColumns(db, TBL_ORDEREDITEMS);

        //step4 - backup table
        


    }


    public static String createTableString(String tableName, String[] columns, String[] dataProperty){

        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + tableName +
                " (";

        for (int i = 0; i < columns.length; i++) {
            sqlCreate = sqlCreate + columns[i] + " " + dataProperty[i] + ", ";
        }

        sqlCreate = sqlCreate.substring(0, sqlCreate.length() - 2);
        sqlCreate = sqlCreate +
                " )";
        return sqlCreate;
    }



    public static List<String> GetColumns(SQLiteDatabase db, String tableName) {
        List<String> ar = null;
        Cursor c = null;
        try {
            c = db.rawQuery("select * from " + tableName + " limit 1", null);
            if (c != null) {
                ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
            }
        } catch (Exception e) {
            Log.v(tableName, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return ar;
    }

    public static String join(List<String> list, String delim) {
        StringBuilder buf = new StringBuilder();
        int num = list.size();
        for (int i = 0; i < num; i++) {
            if (i != 0)
                buf.append(delim);
            buf.append((String) list.get(i));
        }
        return buf.toString();
    }
}
