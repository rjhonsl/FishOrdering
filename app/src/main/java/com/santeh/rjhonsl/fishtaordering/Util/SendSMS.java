package com.santeh.rjhonsl.fishtaordering.Util;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.santeh.rjhonsl.fishtaordering.Main.MainActivity;

import java.util.ArrayList;

/**
 * Created by rjhonsl on 5/31/2016.
 */
public class SendSMS  {

    public static String ACTION_SENDORDER       = "SMS_SENDING_ORDER";
    public static String ACTION_SMS_DELIVERED   = "SMS_ORDER_SENT";
    public static String MESSAGE_TYPE_SENDORDER = "sendorder";
    public static String MESSAGE_TYPE_SEND_DELIVERY_CONFIRMATION = "senddr";
    public static String MESSAGE_TYPE_RESEND    = "resendorder";

    public static ProgressDialog pd_sending;
    public static int intMessageParts = 1;
    public static int loopCount = 0;
    public static String wholeContent = "";


    public static void sendOrder(Activity activity, Context context, String number, String content){

        pd_sending = new ProgressDialog(context);
        pd_sending.setMessage("Sending order. Please wait...");
        pd_sending.setCancelable(false);
        pd_sending.show();

        Helper.toast.long_(activity, "Sending new order.");

        final SmsManager sms = SmsManager.getDefault();
        Intent sendIntent = new Intent(ACTION_SENDORDER);
        sendIntent.putExtra("sendto", number+"");
        sendIntent.putExtra("content", content+"");
        sendIntent.putExtra("timesent", System.currentTimeMillis()+"");
        sendIntent.putExtra("type", SendSMS.MESSAGE_TYPE_SENDORDER);

        Intent deliverIntent = new Intent(ACTION_SMS_DELIVERED);
        number = "+63" + number.substring(1,number.length());
        Log.d("RECEIVE", number);
        wholeContent = content;

        PendingIntent piSent = PendingIntent.getBroadcast(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piDelivered = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);

        if (content.length() > 160){
//            Helper.toast.short_(activity, "Sending Multipart Message");
            ArrayList<String> parts = sms.divideMessage(content);

            ArrayList<String> messageParts = sms.divideMessage(content);
            ArrayList<PendingIntent> piSents = new ArrayList<>(messageParts.size());
            ArrayList<PendingIntent> piDelivers = new ArrayList<>(messageParts.size());
            intMessageParts = messageParts.size();

            for (int i = 0; i < messageParts.size(); i++) {
                piSents.add(PendingIntent.getBroadcast(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                piDelivers.add(PendingIntent.getBroadcast(context, 0, deliverIntent, 0));
            }

            sms.sendMultipartTextMessage(number, null, parts, piSents, null );



        }else{
//            Helper.toast.short_(activity, "Sending Single Message");
            sms.sendTextMessage(number, null, content, piSent, piDelivered );
            Log.d("RECEIVE 2", number);
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
        number = "+63" + number.substring(1,number.length());
        Log.d("RECEIVE", number);

        PendingIntent piSent = PendingIntent.getBroadcast(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piDelivered = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);

        if (content.length() > 160){
            ArrayList<String> parts = sms.divideMessage(content);

            ArrayList<String> messageParts = sms.divideMessage(content);
            ArrayList<PendingIntent> piSents = new ArrayList<>(messageParts.size());
            ArrayList<PendingIntent> piDelivers = new ArrayList<>(messageParts.size());
            intMessageParts = messageParts.size();

            for (int i = 0; i < messageParts.size(); i++) {
                piSents.add(PendingIntent.getBroadcast(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                piDelivers.add(PendingIntent.getBroadcast(context, 0, deliverIntent, 0));
            }

            sms.sendMultipartTextMessage(number, null, parts, piSents, null );
        }else{
            Helper.toast.short_(activity, "Sending Single Message");
            sms.sendTextMessage(number, null, content, piSent, piDelivered );
        }

    }


    public static void sendDeliveryConfirmation(Activity activity, Context context, String number, String content, String drNumber, String allitems){

        pd_sending = new ProgressDialog(context);
        pd_sending.setMessage("Sending Delivery Confirmation. Please wait...");
        pd_sending.setCancelable(false);
        pd_sending.show();

        final SmsManager sms = SmsManager.getDefault();
        Intent sendIntent = new Intent(ACTION_SENDORDER);
        sendIntent.putExtra("sendto", number+"");
        sendIntent.putExtra("content", content+"");
        sendIntent.putExtra("timesent", System.currentTimeMillis()+"");
        sendIntent.putExtra("drnumber", drNumber+"");
        sendIntent.putExtra("allitems", allitems+"");
        sendIntent.putExtra("type", SendSMS.MESSAGE_TYPE_SEND_DELIVERY_CONFIRMATION);

        Intent deliverIntent = new Intent(ACTION_SMS_DELIVERED);
        number = "+63" + number.substring(1,number.length());
        Log.d("RECEIVE", number);
        wholeContent = content;

        PendingIntent piSent = PendingIntent.getBroadcast(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piDelivered = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);

        if (content.length() > 160){
//            Helper.toast.short_(activity, "Sending Multipart Message");
            ArrayList<String> parts = sms.divideMessage(content);

            ArrayList<String> messageParts = sms.divideMessage(content);
            ArrayList<PendingIntent> piSents = new ArrayList<>(messageParts.size());
            ArrayList<PendingIntent> piDelivers = new ArrayList<>(messageParts.size());
            intMessageParts = messageParts.size();

            for (int i = 0; i < messageParts.size(); i++) {
                piSents.add(PendingIntent.getBroadcast(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                piDelivers.add(PendingIntent.getBroadcast(context, 0, deliverIntent, 0));
            }

            sms.sendMultipartTextMessage(number, null, parts, piSents, null );
        }else{
//            Helper.toast.short_(activity, "Sending Single Message");
            sms.sendTextMessage(number, null, content, piSent, piDelivered );
            Log.d("RECEIVE 1", number);
        }
    }

}
