/**
 * Bug a corriger:
 * --> aprés ajout d'un fichier, si on veux supprimer ce fichier il supprime toutes la sous arbo
 * --> aprés ajout d'un dossier, impossible de créer des fichier dans ce dossier
 * --> aprés ajout d'un dossier, renommage de celui-ci impossible --> fait n'importe quoi
 * --> aprés ajout d'un dossier, renommage de celui-ci impossible --> fait n'importe quoi
 * --> meme probleme que le precedent avec les nouveaux fichiers
 */


// Fonction d'initialisation
$(function() {

	$('.folder').each(function(){
		addFolderAction($(this));
	});
	
	$('.file').each(function(){
		addFileAction($(this));
	});

	closeMenu();
	
});

/***********************************************************************/
/**									Actions							  **/
/***********************************************************************/

function addFolderAction(element){
	addDisableFocus(element);
	addFolderMenu(element);
	addRenameAction(element);
}

function addFileAction(element){
	addDisableFocus(element);
	addFileMenu(element);
	addRenameAction(element);
}

function addDisableFocus(element){
	$(element).click(function() {
		this.onselectstart = function() {
			return false;
		};
		$(this).attr('unselectable', 'on');
		$(this).next().slideToggle('fast');
		return false;
	});
}

function addFolderMenu(element){
	$(element).bind("contextmenu", function(e) {
		contextFolderMenu(e.pageX, e.pageY,$(this));
		return false;
	});
}

function addFileMenu(element){
	$(element).bind("contextmenu", function(e) {
		contextFileMenu(e.pageX, e.pageY,$(this));
		return false;
	});
}


function closeMenu(){
	$(document).keyup(function(event) {
		if (event.keyCode == 27)
				$('.menu').remove();
		return false;
	});
}


function addRenameAction(element){
	$(element).dblclick(function() {
		if ($('#rename').size() == 0) {
			var name = $(this).html();
			$(this).html('<input type="text" id="rename" value="' + name+'" /> ');
			$('#rename').focus();
		}
		
		$('#rename').keyup(function(event) {
			if (event.keyCode == 13) {
				rename($(this));
			}
			return false;
		
		});
		
		return false;
	});
}

function removeAction(element){
	$(element).unbind('click');
	$(element).unbind('dblclick');
	$(element).unbind('contextmenu');
}



/***********************************************************************/
/**							Menu contextuel Methode					  **/
/***********************************************************************/

/**
 * Fonction qui gere le menu contextuel
 * @param posX: position en X de la div
 * @param posY: position en Y de la div
 * @param folder: dossier qui a declencher l'action
 */
function contextFolderMenu(posX, posY,folder) {

	$('.menu').remove();

	var menu = $(
			'<div id="contextFolderMenu" class="menu">' + '<ul>'
					+ '<li id="addFile">Cr&eacute;er un fichier</li>'
					+ '<li id="addFolder">Cr&eacute;er un dossier</li>'
					+ '<li id="removeFolder">Supprimer un dossier</li>'
					+ '</ul>' + '</div>').attr('style',
			'position:fixed;top:' + posY + ';left:' + posX + ';');

	$('#tree').append(menu);
	
	
	// Ajout de l'action permettant de créer de nouveau fichier
	$('#addFile').click(function(){
		$('.menu').remove();
		menuAddFile(posX, posY,folder);
	});
	
	// Ajout de l'action permettant de créer de supprimer un dossier
	$('#removeFolder').click(function(){
		$('.menu').remove();
		removeFolder(folder);
	});
	
	// Ajout de l'action permettant de créer de supprimer un dossier
	$('#addFolder').click(function(){
		//TODO faire le addFolder
		$('.menu').remove();
		menuAddFolder(posX, posY,folder);
	});

}

function contextFileMenu(posX, posY,file) {
	$('.menu').remove();
	
	var menu = $(
			'<div id="contextFileMenu" class="menu">' + '<ul>'
					+ '<li id="removeFile">Supprimer le fichier</li>'
					+ '</ul>' + '</div>').attr('style',
			'position:fixed;top:' + posY + ';left:' + posX + ';');

	$('#tree').append(menu);
	
	
	// Ajout de l'action permettant de créer de nouveau fichier
	$('#removeFile').click(function(){
		$('.menu').remove();
		removeFile(file);
	});

}

/***********************************************************************/
/**							rename  Methode 						  **/
/***********************************************************************/

