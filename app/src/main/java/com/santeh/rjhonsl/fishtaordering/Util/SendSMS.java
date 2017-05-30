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
    public static String MESSAGE_TYPE_SEND_BLBO = "sendBLBO";
    public static String MESSAGE_TYPE_SEND_REPORCESS = "sendREPROCESS";
    public static String MESSAGE_TYPE_RESEND    = "resendorder";

    public static ProgressDialog pd_sending;
    public static int intMessageParts = 1;
    public static int loopCount = 0;
    public static String wholeContent = "";

    public static int sendingCounter = 0;
    public static List<String> newOrderBatch;

    private static String drNumber1 = "", allitems1 = "", hstID1 = "", itempos1 = "", processType1 = "";;

    public static void sendOrder(Activity activity, Context context, String number, String content){

        pd_sending = new ProgressDialog(context);
        pd_sending.setCancelable(false);
        Helper.toast.long_(activity, "Sending new order.");

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
            }
        }

        sendingCounter = 0;
        SendSMS.pd_sending.setMessage("Sending new order : " + (SendSMS.sendingCounter+1) + "/" + SendSMS.newOrderBatch.size());
        pd_sending.show();
        newOrder(context, number, content, MESSAGE_TYPE_SENDORDER);
    }



    public static void resendOrderHistory(Activity activity, Context context, String number, String content, String hstID, String itempos, String itemCount){

        pd_sending = new ProgressDialog(context);
        pd_sending.setCancelable(false);
        hstID1 = hstID;
        itempos1 = itempos;
        pd_sending.show();

        ArrayList<String> orderList = new ArrayList<>(Arrays.asList(content.split(";")));
        String header = orderList.get(0);
        orderList.remove(0);

        String formattedOrder = ""+header;
        int batchCounter = 0;
        newOrderBatch = new ArrayList<String>();

        for (int i = 0; i <orderList.size() ; i++) {
            String tester =  formattedOrder + ";" +orderList.get(i)+"";
            if (tester.length()>160){
                batchCounter++;
                newOrderBatch.add(formattedOrder);
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
                }
            }
        }

        sendingCounter = 0;
        SendSMS.pd_sending.setMessage("Resending order: " + (SendSMS.sendingCounter+1) + "/" + SendSMS.newOrderBatch.size());
        pd_sending.show();
        newOrder(context, number, content, MESSAGE_TYPE_RESEND);

    }


    public static void sendDeliveryConfirmation(Activity activity, Context context, String number, String content, String drNumber, String allitems){

        pd_sending = new ProgressDialog(context);
        pd_sending.setCancelable(false);
        drNumber1 = drNumber;
        allitems1 = allitems;
        pd_sending.show();

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
            }
        }

        sendingCounter = 0;
        SendSMS.pd_sending.setMessage("Sending  order confirmation: " + (SendSMS.sendingCounter+1) + "/" + SendSMS.newOrderBatch.size());
        pd_sending.show();
        newOrder(context, number, content, MESSAGE_TYPE_SEND_DELIVERY_CONFIRMATION);
    }


    public static void sendBLBO(Activity activity, Context context, String number, String content, String allitems, String processType){

        pd_sending = new ProgressDialog(context);
        pd_sending.setCancelable(false);
        allitems1 = allitems;
        processType1 = processType;
        pd_sending.show();

        ArrayList<String> orderList = new ArrayList<>(Arrays.asList(content.split(";")));
        String header = orderList.get(0);
        orderList.remove(0);

        String formattedOrder = ""+header;
        int batchCounter = 0;
        newOrderBatch = new ArrayList<>();

        for (int i = 0; i <orderList.size() ; i++) {
            String tester =  formattedOrder + ";" +orderList.get(i)+"";
            if (tester.length()>160){
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
                }
            }
        }

        String[] allProcessType = new String[]{"BO", "BL", "AI"};
        String theProcess = "";
        if (processType.equalsIgnoreCase(allProcessType[0])){
            theProcess = "BAD ORDER";
        }else if (processType.equalsIgnoreCase(allProcessType[1])){
            theProcess = "BACK LOAD";
        }else if (processType.equalsIgnoreCase(allProcessType[2])){
            theProcess = "ACTUAL INVENTORY";
        }
        sendingCounter = 0;
        SendSMS.pd_sending.setMessage("Sending "+theProcess+": " + (SendSMS.sendingCounter+1) + "/" + SendSMS.newOrderBatch.size());
        pd_sending.show();
        newOrder(context, number, content, MESSAGE_TYPE_SEND_BLBO);
    }





    public static void sendReprocess(Context context, String number, String content, String allitems, String processType){

        pd_sending = new ProgressDialog(context);
        pd_sending.setCancelable(false);
        allitems1 = allitems;
        processType1 = processType;
        pd_sending.show();

        ArrayList<String> orderList = new ArrayList<>(Arrays.asList(content.split(";")));
        String header = orderList.get(0);
        orderList.remove(0);

        String formattedOrder = header;
        int batchCounter = 0;
        newOrderBatch = new ArrayList<>();

        for (int i = 0; i <orderList.size() ; i++) {
            String tester =  formattedOrder + ";" +orderList.get(i)+"";
            if (tester.length()>160){
                batchCounter++;
                newOrderBatch.add(formattedOrder);
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
                }
            }
        }


        sendingCounter = 0;
        SendSMS.pd_sending.setMessage("Sending Reprocess: " + (SendSMS.sendingCounter+1) + "/" + SendSMS.newOrderBatch.size());
        pd_sending.show();
        newOrder(context, number, content, MESSAGE_TYPE_SEND_REPORCESS);
    }








//handles message sending for batches
    public static void newOrder(Context context, String number, String content, String sendingType) {
        if (newOrderBatch.size()>0){

            final SmsManager sms = SmsManager.getDefault();
            Intent sendIntent = new Intent(ACTION_SENDORDER);
            sendIntent.putExtra("sendto", number+"");
            sendIntent.putExtra("content", content+"");
            sendIntent.putExtra("timesent", System.currentTimeMillis()+"");
            sendIntent.putExtra("type", sendingType);

            //for delivery confirmation
            sendIntent.putExtra("drnumber", drNumber1+"");
            sendIntent.putExtra("allitems", allitems1+"");

            //for resending order
            sendIntent.putExtra("hstid", hstID1);
            sendIntent.putExtra("pos", itempos1);
            sendIntent.putExtra("listcount", itempos1);

            //for blboai
            sendIntent.putExtra("processtype", processType1);
            sendIntent.putExtra("allitems_blbo", allitems1+"");

            Intent deliverIntent = new Intent(ACTION_SMS_DELIVERED);
            number = "+63" + number.substring(1,number.length());
            Log.d("RECEIVE", number);
            wholeContent = content;

            PendingIntent piSent = PendingIntent.getBroadcast(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent piDelivered = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);

            sms.sendTextMessage(number, null, newOrderBatch.get(sendingCounter), piSent, piDelivered );
        }
    }


}
