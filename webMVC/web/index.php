<?php
session_start();

define("IN_PHP", true);

require_once('../model/ClassLoader.class.php');
$autoloader = new ClassLoader();

require_once('../actions/front/actions.php');

//verifie si la page est passée en parametre et si le fichier existe
if(!empty($_GET['p'])) {
	//inclut le fichier actions correspondant à la page
	require_once('../actions/front/'.$_GET['p'].'.actions.php');
}
elseif(isset($_SESSION['user_id'])) {
	require_once('../actions/front/edition.actions.php');
}
else {
	require_once('../actions/front/homepage.actions.php');
}
?>
