<?php

class query {

	private $hostname = 'localhost';
	private $user = 'root';
	private $password = 'admin';
	private $database = 'texloud';
	var $link;
	var $result;
	var $last_id;
	
	public function Query($type, $request) { 
		$this->connect();
		//mysql_query("SET NAMES 'utf8';", $this->link);
		$this->result = $this->$type($request);
		$this->close();
	}
	
	private function delete($query){
		try{
			mysql_query($query, $this->link);
			return true;
		}
		catch(Exception $e){
			return false;
		}
	}
	
	private function connect() {
		$this->link = mysql_connect($this->hostname, $this->user, $this->password);
		mysql_select_db($this->database, $this->link);
	}
	
	private function insert($query) {
		//$query = mysql_real_escape_string($query, $this->link);
		$query = mysql_query($query, $this->link) or die(mysql_error());
		$this->last_id = mysql_insert_id($this->link);
		return $query;
	}
	
	private function update($query) {
		$query = mysql_query($query, $this->link) or die(mysql_error());
		return $query;
	}	
	
	private function unique_select($query) {
		$result = mysql_query($query, $this->link) or die(mysql_error());
		if($result)
			return mysql_fetch_object($result);
		else
			return false;
	}
	
	private function select($query) {
		$array = array();
		$result = mysql_query($query, $this->link) or die(mysql_error());
		
		if($result) {
			while($data = mysql_fetch_object($result)) {
				$array[] = $data;
			}
			return $array;
		}
		else
			return false;
	}
	
	private function close() {
		mysql_close($this->link);
	}
	
}

?>
