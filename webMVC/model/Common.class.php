<?php
class Common{
	public static function isEmail($email){
		return preg_match('#^[\w.-]+@[\w.-]+\.[a-zA-Z]{2,6}$#', $email);
	}

	public static function deleteWorkingCopy($project_id, $path){
		$project = new Project($project_id);

		if($project->id){
			$sender = new Sender(FRONTAL_IP, FRONTAL_PORT);
	
			$data_addr = explode(':', $project->server_url);
                    
	                $requete=array(
        	                'label' => 'deleteWorkingCopy',
                	        'username' => '', 
                        	'password' => '', 
	                        'path' => $path,
        	                'servDataIp' => $data_addr[0],
                	        'servDataPort' => $data_addr[1],
                        	'httpPort' => ''
	                ); 	

			$sender->setRequest($requete);
			$sender->sendRequest();
		}
	}

	public static function deleteProject($path, $project_id){
                $project = new Project($project_id);

                if($project->id){
                        $sender = new Sender(FRONTAL_IP, FRONTAL_PORT);
                    
                        $data_addr = explode(':', $project->server_url);

                        $requete=array(
                                'label' => 'deleteProject',
                                'username' => '',
                                'password' => '',
                                'path' => $path,
                                'servDataIp' => $data_addr[0],
                                'servDataPort' => $data_addr[1],
                                'httpPort' => ''
                        );      

                        $sender->setRequest($requete);
                        $sender->sendRequest();
                }   
	}

	public static function getProject($project){
		if(!$project->id)
			die('le projet n\'existe pas');

		$user = User::getCurrentUser();
		
		$receiver = new ReceiverTextOnly(HTTP_IP);
		
		$frontalAddress = FRONTAL_IP;
		$frontalPort = FRONTAL_PORT;
		$sender = new Sender($frontalAddress,$frontalPort);
		
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
		
		if(!$project->server_url){
			$project->server_url = $receiver->getRemoteIp().':'.$trame->port;
			$project->save();
		}
	
		if(!is_array($_SESSION['workingCopyDir']))
			$_SESSION['workingCopyDir']=array();

		$_SESSION['workingCopyDir'][$project->id]=$trame->workingCopyDir;

		$_SESSION['project_id'] = $project->id;	
	}

	public static function endsWith($target, $match){
		return (substr($target, strlen($target) - strlen($match)) === $match);
	}

	public static function startsWith($target, $match){
		$length = strlen($match);
		return (substr($target, 0, $length)===$match);
	}

	public static function writePdf($pdf, $android = false){
		if(!file_exists(PDF_TMP_DIR) || !is_dir(PDF_TMP_DIR))
			mkdir(PDF_TMP_DIR, 0777);

		$filename = uniqid() . '.pdf';
		
		if($android){
			$f = fopen(PDF_ANDROID_TMP_DIR . '/' . $filename, 'w');
			chmod(PDF_ANDROID_TMP_DIR . '/' . $filename, 0777);
		}
		else{
			$f = fopen(PDF_TMP_DIR . '/' . $filename, 'w');
			chmod(PDF_TMP_DIR . '/' . $filename, 0777);
		}
//		fwrite($f, base64_encode($pdf));
		fwrite($f, $pdf);
		fclose($f);

		if($android)
			$_SESSION['pdf'] = $filename;
		else
			$_SESSION['pdf'] = PDF_TMP_DIR . $filename;
	}
}
