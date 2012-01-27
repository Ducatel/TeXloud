<?php

class homepageActions extends Actions {
	public function getTreeSuccess(){
		$file = new File(intval($_GET['file_id']));
		echo $file->getTree();
	}
}

new homepageActions();

?>
