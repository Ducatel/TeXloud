package com.android.texloud;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TeXloudActivity extends Activity {

	private EditText login, passwd;
	private Button connect, forgot, sign_in;
	private Dialog dialog, signin_dialog;

	//private ProgressDialog loading_dialog;
	
	private static final int LOGIN_OK = 0;
	private static final int ERR_LOGIN = 1;
	
	private ProgressDialog connect_dialog;
	private Spinner spinner_month, spinner_gender;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.login);

		// Test connectivité
		testConnectivite();
	}
	
	public void testConnectivite(){
		
		forgot = (Button) (findViewById(R.id.tv_forgot));

		login = (EditText)(findViewById(R.id.EditText_id));
		passwd = (EditText)(findViewById(R.id.EditText_passwd));
		
		
		connect = (Button) (findViewById(R.id.button_connect));
		sign_in = (Button) (findViewById(R.id.button_inscription));
		
		if(isOnline()){
			forgot.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {


					dialog = new Dialog(TeXloudActivity.this, R.style.noBorder);
					dialog.setContentView(R.layout.alertdialog);
					dialog.setTitle("Mot de passe oublié");

					dialog.show();

					Button button = (Button) dialog.findViewById(R.id.button_cancel);
					button.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dismissDialog();
						}
					});
				}
			});

			connect.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					
					connect_dialog = ProgressDialog.show(TeXloudActivity.this, "Authentification", "Authentification en cours...", true);
					
					new Thread(new Runnable(){

						public void run() {
							Comm c = new Comm();
							Comm.statement st = null;
							
							try {
								st = c.getAuth(login.getText(),passwd.getText());
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//st = Comm.statement.SUCCESS;
							
							Message msg = null;
							switch(st){
							case SUCCESS:
								Log.i("switch", "success");
								Intent intent = new Intent(TeXloudActivity.this, MainActivity.class);
								intent.putExtra("isOnline", isOnline());
								intent.putExtra("tree", c.getTree());
								startActivity(intent);
								finish();
								msg = mHandler.obtainMessage(TeXloudActivity.LOGIN_OK);
								
								break;

							case WRONG:
								Log.i("switch", "wrong");
								msg = mHandler.obtainMessage(TeXloudActivity.ERR_LOGIN);
								
								break;

							default:
								Log.i("switch", "error");
								msg = mHandler.obtainMessage(TeXloudActivity.ERR_LOGIN);
								break;
							}
							
							mHandler.sendMessage(msg);
						}
						
					}).start();
					
				}

			});


			
			sign_in.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					signin_dialog = new Dialog(TeXloudActivity.this, R.style.noBorder);
					signin_dialog.setContentView(R.layout.signindialog);

					spinner_month = (Spinner) signin_dialog.findViewById(R.id.spinner);
					ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
							TeXloudActivity.this, R.array.months, android.R.layout.simple_spinner_item);

					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_month.setAdapter(adapter);
					
					spinner_gender = (Spinner) signin_dialog.findViewById(R.id.spinner_gender);
					ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(
							TeXloudActivity.this, R.array.gender, android.R.layout.simple_spinner_item);
					adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_gender.setAdapter(adapter_gender);
					

					Button ok, cancel;
					
					ok = (Button) (signin_dialog.findViewById(R.id.button_ok));
					cancel = (Button) (signin_dialog.findViewById(R.id.button_cancel));
					
					ok.setOnClickListener(new OnClickListener(){
						public void onClick(View arg0) {
							Comm c = new Comm();
							EditText firstName, lastName, userName, mail, password, address, city, country, zip, year, day;
							
							firstName = (EditText) (signin_dialog.findViewById(R.id.firstname));
							lastName = (EditText) (signin_dialog.findViewById(R.id.lastname));
							userName = (EditText) (signin_dialog.findViewById(R.id.username));
							mail = (EditText) (signin_dialog.findViewById(R.id.mail_signin));
							password = (EditText) (signin_dialog.findViewById(R.id.passwd_signin));
							address = (EditText) (signin_dialog.findViewById(R.id.address));
							city = (EditText) (signin_dialog.findViewById(R.id.city));
							country = (EditText) (signin_dialog.findViewById(R.id.country));
							zip = (EditText) (signin_dialog.findViewById(R.id.zip));
							year = (EditText) (signin_dialog.findViewById(R.id.year));
							day = (EditText) (signin_dialog.findViewById(R.id.day));
							
							String month = getMonth(spinner_month.getSelectedItem().toString());
							String gender = spinner_gender.getSelectedItem().toString();
							
							c.signIn(firstName.getText(), lastName.getText(), userName.getText(), mail.getText(), password.getText(), address.getText(), gender,
									city.getText(), country.getText(), zip.getText(), year.getText(), month, day.getText());
							
							signin_dialog.dismiss();
						}
					});
					
					cancel.setOnClickListener(new OnClickListener(){
						public void onClick(View arg0) {
							signin_dialog.dismiss();
						}
					});
					
					signin_dialog.show();
				}
			});
			
		}
		
		// Offline Mode
		else{
			login.setEnabled(false);
			passwd.setEnabled(false);
			TextView tv_id = (TextView)(findViewById(R.id.tv_id));
			tv_id.setTextColor(Color.LTGRAY);
			TextView tv_mdp = (TextView)(findViewById(R.id.tv_passwd));
			tv_mdp.setTextColor(Color.LTGRAY);
			
			/*forgot.setTextColor(Color.LTGRAY);
			sign_in.setTextColor(Color.LTGRAY);
			connect.setTextColor(Color.LTGRAY);*/
			
			forgot.setVisibility(View.GONE);
			sign_in.setVisibility(View.GONE);
			connect.setText("Continuer en mode hors-ligne");
			
			
			connect.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					Log.i("switch", "success");
					Intent intent = new Intent(TeXloudActivity.this, MainActivity.class);
					intent.putExtra("isOnline", isOnline());
					startActivity(intent);
					finish();	
				}
			});
		}
	}

	public String getMonth(String s){
		if(s == "Janvier")
			return "01";
		if(s == "Fevrier")
			return "02";
		if(s == "Mars")
			return "03";
		if(s == "Avril")
			return "04";
		if(s == "Mai")
			return "05";
		if(s == "Juin")
			return "06";
		if(s == "Juillet")
			return "07";
		if(s == "Aout")
			return "08";
		if(s == "Septembre")
			return "09";
		if(s == "Octobre")
			return "10";
		if(s == "Novembre")
			return "11";
		if(s == "Decembre")
			return "12";
		else
			return "00";
		
		
		
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	public void wrongLogin(){
		setErrorTextViewVisibility(View.VISIBLE);
	}
	
	
	public void setErrorTextViewVisibility(int status){
		TextView tv_error = (TextView)(findViewById(R.id.tv_error));
		tv_error.setVisibility(status);
	}

	public void dismissDialog(){
		dialog.dismiss();
	}
	
	public void dismissLoadingDialog(){
		connect_dialog.dismiss();
	}
	
	public static String SHAsum(byte[] convertme) throws NoSuchAlgorithmException{
	    MessageDigest md = MessageDigest.getInstance("SHA-1"); 
	    return byteArray2Hex(md.digest(convertme));
	}

	private static String byteArray2Hex(final byte[] hash) {
	    Formatter formatter = new Formatter();
	    for (byte b : hash) {
	        formatter.format("%02x", b);
	    }
	    return formatter.toString();
	}
	
	
	final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			dismissLoadingDialog();
			switch(msg.what){
			case ERR_LOGIN:
				setErrorTextViewVisibility(View.VISIBLE);
				break;
			
			case LOGIN_OK:
				setErrorTextViewVisibility(View.INVISIBLE);
				break;
			}
		}
	};

	
	
}
