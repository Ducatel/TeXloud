package com.android.texloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
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
import org.json.JSONArray;
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
	public static final String URLgetJSON = "http://"+IP+"/android/getJsonTree";
	public static final String URLcompil = "http://"+IP+"/android/processCompile";
	public static final String URLpdf = "http://"+IP+"/android/getPdf";
	public static final String URLconfirmPDF = "http://"+IP+"/android/deletePdfFile";
	public static final String URLlogout = "http://"+IP+"/user/logout";
	public static final String URLdeleteProject = "http://"+IP+"/android/deleteProject";
	public static final String URLnewfolder = "http://"+IP+"/android/createFolder";
	public static final String URLdeletefile = "http://"+IP+"/android/deleteFile";
	public static final String URLdeletefolder = "http://"+IP+"/android/deleteFolder";
	//public static final String URLrenameFile;
	
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
	
	public static void clearCookie(){
		cookieStore.clear();
	}
	

	public static String getAuth(CharSequence login, CharSequence password) throws JSONException{
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		InputStream is = null;
		String tree;

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
			
			System.out.println("executing request " + httppost.getURI());
			HttpResponse response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
			for(Cookie ck : cookieStore.getCookies()){
				Log.i("cookie", ck.toString());
			}

		}catch(Exception e){
			Log.e("log_tag", "Error in http connection " + e.toString());
			return "pb_connect";
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
			Log.i("getAuth return", tree);
			
			return tree; // Vaut "ko" en cas de mauvais login
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public static String getJSON(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		InputStream is = null;
		String tree;
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLgetJSON);
			
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
			Log.i("getJSON return", tree);

			if(tree.equals("ko"))
				return null;

			else{
				
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
	
	public static void deleteFolder(String fileId, ArrayList<String> children){
		
		JSONArray ja = new JSONArray(children);
		
		Log.i("To delete (JSON) : ", ja.toString());
		
		InputStream is = null;
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("fileId", fileId));
		nameValuePairs.add(new BasicNameValuePair("childrenJson", ja.toString()));
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLdeletefolder);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			Log.i("suppression dossier", fileId);
			HttpResponse response = httpclient.execute(httppost, localContext);
			Log.i("Suppression dossier", "requete envoyée");
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		}catch(Exception e){
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		
		
		String result;
		
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF8"),30);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
			Log.i("deleteFolder result", result);
            
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteFile(String fileId){
		InputStream is = null;
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("fileId", fileId));
		
		
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLdeletefile);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			Log.i("suppression fichier", fileId);
			HttpResponse response = httpclient.execute(httppost, localContext);
			Log.i("Suppression", "requete envoyée");
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		}catch(Exception e){
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		
		
		String result;
		
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF8"),30);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
			Log.i("deleteFile result", result);
            
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public static void deletePdfFile(){
		InputStream is;
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLconfirmPDF);
			HttpResponse response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		}catch(Exception e){
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		
		
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
		Writer writer = null;
		try {
			
			writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader read = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = read.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return writer.toString();
	}
	
	
	public static String compilRequest(String fileId){
		Log.i("CompilRequest", "begin");
		InputStream is = null;
		String result = "";
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("root_file_id", fileId));

		try{
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLcompil);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			HttpResponse response = httpclient.execute(httppost, localContext);
			
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		}catch(Exception e){
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF8"),30);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
			Log.i("Compil result", result);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return result;
	}
	
	public static InputStream pdfRequest(){
		InputStream is = null;
		String result = "";

		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLpdf);
			HttpResponse response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		}catch(Exception e){
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF8"),30);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			result=sb.toString();
			Log.i("PDF result", result + " ");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return is;
	}

	
	public static void syncFile(HashMap<String, String> map){
		InputStream is = null;
		String result = "";
		
		JSONObject jo = new JSONObject(map);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		// Encodage Base64
		String s = jo.toString();
		/*byte[] bytes = s.getBytes();
		byte[] b = Base64.encode(bytes, Base64.DEFAULT);*/
		
		nameValuePairs.add(new BasicNameValuePair("files", s));
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLsync);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			System.out.println("executing request " + httppost.getURI());
			Log.i("HashMap en JSON", jo.toString());
			HttpResponse response = httpclient.execute(httppost, localContext); //HttpHostConnectException
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
	
	public static String signIn(CharSequence firstName, CharSequence lastName, CharSequence userName, CharSequence mail, CharSequence password, CharSequence password_conf, CharSequence address, String gender, 
			CharSequence city, CharSequence country, CharSequence zip, CharSequence year, String month, CharSequence day){

		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username", userName.toString()));
		nameValuePairs.add(new BasicNameValuePair("email", mail.toString()));
		nameValuePairs.add(new BasicNameValuePair("password", password.toString()));
		nameValuePairs.add(new BasicNameValuePair("password_conf", password_conf.toString()));

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
			return result;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	public static void deleteProject(int idProject){
		InputStream is = null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("projectId", Integer.toString(idProject)));
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLdeleteProject);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			System.out.println("executing request " + httppost.getURI() + " - Delete project id:" + idProject );
			HttpResponse response = httpclient.execute(httppost, localContext);
			System.out.println("delete project executed");
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
			Log.i("deleteProject return", result);

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
	
	public static void createFolder(String name, String parentId, String projectId){
		InputStream is = null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("fileName", name));
		nameValuePairs.add(new BasicNameValuePair("parentId", parentId));
		nameValuePairs.add(new BasicNameValuePair("projectId", projectId));
		
		Log.i("projectId", projectId);
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLnewfolder);
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
			Log.i("createFolder return", result);

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
	
	public static void logOut(){
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("android", "true"));
		
		InputStream is = null;
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URLlogout);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			System.out.println("executing request " + httppost.getURI());
			
			HttpResponse response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
			cookieStore.clear();
			
			for(Cookie ck : cookieStore.getCookies()){
				Log.i("cookie apres deco", ck.toString());
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
			Log.i("logOut return", result);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
