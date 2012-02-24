<?php
class androidActions extends Actions{

	public function loginSuccess(){
		$login = $_POST['login'];
		$password = $_POST['password'];

		//$login = "meva";
		//$password = "plop";
		$query = new Query('unique_select', "SELECT id, salt, password FROM user WHERE username='$login'");
		$result = $query->result;
		
		if(!($result && sha1($result->salt.$password) == $result->password)){
			echo "ko";
		}
		else{
			$_SESSION['user_id'] = $result->id;

			$arboresence=array();

			// recuperation de tous les project de l'utilisateur
			$getProjects = new Query('select',"select P.id as id, P.name as name from user U,group_user GU, project P where U.username='".$login."' and U.id=GU.user_id and GU.ugroup_id=P.ugroup_id");
			$i=0;

			if(count($getProjects->result) == 0){
				echo "noTree";
				
			}
			else{
			foreach($getProjects->result as $res){
				$arboresence[$i]=array();
				$arboresence[$i]['projectName']=$res->name;
				$arboresence[$i]['projectId']=$res->id;

	
				$getFilesInProject= new Query('select',"select F.* from file F, project P where P.id=".$res->id." and F.project_id=P.id");
				$j=0;
				$arboresence[$i]['files']=array();
				foreach($getFilesInProject->result as $file){
					$tmp=explode('/',$file->path);
					$arboresence[$i]['files'][$j]=array();
					$arboresence[$i]['files'][$j]['parentId']=$file->parent;
					$arboresence[$i]['files'][$j]['filename']=$tmp[count($tmp)-1];
					$arboresence[$i]['files'][$j]['is_dir']=$file->is_dir;
					$arboresence[$i]['files'][$j]['id_file']=$file->id;
					$j++;
				}
				$i++;
			}

			echo json_encode($arboresence);
			}
		}
	}
	
	public function getJsonTreeSuccess(){
			$arboresence=array();

			$user=User::getCurrentUser();
			// recuperation de tous les project de l'utilisateur
			$getProjects = new Query('select',"select P.id as id, P.name as name from user U,group_user GU, project P where U.username='".$user->username."' and U.id=GU.user_id and GU.ugroup_id=P.ugroup_id");
			$i=0;
	
			if(count($getProjects->result) == 0){
				echo "noTree";
				return;
			}
			foreach($getProjects->result as $res){
				$arboresence[$i]=array();
				$arboresence[$i]['projectName']=$res->name;
				$arboresence[$i]['projectId']=$res->id;

	
				$getFilesInProject= new Query('select',"select F.* from file F, project P where P.id=".$res->id." and F.project_id=P.id");
				$j=0;
				$arboresence[$i]['files']=array();
				foreach($getFilesInProject->result as $file){
					$tmp=explode('/',$file->path);
					$arboresence[$i]['files'][$j]=array();
					$arboresence[$i]['files'][$j]['parentId']=$file->parent;
					$arboresence[$i]['files'][$j]['filename']=$tmp[count($tmp)-1];
					$arboresence[$i]['files'][$j]['is_dir']=$file->is_dir;
					$arboresence[$i]['files'][$j]['id_file']=$file->id;
					$j++;
				}
				$i++;
			}

			echo json_encode($arboresence);
	}
	
