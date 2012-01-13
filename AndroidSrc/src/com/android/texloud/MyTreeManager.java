package com.android.texloud;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MyTreeManager {

	private Node root;
	private ArrayList<Node> tree;
	private Activity act;
	private int current_id = 1;
	private final int PADDING_GAP = 22;
	private Dialog folder_dialog;
	private ListView listview_folderdialog;

	private final String[] folderDialogItems = {"Add File...", "Add Folder...", "Rename Folder", "Delete Folder" };
	
	protected class Node extends View {
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

	public MyTreeManager(Activity act, String root_name){
		this.act = act;
		root = new Node(act, root_name, Node.ROOT, current_id++, 5);
		tree = new ArrayList<Node>();
		tree.add(root);
	}

	public void addNode(String node_name, String parent_name, int type){
		tree.add(new Node(act, node_name, getNodeId(parent_name), type, current_id++, (getNode(getNodeId(parent_name)).getPadding())+PADDING_GAP)); // AJOUTER PADDING
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

	
	public void popFolderClickDialog(){
		folder_dialog = new Dialog(act, R.style.noBorder);
		folder_dialog.setContentView(R.layout.folderdialoglayout);
				
		listview_folderdialog = (ListView) (folder_dialog.findViewById(R.id.listview_folderdialog));
		listview_folderdialog.setAdapter(new ArrayAdapter<String>(act, R.layout.itemlayout, R.id.folder_list_content,folderDialogItems));
		
		folder_dialog.show();
	}
	
	public void printTree(){
		tree = sortList();
		LinearLayout layout_arbo;
		layout_arbo = (LinearLayout) (act.findViewById(R.id.layout_arbo));

		View v;
		TextView tv;

		for(Node n : tree){
			System.out.println(n.getParentId() + " " + n.getName() + " " + n.getNodeId());
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
						Log.i("LongClick", "clic root");
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
						popFolderClickDialog();
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

				v.setOnLongClickListener(new OnLongClickListener() {

					public boolean onLongClick(View v) {
						Log.i("LongClick", "clic fichier");
						return false;
					}
				});

				layout_arbo.addView(v);
				break;

			default: Log.e("Error Tree Manager", "Erreur printTree");
			}
		}
	}
}
