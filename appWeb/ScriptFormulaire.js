$(document).ready( function() {
  

  //$('#editeur').hide();
  
  $('#pdfTree li').click(function(){
			
			$('#editeur').show();
			});
	/*simpleTree = $('.simpleTree').simpleTree({
		autoclose: false,
		/**
		 * restore tree state according the cookies it stored.
		 */
	//	restoreTreeState: true,
		
		/**
		 * Callback function is called when one item is clicked
		 */	
	//	afterClick:function(node){
				/*alert($('span:first', node).text() + " clicked");
				alert($('span:first',node).parent().attr('id'));*/
				
	//	},
		/**
		 * Callback function is called when one item is double-clicked
		 */	
		/*afterDblClick:function(node){
			alert($('span:first',node).text() + " double clickedfile");	
			
		},
	
		
	});*/
  
  var codelatex=$("#codelatex").val();
 
});