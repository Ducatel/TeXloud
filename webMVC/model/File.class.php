<?php
class File extends object {		
	public $id, $path, $project_id, $is_dir, $parent;
		
	public function __construct($id=0) {
		parent::__construct($id);
	}
	
	public function setPathPrefix($prefix){
		$filePath = explode('/', $this->path);
		
		$this->path = $prefix . $filePath[count($filePath)-1];
		$this->save();
	}
	
	public function renameChildrenPath(){
		if($this->is_dir){
			$filePath = explode('/', $this->path);
			$children = $this->getChildren();
			
			$prefix = $this->path . '/';
			
			if($prefix){
				foreach($children as $c){
					$c->setPathPrefix($prefix);
					
					if($c->is_dir)
						$c->renameChildrenPath();
				}
			}
		}
	}
	
	public function getChildren(){
		$children = File::getAll(null, null, 'parent = ' . $this->id);
		
		return $children;
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

	public static function deleteFromData($id){
		$file = new File($id);
		
		if($file->id){	
			$sender = new Sender(FRONTAL_IP, FRONTAL_PORT);

			$project = new Project($file->project_id);

			$dataUrl=explode(':', $project->server_url);
			$dataIp=$dataUrl[0];
			$dataPort=$dataUrl[1];
		
			if(!$_SESSION['workingCopyDir'][$project->id]){
				Common::getProject($project);
			}

			$request = array('label' => 'deleteFile',
					 'path' => $_SESSION['workingCopyDir'][$_SESSION['project_id']],
					 'filename' => $file->path,
                			 'httpPort' => '',
					 'servDataIp' => $dataIp,
					 'servDataPort' => $dataPort);
		
			$sender->setRequest($request);
			$sender->sendRequest();
			unset($sender);
		}

		parent::delete($id);
	}
	
	public function _delete(){
		if($this->is_dir){
			foreach($this->getChildren() as $c){
				$c->_delete();
			}
		}
		
		return parent::delete($this->id);
	}

	public static function getPath($id){
		$q = new Query('unique_select', 'SELECT path FROM file WHERE id = ' . $id);

		return $q->result->path?$q->result->path:null;
	}
}
?>
