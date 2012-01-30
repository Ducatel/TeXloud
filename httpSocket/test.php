<?php

	
function endWith($string,$fin){
     return (substr($string, (strlen($fin) * -1)) === $fin);
}

$s="sdoijgemlkgnfmdkjnvsmkjdljfvmlhnvmfvnl+==\endTrame==+";
var_dump( endWith($s,"+==\endTrame==+"));


?>
