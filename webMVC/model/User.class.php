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
		
		return false;
	}
	
	public function getProjects(){
		$q = new Query('select', 'SELECT p.id FROM project p, user u, group_user gu, `group` g WHERE u.id = ' . $this->id . ' AND u.id=gu.user_id AND gu.group_id=g.id AND p.group_id=g.id');
		
		$projects = array();
		
		foreach($q->result as $p)
			$projects[] = new Project($p->id);
		
		return $projects;
	}
}
?>
