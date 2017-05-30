package com.santeh.rjhonsl.fishtaordering.Util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.santeh.rjhonsl.fishtaordering.Main.DeliveryConfirmation.Activity_DeliveryConfirmation;
import com.santeh.rjhonsl.fishtaordering.Main.OrderItems.Activity_OrderHistory;
import com.santeh.rjhonsl.fishtaordering.Main.OrderItems.MainActivity;

import java.util.List;

public class BR_SMSDelivery extends BroadcastReceiver {

	private final String DEBUG_TAG = getClass().getSimpleName();
	private static final String ACTION_SMS_SENT = SendSMS.ACTION_SENDORDER;
	public static boolean stillsending = false;
	Context ctx;
	DBaseQuery db;


	// When the SMS has been sent
	public void onReceive(Context context, Intent intent) {
		ctx=context;
		db = new DBaseQuery(context);
		db.open();
		SendSMS.loopCount = SendSMS.loopCount+ 1;
		Log.d("RECEIVER","Loopcount: "+ SendSMS.loopCount + "    intParts:" + SendSMS.intMessageParts );

		String action = intent.getAction();
		String timesend = intent.getStringExtra("timesent");
		String content = intent.getStringExtra("content");
		String number = intent.getStringExtra("sendto");
		String type = intent.getStringExtra("type");
		broadcaster = LocalBroadcastManager.getInstance(context);
		String isSent = "0";


		checkifSent(context, intent, action, timesend, SendSMS.wholeContent, number, type, isSent);
	}

