$(function(){
	$('.folder').click(function(){
		this.onselectstart=function(){return false;};
		$(this).attr('unselectable','on');
		$(this).next().slideToggle('fast');
		return false;
	});
	
	$('.folder').dblclick(function(){
		return false;
	});
	
	
	
});