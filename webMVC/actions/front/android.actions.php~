<?php
class androidActions extends Actions{

	public function loginSuccess(){

		$login = $_POST['login'];
		$password = $_POST['password'];

		$login='meva';
		$password = 'plop';

		$query = new Query('unique_select', "SELECT id, salt, password FROM user WHERE username='$login'");
		$result = $query->result;
		
		if(!($result && sha1($result->salt.$password) == $result->password)){
			echo "ko";
		}
		else{
			$_SESSION['user_id'] = $result->id;
			$arboresence=array();

			// recuperation de tous les project de l'utilisateur
			$getProjects = new Query('select',"select P.id as id, P.name as name from user U,group_user GU, project P where U.username='".$login."' and U.id=GU.user_id and GU.group_id=P.group_id");
			
			foreach($getProjects->result as $res){
				$arboresence[$res->id]=array();
				$arboresence[$res->id]['projectName']=$res->name;

	
				$getFilesInProject= new Query('select',"select F.* from file F, project P where P.id=".$res->id." and F.project_id=P.id");
				foreach($getFilesInProject->result as $file){
					$tmp=explode('/',$file->path);
					$arboresence[$res->id][$file->id]=array();
					$arboresence[$res->id][$file->id]['parentId']=$file->parent;
					$arboresence[$res->id][$file->id]['filename']=$tmp[count($tmp)-1];
					$arboresence[$res->id][$file->id]['is_dir']=$file->is_dir;
				}
				
			}
			echo json_encode($arboresence);
		}
	}
	
		
	//TODO a tester
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
		$email = $_POST['email'];
      
	    if(!(isset($username) && !empty($username) && isset($password) && !empty($password) && isset($email) && !empty($email)))
			die("Tout les champs doivent etre remplis !");
	    
		if($username == $password)
			die("Le mot de passe doit etre different du login");
		  
	    // Si c'est bon on regarde dans la base de donnée si le nom de compte est déjà utilisé :
	    $result = new Query('unique_select',"SELECT username FROM user WHERE  username='$username'");
 
	 	if($result->result)
	 		die("Username deja utilise");

		if(!(strlen($password < 60) && strlen($username < 60)))
			die("username ou mot de passe trop long !");

		
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

