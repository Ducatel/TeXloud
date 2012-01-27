<?php

class signupActions extends Actions {
	public function indexSuccess(){
		$this->title= 'Inscription';
		$this->addCss('signup');
		$this->setTemplate('signup', $this);
	}
	
	public function registerSuccess(){
		$userVars = $_POST['register'];
		
		if($userVars['username'] && $userVars['pwd'] && $userVars['pwd_check'] && $userVars['mail'] && 
		   $userVars['pwd'] == $userVars['pwd_check'] && Common::isEmail($userVars['mail'])){
			
			$user = new User();
			$user->username = $userVars['username'];
			$user->salt = md5(microtime());
			$user->password = sha1($user->salt . $userVars['pwd']);
			$user->email = $userVars['mail'];
			
			$user->save();
			
			$this->flash = 'Votre inscription a été prise en compte';
			
			$this->addCss('homepage');
			$this->setTemplate('homepage.offline', $this);
		}
		else
			$this->redirect('/signup');
	}
}

new signupActions();

?>
