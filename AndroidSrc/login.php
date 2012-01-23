<?php
	$connect	= mysql_connect("localhost","root","ggteam");
	$db			= mysql_select_db("Tetris",$connect);
	
	$login	= $_REQUEST['login']; //username
	$password	= $_REQUEST['password']; //password
	$sql	= 'select password from user where username="'.$login.'"';
	$req	= mysql_query($sql);
	mysql_close();
	
	$fichier=fopen('test.log','w'); 
	fwrite($fichier,print_r($sql,true));
	fclose($fichier);
	
	
	// Retour (true ou false)
	print(json_encode(""));
	
?>