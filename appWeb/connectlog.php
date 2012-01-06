
<?php

   session_start();
   $pdo_options[PDO::ATTR_ERRMODE] = PDO::ERRMODE_EXCEPTION;
   
   $bdd = new PDO('mysql:host=localhost;dbname=Texloud', 'root', 'debouz1990', $pdo_options);



  // On met les variables utilisé dans le code PHP à FALSE (C'est-à-dire les désactiver pour le moment).
  $error = FALSE;
  $registerOK = FALSE;
             
            
   
	
  try
      {
	  $motpasse = sha1($_POST['motpasse']);
	  // On récupère tout le contenu de la table jeux_video
	    $reponse = $bdd->prepare('SELECT identifiant,motpasse FROM utilisateur WHERE identifiant = ? AND motpasse = ?');
	    $reponse->execute(array($_POST['identifiant'],$motpasse));
	    $nbligne = $reponse->rowCount();
	    


	  // mot de passe ou identifiant n'existe pas
	  if($nbligne == 0){
	      if(strlen($_POST["motpasse"] == 0) OR strlen($_POST["identifiant"] == 0)){
			  $error = TRUE;
			  $errorMSG = "Tout les champs doivent être remplis !";
			  $motpasse = NULL;

	      }


	  }
 
	  elseif($nbligne){
	      
		  // On met la variable $registerOK à TRUE pour que l'inscription soit finalisé
		  $registerOK = TRUE;
		  // On l'affiche un message pour le dire que l'inscription c'est bien déroulé :
		  $registerMSG = "Identification réussie ! Vous allez être dirigé vers le site.";
		  
		  // On le met des variables de session pour stocker le nom de compte et le mot de passe :
		  $_SESSION["identifiant"] = $_POST["identifiant"];
		  $_SESSION["motpasse"] = $_POST["motpasse"];
		  $_SESSION['identifiant']=$_POST['identifiant'];
		  // Comme un utilisateur est différent, on crée des variables de sessions pour "varier" l'utilisateur comme ceci :
		  // echo $_SESSION["login"]; (bien entendu avec les balises PHP, sinons cela ne marchera pas.
	      
	      }
	  
	
      $reponse->closeCursor(); // Termine le traitement de la requête

      }
    
               
  catch(Exception $e)
      {
	  // En cas d'erreur précédemment, on affiche un message et on arrête tout
	  die('Erreur : '.$e->getMessage());
      }



  if($error == TRUE){ 
     ?>
        <p>
         <?php  echo "<p>Tout les champs doivent être remplis !</p>";?><br />
               
        </p>
    <?php
    
    header('Location: Acceuil.html');
  }

  if($registerOK == TRUE){ 
    ?>
        <p>
         <?php echo "<p>".$registerMSG."</strong></p>";?><br />
               
        </p>
    <?php
       
      $_SESSION['last_access']=time();
      $_SESSION['ipaddr']=$_SERVER['REMOTE_ADDR'];
      
         
      header('Location: texloud.php'); 

   }


?>