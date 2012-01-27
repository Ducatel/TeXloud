<?php
class Common{
	public static function isEmail($email){
		return preg_match('#^[\w.-]+@[\w.-]+\.[a-zA-Z]{2,6}$#', $email);
	}
}