	private void checkifSent(Context context, Intent intent, String action, String timesend, String content, String number, String type, String isSent) {
		if (action.equals( SendSMS.ACTION_SENDORDER )) {
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				isSent = "1";
				Log.d("MESSAGERECEIVER","OK");
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				isSent = "0";
				Log.d("MESSAGERECEIVER","GENERIC");
				break;
			case SmsManager.RESULT_ERROR_NO_SERVICE:
				isSent = "0";
				Log.d("MESSAGERECEIVER","NO SERVICE");
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				Log.d("MESSAGERECEIVER","NULL PDU");
				isSent = "0";
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				Log.d("MESSAGERECEIVER","RADIO OFF");
				isSent = "0";
				break;
			}



			Log.d("BATCHCOUNT", SendSMS.sendingCounter + " || " + (SendSMS.newOrderBatch.size()-1));
			if (SendSMS.sendingCounter < SendSMS.newOrderBatch.size()-1){

				if (type.equalsIgnoreCase(SendSMS.MESSAGE_TYPE_SENDORDER)){
					if (isSent.equalsIgnoreCase("0")){
						SendSMS.pd_sending.dismiss();
						Toast.makeText(context, "Sending Failed. Please try again", Toast.LENGTH_LONG).show();
					}else {
						SendSMS.sendingCounter++;
						SendSMS.pd_sending.setMessage("Sending new order: " + (SendSMS.sendingCounter+1) + "/" + SendSMS.newOrderBatch.size());
						SendSMS.newOrder(context, number, content, type);
					}
				}

				if (type.equalsIgnoreCase(SendSMS.MESSAGE_TYPE_SEND_DELIVERY_CONFIRMATION)){
					if (isSent.equalsIgnoreCase("0")){
						SendSMS.pd_sending.dismiss();
						Toast.makeText(context, "Sending Failed Delivery Confirmation Failed. Please try again", Toast.LENGTH_LONG).show();
					}else {
						SendSMS.sendingCounter++;
						SendSMS.pd_sending.setMessage("Sending delivery confirmation: " + (SendSMS.sendingCounter+1) + "/" + SendSMS.newOrderBatch.size());
						SendSMS.newOrder(context, number, content, type);
					}
				}


				if (type.equalsIgnoreCase(SendSMS.MESSAGE_TYPE_RESEND)){
					if (isSent.equalsIgnoreCase("0")){
						SendSMS.pd_sending.dismiss();
						Toast.makeText(context, "Sending Failed. Please try again", Toast.LENGTH_LONG).show();
					}else {
						SendSMS.sendingCounter++;
						SendSMS.pd_sending.setMessage("Resending order: " + (SendSMS.sendingCounter+1) + "/" + SendSMS.newOrderBatch.size());
						SendSMS.newOrder(context, number, content, type);
					}
				}

				if (type.equalsIgnoreCase(SendSMS.MESSAGE_TYPE_SEND_BLBO)){
					if (isSent.equalsIgnoreCase("0")){
						SendSMS.pd_sending.dismiss();
						Toast.makeText(context, "Sending BL/BO/AI Failed. Please try again", Toast.LENGTH_LONG).show();
					}else {
						SendSMS.sendingCounter++;
						SendSMS.pd_sending.setMessage("Sending BLBO: " + (SendSMS.sendingCounter+1) + "/" + SendSMS.newOrderBatch.size());
						SendSMS.newOrder(context, number, content, type);
					}
				}


				if (type.equalsIgnoreCase(SendSMS.MESSAGE_TYPE_SEND_REPORCESS)){
					if (isSent.equalsIgnoreCase("0")){
						SendSMS.pd_sending.dismiss();
						Toast.makeText(context, "Sending Reprocess Failed. Please try again", Toast.LENGTH_LONG).show();
					}else {
						SendSMS.sendingCounter++;
						SendSMS.pd_sending.setMessage("Sending Reprocess: " + (SendSMS.sendingCounter+1) + "/" + SendSMS.newOrderBatch.size());
						SendSMS.newOrder(context, number, content, type);
					}
				}

			}else{
				SendSMS.sendingCounter = 0;
				SendSMS.loopCount = 0;
				SendSMS.pd_sending.dismiss();
				if (type.equalsIgnoreCase(SendSMS.MESSAGE_TYPE_SENDORDER)){
					if (isSent.equalsIgnoreCase("0")){
						Toast.makeText(context, "Sending Failed. Please try again", Toast.LENGTH_LONG).show();
					}else {
						Toast.makeText(context, "Order has been sent", Toast.LENGTH_LONG).show();
						if (MainActivity.orderList != null){
							int size = MainActivity.orderList.size();
							List<VarFishtaOrdering> list = MainActivity.orderList;
							if (size > 0) {
								for (int i = 0; i < size; i++) {
									list.remove(0);
								}
								MainActivity.itemsViewAdapter.notifyItemRangeRemoved(0, size);
								MainActivity.toggleNoItemImage();
							}
						}

						Log.d("FISHCONTENT", "SAVING: " + content.length() + " " + content );
						long orderID = db.insertOrderHistory(number, content, timesend, "0", isSent, "0");

						String[] contentss = content.split(";");
						for (int i = 0; i < contentss.length; i++) {
							if (i > 0){
								String[] itemDetails = contentss[i].split(",");
								db.insertOrderedItems(itemDetails[0], orderID + "");
							}
						}

					}

				}else if (type.equalsIgnoreCase(SendSMS.MESSAGE_TYPE_RESEND)) {

					if (isSent.equalsIgnoreCase("0")){
						Toast.makeText(context, "Rending Failed. Please try again", Toast.LENGTH_LONG).show();
					}else {
						Toast.makeText(context, "Resend Successful", Toast.LENGTH_LONG).show();
					}

					String hstid = intent.getStringExtra("hstid");

					db.updateSentHistory(hstid, number, content, timesend, "0", isSent, "0");
					if (Activity_OrderHistory.isActive) {
						Intent refresh = new Intent(context, Activity_OrderHistory.class);
						refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
						context.startActivity(refresh);
					}

				} else if (type.equalsIgnoreCase(SendSMS.MESSAGE_TYPE_SEND_DELIVERY_CONFIRMATION)){

					if (isSent.equalsIgnoreCase("0")){
						Toast.makeText(context, "SENDING FAILED. Please try again", Toast.LENGTH_LONG).show();
					}else {
						Toast.makeText(context, "Delivery Confirmation Successful", Toast.LENGTH_LONG).show();
					}

					String drnumber = intent.getStringExtra("drnumber");
					String allitems = intent.getStringExtra("allitems");
					Log.d("DRCONF", drnumber + " ||  " + allitems);

					db.updateDRSentItems(drnumber, content, allitems);

					if (Activity_DeliveryConfirmation.isActive) {
						Intent refresh = new Intent(context, Activity_DeliveryConfirmation.class);
						refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
						context.startActivity(refresh);
					}

				} else if (type.equalsIgnoreCase(SendSMS.MESSAGE_TYPE_SEND_BLBO)){

					String processType = intent.getStringExtra("processtype");
					String allitems = intent.getStringExtra("allitems_blbo");

					if (isSent.equalsIgnoreCase("0")){
						Toast.makeText(context, "SENDING FAILED. Please try again", Toast.LENGTH_LONG).show();
					}else {
						String[] allProcessType = new String[]{"BO", "BL", "AI"};
						String theProcess = "";
						if (processType.equalsIgnoreCase(allProcessType[0])){
							theProcess = "BAD ORDER";
						}else if (processType.equalsIgnoreCase(allProcessType[1])){
							theProcess = "BACK LOAD";
						}else if (processType.equalsIgnoreCase(allProcessType[2])){
							theProcess = "ACTUAL INVENTORY";
						}
						Toast.makeText(context, "Sending "+theProcess+" Successful", Toast.LENGTH_LONG).show();

						String[] header = content.split(";");
						String[] header_values = header[0].split("-");

						db.insertBLBOAI(number,  header_values[1], allitems, content ,System.currentTimeMillis()+"", "0", "1", "0");

						broadCastSendingResult();
					}
				} else if (type.equalsIgnoreCase(SendSMS.MESSAGE_TYPE_SEND_REPORCESS)){

					String processType = intent.getStringExtra("processtype");
					String allitems = intent.getStringExtra("allitems_blbo");

					if (isSent.equalsIgnoreCase("0")){
						Toast.makeText(context, "SENDING FAILED. Please try again", Toast.LENGTH_LONG).show();
					}else {
						Toast.makeText(context, "Sending Reprocess Successful", Toast.LENGTH_LONG).show();

						String[] header = content.split(";");
						String[] header_values = header[0].split("-");

						db.insertReprocess(number,  header_values[1], allitems, content ,System.currentTimeMillis()+"", "0", "1", "0", processType);

						broadCastSendingResult();
					}
				}

			}


		}
	}

	LocalBroadcastManager broadcaster;
	static final public String FCM_RESULT = "firebaseResult";
	public void broadCastSendingResult() {
		Log.d("BR_SMS_DELIVERY", "start receiver for firebase notification broadcast");
		Intent intent = new Intent(FCM_RESULT);
		intent.putExtra("isSuccessful",  "1");
		broadcaster.sendBroadcast(intent);
	}


}
