<?php
class Project extends object {		
	public $id, $name, $server_url, $ugroup_id;
		
	public function __construct($id=0) {
		parent::__construct($id);
	}
	
	public function getFiles(){
		$q = new Query('select', 'SELECT id FROM file WHERE project_id = ' . $this->id . ' ORDER BY parent');
		$files = array();
		
		foreach($q->result as $f)
			$files[] = new File($f->id);
		
		return $files;
	}
}
?>
