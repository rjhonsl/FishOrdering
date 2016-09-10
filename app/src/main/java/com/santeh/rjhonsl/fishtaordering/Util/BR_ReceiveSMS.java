package com.santeh.rjhonsl.fishtaordering.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class BR_ReceiveSMS extends BroadcastReceiver {
	// Get the object of SmsManager
	public static String contactNumber="", contactName="" , branchName="";
	String branchCode="";
	Context ctx;

	String SetServerNum = "sn";
	String SetSTORE = "ss";
	String SetPIN = "pn";
	String LoopCount = "lc";
	String AddItem = "itm";

	DBaseQuery db;

	public void onReceive(Context context, Intent intent) {
		ctx = context;
		// Retrieves a map of extended data from the intent.
		final Bundle extras = intent.getExtras();
		try {
			if (extras != null) {

				Object[] pdusObj = (Object[]) extras.get("pdus");
				SmsMessage[] messages = new SmsMessage[pdusObj.length];
				
				for (int i = 0; i < pdusObj.length; i++){
					messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
				}

				//gets all values from new messages and puts it to variables for storage
				String finalmessage, finalNumber, finalBody;
				long finalDate; int finalStatus;
				finalNumber = messages[0].getDisplayOriginatingAddress();
				finalBody =  messages[0].getMessageBody();
				finalDate = messages[0].getTimestampMillis();
				finalStatus = messages[0].getStatus();
				
//				check if message is multipart or not
				if (messages.length == 1) {
					finalmessage = messages[0].getDisplayMessageBody();
				}
				else{
					StringBuilder bodyText = new StringBuilder();
					for (int a = 0; a < messages.length; a++) {
					      bodyText.append(messages[a].getDisplayMessageBody());
					    }
					finalmessage = String.valueOf(bodyText);
				}


				String phoneNumber = finalNumber;
				String senderNum = phoneNumber;
				String message = finalmessage;
				String body = finalBody;
				long date = finalDate;
				int status = finalStatus;
				
				contactName = phoneNumber;
				Log.d("Receive", "I received something from: " + senderNum + " - " + body.substring(0, 3) + " - " + finalDate);

				db = new DBaseQuery(context);
				db.open();

//				"set[sn:09159231478];
//				"set[ss:puregold baliwag];
//				"set[itm:1A;Crab;PACKEDCRAB120G;PCs,CASE,KILOs];


				String hexed = Helper.convert.HextoString(body);
				if (hexed.substring(0, 3).equalsIgnoreCase("set")) {

					String keyvaluepair =  hexed.substring(hexed.indexOf("[")+1, hexed.indexOf("]"));
					String key = keyvaluepair.split(":")[0];
					String value = keyvaluepair.split(":")[1];

					if (key.equalsIgnoreCase(SetServerNum)){
						db.updateSettingsServerNum(value);
//						Log.d("Receive", "ServerNum Updated");
					} else if (key.equalsIgnoreCase(SetSTORE)) {
						db.updateSettingsStoreName(value);
//						Log.d("Receive", "Storename Updated");
					}else if (key.equalsIgnoreCase(LoopCount)) {
						db.updateSettingsLoopCount(value);
//						Log.d("Receive", "loopcount Updated");
					}else if (key.equalsIgnoreCase(SetPIN)) {
						db.updateSettingsPin(value);
//						Log.d("Receive", "Pin Updated");
					}else if (key.equalsIgnoreCase(AddItem)) {

//						Log.d("Receive", "Added Item: "+ i +" : " + key + " : "+ value);
					}


				}

//				Log.d("Receive", "End if onReceive 'PDUs'");

			} // bundle is null
		} catch (Exception e) {
			// Log.d("SmsReceiver", "Exception smsReceiver: " +e);
		}//end of Try/Catch

	}   //end of onreceive


/**
 * The notification is the icon and associated expanded entry in the status
 * bar.
 */
	protected void showNotification(String contactId, String message) {
		
//		// Log.d("SMS", String.valueOf("NOTIF"));
//		settings = PreferenceManager.getDefaultSharedPreferences(ctx);
//		String prefValue = settings.getString("pref_tone", "Silent");
//		boolean bool_vibrate = settings.getBoolean("pref_vibrate", false);
//		boolean bool_silent = settings.getBoolean("pref_silent_mode", false);
//		boolean bool_showNotif = settings.getBoolean("pref_show_notif", false);
//		Vibrator v = (Vibrator) this.ctx.getSystemService(Context.VIBRATOR_SERVICE);

//		Uri sound = Uri.parse(prefValue);
//
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx);
//		if (bool_showNotif) {
//		mBuilder.setSmallIcon(R.drawable.white_mail);
//		mBuilder.setContentTitle("SMS Inquiry");
//		mBuilder.setContentText("You have new Delivery Request!");
//		}
//
//		mBuilder.setAutoCancel(true);
//		mBuilder.setNumber(counter);
//		// Log.d("TONE_notif", String.valueOf(prefValue));
//
//		if (!bool_silent) {
//			mBuilder.setSound(sound);
//		}
//		if (bool_vibrate) {
//			long[] pattern = {1000, 500, 1000, 500};
//			mBuilder.setVibrate(pattern);
//		}
//
////		int numMessages = 0;
//
//		Intent resultIntent = new Intent(ctx, MainActivity.class);
//		// Because clicking the notification opens a new ("special") activity, there's
//		// no need to create an artificial back stack.
//		PendingIntent resultPendingIntent =
//		    PendingIntent.getActivity(
//		    ctx,
//		    0,
//		    resultIntent,
//		    PendingIntent.FLAG_UPDATE_CURRENT
//		);
//		mBuilder.setContentIntent(resultPendingIntent);
//		int mNotificationId = 001;
//		// Gets an instance of the NotificationManager service
//		NotificationManager mNotifyMgr =
//				(NotificationManager) ctx.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
//		// Builds the notification and issues it.
//		mNotifyMgr.notify(mNotificationId, mBuilder.build());


	}//end of show Notification




}
