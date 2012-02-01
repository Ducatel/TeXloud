
<?php

session_start();
   $pdo_options[PDO::ATTR_ERRMODE] = PDO::ERRMODE_EXCEPTION;
   $bdd = new PDO('mysql:host=localhost;dbname=Texloud', 'root', 'debouz1990', $pdo_options);


// On met les variables utilisé dans le code PHP à FALSE (C'est-à-dire les désactiver pour le moment).
$error = FALSE;
$registerOK = FALSE;

    // On regarde si l'utilisateur est bien passé par le module d'inscription
          
        // On regarde si tout les champs sont remplis, sinon, on affiche un message à l'utilisateur.
        if($_POST["identifiant"] == NULL OR $_POST["motpasse"] == NULL OR $_POST["motpassedeux"] == NULL){
           
            // On met la variable $error à TRUE pour que par la suite le navigateur sache qu'il y'a une erreur à afficher.
            $error = TRUE;
           
            // On écrit le message à afficher :
            $errorMSG = "Tout les champs doivent être remplis !";
               
        }
       
        // Sinon, si les deux mots de passes correspondent :
        elseif($_POST["motpasse"] == $_POST["motpassedeux"]){
           
            // On regarde si le mot de passe et le nom de compte n'est pas le même
            if($_POST["identifiant"] != $_POST["motpasse"]){
              
	    
                // Si c'est bon on regarde dans la base de donnée si le nom de compte est déjà utilisé :
		$sql = $bdd->prepare('SELECT idutilisateur FROM utilisateur WHERE  identifiant= ?');
	        $sql->execute(array($_POST['identifiant']));
   
			       
            // On compte combien de valeur à pour nom de compte celui tapé par l'utilisateur.
               $sql = $sql->rowCount();
           /* Effacement de toutes les lignes de la table FRUIT */

              // Si $sql est égal à 0 (c'est-à-dire qu'il n'y a pas de nom de compte avec la valeur tapé par l'utilisateur
              if($sql == 0){
             
                  // Si tout va bien on regarde si le mot de passe n'exède pas 60 caractères.
                  if(strlen($_POST["motpasse"] < 60)){
                 
                    // Si tout va bien on regarde si le nom de compte n'exède pas 60 caractères.
                    if(strlen($_POST["identifiant"] < 60)){
                   
                        // Si le nom de compte et le mot de passe sont différent :
                        if($_POST["identifiant"] != $_POST["motpasse"]){
                   
                          // Si tout ce passe correctement, on peut maintenant l'inscrire dans la base de données :
			    $salt = sha1($_POST['motpasse']);
			    $motpasse = sha1($_POST['motpasse'].$salt);
			    
                            /* $sql=$bdd->prepare('INSERT INTO utilisateur (prenom,nom,sex,datenaissance,adresse,codepostal,ville,pays,identifiant,motpasse,motpassedeux,mail) VALUES  (?,?,?,?,?,?,?,?,?,?,?,?)');
			      $sql->execute(array($_POST['prenom'],$_POST['nom'],$_POST['sex'],$_POST['datenaissance'],$_POST['adresse'],$_POST['codepostal'],$_POST['ville'],$_POST['pays'],$_POST['identifiant'],$motpasse,$motpassedeux,$_POST['mail']));*/
			      $sql=$bdd->prepare('INSERT INTO utilisateur (identifiant,motpasse,salt,mail) VALUES  (?,?,?,?)');
			      $sql->execute(array($_POST['identifiant'],$motpasse,$salt,$_POST['mail']));
                          // Si la requête s'est bien effectué :
                          if($sql){
                         
                              // On met la variable $registerOK à TRUE pour que l'inscription soit finalisé
                              $registerOK = TRUE;
                              // On l'affiche un message pour le dire que l'inscription c'est bien déroulé :
                              $registerMSG = "Inscription réussie ! Vous êtes maintenant membre du site.";
                             
                              // On le met des variables de session pour stocker le nom de compte et le mot de passe :
                              $_SESSION["identifiant"] = $_POST["identifiant"];
                              $_SESSION["motpasse"] = $_POST["motpasse"];
                             
                              // Comme un utilisateur est différent, on crée des variables de sessions pour "varier" l'utilisateur comme ceci :
                              // echo $_SESSION["login"]; (bien entendu avec les balises PHP, sinons cela ne marchera pas.
                         
                          }
                         
                          // Sinon on l'affiche un message d'erreur (généralement pour vous quand vous testez vos scripts PHP)
                          else{
                         
                              $error = TRUE;
                             
                              $errorMSG = "Erreur dans la requête SQL<br/>".$sql."<br/>";
                         
                          }
                       
                        }
                       
                        // Sinon on fais savoir à l'utilisateur qu'il a mis un nom de compte trop long.
                        else{
                       
                          $error = TRUE;
                         
                          $errorMSG = "Votre nom compte ne doit pas dépasser <strong>60 caractères</strong> !";
                         
                          $login = NULL;
                         
                          $pass = $_POST["pass"];
                       
                        }
                   
                    }
                 
                  }
                 
                  // Si le mot de passe dépasse 60 caractères on le fait savoir
                  else{
                 
                    $error = TRUE;
                   
                    $errorMSG = "Votre mot de passe ne doit pas dépasser <strong>60 caractères</strong> !";
                   
                    $login = $_POST["login"];
                   
                    $pass = NULL;
                 
                  }
             
              }
             
              // Sinon on affiche un message d'erreur lui disant que ce nom de compte est déjà utilisé.
              else{
             
                  $error = TRUE;
                 
                  $errorMSG = "Le nom de compte <strong>".$_POST["identifiant"]."</strong> est déjà utilisé !";
                 
                  $login = NULL;
                 
                  $pass = $_POST["motpasse"];
             
              }
            }
           
            // Sinon on fais savoir à l'utilisateur qu'il doit changer le mot de passe ou le nom de compte
            else{
               
                $error = TRUE;
               
                $errorMSG = "Le nom de compte et le mot de passe doivent êtres différents !";
               
            }
           
        }
     
      // Sinon si les deux mots de passes sont différents :     
      elseif($_POST["motpasse"] != $_POST["motpassedeux"]){
     
        $error = TRUE;
       
        $errorMSG = "Les deux mots de passes sont différents !";
       
        $login = $_POST["identifiant"];
       
        $pass = NULL;
     
      }
     
      // Sinon si le nom de compte et le mot de passe ont la même valeur :
      elseif($_POST["identifiant"] == $_POST["motpasse"]){
     
        $error = TRUE;
       
        $errorMSG = "Le nom de compte et le mot de passe doivent être différents !";
     
      }
       
    
  $sql->closeCursor();

  if($error == TRUE){ echo "<p>".$errorMSG."</p>"; }

  if($registerOK == TRUE){ echo "<p>".$registerMSG."</strong></p>"; }

