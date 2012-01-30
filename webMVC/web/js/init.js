// JavaScript Document
/********************************************
* 	Filename:	js/init.js
*	Author:		Ahmet Oguz Mermerkaya
*	E-mail:		ahmetmermerkaya@hotmail.com
*	Begin:		Sunday, April 20, 2008  16:22
***********************************************/


/**
 * initialization script 
 */
var langManager = new languageManager();
//de for german
//en for english
//tr for turkish
langManager.load("en");  

$(document).ready(function() {
	$(document).ready(function(){
		editAreaLoader.init({
			id : 'codelatex',
			syntax: 'php',
			word_wrap: true,
			start_highlight: true,
			language: 'fr',
			toolbar: 'undo,redo',
		});
	});
});