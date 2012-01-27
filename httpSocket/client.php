<?php
include('Sender.php');

echo "Debut<br/>";
$frontalAddress = "127.0.0.1";
$frontalPort = 12800;

$requete=array(
	'label'=>'create',
	'username'=>'UnNomDUtilisateur',
	'projectName'=>'UnNomDeProjet',
);

// Méthode 1
$sender= new Sender($frontalAddress,$frontalPort);
$sender->setRequest($requete);
$sender->sendRequest();
unset($sender);
echo "Fin";
// Méthode 2
/*
$sender= new Sender($frontalAddress,$frontalPort);
$sender->setRequest($requete);
$sender->connectSocket();
$sender->sendRequest();
$sender->closeSocket();*/


?>
