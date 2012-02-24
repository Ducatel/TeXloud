<?php

class homepageActions extends Actions {
	public function indexSuccess() {
		$this->title = 'Accueil';
		$this->addCss('homepage');
		$this->dontDisplayOptions = false;
		
		if(User::isLogged()){
			$this->redirect('/');
		}
		else {
			$this->setTemplate('homepage.offline', $this);
		}
	}
}

new homepageActions();

?>
