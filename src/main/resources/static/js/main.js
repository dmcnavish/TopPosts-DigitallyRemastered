$(document).ready(function(){
	
	$('.show-more-content').on('click', function(event){
		var elm = event.target;
		
		$(elm).parent().siblings().removeClass('minimized-content');
		$(elm).parent().find('.show-less-content').show();
		$(elm).hide();
	});
	
	$('.show-less-content').on('click', function(event){
		var elm = event.target;
		$(elm).parent().parent().find('.post-content').addClass('minimized-content');
		
		$(elm).parent().find('.show-more-content').show();
		$(elm).hide();
	});
	
	function init(){
		$('img').addClass('img-responsive');
	}
	
	init();
});