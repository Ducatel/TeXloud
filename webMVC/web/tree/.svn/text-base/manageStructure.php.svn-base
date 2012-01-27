<?php
/********************************************
*
*	Filename:	manageStructure.php
*	Author:		Ahmet Oguz Mermerkaya
*	E-mail:		ahmetmermerkaya@hotmail.com
*	Begin:		Sunday, July 6, 2008  20:21
*
*********************************************/

if (isset($_REQUEST['action']) && !empty($_REQUEST['action'])) 
{
	$action = $_REQUEST['action'];	
}
else 
{
	die(FAILED);
}
define("IN_PHP", true);

require_once("common.php");

$out = NULL;

switch($action)
{
	case "insertElement":
	{	
		/**
		 * insert new element
		 */	
		if ( ( isset($_POST['name']) === true && $_POST['name'] != NULL )  &&
		     ( isset($_POST['ownerEl']) === true && $_POST['ownerEl'] != NULL )  &&
	         ( isset($_POST['slave']) === true && $_POST['slave'] != NULL )		   
		   )
		{				
			$ownerEl = checkVariable($_POST['ownerEl']);
			$slave = (int) checkVariable($_POST['slave']);
			$name = checkVariable($_POST['name']);			
		  
			$out = $treeManager->insertElement($name, $ownerEl, $slave);						
		}
		else {
			$out = FAILED; 
		}
	}
	break;	
	case  "getElementList":  
	{
		/**
		 * getting element list
		 */
        if( isset($_REQUEST['ownerEl']) == true && $_REQUEST['ownerEl'] != NULL ) {  	
			$ownerEl = checkVariable($_REQUEST['ownerEl']); 
		}
		else {
			$ownerEl = 0;
		}
  		$out = $treeManager->getElementList($ownerEl, $_SERVER['PHP_SELF']);
    }
	break;		
    case "updateElementName":
    {
    	/**
    	 * Changing element name
    	 */
		if (isset($_POST['name']) && !empty($_POST['name']) &&
		    isset($_POST['elementId']) && !empty($_POST['elementId']) &&
		    isset($_POST['ownerEl']) && $_POST['ownerEl'] != NULL)
		{			
			$name = checkVariable($_POST['name']);
			$elementId = checkVariable($_POST['elementId']); 			
			$ownerEl = checkVariable($_POST['ownerEl']);
			$out = $treeManager->updateElementName($name, $elementId, $ownerEl);
		}                         
		else {
			$out = FAILED;	
		}
    }    
    break;

	case "deleteElement":
	{
		/**
		 * deleting an element and elements under it if exists
		 */
		if (isset($_POST['elementId']) && !empty($_POST['elementId']) &&
		    isset($_POST['ownerEl']) && $_POST['ownerEl'] != NULL)
		{
        	$elementId =  checkVariable($_POST['elementId']);	 
        	$ownerEl = checkVariable($_POST['ownerEl']); 			 
        	$index = 0;
			$out = $treeManager->deleteElement($elementId, &$index, $ownerEl);             
	    }
        else {
			$out = FAILED;	
		}
	}
	break;
	case "changeOrder":
	{		
		/**
		 * Change the order of an element
		 */
		if ((isset($_POST['elementId']) && $_POST['elementId'] != NULL) &&
			(isset($_POST['destOwnerEl']) && $_POST['destOwnerEl'] != NULL) &&
			(isset($_POST['position']) && $_POST['position'] != NULL) &&
			(isset($_POST['oldOwnerEl']) && $_POST['oldOwnerEl'] != NULL) 
			)			
		{			
			$oldOwnerEl = checkVariable($_POST['oldOwnerEl']);
			$elementId = checkVariable($_POST['elementId']);
			$destOwnerEl = checkVariable($_POST['destOwnerEl']);
			$position = (int) checkVariable($_POST['position']);
		
			$out = $treeManager->changeOrder($elementId, $oldOwnerEl, $destOwnerEl, $position);
		}			
		else{		
			$out = FAILED;
		}			
	}
	break;		
    default:
    	/**
    	 * if an unsupported action is requested, reply it with FAILED
    	 */
      	$out = FAILED;
	break;
}
echo $out;
