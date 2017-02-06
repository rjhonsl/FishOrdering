package com.santeh.rjhonsl.fishtaordering.Util;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static int sendingCounter = 0;
    public static List<String> newOrderBatch;

    public static void sendOrder(Activity activity, Context context, String number, String content){

        pd_sending = new ProgressDialog(context);
        pd_sending.setMessage("Sending order. Please wait...");
        pd_sending.setCancelable(false);
//        pd_sending.show();

        Helper.toast.long_(activity, "Sending new order.");


        Log.d("CONTENT",  "Before: " + content );
        content = "#OF-2;33,1000,KGS;99,1000,KGS;45,1000,KGS;139,1000,KGS;231,1000,KGS;232,1000,KGS;267,1000,KGS;309,1000,PCS;308,1000,PCS;439,1000,KGS;545,1000,KGS;549,1000,KGS;" +
                        "584,1000,KGS;612,1000,KGS;3,1000,PCS;23,1000,KGS;23,1000,KGS;23,1000,KGS;23,1000,KGS;23,1000,KGS;23,1000,KGS;24,1000,KGS;25,1000,KGS;26,1000,KGS;27,1000,KGS;28,1000,KGS;29,1000,KGS;30,1000,KGS;31,1000,KGS;32,1000,KGS;33,1000,KGS";
        Log.d("CONTENT", "AFter: " + content.length() + " " + content );


        ArrayList<String> orderList = new ArrayList<>(Arrays.asList(content.split(";")));
        String header = orderList.get(0);
        orderList.remove(0);

        String formattedOrder = ""+header;
        int batchCounter = 0;
        newOrderBatch = new ArrayList<String>();

        for (int i = 0; i <orderList.size() ; i++) {
            String tester =  formattedOrder + ";" +orderList.get(i)+"";
            if (tester.length()>160){
//                Log.d("BATCHING", "greater than: "+formattedOrder.length()+" \n"+formattedOrder);
                batchCounter++;
                newOrderBatch.add(formattedOrder);
                Log.d("BATCHLIST_ADD_greater", formattedOrder);
                formattedOrder = header + "-" + batchCounter+"";
                i--;
            }else{
                if (batchCounter==0 && i ==0){
                    formattedOrder = formattedOrder + "-0"+ ";" + orderList.get(i)+"";
                }else{
                    formattedOrder = formattedOrder + ";" + orderList.get(i)+"";
                }
                if (i==orderList.size()-1){
                    newOrderBatch.add(formattedOrder);
                    Log.d("BATCHLIST_ADD_less_than", formattedOrder);
                }


//                Log.d("BATCHING", "less than: "+formattedOrder.length()+"");
            }
        }

        sendingCounter = 0;
        pd_sending.show();
        newOrder(context, number, content);
//        if (content.length() > 160){
////            Helper.toast.short_(activity, "Sending Multipart Message");
//            ArrayList<String> parts = sms.divideMessage(content);
//
//            ArrayList<String> messageParts = sms.divideMessage(content);
//            ArrayList<PendingIntent> piSents = new ArrayList<>(messageParts.size());
//            ArrayList<PendingIntent> piDelivers = new ArrayList<>(messageParts.size());
//            intMessageParts = messageParts.size();
//
//            for (int i = 0; i < messageParts.size(); i++) {
//                piSents.add(PendingIntent.getBroadcast(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT));
//                piDelivers.add(PendingIntent.getBroadcast(context, 0, deliverIntent, 0));
//            }
//
//            sms.sendMultipartTextMessage(number, null, parts, piSents, null );
//        }else{
////            Helper.toast.short_(activity, "Sending Single Message");
//            sms.sendTextMessage(number, null, content, piSent, piDelivered );
//            Log.d("RECEIVE 2", number);
//        }
    }

    public static void newOrder(Context context, String number, String content) {
        if (newOrderBatch.size()>0){

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


            sms.sendTextMessage(number, null, newOrderBatch.get(sendingCounter), piSent, piDelivered );
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
