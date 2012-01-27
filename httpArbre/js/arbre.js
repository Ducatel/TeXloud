$(function() {
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

	$(".folder").bind("contextmenu", function(e) {
		contextMenu(e.pageX, e.pageY);
		return false;
	});

	$('.file').dblclick(function() {
		// faire le rename
		return false;
	});

	$("body").click(function() {
		$('#contextMenu').remove();
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