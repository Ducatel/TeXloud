<?php
class Actions {
	
	var $user, $js, $css;
	
	function __construct() {
		$this->js = array();
		$this->css = array();
		$this->user = user::isLogged();
		if(!empty($_GET['p']) && file_exists('../actions/front/'.$_GET['p'].'.actions.php'))
			$className = $_GET['p'].'Actions';
		elseif($this->user) {
			//$className = 'userActions';
			$className = 'editionAction';
		}
		else {
			$className = 'homepageActions';
		}
		if(!empty($_GET['a']))
			$function = $_GET['a'].'Success';
		else{
			$function = 'indexSuccess';
		}
		
		$this->$function();
	}
	
	function setTemplate($name, $var=NULL) {
		ob_start();
		require_once('../view/front/templates/'.$name.'.php');
		$action_content = ob_get_contents();
		ob_end_clean();
		ob_start();
		require_once('../view/front/layout.php');
		$content = ob_get_contents();
		ob_end_clean();
		echo $content;
	}

	function setTemplateAlone($name, $var=NULL) {
		ob_start();
		require_once('../view/front/templates/'.$name.'.php');
		$content = ob_get_contents();
		ob_end_clean();
		echo $content;
	}	
	
	public function addJs($filename) {
		$this->js[] = $filename;
	}
	
	public function addCss($filename) {
		$this->css[] = $filename;
	}
	
	function redirect($url) {
		header('Location: '.$url);
		exit;
	}
}

?>
