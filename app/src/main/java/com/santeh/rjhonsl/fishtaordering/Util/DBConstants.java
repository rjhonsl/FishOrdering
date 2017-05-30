package com.santeh.rjhonsl.fishtaordering.Util;

/**
 * Created by Globe on 10/3/2016.
 */

public class DBConstants {

    private static String DATE = "DATE", TEXT = "TEXT", INTEGER = "INTEGER", DOUBLE = "DOUBLE", DATETIME = "DATETIME",
            PRIMARY_AUTOINCRE = "PRIMARY KEY AUTOINCREMENT", BOOLEAN = "BOOLEAN", TEMP = "TMP", ROWID_AUTOINCRE = INTEGER + " " + PRIMARY_AUTOINCRE;


    public static class OrderConfirmation {
        //TBL OR_CONF
        public static String tableName = "tbl_orconfirmation";
        public static String id = "oc_id";
        public static String ORnumber = "oc_ORnumber";
        public static String sender = "oc_sender";
        public static String content = "oc_content";
        public static String items = "oc_items";
        public static String timeReceived = "oc_timereceived";
        public static String custID = "oc_timeOpened";
        public static String isSent = "oc_isSent";
        public static String batchnumber = "oc_isRead";

        public static String[] ALL_KEYS = new String[]{id,ORnumber, sender, content, timeReceived, custID,isSent, batchnumber, items};
        public static String[] ALL_DATATYPE = new String[]{ROWID_AUTOINCRE, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT};
    }


    public static class DeliveryConfirmation {
        //TBL DR
        public static String tableName = "tbl_drConfirmation";
        public static String id = "dc_id";
        public static String drNumber = "dc_drNumber";
        public static String sender = "dc_sender";
        public static String items_received = "dr_items_received";
        public static String items_sent = "dr_items_sent";
        public static String item_batchNumber = "dr_batch_number";
        public static String content_received = "oc_content_received";
        public static String content_sent = "oc_content_sent";
        public static String timeReceived = "dr_time_received";
        public static String timeSent = "dr_time_sent";
        public static String custID = "dc_timeOpened";
        public static String isSent = "dc_isSent";
        public static String isRead = "dc_isRead";

        public static String[] ALL_KEYS = new String[]{id, drNumber, sender, items_received, items_sent, content_received, content_sent, timeReceived, timeSent, custID, isSent, isRead, item_batchNumber};
        public static String[] ALL_DATATYPE = new String[]{ROWID_AUTOINCRE, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT};
    }


    public static class BLBO {
        //TBL DR
        public static String tableName = "tblBLBO";
        public static String id = "id";
        public static String sent_to = "sent_to";
        public static String custid = "cust_ID";
        public static String items_array = "item_array";
        public static String content = "content";
        public static String timeSent = "time_sent";
        public static String time_opened = "time_opened";
        public static String isSent = "dc_isSent";
        public static String isRead = "dc_isRead";

        public static String[] ALL_KEYS = new String[]{id, sent_to, custid, items_array, content, timeSent, time_opened, isSent, isRead};
        public static String[] ALL_DATATYPE = new String[]{ROWID_AUTOINCRE, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT};
    }


    public static class Reprocess {
        //TBL DR
        public static String tableName = "tblReprocess";
        public static String id = "id";
        public static String sent_to = "sent_to";
        public static String custid = "cust_ID";
        public static String items_array = "item_array";
        public static String content = "content";
        public static String type = "type";
        public static String timeSent = "time_sent";
        public static String time_opened = "time_opened";
        public static String isSent = "isSent";
        public static String isRead = "isRead";

        public static String[] ALL_KEYS = new String[]{id, sent_to, custid, items_array, content, type, timeSent, time_opened, isSent, isRead};
        public static String[] ALL_DATATYPE = new String[]{ROWID_AUTOINCRE, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, TEXT};
    }

}