	public function createAccountSuccess(){
		$firstname=(empty($_POST['firstname']))?'':$_POST['firstname'];
		$lastname=(empty($_POST['lastname']))?'':$_POST['lastname'];
		$gender=(empty($_POST['gender']))?'':$_POST['gender'];
		$date_of_birth=(empty($_POST['date_of_birth']))?'':$_POST['date_of_birth'];
		$address=(empty($_POST['address']))?'':$_POST['address'];
		$zip=(empty($_POST['zip']))?'':$_POST['zip'];
		$city=(empty($_POST['city']))?'':$_POST['city'];	
		$country=(empty($_POST['country']))?'':$_POST['country'];
		
	
		$username = $_POST['username'];
		$password = $_POST['password'];
		$confirm_passwd = $_POST['password_conf'];
		$email = $_POST['email'];
      
	    if(!(isset($username) && !empty($username) && isset($password) && !empty($password) && isset($email) && !empty($email) && isset($confirm_passwd) && !empty($confirm_passwd)))
			die("Tous les champs requis doivent etre remplis !");
	    
		if($username == $password)
			die("Le mot de passe doit etre different du login");
		
		if($confirm_passwd != $password)
			die("La confirmation du mot de passe ne correspond pas");
 
	    // Si c'est bon on regarde dans la base de donnée si le nom de compte est déjà utilisé :
	    $result = new Query('unique_select',"SELECT username FROM user WHERE  username='$username'");
 
	 	if($result->result)
	 		die("Username deja utilise");

		if(!(strlen($password < 60) && strlen($username < 60)))
			die("Username ou mot de passe trop long !");

		
		$user=new User();
		$user->firstname=$firstname;
		$user->lastname=$lastname;
		$user->username=$username;
		$user->gender=$gender;
		$user->date_of_birth=$date_of_birth;
		$user->address=$address;
		$user->zip=$zip;
		$user->city=$city;
		$user->country=$country;
		$user->email=$email;
		$user->salt = md5(microtime());
		$user->password = sha1($user->salt . $password);
		$user->save();
		
		$ugroup=new Ugroup();
		$ugroup->name=$username;
		$ugroup->save();
		
		$permAdmin = Permission::getAll(0, 1, 'type = \'admin\'');
		new Query('insert', 'INSERT INTO group_user VALUES(' . $user->id . ', ' . $ugroup->id . ', \'valide\', ' . $permAdmin[0]->id.')');

		echo "Inscription reussie ! Vous etes maintenant membre de TeXloud.";
	}	
	
		
	public function createProjectSuccess(){

		$projectName=utf8_encode($_POST['projectName']);
		
		if(!isset($projectName) || empty($projectName))
			die("Erreur sur le nom du projet");
			
		$user=User::getCurrentUser();
	
		$groups = $user->getCreatedGroups();
		
		$project = new Project();
		$project->ugroup_id = $groups[0]->id;
		$project->name = $projectName;
		
		
		// creation de la socket de reception
		$receiver=new ReceiverTextOnly(HTTP_IP);
		
		//creation de la requete d'emission
		$frontalAddress = FRONTAL_IP;
		$frontalPort = FRONTAL_PORT;
		$sender= new Sender($frontalAddress,$frontalPort);


		$requete=array(
			'label'=>'create',
			'username'=>$user->username,
			'projectName'=>$projectName,
			'httpPort'=>$receiver->getPort(),
		);
		
		$sender->setRequest($requete);
		
		///envoie de la commande de creation de projet
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

		echo $project->id . '_project';
	}
	
