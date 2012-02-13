<?php
class userActions extends Actions {	
	public function processLoginSuccess(){
		$values = User::login($_POST['identifiant'], $_POST['motpasse']);

		$this->redirect('/');
	}
	
	public function logoutSuccess() {
		foreach($_SESSION['workingCopyDir'] as $project_id => $path)
			Common::deleteWorkingCopy($project_id, $path);
		
		session_destroy();
		
		if(!$_POST['android'])
			$this->redirect('/');
	}
	
	public function myAccountSuccess(){
		$this->user = User::getCurrentUser();
		
		if($this->user){
			$this->title= 'Modifier mon profil';
			$this->addCss('signup');
			$this->addJs('account');

			$this->setTemplate('myAccount', $this);
		}
		else
			$this->redirect('/');
	}
	
	public function processEditAccountSuccess(){
		$userVars = $_POST['editAccount'];

		$user = User::getCurrentUser();
		
		if($user){
			if(trim($userVars['firstname']))
				$user->firstname = $userVars['firstname'];
			if(trim($userVars['lastname']))
				$user->lastname = $userVars['lastname'];
			
			$user->gender = $userVars['gender'];
			$user->date_of_birth = $userVars['birthday'];
			$user->address = ucwords($userVars['address']);
			$user->zip = $userVars['zip'];
			$user->city = ucwords($userVars['city']);
			$user->country = ucwords($userVars['country']);
			
			if($userVars['change_pwd'] && $user->password == sha1($user->salt.$userVars['pwd']) 
			  && $userVars['pwd_check'] == $userVars['pwd_check2'])
			    $user->password=sha1($user->salt.$userVars['pwd_check']);
			   
			    
			
			$user->save();
			$this->redirect('/');
		}
		else
			$this->redirect('/user/myAccount');
	}
}

new userActions();

?>
