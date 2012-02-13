package com.android.texloud;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.texloud.MyTreeManager.Node;

public class MainActivity extends Activity implements ScrollViewListener{
	/** Called when the activity is first created. */

	private EditText main_text;
	private String text;
	private MyScrollView sv1, sv2;
	private LinearLayout layout_lineCount;
	private Button deconnect, save_file, compil, create, sync;

	private Dialog dialog_fileSaved, dialog_createProject;

	private MyTreeManager mtm;
	private ProgressDialog loading_dialog, compil_loading, sync_dialog;


	/*---------------TEST BASE 64---------------*/

	//private String pdf64="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";

	/* ----------------------------------------*/

	private static final int LOAD_OK = 0; // Chargement fichier OK
	private static final int LOAD_ERR = 1;
	private static final int COMP_OK = 2; // Compilation OK
	private static final int COMP_ERR = 3; // Erreur lors de la compilation
	private static final int PDF_OK = 4; // Reception pdf ok
	private static final int PDF_ERR = 5; // Erreur reception pdf
	private static final int SYNC_OK = 6; // Synchro
	private static final int SYNC_ERR = 7;
	private static final int SYNC = 8;

	protected Mode currentMode;
	protected enum Mode{ONLINE, OFFLINE};

	private String loaded_file = "";
	private String tree_json;

	private HashMap<String,String> projectsList;

	private int nb_projects;
	private String current_fileId = ""; 
	private String currentProject;

	private Spinner project_spinner;
	private String [] projectsStringArray;
	private HashMap<String, String> modifiedFiles;

