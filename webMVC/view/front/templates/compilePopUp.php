<div id="popup_compile">
	<form id="form_compile">
		Nom du projet: 
		<select id="project_id" name="project_id">
			<option value=""></option>
			<?php
			foreach($var->projects as $p){
				echo '<option value="' . $p->id . '">' . $p->name . '</option>';
			}
			?>
		</select><br />
	
		<div id="root_file_wrapper" style="display: none;">
		</div>
		<br />
		<input type="submit" value="Compiler" style="display: none;" id="submitCompile" />
		<button id="cancelPopup">Annuler</button>
	</form>
</div>
