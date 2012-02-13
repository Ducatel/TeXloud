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
		
		$project = new Project($file->project_id);
		
		if(!$_SESSION['workingCopyDir'][$file->project_id])
			Common::getProject($project);
		
		$id=$tree->addNode($_POST['name'], $_POST['idParent'], false, $file->id);
		
		$_SESSION['tree']=serialize($tree);

		$request = array('label' => 'sync');

		$files = array();	
		$files[] = array('filename' => $file->path, 'content' => '');

		$sender= new Sender(FRONTAL_IP, FRONTAL_PORT);
		
		$data_addr = explode(':', $project->server_url);

		$request['httpPort'] = '';
		$request['path'] = $_SESSION['workingCopyDir'][$_SESSION['project_id']];
//		$request['path'] = '82357dfb1a833466744dd6ce186d7336';
		$request['servDataIp'] = $data_addr[0];
		$request['servDataPort'] = $data_addr[1];
		$request['files'] = $files;

		$sender->setRequest($request);

		$sender->sendRequest();
		unset($sender);

//		echo 'envoyé';	
	
		echo $file->id;
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
		
		$tree->addNode($_POST['name'], $_POST['idParent'], true, $file->id);
		
		$_SESSION['tree']=serialize($tree);
		
		echo $file->id;
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
		
		$id_splitted = explode('_', $_POST['id']);
		$project = new Project($id_splitted[0]);

		if($project){
			$tree->removeNode($_POST['id']);
			$project->_delete();
		}

		$_SESSION['tree']=serialize($tree);
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
		
		$user=User::getCurrentUser();
		
		// creation de la socket de reception
		$receiver=new ReceiverTextOnly(HTTP_IP);
		
		//creation de la requete d'emission
		$frontalAddress = FRONTAL_IP;
		$frontalPort = FRONTAL_PORT;
		$sender= new Sender($frontalAddress,$frontalPort);
		
		$requete=array(
			'label'=>'create',
			'username'=>$user->username,
			'projectName'=>$project->name,
			'httpPort'=>$receiver->getPort(),
		);
		
		$sender->setRequest($requete);
		
		//envoie de la commande de creation de projet
		$sender->sendRequest();
		unset($sender);
		
		// Attente de recuperation des infos
		$trame=$receiver->getReturn();
		
		$project->server_url = $receiver->getRemoteIp().':'.$trame->port;
		$project->repo = $trame->projectName;
		$project->save();
	
		if(!is_array($_SESSION['workingCopyDir']))
			$_SESSION['workingCopyDir']=array();

		$_SESSION['workingCopyDir'][$project->id]=$trame->workingCopyDir;
		
		$_SESSION['project_id'] = $project->id;	
	
	//	echo "Creation du projet termine";
		
		$id = $tree->addNode($project->name, $tree->getRoot()->getId(), true, $project->id . '_project');

		$_SESSION['tree']=serialize($tree);

		echo $id;
	}
	
	public function removeProject(){
		$tree=unserialize($_SESSION['tree']);
		$_SESSION['tree']=serialize($tree);
		echo "pasFait";
	}
}

new treeActions();

?>
