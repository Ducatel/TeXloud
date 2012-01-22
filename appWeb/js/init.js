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

var treeOps = new TreeOperations();
$(document).ready(function() {
	
	  
	


            // Save Form
          /*  $(window).keypress(function(event) {
                if (!(event.which == 115 && event.ctrlKey) && !(event.which == 19)) return true;
                $("#container form").submit();
                event.preventDefault();
                return false;
            });*/

   
    

		
	// binding menu functions
	$('#myMenu1 .addDoc').click(function()  {  treeOps.addElementReq(); });									   						    
	$('#myMenu1 .addFolder').click(function()  {  treeOps.addElementReq(true); });	
	$('#myMenu1 .edit, #myMenu2 .edit').click(function() {  treeOps.updateElementNameReq(); });
	$('#myMenu1 .delete, #myMenu2 .delete').click(function() {  treeOps.deleteElementReq(); });
	$('#myMenu1 .expandAll').click(function (){ treeOps.expandAll($('.simpleTree > .root > ul')); });
	$('#myMenu1 .collapseAll').click(function (){ treeOps.collapseAll(); });
	
	
	// setting menu texts 
	$('#myMenu1 .addDoc').append(langManager.addDocMenu);
	$('#myMenu1 .addFolder').append(langManager.addFolderMenu);
	$('#myMenu1 .edit, #myMenu2 .edit').append(langManager.editMenu);
	$('#myMenu1 .delete, #myMenu2 .delete').append(langManager.deleteMenu);
	$('#myMenu1 .expandAll').append(langManager.expandAll);
	$('#myMenu1 .collapseAll').append(langManager.collapseAll);
	
		
	// initialization of tree
	simpleTree = $('.simpleTree').simpleTree({
	  
	   
		autoclose: false,
		/**
		 * restore tree state according the cookies it stored.
		 */
		restoreTreeState: true,
		
		/**
		 * Callback function is called when one item is clicked
		 */	
		afterClick:function(node){
				var temp=$('span:first', node).parent().attr('id');
				/*$("#save").click(function(){
				  
				  saveFile(temp);
				    });*/
				  shortcut.add("Ctrl+S",function() {
				       saveFile(temp);
				    });
				//nameFile(temp);
				readFile(temp);
				//alert($('span:first',node).parent().attr('id').toggleClass('doc'));
				
		},
		/**
		 * Callback function is called when one item is double-clicked
		 */	
		afterDblClick:function(node){
			//alert($('span:first',node).text() + " double clickedfile");	
			
		},
		afterMove:function(destination, source, pos) {
		//	alert("destination-"+destination.attr('id')+" source-"+source.attr('id')+" pos-"+pos);	
			if (dragOperation == true) 
			{				
				
				var params = "action=changeOrder&elementId="+source.attr('id')+"&destOwnerEl="+destination.attr('id')+
							 "&position="+pos + "&oldOwnerEl=" + simpleTree.get(0).ownerElOfDraggingItem;
				
				treeOps.ajaxReq(params, structureManagerURL, null, function(result)
				{						
					treeOps.treeBusy = false;
					treeOps.showInProcessInfo(false);
					try {
						var t = eval(result);
						// if result is less than 0, it means an error occured														
						if (treeOps.isInt(t) == true  && t < 0) { 
							alert(eval("langManager.error_" + Math.abs(t)) + "\n"+langManager.willReload);									
							window.location.reload();							
						}
						else {
							var info = eval("(" + result + ")");
							$('#' + info.oldElementId).attr("id", info.elementId);
						}
					}
					catch(ex) {	
							var info = eval("(" + result + ")");
							$('#' + info.oldElementId).attr("id", info.elementId);	
					}	
				});
			}
		},
		afterAjax:function(node)
		{			
			if (node.html().length == 1) {
				node.html("<li class='line-last'></li>");
			}
		},		
		afterContextMenu: function(element, event)
		{
			var className = element.attr('class');
			if (className.indexOf('doc') >= 0) {
				$('#myMenu2').css('top',event.pageY).css('left',event.pageX).show();				
			}
			else {
				if (className.indexOf('root') >= 0) {
					$('#myMenu1 .edit, #myMenu1 .delete').hide();
					$('#myMenu1 .expandAll, #myMenu1 .collapseAll').show();
				}
				else {
					$('#myMenu1 .expandAll, #myMenu1 .collapseAll').hide();
				}
				$('#myMenu1').css('top',event.pageY).css('left',event.pageX).show();
			}
			
			$('*').click(function() { $('#myMenu1, #myMenu2').hide(); $('#myMenu1 .edit, #myMenu1 .delete').show(); });
			
		},
		animate:true
		//,docToFolderConvert:true		
	});		
	
	/*function nameFile(temp){
	  
	  var temp2=temp;
	  console.log("nom"+temp2);
	  return temp2;
	  
	}*/
	
	function saveFile(temp){
	  
	//var temp=nameFile(temp);
	
	//alert("voila voila "+temp);
	$.ajax({
	type: "POST",
	url:"FichierSave.php",
	data: "fichier="+temp,
	success: function(requester) {
	  //document.forms.fff.codelatex.value = document.forms.fff.dmc.value
	  //$('#codelatex').val()=requester; 
	  alert("resultar"+requester);
	  //$('#codelatex').attr('value', requester);
	  

	}
	
      });
	  
	  
	}
	function readFile(temp){
	  //console.log("here"+temp);
	
	$.ajax({
	type: "POST",
	url:"Fichier.php",
	data: "fichier="+temp,
	success: function(requester) {
	  //document.forms.fff.codelatex.value = document.forms.fff.dmc.value
	  //$('#codelatex').val()=requester; 
	  $('#codelatex').attr('value', requester);
	  

	}
	
      });
	}
});