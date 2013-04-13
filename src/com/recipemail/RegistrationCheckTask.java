package com.recipemail;

import java.util.TimerTask;

import android.os.Handler;

public class RegistrationCheckTask extends TimerTask {
	
	private Handler handler = null;
	
	public RegistrationCheckTask(Handler handler_value)
	{
		this.handler = handler_value;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Send Test
		this.handler.sendEmptyMessage(0);
	}

}
