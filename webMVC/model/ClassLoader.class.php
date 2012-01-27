<?php
class ClassLoader {
	public function __construct() {
		spl_autoload_register(array($this, 'loader'));
	}
	
	private function loader($className) {
		if(file_exists('../model/' . $className . '.class.php'))
			require '../model/' . $className . '.class.php';
		else if(file_exists('../model/' . ucfirst($className) . '.class.php'))
			require '../model/' . ucfirst($className) . '.class.php';
		else
			throw new ErrorException('could not load class ' . $className);
	}
}
?>