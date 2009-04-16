/* Slide Subnav Menu v1.1.1
 * by Justin Volpato http://www.justjquery.com/
 */

(function($){
	$.slidenav = function(options){
	
		var defaults = {
			animspeed: 'medium',
			hideonout: 'true'
		};
		
		var options = $.extend(defaults, options);
		
		
		$('.slidenav > ul > li:not(:has(ul))').append('<ul></ul>');
		
		$(".subnav").append($(".slidenav ul li ul"));
		$('.slidenav-box').addClass('inactive-box');
		
		
		function hideonout(){
			if (options.hideonout == 'true') {
				$('.slidenav-box').addClass('inactive-box');
			}
		}
		($(".slidenav-container"))? $(".slidenav-container").hoverIntent(function(){}, hideonout) : false;

		
		var subwidth = [];
		$(".slidenav ul li").each(function(aIndex){
			var bw = parseInt($('.slidenav-box').css("border-left-width")) + parseInt($('.slidenav-box').css("border-right-width"));
			var bp = parseInt($('.slidenav-box').css('padding-left')) + parseInt($('.slidenav-box').css('padding-right'));
			var minwidth = bw + bp;
			
			subwidth.push($('.subnav ul:eq(' + aIndex + ')').width());
			
			function changeWidth(){
				$('.slidenav-box').removeClass('inactive-box');
				$('.subnav ul').hide();
				$('.subnav ul:eq(' + aIndex + ')').show(1);
				if (subwidth[aIndex] == 0) {
					$('.slidenav-box').addClass('inactive-box');
				}
				else 
					$(".slidenav-box").animate({
						marginLeft: offsetvalue[aIndex],
						width: subwidth[aIndex]
					}, options.animspeed)
			}
			
			$(this).hoverIntent(changeWidth,function(){});
			
			
			var navitemwidth = [];
			var navculm = 0;
			var navculmwidth = [];
			var navitemcentre = [];
			var offsetvalue = [];
			for (i = 0; i < aIndex + 1; i++) {
				navitemwidth.push($('.slidenav li:eq(' + i + ')').width());
				navculm = navculm + navitemwidth[i];
				navculmwidth.push(navculm);
				navitemcentre.push(navculmwidth[i] - (navitemwidth[i] * 0.5));
				offsetvalue.push(navitemcentre[i] - (subwidth[i] * 0.5));
			}
			
			if (navitemcentre[aIndex] < (subwidth[aIndex] * 0.5)) {
				offsetvalue[aIndex] = 0;
			}
			else 
				if ((offsetvalue[aIndex] + subwidth[aIndex]) > $(".slidenav").width()) {
					offsetvalue[aIndex] = $(".slidenav").width() - subwidth[aIndex] - minwidth;
				}
		});
	}
})(jQuery);
