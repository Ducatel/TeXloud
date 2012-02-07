<?php

class ajaxActions extends Actions {
	function __construct() {
		if(!User::isLogged())
			$this->redirect('/');
	
		parent::__construct();
	}

	public function compileSuccess(){
		$user = User::getCurrentUser();

		if($user){
			$this->projects = $user->getProjects();
			$this->setTemplateAlone('compilePopUp', $this);
		}
	}

	public function getFileListForProjectSuccess(){
		if($_GET['id']){
			$this->project = new Project($_GET['id']);
			$this->files = File::getAll(null, null, 'project_id = ' . $this->project->id . ' AND is_dir = 0', 'path DESC');

			$this->setTemplateAlone('_project_file_list', $this);
		}
	}

	public function processCompileSuccess(){
		if($_POST['root_file_id']){
			$rootFile = new File($_POST['root_file_id']);
			$project = new Project($rootFile->project_id);
			
			if(!$_SESSION['workingCopyDir'][$project->id]){
				Common::getProject($project);
			}
			
			$receiver=new ReceiverTextOnly(HTTP_IP);
			$sender= new Sender(FRONTAL_IP, FRONTAL_PORT);
			
			$dataUrl=explode(':', $project->server_url);
			$dataIp=$dataUrl[0];
			$dataPort=$dataUrl[1];
		
			$request = array('label' => 'compile',
					 'path' => $_SESSION['workingCopyDir'][$_SESSION['project_id']],
					 'httpPort' => $receiver->getPort(),
					 'rootFile' => $rootFile->path,
					 'servDataIp' => $dataIp,
					 'servDataPort' => $dataPort);
			
			$sender->setRequest($request);
			$sender->sendRequest();
			unset($sender);

			$trame=$receiver->getReturn(false);
			echo $trame;
		}
	}

	public function syncSuccess(){
		$user = User::getCurrentUser();
		
		$data = $_POST['files'];
		$request = array('label' => 'sync');
		$files = array();
		
		$project = new Project($_SESSION['project_id']);
		
		if($user && $data){
			foreach($data as $id => $content){
				$files[] = array('filename' => File::getPath($id), 'content' => urldecode($content));
//				 $files[File::getPath($id)] = $content;
			}
			
			$sender= new Sender(FRONTAL_IP, FRONTAL_PORT);
			$request['httpPort'] = '';
			$request['path'] = $_SESSION['workingCopyDir'][$_SESSION['project_id']];
			$request['files'] = $files;

			$data_addr = explode(':', $project->server_url);
			$request['servDataIp'] = $data_addr[0];
			$request['servDataPort'] = $data_addr[1];
			$sender->setRequest($request);

			$sender->sendRequest();
			unset($sender);
		}
	}
	
	public function getFileSuccess(){
		$file = new File($_POST['id']);

		$_SESSION['project_id'] = $file->project_id;
		
		$project = new Project($file->project_id);
		
		if(!$_SESSION['workingCopyDir'][$file->project_id]){
			Common::getProject($project);
		}
			
		if(!$file->id)
			die('Erreur sur l\'id du fichier');
		
		// creation de la socket de reception
		$receiver=new ReceiverTextOnly(HTTP_IP);

		//creation de la requete d'emission
		$sender= new Sender(FRONTAL_IP, FRONTAL_PORT);

		$dataUrl=explode(':', $project->server_url);
		$dataIp=$dataUrl[0];
		$dataPort=$dataUrl[1];
	
		$requete=array(
			'label'=>'getFile',
			'path'=>$_SESSION['workingCopyDir'][$file->project_id],
//			'path'=> 'plop',
			'filename' => $file->path,
			'servDataIp'=>$dataIp,
			'servDataPort'=>$dataPort,
			'httpPort'=>$receiver->getPort(),
		);
		$sender->setRequest($requete);
		
		//envoie de la commande de chargement de fichier
		$sender->sendRequest();
		unset($sender);

		// Attente de recuperation des infos
		$trame=$receiver->getReturn(false);

		// chargement du projet dans la BDD
		new Query('select',"select id from file where id=".$file->id);
		
		//echo "recuperation du fichier termine";
		echo $trame;
	}

	public function getProjectSuccess($project){
		if(!$project){
			$project = new Project($_POST['project_id']);
		}

		Common::getProject($project);
	}
}

new ajaxActions();

?>
