<?php
class Common{
	public static function isEmail($email){
		return preg_match('#^[\w.-]+@[\w.-]+\.[a-zA-Z]{2,6}$#', $email);
	}

	public static function getProject($project){
		$user=User::getCurrentUser();
		
		$receiver=new ReceiverTextOnly(HTTP_IP);
		
		$frontalAddress = FRONTAL_IP;
		$frontalPort = FRONTAL_PORT;
		$sender= new Sender($frontalAddress,$frontalPort);
		
		$data_addr = explode(':', $project->server_url);
		
		$requete=array(
			'label' => 'getProject',
			'username' => '',
			'password' => '',
			'path' => $project->repo,
			'servDataIp' => $data_addr[0],
			'servDataPort' => $data_addr[1],
			'httpPort' => $receiver->getPort(),
		);

		$sender->setRequest($requete);
		
		//envoie de la commande de creation de projet
		$sender->sendRequest();
		unset($sender);
		
		// Attente de recuperation des infos
		$trame=$receiver->getReturn();
		
		$project->server_url = $receiver->getRemoteIp().':'.$trame->port;
		$project->save();
	
		if(!is_array($_SESSION['workingCopyDir']))
			$_SESSION['workingCopyDir']=array();

		$_SESSION['workingCopyDir'][$project->id]=$trame->workingCopyDir;

		$_SESSION['project_id'] = $project->id;	
	}

	public static function startsWith($target, $match){
		$length = strlen($match);
		return (substr($target, 0, $length)===$needle);
	}
}
