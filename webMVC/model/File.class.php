<?php
class File extends object {		
	public $id, $path, $project_id, $is_dir, $parent;
		
	public function __construct($id=0) {
		parent::__construct($id);
	}
	
	public function getTree(){
		$files = parent::getAll(0, 1000, 'parent = ' . $this->id, 'id' );
		
		$tree = "";
		
		foreach($files as $t){
			$supp = "";
			
			if($t->is_dir == 1)
				$supp = '<ul class="ajax"><li id="file_' . $t->id . '">{url: /ajax/getTree/file_id/' . $t->id . '}</li></ul>';
			
			$tree .= '<li class="text" id="file_' . $t->id. '"><span>' . $t->filename . '</span>' . $supp . '</li>';
			$tree .= $t->getTree();
		}
		
		return $tree;
	}
}
?>
