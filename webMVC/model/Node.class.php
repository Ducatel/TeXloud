<?php

/**
 * 
 * Classe qui represente les noeuds de l'arbre
 * @author David Ducatel, Zakaria Bouchakor
 *
 */
class Node{
	
	private $id;
	private $name;
	private $parentId;
	private $folder;
	
	/**
	 * Constructeur de Node
	 * @param $name: Nom du noeud
	 * @param $parentId: id du parent du noeud
	 * @param Boolean $folder: repertoire ou non
	 */
	public function __construct($name,$parentId,$folder, $nodeId=null){
		$this->name=$name;
		$this->parentId=$parentId;
		
		$this->id=$nodeId!==null?$nodeId:uniqid();
		
		$this->folder=$folder;
	}
	
	public function isFolder(){
		return $this->folder;
	}
	
	public function toString(){
		$s="Id: ".$this->id."<br/>";
		$s.="Parent Id: ".$this->parentId."<br/>";
		$s.="Name: ".$this->name."<br/>";
		
		$type=($this->isFolder())?"Folder":"File";
		$s.="Type: ".$type;
		return $s;
	}
	
	/************************************************/
	/*					 GET & SET				    */
	/************************************************/

	public function getName(){
		return $this->name;	
	}
	
	public function setName($name){
		$file = new File($this->getId());
		$filePath = explode('/', $file->path);
	
		$this->name = $name;
		$oldPath = $file->path;
		
		if(count($filePath)>1)
			$newPath = substr($file->path, 0, -strlen($filePath[count($filePath)-1])) . $name;
		else
			$newPath = $name;
		
		$file->path = $newPath;
		$file->save();
		
		$file->renameChildrenPath();
		$project = new Project($file->project_id);	
	
		if(!$_SESSION['workingCopyDir'][$project->id]){
			Common::getProject($project);
		}
	
		$sender = new Sender(FRONTAL_IP, FRONTAL_PORT);
			
		$dataUrl=explode(':', $project->server_url);
		$dataIp=$dataUrl[0];
		$dataPort=$dataUrl[1];
		
		$request = array('label' => 'rename',
				 'path' => $_SESSION['workingCopyDir'][$_SESSION['project_id']],
				 'target' => $oldPath,
				 'dest' => $newPath,
                 'httpPort' => '',
				 'servDataIp' => $dataIp,
				 'servDataPort' => $dataPort);
	
		$sender->setRequest($request);
		$sender->sendRequest();
	}
	
	public function getParentId(){
		return $this->parentId;	
	}
	
	public function setParentId($parentId){
		$this->parentId=$parentId;
	}
	
	public function getId(){
		return $this->id;	
	}
	
}

?>
