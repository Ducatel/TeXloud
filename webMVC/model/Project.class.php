<?php
class Project extends object {		
	public $id, $name, $server_url, $ugroup_id, $repo;
		
	public function __construct($id=0) {
		parent::__construct($id);
	}
	
	public function getFiles(){
		$q = new Query('select', 'SELECT id FROM file WHERE project_id = ' . $this->id . ' ORDER BY parent');
		$files = array();
		
		foreach($q->result as $f)
			$files[] = new File($f->id);
		
		return $files;
	}

	public function deleteFiles(){
		$q = new Query('delete', 'DELETE FROM files WHERE project_id = ' . $this->id);

		return $q->result;
	}

	public function _delete(){
		$this->deleteFiles();
		self::delete($this->id);
	}

	public static function delete($id){
		$project=new Project($id);

		if(!$project->id)
			die('pas de projet correspondant');

		if(!$_SESSION['workingCopyDir'][$project->id]){
			Common::getProject($project);
		}
		
		//creation de la requete d'emission
		$sender= new Sender(FRONTAL_IP,FRONTAL_PORT);

		$dataUrl=explode(':',$project->server_url);
		$dataIp=$dataUrl[0];
		$dataPort=$dataUrl[1];
		
		$requete=array(
			'label'=>'deleteProject',
			'path'=>$_SESSION['workingCopyDir'][$project->id],
			'servDataIp'=>$dataIp,
			'servDataPort'=>$dataPort,
			'httpPort'=>'',
		);

		var_dump($requete);
		
		$sender->setRequest($requete);

		//envoie de la commande de suppression de projet
		$sender->sendRequest();
		unset($sender);

		unset($_SESSION['workingCopyDir'][$project->id]);

		parent::delete($project->id);

		echo "Suppression du projet termine";			
	}
}
?>
