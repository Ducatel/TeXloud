<?php
//define("IN_PHP", true);
//require_once("common.php");

//$rootName = "Workspace";
//$treeElements = $treeManager->getElementList(null, "manageStructure.php");
?>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />




<?php 
	if(isset($var->title))
		echo '<title>TeXloud - '.$var->title.'</title>';
	else
		echo '<title>TeXloud</title>';
?>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<link rel="stylesheet" type="text/css" href="/css/common.css" />
<link rel="stylesheet" type="text/css" href="/js/edit_area/edit_area.css" />
<link rel="stylesheet" type="text/css" href="/css/style.css" />
<link rel="stylesheet" type="text/css" href="/css/arbre.css" />

<?php
	foreach($var->css as $css)
		echo '<link rel="stylesheet" type="text/css" href="/css/'.$css.'.css" />';
?>

<link rel="stylesheet" type="text/css" href="/css/style.css" />

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>

<?php 
	foreach($var->js as $js)
		echo '<script type="text/javascript" src="/js/'.$js.'.js"></script>';
?>
</head>

<body>
	<header id="titre_principal">
		<a href="/" id="titretexloud">TeXloud</a>
		<?php if(User::isLogged()): ?>
			<p id="session">
				<span id="username_header"><?php echo ucwords(User::getCurrentUser()->username); ?></span>
				<a href="/user/logout" class="top_link">déconnexion</a>
				<a href="/user/myAccount" class="top_link">mon compte</a>
			</p>
		<?php endif; ?>

		<nav id='menu'>
			<ul>
				<li><a href="pdfjs/web/viewer.html" title="compiler">Compiler</a></li>
				<li><a href="#" title="telecharger">Télécharger Projet</a></li>
				<li><a href="#" title="contact">Contact</a></li>
			</ul>

		</nav>
	</header>
	<div id="bloc_texloud">
	<?php echo $action_content; ?>
	</div>
</body>