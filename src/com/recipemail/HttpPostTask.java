package com.recipemail;

import java.io.IOException;
import java.util.ArrayList;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpPostTask extends AsyncTask<String, Integer, Long> {
	
	private Activity parent_activity = null;
	private String url = "";
	private Handler ui_handler = null;
	private ArrayList<NameValuePair> postParams = null;
	private ProgressDialog dialog = null;
	private String dialogInitialMsg = "Connecting...";
	private ResponseHandler<Void> responseHandler = null;
	private String httpRetMessage = "";
	private String httpResponseEncoding = "UTF-8";
	private String httpRequestEncoding = "UTF-8";
	private String httpErrMessage = "";
	
	private String TAG = "HttpPostTask";
	
	public HttpPostTask(Activity activity_value, String url_value, Handler handler_value){
		
		this.parent_activity = activity_value;
		this.url = url_value;
		this.ui_handler = handler_value;
		
		this.postParams = new ArrayList<NameValuePair>();
	};
	
	public void addRequestParam(String name, String value){
		this.postParams.add(new BasicNameValuePair(name, value));
	};
	
	@Override
	// Execute on UI thread before async task starts
	protected void onPreExecute()
	{
		// Dialog
		this.dialog = new ProgressDialog(this.parent_activity);
		this.dialog.setMessage(this.dialogInitialMsg);
		this.dialog.show();
		
		this.responseHandler = new ResponseHandler<Void>() {

			@Override
			public Void handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				
				// Get Status Code
				int statusCode = response.getStatusLine().getStatusCode();
				Log.d(TAG, "Status Code=" + statusCode);
				switch(statusCode)
				{
				case HttpStatus.SC_OK:
					Log.d(TAG, "Get HttpResponse Success.");
					HttpPostTask.this.httpRetMessage = EntityUtils.toString(response.getEntity(),
							HttpPostTask.this.httpResponseEncoding);
					
					break;
					
				case HttpStatus.SC_NOT_FOUND:
					Log.d(TAG, "HttpResponse Not Found");
					
					break;
				
					default:
						Log.d(TAG, "");
						HttpPostTask.this.httpErrMessage = "Error Occured in getting HttpResponse";
				}
				
				
				return null;
			}
		};
	}
	
	@Override
	protected Long doInBackground(String... params) {
		
		Log.d(TAG, "doInBackground called");
		Log.d(TAG, "Try POST Request");
		
		// URL
		URI uri = null;
		try {
			uri = new URI(this.url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.httpErrMessage = "Failed to create URI obj";
			return null;
		}
		
		// Ready Request
		HttpPost postRequest = new HttpPost(uri);
		try{
			postRequest.setEntity(new UrlEncodedFormEntity(this.postParams, this.httpRequestEncoding));
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		// Execute Post Request
		DefaultHttpClient httpClient = new DefaultHttpClient();
		Log.d(TAG, "Starting POST request...");
		
		try {
			httpClient.execute(postRequest, this.responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.d(TAG, "ClientProtocolException error");
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "IO Exception error");
		}
		
		// Shut donw
		httpClient.getConnectionManager().shutdown();
		Log.d(TAG, "Connection Shut down");
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Long result)
	{
		// Close dialog
		this.dialog.dismiss();
		
		Message msg = new Message();
		Bundle bdl = new Bundle();
		
		if(this.httpErrMessage.isEmpty())
		{
			// Request Succeed
			bdl.putBoolean("httppost_success", true);
			bdl.putString("http_response", this.httpRetMessage);
			
		}
		else
		{
			
			// Error Occured somewhere
			bdl.putBoolean("httppost_success", false);
			bdl.putString("http_response", this.httpErrMessage);
		}
		
		msg.setData(bdl);
		
		// Send data to UI thread
		this.ui_handler.sendMessage(msg);
		
	}
}
