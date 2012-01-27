<?php
class userActions extends Actions {	
	public function processLoginSuccess(){
		$values = User::login($_POST['identifiant'], $_POST['motpasse']);

		$this->redirect('/');
	}
	
	public function logoutSuccess() {
		session_destroy();
		$this->redirect('/');
	}
	
	public function myAccountSuccess(){
		$this->user = User::getCurrentUser();
		
		if($this->user){
			$this->title= 'Modifier mon profil';
			$this->addCss('signup');
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
			
			$user->save();
			$this->redirect('/user/myAccount');
		}
		else
			$this->redirect('/user/myAccount');
	}
}

new userActions();

?>
