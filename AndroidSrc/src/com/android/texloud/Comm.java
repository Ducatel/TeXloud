package com.android.texloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.util.Log;

public class Comm{

	public static final String URLauth = "http://172.16.21.183/texloud/login.php";

	public enum statement{SUCCESS, WRONG, ERROR};
	
	public Comm(){

	}


	public Comm.statement getAuth(CharSequence login, CharSequence password){
		InputStream is = null;
		String result = "";

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

		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),30);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString().trim();
			Log.i("str", result);
			
			if(result.equals("ok")){
				Log.i("ok", result);
				return Comm.statement.SUCCESS;
			}
			else{ 
				Log.i("pas ok", result);
				return Comm.statement.WRONG;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Comm.statement.ERROR;
	}
}
