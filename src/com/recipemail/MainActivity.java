package com.recipemail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	
	public static String SENDER_ID = "132958600254";
	String TAG = "Main";
	public static Handler handler = null;
	
	private GCMController gcmController = null;
	private UserDbControl userDbControl = null;
	private TimerRegistrationChecker checker = null;
	private RegistrationCheckTask checkTask = null;
	
	private GCMIntentService gcmService = null;
	
	private ProgressDialog dialog = null;
	private String dialogInitialMsg = "Connecting...";
	
	private DialogTimeoutController dialogTimeoutController = null;
	private long dialogTimeoutDuration = 5000; // 10sec
	
	private Button registerButton = null;
	
	private MainViewController mainViewController = null;
	
	private boolean gcmRegistrationFlag = false;
	private boolean userDbRegistrationFlag = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // initialize
        this.init();
        
        // GCM Controller
        this.gcmController = new GCMController(this);
        
        // User DB Controller
        this.userDbControl = new UserDbControl(this);
        
        if(this.gcmController.CheckRegistration())
        {	
        	// Already Registered -> Show Setting
        	
        	setContentView(R.layout.settings);
        	setContentView(R.layout.welcome_registration);
        	this.initEventHandler();
        }
        else
        {
        	// Not Registered -> Show Initial Screen
        	setContentView(R.layout.welcome_registration);
        	this.initEventHandler();
        	this.gcmController.Register();
        }
        
        // 
        MainActivity.handler = new Handler(){
        	public void handleMessage(Message msg) {
        		onHandleMessage(msg);
        	}
        };
        
        // Initialize Dialog
        this.dialog = new ProgressDialog(this);
     	this.dialog.setMessage(this.dialogInitialMsg);
     	
     	// View Controller
     	this.mainViewController = new MainViewController(this);
     	
     	setContentView(R.layout.home_long);
    }
    
    private void onHandleMessage(Message msg)
    {
    	Log.d(TAG, "onHandleMessage called: ID:" + msg.what);
    	// this.dialog.dismiss();
    	
    	if(CommonMessageId.GCM_MESSAGE_ID_ON_REGISTERD == msg.what)
    	{
    		this.gcmRegistrationFlag = true;
    	}
    	else if(CommonMessageId.USER_DB_MESSAGE_ID_ON_COMPLETE == msg.what)
    	{
    		this.userDbRegistrationFlag = true;
    	}
    	
    	if(this.gcmRegistrationFlag && this.userDbRegistrationFlag)
    	{
    		this.mainViewController.setRegistrationCompletedView();
    		this.dialog.dismiss();
    	}
    	
    }
    
	private void initEventHandler()
    {
    	// Click event handler
        // this.registerButton = (Button)findViewById(R.id.buttonRegister);
    	// this.registerButton.setOnClickListener(this);
    }
    
    private void init()
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onClick(View arg0) {
		
		// this.executePostRequest();
		this.gcmController.Register();
		
		// User DB
	    this.userDbControl.Register(this.gcmController.GetCurrentId());
		this.dialog.show();
		this.dialogTimeoutController = new DialogTimeoutController(this.dialog, 10000);
	}
    
}