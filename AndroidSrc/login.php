<?php
	/*$connect	= mysql_connect("localhost","phpmyadmin","texloud");
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
	}*/
	
require_once("pdo.php");

$login = $_REQUEST['login'];
$password = $_REQUEST['password'];

//$login='meva';
//$password = 'plop';

$pdo = getPDO();
$req = $pdo->query("select id, password, salt from user where username=".$pdo->quote($login));

$res = $req->fetch(PDO::FETCH_OBJ);

if($res && sha1($res->salt.$password)==$res->password){
	echo "ok";
}

$req->closeCursor();
?>
