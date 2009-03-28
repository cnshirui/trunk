var myWidth;
var myHeight;

function WindowSize()
{
     if( typeof( window.innerWidth ) == 'number' ) {
          //Non-IE
          myWidth = window.innerWidth;
          myHeight = window.innerHeight;
     }
     else if( document.documentElement &&
            (document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
          //IE 6+ in 'standards compliant mode'
          myWidth = document.documentElement.clientWidth;
          myHeight = document.documentElement.clientHeight;
     }
     else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
          //IE 4 compatible
          myWidth = document.body.clientWidth;
          myHeight = document.body.clientHeight;
     }
}
function GetWindowHeight () {
    WindowSize();
     return myHeight;
}
function GetWindowWidth () {
    WindowSize();
     return myWidth;
}
function AutoResize() {
     var width, height;
     width = GetWindowWidth();
     height = GetWindowHeight();
     if($('typebox') && $('header')) {
		   height = height - $('typebox').clientHeight - $('header').clientHeight - 6;
	   }
	   if($('conversation')){
		   $('conversation').style.height = height + "px";
	   }
	   if($('userlist')){
		   $('userlist').style.height = height + "px";
	   }
}