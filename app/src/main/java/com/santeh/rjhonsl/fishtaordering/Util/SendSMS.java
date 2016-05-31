package com.santeh.rjhonsl.fishtaordering.Util;

import android.app.Activity;
import android.app.PendingIntent;
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




    public static void sendOR(Activity activity, Context context, String number, String content){

        final SmsManager sms = SmsManager.getDefault();
        Intent sendIntent = new Intent(ACTION_SENDORDER);
        Intent deliverIntent = new Intent(ACTION_SMS_DELIVERED);
        sendIntent.putExtra("sendto", number+"");
        sendIntent.putExtra("content", content+"");
        sendIntent.putExtra("timesent", System.currentTimeMillis()+"");


        PendingIntent piSent = PendingIntent.getBroadcast(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piDelivered = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);



        if (content.length() > 159){
            Helper.toast.short_(activity, "Sending Multipart Message");
            ArrayList<String> parts = sms.divideMessage(content);
            sms.sendMultipartTextMessage(number, content, parts, null, null );
        }else{
            Helper.toast.short_(activity, "Sending Single Message");
            sms.sendTextMessage(number, number, content, piSent, piDelivered );
        }
    }
}
