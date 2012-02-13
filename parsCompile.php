<?php

$pars=simplexml_load_file('Exemplelog.xml');

  if($pars){

	if(!empty($pars->warning)){
	     echo "<b> Warning </b></br>";
	     foreach($pars->warning as $warning){
		    echo "<ul>";
		    $type = (string)$warning->type;
		    $message = (string)$warning->message;
		    $tabLigne='';
		    foreach($warning->line as $ligne){
				$line = (string)$ligne;
				
				$tabLigne.= " ".$line;
				//var_dump('voila '.$tabLigne);
						
		  }

		   // $line = (string)$warning->line;
		    echo "Lignes :".$tabLigne."<ul> message : ".$message."</ul></br>";
		    $tabLigne="";
		    echo "</ul>"; 


	     }

	}


	if(!empty($pars->error)){
	     echo "</br><b> Error </b></br>";
	     foreach($pars->error as $error){
		    echo "<ul>";
		    $type = (string)$warning->type;
		    $message = (string)$warning->message;
		    $tabLigne='';
		    foreach($warning->line as $ligne){
				$line = (string)$ligne;
				
				$tabLigne.= " ".$line;
				//var_dump('voila '.$tabLigne);
						
		  }

		   // $line = (string)$warning->line;
		    echo "Lignes :".$tabLigne."<ul> message : ".$message."</ul></br>";
		    //echo "type :".$type.", message :".$message." line: ".$tabLigne."</br>";
		    $tabLigne="";
		    echo "</ul>"; 


	     }

	}
	    

  }


?>