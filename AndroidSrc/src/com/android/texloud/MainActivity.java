package com.android.texloud;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
	private ProgressDialog loading_dialog;


	private static final int LOAD_OK = 0;
	private static final int LOAD_ERR = 1;

	protected Mode currentMode;
	protected enum Mode{ONLINE, OFFLINE};

	private String loaded_file = "";
	private String tree_json;

	private HashMap<String,String> projectsList;

	private int nb_projects;
	private String current_fileId, currentProject;
	
	private Spinner project_spinner;
	private String [] projectsStringArray;
	private HashMap<String, String> modifiedFiles;
	
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
				if(loaded_file != "")
					compil(loaded_file);
			}

		});

		deconnect = (Button) (findViewById(R.id.deco));

		deconnect.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
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
				Log.i("projet", "creation projet");
				popDialogProject();
			}
			
		});
		
		sv1.setScrollViewListener(this);
		sv1.setVerticalScrollBarEnabled(false);
		sv2.setScrollViewListener(this);

		main_text = (EditText) (findViewById(R.id.main_editText));
		main_text.addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable arg0) {
				modifiedFiles.put(current_fileId, main_text.getText().toString().trim());		
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}

			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
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
	
	public HashMap<String, String> getProjectsList() {
		return projectsList;
	}

	public void sync(){
		Comm.syncFile(modifiedFiles);
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

	public void compil(String name_file){
		Comm c = new Comm();
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
				Comm c = new Comm();
				String result = c.getFile(current_fileId);

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
		Log.i("nb_project", Integer.toString(nb_projects));
		Log.i("json1", jo.toString());

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
				loading_dialog.dismiss();
				break;

			case LOAD_ERR:
				break;
			}

		}
	};



}