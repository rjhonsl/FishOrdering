package com.santeh.rjhonsl.fishtaordering.Util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.santeh.rjhonsl.fishtaordering.Main.Activity_OrderHistory;
import com.santeh.rjhonsl.fishtaordering.Main.MainActivity;

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

		String action = intent.getAction();
		String timesend = intent.getStringExtra("timesent");
		String content = intent.getStringExtra("content");
		String number = intent.getStringExtra("sendto");
		String type = intent.getStringExtra("type");

		String isSent = "0";


//		Log.d("DELIVERY", "SENDING INTENT");
		//checks if intent action passed is equals to sms sent
		if (action.equals(ACTION_SMS_SENT)) {
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				isSent = "1";
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				isSent = "0";
				break;
			case SmsManager.RESULT_ERROR_NO_SERVICE:
				isSent = "0";
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				isSent = "0";
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				isSent = "0";
				break;
			}


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
							MainActivity.showNoItemImage();
						}
					}

					long orderID = db.insertOrderHistory(number, content, timesend, "0", isSent, "0");

					String[] contentss = content.split(";");
					for (int i = 0; i < contentss.length; i++) {

						if (i > 0){
							String[] itemdetails = contentss[i].split(",");
//							itemdetails[0] //itemid
//							itemdetails[1] //itemqty
//							itemdetails[2] //itemunits
							db.insertOrderedItems(itemdetails[0], orderID + "");
						}
					}



				}

			}else if (type.equalsIgnoreCase(SendSMS.MESSAGE_TYPE_RESEND)){

				if (isSent.equalsIgnoreCase("0")){
					Toast.makeText(context, "Rending Failed. Please try again", Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(context, "Resend Successful", Toast.LENGTH_LONG).show();
				}

				String hstid = intent.getStringExtra("hstid");
				int itemcount = Integer.valueOf(intent.getStringExtra("listcount"));
				int itemPosition = Integer.valueOf(intent.getStringExtra("pos"));

				db.updateSentHistory(hstid, number, content, timesend, "0", isSent, "0");
				if (Activity_OrderHistory.isActive) {
					Intent refresh = new Intent(context, Activity_OrderHistory.class);
					refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
					context.startActivity(refresh);
				}

//				if (Activity_OrderHistory.orderHistoryAdapter!=null) {
//					Activity_OrderHistory.orderHistoryAdapter.notifyDataSetChanged();
////					Activity_OrderHistory.orderHistoryAdapter.notifyItemRangeChanged(itemPosition, itemcount);
////					Activity_OrderHistory.orderHistoryAdapter.notifyItemRangeChanged(Integer.valueOf(itemPosition), Integer.valueOf(itemcount));
////					Activity_OrderHistory.orderHistoryAdapter.notifyItemChanged(Integer.valueOf(itemPosition));
//				}
			}
		}
	}





}