function rename(entry){
	//TODO faire la mise a jour (rename) de l'objet PHP
	var newName = $(entry).val();
	$($(entry).parent()).html(newName);
}

/***********************************************************************/
/**								remove Methode 						  **/
/***********************************************************************/

function removeFile(file){
	//TODO faire le remove file dans l'objet PHP
	removeAction($(file));
	$(file).remove();
}

function removeFolder(folder){
	//TODO faire le remove folder dans l'objet PHP /!\ attention doit etre recursif

	removeAction($(folder));
	$(folder).next().children().remove();
	$(folder).remove();
	
}

/***********************************************************************/
/**								Add Methode 						  **/
/***********************************************************************/
function menuAddFolder(posX, posY,folder){
	
	$('.menu').remove();
	var menu = $(
			'<div id="menuAddFile" class="menu">'+
			'<label for="nameAddfolder" >Nom du dossier:</label><input type="text" id="nameAddfolder" name="nameAddfolder" />'+
			'<br/><button id="addFileButton">Ajouter le fichier</button>'+
			'</div>').attr('style',
			'position:fixed;top:' + posY + ';left:' + posX + ';');
	$('#tree').append(menu);
	
	
	$('#addFileButton').click(function(){
		var fatherId=$(folder).attr('id');
		var foldername=$("#nameAddfolder").val();
		addFolder(fatherId,foldername);
		$('#menuAddFile').remove();

	});
	
}
function menuAddFile(posX, posY,folder){
	
	$('.menu').remove();
	var menu = $(
			'<div id="menuAddFile" class="menu">'+
			'<label for="nameAddFile" >Nom du fichier:</label><input type="text" id="nameAddFile" name="nameAddfile" />'+
			'<br/><button id="addFileButton">Ajouter le fichier</button>'+
			'</div>').attr('style',
			'position:fixed;top:' + posY + ';left:' + posX + ';');
	$('#tree').append(menu);
	
	
	$('#addFileButton').click(function(){
		var fatherId=$(folder).attr('id');
		var filename=$("#nameAddFile").val();
		addFile(fatherId,filename);
		$('#menuAddFile').remove();

	});
	
}

function addFolder(fatherId,foldername){
	//TODO faire l'ajout dans l'objet PHP
	
	var margin=$('#'+fatherId).next().children().first().attr('style');
	var newFolder=$("<li class='folder' style='"+margin+"'>"+foldername+"</li>");
	$('#'+fatherId).next().prepend(newFolder);
}

function addFile(fatherId,filename){
	//TODO faire l'ajout dans l'objet PHP
	
	var margin=$('#'+fatherId).next().children().first().attr('style');
	var newFile=$("<li class='file' style='"+margin+"'>"+filename+"</li>");
	var file=$('#'+fatherId).next().prepend(newFile);
	addFileAction($(file));
	
}


function menuAddFolder(posX, posY,folder){
	$('.menu').remove();
	var menu = $(
			'<div id="menuAddFolder" class="menu">'+
			'<label for="nameAddFolder" >Nom du Dossier:</label><input type="text" id="nameAddFolder" name="nameAddfile" />'+
			'Position relative au dossier: "'+$(folder).html()+'"<br/><select id="levelFolder"><option value="same">M&ecirc;me niveau</option><option value="child">Fils</option></select> '+
			'<br/><button id="addFolderButton">Ajouter le dossier</button>'+
			'</div>').attr('style',
			'position:fixed;top:' + posY + ';left:' + posX + ';');
	$('#tree').append(menu);
	
	$('#addFolderButton').click(function(){
		
		var folderName=$("#nameAddFolder").val();
		addFolder(folderName,$('#levelFolder option:selected').val(),folder);
		$('#menuAddFolder').remove();

	});
}

function addFolder(folderName,level,folder){
	
	var fatherId="";
	var margin="";
	if(level=="child"){
		fatherId=$(folder).attr('id');
		margin=$(folder).next().children().first().attr('style');

	}
	else if(level=="same"){
		fatherId=$(folder).prev().parent().prev().attr('id');
		margin=$(folder).attr('style');
	}
	
	console.log($('#'+fatherId));

	var newFolder=$("<li class='folder' style='"+margin+"'>"+folderName+"</li>");
	newFolder=$('#'+fatherId).next().prepend(newFolder);
	addFolderAction($(newFolder));
}