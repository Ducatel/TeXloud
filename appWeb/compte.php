
<?php

   session_start();
   $pdo_options[PDO::ATTR_ERRMODE] = PDO::ERRMODE_EXCEPTION;
   $bdd = new PDO('mysql:host=localhost;dbname=Texloud', 'root', 'debouz1990', $pdo_options);

		 $login=$_SESSION["identifiant"];
		 $prenom=$_POST['prenom'];
		 $nom=$_POST['nom'];
		 $sex=$_POST['sex'];
		 $datenaissance=$_POST['datenaissance'];
		 $adresse=$_POST['adresse'];
		 $codepostal=$_POST['codepostal'];
		 $ville=$_POST['ville'];
		 $pays=$_POST['pays'];
	 /*   $reponse = $bdd->query('SELECT prenom,nom,sex,datenaissance,adresse,codepostal,ville,pays FROM utilisateur WHERE identifiant = '.$login.'');
	    $affichage=$reponse->fetch(PDO::FETCH_OBJ);
	    echo "$affichage->prenom";	
	    echo "$affichage->nom";
	    echo "$affichage->sex";*/
	    
	    
	      
    $sql=$bdd->query("UPDATE utilisateur
			SET     prenom = '$prenom',
			        nom='$nom',
			        sex='$sex',
				datenaissance='$datenaissance',
				adresse='$adresse',
				codepostal='$codepostal',
				ville='$ville',
				pays='$pays'
			WHERE  identifiant='$login'");
				
                            
			  
                          if($sql){
                         
                              // On met la variable $registerOK à TRUE pour que l'inscription soit finalisé
                              $registerOK = TRUE;
                              // On l'affiche un message pour le dire que l'inscription c'est bien déroulé :
                              $registerMSG = "Inscription réussie ! Vous êtes maintenant membre du site.";
                             
        
                         
                          }
                         
                          // Sinon on l'affiche un message d'erreur (généralement pour vous quand vous testez vos scripts PHP)
                          else{
                         
                              $error = TRUE;
                             
                              $errorMSG = "Erreur dans la requête SQL<br/>".$sql."<br/>";
                         
                          }
     
          


    
  $sql->closeCursor();

  if($error == TRUE){ echo "<p>".$errorMSG."</p>"; }

  if($registerOK == TRUE){ echo "<p>".$registerMSG."</strong></p>"; }

header('Location: texloud.php');
?>