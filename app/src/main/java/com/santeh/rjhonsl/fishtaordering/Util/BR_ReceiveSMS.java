package com.santeh.rjhonsl.fishtaordering.Util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.santeh.rjhonsl.fishtaordering.Main.DeliveryConfirmation.Activity_ConfirmedOrders;
import com.santeh.rjhonsl.fishtaordering.Main.DeliveryConfirmation.Activity_DeliveryConfirmation;
import com.santeh.rjhonsl.fishtaordering.Main.SetupWizard.Activity_Welcome;
import com.santeh.rjhonsl.fishtaordering.R;

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
	String LOCKDOWN = "#LOCKDOWN";

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
//				"set[ :1A;Crab;PACKEDCRAB120G;PCs,CASE,KILOs];


//				String hexed = Helper.convert.HextoString(body);
				String hexed = body;

				if (hexed.length()>4){
					String first3 = body.substring(0, 3);

					if (first3.equalsIgnoreCase("#OF")){
						try {
							String[] splitted = body.split(";");
							String ofnumber = splitted[0].substring(3, splitted[0].length()).split("-")[1];
							String custID = splitted[0].substring(3, splitted[0].length()).split("-")[2];
							String batchnumber = splitted[0].substring(3, splitted[0].length()).split("-")[3];
							Log.d("OF", ofnumber + " " + custID);

							String items = "";
							for (int i = 1; i < splitted.length; i++) {
								if (i == 1) {
									items = items + splitted[i];
								} else {
									items = items + ";" + splitted[i];
								}
							}

							if (db.getServerNum().length() >10 && finalNumber.length()> 0){
								String cleanedServer = db.getServerNum().substring(db.getServerNum().length() - 10, db.getServerNum().length());
								String cleanedFinal = finalNumber.substring(finalNumber.length() - 10,finalNumber.length());
//								Log.d("ORDERCONF", ofnumber + " " + custID + "-" + db.getStoreName(custID) +"\n" +arranged+ "\n" +cletanedServer + "\n" +cleanedFinal);

								if(db.getStoreName(custID).length()>0 && cleanedFinal.equalsIgnoreCase(cleanedServer)){
									String idIfExisting = db.isOFExisting(body);//returns id if existing empty string if not
									String isOrExisting = db.isOFBatchExisting(ofnumber, custID, batchnumber);//returns id if existing empty string if not
									if (idIfExisting.length() > 0){
										Log.d("ORDERCONF", "already existing, exact");
										db.updateOF(idIfExisting, body, items);
									}else if (isOrExisting.length() > 0){
										Log.d("ORDERCONF", "already existing, same of batch num and store id");
										db.updateOF(isOrExisting, body, items);
									}else{
										String[] sampler = items.split(";");
										if (sampler[0].split(",").length<3){
											Log.d("ORDERCONF", "new order, illegal item format: " + sampler[0]);
										}else{
											Log.d("ORDERCONF", "new order, inserting");
											db.insertORConfirmation("", ofnumber, senderNum, body, finalDate+"",custID , "0", batchnumber, items);
											showNotification(context, "New Order Confirmation with: OF#"+ofnumber, 1025, "Fishta - OF#" + ofnumber);
										}

									}
								}else{
									Log.d("ORDERCONF", "CUSTOMER " + custID + " NOT ASSIGNED TO USER");
								}
							}

						}catch (Exception e){
							Log.e("Ordeconfirmation error", e.toString());
						}

					}else if (first3.equalsIgnoreCase("#DR")){
						try {
							String[] splitted = body.split(";");
							String drNumber = splitted[0].substring(3, splitted[0].length()).split("-")[1];
							String custID = splitted[0].substring(3, splitted[0].length()).split("-")[2];
							String batchnumber = splitted[0].substring(3, splitted[0].length()).split("-")[3];
							Log.d("OF", drNumber + " " + custID);

							String items = "";
							for (int i = 1; i < splitted.length; i++) {
								if (i == 1) {
									items = items + splitted[i];
								} else {
									items = items + ";" + splitted[i];
								}
							}

							String allItems = "";
							for (int i = 1; i < splitted.length; i++) {

								if (i == 1) {
									allItems = allItems + splitted[i];
								} else {
									allItems = allItems+";" + splitted[i];
								}
							}

							if (db.getServerNum().length() >10 && finalNumber.length()> 0){
								String cleanedServer = db.getServerNum().substring(db.getServerNum().length() - 10, db.getServerNum().length());
								String cleanedFinal = finalNumber.substring(finalNumber.length() - 10,finalNumber.length());

								if(db.getStoreName(custID).length()>0 && cleanedFinal.equalsIgnoreCase(cleanedServer)){
									String idIfExisting = db.isDRExisting(body);//returns id if existing empty string if not
									String isOrExisting = db.isDRBatchExisting(drNumber, custID, batchnumber);//returns id if existing empty string if not

									if (idIfExisting.length() > 0){
										db.updateDRITems(idIfExisting, body, allItems);
										showNotification(context, "You have received a Delivery Receipt with: DR#"+drNumber, 1024, "Fishta - Delivery Receipt");
									}if (isOrExisting.length() > 0){
										db.updateDRITems(idIfExisting, body, allItems);
										showNotification(context, "You have received a Delivery Receipt with: DR#"+drNumber, 1024, "Fishta - Delivery Receipt");
									}else{
										String[] sampler = items.split(";");
										if (sampler[0].split(",").length<3){
											Log.d("DELIVERYCONF", "new delivery, illegal item format: " + sampler[0]);
										}else {
											db.insertDeliveryConfirmation(drNumber, senderNum, allItems, "", body, "", System.currentTimeMillis() + "", "",
													custID, "0", "0", batchnumber);
											showNotification(context, "You have received a Delivery Receipt with: DR#" + drNumber, 1024, "Fishta - Delivery Receipt");
										}
									}
								}else{
									Log.d("DELIVERYCONF", "CUSTOMER " + drNumber + " NOT ASSIGNED TO USER");
								}
							}



						}catch (Exception e){
							Log.e("Ordeconfirmation error", e.toString());
						}


					}else if (hexed.substring(0, 3).equalsIgnoreCase("set")) {

						String keyvaluepair =  hexed.substring(hexed.indexOf("[")+1, hexed.indexOf("]"));
						String key = keyvaluepair.split(":")[0];
						String value = "";
						if (keyvaluepair.split(":")[1] != null) {
							value = keyvaluepair.split(":")[1];
						}

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

						}else if (key.equalsIgnoreCase(LOCKDOWN)) {
							Log.d("RECEIVE", "LOCK doWN");
							db.deleteALL(DBaseHelper.TBL_CUST);
							db.deleteALL(DBaseHelper.TBL_ITEMS);
							db.deleteALL(DBaseHelper.TBL_ORDEREDITEMS);
							db.deleteALL(DBaseHelper.TBL_SETINGS_KEYVAL_PAIR);
							db.deleteALL(DBaseHelper.TBL_SETTINGS);
							db.deleteALL(DBConstants.OrderConfirmation.tableName);
							db.deleteALL(DBConstants.DeliveryConfirmation.tableName);

							Intent intent1 = new Intent(context, Activity_Welcome.class);
							intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
							context.startActivity(intent);
						}
					}
				}


//				Log.d("Receive", "End if onReceive 'PDUs'");

			} // bundle is null
		} catch (Exception e) {
			// Log.d("SmsReceiver", "Exception smsReceiver: " +e);
		}//end of Try/Catch

	}   //end of onreceive


/**
 * The notification is the icon and associated expanded entry in the status bar.
 */
	protected void showNotification(Context context, String message, int notifID, String title) {

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(context)
						.setSmallIcon(R.drawable.fishta_logo)
						.setContentTitle(title)
						.setAutoCancel(true)
						.setColor(context.getResources().getColor(R.color.amber_100))
						.setContentText(message);

// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, Activity_ConfirmedOrders.class);
		if (notifID==1024){
			resultIntent = new Intent(context, Activity_DeliveryConfirmation.class);
		}else if(notifID==1025){
			 resultIntent = new Intent(context, Activity_ConfirmedOrders.class);
		}

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(Activity_ConfirmedOrders.class);
// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
				stackBuilder.getPendingIntent(
						0,
						PendingIntent.FLAG_UPDATE_CURRENT
				);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
		mNotificationManager.notify(notifID, mBuilder.build());

		
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
