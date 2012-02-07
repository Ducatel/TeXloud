<br />
<?php
if(count($var->files)):
?> 
Fichier principal: 
<select id="root_file_id" name="root_file_id">
	<option value=""></option>
	<?php
		$prevGrp = null;
		
		foreach($var->files as $f){
			$tree = explode('/', $f->path);
			
			if(count($tree)>1 && (!$prevGrp || Common::startsWith($f->path, $prevGrp))){

				unset($tree[count($tree)-1]);
					
				$prevGrp = implode('/', $tree);
				echo '<optgroup label="/' . $prevGrp . '">';
			}

			$label = ($prevGrp)?preg_replace('/'.$prevGrp.'\//', '', $f->path):$f->path;

			echo '<option value="' . $f->id . '">' . $label . '</option>';
		}
		
		if($prevGrp)
			echo '</optgroup>';
	?>
</select>
<?php
else:
	echo 'Aucun fichier disponible à la compilation';
endif;
?>
