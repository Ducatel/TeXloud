<?php
class Project extends object {		
	public $id, $name, $server_url, $ugroup_id, $repo;
		
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

	public function deleteFiles(){
		$q = new Query('delete', 'DELETE FROM files WHERE project_id = ' . $this->id);

		return $q->result;
	}

	public function _delete(){
		$this->deleteFiles();
		parent::delete($this->id);
	}
}
?>
