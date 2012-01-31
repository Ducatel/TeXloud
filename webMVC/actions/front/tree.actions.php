<?php

class treeActions extends Actions {
	function __construct() {
		if(!User::isLogged())
			$this->redirect('/');
		
		parent::__construct();
	}
	
	public function addFileSuccess(){
		$tree=unserialize($_SESSION['tree']);
	
		$id=$tree->addNode($name,$idParent,False);
		
		$_SESSION['tree']=serialize($tree);
		
		return $id;
	}
	
	public function addFolderSuccess(){
		$tree=unserialize($_SESSION['tree']);
		$id=$tree->addNode($_POST['name'],$_POST['idParent'],True);
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
		
		$tree->getNode($_POST['id'])->setName($name);
		$_SESSION['tree']=serialize($tree);
	}
	
	public function addProjectSuccess(){
		$tree=unserialize($_SESSION['tree']);
		
		$_SESSION['tree']=serialize($tree);
		echo "pasFait";
	}
	
	public function removeProject(){
		$tree=unserialize($_SESSION['tree']);
		$_SESSION['tree']=serialize($tree);
		echo "pasFait";
	}
}

new treeActions();

?>
