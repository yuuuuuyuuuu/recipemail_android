package com.recipemail;

import android.app.Dialog;
import android.os.Handler;

public class DialogTimeoutController {
	
	
	public DialogTimeoutController(final Dialog dialog_value, long duration_value)
	{
		this.setTimeout(dialog_value, duration_value);
	}
	
	private void setTimeout(final Dialog dialog_value, long duration_value)
	{
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				dialog_value.cancel();
			}
			
		}, duration_value);
	}
}
