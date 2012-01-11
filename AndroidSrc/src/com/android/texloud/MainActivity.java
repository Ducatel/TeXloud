package com.android.texloud;

import com.android.texloud.MyTreeManager.Node;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

	private EditText text;
	private TextView lineCount;
	private String default_text;
	private MyScrollView sv1, sv2;
	private View arbo, layout_touch;
	private LinearLayout layout_left, layout_arbo;
	private Button deconnect;
	private SimpleAdapter adapter;
	private ListView myListView;
	
	private float down_X; // Elargissement de la View arborescence
	
	private MyTreeManager mtm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.main);
		
		sv1 = (MyScrollView) (findViewById(R.id.scroll1));
		sv2 = (MyScrollView) (findViewById(R.id.scroll2));
		arbo = (View) (findViewById(R.id.layout_arbo));
		//layout_touch = (View) (findViewById(R.id.layout_touch));
		
		deconnect = (Button) (findViewById(R.id.deco));
		
		deconnect.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, TeXloudActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		sv1.setScrollViewListener(this);
		sv1.setVerticalScrollBarEnabled(false);
		sv2.setScrollViewListener(this);
		
		text = (EditText) (findViewById(R.id.main_editText));
		layout_left = (LinearLayout) (findViewById(R.id.layout_left));
		
		createDefaultText();
		
		text.addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				updateLineCount(text.getLineCount());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				updateLineCount(text.getLineCount());
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				updateLineCount(text.getLineCount());
			}
			
		});
		
		lineCount = (TextView) (findViewById(R.id.layout_lineCount));
		lineCount.setLineSpacing(5, 1);
		lineCount.setText("1");
		
		/*arbo.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				Log.i("coucou", Float.toString(event.getX()));
				
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					down_X = event.getX();
				}
				else if(event.getAction() == MotionEvent.ACTION_MOVE){
					if(event.getX() < down_X && event.getX() > 150) // deplacement vers la gauche
						layout_left.setLayoutParams(new LinearLayout.LayoutParams((int) (event.getX()), layout_left.getHeight()));
					else if(event.getX() > down_X && event.getX() < 500 )
						layout_left.setLayoutParams(new LinearLayout.LayoutParams((int) (event.getX()), layout_left.getHeight()));
				}
				return true;
			}
		});*/
		
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
		mtm.printTree();
	}

	public void createDefaultText(){
		default_text = "";
    	/*default_text = "\\documentclass[a4paper,10pt]{article}\n" +
								"%\\documentclass[a4paper,10pt]{scrartcl}\n\n" +
								
								"\\usepackage[utf8x]{inputenc}\n\n" +
								
								"\\title{}\n" +
								"\\author{}\n" +
								"\\date{}\n\n" +
								
								"\\pdfinfo{%\n"+
								  "/Title    ()\n"+
								  "/Author   ()\n"+
								  	"/Creator  ()\n"+
								  "/Producer ()\n"+
								  "/Subject  ()\n"+
								  "/Keywords ()\n"+
								"}\n\n"+
								
								"\\begin{document}\n"+
								"\\maketitle\n\n"+
								
								"\\end{document}";*/
		
		
		
    	text.setText(default_text);
    }

	public void updateLineCount(int nbLines){
		lineCount.setText("");
		for(int i=1; i<=nbLines; i++){
			lineCount.setText(lineCount.getText()+""+i+"\n");
		}
	}

	public void onScrollChanged(MyScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		if(scrollView == sv1) {
            sv2.scrollTo(x, y);
        } else if(scrollView == sv2) {
            sv1.scrollTo(x, y);
        }
		
	}

	
}