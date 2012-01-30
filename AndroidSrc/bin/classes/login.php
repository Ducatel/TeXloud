<?php
	$connect	= mysql_connect("localhost","phpmyadmin","texloud");
	$db			= mysql_select_db("texloud",$connect);
	
	//$login	= $_REQUEST['login']; //username
	//$password	= $_REQUEST['password']; //password
	
	$login = 'TexUser';
	$password = 'texloud';
	
	$sql	= 'select id, password, salt from user where username="'.$login.'"';
	
	$req	= mysql_query($sql);
	
	mysql_close();
	
	//sleep(2);
	
	$res = mysql_fetch_object($req);
	
	if($res && sha1($res->salt.$password)==$res->password){
		echo "ok";
		//print(json_encode(""));
	}
	
?>
