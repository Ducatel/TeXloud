// Fonction d'initialisation
$(function() {

	// Rend les champs de text unselectable
	$('.folder').click(function() {
		this.onselectstart = function() {
			return false;
		};
		$(this).attr('unselectable', 'on');
		$(this).next().slideToggle('fast');
		return false;
	});

	$('.folder').dblclick(function() {
		return false;
	});

	// Gestion du clic droit pour le menu contextuel sur un dossier
	$(".folder").bind("contextmenu", function(e) {
		contextFolderMenu(e.pageX, e.pageY,$(this));
		return false;
	});
	
	// Gestion du clic droit pour le menu contextuel sur un fichier
	$(".file").bind("contextmenu", function(e) {
		contextFileMenu(e.pageX, e.pageY,$(this));
		return false;
	});

	// permet de fermer les menu avec le bouton echap
	$(document).keyup(function(event) {
		if (event.keyCode == 27)
				$('.menu').remove();
		return false;
	});

	// permet de faire un rename lors du double click
	$('.file,.folder').dblclick(
			function() {
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

});


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
	$(file).remove();
}

function removeFolder(folder){
	//TODO faire le remove folder dans l'objet PHP /!\ attention doit etre recursif

	$(folder).next().children().remove();
	$(folder).remove();
	
}

/***********************************************************************/
/**							Add File Methode 						  **/
/***********************************************************************/

function menuAddFile(posX, posY,folder){
	
	$('.menu').remove();
	var menu = $(
			'<div id="menuAddFile" class="menu">'+
			'<label for="nameAddfile" >Nom du fichier:</label><input type="text" id="nameAddfile" name="nameAddfile" />'+
			'<br/><button id="addFileButton">Ajouter le fichier</button>'+
			'</div>').attr('style',
			'position:fixed;top:' + posY + ';left:' + posX + ';');
	$('#tree').append(menu);
	
	
	$('#addFileButton').click(function(){
		var fatherId=$(folder).attr('id');
		var filename=$("#nameAddfile").val();
		addFile(fatherId,filename);
		$('#menuAddFile').remove();

	});
	
}

function addFile(fatherId,filename){
	//TODO faire l'ajout dans l'objet PHP
	
	var margin=$('#'+fatherId).next().children().first().attr('style');
	var newFile=$("<li class='file' style='"+margin+"'>"+filename+"</li>");
	$('#'+fatherId).next().prepend(newFile);
}