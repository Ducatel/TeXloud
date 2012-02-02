<?php

class treeActions extends Actions {
	function __construct() {
		if(!User::isLogged())
			$this->redirect('/');
		
		parent::__construct();
	}
	
	public function addFileSuccess(){
		$tree=unserialize($_SESSION['tree']);
	
		$file = new File();
		$file->is_dir = 0;
		
		if(strpos($_POST['idParent'], '_project')){
			$proj_splitted = explode('_', $_POST['idParent']);
			$file->project_id = $proj_splitted[0];
			$file->path = $_POST['name'];
			$file->parent = 0;
		}
		else{
			$parent = new File($_POST['idParent']);
			$file->project_id = $parent->project_id;
			$file->parent = $parent->id;
			$file->path = $parent->path . '/' . $_POST['name'];
		}
		
		$file->save();
		
		$id=$tree->addNode($_POST['name'], $_POST['idParent'], false, $file->id);
		
		$_SESSION['tree']=serialize($tree);
		
		echo $id;
	}
	
	public function addFolderSuccess(){
		$tree=unserialize($_SESSION['tree']);
	
		$file = new File();
		$file->is_dir = 1;
		
		if(strpos($_POST['idParent'], '_project')){
			$proj_splitted = explode('_', $_POST['idParent']);
			$file->project_id = $proj_splitted[0];
			$file->path = $_POST['name'];
			$file->parent = 0;
		}
		else{
			$parent = new File($_POST['idParent']);
			$file->project_id = $parent->project_id;
			$file->parent = $parent->id;
			$file->path = $parent->path . '/' . $_POST['name'];
		}
		
		$file->save();
		
		$id=$tree->addNode($_POST['name'], $_POST['idParent'], true, $file->id);
		
		$_SESSION['tree']=serialize($tree);
		
		echo $id;
	}
	
	public function removeFolderSuccess(){
		$tree=unserialize($_SESSION['tree']);
		$tree->removeNode($_POST['id']);
		$_SESSION['tree']=serialize($tree);
	}
	
	public function removeFileSuccess(){
		$tree=unserialize($_SESSION['tree']);
		$tree->removeNode($_POST['id']);
		$_SESSION['tree']=serialize($tree);
	}
	
	public function removeProjectSuccess(){
		$tree=unserialize($_SESSION['tree']);
	}
	
	public function renameElementSuccess(){
		$tree=unserialize($_SESSION['tree']);
		$node = $tree->getNode($_POST['id']);
		$node->setName($_POST['name']);
		
		$_SESSION['tree']=serialize($tree);
	}
	
	public function addProjectSuccess(){
		$tree=unserialize($_SESSION['tree']);
		$groups = User::getCurrentUser()->getCreatedGroups();
		
		$project = new Project();
		$project->ugroup_id = $groups[0]->id;
		$project->name = $_POST['name'];
		$project->save();
		
		$_SESSION['tree']=serialize($tree);
		
		echo $project->id;
	}
	
	public function removeProject(){
		$tree=unserialize($_SESSION['tree']);
		$_SESSION['tree']=serialize($tree);
		echo "pasFait";
	}
}

new treeActions();

?>
