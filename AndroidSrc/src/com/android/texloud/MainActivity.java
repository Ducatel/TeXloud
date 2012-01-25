package com.android.texloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.android.texloud.MyTreeManager.Node;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements ScrollViewListener{
	/** Called when the activity is first created. */

	private EditText main_text;
	private String text;
	private MyScrollView sv1, sv2;
	private View arbo, layout_touch;
	private LinearLayout layout_left, layout_arbo, layout_lineCount;
	private Button deconnect;
	private SimpleAdapter adapter;
	private ListView myListView;

	private float down_X; // Elargissement de la View arborescence

	private MyTreeManager mtm;
	private ProgressDialog loading_dialog;


	private static final int LOAD_OK = 0;
	private static final int LOAD_ERR = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);


		layout_lineCount = (LinearLayout) (findViewById(R.id.layout_lineCount));
		sv1 = (MyScrollView) (findViewById(R.id.scroll1));
		sv2 = (MyScrollView) (findViewById(R.id.scroll2));
		arbo = (View) (findViewById(R.id.layout_arbo));


		deconnect = (Button) (findViewById(R.id.deco));

		deconnect.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, TeXloudActivity.class);
				startActivity(intent);
				finish();
			}
		});

		sv1.setScrollViewListener(this);

		sv1.setVerticalScrollBarEnabled(false);
		sv2.setScrollViewListener(this);

		main_text = (EditText) (findViewById(R.id.main_editText));
		layout_left = (LinearLayout) (findViewById(R.id.layout_left));



		main_text.addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable s) {
				//updateLineCount(main_text.getLineCount());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				//updateLineCount(main_text.getLineCount());
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				//updateLineCount(main_text.getLineCount());
			}

		});



		/*
		 * Partie Arborescence
		 */

		mtm = new MyTreeManager(this, "Dossier racine");
		mtm.addNode("Dossier1", "Dossier racine", Node.FOLDER);
		mtm.addNode("Dossier2", "Dossier racine", Node.FOLDER);
		mtm.addNode("monFichier.tex", "Dossier1", Node.LEAF);
		mtm.addNode("fichier2.tex", "Dossier1", Node.LEAF);
		mtm.addNode("fichier3.tex", "Dossier2", Node.LEAF);
		mtm.addNode("Dossier3", "Dossier racine", Node.FOLDER);
		mtm.addNode("fichier4.tex", "Dossier racine", Node.LEAF);
		mtm.addNode("Dossier5", "Dossier1", Node.FOLDER);
		mtm.addNode("fichier5.tex", "Dossier5", Node.LEAF);
		mtm.addNode("fichier6.tex", "Dossier3", Node.LEAF);
		mtm.addNode("fichier7.tex", "Dossier3", Node.LEAF);

		mtm.printTree();

		updateLineCount(0);
	}

	public void setText(String s){
		text = s;
	}

	public void updateText(){
		main_text.setText(text);
	}

	public void updateLineCount(int nbLines){
		/*lineCount.setText("");
		for(int i=1; i<=40 + nbLines; i++){
			lineCount.setText(lineCount.getText()+""+i+"\n");
		}*/

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

		//Log.i("scroll", y + " " + y/18 + " ");

		//updateLineCount(y/18);

	}

	public void fileClicked(){
		loading_dialog = ProgressDialog.show(this, "Chargement", "Chargement du fichier...", true);


		new Thread(new Runnable(){

			public void run() {
				Comm c = new Comm();
				String result = c.getFile();

				Message msg = mHandler.obtainMessage(LOAD_OK, result);
				mHandler.sendMessage(msg);
			}

		}).start();
	}

	final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			switch(msg.what){
			case LOAD_OK:
				setText((String)(msg.obj));
				updateText();
				loading_dialog.dismiss();
				break;
			}

		}
	};

}