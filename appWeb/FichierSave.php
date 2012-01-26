<?php 

//require_once("Fichier.php");

$log=  $_POST['fichier'];
$contenu= $_POST['contenu'];

var_dump($_POST);
//$log=end($log);
//$f = dirname(__FILE__) . '/log/fichier'.$log.'.txt';
$f = 'fichier'.$log.'.txt';

$handle = fopen($f,"w");

// regarde si le fichier est accessible en écriture
if (is_writable($f)) {
// Ecriture
    if (fwrite($handle, $contenu) === FALSE) {
      echo 'Impossible d\'écrire dans le fichier '.$f.'';
      exit;
    }
   
    echo 'Ecriture terminé';
   
    fclose($handle);
                   
}
else {
      echo 'Impossible d\'écrire dans le fichier '.$f.'';
    } 
/*
$Fichier = "fichier97.txt";

$text = "ma chaine de caractères";
$handle = fopen($Fichier,"w");

// regarde si le fichier est accessible en écriture
if (is_writable($Fichier)) {
// Ecriture
    if (fwrite($handle, $text) === FALSE) {
      echo 'Impossible d\'écrire dans le fichier1 '.$Fichier.'';
      exit;
    }
   
    echo 'Ecriture terminé';
   
    fclose($handle);
                   
}
else {
      echo 'Impossible d\'écrire dans le fichier2 '.$Fichier.'';
    } 
*/
/*
$f = fopen($filename_out, "w" );

if (isset($_POST['codelatex'])) {
  $mon_text = $_POST['#codelatex'];
  fwrite($f, $mon_text);
}

fclose($f); */



?>