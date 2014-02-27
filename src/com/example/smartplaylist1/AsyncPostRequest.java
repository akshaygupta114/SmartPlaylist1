package com.example.smartplaylist1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;


class AsyncPostRequest extends AsyncTask<BasicNameValuePair, Integer, Double> {
	private String URL;
	
	public void postData(BasicNameValuePair... args) {
		HttpClient httpclient = new DefaultHttpClient();
		 HttpPost httppost = new HttpPost(this.URL);

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        for (BasicNameValuePair value: args) {
		        	nameValuePairs.add(value);
		        }
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse resp = httpclient.execute(httppost);
		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
	}

	public void setURL(String URL) {
		this.URL = URL;
	}
	
	@Override
	protected Double doInBackground(BasicNameValuePair... arg0) {
		// TODO Auto-generated method stub
		postData(arg0);
		return null;
	}

}
