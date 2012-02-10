<?php
session_start();

define('HTTP_IP', '192.168.0.2');
define('COMPIL_IP', '192.168.0.5');
define('FRONTAL_IP', '192.168.0.6');
define('DATA_IP', '192.168.0.4');
define('FRONTAL_PORT', '12800');
define('PDF_TMP_DIR', '/tmp/texloud/');
define('PDF_ANDROID_TMP_DIR', dirname(__FILE__) . '/pdf/');

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
