<?php

class ObjectAssoc {	
	public function __construct() {
	}
	
	public function save() {
		$attributes = get_object_vars($this);

			$values = '';
			$attr = '';
			
			foreach($attributes as $key=>$attribute) {
					$values .= '\''.$attribute.'\',';
					$attr .= $key . ', ';
			}
			
			$values = trim($values, ' ,');
			$attr = trim($attr, ', ');
				
			$query = new Query('insert', "INSERT INTO  ".self::getTableName()." ( " . $attr . " ) VALUES($values)");
	}
	
	public function delete(){
		$whereString = "";
			
		foreach(get_object_vars($this) as $key=>$attribute) {
			$whereString .= $key . ' = \'' . $attribute . '\' AND ';
		}
			
		$whereString = trim($whereString, 'AND ');
		
		$q = new Query('delete', 'DELETE FROM ' . $this::getTableName() . ' WHERE ' . $whereString);
		
		return $q->result;
	}
	
	public static function getTableName(){
		return strtolower(preg_replace('/([a-z])([A-Z])/', '$1_$2', get_called_class()));
	}
	
	static public function getAll($start=0, $limit=10, $where=1, $order='id ASC') {
		$classname = get_called_class();
	
		$array = array();
		$query = new Query('select', "SELECT * FROM `". $classname::getTableName() ."` WHERE $where ORDER BY $order LIMIT $start, $limit");
	
		if($query->result) {
			foreach($query->result as $data) {
				$array[] = $data;
			}
		}
		return $array;
	}
}

?>
