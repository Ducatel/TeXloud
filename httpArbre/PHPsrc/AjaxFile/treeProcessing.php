<?php
/**
 * Page qui permet de gerer les actions Ajax sur l'arbre
 */
session_start();
include_once('./../class/Tree.php');

if(isset($_POST['action']) && !empty($_POST['action'])){

	if($_POST['action']=='addFile'){
		if(!empty($_POST['name']) && !empty($_POST['idParent']))
			echo addFile($_POST['name'], $_POST['idParent']);
	}
	else if($_POST['action']=='addFolder'){
		if(!empty($_POST['name']) && !empty($_POST['idParent']))
			echo addFolder($_POST['name'], $_POST['idParent']);
	}
	else if($_POST['action']=='removeFile'){
		if(!empty($_POST['id']))
			removeFile($_POST['id']);
	}
	else if($_POST['action']=='removeFolder'){
		if(!empty($_POST['id']))
			removeFolder($_POST['id']);
	}
	else if($_POST['action']=='addProject'){
		if(!empty($_POST['name']))
			echo addProject($_POST['name']);
	}
	else if($_POST['action']=='removeProject'){
		if(!empty($_POST['id']))
			removeProject($_POST['id']);
	}
	else if($_POST['action']=='rename'){
		if(!empty($_POST['id']) && !empty($_POST['name']))
			renameElement($_POST['id'], $_POST['name']);
	}
}

/**
 * Permet d'ajouter un fichier
 * @param $name: Nom du nouveau fichier
 * @param $idParent: Id du dossier parent
 * @return: retourne l'identifiant du nouveau fichier
 */
function addFile($name,$idParent){
	$tree=unserialize($_SESSION['tree']);
	
	return $tree->addNode($name,$idParent,False);
}

/**
 * Supprime un fichier
 * @param $id: Identifiant du fichier
 */
function removeFile($id){
	$tree=unserialize($_SESSION['tree']);
	
	$tree->removeNode($id);
}

/**
 * Ajoute un dossier
 * @param $name: Nom du dossier
 * @param $idParent: Identifiant du dossier parent
 */
function addFolder($name,$idParent){
	$tree=unserialize($_SESSION['tree']);
	
	return $tree->addNode($name,$idParent,True);
}

/**
 * Supprime un dossier
 * @param $id: Identifiant du dossier
 */
function removeFolder($id){
	$tree=unserialize($_SESSION['tree']);
	
	$tree->removeNode($id);
}

/**
 * Permet de créer un nouveau projet
 * @param $name: Nom du projet
 * @return: Identifiant du nouveau projet
 */
function addProject($name){
	$tree=unserialize($_SESSION['tree']);
	
}

/**
* Permet de supprimer un projet
* @param $id: Identifiant du projet
*/
function removeProject($id){
	$tree=unserialize($_SESSION['tree']);
	
}

/**
 * Renome un fichier/dossier
 * @param $id: Identifiant de l'element a renommer
 * @param $name: Nouveau nom de l'element
 */
function renameElement($id,$name){
	$tree=unserialize($_SESSION['tree']);
	
	$tree->getNode($id)->setName($name);
}
?>