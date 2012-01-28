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

	// Gestion du clic droit pour le menu contextuel
	$(".folder").bind("contextmenu", function(e) {
		contextMenu(e.pageX, e.pageY);
		return false;
	});

	// permet de fermer le menu contextuel avec le bouton echap
	$(document).keyup(function(event) {
		if (event.keyCode == 27)
			if ($('#renameFile').size() != 0)
				$('#contextMenu').remove();
		return false;
	});

	// permet de faire un rename lors du double click
	$('.file,.folder').dblclick(
			function() {
				if ($('#rename').size() == 0) {
					var name = $(this).html();
					$(this).html(
							'<input type="text" id="rename" value="' + name
									+ '" /> ');
					$('#rename').focus();
				}

				$('#rename').keyup(function(event) {
					if (event.keyCode == 13) {
						var newName = $(this).val();
						$($(this).parent()).html(newName);
					}
					return false;

				});

				return false;
			});

});

function contextMenu(posX, posY) {

	$('#contextMenu').remove();

	var menu = $(
			'<div id="contextMenu">' + '<ul>'
					+ '<li id="addFile">Cr&eacute;er un fichier</li>'
					+ '<li id="addFolder">Cr&eacute;er un dossier</li>'
					+ '<li id="removeFile">Supprimer un fichier</li>'
					+ '<li id="removeFolder">Supprimer un dossier</li>'
					+ '</ul>' + '</div>').attr('style',
			'position:fixed;top:' + posY + ';left:' + posX + ';');

	$('#tree').append(menu);

}