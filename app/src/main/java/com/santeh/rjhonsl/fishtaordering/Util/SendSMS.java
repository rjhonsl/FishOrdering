package com.santeh.rjhonsl.fishtaordering.Util;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.ArrayList;

/**
 * Created by rjhonsl on 5/31/2016.
 */
public class SendSMS  {

    public static String ACTION_SENDORDER       = "SMS_SENDING_ORDER";
    public static String ACTION_SMS_DELIVERED   = "SMS_ORDER_SENT";
    public static String MESSAGE_TYPE_SENDORDER = "sendorder";
    public static String MESSAGE_TYPE_RESEND    = "resendorder";

    public static ProgressDialog pd_sending;


    public static void sendOrder(Activity activity, Context context, String number, String content){

        pd_sending = new ProgressDialog(context);
        pd_sending.setMessage("Sending order. Please wait...");
        pd_sending.setCancelable(false);
        pd_sending.show();

        final SmsManager sms = SmsManager.getDefault();
        Intent sendIntent = new Intent(ACTION_SENDORDER);
        Intent deliverIntent = new Intent(ACTION_SMS_DELIVERED);
        sendIntent.putExtra("sendto", number+"");
        sendIntent.putExtra("content", content+"");
        sendIntent.putExtra("timesent", System.currentTimeMillis()+"");
        sendIntent.putExtra("type", SendSMS.MESSAGE_TYPE_SENDORDER);


        PendingIntent piSent = PendingIntent.getBroadcast(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piDelivered = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);

        if (content.length() > 160){
            Helper.toast.short_(activity, "Sending Multipart Message");
            ArrayList<String> parts = sms.divideMessage(content);
            sms.sendMultipartTextMessage(number, content, parts, null, null );
        }else{
            Helper.toast.short_(activity, "Sending Single Message");
            sms.sendTextMessage(number, number, content, piSent, piDelivered );
        }
    }

    public static void resendOrderHistory(Activity activity, Context context, String number, String content, String hstID, String itempos, String itemCount){

        pd_sending = new ProgressDialog(context);
        pd_sending.setMessage("Sending order. Please wait...");
        pd_sending.setCancelable(false);
        pd_sending.show();

        final SmsManager sms = SmsManager.getDefault();
        Intent sendIntent = new Intent(ACTION_SENDORDER);
        Intent deliverIntent = new Intent(ACTION_SMS_DELIVERED);
        sendIntent.putExtra("sendto", number+"");
        sendIntent.putExtra("content", content+"");
        sendIntent.putExtra("timesent", System.currentTimeMillis()+"");
        sendIntent.putExtra("type", SendSMS.MESSAGE_TYPE_RESEND);
        sendIntent.putExtra("hstid", hstID);
        sendIntent.putExtra("pos", itempos);
        sendIntent.putExtra("listcount", itempos);


        PendingIntent piSent = PendingIntent.getBroadcast(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piDelivered = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);

        if (content.length() > 160){
            Helper.toast.short_(activity, "Sending Multipart Message");
            ArrayList<String> parts = sms.divideMessage(content);
            sms.sendMultipartTextMessage(number, content, parts, null, null );
        }else{
            Helper.toast.short_(activity, "Sending Single Message");
            sms.sendTextMessage(number, number, content, piSent, piDelivered );
        }
    }


}
