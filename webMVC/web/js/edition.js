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
		console.log('dans edition -> ');
		console.log(FILES_CONTENT);
		
		files_json = convertToJSON(FILES_CONTENT);

		console.log('en json -> ' + files_json);

		$.post('/ajax/sync',
		 	{'files' : FILES_CONTENT},
			function(data){
				console.log(data);
			});	
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
								$.post('/ajax/processCompile',
								       {'root_file_id' : $('#root_file_id :selected').val()},
								       function(data){
										console.log(data);
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
});
