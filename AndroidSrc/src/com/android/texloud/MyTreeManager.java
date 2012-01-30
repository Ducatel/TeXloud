package com.android.texloud;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MyTreeManager{

	private Node root;
	private ArrayList<Node> tree;
	private MainActivity act;
	private int current_id = 1;
	private final int PADDING_GAP = 22;
	private Dialog click_dialog;
	private ListView listview_dialog;
	private View current_italic_view = null;
	
	private Node current_parent;
	private Dialog modifItem_dialog;

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
		
		protected int getType(){
			return type;
		}

		protected int getPadding(){
			return padding;
		}

		protected String getName(){
			return name;
		}
		
		protected void setName(String s){
			this.name = s;
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


	public void popClickDialog(String s, Node n){
		Log.i("ID node", n.getNodeId()+"");
		ArrayList<HashMap<String, String>> listitem;

		listitem = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> map;

		click_dialog = new Dialog(act, R.style.noBorder);

		if(s == "Root"){

			map = new HashMap<String, String>();
			map.put("titre", "Add File");
			map.put("id", n.getNodeId()+"");
			map.put("img", String.valueOf(R.drawable.add_document));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Add Folder");
			map.put("id", n.getNodeId()+"");
			map.put("img", String.valueOf(R.drawable.folder_add2));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Delete Project");
			map.put("id", n.getNodeId()+"");
			map.put("img", String.valueOf(R.drawable.trash_icon));
			listitem.add(map);
			
			SimpleAdapter mSchedule = new SimpleAdapter (act, listitem, R.layout.itemlayout, 
					new String[] {"img", "titre"}, new int[] {R.id.img, R.id.name_item});

			click_dialog.setContentView(R.layout.clickdialoglayout);
			listview_dialog = (ListView) (click_dialog.findViewById(R.id.listview_dialog));
			listview_dialog.setAdapter(mSchedule);
			
			listview_dialog.setOnItemClickListener(new OnItemClickListener(){

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					HashMap<String, String> itemAtPosition = (HashMap<String, String>) listview_dialog.getItemAtPosition(arg2);
					
					if(itemAtPosition.get("titre") == "Add File"){
						Log.i("id", itemAtPosition.get("id"));
						addLeaf(getNode(Integer.parseInt(itemAtPosition.get("id"))));
					}
					else if(itemAtPosition.get("titre") == "Add Folder"){
						Log.i("id", itemAtPosition.get("id"));
						addFolder(getNode(Integer.parseInt(itemAtPosition.get("id"))));
					}
					click_dialog.dismiss();
				}
				
			});
			click_dialog.show();
		}
		else if(s == "Folder"){

			map = new HashMap<String, String>();
			map.put("titre", "Add File");
			map.put("id", n.getNodeId()+"");
			map.put("img", String.valueOf(R.drawable.add_document));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Add Folder");
			map.put("id", n.getNodeId()+"");
			map.put("img", String.valueOf(R.drawable.folder_add2));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Rename Folder");
			map.put("id", n.getNodeId()+"");
			map.put("img", String.valueOf(R.drawable.rename_icon));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Delete Folder");
			map.put("id", n.getNodeId()+"");
			map.put("img", String.valueOf(R.drawable.folder_delete2));
			listitem.add(map);

			SimpleAdapter mSchedule = new SimpleAdapter (act, listitem, R.layout.itemlayout, 
					new String[] {"img", "titre"}, new int[] {R.id.img, R.id.name_item});


			click_dialog.setContentView(R.layout.clickdialoglayout);
			listview_dialog = (ListView) (click_dialog.findViewById(R.id.listview_dialog));
			listview_dialog.setAdapter(mSchedule);
			
			listview_dialog.setOnItemClickListener(new OnItemClickListener(){

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					HashMap<String, String> itemAtPosition = (HashMap<String, String>) listview_dialog.getItemAtPosition(arg2);
					
					if(itemAtPosition.get("titre") == "Add File"){
						Log.i("id", itemAtPosition.get("id"));
						addLeaf(getNode(Integer.parseInt(itemAtPosition.get("id"))));
					}
					else if(itemAtPosition.get("titre") == "Add Folder"){
						Log.i("id", itemAtPosition.get("id"));
						addFolder(getNode(Integer.parseInt(itemAtPosition.get("id"))));
					}
					else if(itemAtPosition.get("titre") == "Rename Folder"){
						renameFolder(getNode(Integer.parseInt(itemAtPosition.get("id"))));
					}
					else if(itemAtPosition.get("titre") == "Delete Folder"){
						deleteFolder(getNode(Integer.parseInt(itemAtPosition.get("id"))));
					}
					click_dialog.dismiss();
				}
				
			});
			
			click_dialog.show();
		}
		else if(s == "Leaf"){

			map = new HashMap<String, String>();
			map.put("titre", "Add File");
			map.put("id", n.getParentId()+"");
			map.put("img", String.valueOf(R.drawable.add_document));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Rename File");
			map.put("id", n.getNodeId()+"");
			map.put("img", String.valueOf(R.drawable.rename_icon));
			listitem.add(map);

			map = new HashMap<String, String>();
			map.put("titre", "Delete File");
			map.put("id", n.getNodeId()+"");
			map.put("img", String.valueOf(R.drawable.document_delete));
			listitem.add(map);

			SimpleAdapter mSchedule = new SimpleAdapter (act, listitem, R.layout.itemlayout, 
					new String[] {"img", "titre"}, new int[] {R.id.img, R.id.name_item});

			click_dialog.setContentView(R.layout.clickdialoglayout);
			listview_dialog = (ListView) (click_dialog.findViewById(R.id.listview_dialog));
			listview_dialog.setAdapter(mSchedule);
			
			listview_dialog.setOnItemClickListener(new OnItemClickListener(){

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					HashMap<String, String> itemAtPosition = (HashMap<String, String>) listview_dialog.getItemAtPosition(arg2);
					
					if(itemAtPosition.get("titre") == "Add File"){
						
						addLeaf(getNode(Integer.parseInt(itemAtPosition.get("id"))));
					}
					else if(itemAtPosition.get("titre") == "Rename File"){
						renameLeaf(getNode(Integer.parseInt(itemAtPosition.get("id"))));
					}
					else if(itemAtPosition.get("titre") == "Delete File"){
						deleteLeaf(getNode(Integer.parseInt(itemAtPosition.get("id"))));
					}
					
					click_dialog.dismiss();
					
				}
				
			});
			
			click_dialog.show();
		}
		else{
			Log.e("popClickDialog", "Error");
		}
	}

	public void addFolder(Node parent){
		current_parent = parent;
		Dialog d = new Dialog(act, R.style.noBorder);
		d.setContentView(R.layout.modifitemdialog);
		TextView tv = (TextView) (d.findViewById(R.id.additem_tv));
		tv.setText("Ajouter un dossier :");
		d.show();
		
		Button b = (Button) (d.findViewById(R.id.button_ok));
		
		
		modifItem_dialog = d;
		b.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				
				// TODO Auto-generated method stub
				EditText text = (EditText) (modifItem_dialog.findViewById(R.id.additem_et));
				String s = text.getText().toString();
				
				addNode(s, current_parent.getName(), Node.FOLDER);
				
				updateTree();
				modifItem_dialog.dismiss();
				
			}
		});
	}
	
	public void addLeaf(Node parent){
		current_parent = parent;
		Dialog d = new Dialog(act, R.style.noBorder);
		d.setContentView(R.layout.modifitemdialog);
		TextView tv = (TextView) (d.findViewById(R.id.additem_tv));
		tv.setText("Ajouter un fichier :");
		d.show();
		
		Button b = (Button) (d.findViewById(R.id.button_ok));
		
		
		modifItem_dialog = d;
		b.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				
				// TODO Auto-generated method stub
				EditText text = (EditText) (modifItem_dialog.findViewById(R.id.additem_et));
				String s = text.getText().toString();
				
				addNode(s, current_parent.getName(), Node.LEAF);
				
				updateTree();
				modifItem_dialog.dismiss();
				
			}
		});
		
	}
	
	public void renameFolder(Node n){
		current_parent = n;
		Dialog d = new Dialog(act, R.style.noBorder);
		d.setContentView(R.layout.modifitemdialog);
		TextView tv = (TextView) (d.findViewById(R.id.additem_tv));
		tv.setText("Renommer le dossier :");
		d.show();
		
		Button b = (Button) (d.findViewById(R.id.button_ok));
		modifItem_dialog = d;
		b.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				
				// TODO Auto-generated method stub
				EditText text = (EditText) (modifItem_dialog.findViewById(R.id.additem_et));
				String s = text.getText().toString();
				current_parent.setName(s);
				
				updateTree();
				modifItem_dialog.dismiss();
				
			}
		});
		
		
	}
	
	public void renameLeaf(Node n){
		current_parent = n;
		Dialog d = new Dialog(act, R.style.noBorder);
		d.setContentView(R.layout.modifitemdialog);
		TextView tv = (TextView) (d.findViewById(R.id.additem_tv));
		tv.setText("Renommer le fichier :");
		d.show();
		
		Button b = (Button) (d.findViewById(R.id.button_ok));
		modifItem_dialog = d;
		b.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				
				EditText text = (EditText) (modifItem_dialog.findViewById(R.id.additem_et));
				String s = text.getText().toString();
				
				current_parent.setName(s);
				
				updateTree();
				modifItem_dialog.dismiss();
				
			}
		});
	}
	
	public void deleteFolder(Node n){
		
		ArrayList<Node> tmp = new ArrayList<Node>();
		
		for(Node node : tree)
			if(hasToBeRemoved(node, n))
				tmp.add(node);
			
		for(Node node : tmp)
			tree.remove(node);
		
		
		tmp.clear();
		tmp = null;
		updateTree();
	}
	
	public boolean hasToBeRemoved(Node n, Node parent){
		Node current = n;
		Node root = tree.get(0);
		while(current != parent && current != root){
			current = getNode(current.getParentId());
		}
		
		if(current == parent)
			return true;
		else
			return false;
	}
	
	public void deleteLeaf(Node n){
		View tmp = (View) (act.findViewById(n.getNodeId()));
		if(tmp == current_italic_view){
			TextView tv = (TextView) (current_italic_view.findViewById(R.id.tv_leaf));
			tv.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
		}
		current_italic_view = null;
		act.setText("");
		act.updateText();
		tree.remove(n);
		updateTree();
	}
	
	public void updateTree(){
		LinearLayout layout_arbo;
		layout_arbo = (LinearLayout) (act.findViewById(R.id.layout_arbo));
		layout_arbo.removeAllViews();
		
		printTree();
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
						Node n = getNode(v.getId());
						popClickDialog("Root", n);
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
						popClickDialog("Folder", getNode(v.getId()));
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
						act.fileClicked(getNode(arg0.getId()).getName());
						changeTextStyle(arg0);
					}
				});

				v.setOnLongClickListener(new OnLongClickListener() {
					public boolean onLongClick(View v) {
						popClickDialog("Leaf", getNode(v.getId()));
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
