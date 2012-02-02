<?php

function getPDO(){
	 try{
		$PARAM_hote='localhost';
		$PARAM_port='3306';
		$PARAM_nom_bd='TeXloud';
		$PARAM_utilisateur='root';
		$PARAM_mot_passe='admin';
		$pdo=new PDO('mysql:host'.$PARAM_hote.';port='.$PARAM_port.';dbname='.$PARAM_nom_bd,$PARAM_utilisateur,$PARAM_mot_passe);
	 }
	 catch(Exception $e){
	 	echo 'Erreur: '.$e->getMessage().'<br/>';
		echo 'Num: '.$e->getCode(); 
	 }
	 return $pdo;
}	 

?>