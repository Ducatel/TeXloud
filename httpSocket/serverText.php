<?php
include('ReceiverTextOnly.php');

$ip='127.0.0.1';

$receiver=new ReceiverTextOnly($ip);
echo "Port: ".$receiver->getPort()."<br/>";

//$trame=$receiver->getReturn();
//var_dump($trame);

?>
