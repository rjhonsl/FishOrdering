package com.santeh.rjhonsl.fishtaordering.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public static String CL_ITEMS_OLD_CODE      = "itm_oldcode";
    public static final String[] ALL_KEY_ITEMS          = new String[]{CL_ITEMS_ID, CL_ITEMS_CODE, CL_ITEMS_DESCRIPTION, CL_ITEMS_OLD_CODE};
    public static final String[] ALL_DATATYPE_ITEMS = new String[]{ROWID_AUTOINCRE, TEXT, TEXT, TEXT};



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



    //connects db
    public DBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOGTAG, "table " + DATABASE_NAME + " has been opened: " + String.valueOf(context));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREATE TABLE FOR ITEMS
        db.execSQL(createTableString(TBL_ITEMS, ALL_KEY_ITEMS, ALL_DATATYPE_ITEMS));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 2)
        {
            //ADDED NEW TABLE TBLSENTHISTORY
            db.execSQL(createTableString(TBL_SENTHISTORY, ALL_KEY_HISTORY, ALL_DATATYPE_HISTORY));
        }

    }


    public static String createTableString(String tableName, String[] columns, String[] dataProperty){

        String sqlCreate = "CREATE TABLE " + tableName +
                " (";

        for (int i = 0; i < columns.length; i++) {
            sqlCreate = sqlCreate + columns[i] + " " + dataProperty[i] + ", ";
        }

        sqlCreate = sqlCreate.substring(0, sqlCreate.length() - 2);
        sqlCreate = sqlCreate +
                " )";
        return sqlCreate;
    }
}
