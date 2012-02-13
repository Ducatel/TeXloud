<?php
class viewerActions extends Actions {
	public function indexSuccess() {
		$this->setTemplate('_viewer', $this);
	}
}

new viewerActions();

?>
