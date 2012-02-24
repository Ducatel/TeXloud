<?php
/**
 * Class qui permet de gérer les sockets en receptions
 * pour du texte seulement
 */
class ReceiverTextOnly{

	/**
	 * Socket de connexion
	 */
	private $socket;

	private $remote_ip;	
	
	/**
	 * Port de connexion
	 */
	private $port;
	
	/**
	 * Constructeur du socket recepteur
	 * @param $ip: adresse IP d'ecoute
	 */
	public function __construct($ip){

		$this->socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
		if ($this->socket === false) 
			throw new Exception("socket_create() failed: reason: " . socket_strerror(socket_last_error()) . "\n");
		
		if(socket_bind($this->socket, $ip,0)===false)
			throw new Exception("socket_bind() failed: reason: " . socket_strerror(socket_last_error($this->socket)) . "\n");
			
		if (socket_listen($this->socket, 1) === false) 
   			throw new Exception("socket_listen() failed: reason: " . socket_strerror(socket_last_error($this->socket)) . "\n");
			
		socket_getsockname($this->socket, $socket_address, $this->port);
	}
	
	/**
	 * Methode qui retourne le bord d'ecoute de la socket
	 * @Return: le numero du port
	 */
	public function getPort(){
		return $this->port;
	}	
	
	/**
	 * Methode qui attend une connexion
	 * Puis qui recupere et retourne la requete sous forme de tableau associatif PHP
	 * Enfin, close la socket
	 * @param $json true si le résultat attendu est en json, false sinon
	 * @return : le tableau associatif représentant la requete
	 */
	public function getReturn($json = true){
		if (($msgsock = socket_accept($this->socket)) === false) 
			throw new Exception("socket_accept() failed: reason: " . socket_strerror(socket_last_error($this->socket)) . "\n");
		
		$trame="";
		do{
			$buf = socket_read($msgsock, 2048, PHP_BINARY_READ);
			$trame.=$buf;
		}while(strlen($buf)>0);
		
		socket_getpeername($msgsock, $this->remote_ip);
		socket_close($msgsock);
		socket_close($this->socket);

		return ($json)?json_decode($trame):$trame;
	}

	public function getRemoteIp(){
		return $this->remote_ip;
	}
}

?>
