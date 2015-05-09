$(document).ready(function(){
	
	$('.remove-post-button').on('click', function(event){
		var elm = event.target;
		$(elm).parent().parent().hide();
	});
	
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
	
	$('#navigateOlderPosts').on('click', function(event){
		var currentDate = getQueryParameterByName('date');
		var previousDate = '';
		if(currentDate){
			var currentMoment = moment(currentDate, 'MMDDYYYY');
			var previousMoment = currentMoment.subtract(1, 'days');
			previousDate = previousMoment.format("MMDDYYYY");
		}
		else{
//			utcMillis = new Date().getTime();
//			var currentMoment = moment.utc(utcMillis);
			previousDate = moment().format("MMDDYYYY");
		}
		
		window.location.href = '/?date=' + previousDate;
	});
	
	$('#navigateNewerPosts').on('click', function(event){
		var currentDate = getQueryParameterByName('date');
		var nextDate = '';
		if(currentDate){
			var currentMoment = moment(currentDate, 'MMDDYYYY');
			var nextMoment = currentMoment.add(1, 'days');
			nextDate = nextMoment.format("MMDDYYYY");
		}
		else{
			//can't see the future, the button shouldn't be visible
			return;
		}
		
		window.location.href = '/?date=' + nextDate;
	});
	
	//-----------------Utils start----------------------//
	
    function getQueryParameterByName(name){
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
        return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }
    
    //-----------------Utils end ----------------------//
    
    
	function init(){
		$('img').addClass('img-responsive');
		
		var titleDisplay = 'Check out the posts for today';
		var currentDate = getQueryParameterByName('date');
		if(currentDate){
			var currentMoment = moment(currentDate, 'MMDDYYYY');
			if(currentMoment.isAfter(moment()) ){
				$('#navigateNewerPosts').hide();
			}
			
			titleDisplay = 'Check out the posts for ' + currentMoment.format("dddd, MMMM Do YYYY");
		}
		else{
			$('#navigateNewerPosts').hide();
		}
		
		
		$('#titleText').text(titleDisplay);
	}
	
	init();
});