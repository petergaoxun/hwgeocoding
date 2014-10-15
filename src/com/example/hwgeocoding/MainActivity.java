package com.example.hwgeocoding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void submit(View view) throws ClientProtocolException, IOException{
    	EditText text  = (EditText) findViewById(R.id.editText1);
    	//find the text user input in the plain text bar
    	String res = text.getText().toString().trim();
    	System.out.println(res);
    	res = res.replaceAll("\\s+", "+");
    	
    	if (res.equalsIgnoreCase("no info")){
    		return;
    	}
    	System.out.println(res);
    	String finish = "http://maps.googleapis.com/maps/api/geocode/json?address="+res;
    	System.out.println(finish);

    	if (android.os.Build.VERSION.SDK_INT > 9){
    		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    		StrictMode.setThreadPolicy(policy);
    	}
    	
    	
    	HttpClient client = new DefaultHttpClient();
    	HttpGet request = new HttpGet(finish);
    	HttpResponse response;
    	try {
    		response = client.execute(request);

    		// some response object
    		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
    		StringBuilder builder = new StringBuilder();
    		for (String line = null; (line = reader.readLine()) != null;) {
    		    builder.append(line).append("\n");
    		}
    		System.out.println(builder.toString());
    		JSONTokener tokener = new JSONTokener(builder.toString());
    		try {
				JSONObject results = new JSONObject(tokener);
				
				JSONArray result = (JSONArray) results.get("results");
				JSONObject first = result.getJSONObject(0);
				String address = first.getString("formatted_address");
				JSONObject geometry = (JSONObject) first.get("geometry");
				JSONObject location = (JSONObject) geometry.get("location");
				double lng =  (Double) location.get("lng");
				double lat = (Double) location.get("lat");
				System.out.println(lng + " " + lat);
				
		    	Intent intent = new Intent();
		    	intent.setClass(this, MapViewActivity.class);
				intent.putExtra("lng", lng);
				intent.putExtra("lat", lat);
				intent.putExtra("address", address);
				startActivity(intent);
			} catch (JSONException e) {
				e.printStackTrace();
			}
    		
    		
    		Log.d("Response of request", response.toString());
    	} catch (ClientProtocolException e){
    		e.printStackTrace();
    	} catch (IOException e){
    		e.printStackTrace();
    	}
	}
}
