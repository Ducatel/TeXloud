$(function(){
      
  $('#updatePass').children().last().hide();
  
  $('#chekp').click(function () {
    
    $('#updatePass').children().last().toggle();
        var choix=$('#chekp').is(':checked');
	
	if(choix == true){
	  
	    $("input").blur(function(){
		var value = ($(this).attr("value"));
		var id = ($(this).attr("id"));
			
		if(value=='')
		    $('#'+id).css('background-color','red');
			  
		  
		  $('#'+id).focusin(function () {
		    
		     $('#'+id).css('background-color','white');
		    
		  });
	    
	    });

	}
  
  });
  
});