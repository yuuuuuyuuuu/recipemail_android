package com.recipemail;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMBroadcastReceiver;

public class GCMIntentService extends GCMBaseIntentService {
	
	private Handler uiHandler = null;
	
	public GCMIntentService()
	{
		super(MainActivity.SENDER_ID);		
	}
	
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Log.d("onMessage", arg1.toString());
		
		String n_title = arg1.getStringExtra("notification_title");
		Log.d("onMessage:notification_title", n_title);
		String i_title = arg1.getStringExtra("info_title");
		Log.d("onMessage:info_title", i_title);
		String i_content = arg1.getStringExtra("info_content");
		Log.d("onMessage:info_content", i_content);
		String i_url = arg1.getStringExtra("info_url");
		Log.d("onMessage:i_url", i_url);
		
		this.ShowNotification(n_title, i_title, i_content, i_url);
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.d("onRegistered", "onRegistered called");
		MainActivity.handler.sendEmptyMessage(CommonMessageId.GCM_MESSAGE_ID_ON_REGISTERD);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
		MainActivity.handler.sendEmptyMessage(CommonMessageId.GCM_MESSAGE_ID_ON_UNREGISTERED);
	}

	private void ShowNotification(String notification_title, String information_title, String information_content, String information_url)
	{
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(
				R.drawable.dish_icon_test,
				notification_title,
				System.currentTimeMillis());
		
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(information_url));
		//intentの設定
		PendingIntent contentIntent =
				PendingIntent.getActivity(this, 0, intent, 0);
		
		notification.setLatestEventInfo(
				getApplicationContext(),
				information_title,
				information_content,
				contentIntent);
		
		notificationManager.cancelAll();
		notificationManager.notify(R.string.app_name, notification);
		
	}
}
