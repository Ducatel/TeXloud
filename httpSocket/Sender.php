<?php

/**
 * class Sender
 * Classe qui permet de gerer l'envoie d'une requete a un serveur distant
 **/
class Sender {

	/**
	 * Adresse IP du serveur distant
	 */
	private $remoteIpAdress;
	
	/**
	 * Port du service vise sur le serveur distant
	 */
	private $remoteServicePort;
	
	/**
	 * Tableau associatif contenant la requete
	 */
	private $request;
	
	/**
	 * Socket de connexion
	 */
	private $socket;
	
	/**
	 * Boolean permettant de savoir si la socket est connecter
	 */
	private $connect;
	
	public function __construct($ip,$port){
		$regexAdresse="#^[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}$#";
        $regexPort="#^[0-9]{1,5}$#";
		
		if(preg_match($regexAdresse,$ip))
			$this->remoteIpAdress=$ip;
		else
			throw new Exception("Construct Sender failed.\nReason: Bad IP adress\n");

		if(preg_match($regexPort,$port))
			$this->remoteServicePort=$port;
		else
			throw new Exception("Construct Sender failed.\nReason: Bad port adress\n");

		// Creation de la socket TCP_IP
		$this->socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
		if ($this->socket === false) 
			throw new Exception("socket_create() failed: reason: " . socket_strerror(socket_last_error()) . "\n");
		
		$this->connect=false;
		$this->request="";		
	}
	
	public function __destruct() {
		if($this->connect)
			socket_close($this->socket);
    }

	/**
	 * Methode permettant de ce connecter au serveur distant
	 * @raise: Peux declencher une Exception en cas de problème de connexion
	 */
	public function connectSocket(){
		$result = socket_connect($this->socket, $this->remoteIpAdress, $this->remoteServicePort);
		if ($result === false) 
			throw new Exception("socket_connect() failed.\nReason: (".$result.") " . socket_strerror(socket_last_error($this->socket)) . "\n");
			
		$this->connect=true;
	}
	
	/**
	 * Méthode permettant d'envoyer la requete
	 * @raise: Peux declencher une Exception si la requete est vide
	 */
	public function sendRequest(){
			if(!empty($this->request)){
			
				if(!$this->connect)
					$this->connectSocket();
				
				socket_write($this->socket, $this->request, strlen($this->request));
			}
			else
				throw new Exception("socket_write() failed.\nReason: request is empty\n");
	}
	
	public function closeSocket(){
		if($this->connect)
			socket_close($this->socket);
			
		$this->connect=false;
	}
	
	/**
	 * Méthode qui permet de renseigner la requete a envoyer
	 * @param $request: Tableau associatif contenant la requete
	 */
	public function setRequest($request){
		$this->request=json_encode($request);
	}
	
	public function getRequest(){
		return $this->request;
	}
	
	public function getRemoteIpAdress(){
		return $this->remoteIpAdress;
	}
	
	public function getRemoteServicePort(){
		return $this->remoteServicePort;
	}
}



?>
