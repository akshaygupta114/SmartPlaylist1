package com.example.smartplaylist1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.util.Log;

public class GetRequest {
	private String URL;
	
	public String getData(BasicNameValuePair... args) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(this.URL);
		
		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        for (BasicNameValuePair value: args) {
		        	nameValuePairs.add(value);
		        }
		       
		      //  httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        
		        HttpResponse response = httpclient.execute(httpGet);
		        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		        StringBuilder builder = new StringBuilder();
		        for (String line = null; (line = reader.readLine()) != null;) {
		            builder.append(line).append("\n");
		        }
		        JSONTokener tokener = new JSONTokener(builder.toString());
		        JSONArray finalResult = new JSONArray(tokener);
		        Log.i("ASyncGet", finalResult.toString());
		        return finalResult.toString();
		    }catch(JSONException jse){
		    
		    }
		    catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
		    return null; //returns null when query fails
	}

	public void setURL(String URL) {
		this.URL = URL;
	}
}

