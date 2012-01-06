<?php		

	
	    //ini_set('session.gc_maxlifetime', 3); 
	    session_start();
	    $session_timeout=300;
	    if(!isset($_SESSION['last_access']) || !isset($_SESSION['ipaddr']))
		{
		  $_SESSION=array();
		  session_destroy();
		  header("Location: Acceuil.html");
		  die();
		}

		if(time()-$_SESSION['last_access']>$session_timeout)
		{
		  $_SESSION=array();
		  session_destroy();
		  header("Location: Acceuil.html");
		  die();
		}
		if($_SERVER['REMOTE_ADDR']!=$_SESSION['ipaddr'])
		{
		  $_SESSION=array();
		  session_destroy();
		  header("Location: Acceuil.html");
		  die();
		}
		$_SESSION['last_access']=time(); 
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
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
	<meta name="keywords"  content="" />
	<meta name="description" content="" />


	<link rel="stylesheet" href="Css/texloud2.css" />
	<link rel="stylesheet" href="js/jquery/plugins/simpleTree/style.css" />
	<link rel="stylesheet" href="Css/styletexloud.css" />
	<script src="js/jquery/jquery.min.js"></script>
	<script src="js/jquery/plugins/jquery.cookie.js"></script>
	<script src="js/jquery/plugins/simpleTree/jquery.simple.tree.js"></script>
	<script src="js/langManager.js" ></script>
	<script src="js/treeOperations.js"></script>
	<script src="js/init.js"></script>
     
	<script src="jquery-linedtextarea/jquery-linedtextarea.js"></script>
	<script src="texloud.js"></script>
	<link href="jquery-linedtextarea/jquery-linedtextarea.css" type="text/css" rel="stylesheet" />
        <title>TeXloud</title>
    </head>
    
  <body>

        <div id="bloc_texloud">
	     
	     <header id="titre_principal">
                            
                    <h2 id="titretexloud">TeXloud</h2>
		    <p id="session" style="font-size:0.8em ;">
		     <?php echo $_SESSION['identifiant']; ?>|
		     <a style="font-size:0.8em ;" href="deconnexion.php">Déconnexion</a>|
		    <a style="font-size:0.8em ;" href="ModifCompte.php">Compte</a>
		    </p>

                <nav id='menu'>
                    <ul>
                        <li><a href="pdfjs/web/viewer.html" title="compiler">Compiler</a></li>
                        <li><a href="#" title="telecharger">Télécharger Projet</a></li>
                        <li><a href="#" title="contact">Contact</a></li>
                    </ul>
		
                </nav>
	     
            </header>
            
        
               <section>
               
                <div id="projet" class='coteprojet'>
		      		 
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
				
			</div> 
			<div id='processing'></div>
            
		</div>
            
           
	 <div id="editeur">

	   
		  <textarea class="lined" rows="38" cols="160">
		  \documentclass{article}
		  \begin{document}
		  Bonjour, j'\'edite en \LaTeX!
		  \end{document}
		  </textarea>
	    
	  </div>
	    
        </section>    
        </div>
    </body>
</html>
