
<?php

require("/var/www/phpmailer/class.phpmailer.php");
include("/var/www/phpmailer/class.smtp.php");
$mail = new PHPMailer();
$mail->SetLanguage('fr','/var/www/phpmailer/language/');
$mail->SMTPDebug = true;

$mail->IsSMTP();                                      // set mailer to use SMTP
$mail->Host = "smtp.googlemail.com";  // specify main and backup server
$mail->SMTPAuth = true  ;   // turn on SMTP authentication
$mail->Username = "zakariabouchakor";  // SMTP username
$mail->Password = "debouz1990"; // SMTP password
$mail->Port = 465; 
$mail->From = "zakariabouchakor@gmail.com";
$mail->FromName = "zakariabouchakor";
$mail->AddAddress("bouchakor_zakaria@hotmail.fr", "Josh Adams");
                // name is optional
$mail->AddReplyTo("bouchakor_zakaria@hotmail.fr", "Information");

$mail->WordWrap = 50;                                 // set word wrap to 50 characters
   // optional name
$mail->IsHTML(true);                                  // set email format to HTML

$mail->Subject = "Here is the subject";
$mail->Body    = "This is the HTML message body <b>in bold!</b>";
$mail->AltBody = "This is the body in plain text for non-HTML mail clients";

if(!$mail->Send())
{
   echo "Message could not be sent. <p>";
   echo "Mailer Error: " . $mail->ErrorInfo;
   exit;
}

echo "Message has been sent";
?>