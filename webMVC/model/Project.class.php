<?php
class Project extends object {		
	public $id, $name, $server_url, $group_id;
		
	public function __construct($id=0) {
		parent::__construct($id);
	}
}
?>