	//TODO a tester
	public function deleteProjectSuccess(){
		$projectId=$_POST['projectId'];

		if(!isset($projectId) || empty($projectId))
			die("Erreur sur l'id du projet");
	
		$user=User::getCurrentUser();

		$project=new Project($projectId);

		if(!$project->id)
			die('pas de projet correspondant');

		if(!$_SESSION['workingCopyDir'][$project->id]){
			Common::getProject($project);
		}

		//creation de la requete d'emission
		$frontalAddress = "192.168.0.6";
		$frontalPort = 12800;
		$sender= new Sender($frontalAddress,$frontalPort);

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
		$sender->setRequest($requete);

		//envoie de la commande de suppression de projet
		$sender->sendRequest();
		unset($sender);

		// suppression du projet dans la BDD
		$project->_delete();

		unset($_SESSION['workingCopyDir'][$project->id]);

		echo "Suppression du projet termine";			
	}
	
	
	public function createTextFileSuccess(){
		$parentId=$_POST['parentId'];
		if(!isset($parentId) || $parentId=="")
			die("Erreur l'id parent est vide ou indefini");
			
		$fileName=$_POST['fileName'];
		if(!isset($fileName) || empty($fileName))
			die("Erreur le nom du fichier est vide ou indefini");
			
		$projectId=$_POST['projectId'];
		if(!isset($projectId) || empty($projectId))
			die("Erreur l'id du projet est vide ou indefini");
			
			
		$file = new File();
		$file->is_dir = 0;
		$file->project_id=$projectId;
		$file->parent=$parentId;
		
		if($parentId==0)
			$file->path=$fileName;	
		else{
			$parent=new File($parentId);
			$file->path=$parent->path."/".$fileName;
		}
		$file->save();	
		
		$request = array('label' => 'sync');
		$files = array();
		$files[] = array('filename' => $file->path, 'content' => '');
		
		$project = new Project($file->project_id);
		$sender= new Sender(FRONTAL_IP, FRONTAL_PORT);
		$request['httpPort'] = '';
		
		if(!$_SESSION['workingCopyDir'][$project->id]){
			$project = new Project($file->project_id);
			Common::getProject($project);
		}
	
	        $request['path'] = $_SESSION['workingCopyDir'][$project->id];
		$request['files'] = $files;
		$data_addr = explode(':', $project->server_url);
		$request['servDataIp'] = $data_addr[0];
		$request['servDataPort'] = $data_addr[1];
		$sender->setRequest($request);

		$sender->sendRequest();
		unset($sender);
		echo "ok";
	}
	

	public function createFolderSuccess(){
		
		$parentId=$_POST['parentId'];
		if(!isset($parentId) || $parentId=="")
			die("Erreur l'id parent est vide ou indefini");
			
		$fileName=$_POST['fileName'];
		if(!isset($fileName) || empty($fileName))
			die("Erreur le nom du fichier est vide ou indefini");
			
		$projectId=$_POST['projectId'];
		if(!isset($projectId) || empty($projectId))
			die("Erreur l'id du projet est vide ou indefini");

		
		$file = new File();
		$file->is_dir = 1;
		$file->project_id = $projectId;
		$file->parent = $parentId;
		
		if($parentId == 0){
			$file->path = $fileName;
		}
		else{
			$parent = new File($parentId);
			$file->path = $parent->path.'/'.$fileName;
		}

		$file->save();

		

		echo "ok";

	}
	
