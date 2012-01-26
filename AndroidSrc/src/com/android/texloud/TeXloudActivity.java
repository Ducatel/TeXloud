package com.android.texloud;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.login);


		forgot = (Button) (findViewById(R.id.tv_forgot));

		login = (EditText)(findViewById(R.id.EditText_id));
		passwd = (EditText)(findViewById(R.id.EditText_passwd));
		
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

		connect = (Button) (findViewById(R.id.button_connect));
		connect.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				connect_dialog = ProgressDialog.show(TeXloudActivity.this, "Authentification", "Authentification en cours...", true);
				
				new Thread(new Runnable(){

					public void run() {
						Comm c = new Comm();
						Comm.statement st;

						st = c.getAuth(login.getText(),passwd.getText());
						//st = Comm.statement.SUCCESS;
						Message msg = null;
						switch(st){
						case SUCCESS:
							Log.i("switch", "success");
							Intent intent = new Intent(TeXloudActivity.this, MainActivity.class);
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


		sign_in = (Button) (findViewById(R.id.button_inscription));
		sign_in.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				signin_dialog = new Dialog(TeXloudActivity.this, R.style.noBorder);
				signin_dialog.setContentView(R.layout.signindialog);

				Spinner s = (Spinner) signin_dialog.findViewById(R.id.spinner);
				ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
						TeXloudActivity.this, R.array.months, android.R.layout.simple_spinner_item);

				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				s.setAdapter(adapter);

				signin_dialog.show();
			}
		});
		
		if(isOnline())
			Log.i("status", "online");
		else
			Log.i("status", "offline");

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
