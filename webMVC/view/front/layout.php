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
<link rel="icon" type="image/png" href="/images/logoTexloud.png" />
<!--[if IE]><link rel="shortcut icon" type="image/x-icon" href="/images/logoTexloud.ico" /><![endif]-->

<?php
	foreach($var->css as $css)
		echo '<link rel="stylesheet" type="text/css" href="/css/'.$css.'.css" />';
?>

<link rel="stylesheet" type="text/css" href="/css/style.css" />

<script type="text/javascript" src="/js/jquery/jquery.min.js"></script>

<?php 
	foreach($var->js as $js)
		echo '<script type="text/javascript" src="/js/'.$js.'.js"></script>';
?>
</head>

<body>
	<header id="titre_principal">
		<img src='/images/logoTexloud.png' alt='logo TeXloud' id='logoTexloud' />
		<a href="/" id="titretexloud">TeXloud</a>
		<?php if(User::isLogged()): ?>
			<p id="session">
				<span id="username_header"><?php echo ucwords(User::getCurrentUser()->username); ?></span>
				<a href="/user/logout" class="top_link">déconnexion</a>
				<a href="/user/myAccount" class="top_link">mon compte</a>
			</p>
	
			<nav id='menu'>
				<ul>
					<li><a id="refresh_cache">Effacer cache</a></li>
					<li><a id="sync">Synchroniser</a></li>
					<li><a id="compile">Compiler</a></li>
					<li><a href="js/pdfjs/web/viewer.html">Télécharger Projet</a></li>
					<li><a id="show_viewer">Contact</a></li>
				</ul>
			</nav>
		<?php endif; ?>
	</header>
	<div id="bloc_texloud">
	<?php echo $action_content; ?>
	</div>
	<div id="viewer_block"></div>
</body>
