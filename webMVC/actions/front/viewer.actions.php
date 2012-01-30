<?php
class viewerActions extends Actions {
	public function indexSuccess() {
		$this->pdfAddr = 'ladressedemonpdf';
		$this->setTemplate('pdfView', $this);
	}
}

new viewerActions();

?>
