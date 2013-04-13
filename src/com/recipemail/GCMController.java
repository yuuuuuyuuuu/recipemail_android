package com.recipemail;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class GCMController {
	
	private Context context = null;
	private String SENDER_ID = "132958600254";
	private String TAG = "GCMController";
	private String regId = "";
	
	public GCMController(Context context_value)
	{
		this.context = context_value;
		
		GCMRegistrar.checkDevice(this.context);
        GCMRegistrar.checkManifest(this.context);
        
	}
	
	public boolean CheckRegistration()
	{
		boolean isRegistered = false;
		
		String regId = this.getRegistrationId();
		if("" != regId) isRegistered = true;
		
		Log.d(this.TAG, "regId: " + regId);
		
		return isRegistered;
	}
	
	public boolean Register()
	{
		boolean isSucceeded = false;
		
		GCMRegistrar.register(this.context, this.SENDER_ID);
		
		return isSucceeded = true;
	}
	
	private String getRegistrationId()
	{
		GCMRegistrar.checkDevice(this.context);
        GCMRegistrar.checkManifest(this.context);
        return GCMRegistrar.getRegistrationId(this.context);
	}
	
	public String GetCurrentId()
	{
		if("" == this.regId)
		{
			this.regId = GCMRegistrar.getRegistrationId(this.context);
		}
		
		return this.regId;
	}
}
