<?php
class viewerActions extends Actions {
	public function indexSuccess() {
		$this->setTemplateAlone('_viewer', $this);
	}
}

new viewerActions();

?>
