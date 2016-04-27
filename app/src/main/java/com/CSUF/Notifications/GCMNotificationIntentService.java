package com.CSUF.Notifications;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.CSUF.EventFy.EventInfoActivity;
import com.CSUF.EventFy.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMNotificationIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	static  int numMessages = 0;
	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCMNotificationIntentService";

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		//if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				for (int i = 0; i < 3; i++) {
					//Log.i(TAG, "Working. " +(i+1)+"/5 @ ");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}

		//		}
			//	Log.i(TAG, "Cop work ");

				sendNotification(""
						+ extras.get(Config.MESSAGE_KEY));
			//	Log.i(TAG, "Received: " + extras.toString());
			}
		}
	//	GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {

		Intent intent = new Intent(this, EventInfoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
				PendingIntent.FLAG_ONE_SHOT);

		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.logo)
				.setContentTitle("EventFy")
				.setContentText(msg)
				.setAutoCancel(true)
				.setSound(defaultSoundUri)
				.setContentIntent(pendingIntent);



// Start of a loop that processes data and then notifies the user

		notificationBuilder.setContentText(msg)
				.setNumber(++numMessages);
		// Because the ID remains unchanged, the existing notification is
		// updated.



		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


//	//	Log.d(TAG, "Preparing to send notification...: " + msg);
//		mNotificationManager = (NotificationManager) this
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//
//		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//				new Intent(this, MainActivity.class), 0);
//
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//				this).setSmallIcon(R.drawable.logo)
//				.setContentTitle("EventFy")
//				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//				.setContentText(msg);
//
//		mBuilder.setContentIntent(contentIntent);
//		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	//	Log.d(TAG, "Noti sent s");
	}
}
