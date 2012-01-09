package com.android.texloud;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TeXloudActivity extends Activity {

	private EditText login, passwd;
	private Button connect, forgot, sign_in;
	private static final int MON_DIALOG_ID = 1;
	private Dialog dialog, signin_dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.login);

		LayoutInflater factory = LayoutInflater.from(this);

		// Création de l'AlertDialog

		forgot = (Button) (findViewById(R.id.tv_forgot));



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
				Intent intent = new Intent(TeXloudActivity.this,
						MainActivity.class);

				startActivity(intent);
				finish();
			}
		});
		
		sign_in = (Button) (findViewById(R.id.button_inscription));
		sign_in.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				signin_dialog = new Dialog(TeXloudActivity.this, R.style.noBorder);
				signin_dialog.setContentView(R.layout.signindialog);
				
				Spinner s = (Spinner) signin_dialog.findViewById(R.id.spinner);
			    ArrayAdapter adapter = ArrayAdapter.createFromResource(
			            TeXloudActivity.this, R.array.months, android.R.layout.simple_spinner_item);
			    
			    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			    s.setAdapter(adapter);
				
				signin_dialog.show();
			}
		});

	}

	public void dismissDialog(){
		dialog.dismiss();
	}

}
