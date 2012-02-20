function convertToJSON(map){
	mapJSON = "{";

	$.each(map, function(key, data){
		mapJSON += '"' + key + '" : "' + data + '", ';
	});

	mapJSON = mapJSON.substring(0, mapJSON.length - 2) + "}";

	return mapJSON;
}

$(function(){
	$('#sync').click(function(){
		if(CURRENT_FILE_ID)
			FILES_CONTENT[CURRENT_FILE_ID] = editAreaLoader.getValue('codelatex');
		
		files_json = convertToJSON(FILES_CONTENT);
		var loadingDiv="<div id='loadingDiv' style='text-align:center;'><b>Synchronistation en cours</b><br/><br/><img src='/images/loading.gif' alt='loading gif' /></div>";
		
		$('#popup_wrapper').html(loadingDiv).css('top', ($(window).height()/2)-16 + 'px').css('left', ($(window).width()/2)-16 + 'px').css('position', 'fixed').fadeIn();
		
		
		$.post('/ajax/sync',
		 	{'files' : FILES_CONTENT},
			function(data){

					$('#loadingDiv').fadeOut().remove();
			});	
	});
	
	$(window).keypress(function(event) {
	  if (!(event.which == 115 && event.ctrlKey) && event.which!=19)
			return true;
			
		$('#sync').trigger('click');
		event.preventDefault();
		return false;
	  
	});

	$('#compile').click(
		function(){
			$.get('/ajax/compile',
			      function(data){
				$('#popup_wrapper').html(data).css('top', ($(window).height()/2)-60 + 'px').css('left', ($(window).width()/2)-200 + 'px').css('position', 'fixed').fadeIn();

				$(document).keyup(function(event){
					if(event.which == 27)
						$('#popup_compile').fadeOut().remove();	
				});
	
				$('#cancelPopup').click(function(){
					$('#popup_compile').fadeOut().remove();	
				});	

				$('#project_id').change(function(){
					var pid = $(this).val();
			
					$.get('/ajax/getFileListForProject/id/' + pid,
					      function(files){
						$('#root_file_wrapper').html(files).fadeIn();

						$('#root_file_id').change(function(){
							if($(this).val())
								$('#submitCompile').fadeIn();
							else
								$('#submitCompile').fadeOut();

						});

						$('#form_compile').unbind('submit');

						$('#form_compile').submit(
							function(){
								var rootFile=$('#root_file_id :selected').val();
								$('#popup_compile').fadeOut().remove();
								
								var loadingDiv="<div id='loadingDiv' style='text-align:center;'><b>Compilation en cours</b><br/><br/><img src='/images/loading.gif' alt='loading gif' /></div>";
								$('#popup_wrapper').html(loadingDiv).css('top', ($(window).height()/2)-16 + 'px').css('left', ($(window).width()/2)-16 + 'px').css('position', 'fixed').fadeIn();
								$.post('/ajax/processCompile',
								       {'root_file_id' : rootFile},
								       
								       function(data){
								       		$('#loadingDiv').fadeOut().remove();
											$.post('/ajax/parseXml',
								      		 {'log' : data},
								      		 function(xml){
								      		 	if(xml!=""){
										  		 	var divLog="<div id='divLog'><div id='divLogTopBar'><span id='closeDivLog'>fermer</span></div><div id='divLogContent'>"+xml+"</div></div>";

										  			var heightEditor=stripPx($('#frame_codelatex').css('height'));
									 
										  			$('#frame_codelatex').css('height',(heightEditor-150));
										  			
										  		 	$('#editeur').append(divLog);
										  		 	$('#closeDivLog').unbind('click');	
										  		 	$('#closeDivLog').click(function(){
										  		 		$('#divLog').fadeOut().remove();
										  		 		$('#frame_codelatex').css('height',heightEditor);
										  		 	});
												}
												
												$('#viewer_frame').remove();
												if(xml.indexOf("Error")==-1){

											  		 
													//window.open("/viewer", "_blank");	
													var viewFrame = $('<iframe />', {name: 'viewer_frame', id: 'viewer_frame', src: '/viewer'});
													viewFrame.appendTo('body')
													viewFrame.css('position', 'absolute').css('bottom', 0).css('right', 0).css('height',  $(window).height() - $('#titre_principal').height()).css('width', '100%');
													$('#bloc_texloud').hide();
											
													$('#menu ul').prepend('<li><a id="back_edit">Revenir</a></li>');
													$('#back_edit').unbind('click');
													$('#back_edit').click(function(){
														$('#viewer_frame').toggle();
														$('#bloc_texloud').toggle();
														if($(this).html()=="Voir le dernier PDF compilé")
															$(this).html("Revenir");
														else
															$(this).html("Voir le dernier PDF compilé");
													});
													}
										  		 });	
								      		 								
											
								       });
							
							return false; 
						});
					      });
				    }
				);
			      }
			);
		}
	);

	$('#show_viewer').click(
		function(){
			$.get('/ajax/getViewer',
			     function(viewer){
				$('#edition').hide();
				$('#viewer').html(viewer).show();
			     }
			);
		}
	);

	$('#refresh_cache').click(function(){
		FILES_CONTENT = {};
	});
});

function stripPx( value ) {
    if( value == "" ) return 0;
    return parseFloat( value.substring( 0, value.length - 2 ) );
}  
