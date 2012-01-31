<?php
class editionActions extends Actions {
	public function indexSuccess() {
		$this->title = 'Edition';
		$this->addCss('edition');
		$this->addJs('arbre');

		if(User::isLogged()){
			$this->flash = 'Votre inscription a été prise en compte'; 
			$this->addJs('edit_area/edit_area_full');
			$this->addJs('langManager');
			$this->addJs('init');
			
			$user = User::getCurrentUser();
			
			/* mockup */
			$tree=new Tree("Workspace");
			
			
			$id=$tree->addNode("Un dossier_1",$tree->getRoot()->getId(),true);
			$tree->addNode("Un Fichier_1_1",$id,false);
			$fichier20=$tree->addNode("Un Fichier_1_2 ",$id,false);
			
			
			$id2=$tree->addNode("Un dossier_2",$tree->getRoot()->getId(),true);
			$tree->addNode("Un Fichier_2_1",$id2,false);
			$tree->addNode("Un Fichier_2_2 ",$id2,false);
			$id3=$tree->addNode("Un dossier_2_1",$id2,true);
			$tree->addNode("Un Fichier_2_1_1",$id3,false);
			$tree->addNode("Un Fichier_2_1_2 ",$id3,false);
			
			$this->tree = $tree;
			
			$_SESSION['tree']=serialize($this->tree);
			
			/* Fin mockup */
			
			$this->setTemplate('edition', $this);
		}
	}
	
	public function testSuccess(){
		echo 'plop';
	}
}

new editionActions();

?>
