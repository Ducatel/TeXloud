<?php
class editionActions extends Actions {
	public function indexSuccess() {
		$this->title = 'Edition';
		$this->addCss('edition');

		if(User::isLogged()){
			$this->flash = 'Votre inscription a été prise en compte'; 
			$this->addJs('edit_area/edit_area_full');
			$this->addJs('jquery/plugins/jquery.cookie');
			$this->addJs('jquery/plugins/simpleTree/jquery.simple.tree');
			$this->addJs('langManager');
			$this->addJs('treeOperations');
			$this->addJs('init');
			
			$user = User::getCurrentUser();
			$this->tree = $user->getTree();
			$this->setTemplate('edition', $this);
		}
	}
	
	public function testSuccess(){
		echo 'plop';
	}
}

new editionActions();

?>
