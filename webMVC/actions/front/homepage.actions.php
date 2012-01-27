<?php

class homepageActions extends Actions {
	public function indexSuccess() {
		$this->title = 'Accueil';
		$this->addCss('homepage');
		
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
