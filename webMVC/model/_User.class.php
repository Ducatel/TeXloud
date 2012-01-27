<?php
class User extends object {		
	public $id, $firstname, $lastname, $username, $gender, $date_of_birth, $address, $zip, $city, $country, $email, $salt, $password;
  public function __construct($id=0) {
    parent::__construct($id);
  }	
  
	static function isLogged() {
		if(isset($_SESSION['user_id']) && $_SESSION['user_id'] != null) {
			return new user($_SESSION['user_id']);
		}
	}
	
	static function login($email, $password) {
		$query = new Query('unique_select', "SELECT id, email, salt, password FROM user WHERE email='$email'");
		$result = $query->result;
		
		if($result && sha1($result->salt.$password) == $result->password)
			return $result->id;
		else 
			return false;
	}
	
	static function getCurrentUser() {
		if(isset($_SESSION['user_id']) && $_SESSION['user_id'] != null) {
			return new user($_SESSION['user_id']);
		}
		else
			return false;
	}
	
	public function getRatings($type = null, $limit = 40){
		if($type)
			$rq = 'user_id = ' . $this->id . ' AND table_name = \'' . $type . '\''; 
		else
			$rq = 'user_id = ' . $this->id;
			
		return Rating::getAll(0, $limit, $rq, 'table_name ASC');
	}
	
	public function getAvatar(){
		$q = new Query('unique_select', 'SELECT id FROM avatar WHERE user_id = ' . $this->id);
		
		if($q->result->id)
			return new Avatar($q->result->id);
		else
			return false;
	}
	
	public function setLearningSociety($learning_society_name){
		$q = new Query('unique_select', 'SELECT id FROM learning_society WHERE label = \'' . addslashes($learning_society_name) . '\'');

		if($q->result->id){
			$this->learning_society_id = $q->result->id;
		}
		else{
			$ls = new LearningSociety();
			$ls->label = $learning_society_name;
			$ls->save();
			
			$this->learning_society_id = $ls->id;
		}
	}
	
	public function updatePainInterest($selected_pain_interest_ids){
		$notInString = !empty($selected_pain_interest_ids)?implode(', ', $selected_pain_interest_ids):'\'\'';
		
		$q = new Query('delete', 'DELETE FROM user_pain_interest WHERE user_id = ' . $this->id . ' AND pain_interest_id NOT IN (' . $notInString . ')');
		
		$exist_q = new Query('select', 'SELECT pain_interest_id AS id FROM user_pain_interest WHERE user_id = ' . $this->id);
		$existing_indexes = array();
		
		foreach($exist_q->result as $piid)
			$existing_indexes[] = $ppid->id;
					
		$insertStatement = 'INSERT INTO user_pain_interest VALUES ';
		
		$execute = false;
		
		foreach($selected_pain_interest_ids as $spiid){
			if(!in_array($sppid, $existing_indexes)){
				$insertStatement .= '(' . $this->id . ', ' . intval($spiid) . '), ';
				$execute = true;
			}
		}
		
		if($execute){
			$insertStatement = trim($insertStatement, ', ');
			$q = new Query('insert', $insertStatement);
		}
	}
	
	public function getMailPreferenceValue($column_name){
		$q = new Query('unique_select', 'SELECT value FROM mail_preference mp, user_mail_preference ump WHERE mp.id = ump.mail_preference_id AND user_id = ' . $this->id . ' AND mp.type = \'' . addslashes($column_name) . '\'');
		
		if($q->result->value){
			return true;
		}
		else{
			return false;
		}
	}
	
	public function setMailPreference($column_name, $value){
		$escaped_column_name = addslashes($column_name);
		
		$q = new Query('unique_select', 'SELECT id FROM mail_preference WHERE type = \'' . $escaped_column_name . '\'');
		$mpid = $q->result->id;
		
		if($mpid){
			$q = new Query('unique_select', 'SELECT value FROM user_mail_preference WHERE user_id = ' . $this->id . ' AND mail_preference_id = ' . $mpid);
			
			if($q->result->value!==null){
				$q = new Query('update', 'UPDATE user_mail_preference SET value = ' . intval($value) . ' WHERE mail_preference_id = ' . $mpid . ' AND user_id = ' . $this->id);
				
				return $q->result?true:false;
			}
			else{
				$ump = new UserMailPreference();
				$ump->mail_preference_id = $mpid;
				$ump->user_id = $this->id;
				$ump->value = intval($value);
				$ump->save();
			}
		}
	}
	
	public function getPainInterestIds(){
		$q = new Query('select', 'SELECT pain_interest_id FROM user_pain_interest WHERE user_id = ' . $this->id);
		
		$results = array();
		
		foreach($q->result as $r)
			$results[] = $r->pain_interest_id;
		
		return $results;
	}
	
	public function getFavourites($type) {
		
		$q = new Query('select', 'SELECT entry_id FROM favourite WHERE user_id='.$this->id);
		$results = array();
		
		foreach($q->result as $r)
			$results[] = new staticPage($r->entry_id);
		
		return $results;
	}
}
?>
