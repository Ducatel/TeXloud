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
	private final int PADDING_GAP = 13;
	
	protected class Node{
		private String name;
		private int parent_id;
		private int padding;
		
		public int type; // FOLDER, LEAF;
		
		protected static final int FOLDER = 0;
		protected static final int LEAF = 1;
		private int id;
		
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
		for(Node n : tree)
			if(n.name == parent_name)
				return n.id;
		
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
		for(Node j : tree){
			if(j.parent_id == v.getId()){
				View tmp = (View) (act.findViewById(j.getId()));
				if(tmp.getVisibility() == View.VISIBLE){
					Log.i("1","1");
					tmp.setVisibility(View.GONE);
				}
				else if(tmp.getVisibility() == View.GONE){
					Log.i("0","0");
					tmp.setVisibility(View.VISIBLE);
				}
				
			}
				
		}
	}
	
	public void printTree(){
		LinearLayout layout_arbo;
		layout_arbo = (LinearLayout) (act.findViewById(R.id.layout_arbo));
		
		View v;
		TextView tv;
		
		for(Node n : tree){
			switch(n.type){
				case Node.FOLDER:
					v = LayoutInflater.from(act).inflate(R.layout.folderlayout, null);
					v.setId(n.getId());
					tv = (TextView) (v.findViewById(R.id.tv_fold));
					tv.setText(n.getName());
					v.setPadding(n.getPadding(), 0, 0, 0);
					
					v.setOnTouchListener(new OnTouchListener(){

						public boolean onTouch(View arg0, MotionEvent arg1) {
							
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