	private String lastText = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);

		modifiedFiles = new HashMap<String,String>();
		projectsList = new HashMap<String, String>();

		Bundle b = getIntent().getExtras();
		boolean bl = b.getBoolean("isOnline");
		tree_json = b.getString("tree");

		try {
			traitementJson();
		} catch (JSONException e) {e.printStackTrace();}

		currentMode = (bl) ? Mode.ONLINE : Mode.OFFLINE;

		layout_lineCount = (LinearLayout) (findViewById(R.id.layout_lineCount));
		sv1 = (MyScrollView) (findViewById(R.id.scroll1));
		sv2 = (MyScrollView) (findViewById(R.id.scroll2));


		compil = (Button) (findViewById(R.id.bouton_compil));

		compil.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				Log.i("compil1", "compil1");
				if(current_fileId != ""){
					Log.i("compil2", "compil2");
					compil(current_fileId);
					//testPDF();
				}
			}
		});

		deconnect = (Button) (findViewById(R.id.deco));

		deconnect.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Comm.logOut();
				Intent intent = new Intent(MainActivity.this, TeXloudActivity.class);
				startActivity(intent);
				finish();
			}
		});


		save_file = (Button) (findViewById(R.id.bouton_dl));
		save_file.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				saveFile(main_text.getText().toString(), loaded_file);
			}

		});


		create = (Button) (findViewById(R.id.bouton_create_project));
		create.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				//Log.i("projet", "creation projet");
				popDialogProject();
			}

		});

		sv1.setScrollViewListener(this);
		sv1.setVerticalScrollBarEnabled(false);
		sv2.setScrollViewListener(this);

		main_text = (EditText) (findViewById(R.id.main_editText));
		main_text.addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable s) {

				String str = "";

				for(int i=0; i<main_text.getText().length(); i++){
					int x = (int)(main_text.getText().charAt(i));

					if(x != 65279){
						Log.i("text", Integer.toString((int)(main_text.getText().charAt(i))));
						str += main_text.getText().charAt(i);
					}
				}
				
				str.trim();
				modifiedFiles.put(current_fileId, str);	
				ImageView iv = (ImageView) (findViewById(R.id.sync_icon));
				iv.setImageResource(R.drawable.annuler);
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {

			}

		});

		sync = (Button) (findViewById(R.id.bouton_sync));
		sync.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				sync();
			}
		});


		updateLineCount(0);
	}

	/*public void testPDF(){
		Log.i("testPDF", "testPDF");
		byte[] decoded = Base64.decode(pdf64, Base64.DEFAULT);
		String s = new String(decoded);
		Log.i("Base64 decoded", s);

		File f = new File("/sdcard/TeXloudDocs/cdc.pdf");
		OutputStream fos;
		try {
			fos = new FileOutputStream(f);
			fos.write(decoded);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		File file = new File("/sdcard/TeXloudDocs/cdc.pdf");
		intent.setDataAndType(Uri.fromFile(file), "application/pdf");// "http://192.168.0.2/android/getPdf"
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		try {
			startActivity(intent);
		}
		catch (ActivityNotFoundException e) {
			Toast.makeText(MainActivity.this, 
					"No Application Available to View PDF", 
					Toast.LENGTH_SHORT).show();
		}


	}*/

	public HashMap<String, String> getProjectsList() {
		return projectsList;
	}

	public void sync(){
		sync_dialog = ProgressDialog.show(MainActivity.this, "Synchronisation", "Synchro en cours...", true);

		new Thread(new Runnable(){

			public void run() {
				Message msg = mHandler.obtainMessage(SYNC);
				mHandler.sendMessage(msg);
				Comm.syncFile(modifiedFiles);
				modifiedFiles.clear();


				msg = mHandler.obtainMessage(SYNC_OK);
				mHandler.sendMessage(msg);
			}

		}).start();

	}

	public void popDialogProject(){
		dialog_createProject = new Dialog(this, R.style.noBorder);
		dialog_createProject.setContentView(R.layout.newprojectlayout);

		Button ok = (Button) (dialog_createProject.findViewById(R.id.button_ok_newproject));
		ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				EditText editText = (EditText) (dialog_createProject.findViewById(R.id.et_newproject));
				Comm.createProject(editText.getText().toString());
				dialog_createProject.dismiss();
				main_text.setText("");
				current_fileId = null;

			}
		});

		Button cancel = (Button)(dialog_createProject.findViewById(R.id.button_cancel_newproject));
		cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog_createProject.dismiss();
			}
		});
		dialog_createProject.show();
	}

	public void compil(String fileId){
		//Log.i("compil2b", "compil2b");

		compil_loading = ProgressDialog.show(MainActivity.this, "Compilation", "Compilation en cours...", true);

		new Thread(new Runnable(){
			public void run(){
				Message msg;
				String url = Comm.compilRequest(current_fileId);
				Log.i("url reçue : ", url);
				msg = mHandler.obtainMessage(COMP_OK);
				mHandler.sendMessage(msg);

				InputStream is = Comm.pdfRequest();

				//Log.i("compil3", "compil3");
				/*try {
					traitementPDF(is);
				} catch (IOException e1) {e1.printStackTrace();}*/

				try {
					traitementPDF(url);
				} catch (IOException e) {e.printStackTrace();}

				msg = mHandler.obtainMessage(PDF_OK);
				mHandler.sendMessage(msg);
			}
		}).start();



	}

	/*public void traitementPDF(String url) throws IOException{

		byte[] buffer = new byte[16384];

		Log.i("Chargement de l'url ", "http://" + url);
		URL u = new URL("http://192.168.0.2/images/cahierDesCharges.pdf");

		URLConnection uc = u.openConnection();

		FileOutputStream fos2 = new FileOutputStream("/sdcard/TeXloudDocs/cdc.pdf");
		InputStream myInput = uc.getInputStream();
		//Log.i("InputStream", readInputStream(myInput));

		int l = myInput.read(buffer);
		while(l>0){
			fos2.write(buffer, 0, l);
			l = myInput.read(buffer);
		}
		fos2.flush();
		fos2.close();


		//Ouverture du fichier pdf

		Intent intent = new Intent(Intent.ACTION_VIEW);
		File file = new File("/sdcard/TeXloudDocs/cdc.pdf");
		intent.setDataAndType(Uri.fromFile(file), "application/pdf");// "http://192.168.0.2/android/getPdf"
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		try {
			startActivity(intent);
		}
		catch (ActivityNotFoundException e) {
			Toast.makeText(MainActivity.this, 
					"No Application Available to View PDF", 
					Toast.LENGTH_SHORT).show();
		}
		Log.i("compil6", "compil6");
	}*/


	public void traitementPDF(String url) throws IOException{
		//Log.i("compil4", "compil4");


		byte[] buffer = new byte[16384];

		/* Methode 1*/
		/*int bytesRead;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//Log.i("is.toString()", ":" + is.toString());
		while ((bytesRead = is.read(buffer)) != -1)
		{
		baos.write(buffer, 0, bytesRead);
		}*/


		/* Base 64*/
		/*Base64 b64;
		String result = "";

		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF8"),30);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		result=sb.toString();*/


		/* Methode 2*/
		/*File someFile = new File("/sdcard/TeXloudDocs/cdc.pdf"); 
		OutputStream fos;
		fos = new FileOutputStream(someFile);

		int l;
		while((l = is.read(buffer)) > 0)
			fos.write(buffer, 0, l);

		//fos.flush(); 
		fos.close();
		is.close();*/

		String correctUrl = "http://";

		for(int i = 1; i<url.length(); i++){
			correctUrl += url.charAt(i);
		}


		Log.i("compil5", "compil5");




		/*Cahier des charges par URL directe*/

		//URL u = new URL("http://192.168.0.2/pdf/4f352df8bfe7f.pdf");
		//URL u = new URL("http://192.168.0.2/pdf/4f3529376f506.pdf");
		URL u = new URL(correctUrl);
		//URL u = new URL("http://192.168.0.2/android/getPdf");

		URLConnection uc = u.openConnection();

		FileOutputStream fos2 = new FileOutputStream("/sdcard/TeXloudDocs/cdc.pdf");
		//FileWriter fw = new FileWriter("/sdcard/TeXloudDocs/cdc.pdf");
		//PrintWriter pw = new PrintWriter(fw);
		InputStream myInput = uc.getInputStream();
		//Log.i("InputStream", readInputStream(myInput));

		int l = myInput.read(buffer);
		Log.i("l", Integer.toString(l));
		while(l>0){
			fos2.write(buffer, 0, l);
			l = myInput.read(buffer);
		}
		fos2.flush();
		fos2.close();

		/*while(l>0){
			pw.print(myInput.read(buffer));
			l = myInput.read(buffer);
			Log.i("coucou", "coucou");
		}

		pw.flush();
		pw.close();*/

		/*Ouverture du fichier pdf*/

		Intent intent = new Intent(Intent.ACTION_VIEW);
		File file = new File("/sdcard/TeXloudDocs/cdc.pdf");
		intent.setDataAndType(Uri.fromFile(file), "application/pdf");// "http://192.168.0.2/android/getPdf"
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		try {
			startActivity(intent);
		}
		catch (ActivityNotFoundException e) {
			Toast.makeText(MainActivity.this, 
					"No Application Available to View PDF", 
					Toast.LENGTH_SHORT).show();
		}
		Log.i("compil6", "compil6");

	}

	public String readInputStream(InputStream is){
		StringBuilder sb = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF8"),30);
			sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public void setText(String s){
		text = s;
	}

	public void updateText(){
		main_text.setText(text);
	}

	public void updateLineCount(int nbLines){
		TextView v;

		for(int i=1; i<2000; i++){
			v = (TextView) (LayoutInflater.from(this).inflate(R.layout.linenumberlayout, null));
			v.setText(i + "");
			layout_lineCount.addView(v);
		}
	}

	public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
		if(scrollView == sv1) {
			sv2.scrollTo(x, y);
		} else if(scrollView == sv2) {
			sv1.scrollTo(x, y);
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public void fileClicked(String fileName, String fileId){
		loading_dialog = ProgressDialog.show(this, "Chargement", "Chargement du fichier " + fileName + "...", true);
		current_fileId = fileId;

		new Thread(new Runnable(){

			public void run() {
				String result = Comm.getFile(current_fileId);

				Message msg = mHandler.obtainMessage(LOAD_OK, result);
				mHandler.sendMessage(msg);
			}

		}).start();
	}

	public void saveFile(String file_content, String file_name){

		if(loaded_file != ""){
			File file = new File(Environment.getExternalStorageDirectory() + "/TeXloudDocs", file_name);

			try {
				file.createNewFile();
				FileWriter filewriter = new FileWriter(file,false);
				filewriter.write(file_content);
				filewriter.close();
				popDialogFileSaved();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void popDialogFileSaved(){
		dialog_fileSaved = new Dialog(this, R.style.noBorder);
		dialog_fileSaved.setContentView(R.layout.filesaveddialoglayout);
		Button b = (Button) (dialog_fileSaved.findViewById(R.id.button_ok_filesaved));
		dialog_fileSaved.show();

		b.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dismissDialogFileSaved();
			}
		});

	}

	public void traitementJson() throws JSONException{
		mtm = new MyTreeManager(this, "Workspace");
		JSONArray jo = new JSONArray(tree_json);
		nb_projects = jo.length();
		//Log.i("nb_project", Integer.toString(nb_projects));
		//Log.i("json1", jo.toString());

		ArrayList<JSONObject> projects = new ArrayList<JSONObject>();


		for(int i=0; i<nb_projects; i++){
			projects.add(jo.getJSONObject(i));
			projectsList.put(projects.get(i).getString("projectName"), projects.get(i).getString("projectId"));
		}

		selectProject(projects.get(0).getString("projectName"));
		updateProjectSpinner();
	}

	public void updateProjectSpinner(){
		project_spinner = (Spinner) (findViewById(R.id.project_spinner));

		projectsStringArray = new String[projectsList.size()];

		/*for(int i=0; i<projectsList.size(); i++){
			projectsStringArray[i] = projectsList.get(i);
		}*/

		int i=0;
		for(String s : projectsList.keySet()){
			projectsStringArray[i] = s;
			i++;
		}

		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, projectsStringArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		project_spinner.setAdapter(adapter);
		//TextView tv = (TextView) (project_spinner.findViewById(android.R.id.text1));

		/*tv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("test", "test");
			}
		});*/

		project_spinner.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				updateJSONTree();
				return false;
			}
		});

		project_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				try {
					selectProject(projectsStringArray[position]);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			public void onNothingSelected(AdapterView<?> parentView) {

			}

		});
	}

	public void updateJSONTree(){
		tree_json = Comm.getJSON();
		try {
			traitementJson();
		} catch (JSONException e) {e.printStackTrace();}
	}

	public String getCurrentProject(){
		return currentProject;
	}

	public void selectProject(String name_project) throws JSONException{
		currentProject = name_project;
		mtm.setTree(new ArrayList<Node>());
		//Log.i("select", name_project);

		JSONArray jo = new JSONArray(tree_json);
		ArrayList<JSONObject> projects = new ArrayList<JSONObject>();

		for(int i=0; i<nb_projects; i++){
			projects.add(jo.getJSONObject(i));
		}


		for(JSONObject project : projects){
			//Log.i(name_project, project.getString("projectName"));


			if(project.getString("projectName").trim().equals(name_project.trim())){
				//Log.i("coucou", "test");
				mtm = new MyTreeManager(this, project.getString("projectName"));
				//mtm.getNode(0).setName(project.getString("projectName"));
				JSONArray files = new JSONArray(project.getString("files"));

				for(int i=0; i<files.length(); i++){
					JSONObject object = files.getJSONObject(i);
					//Log.i("file", object.getString("filename"));


					if(object.getInt("is_dir") == 0){
						mtm.addNode(object.getString("filename"), Integer.parseInt(object.getString("parentId")), Node.LEAF, Integer.parseInt(object.getString("id_file")));
					}
					else{
						mtm.addNode(object.getString("filename"), Integer.parseInt(object.getString("parentId")), Node.FOLDER, Integer.parseInt(object.getString("id_file")));
					}
				}
			}
		}

		mtm.updateTree();
	}

	public void dismissDialogFileSaved(){
		dialog_fileSaved.dismiss();
	}

	final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			switch(msg.what){
			case LOAD_OK:
				setText((String)(msg.obj));
				updateText();
				ImageView iv = (ImageView) (findViewById(R.id.sync_icon));
				iv.setImageResource(R.drawable.valider);
				loading_dialog.dismiss();
				break;

			case LOAD_ERR:
				break;


			case COMP_OK:
				compil_loading.dismiss();
				compil_loading = ProgressDialog.show(MainActivity.this, "Récupération PDF", "Récupération PDF en cours...", true);
				break;


			case PDF_OK:
				compil_loading.dismiss();
				break;

			case SYNC:

				break;

			case SYNC_OK:
				sync_dialog.dismiss();
				ImageView iv2 = (ImageView) (findViewById(R.id.sync_icon));
				iv2.setImageResource(R.drawable.valider);
				break;
			}
		}
	};



}