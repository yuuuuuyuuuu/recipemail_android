package com.recipemail;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainViewController {
	
	private Activity activity = null;
	
	public MainViewController(Activity activity_value)
	{
		this.activity = activity_value;
	}
	
	public void setRegistrationCompletedView()
	{
		// Show Ready Text
		// TextView text = (TextView) this.activity.findViewById(R.id.textViewReadyToUse);
		// text.setVisibility(View.VISIBLE);
		
		// Hide Register Button
		// Button button = (Button)this.activity.findViewById(R.id.buttonRegister);
		// button.setVisibility(View.INVISIBLE);
	}
}
