<?php
/********************************************
*
*	Filename:	index.php
*	Author:		Ahmet Oguz Mermerkaya
*	E-mail:		ahmetmermerkaya@hotmail.com
*	Begin:		Tuesday, Feb 23, 2009  10:21
*
*********************************************/

define("IN_PHP", true);

require_once("common.php");

$rootName = "Root";
$treeElements = $treeManager->getElementList(null, "manageStructure.php");

?>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="keywords"  content="" />
<meta name="description" content="" />
<title>Editable jquery tree with php codes</title>
<link rel="stylesheet" type="text/css" href="js/jquery/plugins/simpleTree/style.css" />
<link rel="stylesheet" type="text/css" href="style.css" />
<script type="text/javascript" src="js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery/plugins/jquery.cookie.js"></script>
<script type="text/javascript" src="js/jquery/plugins/simpleTree/jquery.simple.tree.js"></script>
<script type="text/javascript" src="js/langManager.js" ></script>
<script type="text/javascript" src="js/treeOperations.js"></script>
<script type="text/javascript" src="js/init.js"></script>
</head>
<body>
<div class="contextMenu" id="myMenu1">	
		<li class="addFolder">
			<img src="js/jquery/plugins/simpleTree/images/folder_add.png" /> </li>
		<li class="addDoc"><img src="js/jquery/plugins/simpleTree/images/page_add.png" /> </li>	
		<li class="edit"><img src="js/jquery/plugins/simpleTree/images/folder_edit.png" /> </li>
		<li class="delete"><img src="js/jquery/plugins/simpleTree/images/folder_delete.png" /> </li>
		<li class="expandAll"><img src="js/jquery/plugins/simpleTree/images/expand.png"/> </li>
		<li class="collapseAll"><img src="js/jquery/plugins/simpleTree/images/collapse.png"/> </li>	
</div>
<div class="contextMenu" id="myMenu2">
		<li class="edit"><img src="js/jquery/plugins/simpleTree/images/page_edit.png" /> </li>
		<li class="delete"><img src="js/jquery/plugins/simpleTree/images/page_delete.png" /> </li>
</div>

<div id="wrap">
	<div id="annualWizard">	
			<ul class="simpleTree" id='pdfTree'>		
					<li class="root" id='<?php echo $treeManager->getRootId();  ?>'><span><?php echo $rootName; ?></span>
						<ul><?php echo $treeElements; ?></ul>				
					</li>
			</ul>						
	</div>	
	<div><a href='doc/editable_jquery_tree_with_php_codes_doc_tr.html'>Döküman</a>
		<a href='doc/editable_jquery_tree_with_php_codes_doc_en.html'>Document</a>		
	</div>
</div> 
<div id='processing'></div>
</body>
</html>