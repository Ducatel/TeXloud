<?php
/**
 * Class qui permet de gérer les sockets en receptions
 * pour du texte ET du binaire
 */
class ReceiverTextWithBinary{
//TODO a testé
	/**
	 * Socket de connexion
	 */
	private $socket;
	
	
	/**
	 * Port de connexion
	 */
	private $port;
	
	/**
	 * Flag utiliser pour sectionner une trame
	 */
	private $messageSeparator;
	
	/**
	 * Flag utiliser pour indiquer la fin d'une trame
	 */
	private $messageEnd;
	
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
		
		$this->messageSeparator = '+==\sep==+';
	        $this->messageEnd = '+==\endTrame==+';
	}
	
	/**
	 * Methode qui retourne le bord d'ecoute de la socket
	 * @Return: le numero du port
	 */
	public function getPort(){
		return $this->port;
	}
	
	/**
	 * Methode qui attend une connexion recupere le binaire envoyé et la trame
	 * La fonction close la socket
	 * /!\ Passage par reference, les variables $request et $binaryFile sont modifié par la fonction /!\
	 * @param $request: variable qui va contenir le xml du log
	 * @param $binaryFile: variable qui va contenir le binaire
	 */
	public function getReturn(&$request,&$binaryFile){
		if (($msgsock = socket_accept($this->socket)) === false) 
			throw new Exception("socket_accept() failed: reason: " . socket_strerror(socket_last_error($this->socket)) . "\n");
		
		$trame="";
		do{
			$buf = socket_read($msgsock, 2048, PHP_BINARY_READ);
			$trame.=$buf;
		}while(!$this->endWith($trame, $this->messageEnd));
		
		socket_close($msgsock);
		socket_close($this->socket);
		
		$tabTrame=explode($this->messageSeparator,$trame);

		$tabTrame[0]=str_replace('\\','\\\\\\\\',$tabTrame[0]);

		$request=json_decode($tabTrame[0],true);
		$request=$request['log'];
		
		$binaryFileArray=explode($this->messageEnd,$tabTrame[1]);
		$binaryFile = $binaryFileArray[0];
	}

	/**
	 * Methode qui permet de detecter si la chaine ce termine bien par $fin
	 * @param $string: La chaine a testé
	 * @param $fin: la sous chaine de fin
	 * @return True si la $string ce termine par $fin sinon false
	 */
	private function endWith($string,$fin){
    	return (substr($string, (strlen($fin) * -1)) === $fin);
	}
}

?>
