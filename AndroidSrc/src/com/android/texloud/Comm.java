package com.android.texloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Comm{


	public static final String IP = "192.168.0.2";

	public static final String URLauth = "http://"+IP+"/android/login";
	public static final String URLgetFile = "http://"+IP+"/android/getFile";
	public static final String URLsignin = "http://"+IP+"/android/createAccount";
	public static final String URLnewproject = "http://"+IP+"/android/createProject";
	public static final String URLnewfile = "http://"+IP+"/android/createTextFile";
	public static final String URLsync = "http://"+IP+"/android/sync";

	private String sessionID;
	
	public enum statement{SUCCESS, WRONG, ERROR};

	static HttpContext localContext = new BasicHttpContext();
	static CookieStore cookieStore = new BasicCookieStore();
	DefaultHttpClient client = new DefaultHttpClient();
	
	public Comm(){
		
	}
	
	public Comm(BasicHttpContext bhc, CookieStore cs){
		localContext = bhc;
		cookieStore = cs;
	}
	

	public static String getAuth(CharSequence login, CharSequence password) throws JSONException{
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		InputStream is = null;
		String tree;

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
			cookieStore = new BasicCookieStore();
		    localContext = new BasicHttpContext();
		    localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		    
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLauth);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			System.out.println("executing request " + httppost.getURI());
			HttpResponse response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
			for(Cookie ck : cookieStore.getCookies()){
				Log.i("cookie", ck.toString());
			}

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
				return null;

			else{
				//Traitement JSON arborescence
				
				/*String[] s = tree.split("-==sep==-");
				Log.i("split", s[0]);
				Log.i("split", s[1]);*/
				
				return tree;
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public String getSessionID() {
		return sessionID;
	}



	public static String getFile(String fileId){
		String result = "";

		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("fileId", fileId));

		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLgetFile);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost, localContext);
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
	public static void compilRequest(String namefile){
		InputStream is = null;
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("file", namefile));

		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLauth);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		}catch(Exception e){
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// Reception pdf : ?
	}

	
	//TODO
	public static void syncFile(HashMap<String, String> map){
		InputStream is = null;
		String result = "";
		
		JSONObject jo = new JSONObject(map);
		
		Log.i("HashMap en JSON", jo.toString());
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("files", jo.toString().trim()));

		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLsync);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			System.out.println("executing request " + httppost.getURI());
			HttpResponse response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
			for(Cookie ck : cookieStore.getCookies()){
				Log.i("cookie", ck.toString());
			}

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
			Log.i("Sync result", result);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	public static void signIn(CharSequence firstName, CharSequence lastName, CharSequence userName, CharSequence mail, CharSequence password, CharSequence address, String gender, 
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
			HttpResponse response = httpclient.execute(httppost, localContext);
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

	public static void createProject(String name){
		
		InputStream is = null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("projectName", name));
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLnewproject);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			System.out.println("executing request " + httppost.getURI());
			HttpResponse response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
			for(Cookie ck : cookieStore.getCookies()){
				Log.i("cookie", ck.toString());
			}

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
			Log.i("createProject return", result);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createFile(String name, String parentId, String projectId){
		InputStream is = null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("fileName", name));
		nameValuePairs.add(new BasicNameValuePair("parentId", parentId));
		nameValuePairs.add(new BasicNameValuePair("projectId", projectId));
		
		Log.i("projectId", projectId);
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLnewfile);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			System.out.println("executing request " + httppost.getURI());
			HttpResponse response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
			for(Cookie ck : cookieStore.getCookies()){
				Log.i("cookie", ck.toString());
			}

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
			Log.i("createFile return", result);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
