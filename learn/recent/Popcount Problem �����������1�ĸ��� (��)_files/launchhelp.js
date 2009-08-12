//Help launch script
var helpWinName = "_help"; 
function LaunchHelp(baseUrl,project,market,hostedUrl,querytype,query,resize,format,cu,puid,filter,target){
	var isWinLive = (baseUrl.indexOf('.live') > -1 || format.charAt(0)=='b');
	var winWidth = (isWinLive) ? 640 : 230;
	var winHeight = (isWinLive) ? 640 : screen.availHeight;
	var winLeft = (isWinLive) ? (screen.availWidth - winWidth) * 0.5 : screen.availWidth - winWidth;
	var winTop = (isWinLive) ? (screen.availHeight - winHeight) * 0.5 : 0;
	var winFeatures = "resizable=yes,top="+winTop+",width="+winWidth+",height="+winHeight+",left="+winLeft;
	baseUrl += "/help.aspx";
    helpWinName= (target && target.length > 0)? target : helpWinName ;

	var sQS =(hostedUrl.indexOf("?") > -1) ? "&" : "?";		
	sQS += "project="+project+"&market="+market+"&querytype="+querytype.toLowerCase() +"&query="+query;
	sQS += "&tmt=" + escape(window.name) + "&domain=" + document.domain;
	
	if (!isWinLive) {
		//Get the window dimensions and position
		var iW=0; var iH=0; var iX=0; var iY=0; var oWin = window;
		if (typeof (oWin.innerWidth) == "number" ) {
			iW = oWin.innerWidth; iH = oWin.innerHeight;
		} else if (oWin.document.documentElement && oWin.document.documentElement.clientWidth) {
			iW = oWin.document.documentElement.clientWidth; iH = oWin.document.documentElement.clientHeight;
		} else if (oWin.document.body && oWin.document.body.clientWidth) {
			iW = oWin.document.body.clientWidth; iH = oWin.document.body.clientHeight;
		}
		if (typeof (oWin.top.screenLeft) == "number") {
			iX = oWin.top.screenLeft; iY = oWin.top.screenTop;
		} else if (typeof (oWin.top.screenX) == "number") {
			iX = oWin.top.screenX; iY = oWin.top.screenY;
		}
		sQS += "&od="+resize+","+iW+","+iH+","+iX+","+iY;
	}
	
	if (format != null && format != '') sQS += "&format="+format; 
	if (filter != null && filter != '') sQS += "&filter="+filter;
	if (cu != null && cu != '') sQS += "&cu="+cu;
	if (puid != null && puid != '') sQS += "&puid="+puid;

	var agent = navigator.userAgent.toLowerCase();
	if (((agent.indexOf('msn ') > -1) && (agent.indexOf('msn optimized')<0)) || (agent.indexOf('msmoney') > -1)) { //(MSN client but not 'IE Optimized for MSN') or Money client
		window.external.showHelpPane(baseUrl + sQS, winWidth);
	} else {
		var win;
		if (hostedUrl != null && hostedUrl != '' && !(agent.indexOf("safari") > 0)) win = window.open(hostedUrl + sQS, helpWinName, winFeatures); 
		else win = window.open(baseUrl + sQS, helpWinName, winFeatures); //floating portal
		if (win != null && typeof win == 'object') win.focus(); //no hidden window
	}
}
function Help_OpenPortal(sUrl,queryId,width,height) {
  var url = sUrl;
  if (queryId != null && queryId != '') {
     var oInput = document.getElementById(queryId);
     if (oInput != null && oInput.value) url += oInput.value;
  }
  var winWidth = (width != null && width != '') ? width : "550";
  var winHeight = (height != null && height != '') ? height : "575";
  var winFeatures = "resizable=yes,width="+winWidth+",height="+winWidth;
  var win = window.open(url,helpWinName,winFeatures);
  if (win != null && typeof win == 'object') win.focus();
}