	public function syncSuccess($affichage=true) {
		$data=$_POST['files'];
		
		if(!isset($data) || empty($data))
			die("Erreur sur le chargement du fichier");
			

		$data=json_decode($data,true);
		$user = User::getCurrentUser();
		$request = array('label' => 'sync');
		$files = array();

		if($user){
			$fileId="";
			
			foreach($data as $id => $content){
				if(empty($fileId))
					$fileId=$id;
				$files[] = array('filename' => File::getPath($id), 'content' => $content);
			}
			
			$file = new File($fileId);
			$project = new Project($file->project_id);
			
		
			if(!$_SESSION['workingCopyDir'][$file->project_id]){
				$project = new Project($file->project_id);
				Common::getProject($project);
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
			
			if($affichage)
				echo "ok";
		}
		else
			echo "ERROR";
	}
		
	public function getFileSuccess(){	
		$fileId = $_POST['fileId'];
		
		if(!isset($fileId) && empty($fileId))
			die('Erreur sur l id du fichier');
	
		$file = new File($_POST['fileId']);

		$_SESSION['project_id'] = $file->project_id;
		
		if(!$_SESSION['workingCopyDir'][$file->project_id]){
			$project = new Project($file->project_id);
			Common::getProject($project);
		}
			
		if(!$file->id)
			die('Erreur sur l\'id du fichier');
		
		// creation de la socket de reception
		$receiver=new ReceiverTextOnly(HTTP_IP);

		//creation de la requete d'emission
		$sender= new Sender(FRONTAL_IP, FRONTAL_PORT);

		$requete=array(
			'label'=>'getFile',
			'path'=>$_SESSION['workingCopyDir'][$file->project_id],
			'filename' => $file->path,
			'servDataIp'=>DATA_IP,
			'servDataPort'=>6668,
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

	public function deleteFolderSuccess(){
		
		$fileId = $_POST['fileId'];	

		if(!isset($_POST['childrenJson']) && empty($_POST['childrenJson'])){
			die("Erreur sur le JSON, vide ou unset");
		}

		if(!isset($fileId) && empty($fileId))
			die("Erreur sur l'id du dossier");

		// Suppression côté Data
		File::deleteFromData($fileId);

		$children = json_decode($_POST['childrenJson'], true);

		foreach($children as $id){
			new Query('delete',"delete from file where id=".$id);
			unset($_SESSION['workingCopyDir'][$id]);
		}

		echo "delete folder ok";

	}
		
	//TODO a tester
	public function deleteFileSuccess(){
		
		$fileId=$_POST['fileId'];

		if(!isset($fileId) && empty($fileId))
			die("Erreur sur l'id du fichier");
	
	
		//creation de la requete d'emission
		$frontalAddress = "192.168.0.6";
		$frontalPort = 12800;
		$sender= new Sender($frontalAddress,$frontalPort);

		$file=new File($fileId);
		$dataUrl=explode(':',$file->serverUrl);
		$dataIp=$dataUrl[0];
		$dataPort=$dataUrl[1];
		
		$requete=array(
			'label'=>'deleteFile',
			'path'=>$_SESSION['workingCopyDir'][$file->id],
			'servDataIp'=>$dataIp,
			'servDataPort'=>$dataPort,
			'httpPort'=>$receiver->getPort(),
		);
		$sender->setRequest($requete);

		//envoie de la commande de suppression de file
		$sender->sendRequest();
			
		unset($sender);
	
		// suppression du file dans la BDD
		new Query('delete',"delete from file where id=".$file->id);


		unset($_SESSION['workingCopyDir'][$file->id]);

		echo "Suppression du file termine";
	}	
	
	public function processCompileSuccess(){
		if($_POST['root_file_id']){
			$rootFile = new File($_POST['root_file_id']);
			$project = new Project($rootFile->project_id);
			
			if(!$_SESSION['workingCopyDir'][$project->id]){
				Common::getProject($project);
			}
		
			$receiver=new ReceiverTextWithBinary(HTTP_IP);
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

			$receiver->getReturn($result_log, $pdf);
			
			if($pdf)
				Common::writePdf($pdf, true);		

			$result = array();
			$result['status'] = $pdf?1:0;
			$result['log'] = $result_log;
			$result['url'] = $pdf?HTTP_IP . '/pdf/' . $_SESSION['pdf']:'';

			echo json_encode($result);
		}
	}

	public function deletePdfFileSuccess(){
		$user = User::getCurrentUser();
		
		if($user && $_SESSION['pdf']){
			unlink(PDF_ANDROID_TMP_DIR . $_SESSION['pdf']);
		}	
	}

	public function getPdfSuccess(){
		$user = User::getCurrentUser();
		$path = $_SESSION['pdf'];
			
		if($user && $path && file_exists($path)){
			$size = filesize($path);
			
			header('Content-Type: application/octet-stream');
			header('Content-Transfer-Encoding: base64');
			header('Content-Length: ' . $size);
			header('Content-Disposition: attachement; filename="document.PDF"');
			header('Expires: 0');
			header('Cache-Control: no-cache, must-revalidate');
			header('Pragma: no-cache');

			readfile($path);

			unlink($path);
			unset($_SESSION['pdf']);
			
			exit;
		}
	}
}
new androidActions();

?>
