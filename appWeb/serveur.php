<?php

$address = "127.0.0.1";
$port = 12800;

echo "";

if (($sock = socket_create(AF_INET, SOCK_STREAM, SOL_TCP)) === false) {
    echo "socket_create() failed: reason: " . socket_strerror(socket_last_error()) . "\n";
}else{
	echo "socket_create():  OK<br/>";
}


if (socket_bind($sock, $address, $port) === false) {
    echo "socket_bind() failed: reason: " . socket_strerror(socket_last_error($sock)) . "\n";
}else{
	echo "socket_bind(): OK<br/>";
}


if (socket_listen($sock, 5) === false) {
    echo "socket_listen() failed: reason: " . socket_strerror(socket_last_error($sock)) . "\n";
}else{
	echo "socket_listen(): OK<br/>";
}



if (($msgsock = socket_accept($sock)) === false) {
    echo "socket_accept() failed: reason: " . socket_strerror(socket_last_error($sock)) . "\n";
    break;
}
else{
	echo "connexion accepter<br/>";
}

$buf = socket_read($msgsock, 2048, PHP_BINARY_READ);
echo $buf."<br/>";

socket_close($msgsock);


socket_close($sock);

?>
