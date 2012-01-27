<?php
include_once('Node.php');

/**
 * 
 * Class qui definie un Arbre
 * @author David Ducatel, Zakaria Bouchakor
 *
 */
class Tree{

	private $root;
	private $arrayOfNode;
	
	public function __construct($rootName){
		$this->root=new Node($rootName, 0,true);
		$this->arrayOfNode=array();
		$this->arrayOfNode[$this->root->getId()]=$this->root;
	}
	
	
	/**
	* Ajoute le noeud a l'arbre
	* @param $nodeName: nom du noeud
	* @param $nodeParentId: id du parent du noeud a ajouter
	* @param Boolean $folder: repertoire ou non
	* @return: id du node si ajouter, -1 sinon
	*/
	public function addNode($nodeName,$nodeParentId,$folder){
		
		if(is_bool($folder)){
			
			$find=false;
			foreach($this->arrayOfNode as $possibleParentNode){
				if($possibleParentNode->getId()==$nodeParentId && $possibleParentNode->isFolder()){
					$find=true;
					break;
				}
			}
			
			// Le parent n'existe pas, ou n'est pas un dossier
			if(!$find)
				return -1;
		
			$node=new Node($nodeName,$nodeParentId,$folder);
			$this->arrayOfNode[$node->getId()]=$node;
			
			return $node->getId();
		}
		else
			return -1;
	}

	/**
	 * Supprime un noeud de l'arbre
	 * @param $id: id du noeud supprimer
	 * @return: true si supprimer, false sinon
	 */
	public function removeNode($id){
		
		if($id != $this->root->getId()){
			foreach($this->arrayOfNode as $node){
				if($node->getId()==$id){
					if($node->isFolder())
						$this->removeFolder($node);
					else 
						$this->removeFile($node);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Méthode qui supprime un dossier
	 * @param $folder: objet Node qui représente le dossier a supprimer
	 */
	private function removeFolder($folder){
		$tabSon=array();
		foreach($this->arrayOfNode as $node){
			if($node->getParentId()==$folder->getId()){
				if($node->isFolder()){
					array_push($tabSon,$node);
				}
				else{
					$this->removeFile($node);
				}
			}
		}
		
		foreach($tabSon as $son)
			$this->removeFolder($son);
		unset($this->arrayOfNode[$folder->getId()]);
	}
	
	/**
	 * méthode qui supprime un fichier
	 * @param $folder: objet Node qui représente le fichier a supprimer
	 */
	private function removeFile($file){
		unset($this->arrayOfNode[$file->getId()]);
	}
	
	public function toString(){
		$s="Root: <br/>";
		$s.=$this->root->toString();
		
		$s.="<br/><br/>Other:<br/>";
		foreach($this->arrayOfNode as $node){
			if($this->root!=$node)
			$s.=$node->toString()."<br/><br/>";
		}
		return $s;
	}
	
	public function toStringHTML(){
		$s="<ul>";
		
		$s.=$this->_toStringHTML($this->root,0);
		
		$s.="</ul>";
		return $s;
	}

	private function _toStringHTML($currentNode,$dec){
		if($currentNode->getId()== $this->root->getId())
			$s="<li class='root' style='margin-left:".$dec."px;'>".$currentNode->getName()."</li>";
		else
			$s="<li class='folder' style='margin-left:".$dec."px;'>".$currentNode->getName()."</li>";
		
		$dec+=20;
		
		foreach($this->arrayOfNode as $node){
			if($currentNode->getId() == $node->getParentId()){
			 if($node->isFolder())
			 	$s.=$this->_toStringHTML($node,$dec);
			 else
			  $s.="<li class='file' style='margin-left:".$dec."px;' >".$node->getName()."</li>";		 	
			}
		}
		return $s;
	}
	
	/************************************************/
	/*					 GET & SET				    */
	/************************************************/
	
	public function getRoot(){
		return $this->root;
	}
	

}

?>