		echo "Inscription reussie ! Vous etes maintenant membre de TeXloud.";
	}	
	
		
	//TODO a tester
	public function createProject(){
		$projectName=$_POST['projectName'];
		
		if(!isset($projetName) && empty($projectName))
			die("Erreur sur le nom du projet");
			
		$user=User::getCurrentUser();
		
		// creation de la socket de reception
		$receiver=new ReceiverTextOnly("192.168.0.2");
		
		//creation de la requete d'emission
		$frontalAddress = "192.168.0.6";
		$frontalPort = 12800;
		$sender= new Sender($frontalAddress,$frontalPort);

		$requete=array(
			'label'=>'create',
			'username'=>$user->username,
			'projectName'=>$projectName,
			'httpPort'=>$receiver->getPort(),
		);
		$sender->setRequest($requete);
		
		//envoie de la commande de creation de projet
		$sender->sendRequest();
		unset($sender);
		
		// Attente de recuperation des infos
		$trame=$receiver->getReturn();
		
		// Creation du projet dans la BDD
		$req=new Query('insert',"INSERT INTO project VALUES ('','".$projectName."','".$trame['serverUrl']."',(select GU.group_id from user U , group_user GU where U.id='".$user->id."' and GU.user_id=U.id limit 0,1))");
		
		
		$_SESSION['workingCopyDir']=array();
		$_SESSION['workingCopyDir'][$req->last_id]=$trame['workingCopyDir'];
		
		echo "Creation du projet termine";
	}
	
	//TODO a tester
	public function getProject(){
		
		$projectId=$_POST['projectId'];
		
		if(!isset($projectId) && empty($projectId))
			die('Erreur dans le chargement de projet ');
			$user=User::getCurrentUser();

		// creation de la socket de reception
		$receiver=new ReceiverTextOnly("192.168.0.2");

		//creation de la requete d'emission
		$frontalAddress = "192.168.0.6";
		$frontalPort = 12800;
		$sender= new Sender($frontalAddress,$frontalPort);

		$project=new Project($projectId);
		$dataUrl=explode(':',$project->serverUrl);
		$dataIp=$dataUrl[0];
		$dataPort=$dataUrl[1];
		
		$requete=array(
			'label'=>'getProject',
			'path'=>$_SESSION['workingCopyDir'][$project->id],
			'servDataIp'=>$dataIp,
			'servDataPort'=>$dataPort,
			'httpPort'=>$receiver->getPort(),
		);
		$sender->setRequest($requete);

		//envoie de la commande de chargement de projet
		$sender->sendRequest();
		unset($sender);

		// Attente de recuperation des infos
		$trame=$receiver->getReturn();

		// chargement du projet dans la BDD
		new Query('select',"select id from project where id=".$project->id);


		unset($_SESSION['workingCopyDir'][$project->id]);

		echo "Suppression du projet termine";
	
	
	}
	
	
		
	//TODO a tester
	public function deleteProject(){
	
		$projectId=$_POST['projectId'];

		if(!isset($projetId) && empty($projectId))
			die("Erreur sur l'id du projet");
	
		$user=User::getCurrentUser();

		// creation de la socket de reception
		$receiver=new ReceiverTextOnly("192.168.0.2");

		//creation de la requete d'emission
		$frontalAddress = "192.168.0.6";
		$frontalPort = 12800;
		$sender= new Sender($frontalAddress,$frontalPort);

		$project=new Project($projectId);
		$dataUrl=explode(':',$project->serverUrl);
		$dataIp=$dataUrl[0];
		$dataPort=$dataUrl[1];
		
		$requete=array(
			'label'=>'deleteProject',
			'path'=>$_SESSION['workingCopyDir'][$project->id],
			'servDataIp'=>$dataIp,
			'servDataPort'=>$dataPort,
			'httpPort'=>$receiver->getPort(),
		);
		$sender->setRequest($requete);

		//envoie de la commande de suppression de projet
		$sender->sendRequest();
		unset($sender);

		// Attente de recuperation des infos
		$trame=$receiver->getReturn();

		// suppression du projet dans la BDD
		new Query('delete',"delete from project where id=".$project->id);


		unset($_SESSION['workingCopyDir'][$project->id]);

		echo "Suppression du projet termine";
			
	}
	
	//TODO a tester
	public function sync {
	
		$currentFile=$_POST['currentFile'];
		
		if(!isset($currentFile) && empty($currentFile))
			die("Erreur sur le chargement du fichier");
			
		$user=User::getCurrentUser();
		
		// creation de la socket de reception
		$receiver=new ReceiverTextOnly("192.168.0.2");
		
		//creation de la requete d'emission
		$frontalAddress = "192.168.0.6";
		$frontalPort = 12800;
		$sender= new Sender($frontalAddress,$frontalPort);
		
		$file=new File($fileId);
		
		$requete=array(
			'label'=>'sync',
			'path'=>$_SESSION['workingCopyDir'][$file->id],
			'currentFile'=>$currentFile,
			'httpPort'=>$receiver->getPort(),
		);
		$sender->setRequest($requete);
		
		//envoie de la commande de syncronisation du fichier
		$sender->sendRequest();
		unset($sender);
		
		// Attente de recuperation des infos
		$trame=$receiver->getReturn();
				
		echo "Syncronisation du fichier termine";
		
	}
	
		
	//TODO a tester
	public function deleteFile(){
	
		$fileId=$_POST['fileId'];

		if(!isset($fileId) && empty($fileId))
			die("Erreur sur l'id du fichier");
	
		$user=User::getCurrentUser();

		// creation de la socket de reception
		$receiver=new ReceiverTextOnly("192.168.0.2");

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

		// Attente de recuperation des infos
		$trame=$receiver->getReturn();

		// suppression du file dans la BDD
		new Query('delete',"delete from file where id=".$file->id);


		unset($_SESSION['workingCopyDir'][$file->id]);

		echo "Suppression du file termine";
			
	
	
	}
	
	
}
new androidActions();

?>
