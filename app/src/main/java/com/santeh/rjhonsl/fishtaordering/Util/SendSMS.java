package com.santeh.rjhonsl.fishtaordering.Util;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

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


//    private void sendLongSmsMessage4(Context context, final SmsMessageInfo messageInfo) {
//
//        // Receive when each part of the SMS has been sent
//        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                // We need to make all the parts succeed before we say we have succeeded.
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        messageInfo.fail("Error - Generic failure");
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        messageInfo.fail("Error - No Service");
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        messageInfo.fail("Error - Null PDU");
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        messageInfo.fail("Error - Radio off");
//                        break;
//                }
//
//                nMsgParts--;
//                if (nMsgParts <= 0) {
//                    // Stop us from getting any other broadcasts (may be for other messages)
//                    Log.i(LOG_TAG, "All message part resoponses received, unregistering message Id: " + messageInfo.getMessageId());
//                    context.unregisterReceiver(this);
//
//                    if (messageInfo.isFailed()) {
//                        Log.d(LOG_TAG, "SMS Failure for message id: " + messageInfo.getMessageId());
//                    } else {
//                        Log.d(LOG_TAG, "SMS Success for message id: " + messageInfo.getMessageId());
//                        messageInfo.setSent(true);
//                    }
//                }
//            }
//        };
//
//        context.registerReceiver(broadcastReceiver, new IntentFilter(SENT + messageInfo.getMessageId()));
//
//        SmsManager smsManager = SmsManager.getDefault();
//
//        ArrayList<String> messageParts = smsManager.divideMessage(messageInfo.getMessage());
//        ArrayList<PendingIntent> pendingIntents = new ArrayList<PendingIntent>(messageParts.size());
//        nMsgParts = messageParts.size();
//
//        for (int i = 0; i < messageParts.size(); i++) {
//            Intent sentIntent = new Intent(SENT + messageInfo.getMessageId());
//            pendingIntents.add(PendingIntent.getBroadcast(context, 0, sentIntent, 0));
//        }
//
//        Log.i(LOG_TAG, "About to send multi-part message Id: " + messageInfo.getMessageId());
//        smsManager.sendMultipartTextMessage(messageInfo.getPhoneNumber(), null, messageParts, pendingIntents, null);
//    }

}
