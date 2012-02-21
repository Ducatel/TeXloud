package com.android.texloud;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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

	private EditText main_text; // EditText principal
	private String text; 
	private MyScrollView sv1, sv2; // Scrollview permettant le scroll du champ texte et des 
	private LinearLayout layout_lineCount; // zone de comptage de ligne
	private Button deconnect, save_file, compil, create, sync; 

	private Dialog dialog_fileSaved, dialog_createProject, dialog, dialog_fail_compil;

	private MyTreeManager mtm; // classe gérant la partie arborescence (voir MyTreeManager.java)

	/*
		Boite de dialogue réutilisée chaque fois qu'un affichage de chargement est nécessaire
	 */
	private ProgressDialog current_loading_dialog; 

	/*TextView servant à déclencher le spinner - action nécessaire entre le clic et l'affichage du spinner (rechargement des projets JSON)*/
	/* Problème d'implémentation*/
	//private TextView spinnerTV;


	/*
	 * Declaration des constantes
	 */

	private static final int LOAD_OK = 0; // Chargement fichier OK
	private static final int LOAD_ERR = 1;
	private static final int COMP_OK = 2; // Compilation OK
	private static final int COMP_ERR = 3; // Erreur lors de la compilation
	private static final int PDF_OK = 4; // Reception pdf ok
	private static final int PDF_ERR = 5; // Erreur reception pdf
	private static final int SYNC_OK = 6; // Synchro
	private static final int SYNC_ERR = 7; 
	private static final int SYNC = 8;
	private static final int UPDATE_TREE_OK = 9; 
	private static final int UPDATE_TREE_ERROR = 10;
	private static final int PDF_SAVED = 11;
	private static final int CONNECTION_LOST = 12; // Connexion perdue
	private static final int CONNECTION_FOUND = 13; // QUand la connexion a été retrouvée


	protected boolean isOnline; // acces reseau ou non
	protected boolean itemSelected = false;

	private String loaded_file = "";
	private String tree_json;
	private String log_compil_error = "";

	private HashMap<String,String> projectsList; // HashMap<Nom de projet, ID du projet>

	private int nb_projects;
	private String current_fileId = ""; 
	private String currentProject;

	private Spinner project_spinner;
	private String [] projectsStringArray;
	private HashMap<String, String> modifiedFiles;

	private String pdfName; // variable nécessaire pour l'enregistrement du fichier pdf. Par défaut, le nom du fichier pdf est le même que le nom du fichier en cours d'édition

	private Thread checkConnection;

	private HashMap<String, String> errorLog;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("onCreate", "onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);

		modifiedFiles = new HashMap<String,String>();
		projectsList = new HashMap<String, String>();

		Bundle b = getIntent().getExtras();
		boolean bl = b.getBoolean("isOnline");
		tree_json = b.getString("tree");

		try {
			ArrayList<JSONObject> projects = traitementJson();
			selectProject(projects.get(0).getString("projectName"));
			updateProjectSpinner();
		} catch (JSONException e) {e.printStackTrace();}

		isOnline = (bl) ? true : false;

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

				//TODO à tester

				/*if((int)(main_text.getText().charAt(0)) == 65279){
					str = main_text.getText().delete(0, 1).toString();
				}*/


				for(int i=0; i<main_text.getText().length(); i++){
					int x = (int)(main_text.getText().charAt(i));

					if(x != 65279){
						str += main_text.getText().charAt(i);
					}
				} 




				str.trim();
				Log.i("afterTextChanged", str);
				modifiedFiles.put(current_fileId, str);	
				ImageView iv = (ImageView) (findViewById(R.id.sync_icon));
				iv.setImageResource(R.drawable.annuler);
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

			}

		});

		sync = (Button) (findViewById(R.id.bouton_sync));
		sync.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {sync();}
		});


		checkConnection = new Thread(new Runnable(){

			public void run() {
				while(true){
					if(isOnline() != isOnline){
						isOnline = isOnline();
						Message msg;
						if(!isOnline()){ // Perte de connexion
							msg = mHandler.obtainMessage(CONNECTION_LOST);
						}
						else{
							msg = mHandler.obtainMessage(CONNECTION_FOUND);
						}

						mHandler.sendMessage(msg);
					}

					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {e.printStackTrace();}
				}
			}

		});
		checkConnection.start();

		/*spinnerTV = (TextView) (findViewById(R.id.spinner_tv));
		spinnerTV.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {

				// Création de la ProgressDialog de chargement
				current_loading_dialog =  ProgressDialog.show(MainActivity.this, "Téléchargement", "Téléchargement de l'arborescence...", true);

				// Les actions effectuées pendant le chargement sont lancées dans un Thread
				new Thread(new Runnable(){

					public void run() {
						System.out.println("updt1");
						tree_json = Comm.getJSON();
						Message msg = mHandler.obtainMessage(UPDATE_TREE_OK);
						mHandler.sendMessage(msg);
						System.out.println("updt2");
					}

				}).start();
			}

		});*/

		project_spinner = (Spinner) (findViewById(R.id.project_spinner));
		project_spinner.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == MotionEvent.ACTION_UP){
					// Création de la ProgressDialog de chargement
					current_loading_dialog =  ProgressDialog.show(MainActivity.this, "Téléchargement", "Téléchargement de l'arborescence...", true);

					// Les actions effectuées pendant le chargement sont lancées dans un Thread
					new Thread(new Runnable(){

						public void run() {
							System.out.println("updt1");
							tree_json = Comm.getJSON();
							Message msg = mHandler.obtainMessage(UPDATE_TREE_OK);
							mHandler.sendMessage(msg);
							System.out.println("updt2");
						}

					}).start();
				}

				return false;
			}
		});


		errorLog = new HashMap<String, String>();

		updateLineCount(0);
	}



	@Override
	protected void onPause(){
		//TODO
		//Comm.clearCookie();
		Log.i("onPause", "onPause");
		super.onPause();
	}

	@Override
	protected void onStop(){
		//TODO
		//Comm.clearCookie();
		Log.i("onStop", "onStop");
		super.onStop();
	}

	@Override
	protected void onResume(){
		Log.i("onResume", "onResume");
		super.onResume();
	}

	@Override
	protected void onRestart(){
		Log.i("onRestart", "onRestart");
		super.onRestart();
	}

	public HashMap<String, String> getProjectsList() {
		return projectsList;
	}

	public void sync(){
		current_loading_dialog = ProgressDialog.show(MainActivity.this, "Synchronisation", "Synchro en cours...", true);

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

	public void parseLog(String log){


		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = parser.parse(new InputSource(new StringReader(log)));

			Element message = (Element) doc.getElementsByTagName("message").item(0);
			Element type = (Element) doc.getElementsByTagName("type").item(0);
			Element line = (Element) doc.getElementsByTagName("line").item(0);

			if(message != null)
				errorLog.put("log", message.getTextContent());

			if(type != null)
				errorLog.put("type", type.getTextContent());

			if(line != null)
				errorLog.put("line", line.getTextContent());


		} catch (ParserConfigurationException e) {e.printStackTrace();} 
		catch (SAXException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}


	}



	public void compil(String fileId){

		current_loading_dialog = ProgressDialog.show(MainActivity.this, "Compilation", "Compilation en cours...", true);

		new Thread(new Runnable(){
			public void run(){
				Message msg;
				String compilReturn = Comm.compilRequest(current_fileId);

				String json = "";

				//Eviter le caractere insécable
				for(int i = 1; i<compilReturn.length(); i++){
					json += compilReturn.charAt(i);
				}

				JSONObject jo;
				try {
					Log.i("json", json);
					jo = new JSONObject(json);

					int status = jo.getInt("status");

					switch(status){
					case 0:
						log_compil_error = jo.getString("log");
						Log.i("Log reçu : ", log_compil_error);

						parseLog(log_compil_error);

						msg = mHandler.obtainMessage(COMP_ERR);
						mHandler.sendMessage(msg);
						break;

					case 1:

						log_compil_error = jo.getString("log");
						Log.i("Log reçu : ", log_compil_error);

						parseLog(log_compil_error);

						String url = jo.getString("url");
						Log.i("url reçue : ", url);
						msg = mHandler.obtainMessage(COMP_OK);
						mHandler.sendMessage(msg);
						traitementPDF(url);
						msg = mHandler.obtainMessage(PDF_OK);
						mHandler.sendMessage(msg);
						break;
					}


				} catch (JSONException e1) {e1.printStackTrace();}catch (IOException e) {e.printStackTrace();}

			}
		}).start();
	}

	public void traitementPDF(String url) throws IOException{
		//Log.i("compil4", "compil4");


		byte[] buffer = new byte[1024];


		String correctUrl = "http://";

		for(int i = 0; i<url.length(); i++){
			if((int)(url.charAt(i)) !=  65279){
				correctUrl += url.charAt(i);
				Log.i("url"+i, url.charAt(i) + "");
			}
		}

		Log.i("compil5", "compil5");

		URL u = new URL(correctUrl);


		URLConnection uc = u.openConnection();

		FileOutputStream fos2 = new FileOutputStream("/sdcard/TeXloudDocs/"+pdfName+".pdf");

		Log.i("compil51", "compil51");

		InputStream myInput = uc.getInputStream();

		Log.i("compil52", "compil52");

		int l = myInput.read(buffer);
		Log.i("l", Integer.toString(l));
		while(l>0){
			fos2.write(buffer, 0, l);
			l = myInput.read(buffer);
		}
		fos2.flush();
		fos2.close();

		/*Ouverture du fichier pdf*/


		Message msg = mHandler.obtainMessage(PDF_SAVED);
		mHandler.sendMessage(msg);


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
		if (netInfo != null && netInfo.isConnectedOrConnecting()) { // Test connectivité
			return true;
		}
		return false;
	}

	public void fileClicked(String fileName, String fileId){
		current_loading_dialog = ProgressDialog.show(this, "Chargement", "Chargement du fichier " + fileName + "...", true);
		current_fileId = fileId;
		pdfName = fileName.split(".tex")[0]; // Le nom qu'aura le pdf en cas de compilation (même nom que le fichier .tex)

		new Thread(new Runnable(){

			public void run() {
				String result = Comm.getFile(current_fileId);

				Message msg = mHandler.obtainMessage(LOAD_OK, result);
				mHandler.sendMessage(msg);
			}

		}).start();
	}

	public String getCurrent_fileId() {
		return current_fileId;
	}



	public void saveFile(String file_content, String file_name){

		if(loaded_file != ""){
			File file = new File(Environment.getExternalStorageDirectory() + "/TeXloudDocs", file_name); //TODO ajouter folder

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

	public ArrayList<JSONObject> traitementJson() throws JSONException{
		mtm = new MyTreeManager(this, "Workspace");
		JSONArray jo = new JSONArray(tree_json);
		nb_projects = jo.length();

		//Log.i("nb_project", Integer.toString(nb_projects));
		//Log.i("json1", jo.toString());

		ArrayList<JSONObject> projects = new ArrayList<JSONObject>();

		projectsList.clear();

		for(int i=0; i<nb_projects; i++){
			projects.add(jo.getJSONObject(i));
			//Log.i(projects.get(i).getString("projectName"), projects.get(i).getString("projectId"));
			projectsList.put(projects.get(i).getString("projectName"), projects.get(i).getString("projectId"));
		}

		updateProjectSpinner();

		return projects;

		//selectProject(projects.get(0).getString("projectName"));

	}

	public void updateProjectSpinner(){
		project_spinner = (Spinner) (findViewById(R.id.project_spinner));

		projectsStringArray = new String[projectsList.size()];

		int i=0;
		for(String s : projectsList.keySet()){
			projectsStringArray[i] = s;
			i++;
		}

		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, projectsStringArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		project_spinner.setAdapter(adapter);


		project_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				try {
					Log.i("projectsStringArray", projectsStringArray[position]);
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

		current_loading_dialog = ProgressDialog.show(MainActivity.this, "Mise à jour", "Mise à jour de l'arborescence...", true);

		new Thread(new Runnable(){

			public void run() {
				tree_json = Comm.getJSON();
				Message msg = mHandler.obtainMessage(UPDATE_TREE_OK);
				mHandler.sendMessage(msg);
			}

		}).start();

	}

	public String getCurrentProject(){
		return currentProject;
	}

	public void selectProject(String name_project) throws JSONException{
		currentProject = name_project;
		mtm.setTree(new ArrayList<Node>());
		Log.i("select", name_project);

		JSONArray jo = new JSONArray(tree_json);
		ArrayList<JSONObject> projects = new ArrayList<JSONObject>();

		for(int i=0; i<nb_projects; i++){
			projects.add(jo.getJSONObject(i));
		}


		for(JSONObject project : projects){

			if(project.getString("projectName").trim().equals(name_project.trim())){
				mtm = new MyTreeManager(this, project.getString("projectName"));
				JSONArray files = new JSONArray(project.getString("files"));

				for(int i=0; i<files.length(); i++){
					JSONObject object = files.getJSONObject(i);

					/*Log.i("filename", object.getString("filename"));
					Log.i("parentId", object.getString("parentId"));
					Log.i("idFile", object.getString("id_file"));*/


					if(object.getInt("is_dir") == 0){
						mtm.addNode(object.getString("filename"), Integer.parseInt(object.getString("parentId")), Node.LEAF, Integer.parseInt(object.getString("id_file")));
					}
					else{
						mtm.addNode(object.getString("filename"), Integer.parseInt(object.getString("parentId")), Node.FOLDER, Integer.parseInt(object.getString("id_file")));
					}
				}
			}
		}

		mtm.updateTree(true);
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
				current_loading_dialog.dismiss();
				break;

			case LOAD_ERR:
				break;


			case COMP_OK:
				current_loading_dialog.dismiss();
				current_loading_dialog = ProgressDialog.show(MainActivity.this, "Récupération PDF", "Récupération PDF en cours...", true);
				break;

			case COMP_ERR:
				Log.i("comp_err", "comp_err");
				dialog_fail_compil = new Dialog(MainActivity.this, R.style.noBorder);
				dialog_fail_compil.setContentView(R.layout.compilfaillayout);

				dialog_fail_compil.show();

				Button ok = (Button) (dialog_fail_compil.findViewById(R.id.button_ok_fail_compil));
				ok.setOnClickListener(new OnClickListener(){
					public void onClick(View arg0) {
						dialog_fail_compil.dismiss();
					}
				});

				current_loading_dialog.dismiss();


				TextView tv_type = (TextView) (dialog_fail_compil.findViewById(R.id.failCompil_tv_type));
				TextView tv_log = (TextView) (dialog_fail_compil.findViewById(R.id.failCompil_tv_log));
				TextView tv_line = (TextView) (dialog_fail_compil.findViewById(R.id.failCompil_tv_lineNumber));

				//tv_type.setText(errorLog.get("type"));
				tv_log.setText(errorLog.get("log") + " ");
				tv_line.setText(errorLog.get("line") + " ");

				break;



			case PDF_OK:
				current_loading_dialog.dismiss();
				break;

			case PDF_SAVED:
				dialog = new Dialog(MainActivity.this, R.style.noBorder);
				dialog.setContentView(R.layout.pdfsavedlayout);

				dialog.show();

				TextView pdf_tv = (TextView) (dialog.findViewById(R.id.pdf_tv));
				pdf_tv.setText("PDF sauvegardé : " + "/sdcard/TeXloudDocs/" + pdfName + ".pdf");

				Button button = (Button) dialog.findViewById(R.id.button_ok_pdf);
				button.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						File file = new File("/sdcard/TeXloudDocs/"+pdfName+".pdf");
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
						dialog.dismiss();

					}
				});

				Button cancel = (Button) dialog.findViewById(R.id.button_cancel_pdf);
				cancel.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {
						dialog.dismiss();					
					}
				});


				//TextView tv_type2 = (TextView) (dialog.findViewById(R.id.successCompil_tv_type));
				TextView tv_log2 = (TextView) (dialog.findViewById(R.id.successCompil_tv_log));
				TextView tv_line2 = (TextView) (dialog.findViewById(R.id.successCompil_tv_lineNumber));

				//tv_type2.setText(errorLog.get("type"));
				tv_log2.setText(errorLog.get("log") + " ");
				tv_line2.setText(errorLog.get("line") + " ");


				break;

			case SYNC_OK:
				current_loading_dialog.dismiss();
				ImageView iv2 = (ImageView) (findViewById(R.id.sync_icon));
				iv2.setImageResource(R.drawable.valider);
				break;


			case UPDATE_TREE_OK:
				current_loading_dialog.dismiss();
				try {
					traitementJson();
					project_spinner.setVisibility(View.VISIBLE);
					project_spinner.performClick();
				} catch (JSONException e) {e.printStackTrace();}
				break;

			}
		}

	};
}
