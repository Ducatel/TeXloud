<?php
include('ReceiverTextWithBinary.php');

$ip="127.0.0.1",
$receiver=new ReceiverTextWithBinary($ip);
echo "Port: ".$receiver->getPort()."<br/>";

$receiver->getReturn(&$request,&$binary);

var_dump($request);


?>
