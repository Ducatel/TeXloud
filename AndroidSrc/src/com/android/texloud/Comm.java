package com.android.texloud;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class Comm{

	public static final String URLauth = "... login.php";
	private String returnString;

	public Comm(){

	}

	
	public boolean getAuth(CharSequence login, CharSequence password) throws JSONException{
		InputStream is = null;
		
	
		// Envoyer la requète au script PHP.
		// Envoi de la commande http
		
		String string_login, string_password;
		string_login = string_password = "";
		
		for(int i=0; i<login.length(); i++){
			string_login += login.charAt(i);
		}
		
		for(int i=0; i<password.length(); i++){
			string_password += password.charAt(i);
		}
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("login", string_login));
		nameValuePairs.add(new BasicNameValuePair("password", string_password));
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLauth);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();		
			
		}catch(Exception e){
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		
		return true;
	}
}
