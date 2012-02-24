<?php

class signupActions extends Actions {
	public function indexSuccess(){
		$this->title= 'Inscription';
		$this->dontDisplayOptions = false;
		$this->addCss('signup');
		$this->setTemplate('signup', $this);
	}
	
	public function registerSuccess(){
		$userVars = $_POST['register'];
		
		if($userVars['username'] && $userVars['pwd'] && $userVars['pwd_check'] && $userVars['mail'] && 
		   $userVars['pwd'] == $userVars['pwd_check'] && $userVars['username']!= $userVars['pwd'] 
		   && Common::isEmail($userVars['mail'])){
			
			$user = new User();
			$user->username = $userVars['username'];
			$user->salt = md5(microtime());
			$user->password = sha1($user->salt . $userVars['pwd']);
			$user->email = $userVars['mail'];
			
			$user->save();
			
			$group = new Ugroup();
			$group->name = 'Groupe de ' . $user->username;
			$group->save();
			
			$permAdmin = Permission::getAll(0, 1, 'type = \'admin\'');
			
			$query = new Query('insert', 'INSERT INTO group_user VALUES(' . $user->id . ', ' . $group->id . ', \'valide\', ' . $permAdmin[0]->id . ')');
			
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
