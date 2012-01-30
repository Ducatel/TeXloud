<?php
class User extends object {		
	public $id, $firstname, $lastname, $username, $gender, $date_of_birth, $address, $zip, $city, $country, $email, $salt, $password;
		
	public function __construct($id=0) {
		parent::__construct($id);
	}
	
	static function isLogged() {
		if(isset($_SESSION['user_id']) && $_SESSION['user_id'] != null) {
			return new user($_SESSION['user_id']);
		}
	}
	
	static function login($username, $password) {
		$query = new Query('unique_select', "SELECT id, username, salt, password FROM user WHERE username='$username'");
		$result = $query->result;
		
		if($result && sha1($result->salt.$password) == $result->password){
			$_SESSION['user_id'] = $result->id;
			return $result->id;
		}
		else
			return false;
	}
	
	static function getCurrentUser() {
		if(isset($_SESSION['user_id']) && $_SESSION['user_id'] != null) {
			return new user($_SESSION['user_id']);
		}
		else
		return false;
	}
	
	public function getTree(){
		$q = new Query('unique_select', 'SELECT id FROM file WHERE uid = ' . $this->id . ' AND parent = 0');
		
		if($q->result){
			$file = new File($q->result->id);
			
			$tree = '<div id="wrap"><div id="annualWizard"><ul class="simpleTree" id="pdfTree">' . 
					'<li class="root" id="Projets"><span>Projets</span>' . 
					 	'<ul>' . $file->getTree() . '</ul>' .				
					'</li>' .
					'</ul></div></div>';
			
			return $tree;
		}			
			
		return 'root';
	}
}
?>
