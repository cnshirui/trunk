/* Acknowledgments: Josh Heyer / David Stone for initial versions. Bradml for stripdown and package */
var keyMapping = [];
keyMapping['13'] = "ENTER";
keyMapping['37'] = "LEFT";
keyMapping['38'] = "UP";
keyMapping['39'] = "RIGHT";
keyMapping['40'] = "DOWN";
keyMapping['16'] = "SHIFT";
keyMapping['17'] = "CTRL";
keyMapping['33'] = "PGUP";
keyMapping['34'] = "PGDOWN";

var commandMapping = {
	Combinations: {
		"CTRL+ENTER"	: function() {Submit();},
		"CTRL+LEFT"		: function() {MoveToPost(false, false);},
		"CTRL+RIGHT"	: function() {MoveToPost(true, false);},
		"CTRL+UP"		: function() {MoveToPost(false, true);},
		"CTRL+DOWN"		: function() {MoveToPost(true, true);},
		"CTRL+PGUP"		: function() {MoveToPage(true);},
		"CTRL+PGDOWN"	: function() {MoveToPage(false);}
	}
};

document.onkeydown = function(e) {
	return shortcutEventHandler(e, commandMapping, keyMapping);
};

function getControlKeys(e) {
	var controls = "";
	if (e.ctrlKey)controls += "CTRL+";
	if (e.shiftKey)controls += "SHIFT+";
	if (e.altKey)controls += "ALT+";
	return controls;
}

// return: false == handled, true == keep looking
function shortcutEventHandler(KeyEvent, commandMapping, keyMapping) {
	if (!KeyEvent) KeyEvent = event;
	//Because IE knows better
	var eventKeyValue = (KeyEvent.keyCode) ? KeyEvent.keyCode: KeyEvent.charCode;
	var eventKeyName = "";
	if (keyMapping[eventKeyValue]) eventKeyName = keyMapping[eventKeyValue];
	var controlValue = getControlKeys(KeyEvent);
	eventKeyName = controlValue + eventKeyName;

	if (commandMapping.Combinations[eventKeyName]) 	{
		commandMapping.Combinations[eventKeyName]();
		
		// Stop propogation if we've found it
		if (KeyEvent.preventDefault) KeyEvent.preventDefault();
		else KeyEvent.returnValue = false;
		
		if (KeyEvent.stopPropagation) KeyEvent.stopPropagation();
		else KeyEvent.cancelBubble = true;

		return false;
	} 
	return true;
}

function Submit() {
	var theForm = document.forms['aspnetForm'];
	if (!theForm) theForm = document.aspnetForm;
	if (theForm) theForm.submit();
}

function MoveToPage(pageUp) {
	var elm = document.getElementById(pageUp?'_mbpUrl':'_mbnUrl');
	if (elm) location.href=elm.value;
}

function MoveToPost(next, thread) {
	if (Selected=="" || isNaN(Selected)) return;
	var ForumTable = document.getElementById("ForumTable");
	if (!ForumTable) return;
	var elems = getElementsByClass("MsgHd",ForumTable,"tr");
	if (!elems) return;
	
	var moveTo = null;
	var selectedId = parseInt(Selected);
			
	var selectedIndex = 0;
	var selectedThreadIndex = 0;
	for (var i=0; i<elems.length; ++i) 	{
		var id = elems[i].id.substring(1, elems[i].id.length-3);
		id = parseInt(id);
		
		if (IsThreadHead(elems[i]))
			selectedThreadIndex = i;
		
		if (id == selectedId) {
			selectedIndex = i;
			break;
		}
	}
	
	var nextId = null;
	for (var i=selectedIndex + (next ? 1 : -1); i<elems.length && i >= 0; next ? ++i : --i)	{
		if ((thread && IsThreadHead(elems[i])) || !thread) {
			nextId = elems[i].id.substring(1, elems[i].id.length-3);
			nextId = parseInt(nextId);
			break;
		}
	}
	
	if (nextId && nextId != parseInt(Selected)) {
		SwitchMessage(null,nextId);
		//EnsureMessageVisible(Selected, true);
	}
}

function IsThreadHead(elem) {
	return elem.className.indexOf("Rt") >= 0;
}

function EnsureMessageVisible(msgID, bShowTop) {
	var msgHeader = document.getElementById("F" + msgID + "_h0");
	var msgBody = document.getElementById("F" + msgID + "_h1");
	if (!msgBody || !msgHeader) return;

	// determine scroll position of top and bottom
	var scrollContainer = document.documentElement;
	var top = getRealPos(msgHeader, 'Top');
	var bottom = getRealPos(msgBody, 'Top') + msgBody.offsetHeight;

	// if not already visible, scroll to make it so
	var scrollTop = scrollContainer.scrollTop;
	if (scrollTop > top && !bShowTop)
		scrollTop = top - scrollContainer.clientHeight / 10;
	if (scrollTop + scrollContainer.clientHeight < bottom)
		scrollTop = bottom - scrollContainer.clientHeight;
	if (scrollTop > top && bShowTop)
		scrollTop = top - scrollContainer.clientHeight / 10;

	// apply corrections
	scrollContainer.scrollTop = scrollTop;
	//$(scrollContainer).stop().animate({'scrollTop' : scrollTop}, 400);
}

function getRealPos(i,which) {
	iPos = 0
	while (i!=null)  {
		iPos += i["offset" + which];
		i = i.offsetParent;
	}
	return iPos
}

function getElementsByClass(className,node,tag) {
  var found = new Array();
  if (node == null)node = document;
  if (tag == null)tag = '*';
  var elms = node.getElementsByTagName(tag);
  var length = elms.length;
  var pattern = new RegExp("(^|\\s)"+className+"(\\s|$)");
  for (i=0, j=0; i<length; i++) {
	 if (pattern.test(elms[i].className) ) {
		found[j] = elms[i];
		j++;
	 }
  }
  return found;
}