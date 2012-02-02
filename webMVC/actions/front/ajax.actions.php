<?php

class ajaxActions extends Actions {
	function __construct() {
		if(!User::isLogged())
			$this->redirect('/');
	
		parent::__construct();
	}
	
	public function getFileSuccess(){
		$fileId = intval($_POST['fileId']);
		$file = new File($_POST['fileId']);
		
		if(!$file->id)
			die('Erreur sur l\'id du fichier');
		
		// creation de la socket de reception
		$receiver=new ReceiverTextOnly(HTTP_IP);

		//creation de la requete d'emission
		$sender= new Sender(FRONTAL_IP, FRONTAL_PORT);

		$dataUrl=explode(':', $file->serverUrl);
		$dataIp=$dataUrl[0];
		$dataPort=$dataUrl[1];
		
		$requete=array(
			'label'=>'getFile',
			'path'=>$_SESSION['workingCopyDir'][$file->project_id],
			'servDataIp'=>$dataIp,
			'servDataPort'=>$dataPort,
			'httpPort'=>$receiver->getPort(),
		);
		$sender->setRequest($requete);

		//envoie de la commande de chargement de fichier
		$sender->sendRequest();
		unset($sender);

		// Attente de recuperation des infos
		$trame=$receiver->getReturn();

		// chargement du projet dans la BDD
		new Query('select',"select id from file where id=".$file->id);

		echo "recuperation du fichier termine";
	}
}

new ajaxActions();

?>
