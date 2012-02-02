<?php

class object {
	public $id=null;
	
	public function __construct($id=0) {
		if($id>0) {
			$attributes = get_object_vars($this);
			$query = new Query('unique_select', "SELECT * FROM ".$this::getTableName()." WHERE id=$id");

			if($query->result) {
				foreach($attributes as $key=>$attribute) {
					$this->$key = $query->result->$key;
				}
				return true;
			}
			else
				return false;
		}
	}
	
	public static function delete($id=0){
		$classname = get_called_class();
		
		if($id){
			$q = new Query('delete', 'DELETE FROM ' . $classname::getTableName() . ' WHERE id=' . $id);
			return $q->result;
		}
	
		return false;
	}
	
	public function save() {
		$attributes = get_object_vars($this);
		// nouvel objet
		if($this->id == null) {
			$values = '';
			$attr = '';
			
			foreach($attributes as $key=>$attribute) {
				if($key != 'id'){
					if(!is_null($attribute))
						$values .= '\''.addslashes($attribute).'\',';
					else
						$values .= 'DEFAULT,';
				}
				$attr .= $key . ', ';
			}
			
			$values = trim($values, ' ,');
			$attr = trim($attr, ', ');
			
			//	echo "INSERT INTO  ".$this::getTableName()." ( " . $attr . " ) VALUES('', $values)<br />";
			$query = new Query('insert', "INSERT INTO  ".$this::getTableName()." ( " . $attr . " ) VALUES('', $values)");
			
			$this->id = $query->last_id;
		}
		// sauvegarde d'un objet
		else {
			$values = '';
			foreach($attributes as $key=>$attribute) {
				if($key != 'id'){
					if(!is_null($attribute))
						$values .= $key.'=\''.addslashes($attribute).'\',';
					else 
						$values .= $key.'=DEFAULT,';
				}
			}
			$values = trim($values, ' ,');
			//echo "UPDATE ".$this::getTableName()." SET $values WHERE id=$this->id<br />";
			$query = new Query('update', "UPDATE ".$this::getTableName()." SET $values WHERE id=$this->id");
		}
		
	}
	
	public static function getTableName(){
		return strtolower(preg_replace('/([a-z])([A-Z])/', '$1_$2', get_called_class()));
	}
	
	static public function getAll($start=null, $limit=null, $where=1, $order='id ASC') {
		$classname = get_called_class();
		
		$array = array();
		
		$rq = "SELECT id FROM `". $classname::getTableName() ."` WHERE $where ORDER BY $order ";
		
		if($start && $limit)
			$rq .= "LIMIT $start, $limit";
		
		$query = new Query('select', $rq);
		
		if($query->result) {
			foreach($query->result as $data) {
				$array[] = new $classname($data->id);
			}
		}
		return $array;
	}
	
	public function get($table_name) {
		$attribute_name = $table_name.'_id';
		$classname = Functions::getClassnameFromTableName($table_name);
		
		return new $classname($this->$attribute_name);
	}
}

?>
