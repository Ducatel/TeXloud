package com.android.texloud;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MyTreeManager {

	private Node root;
	private ArrayList<Node> tree;
	private MainActivity act;
	private int current_id = 1;
	private final int PADDING_GAP = 22;
	private Dialog click_dialog;
	private ListView listview_dialog;
	private View current_italic_view = null;
	private ProgressDialog loading_dialog;

	private final String[] folderDialogItems = {"Add File", "Add Folder", "Rename Folder", "Delete Folder"};
	private final String[] rootDialogItems = {"Add File", "Add Folder", "Delete Project"};
	private final String[] leafDialogItems = {"Add File", "Rename File", "Delete File"};


	protected class Node extends View{
		private String name;
		private int parent_id;
		private int padding;

		public int type; // FOLDER, LEAF;

		protected static final int ROOT = 0;
		protected static final int FOLDER = 1;
		protected static final int LEAF = 2;
		private int id;
		private boolean open = true;

		private Node(Context context, String name, int type, int id, int padding){
			super(context);
			this.name = name;
			this.type = type;
			this.id = id;
			this.padding = padding;
		}

		private Node(Context context, String name, int parent, int type, int id, int padding){
			super(context);
			this.name = name;
			parent_id = parent;
			this.type = type;
			this.id = id;
			this.padding = padding;
		}

		protected int getPadding(){
			return padding;
		}

		protected String getName(){
			return name;
		}

		protected int getParentId(){
			return parent_id;
		}
		public int getNodeId(){
			return id;
		}

		public boolean isOpen() {
			return open;
		}

		public void setOpen(boolean open) {
			this.open = open;
		}

	}

	public MyTreeManager(MainActivity act, String root_name){
		this.act = act;
		root = new Node(act, root_name, Node.ROOT, current_id++, 5);
		tree = new ArrayList<Node>();
		tree.add(root);
	}

	public void addNode(String node_name, String parent_name, int type){
		tree.add(new Node(act, node_name, getNodeId(parent_name), type, current_id++, (getNode(getNodeId(parent_name)).getPadding())+PADDING_GAP));
	}

	protected int getNodeId(String parent_name){
		for(Node n : tree){
			if(n.name == parent_name)
				return n.id;
		}
		return 0;
	}

	protected Node getNode(int id){
		for(Node n : tree){
			if(n.id == id)
				return n;
		}

		return null;
	}

	public void click(View v){
		Node clicked = getNode(v.getId());

		if(clicked.isOpen())
			clicked.setOpen(false);
		else
			clicked.setOpen(true);	

		for(Node j : tree){

			if(toggleNode(j, clicked)){
				View tmp = (View) (act.findViewById(j.getNodeId()));

				if(!clicked.isOpen())
					tmp.setVisibility(View.GONE);

				else
					tmp.setVisibility(View.VISIBLE);
			}
		}
	}

	public boolean toggleNode(Node toggled, Node root){

		if(getNode(toggled.getParentId()) == root)
			return true;
		else if(toggled == this.root){ // global root
			return false;
		}
		else{
			return toggleNode(getNode(toggled.getParentId()), root);
		}
	}

	public ArrayList<Node> sortList(){
		ArrayList<Node> sorted = new ArrayList<Node>();

		sortListRecursive(sorted, tree, root);

		return sorted;
	}

	public void sortListRecursive(ArrayList<Node> sorted, ArrayList<Node> original, Node node){

		sorted.add(node);

		if(node.type == Node.FOLDER || node.type == Node.ROOT){
			for(Node n : original){
				if(n.getParentId() == node.getNodeId())
					sortListRecursive(sorted, original, n);
			}
		}

	}


	public void popClickDialog(String s){
		ArrayList<HashMap<String, String>> listitem;

		listitem = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> map;

		click_dialog = new Dialog(act, R.style.noBorder);

		if(s == "Root"){

			map = new HashMap<String, String>();
			map.put("titre", "Add File");
			map.put("img", String.valueOf(R.drawable.add_document));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Add Folder");
			map.put("img", String.valueOf(R.drawable.folder_add2));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Delete Project");
			map.put("img", String.valueOf(R.drawable.trash_icon));
			listitem.add(map);

			SimpleAdapter mSchedule = new SimpleAdapter (act, listitem, R.layout.itemlayout, 
					new String[] {"img", "titre"}, new int[] {R.id.img, R.id.name_item});


			click_dialog.setContentView(R.layout.clickdialoglayout);
			listview_dialog = (ListView) (click_dialog.findViewById(R.id.listview_dialog));
			listview_dialog.setAdapter(mSchedule);
			click_dialog.show();
		}
		else if(s == "Folder"){

			map = new HashMap<String, String>();
			map.put("titre", "Add File");
			map.put("img", String.valueOf(R.drawable.add_document));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Add Folder");
			map.put("img", String.valueOf(R.drawable.folder_add2));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Rename Folder");
			map.put("img", String.valueOf(R.drawable.rename_icon));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Delete Folder");
			map.put("img", String.valueOf(R.drawable.folder_delete2));
			listitem.add(map);

			SimpleAdapter mSchedule = new SimpleAdapter (act, listitem, R.layout.itemlayout, 
					new String[] {"img", "titre"}, new int[] {R.id.img, R.id.name_item});


			click_dialog.setContentView(R.layout.clickdialoglayout);
			listview_dialog = (ListView) (click_dialog.findViewById(R.id.listview_dialog));
			listview_dialog.setAdapter(mSchedule);
			click_dialog.show();
		}
		else if(s == "Leaf"){

			map = new HashMap<String, String>();
			map.put("titre", "Add File");
			map.put("img", String.valueOf(R.drawable.add_document));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Rename File");
			map.put("img", String.valueOf(R.drawable.rename_icon));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Delete File");
			map.put("img", String.valueOf(R.drawable.document_delete));
			listitem.add(map);

			SimpleAdapter mSchedule = new SimpleAdapter (act, listitem, R.layout.itemlayout, 
					new String[] {"img", "titre"}, new int[] {R.id.img, R.id.name_item});

			click_dialog.setContentView(R.layout.clickdialoglayout);
			listview_dialog = (ListView) (click_dialog.findViewById(R.id.listview_dialog));
			listview_dialog.setAdapter(mSchedule);
			click_dialog.show();
		}
		else{
			Log.e("popClickDialog", "Error");
		}
	}

	public void printTree(){
		tree = sortList();
		LinearLayout layout_arbo;
		layout_arbo = (LinearLayout) (act.findViewById(R.id.layout_arbo));

		View v;
		TextView tv;

		for(Node n : tree){
			//System.out.println(n.getParentId() + " " + n.getName() + " " + n.getNodeId());
			switch(n.type){

			case Node.ROOT:
				v = LayoutInflater.from(act).inflate(R.layout.rootlayout, null);
				v.setId(n.getNodeId());
				tv = (TextView) (v.findViewById(R.id.tv_fold));
				tv.setText(n.getName());
				v.setPadding(n.getPadding(), 0, 0, 0);

				v.setOnClickListener(new OnClickListener(){
					public void onClick(View arg0) {
						click(arg0);
					}

				});

				v.setOnLongClickListener(new OnLongClickListener() {
					public boolean onLongClick(View v) {
						popClickDialog("Root");
						return false;
					}
				});

				layout_arbo.addView(v);
				break;

			case Node.FOLDER:
				v = LayoutInflater.from(act).inflate(R.layout.folderlayout, null);
				v.setId(n.getNodeId());
				tv = (TextView) (v.findViewById(R.id.tv_fold));
				tv.setText(n.getName());
				v.setPadding(n.getPadding(), 0, 0, 0);

				v.setOnClickListener(new OnClickListener(){
					public void onClick(View arg0) {
						click(arg0);
					}
				});

				v.setOnLongClickListener(new OnLongClickListener() {

					public boolean onLongClick(View v) {
						popClickDialog("Folder");
						return false;
					}
				});

				layout_arbo.addView(v);
				break;

			case Node.LEAF:
				v = LayoutInflater.from(act).inflate(R.layout.leaflayout, null);
				v.setId(n.getNodeId());
				tv = (TextView) (v.findViewById(R.id.tv_leaf));
				tv.setText(n.getName());

				v.setPadding(n.getPadding(), 0, 0, 0);

				v.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						act.fileClicked();
						changeTextStyle(arg0);
					}
				});

				v.setOnLongClickListener(new OnLongClickListener() {
					public boolean onLongClick(View v) {
						popClickDialog("Leaf");
						return false;
					}
				});

				layout_arbo.addView(v);
				break;

			default: Log.e("Error Tree Manager", "Erreur printTree");
			}
		}
	}

	public void getFile(View v){

		
	}

	public void changeTextStyle(View v){

		if(current_italic_view != v){
			TextView tv = (TextView) (v.findViewById(R.id.tv_leaf));
			tv.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);

			if(current_italic_view != null){
				tv = (TextView) (current_italic_view.findViewById(R.id.tv_leaf));
				tv.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
			}

			current_italic_view = v;
		}
	}

	
}
