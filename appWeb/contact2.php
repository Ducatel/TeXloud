<?
	require "/var/www/PHPMailer_v5.1/class.phpmailer.php";
	$mail = new PHPmailer();
	$mail->IsSMTP();
	$mail->Host='smtp.gmail.com';
	$mail->Port = 465; 
	$mail->From='zakariabouchakor@gmail.com';
	$mail->Password = 'debouz1990'; 
	$mail->AddAddress('bouchakor_zakaria@hotmail.fr');
	$mail->AddReplyTo('zakariabouchakor@gmail.com');	
	$mail->Subject='Exemple trouvé sur DVP';
	$mail->Body='Voici un exemple d\'e-mail au format Texte';
	if(!$mail->Send()){ //Teste le return code de la fonction
	  echo $mail->ErrorInfo; //Affiche le message d'erreur (ATTENTION:voir section 7)
	}
	else{	  
	  echo 'Mail envoyé avec succès';
	}
	$mail->SmtpClose();
	unset($mail);
?>