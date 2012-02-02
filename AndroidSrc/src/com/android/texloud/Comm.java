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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Comm{


	public static final String IP = "192.168.0.2";

	public static final String URLauth = "http://"+IP+"/android/login";
	public static final String URLgetFile = "http://"+IP+"/android/getFile";
	public static final String URLsignin = "http://"+IP+"/android/createAccount";
	private String tree;

	public enum statement{SUCCESS, WRONG, ERROR};

	public Comm(){

	}


	public Comm.statement getAuth(CharSequence login, CharSequence password) throws JSONException{

		InputStream is = null;
		tree = "";

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
			reader = new BufferedReader(new InputStreamReader(is,"UTF8"),30);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			is.close();
			tree=sb.toString();
			Log.i("str", tree);

			if(tree.equals("ko"))
				return Comm.statement.WRONG;

			else{
				//Traitement JSON arborescence

				//JSONArray jArray = new JSONArray(result);


				return Comm.statement.SUCCESS;
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Comm.statement.ERROR;

	}
	public String getTree(){
		return tree;
	}


	public String getFile(String fileId){
		String result = "";

		InputStream is = null;
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("fileId", fileId));
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLgetFile);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		}catch(Exception e){
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(is,"UTF8"),30);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString().trim();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	//TODO à terminer
	public void compilRequest(String namefile){
		InputStream is = null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("file", namefile));

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

		// Reception pdf : ?
	}

	public void signIn(CharSequence firstName, CharSequence lastName, CharSequence userName, CharSequence mail, CharSequence password, CharSequence address, String gender, 
			CharSequence city, CharSequence country, CharSequence zip, CharSequence year, String month, CharSequence day){

		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username", userName.toString()));
		nameValuePairs.add(new BasicNameValuePair("email", mail.toString()));
		nameValuePairs.add(new BasicNameValuePair("password", password.toString()));

		// Champs optionnels :
		nameValuePairs.add(new BasicNameValuePair("firstname", firstName.toString()));
		nameValuePairs.add(new BasicNameValuePair("lastname", lastName.toString()));
		nameValuePairs.add(new BasicNameValuePair("address", address.toString()));
		nameValuePairs.add(new BasicNameValuePair("gender", gender.toString()));
		nameValuePairs.add(new BasicNameValuePair("city", city.toString()));
		nameValuePairs.add(new BasicNameValuePair("country", country.toString()));
		nameValuePairs.add(new BasicNameValuePair("zip", zip.toString()));
		nameValuePairs.add(new BasicNameValuePair("year", year.toString()));
		nameValuePairs.add(new BasicNameValuePair("month", month));
		nameValuePairs.add(new BasicNameValuePair("day", day.toString()));

		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLsignin);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		}catch(Exception e){
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		BufferedReader reader;
		String result = "";
		try {
			reader = new BufferedReader(new InputStreamReader(is,"UTF8"),30);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString().trim();
			Log.i("SignIn return", result);


		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
