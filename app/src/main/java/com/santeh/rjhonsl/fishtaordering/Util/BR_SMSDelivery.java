package com.santeh.rjhonsl.fishtaordering.Util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class BR_SMSDelivery extends BroadcastReceiver {

	private final String DEBUG_TAG = getClass().getSimpleName();
	private static final String ACTION_SMS_SENT = SendSMS.ACTION_SENDORDER;
	public static boolean stillsending = false;
	Context ctx;


	// When the SMS has been sent
	public void onReceive(Context context, Intent intent) {
		ctx=context;
		String action = intent.getAction();
		String timesend = intent.getStringExtra("timesent");
		String content = intent.getStringExtra("content");
		String number = intent.getStringExtra("sendto");


		Log.d("DELIVERY", "SENDING INTENT");
		//checks if intent action passed is equals to sms sent
		if (action.equals(ACTION_SMS_SENT)) {
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				Toast.makeText(context, "SENT", Toast.LENGTH_LONG).show();
				Bundle b = intent.getExtras();
				if (b != null) {
					String value = b.getString("extra_key");
				}
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				Toast.makeText(context, "SENDING FAILED! Check Operator Service!", Toast.LENGTH_LONG)
				.show();
				break;
			case SmsManager.RESULT_ERROR_NO_SERVICE:
				Toast.makeText(context, "SENDING FAILED! No Network Service!", Toast.LENGTH_LONG)
				.show();
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				Toast.makeText(context, "Null PDU", Toast.LENGTH_LONG).show();
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				Toast.makeText(context, "SENDING FAILED! Connectivity is Off!", Toast.LENGTH_LONG).show();
				break;
				
			}


		}
	}





}
