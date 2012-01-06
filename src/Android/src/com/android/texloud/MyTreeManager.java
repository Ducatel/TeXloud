package com.android.texloud;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class MyTreeManager {
	
	private Node root;
	private ArrayList<Node> tree;
	private Activity act;
	private int current_id = 1;
	
	protected class Node{
		private String name;
		private int parent_id;
		
		public int type; // FOLDER, LEAF;
		
		protected static final int FOLDER = 0;
		protected static final int LEAF = 1;
		private int id;
		
		private Node(String name, int type, int id){
			this.name = name;
			this.type = type;
			this.id = id;
		}
		
		private Node(String name, int parent, int type, int id){
			this.name = name;
			parent_id = parent;
			this.type = type;
			this.id = id;
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
	}
	
	public MyTreeManager(Activity act, String root_name){
		this.act = act;
		root = new Node(root_name, Node.FOLDER, current_id++);
		tree = new ArrayList<Node>();
		tree.add(root);
	}
	
	public void addNode(String node_name, String parent_name, int type){
		tree.add(new Node(node_name, getNodeId(parent_name), type, current_id++));
	}
	
	protected int getNodeId(String parent_name){
		for(Node n : tree)
			if(n.name == parent_name)
				return n.id;
		
		return 0;
	}
	
	public void printTree(){
		LinearLayout layout_arbo;
		layout_arbo = (LinearLayout) (act.findViewById(R.id.layout_arbo));
		
		View v;
		for(Node n : tree){
			switch(n.type){
				case Node.FOLDER:
					v = LayoutInflater.from(act).inflate(R.layout.folderlayout, null);
					v.setId(n.getId());
					layout_arbo.addView(v);
					break;
					
				case Node.LEAF:
					v = LayoutInflater.from(act).inflate(R.layout.leaflayout, null);
					v.setId(n.getId());
					layout_arbo.addView(v);
					break;
					
				default: Log.e("Error Tree Manager", "Erreur printTree");
			}
		}
	}
}
