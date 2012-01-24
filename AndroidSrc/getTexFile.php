<?php
	
	$handler = fopen("https://github.com/Ducatel/TeXloud/raw/master/cahierDesCharges/cahierDesCharges.tex", "r"); 
	$contents = '';
	 
	if($handler) 
		while(!feof($handler)) 
			$contents .= fread($handler, 8192);
		 
	fclose($handler); 
	$handlew = fopen("/home/adrien/Bureau/cahierDesCharges.tex", "w"); 
	fwrite($handlew, $contents); 
	fclose($handlew);
	
	echo $contents;
?>