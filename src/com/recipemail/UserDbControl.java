package com.recipemail;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;
import android.text.format.Time;
import android.util.Log;

public class UserDbControl extends DbControl {

	private Context context = null;
	private String serverUrl = "http://ec2-54-248-37-34.ap-northeast-1.compute.amazonaws.com:3000";
	
	// Post Parameter name
	private String paramName_regId = "registration_id";
	private String paramName_userRegDate = "user_registraion_date";
	private String paramName_favoriteTime = "favorite_time";
	private String paramName_favoriteCategoryId1 = "favorite_category_id1";
	private String paramName_favoriteCategoryId2 = "favorite_category_id2";
	private String paramName_favoriteCategoryId3 = "favorite_category_id3";
	
	private String TAG = "UserDbControl";
	
	public UserDbControl(Context context_value)
	{
		this.context = context_value;
	}
	
	public boolean Register(String regId_value) {
		
		if (android.os.Build.VERSION.SDK_INT > 9) { 
			StrictMode.ThreadPolicy policy = 
					new StrictMode.ThreadPolicy.Builder().permitAll().build(); 
			StrictMode.setThreadPolicy(policy); 
		}
		
		Log.d(TAG, "executing POST request");
		
		// Define Async Task
		HttpPostTask postTask = new HttpPostTask((Activity) this.context, this.serverUrl, 
				new HttpPostHandler() {
					
					@Override
					public void onPostFailed(String response) {
						Log.d(TAG, "Post Failed");
						//MainActivity.this.textView.setText(response);
						MainActivity.handler.sendEmptyMessage(CommonMessageId.USER_DB_MESSAGE_ID_ON_FAILED);
					}
					
					@Override
					public void onPostCompleted(String response) {
						Log.d(TAG, "Post Success");
						MainActivity.handler.sendEmptyMessage(CommonMessageId.USER_DB_MESSAGE_ID_ON_COMPLETE);
					}
				});
		
		// Set parameter
		postTask.addRequestParam(this.paramName_regId, regId_value);
		postTask.addRequestParam(this.paramName_userRegDate, this.getCurrentTime());
		postTask.addRequestParam(this.paramName_favoriteCategoryId1, "not_set");
		postTask.addRequestParam(this.paramName_favoriteCategoryId2, "not_set");
		postTask.addRequestParam(this.paramName_favoriteCategoryId3, "not_set");
		
		// Execute
		postTask.execute();
		
		return false;
	}

	@Override
	public boolean Unregister() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean CheckRegistration() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private String getCurrentTime()
	{	
		return java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
	}

}
