<?php
class GroupUser extends ObjectAssoc {		
	public $user_id, $group_id, $status, $permission_id;
		
	public function __construct() {
		parent::__construct();
	}
}
?>