header('Location: texloud.php');
/*

<?php

session_start();
   $pdo_options[PDO::ATTR_ERRMODE] = PDO::ERRMODE_EXCEPTION;
   $bdd = new PDO('mysql:host=localhost;dbname=Texloud', 'root', 'debouz1990', $pdo_options);


// On met les variables utilisé dans le code PHP à FALSE (C'est-à-dire les désactiver pour le moment).
$error = FALSE;
$registerOK = FALSE;

    // On regarde si l'utilisateur est bien passé par le module d'inscription
          
        // On regarde si tout les champs sont remplis, sinon, on affiche un message à l'utilisateur.
        if($_POST["identifiant"] == NULL OR $_POST["motpasse"] == NULL OR $_POST["motpassedeux"] == NULL){
           
            // On met la variable $error à TRUE pour que par la suite le navigateur sache qu'il y'a une erreur à afficher.
            $error = TRUE;
           
            // On écrit le message à afficher :
            $errorMSG = "Tout les champs doivent être remplis !";
               
        }
       
        // Sinon, si les deux mots de passes correspondent :
        elseif($_POST["motpasse"] == $_POST["motpassedeux"]){
           
            // On regarde si le mot de passe et le nom de compte n'est pas le même
            if($_POST["identifiant"] != $_POST["motpasse"]){
              
	    
                // Si c'est bon on regarde dans la base de donnée si le nom de compte est déjà utilisé :
		$sql = $bdd->prepare('SELECT idutilisateur FROM utilisateur WHERE  identifiant= ?');
	        $sql->execute(array($_POST['identifiant']));
   
			       
            // On compte combien de valeur à pour nom de compte celui tapé par l'utilisateur.
               $sql = $sql->rowCount();
           /* Effacement de toutes les lignes de la table FRUIT */

              // Si $sql est égal à 0 (c'est-à-dire qu'il n'y a pas de nom de compte avec la valeur tapé par l'utilisateur
             /* if($sql == 0){
             
                  // Si tout va bien on regarde si le mot de passe n'exède pas 60 caractères.
                  if(strlen($_POST["motpasse"] < 60)){
                 
                    // Si tout va bien on regarde si le nom de compte n'exède pas 60 caractères.
                    if(strlen($_POST["identifiant"] < 60)){
                   
                        // Si le nom de compte et le mot de passe sont différent :
                        if($_POST["identifiant"] != $_POST["motpasse"]){
                   
                          // Si tout ce passe correctement, on peut maintenant l'inscrire dans la base de données :
			    $salt = sha1($_POST['motpasse']);
			    $motpasse=sha1($salt.$_POST['motpasse']);;

	                      /* $sql=$bdd->prepare('INSERT INTO utilisateur (prenom,nom,sex,datenaissance,adresse,codepostal,ville,pays,identifiant,motpasse,motpassedeux,mail) VALUES  (?,?,?,?,?,?,?,?,?,?,?,?)');
			      $sql->execute(array($_POST['prenom'],$_POST['nom'],$_POST['sex'],$_POST['datenaissance'],$_POST['adresse'],$_POST['codepostal'],$_POST['ville'],$_POST['pays'],$_POST['identifiant'],$motpasse,$motpassedeux,$_POST['mail']));*/
			     /* $sql=$bdd->prepare('INSERT INTO utilisateur (identifiant,motpasse,salt,mail) VALUES  (?,?,?,?)');
			      $sql->execute(array($_POST['identifiant'],$motpasse,$salt,$_POST['mail']));
                          // Si la requête s'est bien effectué :
                          if($sql){
                         
                              // On met la variable $registerOK à TRUE pour que l'inscription soit finalisé
                              $registerOK = TRUE;
                              // On l'affiche un message pour le dire que l'inscription c'est bien déroulé :
                              $registerMSG = "Inscription réussie ! Vous êtes maintenant membre du site.";
                             
                              // On le met des variables de session pour stocker le nom de compte et le mot de passe :
                              $_SESSION["identifiant"] = $_POST["identifiant"];
                              $_SESSION["motpasse"] = $_POST["motpasse"];
                             
                              // Comme un utilisateur est différent, on crée des variables de sessions pour "varier" l'utilisateur comme ceci :
                              // echo $_SESSION["login"]; (bien entendu avec les balises PHP, sinons cela ne marchera pas.
                         
                          }
                         
                          // Sinon on l'affiche un message d'erreur (généralement pour vous quand vous testez vos scripts PHP)
                          else{
                         
                              $error = TRUE;
                             
                              $errorMSG = "Erreur dans la requête SQL<br/>".$sql."<br/>";
                         
                          }
                       
                        }
                       
                        // Sinon on fais savoir à l'utilisateur qu'il a mis un nom de compte trop long.
                        else{
                       
                          $error = TRUE;
                         
                          $errorMSG = "Votre nom compte ne doit pas dépasser <strong>60 caractères</strong> !";
                         
                          $login = NULL;
                         
                          $pass = $_POST["pass"];
                       
                        }
                   
                    }
                 
                  }
                 
                  // Si le mot de passe dépasse 60 caractères on le fait savoir
                  else{
                 
                    $error = TRUE;
                   
                    $errorMSG = "Votre mot de passe ne doit pas dépasser <strong>60 caractères</strong> !";
                   
                    $login = $_POST["login"];
                   
                    $pass = NULL;
                 
                  }
             
              }
             
              // Sinon on affiche un message d'erreur lui disant que ce nom de compte est déjà utilisé.
              else{
             
                  $error = TRUE;
                 
                  $errorMSG = "Le nom de compte <strong>".$_POST["identifiant"]."</strong> est déjà utilisé !";
                 
                  $login = NULL;
                 
                  $pass = $_POST["motpasse"];
             
              }
            }
           
            // Sinon on fais savoir à l'utilisateur qu'il doit changer le mot de passe ou le nom de compte
            else{
               
                $error = TRUE;
               
                $errorMSG = "Le nom de compte et le mot de passe doivent êtres différents !";
               
            }
           
        }
     
      // Sinon si les deux mots de passes sont différents :     
      elseif($_POST["motpasse"] != $_POST["motpassedeux"]){
     
        $error = TRUE;
       
        $errorMSG = "Les deux mots de passes sont différents !";
       
        $login = $_POST["identifiant"];
       
        $pass = NULL;
     
      }
     
      // Sinon si le nom de compte et le mot de passe ont la même valeur :
      elseif($_POST["identifiant"] == $_POST["motpasse"]){
     
        $error = TRUE;
       
        $errorMSG = "Le nom de compte et le mot de passe doivent être différents !";
     
      }
       
    
  $sql->closeCursor();

  if($error == TRUE){ echo "<p>".$errorMSG."</p>"; }

  if($registerOK == TRUE){ echo "<p>".$registerMSG."</strong></p>"; }

header('Location: texloud.php');
?>
*/
?>
