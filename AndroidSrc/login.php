<?php
	$connect	= mysql_connect("sql.olympe-network.com","162812_texloud","texloud");
	$db			= mysql_select_db("162812_texloud",$connect);
	
	$login	= $_REQUEST['login']; //username
	$password	= $_REQUEST['password']; //password
	
	//$login = 'TexUser';
	//$password = 'texloud';
	
	$sql	= 'select id, password, salt from user where username="'.$login.'"';
	
	$req	= mysql_query($sql);
	
	mysql_close();
	
	$res = mysql_fetch_object($req);
	
	if($res && sha1($res->salt.$password)==$res->password){
		echo "ok";
		//print(json_encode(""));
	}
	
?>
