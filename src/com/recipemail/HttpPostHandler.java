package com.recipemail;

import android.os.Handler;
import android.os.Message;

public abstract class HttpPostHandler extends Handler {
	
	public void handleMessage(Message msg)
	{
		boolean isPostSuccess = msg.getData().getBoolean("httppost_success");
		String httpResponse = msg.getData().getString("http_response");
		
		if(isPostSuccess)
		{
			this.onPostCompleted(httpResponse);
		}
		else
		{
			this.onPostFailed(httpResponse);
		}
	}
	
	public abstract void onPostCompleted(String response);
	
	public abstract void onPostFailed(String response);
}
