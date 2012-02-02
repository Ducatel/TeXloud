<?php

session_start();
   $pdo_options[PDO::ATTR_ERRMODE] = PDO::ERRMODE_EXCEPTION;
   $bdd = new PDO('mysql:host=localhost;dbname=TeXloud', 'root', 'admin', $pdo_options);


// On met les variables utilisé dans le code PHP à FALSE (C'est-à-dire les désactiver pour le moment).
$error = FALSE;
$registerOK = FALSE;

  if(empty($_REQUEST['firstname']))
      $firstname='';
    else
      $firstname=$_REQUEST['firstname'];

  if(empty($_REQUEST['lastname']))
      $lastname='';
    else
      $lastname=$_REQUEST['lastname'];

  if(empty($_REQUEST['gender']))
      $gender='';
    else
      $gender=$_REQUEST['gender'];

  if(empty($_REQUEST['date_of_birth']))
      $date_of_birth='';
    else
      $date_of_birth=$_REQUEST['date_of_birth'];

  if(empty($_REQUEST['address']))
      $address='';
    else
      $address=$_REQUEST['adress'];

  if(empty($_REQUEST['zip']))
      $zip='';
    else
      $zip=$_REQUEST['zip'];

  if(empty($_REQUEST['city']))
      $city='';
    else
      $city=$_REQUEST['city'];

  if(empty($_REQUEST['country']))
      $country='';
  else
      $country=$_REQUEST['country'];
    
    $username = $_REQUEST['username'];
    $password = $_REQUEST['password'];
    $password2 = $_REQUEST['password2'];
    $email = $_REQUEST['email'];
  

    // On regarde si l'utilisateur est bien passé par le module d'inscription
          
        // On regarde si tout les champs sont remplis, sinon, on affiche un message à l'utilisateur.
        if($username == NULL OR $password == NULL OR $password2 == NULL){
           
            // On met la variable $error à TRUE pour que par la suite le navigateur sache qu'il y'a une erreur à afficher.
            $error = TRUE;
           
            // On écrit le message à afficher :
            $errorMSG = "Tout les champs doivent être remplis !";
               
        }
       
        // Sinon, si les deux mots de passes correspondent :
        elseif($password == $password2){
           
            // On regarde si le mot de passe et le nom de compte n'est pas le même
            if($username != $password){
              
	    
                // Si c'est bon on regarde dans la base de donnée si le nom de compte est déjà utilisé :
		$sql = $bdd->prepare('SELECT username FROM user WHERE  username= ?');
	        $sql->execute(array($username));
   
			       
            // On compte combien de valeur à pour nom de compte celui tapé par l'utilisateur.
               $sql = $sql->rowCount();
           /* Effacement de toutes les lignes de la table FRUIT */

              // Si $sql est égal à 0 (c'est-à-dire qu'il n'y a pas de nom de compte avec la valeur tapé par l'utilisateur
              if($sql == 0){
             
                  // Si tout va bien on regarde si le mot de passe n'exède pas 60 caractères.
                  if(strlen($password < 60)){
                 
                    // Si tout va bien on regarde si le nom de compte n'exède pas 60 caractères.
                    if(strlen($username < 60)){
                   
                        // Si le nom de compte et le mot de passe sont différent :
                        if($username != $password){
                   
                          // Si tout ce passe correctement, on peut maintenant l'inscrire dans la base de données :
			    $res = $bdd->query('select md5(rand()) from dual');
			    $salt = $res->fetchColumn();
			    $password = sha1($_REQUEST['password'].$salt);
			      
                      	      $sql=$bdd->prepare('INSERT INTO user (firstname,lastname,username,gender,date_of_birth,address,zip,city,country,email,salt,password) VALUES  (?,?,?,?,?,?,?,?,?,?,?,?)');
			      $sql->execute(array($firstname,$lastname,$username,$gender,$date_of_birth,$address,$zip,$city,$country,$email,$salt,$password));
                          // Si la requête s'est bien effectué :
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
                       
                        }
                       
                        // Sinon on fais savoir à l'utilisateur qu'il a mis un nom de compte trop long.
                        else{
                       
                          $error = TRUE;
                         
                          $errorMSG = "Votre nom compte ne doit pas dépasser <strong>60 caractères</strong> !";
                         
                          $login = NULL;
                         
                          $pass = $_REQUEST["password"];
                       
                        }
                   
                    }
                 
                  }
                 
                  // Si le mot de passe dépasse 60 caractères on le fait savoir
                  else{
                 
                    $error = TRUE;
                   
                    $errorMSG = "Votre mot de passe ne doit pas dépasser <strong>60 caractères</strong> !";
                   
                    $username = $_REQUEST["username"];
                   
                    $password = NULL;
                 
                  }
             
              }
             
              // Sinon on affiche un message d'erreur lui disant que ce nom de compte est déjà utilisé.
              else{
             
                  $error = TRUE;
                 
                  $errorMSG = "Le nom de compte <strong>".$_REQUEST["username"]."</strong> est déjà utilisé !";
                 
                  $login = NULL;
                 
                  $pass = $_REQUEST["password"];
             
              }
            }
           
            // Sinon on fais savoir à l'utilisateur qu'il doit changer le mot de passe ou le nom de compte
            else{
               
                $error = TRUE;
               
                $errorMSG = "Le nom de compte et le mot de passe doivent êtres différents !";
               
            }
           
        }
     

      // Sinon si le nom de compte et le mot de passe ont la même valeur :
      elseif($_REQUEST["username"] == $_REQUEST["password"]){
     
        $error = TRUE;
       
        $errorMSG = "Le nom de compte et le mot de passe doivent être différents !";
     
      }
       
    
  $sql->closeCursor();

  if($error == TRUE){ echo "<p>".$errorMSG."</p>"; }

  if($registerOK == TRUE){ echo "<p>".$registerMSG."</strong></p>"; }

?>
