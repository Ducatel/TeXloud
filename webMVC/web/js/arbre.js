/**
 * Bug a corriger:
 * --> apres ajout d'un fichier, si on veux supprimer ce fichier il supprime toutes la sous arbo
 * --> apres ajout d'un dossier, impossible de creer des fichier dans ce dossier
 * --> apres ajout d'un dossier, renommage de celui-ci impossible --> fait n'importe quoi
 * --> apres ajout d'un dossier, renommage de celui-ci impossible --> fait n'importe quoi
 * --> meme probleme que le precedent avec les nouveaux fichiers
 */


// Fonction d'initialisation

CURRENT_FILE_ID = null;
FILES_CONTENT = {};

$(function(){
	$('.folder').each(function(){
		addFolderAction($(this));
	});
	
	$('.file').each(function(){
		addFileAction($(this));
	});
	
	$('.root').bind("contextmenu", function(e) {
		contextProjectMenu(e.pageX, e.pageY);
		return false;
	});

	closeMenu();
	
});

/***********************************************************************/
/**									Actions							  **/
/***********************************************************************/

function addFolderAction(element){
	addDisableFolderFocus(element);
	addFolderMenu(element);
	
}

function addFileAction(element){
	addDisableFileFocus(element);
	addFileMenu(element);
	
	$(element).click(function(){
		
		CURRENT_FILE_ID = $(this).attr('id');
		
		$('.file').css('color','black');
		$(this).css('color','red');	

		if(!CURRENT_FILE_ID || !FILES_CONTENT[CURRENT_FILE_ID]){
			$.post('/ajax/getFile/',
				   {'id' : $(this).attr('id')},
				   function(content){
					editAreaLoader.setValue('codelatex', content);
					editAreaLoader.execCommand('codelatex', 'set_editable', true);
					$('#frame_codelatex').contents().find('#result').css('background-color', 'white');
					//console.log(editAreaLoader.getValue('codelatex'));
					FILES_CONTENT[CURRENT_FILE_ID] = editAreaLoader.getValue('codelatex');
					//$('textarea#textarea').val(content);

					if($('#pending').filter(':hidden').length && $('#synchronized').filter(':hidden').length){
						$('#synchronized').show();
					}
			});
		}
		else
			editAreaLoader.setValue('codelatex', FILES_CONTENT[CURRENT_FILE_ID]);
	});
}

function addDisableFolderFocus(element){
	$(element).click(function() {
		this.onselectstart = function() {
			return false;
		};
		$(this).attr('unselectable', 'on');
		$(this).next().slideToggle('fast');
		return false;
	});
}

