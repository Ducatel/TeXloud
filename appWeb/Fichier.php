<?php 

$log=  $_POST['fichier'];

$Fichier = "fichier".$log.".txt";

if (is_file($Fichier)) {
	if ($TabFich = file($Fichier)) {
		for($i = 0; $i < count($TabFich); $i++)
			echo $TabFich[$i];

	}
	else {
	echo "Le fichier ne peut être lu...<br>";

	}
}



/*
$f = fopen($filename_out, "w" );

if (isset($_POST['codelatex'])) {
  $mon_text = $_POST['#codelatex'];
  fwrite($f, $mon_text);
}

fclose($f); */



?>