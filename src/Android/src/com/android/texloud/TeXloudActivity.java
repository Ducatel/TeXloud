package com.android.texloud;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class TeXloudActivity extends Activity {

	private EditText login, passwd;
	private Button connect, forgot;
	private static final int MON_DIALOG_ID = 1;
	private Dialog dialog;

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

				

				/*final AlertDialog.Builder adb = new AlertDialog.Builder(TeXloudActivity.this);
				adb.setView(alertDialogView);

				adb.setTitle("Mot de passe oublié");

				adb.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								EditText et = (EditText) alertDialogView
										.findViewById(R.id.email_edittext);

							}
						});

				adb.setNegativeButton("Annuler",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Lorsque l'on cliquera sur annuler on quittera
								// l'application
								adb.setView(null);
							}
						});

				adb.show();*/
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

	}

	public void dismissDialog(){
		dialog.dismiss();
	}

}