function addDisableFileFocus(element){
	$(element).click(function() {
		this.onselectstart = function() {
			return false;
		};
		$(this).attr('unselectable', 'on');
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
	
	
		if ($('#rename').size() == 0) {
			var name = $(element).html();
			console.log('nom '+name);
			$(element).html('<input type="text" id="rename" value="' + name+'" /> ');
			$('#rename').focus();
		}
		
		$('#rename').keyup(function(event) {
			if (event.keyCode == 13) {
				rename($(this));
			}
			return false;
		
		});
		
		return false;
	
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
	
	var folderEdit = "";
	
	if($(folder).parent().parent().children().first().attr('class')!="root")
		folderEdit = '<li id="editeFolder">Editer un dossier</li>';
	else
		folderEdit = '';

	var menu = $(
			'<div id="contextFolderMenu" class="menu">' + '<ul>'
					+ '<li id="addFile">Cr&eacute;er un fichier</li>'
					+ '<li id="addFolder">Cr&eacute;er un dossier</li>'
					+ folderEdit
					+ '<li id="removeFolder">Supprimer un dossier</li>'
					+ '</ul>' + '</div>').attr('style',
			'position:fixed;top: 0; left: 0;margin-left: ' + posX + 'px; margin-top: ' + posY + 'px;');

	$('#tree').append(menu);
	
	
	// Ajout de l'action permettant de creer de nouveau fichier
	$('#addFile').click(function(){
		$('.menu').remove();
		menuAddFile(posX, posY,folder);
	});
	
	// Ajout de l'action permettant de creer de supprimer un dossier
	$('#removeFolder').click(function(){
		$('.menu').remove();
		removeFolder(folder);
	});
	
	// Ajout de l'action permettant de creer de supprimer un dossier
	$('#addFolder').click(function(){
		$('.menu').remove();
		menuAddFolder(posX, posY,folder);
	});
	
	// Ajout de l'action permettant d'editer un dossier
	$('#editeFolder').click(function(){
		$('.menu').remove();
		addRenameAction(folder);
	});

}

function contextFileMenu(posX, posY,file) {
	$('.menu').remove();
	
	var menu = $(
			'<div id="contextFileMenu" class="menu">' + '<ul>'
					+ '<li id="removeFile">Supprimer le fichier</li>'
					+ '<li id="editeFile">Editer le fichier</li>'
					+ '</ul>' + '</div>').attr('style',
				'position:fixed;top: 0; left: 0;margin-left: ' + posX + 'px; margin-top: ' + posY + 'px;');

	$('#tree').append(menu);
	
	
	// Ajout de l'action permettant de supprimer un fichier
	$('#removeFile').click(function(){
		$('.menu').remove();
		removeFile(file);
	});
	
	// Ajout de l'action permettant d'editer un fichier
	$('#editeFile').click(function(){
		$('.menu').remove();
		addRenameAction(file);
	});

}

function contextProjectMenu(posX, posY){
$('.menu').remove();
	
	var menu = $(
			'<div id="contextProjectMenu" class="menu">' + '<ul>'
					+ '<li id="addProject">Cr&eacute;er un projet</li>'
					+ '</ul>' + '</div>').attr('style',
			'position:fixed;top: 0; left: 0;margin-left: ' + posX + 'px; margin-top: ' + posY + 'px;');

	$('#tree').append(menu);
	
	
	// Ajout de l'action permettant de creer de nouveau fichier
	$('#addProject').click(function(){
		$('.menu').remove();
		menuAddProject(posX, posY);
	});
}

/***********************************************************************/
/**							rename  Methode 						  **/
/***********************************************************************/

function rename(entry){
	$.ajax({
        url:"/tree/renameElement",
  	  	type: 'POST',
        data:{
                'id':$($(entry).parent()).attr('id'),
                'name':$(entry).val(),
        },
        success:function(data){
        	var newName = $(entry).val();
        	$($(entry).parent()).html(newName);
        }
	});


}

/***********************************************************************/
/**								remove Methode 						  **/
/***********************************************************************/

function removeFile(file){	
	$.ajax({
        url:"/tree/removeFile",
  	  	type: 'POST',
        data:{
                'id':$(file).attr('id'),
        },
        success:function(data){
		if($(file).attr('id') == CURRENT_FILE_ID){
			delete(FILES_CONTENT[CURRENT_FILE_ID]);
			CURRENT_FILE_ID = null;
			editAreaLoader.setValue('codelatex', '');
			editAreaLoader.execCommand('codelatex', 'set_editable', false);
			$('#frame_codelatex').contents().find('#result').css('background-color', '#ffede0');
		}
	
        	
		removeAction($(file));
        	$(file).remove();

        }
	});

}

function removeFolder(folder){
	//on regarde si le dossier est un projet ou non
	if($(folder).parent().parent().children().first().attr('class')=="root"){
		$.ajax({
	        url:"/tree/removeProject",
	  	  	type: 'POST',
	        data:{
	                'id':$(folder).attr('id'),
	        },
	        success:function(data){
	        	removeAction($(folder));

			$(folder).next().children().each(function(){
				if($(this).attr('id') == CURRENT_FILE_ID){
				        delete(FILES_CONTENT[CURRENT_FILE_ID]);
				        CURRENT_FILE_ID = null;
				        editAreaLoader.setValue('codelatex', '');
				        editAreaLoader.execCommand('codelatex', 'set_editable', false);
				        $('#frame_codelatex').contents().find('#result').css('background-color', '#ffede0');
				}

				$(this).remove();
			});

	        	$(folder).next().remove();
	        	$(folder).remove();
	        }
		});
	}
	else{
		$.ajax({
	        url:"/tree/removeFolder",
	  	  	type: 'POST',
	        data:{
	                'id':$(folder).attr('id'),
	        },
	        success:function(data){
	        	removeAction($(folder));
			
			$(folder).next().children().each(function(){
				if($(this).attr('id') == CURRENT_FILE_ID){
				        delete(FILES_CONTENT[CURRENT_FILE_ID]);
				        CURRENT_FILE_ID = null;
				        editAreaLoader.setValue('codelatex', '');
				        editAreaLoader.execCommand('codelatex', 'set_editable', false);
				        $('#frame_codelatex').contents().find('#result').css('background-color', '#ffede0');
				}

				$(this).remove();
			});

	        	$(folder).next().remove();
	        	$(folder).remove();
	        }
		});
	}
}

/***********************************************************************/
/**								Add Methode 						  **/
/***********************************************************************/

function menuAddFile(posX, posY,folder){
	
	$('.menu').remove();
	var menu = $(
			'<div id="menuAddFile" class="menu">'+
			'<label for="nameAddFile" >Nom du fichier:</label><input type="text" id="nameAddFile" name="nameAddfile" />'+
			'<br/><button id="addFileButton">Ajouter le fichier</button>'+
			'</div>').attr('style',
			'position:fixed;top: 0; left: 0;margin-left: ' + posX + 'px; margin-top: ' + posY + 'px;');
	$('#tree').append(menu);
	
	$('#addFileButton').click(function(){
		var fatherId=$(folder).attr('id');
		var filename=$("#nameAddFile").val();

		addFile(fatherId,filename);
		$('#menuAddFile').remove();

	});
	
	$("#nameAddFile").keyup(function(e) {
	  if(e.keyCode == 13){
	    
		var fatherId=$(folder).attr('id');
		var filename=$("#nameAddFile").val();
		addFile(fatherId,filename);
		$('#menuAddFile').remove();
	     
	  }
	  
	});
	
}

function addFile(fatherId,filename){	
	
	margin=$('#'+fatherId).next().children().first().css('margin-left');
	if(margin==undefined){
		margin=$('#'+fatherId).css('margin-left');
		margin=stripPx(margin);
		margin+=20;
	}
	else{
		margin=stripPx(margin);
	}
	
	
	$.ajax({
        url:"/tree/addFile",
  	  	type: 'POST',
        data:{
                'name':filename,
                'idParent':fatherId,
        },
        success:function(data){
        	var newFile=$("<li class='file' id='"+data+"' style='margin-left:"+margin+"px;'>"+filename+"</li>");
        	$('#'+fatherId).next().prepend(newFile);
        	addFileAction('#'+data);
        }
	});
	
}


function menuAddFolder(posX, posY,folder){
	$('.menu').remove();
	var menu = $(
			'<div id="menuAddFolder" class="menu">'+
			'<label for="nameAddFolder" >Nom du Dossier:</label><input type="text" id="nameAddFolder" name="nameAddfile" />'+
			'<br/><button id="addFolderButton">Ajouter le dossier</button>'+
			'</div>').attr('style',
			'position:fixed;top: 0; left: 0;margin-left: ' + posX + 'px; margin-top: ' + posY + 'px;');
	$('#tree').append(menu);
	
	$('#addFolderButton').click(function(){
		
		var folderName=$("#nameAddFolder").val();
		addFolder(folderName,folder);
		$('#menuAddFolder').remove();

	});
	
	$("#nameAddFolder").keyup(function(e){
		
		var folderName=$("#nameAddFolder").val();
		if(e.keyCode == 13){
		addFolder(folderName,folder);
		$('#menuAddFolder').remove();
		  
		}

	});
}

function addFolder(folderName,folder){
	
	fatherId=$(folder).attr('id');
	
	margin=$(folder).next().children().first().css('margin-left');
	if(margin==undefined){
		margin=$(folder).css('margin-left');
		margin=stripPx(margin);
		margin+=20;
	}
	else{
		margin=stripPx(margin);
	}
	
	$.ajax({
        url:"/tree/addFolder",
  	  	type: 'POST',
        data:{
                'name':folderName,
                'idParent':fatherId,
        },
        success:function(data){
        	var newFolder=$("<li class='folder' id='"+data+"' style='margin-left:"+margin+"px;'>"+folderName+"</li>\n<ul class='ulArbre'>\n</ul>\n");
        	newFolder=$('#'+fatherId).next().append(newFolder);
        	addFolderAction('#'+data);
        }
	});
}

function menuAddProject(posX, posY){
	$('.menu').remove();
	var menu = $(
			'<div id="menuAddProject" class="menu">'+
			'<label for="nameAddProject" >Nom du Projet:</label><input type="text" id="nameAddProject" name="nameAddProject" />'+
			'<br/><button id="addProjectButton">Cr&eacute;er le projet</button>'+
			'</div>').attr('style',
			'position:fixed;top: 0; left: 0;margin-left: ' + posX + 'px; margin-top: ' + posY + 'px;');
	$('#tree').append(menu);
	
	$('#addProjectButton').click(function(){
		
		var projectName=$("#nameAddProject").val();
		addProject(projectName);
		$('#menuAddProject').remove();
	});
	
	$("#nameAddProject").keyup(function(e){
		
		var projectName=$("#nameAddProject").val();
		if(e.keyCode == 13){
		    addProject(projectName);	
		    $('#menuAddProject').remove();
		}

	});
}

function addProject(projectName){
	$.ajax({
        url:"/tree/addProject",
  	  	type: 'POST',
        data:{
                'name':projectName,
        },
        success:function(data){
        	var newProject=$("<li class='folder' id='"+data+"' style='margin-left:20px;'>"+projectName+"</li>\n<ul class='ulArbre'>\n</ul>\n");
        	newProject=$('.root').next().append(newProject);
        	addFolderAction('#'+data);
        }
	});
}

/*****************************************************************************************************/

function stripPx( value ) {
    if( value == "" ) return 0;
    return parseFloat( value.substring( 0, value.length - 2 ) );
}  
