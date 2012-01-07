package com.android.texloud;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyTreeManager {

	private Node root;
	private ArrayList<Node> tree;
	private Activity act;
	private int current_id = 1;
	private final int PADDING_GAP = 16;

	protected class Node{
		private String name;
		private int parent_id;
		private int padding;

		public int type; // FOLDER, LEAF;

		protected static final int FOLDER = 0;
		protected static final int LEAF = 1;
		private int id;
		private boolean open = true;

		private Node(String name, int type, int id, int padding){
			this.name = name;
			this.type = type;
			this.id = id;
			this.padding = padding;
		}

		private Node(String name, int parent, int type, int id, int padding){
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
		protected int getId(){
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
		root = new Node(root_name, Node.FOLDER, current_id++, 5);
		tree = new ArrayList<Node>();
		tree.add(root);
	}

	public void addNode(String node_name, String parent_name, int type){
		tree.add(new Node(node_name, getNodeId(parent_name), type, current_id++, (getNode(getNodeId(parent_name)).getPadding())+PADDING_GAP)); // AJOUTER PADDING
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
				View tmp = (View) (act.findViewById(j.getId()));

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
		
		if(node.type == Node.FOLDER){
			for(Node n : original){
				if(n.getParentId() == node.getId())
					sortListRecursive(sorted, original, n);
			}
		}
		
	}
	
	public void printTree(){
		tree = sortList();
		LinearLayout layout_arbo;
		layout_arbo = (LinearLayout) (act.findViewById(R.id.layout_arbo));

		View v;
		TextView tv;

		for(Node n : tree){
			System.out.println(n.getParentId() + " " + n.getName() + " " + n.getId());
			switch(n.type){
			case Node.FOLDER:
				v = LayoutInflater.from(act).inflate(R.layout.folderlayout, null);
				v.setId(n.getId());
				tv = (TextView) (v.findViewById(R.id.tv_fold));
				tv.setText(n.getName());
				v.setPadding(n.getPadding(), 0, 0, 0);

				v.setOnTouchListener(new OnTouchListener(){

					public boolean onTouch(View arg0, MotionEvent arg1) {
						if(getNode(arg0.getId()).type == Node.FOLDER)
							click(arg0);

						return false;
					}

				});
				layout_arbo.addView(v);
				break;

			case Node.LEAF:
				v = LayoutInflater.from(act).inflate(R.layout.leaflayout, null);
				v.setId(n.getId());
				tv = (TextView) (v.findViewById(R.id.tv_leaf));
				tv.setText(n.getName());
				v.setPadding(n.getPadding(), 0, 0, 0);


				layout_arbo.addView(v);
				break;

			default: Log.e("Error Tree Manager", "Erreur printTree");
			}
		}
	}
}
