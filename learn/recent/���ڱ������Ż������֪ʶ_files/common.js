/*
	[Discuz!] (C)2001-2009 Comsenz Inc.
	This is NOT a freeware, use is subject to license terms

	$Id: common.js 17555 2009-02-03 05:46:54Z monkey $
*/

var lang = new Array();
var userAgent = navigator.userAgent.toLowerCase();
var is_opera = userAgent.indexOf('opera') != -1 && opera.version();
var is_moz = (navigator.product == 'Gecko') && userAgent.substr(userAgent.indexOf('firefox') + 8, 3);
var is_ie = (userAgent.indexOf('msie') != -1 && !is_opera) && userAgent.substr(userAgent.indexOf('msie') + 5, 3);
var is_mac = userAgent.indexOf('mac') != -1;
var ajaxdebug = 0;
var codecount = '-1';
var codehtml = new Array();

//FixPrototypeForGecko
if(is_moz && window.HTMLElement) {
	HTMLElement.prototype.__defineSetter__('outerHTML', function(sHTML) {
        	var r = this.ownerDocument.createRange();
		r.setStartBefore(this);
		var df = r.createContextualFragment(sHTML);
		this.parentNode.replaceChild(df,this);
		return sHTML;
	});

	HTMLElement.prototype.__defineGetter__('outerHTML', function() {
		var attr;
		var attrs = this.attributes;
		var str = '<' + this.tagName.toLowerCase();
		for(var i = 0;i < attrs.length;i++){
			attr = attrs[i];
			if(attr.specified)
			str += ' ' + attr.name + '="' + attr.value + '"';
		}
		if(!this.canHaveChildren) {
			return str + '>';
		}
		return str + '>' + this.innerHTML + '</' + this.tagName.toLowerCase() + '>';
        });

	HTMLElement.prototype.__defineGetter__('canHaveChildren', function() {
		switch(this.tagName.toLowerCase()) {
			case 'area':case 'base':case 'basefont':case 'col':case 'frame':case 'hr':case 'img':case 'br':case 'input':case 'isindex':case 'link':case 'meta':case 'param':
			return false;
        	}
		return true;
	});
	HTMLElement.prototype.click = function(){
		var evt = this.ownerDocument.createEvent('MouseEvents');
		evt.initMouseEvent('click', true, true, this.ownerDocument.defaultView, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
		this.dispatchEvent(evt);
	}
}

Array.prototype.push = function(value) {
	this[this.length] = value;
	return this.length;
}

function $(id) {
	return document.getElementById(id);
}

function checkall(form, prefix, checkall) {
	var checkall = checkall ? checkall : 'chkall';
	count = 0;
	for(var i = 0; i < form.elements.length; i++) {
		var e = form.elements[i];
		if(e.name && e.name != checkall && (!prefix || (prefix && e.name.match(prefix)))) {
			e.checked = form.elements[checkall].checked;
			if(e.checked) {
				count++;
			}
		}
	}
	return count;
}

function doane(event) {
	e = event ? event : window.event;
	if(is_ie) {
		e.returnValue = false;
		e.cancelBubble = true;
	} else if(e) {
		e.stopPropagation();
		e.preventDefault();
	}
}

function fetchCheckbox(cbn) {
	return $(cbn) && $(cbn).checked == true ? 1 : 0;
}

function getcookie(name) {
	var cookie_start = document.cookie.indexOf(name);
	var cookie_end = document.cookie.indexOf(";", cookie_start);
	return cookie_start == -1 ? '' : unescape(document.cookie.substring(cookie_start + name.length + 1, (cookie_end > cookie_start ? cookie_end : document.cookie.length)));
}

imggroup = new Array();
function thumbImg(obj, method) {
	if(!obj) {
		return;
	}
	obj.onload = null;
	file = obj.src;
	zw = obj.offsetWidth;
	zh = obj.offsetHeight;
	if(zw < 2) {
		if(!obj.id) {
			obj.id = 'img_' + Math.random();
		}
		setTimeout("thumbImg($('" + obj.id + "'), " + method + ")", 100);
		return;
	}
	zr = zw / zh;
	method = !method ? 0 : 1;
	if(method) {
		fixw = obj.getAttribute('_width');
		fixh = obj.getAttribute('_height');
		if(zw > fixw) {
			zw = fixw;
			zh = zw / zr;
		}
		if(zh > fixh) {
			zh = fixh;
			zw = zh * zr;
		}
	} else {
		var widthary = imagemaxwidth.split('%');
		if(widthary.length > 1) {
			fixw = $('wrap').clientWidth - 200;
			if(widthary[0]) {
				fixw = fixw * widthary[0] / 100;
			} else if(widthary[1]) {
				fixw = fixw < widthary[1] ? fixw : widthary[1];
			}
		} else {
			fixw = widthary[0];
		}
		if(zw > fixw) {
			zw = fixw;
			zh = zw / zr;
			obj.style.cursor = 'pointer';
			if(!obj.onclick) {
				obj.onclick = function() {
					zoom(obj, obj.src);
				}
			}
		}
	}
	obj.width = zw;
	obj.height = zh;
}

function imgzoom() {}
function attachimg() {}

function in_array(needle, haystack) {
	if(typeof needle == 'string' || typeof needle == 'number') {
		for(var i in haystack) {
			if(haystack[i] == needle) {
					return true;
			}
		}
	}
	return false;
}

var clipboardswfdata;
function setcopy(text, alertmsg){
	if(is_ie) {
		clipboardData.setData('Text', text);
		if(alertmsg) {
			alert(alertmsg);
		}
	} else {
		floatwin('open_clipboard', -1, 300, 110);
		$('floatwin_clipboard_title').innerHTML = '剪贴板';
		str = '<div style="text-decoration:underline;">点此复制到剪贴板</div>' +
			AC_FL_RunContent('id', 'clipboardswf', 'name', 'clipboardswf', 'devicefont', 'false', 'width', '100', 'height', '20', 'src', 'images/common/clipboard.swf', 'menu', 'false',  'allowScriptAccess', 'sameDomain', 'swLiveConnect', 'true', 'wmode', 'transparent', 'style' , 'margin-top:-20px');
		$('floatwin_clipboard_content').innerHTML = str;
		text = text.replace(/[\xA0]/g, ' ');
		clipboardswfdata = text;
	}
}

function dconfirm(msg, script, width, height) {
	floatwin('open_confirm', -1, !width ? 300 : width, !height ? 110 : height);
	$('floatwin_confirm_title').innerHTML = '提示信息';
	$('floatwin_confirm_content').innerHTML = msg + '<br /><button onclick="' + script + ';floatwin(\'close_confirm\')">&nbsp;是&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button onclick="floatwin(\'close_confirm\')">&nbsp;否&nbsp;</button>';
}

function dnotice(msg, script, width, height) {
	script = !script ? '' : script;
	floatwin('open_confirm', -1, !width ? 400 : width, !height ? 110 : height);
	$('floatwin_confirm_title').innerHTML = '提示信息';
	$('floatwin_confirm_content').innerHTML = msg + (script ? '<br /><button onclick="' + script + ';floatwin(\'close_confirm\')">确定</button>' : '');
}

function setcopy_gettext() {
	window.document.clipboardswf.SetVariable('str', clipboardswfdata)
}

function isUndefined(variable) {
	return typeof variable == 'undefined' ? true : false;
}

function mb_strlen(str) {
	var len = 0;
	for(var i = 0; i < str.length; i++) {
		len += str.charCodeAt(i) < 0 || str.charCodeAt(i) > 255 ? (charset == 'utf-8' ? 3 : 2) : 1;
	}
	return len;
}

function mb_cutstr(str, maxlen, dot) {
	var len = 0;
	var ret = '';
	var dot = !dot ? '...' : '';
	maxlen = maxlen - dot.length;
	for(var i = 0; i < str.length; i++) {
		len += str.charCodeAt(i) < 0 || str.charCodeAt(i) > 255 ? (charset == 'utf-8' ? 3 : 2) : 1;
		if(len > maxlen) {
			ret += dot;
			break;
		}
		ret += str.substr(i, 1);
	}
	return ret;
}

function setcookie(cookieName, cookieValue, seconds, path, domain, secure) {
	var expires = new Date();
	expires.setTime(expires.getTime() + seconds * 1000);
	domain = !domain ? cookiedomain : domain;
	path = !path ? cookiepath : path;
	document.cookie = escape(cookieName) + '=' + escape(cookieValue)
		+ (expires ? '; expires=' + expires.toGMTString() : '')
		+ (path ? '; path=' + path : '/')
		+ (domain ? '; domain=' + domain : '')
		+ (secure ? '; secure' : '');
}

function strlen(str) {
	return (is_ie && str.indexOf('\n') != -1) ? str.replace(/\r?\n/g, '_').length : str.length;
}

function updatestring(str1, str2, clear) {
	str2 = '_' + str2 + '_';
	return clear ? str1.replace(str2, '') : (str1.indexOf(str2) == -1 ? str1 + str2 : str1);
}

function toggle_collapse(objname, noimg, complex, lang) {
	var obj = $(objname);
	if(obj) {
		obj.style.display = obj.style.display == '' ? 'none' : '';
		var collapsed = getcookie('discuz_collapse');
		collapsed = updatestring(collapsed, objname, !obj.style.display);
		setcookie('discuz_collapse', collapsed, (collapsed ? 2592000 : -2592000));
	}
	if(!noimg) {
		var img = $(objname + '_img');
		if(img.tagName != 'IMG') {
			if(img.className.indexOf('_yes') == -1) {
				img.className = img.className.replace(/_no/, '_yes');
				if(lang) {
					img.innerHTML = lang[0];
				}
			} else {
				img.className = img.className.replace(/_yes/, '_no');
				if(lang) {
					img.innerHTML = lang[1];
				}
			}
		} else {
			img.src = img.src.indexOf('_yes.gif') == -1 ? img.src.replace(/_no\.gif/, '_yes\.gif') : img.src.replace(/_yes\.gif/, '_no\.gif');
		}
		img.blur();
	}
	if(complex) {
		var objc = $(objname + '_c');
		objc.className = objc.className == 'c_header' ? 'c_header closenode' : 'c_header';
	}

}

function sidebar_collapse(lang) {
	if(lang[0]) {
		toggle_collapse('sidebar', null, null, lang);
		$('wrap').className = $('wrap').className == 'wrap with_side s_clear' ? 'wrap s_clear' : 'wrap with_side s_clear';
	} else {
		var collapsed = getcookie('discuz_collapse');
		collapsed = updatestring(collapsed, 'sidebar', 1);
		setcookie('discuz_collapse', collapsed, (collapsed ? 2592000 : -2592000));
		location.reload();
	}
}

function trim(str) {
	return (str + '').replace(/(\s+)$/g, '').replace(/^\s+/g, '');
}

function _attachEvent(obj, evt, func, eventobj) {
	eventobj = !eventobj ? obj : eventobj;
	if(obj.addEventListener) {
		obj.addEventListener(evt, func, false);
	} else if(eventobj.attachEvent) {
		obj.attachEvent("on" + evt, func);
	}
}

var cssloaded= new Array();
function loadcss(cssname) {
	if(!cssloaded[cssname]) {
		css = document.createElement('link');
		css.type = 'text/css';
		css.rel = 'stylesheet';
		css.href = USERCACHE + '/style_' + STYLEID + '_' + cssname + '.css?' + VERHASH;
		var headNode = document.getElementsByTagName("head")[0];
		headNode.appendChild(css);
		cssloaded[cssname] = 1;
	}
}

var jsmenu = new Array();
var ctrlobjclassName;
jsmenu['active'] = new Array();
jsmenu['timer'] = new Array();
jsmenu['iframe'] = new Array();

function initCtrl(ctrlobj, click, duration, timeout, layer) {
	if(ctrlobj && !ctrlobj.initialized) {
		ctrlobj.initialized = true;
		ctrlobj.unselectable = true;

		ctrlobj.outfunc = typeof ctrlobj.onmouseout == 'function' ? ctrlobj.onmouseout : null;
		ctrlobj.onmouseout = function() {
			if(this.outfunc) this.outfunc();
			if(duration < 3 && !jsmenu['timer'][ctrlobj.id]) jsmenu['timer'][ctrlobj.id] = setTimeout('hideMenu(' + layer + ')', timeout);
		}

		ctrlobj.overfunc = typeof ctrlobj.onmouseover == 'function' ? ctrlobj.onmouseover : null;
		ctrlobj.onmouseover = function(e) {
			doane(e);
			if(this.overfunc) this.overfunc();
			if(click) {
				clearTimeout(jsmenu['timer'][this.id]);
				jsmenu['timer'][this.id] = null;
			} else {
				for(var id in jsmenu['timer']) {
					if(jsmenu['timer'][id]) {
						clearTimeout(jsmenu['timer'][id]);
						jsmenu['timer'][id] = null;
					}
				}
			}
		}
	}
}

function initMenu(ctrlid, menuobj, duration, timeout, layer, drag) {
	if(menuobj && !menuobj.initialized) {
		menuobj.initialized = true;
		menuobj.ctrlkey = ctrlid;
		menuobj.onclick = ebygum;
		menuobj.style.position = 'absolute';
		if(duration < 3) {
			if(duration > 1) {
				menuobj.onmouseover = function() {
					clearTimeout(jsmenu['timer'][ctrlid]);
					jsmenu['timer'][ctrlid] = null;
				}
			}
			if(duration != 1) {
				menuobj.onmouseout = function() {
					jsmenu['timer'][ctrlid] = setTimeout('hideMenu(' + layer + ')', timeout);
				}
			}
		}
		menuobj.style.zIndex = 999 + layer;
		if(drag) {
			menuobj.onmousedown = function(event) {try{menudrag(menuobj, event, 1);}catch(e){}};
			menuobj.onmousemove = function(event) {try{menudrag(menuobj, event, 2);}catch(e){}};
			menuobj.onmouseup = function(event) {try{menudrag(menuobj, event, 3);}catch(e){}};
		}
	}
}

var menudragstart = new Array();
function menudrag(menuobj, e, op) {
	if(op == 1) {
		if(in_array(is_ie ? event.srcElement.tagName : e.target.tagName, ['TEXTAREA', 'INPUT', 'BUTTON', 'SELECT'])) {
			return;
		}
		menudragstart = is_ie ? [event.clientX, event.clientY] : [e.clientX, e.clientY];
		menudragstart[2] = parseInt(menuobj.style.left);
		menudragstart[3] = parseInt(menuobj.style.top);
		doane(e);
	} else if(op == 2 && menudragstart[0]) {
		var menudragnow = is_ie ? [event.clientX, event.clientY] : [e.clientX, e.clientY];
		menuobj.style.left = (menudragstart[2] + menudragnow[0] - menudragstart[0]) + 'px';
		menuobj.style.top = (menudragstart[3] + menudragnow[1] - menudragstart[1]) + 'px';
		doane(e);
	} else if(op == 3) {
		menudragstart = [];
		doane(e);
	}
}

function showMenu(ctrlid, click, offset, duration, timeout, layer, showid, maxh, drag) {
	var ctrlobj = $(ctrlid);
	if(!ctrlobj) return;
	if(isUndefined(click)) click = false;
	if(isUndefined(offset)) offset = 0;
	if(isUndefined(duration)) duration = 2;
	if(isUndefined(timeout)) timeout = 250;
	if(isUndefined(layer)) layer = 0;
	if(isUndefined(showid)) showid = ctrlid;
	var showobj = $(showid);
	var menuobj = $(showid + '_menu');
	if(!showobj|| !menuobj) return;
	if(isUndefined(maxh)) maxh = 400;
	if(isUndefined(drag)) drag = false;

	if(click && jsmenu['active'][layer] == menuobj) {
		hideMenu(layer);
		return;
	} else {
		hideMenu(layer);
	}

	var len = jsmenu['timer'].length;
	if(len > 0) {
		for(var i=0; i<len; i++) {
			if(jsmenu['timer'][i]) clearTimeout(jsmenu['timer'][i]);
		}
	}

	initCtrl(ctrlobj, click, duration, timeout, layer);
	ctrlobjclassName = ctrlobj.className;
	ctrlobj.className += ' hover';
	initMenu(ctrlid, menuobj, duration, timeout, layer, drag);

	menuobj.style.display = '';
	if(!is_opera) {
		menuobj.style.clip = 'rect(auto, auto, auto, auto)';
	}

	setMenuPosition(showid, offset);

	if(maxh && menuobj.scrollHeight > maxh) {
		menuobj.style.height = maxh + 'px';
		if(is_opera) {
			menuobj.style.overflow = 'auto';
		} else {
			menuobj.style.overflowY = 'auto';
		}
	}

	if(!duration) {
		setTimeout('hideMenu(' + layer + ')', timeout);
	}

	jsmenu['active'][layer] = menuobj;
}

function setMenuPosition(showid, offset) {
	var showobj = $(showid);
	var menuobj = $(showid + '_menu');
	if(isUndefined(offset)) offset = 0;
	if(showobj) {
		showobj.pos = fetchOffset(showobj);
		showobj.X = showobj.pos['left'];
		showobj.Y = showobj.pos['top'];
		if($(InFloat) != null) {
			var InFloate = InFloat.split('_');
			if(!floatwinhandle[InFloate[1] + '_1']) {
				floatwinnojspos = fetchOffset($('floatwinnojs'));
				floatwinhandle[InFloate[1] + '_1'] = floatwinnojspos['left'];
				floatwinhandle[InFloate[1] + '_2'] = floatwinnojspos['top'];
			}
			showobj.X = showobj.X - $(InFloat).scrollLeft - parseInt(floatwinhandle[InFloate[1] + '_1']);
			showobj.Y = showobj.Y - $(InFloat).scrollTop - parseInt(floatwinhandle[InFloate[1] + '_2']);
			InFloat = '';
		}
		showobj.w = showobj.offsetWidth;
		showobj.h = showobj.offsetHeight;
		menuobj.w = menuobj.offsetWidth;
		menuobj.h = menuobj.offsetHeight;
		if(offset < 3) {
			menuobj.style.left = (showobj.X + menuobj.w > document.body.clientWidth) && (showobj.X + showobj.w - menuobj.w >= 0) ? showobj.X + showobj.w - menuobj.w + 'px' : showobj.X + 'px';
			menuobj.style.top = offset == 1 ? showobj.Y + 'px' : (offset == 2 || ((showobj.Y + showobj.h + menuobj.h > document.documentElement.scrollTop + document.documentElement.clientHeight) && (showobj.Y - menuobj.h >= 0)) ? (showobj.Y - menuobj.h) + 'px' : showobj.Y + showobj.h + 'px');
		} else if(offset == 3) {
			menuobj.style.left = (document.body.clientWidth - menuobj.clientWidth) / 2 + document.body.scrollLeft + 'px';
			menuobj.style.top = (document.body.clientHeight - menuobj.clientHeight) / 2 + document.body.scrollTop + 'px';
		}

		if(menuobj.style.clip && !is_opera) {
			menuobj.style.clip = 'rect(auto, auto, auto, auto)';
		}
	}
}

function hideMenu(layer) {
	if(isUndefined(layer)) layer = 0;
	if(jsmenu['active'][layer]) {
		try {
			$(jsmenu['active'][layer].ctrlkey).className = ctrlobjclassName;
		} catch(e) {}
		clearTimeout(jsmenu['timer'][jsmenu['active'][layer].ctrlkey]);
		jsmenu['active'][layer].style.display = 'none';
		if(is_ie && is_ie < 7 && jsmenu['iframe'][layer]) {
			jsmenu['iframe'][layer].style.display = 'none';
		}
		jsmenu['active'][layer] = null;
	}
}

function fetchOffset(obj) {
	var left_offset = obj.offsetLeft;
	var top_offset = obj.offsetTop;
	while((obj = obj.offsetParent) != null) {
		left_offset += obj.offsetLeft;
		top_offset += obj.offsetTop;
	}
	return { 'left' : left_offset, 'top' : top_offset };
}

function ebygum(eventobj) {
	if(!eventobj || is_ie) {
		window.event.cancelBubble = true;
		return window.event;
	} else {
		if(eventobj.target.type == 'submit') {
			eventobj.target.form.submit();
		}
		eventobj.stopPropagation();
		return eventobj;
	}
}

function menuoption_onclick_function(e) {
	this.clickfunc();
	hideMenu();
}

function menuoption_onclick_link(e) {
	choose(e, this);
}

function menuoption_onmouseover(e) {
	this.className = 'popupmenu_highlight';
}

function menuoption_onmouseout(e) {
	this.className = 'popupmenu_option';
}

function choose(e, obj) {
	var links = obj.getElementsByTagName('a');
	if(links[0]) {
		if(is_ie) {
			links[0].click();
			window.event.cancelBubble = true;
		} else {
			if(e.shiftKey) {
				window.open(links[0].href);
				e.stopPropagation();
				e.preventDefault();
			} else {
				window.location = links[0].href;
				e.stopPropagation();
				e.preventDefault();
			}
		}
		hideMenu();
	}
}

var Ajaxs = new Array();//note 每个URL只允许生成一个AJAX对象，防止重复点击。
var AjaxStacks = new Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);//note ajax 对象时间占位堆栈。预置位10个，应该足够用了。
var attackevasive = isUndefined(attackevasive) ? 0 : attackevasive;
function Ajax(recvType, waitId) {

	for(var stackId = 0; stackId < AjaxStacks.length && AjaxStacks[stackId] != 0; stackId++);//note 查询空闲的时间位
	AjaxStacks[stackId] = 1;//note 表示该任务占位

	var aj = new Object();

	aj.loading = '加载中...';//public
	aj.recvType = recvType ? recvType : 'XML';//public
	aj.waitId = waitId ? $(waitId) : null;//public

	aj.resultHandle = null;//private
	aj.sendString = '';//private
	aj.targetUrl = '';//private
	aj.stackId = 0;
	aj.stackId = stackId;

	//note loading 为 null 时，表示使用默认，为空字符串时表示不显示。
	aj.setLoading = function(loading) {
		if(typeof loading !== 'undefined' && loading !== null) aj.loading = loading;
	}

	//note 默认为 XML 方式
	aj.setRecvType = function(recvtype) {
		aj.recvType = recvtype;
	}

	aj.setWaitId = function(waitid) {
		aj.waitId = typeof waitid == 'object' ? waitid : $(waitid);
	}

	aj.createXMLHttpRequest = function() {
		var request = false;
		if(window.XMLHttpRequest) {
			request = new XMLHttpRequest();
			if(request.overrideMimeType) {
				request.overrideMimeType('text/xml');
			}
		} else if(window.ActiveXObject) {
			var versions = ['Microsoft.XMLHTTP', 'MSXML.XMLHTTP', 'Microsoft.XMLHTTP', 'Msxml2.XMLHTTP.7.0', 'Msxml2.XMLHTTP.6.0', 'Msxml2.XMLHTTP.5.0', 'Msxml2.XMLHTTP.4.0', 'MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP'];
			for(var i=0; i<versions.length; i++) {
				try {
					request = new ActiveXObject(versions[i]);
					if(request) {
						return request;
					}
				} catch(e) {}
			}
		}
		return request;
	}

	aj.XMLHttpRequest = aj.createXMLHttpRequest();
	//note private
	aj.showLoading = function() {
		if(aj.waitId && (aj.XMLHttpRequest.readyState != 4 || aj.XMLHttpRequest.status != 200)) {
			aj.waitId.style.display = '';
			aj.waitId.innerHTML = '<span><img src="' + IMGDIR + '/loading.gif"> ' + aj.loading + '</span>';
		}
	}

	//note private
	aj.processHandle = function() {
		if(aj.XMLHttpRequest.readyState == 4 && aj.XMLHttpRequest.status == 200) {
			for(k in Ajaxs) {
				if(Ajaxs[k] == aj.targetUrl) {
					Ajaxs[k] = null;
				}
			}
			if(aj.waitId) {
				aj.waitId.style.display = 'none';
			}
			if(aj.recvType == 'HTML') {
				aj.resultHandle(aj.XMLHttpRequest.responseText, aj);
			} else if(aj.recvType == 'XML') {
				if(aj.XMLHttpRequest.responseXML.lastChild) {
					aj.resultHandle(aj.XMLHttpRequest.responseXML.lastChild.firstChild.nodeValue, aj);
				} else {
					if(ajaxdebug) {
						var error = mb_cutstr(aj.XMLHttpRequest.responseText.replace(/\r?\n/g, '\\n').replace(/"/g, '\\\"'), 200);
						aj.resultHandle('<root>ajaxerror<script type="text/javascript" reload="1">alert(\'Ajax Error: \\n' + error + '\');</script></root>', aj);
					}
				}
			}
			AjaxStacks[aj.stackId] = 0;//note 清空标志位，使其它对象有时间段可以被申请
		}
	}

	//note public
	aj.get = function(targetUrl, resultHandle) {

		setTimeout(function(){aj.showLoading()}, 250);
		if(in_array(targetUrl, Ajaxs)) {
			return false;
		} else {
			Ajaxs.push(targetUrl);
		}
		aj.targetUrl = targetUrl;
		aj.XMLHttpRequest.onreadystatechange = aj.processHandle;
		aj.resultHandle = resultHandle;
		var delay = attackevasive & 1 ? (aj.stackId + 1) * 1001 : 100;
		if(window.XMLHttpRequest) {
			setTimeout(function(){
			aj.XMLHttpRequest.open('GET', aj.targetUrl);
			aj.XMLHttpRequest.send(null);}, delay);
		} else {
			setTimeout(function(){
			aj.XMLHttpRequest.open("GET", targetUrl, true);
			aj.XMLHttpRequest.send();}, delay);
		}

	}
	//note public
	aj.post = function(targetUrl, sendString, resultHandle) {
		setTimeout(function(){aj.showLoading()}, 250);
		if(in_array(targetUrl, Ajaxs)) {
			return false;
		} else {
			Ajaxs.push(targetUrl);
		}
		aj.targetUrl = targetUrl;
		aj.sendString = sendString;
		aj.XMLHttpRequest.onreadystatechange = aj.processHandle;
		aj.resultHandle = resultHandle;
		aj.XMLHttpRequest.open('POST', targetUrl);
		aj.XMLHttpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		aj.XMLHttpRequest.send(aj.sendString);
	}
	return aj;
}

//note 封装函数参数
function newfunction(func){
	var args = new Array();
	for(var i=1; i<arguments.length; i++) args.push(arguments[i]);
	return function(event){
		doane(event);
		window[func].apply(window, args);
		return false;
	}
}

function display(id) {
	$(id).style.display = $(id).style.display == '' ? 'none' : '';
}

function display_opacity(id, n) {
	if(!$(id)) {
		return;
	}
	if(n >= 0) {
		n -= 10;
		$(id).style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + n + ')';
		$(id).style.opacity = n / 100;
		setTimeout('display_opacity(\'' + id + '\',' + n + ')', 50);
	} else {
		$(id).style.display = 'none';
		$(id).style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=100)';
		$(id).style.opacity = 1;
	}
}

var evalscripts = new Array();
function evalscript(s) {
	if(s.indexOf('<script') == -1) return s;
	var p = /<script[^\>]*?>([^\x00]*?)<\/script>/ig;
	var arr = new Array();
	while(arr = p.exec(s)) {
		var p1 = /<script[^\>]*?src=\"([^\>]*?)\"[^\>]*?(reload=\"1\")?(?:charset=\"([\w\-]+?)\")?><\/script>/i;
		var arr1 = new Array();
		arr1 = p1.exec(arr[0]);
		if(arr1) {
			appendscript(arr1[1], '', arr1[2], arr1[3]);
		} else {
			p1 = /<script(.*?)>([^\x00]+?)<\/script>/i;
			arr1 = p1.exec(arr[0]);
			appendscript('', arr1[2], arr1[1].indexOf('reload=') != -1);
		}
	}
	return s;
}

function appendscript(src, text, reload, charset) {
	var id = hash(src + text);
	if(!reload && in_array(id, evalscripts)) return;
	if(reload && $(id)) {
		$(id).parentNode.removeChild($(id));
	}

	evalscripts.push(id);
	var scriptNode = document.createElement("script");
	scriptNode.type = "text/javascript";
	scriptNode.id = id;
	scriptNode.charset = charset ? charset : (is_moz ? document.characterSet : document.charset);
	try {
		if(src) {
			scriptNode.src = src;
		} else if(text){
//--++ plugin_code qcomic begin
			if( text.match('typeof submithandle_edit') || text.match('typeof submithandle_reply') )
			{
				if( typeof(qcomic_data) != 'undefined' && qcomic_data == 1 ) 
				{
					location.href=location.href;
					return;
				}
				else
				{
					scriptNode.text = text;
				}
			}
			else
			{
				scriptNode.text = text;
			}
//--++ plugin_code qcomic end
			scriptNode.text = text;
		}
		$('append_parent').appendChild(scriptNode);
	} catch(e) {}
}

function stripscript(s) {
	return s.replace(/<script.*?>.*?<\/script>/ig, '');
}

function ajaxupdateevents(obj, tagName) {
	tagName = tagName ? tagName : 'A';
	var objs = obj.getElementsByTagName(tagName);
	for(k in objs) {
		var o = objs[k];
		ajaxupdateevent(o);
	}
}

function ajaxupdateevent(o) {
	if(typeof o == 'object' && o.getAttribute) {
		if(o.getAttribute('ajaxtarget')) {
			if(!o.id) o.id = Math.random();//note 该标签唯一标识
			var ajaxevent = o.getAttribute('ajaxevent') ? o.getAttribute('ajaxevent') : 'click';//note 默认为 click 事件
			var ajaxurl = o.getAttribute('ajaxurl') ? o.getAttribute('ajaxurl') : o.href;//note 默认为 click 事件
			_attachEvent(o, ajaxevent, newfunction('ajaxget', ajaxurl, o.getAttribute('ajaxtarget'), o.getAttribute('ajaxwaitid'), o.getAttribute('ajaxloading'), o.getAttribute('ajaxdisplay')));
			if(o.getAttribute('ajaxfunc')) {
				o.getAttribute('ajaxfunc').match(/(\w+)\((.+?)\)/);
				_attachEvent(o, ajaxevent, newfunction(RegExp.$1, RegExp.$2));
			}
		}
	}
}

/*
 *@ url: 需求请求的 url
 *@ id : 显示的 id
 *@ waitid: 等待的 id，默认为显示的 id，如果 waitid 为空字符串，则不显示 loading...， 如果为 null，则在 showid 区域显示
 *@ linkid: 是哪个链接触发的该 ajax 请求，该对象的属性(如 ajaxdisplay)保存了一些 ajax 请求过程需要的数据。
*/
function ajaxget(url, showid, waitid, loading, display, recall) {
	waitid = typeof waitid == 'undefined' || waitid === null ? showid : waitid;
	var x = new Ajax();
	x.setLoading(loading);
	x.setWaitId(waitid);
	x.display = typeof display == 'undefined' || display == null ? '' : display;
	x.showId = $(showid);//note 自定义属性。
	if(x.showId) x.showId.orgdisplay = typeof x.showId.orgdisplay === 'undefined' ? x.showId.style.display : x.showId.orgdisplay;//note 纪录 showId 的最原始的显示状态

	if(url.substr(strlen(url) - 1) == '#') {
		url = url.substr(0, strlen(url) - 1);
		x.autogoto = 1;
	}

	var url = url + '&inajax=1&ajaxtarget=' + showid;
	x.get(url, function(s, x) {
		evaled = false;
		if(s.indexOf('ajaxerror') != -1) {
			evalscript(s);
			evaled = true;
		}
		if(!evaled && (typeof ajaxerror == 'undefined' || !ajaxerror)) {
			//note 隐藏 Loading... 切换内容显示状态
			if(x.showId) {
				x.showId.style.display = x.showId.orgdisplay;
				x.showId.style.display = x.display;
				x.showId.orgdisplay = x.showId.style.display;
				ajaxinnerhtml(x.showId, s);
				ajaxupdateevents(x.showId);
				if(x.autogoto) scroll(0, x.showId.offsetTop);
			}
		}

		if(!evaled)evalscript(s);
		ajaxerror = null;
		if(recall) {eval(recall);}
	});
}

var ajaxpostHandle = 0;
//note id 为 menuid
function ajaxpost(formid, showid, waitid, showidclass, submitbtn) {
	showloading();
	var waitid = typeof waitid == 'undefined' || waitid === null ? showid : (waitid !== '' ? waitid : '');
	var showidclass = !showidclass ? '' : showidclass;

	if(ajaxpostHandle != 0) {
		return false;
	}
	var ajaxframeid = 'ajaxframe';
	var ajaxframe = $(ajaxframeid);
	//note 避免重复创建iframe，判断IFRAME对象是否存在
	if(ajaxframe == null) {
		if (is_ie && !is_opera) {
			ajaxframe = document.createElement("<iframe name='" + ajaxframeid + "' id='" + ajaxframeid + "'></iframe>");
		} else {
			ajaxframe = document.createElement("iframe");
			ajaxframe.name = ajaxframeid;
			ajaxframe.id = ajaxframeid;
		}
		ajaxframe.style.display = 'none';
		$('append_parent').appendChild(ajaxframe);

	}
	$(formid).target = ajaxframeid;
	ajaxpostHandle = [showid, ajaxframeid, formid, $(formid).target, showidclass, submitbtn];
	if(ajaxframe.attachEvent) {
		ajaxframe.detachEvent ('onload', ajaxpost_load);
		ajaxframe.attachEvent('onload', ajaxpost_load);
	} else {
		document.removeEventListener('load', ajaxpost_load, true);
		ajaxframe.addEventListener('load', ajaxpost_load, false);
	}
	$(formid).action += '&inajax=1';
	$(formid).submit();
	return false;
}

function ajaxpost_load() {
	showloading('none');
	var s = '';
	try {
		if(is_ie) {
			s = $(ajaxpostHandle[1]).contentWindow.document.XMLDocument.text;
		} else {
			s = $(ajaxpostHandle[1]).contentWindow.document.documentElement.firstChild.nodeValue;
		}
	} catch(e) {
		if(ajaxdebug) {
			var error = mb_cutstr($(ajaxpostHandle[1]).contentWindow.document.body.innerText.replace(/\r?\n/g, '\\n').replace(/"/g, '\\\"'), 200);
			s = '<root>ajaxerror<script type="text/javascript" reload="1">alert(\'Ajax Error: \\n' + error + '\');</script></root>';
		}
	}
	evaled = false;
	if(s != '' && s.indexOf('ajaxerror') != -1) {
		evalscript(s);
		evaled = true;
	}
	if(ajaxpostHandle[4]) {
		$(ajaxpostHandle[0]).className = ajaxpostHandle[4];
		if(ajaxpostHandle[5]) {
			ajaxpostHandle[5].disabled = false;
		}
	}
	if(!evaled && (typeof ajaxerror == 'undefined' || !ajaxerror)) {
		ajaxinnerhtml($(ajaxpostHandle[0]), s);
		if(!evaled)evalscript(s);
		setMenuPosition($(ajaxpostHandle[0]).ctrlid, 0);
		setTimeout("hideMenu()", 3000);
	}
	ajaxerror = null;
	if($(ajaxpostHandle[2])) {
		$(ajaxpostHandle[2]).target = ajaxpostHandle[3];
	}
	ajaxpostHandle = 0;
}

function ajaxmenu(e, ctrlid, timeout, func, cache, duration, ismenu, divclass, optionclass) {
	showloading();
	if(jsmenu['active'][0] && jsmenu['active'][0].ctrlkey == ctrlid) {
		hideMenu();
		doane(e);
		return;
	} else if(is_ie && is_ie < 7 && document.readyState.toLowerCase() != 'complete') {
		return;
	}
	if(isUndefined(timeout)) timeout = 3000;
	if(isUndefined(func)) func = '';
	if(isUndefined(cache)) cache = 1;
	if(isUndefined(divclass)) divclass = 'popupmenu_popup';
	if(isUndefined(optionclass)) optionclass = 'popupmenu_option';
	if(isUndefined(ismenu)) ismenu = 1;
	if(isUndefined(duration)) duration = timeout > 0 ? 0 : 3;
	var div = $(ctrlid + '_menu');
	if(cache && div) {
		showMenu(ctrlid, e.type == 'click', 0, duration, timeout, 0, ctrlid, 400, 1);
		if(func) setTimeout(func + '(' + ctrlid + ')', timeout);
		doane(e);
	} else {
		if(!div) {
			div = document.createElement('div');
			div.ctrlid = ctrlid;
			div.id = ctrlid + '_menu';
			div.style.display = 'none';
			div.className = divclass;
			$('append_parent').appendChild(div);
		}

		var x = new Ajax();
		var href = !isUndefined($(ctrlid).href) ? $(ctrlid).href : $(ctrlid).attributes['href'].value;
		x.div = div;
		x.etype = e.type;
		x.optionclass = optionclass;
		x.duration = duration;
		x.timeout = timeout;
		x.get(href + '&inajax=1&ajaxmenuid='+ctrlid+'_menu', function(s) {
			evaled = false;
			if(s.indexOf('ajaxerror') != -1) {
				evalscript(s);
				evaled = true;
				if(!cache && duration != 3 && x.div.id) setTimeout('$("append_parent").removeChild($(\'' + x.div.id + '\'))', timeout);
			}
			if(!evaled && (typeof ajaxerror == 'undefined' || !ajaxerror)) {
				if(x.div) x.div.innerHTML = '<div class="' + x.optionclass + '">' + s + '</div>';
				showMenu(ctrlid, x.etype == 'click', 0, x.duration, x.timeout, 0, ctrlid, 400, 1);
				if(func) setTimeout(func + '("' + ctrlid + '")', x.timeout);
			}
			if(!evaled) evalscript(s);
			ajaxerror = null;
			showloading('none');
		});
		doane(e);
	}
}

//得到一个定长的hash值， 依赖于 stringxor()
function hash(string, length) {
	var length = length ? length : 32;
	var start = 0;
	var i = 0;
	var result = '';
	filllen = length - string.length % length;
	for(i = 0; i < filllen; i++){
		string += "0";
	}
	while(start < string.length) {
		result = stringxor(result, string.substr(start, length));
		start += length;
	}
	return result;
}

//note 将两个字符串进行异或运算，结果为英文字符组合
function stringxor(s1, s2) {
	var s = '';
	var hash = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
	var max = Math.max(s1.length, s2.length);
	for(var i=0; i<max; i++) {
		var k = s1.charCodeAt(i) ^ s2.charCodeAt(i);
		s += hash.charAt(k % 52);
	}
	return s;
}

function showloading(display, waiting) {
	var display = display ? display : 'block';
	var waiting = waiting ? waiting : '页面加载中...';
	$('ajaxwaitid').innerHTML = waiting;
	$('ajaxwaitid').style.display = display;
}

function ajaxinnerhtml(showid, s) {
	if(showid.tagName != 'TBODY') {
		showid.innerHTML = s;
	} else {
		while(showid.firstChild) {
			showid.firstChild.parentNode.removeChild(showid.firstChild);
		}
		var div1 = document.createElement('DIV');
		div1.id = showid.id+'_div';
		div1.innerHTML = '<table><tbody id="'+showid.id+'_tbody">'+s+'</tbody></table>';
		$('append_parent').appendChild(div1);
		var trs = div1.getElementsByTagName('TR');
		var l = trs.length;
		for(var i=0; i<l; i++) {
			showid.appendChild(trs[0]);
		}
		var inputs = div1.getElementsByTagName('INPUT');
		var l = inputs.length;
		for(var i=0; i<l; i++) {
			showid.appendChild(inputs[0]);
		}
		div1.parentNode.removeChild(div1);
	}
}

function AC_GetArgs(args, classid, mimeType) {
	var ret = new Object();
	ret.embedAttrs = new Object();
	ret.params = new Object();
	ret.objAttrs = new Object();
	for (var i = 0; i < args.length; i = i + 2){
		var currArg = args[i].toLowerCase();
		switch (currArg){
			case "classid":break;
			case "pluginspage":ret.embedAttrs[args[i]] = 'http://www.macromedia.com/go/getflashplayer';break;
			case "src":ret.embedAttrs[args[i]] = args[i+1];ret.params["movie"] = args[i+1];break;
			case "codebase":ret.objAttrs[args[i]] = 'http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0';break;
			case "onafterupdate":case "onbeforeupdate":case "onblur":case "oncellchange":case "onclick":case "ondblclick":case "ondrag":case "ondragend":
			case "ondragenter":case "ondragleave":case "ondragover":case "ondrop":case "onfinish":case "onfocus":case "onhelp":case "onmousedown":
			case "onmouseup":case "onmouseover":case "onmousemove":case "onmouseout":case "onkeypress":case "onkeydown":case "onkeyup":case "onload":
			case "onlosecapture":case "onpropertychange":case "onreadystatechange":case "onrowsdelete":case "onrowenter":case "onrowexit":case "onrowsinserted":case "onstart":
			case "onscroll":case "onbeforeeditfocus":case "onactivate":case "onbeforedeactivate":case "ondeactivate":case "type":
			case "id":ret.objAttrs[args[i]] = args[i+1];break;
			case "width":case "height":case "align":case "vspace": case "hspace":case "class":case "title":case "accesskey":case "name":
			case "tabindex":ret.embedAttrs[args[i]] = ret.objAttrs[args[i]] = args[i+1];break;
			default:ret.embedAttrs[args[i]] = ret.params[args[i]] = args[i+1];
		}
	}
	ret.objAttrs["classid"] = classid;
	if(mimeType) {
		ret.embedAttrs["type"] = mimeType;
	}
	return ret;
}

function AC_FL_RunContent() {
	var ret = AC_GetArgs(arguments, "clsid:d27cdb6e-ae6d-11cf-96b8-444553540000", "application/x-shockwave-flash");
	var str = '';
	if(is_ie && !is_opera) {
		str += '<object ';
		for (var i in ret.objAttrs) {
			str += i + '="' + ret.objAttrs[i] + '" ';
		}
		str += '>';
		for (var i in ret.params) {
			str += '<param name="' + i + '" value="' + ret.params[i] + '" /> ';
		}
		str += '</object>';
	} else {
		str += '<embed ';
		for (var i in ret.embedAttrs) {
			str += i + '="' + ret.embedAttrs[i] + '" ';
		}
		str += '></embed>';
	}
	return str;
}

//PageScroll
function pagescroll_class(obj, pagewidth, pageheight) {
	this.ctrlobj = $(obj);
	this.speed = 2;
	this.pagewidth = pagewidth;
	this.times = 1;
	this.pageheight = pageheight;
	this.running = 0;
	this.defaultleft = 0;
	this.defaulttop = 0;
	this.script = '';
	this.start = function(times) {
		if(this.running) return 0;
		this.times = !times ? 1 : times;
		this.scrollpx = 0;
		return this.running = 1;
	}
	this.left = function(times, script) {
		if(!this.start(times)) return;
		this.stepv = -(this.step = this.pagewidth * this.times / this.speed);
		this.script = !script ? '' : script;
		setTimeout('pagescroll.h()', 1);
	}
	this.right = function(times, script) {
		if(!this.start(times)) return;
		this.stepv = this.step = this.pagewidth * this.times / this.speed;
		this.script = !script ? '' : script;
		setTimeout('pagescroll.h()', 1);
	}
	this.up = function(times, script) {
		if(!this.start(times)) return;
		this.stepv = -(this.step = this.pageheight * this.times / this.speed);
		this.script = !script ? '' : script;
		setTimeout('pagescroll.v()', 1);
	}
	this.down = function(times, script) {
		if(!this.start(times)) return;
		this.stepv = this.step = this.pageheight * this.times / this.speed;
		this.script = !script ? '' : script;
		setTimeout('pagescroll.v()', 1);
	}
	this.h = function() {
		if(this.scrollpx <= this.pagewidth * this.times) {
			this.scrollpx += Math.abs(this.stepv);
			patch = this.scrollpx > this.pagewidth * this.times ? this.scrollpx - this.pagewidth * this.times : 0;
			patch = patch > 0 && this.stepv < 0 ? -patch : patch;
			oldscrollLeft = this.ctrlobj.scrollLeft;
			this.ctrlobj.scrollLeft = this.ctrlobj.scrollLeft + this.stepv - patch;
			if(oldscrollLeft != this.ctrlobj.scrollLeft) {
				setTimeout('pagescroll.h()', 1);
				return;
			}
		}
		if(this.script) {
			eval(this.script);
		}
		this.running = 0;
	}
	this.v = function() {
		if(this.scrollpx <= this.pageheight * this.times) {
			this.scrollpx += Math.abs(this.stepv);
			patch = this.scrollpx > this.pageheight * this.times ? this.scrollpx - this.pageheight * this.times : 0;
			patch = patch > 0 && this.stepv < 0 ? -patch : patch;
			oldscrollTop = this.ctrlobj.scrollTop;
			this.ctrlobj.scrollTop = this.ctrlobj.scrollTop + this.stepv - patch;
			if(oldscrollTop != this.ctrlobj.scrollTop) {
				setTimeout('pagescroll.v()', 1);
				return;
			}
		}
		if(this.script) {
			eval(this.script);
		}
		this.running = 0;
	}
	this.init = function() {
		this.ctrlobj.scrollLeft = this.defaultleft;
		this.ctrlobj.scrollTop = this.defaulttop;
	}

}

//LiSelect
var selectopen = null;
var hiddencheckstatus = 0;
function loadselect(id, showinput, pageobj, pos, method) {
	var obj = $(id);
	var objname = $(id).name;
	var objoffset = fetchOffset(obj);
	objoffset['width'] = is_ie ? (obj.offsetWidth ? obj.offsetWidth : parseInt(obj.currentStyle.width)) : obj.offsetWidth;
	objoffset['height'] = is_ie ? (obj.offsetHeight ? obj.offsetHeight : parseInt(obj.currentStyle.height)) : obj.offsetHeight;
	pageobj = !pageobj ? '' : pageobj;
	showinput = !showinput ? 0 : showinput;
	pos = !pos ? 0 : 1;
	method = !method ? 0 : 1;
	var maxlength = 0;
	var defaultopt = '', defaultv = '';
	var lis = '<ul onfocus="loadselect_keyinit(event, 1)" onblur="loadselect_keyinit(event, 2)" class="newselect" id="' + objname + '_selectmenu" style="' + (!pos ? 'z-index:999;position: absolute; width: ' + objoffset['width'] + 'px;' : '') + 'display: none">';
	for(var i = 0;i < obj.options.length;i++){
		lis += '<li ' + (obj.options[i].selected ? 'class="current" ' : '') + 'k_id="' + id + '" k_value="' + obj.options[i].value + '" onclick="loadselect_liset(\'' + objname + '\', ' + showinput + ', \'' + id + '\',' + (showinput ? 'this.innerHTML' : '\'' + obj.options[i].value + '\'') + ',this.innerHTML, ' + i + ')">' + obj.options[i].innerHTML + '</li>';
		maxlength = obj.options[i].value.length > maxlength ? obj.options[i].value.length : maxlength;
		if(obj.options[i].selected) {
			defaultopt = obj.options[i].innerHTML;
			defaultv = obj.options[i].value;
			if($(objname)) {
				$(objname).setAttribute('selecti', i);
			}
		}
	}
	lis += '</ul>';
	if(showinput) {
		inp = '<input autocomplete="off" class="newselect" id="' + objname + '_selectinput" onclick="loadselect_viewmenu(this, \'' + objname + '\', 0, \'' + pageobj + '\');doane(event)" onchange="loadselect_inputset(\'' + id + '\', this.value);loadselect_viewmenu(this, \'' + objname + '\', 0, \'' + pageobj + '\')" value="' + defaultopt + '" style="width: ' + objoffset['width'] + 'px;height: ' + objoffset['height'] + 'px;" tabindex="1" />';
	} else {
		inp = '<a href="javascript:;" hidefocus="true" class="loadselect" id="' + objname + '_selectinput"' + (!obj.disabled ? ' onfocus="loadselect_keyinit(event, 1)" onblur="loadselect_keyinit(event, 2)" onmouseover="this.focus()" onmouseout="this.blur()" onkeyup="loadselect_key(this, event, \'' + objname + '\', \'' + pageobj + '\')" onclick="loadselect_viewmenu(this, \'' + objname + '\', 0, \'' + pageobj + '\');doane(event)"' : '') + ' tabindex="1">' + defaultopt + '</a>';
	}
	obj.options.length = 0;
	if(defaultopt) {
		obj.options[0]= showinput ? new Option('', defaultopt) : new Option('', defaultv);
	}
	obj.style.width = objoffset['width'] + 'px';
	obj.style.display = 'none';
	if(!method) {
		obj.outerHTML += inp + lis;
	} else {
		if(showinput) {
			var inpobj = document.createElement("input");
		} else {
			var inpobj = document.createElement("a");
		}
		obj.parentNode.appendChild(inpobj);
		inpobj.outerHTML = inp;
		var lisobj = document.createElement("ul");
		obj.parentNode.appendChild(lisobj);
		lisobj.outerHTML = lis;
	}
}

function loadselect_keyinit(e, a) {
	if(a == 1) {
		if(document.attachEvent) {
			document.body.attachEvent('onkeydown', loadselect_keyhandle);
		} else {
			document.body.addEventListener('keydown', loadselect_keyhandle, false);
		}
	} else {
		if(document.attachEvent) {
			document.body.detachEvent('onkeydown', loadselect_keyhandle);
		} else {
			document.body.removeEventListener('keydown', loadselect_keyhandle, false);
		}
	}
}

function loadselect_keyhandle(e) {
	e = is_ie ? event : e;
	if(e.keyCode == 40 || e.keyCode == 38) doane(e);
}

function loadselect_key(ctrlobj, e, objname, pageobj) {
	value = e.keyCode;
	if(value == 40 || value == 38) {
		if($(objname + '_selectmenu').style.display == 'none') {
			loadselect_viewmenu(ctrlobj, objname, 0, pageobj);
		} else {
			lis = $(objname + '_selectmenu').getElementsByTagName('LI');
			selecti = $(objname).getAttribute('selecti');
			lis[selecti].className = '';
			if(value == 40) {
				selecti = parseInt(selecti) + 1;
			} else if(value == 38) {
				selecti = parseInt(selecti) - 1;
			}
			if(selecti < 0) {
				selecti = lis.length - 1
			} else if(selecti > lis.length - 1) {
				selecti = 0;
			}
			lis[selecti].className = 'current';
			$(objname).setAttribute('selecti', selecti);
			lis[selecti].parentNode.scrollTop = lis[selecti].offsetTop;
		}
	} else if(value == 13) {
		lis = $(objname + '_selectmenu').getElementsByTagName('LI');
		for(i = 0;i < lis.length;i++) {
			if(lis[i].className == 'current') {
				loadselect_liset(objname, 0, lis[i].getAttribute('k_id'), lis[i].getAttribute('k_value'), lis[i].innerHTML, i);
				break;
			}
		}
	}
}

function loadselect_viewmenu(ctrlobj, objname, hidden, pageobj) {
	if(!selectopen) {
		if(document.attachEvent) {
			document.body.attachEvent('onclick', loadselect_hiddencheck);
		} else {
			document.body.addEventListener('click', loadselect_hiddencheck, false);
		}
	}
	var hidden = !hidden ? 0 : 1;
	if($(objname + '_selectmenu').style.display == '' || hidden) {
		$(objname + '_selectmenu').style.display = 'none';
	} else {
		if($(selectopen)) {
			$(selectopen).style.display = 'none';
		}
		var objoffset = fetchOffset(ctrlobj);
		if(pageobj) {
			var InFloate = pageobj.split('_');
			objoffset['left'] -= $(pageobj).scrollLeft + parseInt(floatwinhandle[InFloate[1] + '_1']);
			objoffset['top'] -= $(pageobj).scrollTop + parseInt(floatwinhandle[InFloate[1] + '_2']);
		}
		objoffset['height'] = ctrlobj.offsetHeight;
		$(objname + '_selectmenu').style.display = '';
		selectopen = objname + '_selectmenu';
	}
	hiddencheckstatus = 1;
}

function loadselect_hiddencheck() {
	if(hiddencheckstatus) {
		if($(selectopen)) {
			$(selectopen).style.display = 'none';
		}
		hiddencheckstatus = 0;
	}
}

function loadselect_liset(objname, showinput, obj, v, opt, selecti) {
	var change = 1;
	if(showinput) {
		if($(objname + '_selectinput').value != opt) {
			$(objname + '_selectinput').value = opt;
		} else {
			change = 0;
		}
	} else {
		if($(objname + '_selectinput').innerHTML != opt) {
			$(objname + '_selectinput').innerHTML = opt;
		} else {
			change = 0;
		}
	}
	lis = $(objname + '_selectmenu').getElementsByTagName('LI');
	lis[$(objname).getAttribute('selecti')].className = '';
	lis[selecti].className = 'current';
	$(objname).setAttribute('selecti', selecti);
	$(objname + '_selectmenu').style.display='none';
	if(change) {
		obj = $(obj);
		obj.options.length=0;
		obj.options[0]=new Option('', v);
		eval(obj.getAttribute('change'));
	}
}

function loadselect_inputset(obj, v) {
	obj = $(obj);
	obj.options.length=0;
	obj.options[0]=new Option('', v);
	eval(obj.getAttribute('change'));
}

//DetectCapsLock
var detectobj;
function detectcapslock(e, obj) {
	detectobj = obj;
	valueCapsLock = e.keyCode ? e.keyCode : e.which;
	valueShift = e.shiftKey ? e.shiftKey : (valueCapsLock == 16 ? true : false);
	detectobj.className = (valueCapsLock >= 65 && valueCapsLock <= 90 && !valueShift || valueCapsLock >= 97 && valueCapsLock <= 122 && valueShift) ? 'capslock txt' : 'txt';
	if(is_ie) {
		event.srcElement.onblur = detectcapslock_cleardetectobj;
	} else {
		e.target.onblur = detectcapslock_cleardetectobj;
	}
}

function detectcapslock_cleardetectobj() {
	detectobj.className = 'txt';
}

//FloatWin
var hiddenobj = new Array();
var floatwinhandle = new Array();
var floatscripthandle = new Array();
var floattabs = new Array();
var floatwins = new Array();
var InFloat = '';
var floatwinreset = 0;
var floatwinopened = 0;
function floatwin(action, script, w, h, scrollpos) {
	var floatonly = !floatonly ? 0 : 1;
	var actione = action.split('_');
	action = actione[0];
	if((!allowfloatwin || allowfloatwin == 0) && action == 'open' && in_array(actione[1], ['register','login','newthread','reply','edit']) && w >= 600) {
		href = script;
		if(is_ie) {
			href += (script.indexOf('?') != -1 ?  '&' : '?') + 'referer=' + escape(location.href);
		}
		location.href = href;
		return;
	}
	var handlekey = actione[1];
	var layerid = 'floatwin_' + handlekey;
	if(is_ie) {
		var objs = $('wrap').getElementsByTagName("OBJECT");
	} else {
		var objs = $('wrap').getElementsByTagName("EMBED");
	}
	if(action == 'open') {
		loadcss('float');
		floatwinhandle[handlekey + '_0'] = layerid;
		if(!floatwinopened) {
			$('wrap').onkeydown = floatwin_wrapkeyhandle;
			for(i = 0;i < objs.length; i ++) {
				if(objs[i].style.visibility != 'hidden') {
					objs[i].setAttribute("oldvisibility", objs[i].style.visibility);
					objs[i].style.visibility = 'hidden';
				}
			}
		}
		scrollpos = !scrollpos ? '' : 'floatwin_scroll(\'' + scrollpos + '\');';
		var clientWidth = document.body.clientWidth;
		var clientHeight = document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight;
		var scrollTop = document.body.scrollTop ? document.body.scrollTop : document.documentElement.scrollTop;
		if(script && script != -1) {
			if(script.lastIndexOf('/') != -1) {
				script = script.substr(script.lastIndexOf('/') + 1);
			}
			var scriptfile = script.split('?');
			scriptfile = scriptfile[0];
			if(floatwinreset || floatscripthandle[scriptfile] && floatscripthandle[scriptfile][0] != script) {
				if(!isUndefined(floatscripthandle[scriptfile])) {
					$('append_parent').removeChild($(floatscripthandle[scriptfile][1]));
					$('append_parent').removeChild($(floatscripthandle[scriptfile][1] + '_mask'));
				}
				floatwinreset = 0;
			}
			floatscripthandle[scriptfile] = [script, layerid];
		}
		if(!$(layerid)) {
			floattabs[layerid] = new Array();
			div = document.createElement('div');
			div.className = 'floatwin';
			div.id = layerid;
			div.style.width = w + 'px';
			div.style.height = h + 'px';
			div.style.left = floatwinhandle[handlekey + '_1'] = ((clientWidth - w) / 2) + 'px';
			div.style.position = 'absolute';
			div.style.zIndex = '999';
			div.onkeydown = floatwin_keyhandle;
			$('append_parent').appendChild(div);
			$(layerid).style.display = '';
			$(layerid).style.top = floatwinhandle[handlekey + '_2'] = ((clientHeight - h) / 2 + scrollTop) + 'px';
			$(layerid).innerHTML = '<div><h3 class="float_ctrl"><em><img src="' + IMGDIR + '/loading.gif"> 加载中...</em><span><a href="javascript:;" class="float_close" onclick="floatwinreset = 1;floatwin(\'close_' + handlekey + '\');">&nbsp</a></span></h3></div>';
			divmask = document.createElement('div');
			divmask.className = 'floatwinmask';
			divmask.id = layerid + '_mask';
			divmask.style.width = (parseInt($(layerid).style.width) + 14) + 'px';
			divmask.style.height = (parseInt($(layerid).style.height) + 14) + 'px';
			divmask.style.left = (parseInt($(layerid).style.left) - 6) + 'px';
			divmask.style.top = (parseInt($(layerid).style.top) - 6) + 'px';
			divmask.style.position = 'absolute';
			divmask.style.zIndex = '998';
			divmask.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=90,finishOpacity=100,style=0)';
			divmask.style.opacity = 0.9;
			$('append_parent').appendChild(divmask);
			if(script && script != -1) {
				script += (script.search(/\?/) > 0 ? '&' : '?') + 'infloat=yes&handlekey=' + handlekey;
				try {
					ajaxget(script, layerid, '', '', '', scrollpos);
				} catch(e) {
					setTimeout("ajaxget('" + script + "', '" + layerid + "', '', '', '', '" + scrollpos + "')", 1000);
				}
			} else if(script == -1) {
				$(layerid).innerHTML = '<div><h3 class="float_ctrl"><em id="' + layerid + '_title"></em><span><a href="javascript:;" class="float_close" onclick="floatwinreset = 1;floatwin(\'close_' + handlekey + '\');">&nbsp</a></span></h3></div><div id="' + layerid + '_content"></div>';
				$(layerid).style.zIndex = '1099';
				$(layerid + '_mask').style.zIndex = '1098';
			}
		} else {
			$(layerid).style.width = w + 'px';
			$(layerid).style.height = h + 'px';
			$(layerid).style.display = '';
			$(layerid).style.top = floatwinhandle[handlekey + '_2'] = ((clientHeight - h) / 2 + scrollTop) + 'px';
			$(layerid + '_mask').style.width = (parseInt($(layerid).style.width) + 14) + 'px';
			$(layerid + '_mask').style.height = (parseInt($(layerid).style.height) + 14) + 'px';
			$(layerid + '_mask').style.display = '';
			$(layerid + '_mask').style.top = (parseInt($(layerid).style.top) - 6) + 'px';
		}
		floatwins[floatwinopened] = handlekey;
		floatwinopened++;
	} else if(action == 'close' && floatwinhandle[handlekey + '_0']) {
		floatwinopened--;
		for(i = 0;i < floatwins.length; i++) {
			if(handlekey == floatwins[i]) {
				floatwins[i] = null;
			}
		}
		if(!floatwinopened) {
			for(i = 0;i < objs.length; i ++) {
				if(objs[i].attributes['oldvisibility']) {
					objs[i].style.visibility = objs[i].attributes['oldvisibility'].nodeValue;
					objs[i].removeAttribute('oldvisibility');
				}
			}
			$('wrap').onkeydown = null;
		}
		hiddenobj = new Array();
		$(layerid + '_mask').style.display = 'none';
		$(layerid).style.display = 'none';
	} else if(action == 'size' && floatwinhandle[handlekey + '_0']) {
		if(!floatwinhandle[handlekey + '_3']) {
			var clientWidth = document.body.clientWidth;
			var clientHeight = document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight;
			var w = clientWidth > 800 ? clientWidth * 0.9 : 800;
			var h = clientHeight * 0.9;
			floatwinhandle[handlekey + '_3'] = $(layerid).style.left;
			floatwinhandle[handlekey + '_4'] = $(layerid).style.top;
			floatwinhandle[handlekey + '_5'] = $(layerid).style.width;
			floatwinhandle[handlekey + '_6'] = $(layerid).style.height;
			$(layerid).style.left = floatwinhandle[handlekey + '_1'] = ((clientWidth - w) / 2) + 'px';
			$(layerid).style.top = floatwinhandle[handlekey + '_2'] = ((document.documentElement.clientHeight - h) / 2 + document.documentElement.scrollTop) + 'px';
			$(layerid).style.width = w + 'px';
			$(layerid).style.height = h + 'px';
		} else {
			$(layerid).style.left = floatwinhandle[handlekey + '_1'] = floatwinhandle[handlekey + '_3'];
			$(layerid).style.top = floatwinhandle[handlekey + '_2'] = floatwinhandle[handlekey + '_4'];
			$(layerid).style.width = floatwinhandle[handlekey + '_5'];
			$(layerid).style.height = floatwinhandle[handlekey + '_6'];
			floatwinhandle[handlekey + '_3'] = '';
		}
		$(layerid + '_mask').style.width = (parseInt($(layerid).style.width) + 14) + 'px';
		$(layerid + '_mask').style.height = (parseInt($(layerid).style.height) + 14) + 'px';
		$(layerid + '_mask').style.left = (parseInt($(layerid).style.left) - 6) + 'px';
		$(layerid + '_mask').style.top = (parseInt($(layerid).style.top) - 6) + 'px';
	}
}

function floatwin_scroll(pos) {
	var pose = pos.split(',');
	try {
		pagescroll.defaultleft = pose[0];
		pagescroll.defaulttop = pose[1];
		pagescroll.init();
	} catch(e) {}
}

function floatwin_wrapkeyhandle(e) {
	e = is_ie ? event : e;
	if(e.keyCode == 9) {
		doane(e);
	} else if(e.keyCode == 27) {
		for(i = floatwins.length - 1;i >= 0; i--) {
			floatwin('close_' + floatwins[i]);
		}
	}
}

function floatwin_keyhandle(e) {
	e = is_ie ? event : e;
	if(e.keyCode == 9) {
		doane(e);
		var obj = is_ie ? e.srcElement : e.target;
		var srcobj = obj;
		j = 0;
		while(obj.className.indexOf('floatbox') == -1) {
			obj = obj.parentNode;
		}
		obj.id = obj.id ? obj.id : 'floatbox_' + Math.random();
		if(!floattabs[obj.id]) {
			floattabs[obj.id] = new Array();
			var alls = obj.getElementsByTagName("*");
			for(i = 0;i < alls.length;i++) {
				if(alls[i].getAttribute('tabindex') == 1) {
					floattabs[obj.id][j] = alls[i];
					j++;
				}
			}
		}
		if(floattabs[obj.id].length > 0) {
			for(i = 0;i < floattabs[obj.id].length;i++) {
				if(srcobj == floattabs[obj.id][i]) {
					j = e.shiftKey ? i - 1 : i + 1;break;
				}
			}
			if(j < 0) {
				j = floattabs[obj.id].length - 1;
			}
			if(j > floattabs[obj.id].length - 1) {
				j = 0;
			}
			do{
				focusok = 1;
				try{ floattabs[obj.id][j].focus(); } catch(e) {
					focusok = 0;
				}
				if(!focusok) {
					j = e.shiftKey ? j - 1 : j + 1;
					if(j < 0) {
						j = floattabs[obj.id].length - 1;
					}
					if(j > floattabs[obj.id].length - 1) {
						j = 0;
					}
				}
			} while(!focusok);
		}
	}
}

//ShowSelect
function showselect(obj, inpid, t, rettype) {
	if(!obj.id) {
		var t = !t ? 0 : t;
		var rettype = !rettype ? 0 : rettype;
		obj.id = 'calendarexp_' + Math.random();
		div = document.createElement('div');
		div.id = obj.id + '_menu';
		div.style.display = 'none';
		div.className = 'showselect_menu';
		if($(InFloat) != null) {
			$(InFloat).appendChild(div);
		} else {
			$('append_parent').appendChild(div);
		}
		s = '';
		if(!t) {
			s += showselect_row(inpid, '一天', 1, 0, rettype);
			s += showselect_row(inpid, '一周', 7, 0, rettype);
			s += showselect_row(inpid, '一个月', 30, 0, rettype);
			s += showselect_row(inpid, '三个月', 90, 0, rettype);
			s += showselect_row(inpid, '自定义', -2);
		} else {
			if($(t)) {
				var lis = $(t).getElementsByTagName('LI');
				for(i = 0;i < lis.length;i++) {
					s += '<a href="javascript:;" onclick="$(\'' + inpid + '\').value = this.innerHTML">' + lis[i].innerHTML + '</a><br />';
				}
				s += showselect_row(inpid, '自定义', -1);
			} else {
				s += '<a href="javascript:;" onclick="$(\'' + inpid + '\').value = \'0\'">永久</a><br />';
				s += showselect_row(inpid, '7 天', 7, 1, rettype);
				s += showselect_row(inpid, '14 天', 14, 1, rettype);
				s += showselect_row(inpid, '一个月', 30, 1, rettype);
				s += showselect_row(inpid, '三个月', 90, 1, rettype);
				s += showselect_row(inpid, '半年', 182, 1, rettype);
				s += showselect_row(inpid, '一年', 365, 1, rettype);
				s += showselect_row(inpid, '自定义', -1);
			}
		}
		$(div.id).innerHTML = s;
	}
	showMenu(obj.id);
	if(is_ie && is_ie < 7) {
		doane(event);
	}
}

function showselect_row(inpid, s, v, notime, rettype) {
	if(v >= 0) {
		if(!rettype) {
			var notime = !notime ? 0 : 1;
			t = today.getTime();
			t += 86400000 * v;
			d = new Date();
			d.setTime(t);
			return '<a href="javascript:;" onclick="$(\'' + inpid + '\').value = \'' + d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate() + (!notime ? ' ' + d.getHours() + ':' + d.getMinutes() : '') + '\'">' + s + '</a><br />';
		} else {
			return '<a href="javascript:;" onclick="$(\'' + inpid + '\').value = \'' + v + '\'">' + s + '</a><br />';
		}
	} else if(v == -1) {
		return '<a href="javascript:;" onclick="$(\'' + inpid + '\').focus()">' + s + '</a><br />';
	} else if(v == -2) {
		return '<a href="javascript:;" onclick="$(\'' + inpid + '\').onclick()">' + s + '</a><br />';
	}
}

//Smilies
function smilies_show(id, smcols, method, seditorkey) {
	if(seditorkey && !$(seditorkey + 'smilies_menu')) {
		var div = document.createElement("div");
		div.id = seditorkey + 'smilies_menu';
		div.style.display = 'none';
		div.className = 'smilieslist';
		$('append_parent').appendChild(div);
		var div = document.createElement("div");
		div.id = id;
		div.style.overflow = 'hidden';
		$(seditorkey + 'smilies_menu').appendChild(div);
	}
	if(typeof smilies_type == 'undefined') {
		var scriptNode = document.createElement("script");
		scriptNode.type = "text/javascript";
		scriptNode.charset = charset ? charset : (is_moz ? document.characterSet : document.charset);
		scriptNode.src = USERCACHE + '/smilies_var.js?' + VERHASH;
		$('append_parent').appendChild(scriptNode);
		if(is_ie) {
			scriptNode.onreadystatechange = function() {
				smilies_onload(id, smcols, method, seditorkey);
			}
		} else {
			scriptNode.onload = function() {
				smilies_onload(id, smcols, method, seditorkey);
			}
		}
	} else {
		smilies_onload(id, smcols, method, seditorkey);
	}
}

var currentstype = null;
function smilies_onload(id, smcols, method, seditorkey) {
	seditorkey = !seditorkey ? '' : seditorkey;
	smile = getcookie('smile').split('D');
	if(typeof smilies_type == 'object') {
		if(smile[0] && smilies_array[smile[0]]) {
			currentstype = smile[0];
		} else {
			for(i in smilies_array) {
				currentstype = i; break;
			}
		}
		smiliestype = '<div class="smiliesgroup" style="margin-right: 0"><ul>';
		for(i in smilies_type) {
			if(smilies_type[i][0]) {
				smiliestype += '<li><a href="javascript:;" hidefocus="true" ' + (currentstype == i ? 'class="current"' : '') + ' id="stype_'+method+'_'+i+'" onclick="smilies_switch(\'' + id + '\', \'' + smcols + '\', '+i+', 1, ' + method + ', \'' + seditorkey + '\');if(currentstype) {$(\'stype_'+method+'_\'+currentstype).className=\'\';}this.className=\'current\';currentstype='+i+';">'+smilies_type[i][0]+'</a></li>';
			}
		}
		smiliestype += '</ul></div>';
		$(id).innerHTML = smiliestype + '<div style="clear: both" class="float_typeid" id="' + id + '_data"></div><table class="smilieslist_table" id="' + id + '_preview_table" style="display: none"><tr><td class="smilieslist_preview" id="' + id + '_preview"></td></tr></table><div style="clear: both" class="smilieslist_page" id="' + id + '_page"></div>';
		smilies_switch(id, smcols, currentstype, smile[1], method, seditorkey);
	}
}

function smilies_switch(id, smcols, type, page, method, seditorkey) {
	page = page ? page : 1;
	if(!smilies_array[type] || !smilies_array[type][page]) return;
	setcookie('smile', type + 'D' + page, 31536000);
	smiliesdata = '<table id="' + id + '_table" cellpadding="0" cellspacing="0" style="clear: both"><tr>';
	j = k = 0;
	img = new Array();
	for(i in smilies_array[type][page]) {
		if(j >= smcols) {
			smiliesdata += '<tr>';
			j = 0;
		}
		s = smilies_array[type][page][i];
		//note 5d6d edit
		if(s[2].substr(0, 4) == 'http') {
			smilieimg = s[2];
		} else if(smilies_type[type][1].substr(0, 4) == 'http') {
			smilieimg = smilies_type[type][1] + '/' + s[2];
		} else {
			smilieimg = 'images/smilies/' + smilies_type[type][1] + '/' + s[2];
		}

		img[k] = new Image();
		img[k].src = smilieimg;
		smiliesdata += s && s[0] ? '<td onmouseover="smilies_preview(\'' + id + '\', this, ' + s[5] + ')" onmouseout="smilies_preview(\'' + id + '\')" onclick="' + (method ? 'insertSmiley(' + s[0] + ')': 'seditor_insertunit(\'' + seditorkey + '\', \'' + s[1].replace(/'/, '\\\'') + '\')') +
			'"><img id="smilie_' + s[0] + '" width="' + s[3] +'" height="' + s[4] +'" src="' + smilieimg + '" alt="' + s[1] + '" />' : '<td>';
		j++;k++;
	}
	smiliesdata += '</table>';
	smiliespage = '';
	if(smilies_array[type].length > 2) {
		prevpage = ((prevpage = parseInt(page) - 1) < 1) ? smilies_array[type].length - 1 : prevpage;
		nextpage = ((nextpage = parseInt(page) + 1) == smilies_array[type].length) ? 1 : nextpage;
		smiliespage = '<div class="pags_act"><a href="javascript:;" onclick="smilies_switch(\'' + id + '\', \'' + smcols + '\', ' + type + ', ' + prevpage + ', ' + method + ', \'' + seditorkey + '\')">上页</a>' +
			'<a href="javascript:;" onclick="smilies_switch(\'' + id + '\', \'' + smcols + '\', ' + type + ', ' + nextpage + ', ' + method + ', \'' + seditorkey + '\')">下页</a></div>' +
			page + '/' + (smilies_array[type].length - 1);
	}
	$(id + '_data').innerHTML = smiliesdata;
	$(id + '_page').innerHTML = smiliespage;
}

function smilies_preview(id, obj, v) {
	if(!obj) {
		$(id + '_preview_table').style.display = 'none';
	} else {
		$(id + '_preview_table').style.display = '';
		$(id + '_preview').innerHTML = '<img src="' + obj.childNodes[0].src + '" />';
	}
}

//SEditor
function seditor_ctlent(event, script) {
	if(event.ctrlKey && event.keyCode == 13 || event.altKey && event.keyCode == 83) {
		eval(script);
	}
}

function parseurl(str, mode, parsecode) {
	if(!parsecode) str= str.replace(/\s*\[code\]([\s\S]+?)\[\/code\]\s*/ig, function($1, $2) {return codetag($2);});
	str = str.replace(/([^>=\]"'\/]|^)((((https?|ftp):\/\/)|www\.)([\w\-]+\.)*[\w\-\u4e00-\u9fa5]+\.([\.a-zA-Z0-9]+|\u4E2D\u56FD|\u7F51\u7EDC|\u516C\u53F8)((\?|\/|:)+[\w\.\/=\?%\-&~`@':+!]*)+\.(jpg|gif|png|bmp))/ig, mode == 'html' ? '$1<img src="$2" border="0">' : '$1[img]$2[/img]');
	str = str.replace(/([^>=\]"'\/@]|^)((((https?|ftp|gopher|news|telnet|rtsp|mms|callto|bctp|ed2k|thunder|synacast):\/\/))([\w\-]+\.)*[:\.@\-\w\u4e00-\u9fa5]+\.([\.a-zA-Z0-9]+|\u4E2D\u56FD|\u7F51\u7EDC|\u516C\u53F8)((\?|\/|:)+[\w\.\/=\?%\-&~`@':+!#]*)*)/ig, mode == 'html' ? '$1<a href="$2" target="_blank">$2</a>' : '$1[url]$2[/url]');
	str = str.replace(/([^\w>=\]"'\/@]|^)((www\.)([\w\-]+\.)*[:\.@\-\w\u4e00-\u9fa5]+\.([\.a-zA-Z0-9]+|\u4E2D\u56FD|\u7F51\u7EDC|\u516C\u53F8)((\?|\/|:)+[\w\.\/=\?%\-&~`@':+!#]*)*)/ig, mode == 'html' ? '$1<a href="$2" target="_blank">$2</a>' : '$1[url]$2[/url]');
	str = str.replace(/([^\w->=\]:"'\.\/]|^)(([\-\.\w]+@[\.\-\w]+(\.\w+)+))/ig, mode == 'html' ? '$1<a href="mailto:$2">$2</a>' : '$1[email]$2[/email]');
	if(!parsecode) {
		for(var i = 0; i <= codecount; i++) {
			str = str.replace("[\tDISCUZ_CODE_" + i + "\t]", codehtml[i]);
		}
	}
	return str;
}

function codetag(text) {
	codecount++;
	if(typeof wysiwyg != 'undefined' && wysiwyg) text = text.replace(/<br[^\>]*>/ig, '\n').replace(/<(\/|)[A-Za-z].*?>/ig, '');
	codehtml[codecount] = '[code]' + text + '[/code]';
	return '[\tDISCUZ_CODE_' + codecount + '\t]';
}

function seditor_insertunit(key, text, textend, moveend) {
	$(key + 'message').focus();
	textend = isUndefined(textend) ? '' : textend;
	moveend = isUndefined(textend) ? 0 : moveend;
	startlen = strlen(text);
	endlen = strlen(textend);
	if(!isUndefined($(key + 'message').selectionStart)) {
		var opn = $(key + 'message').selectionStart + 0;
		if(textend != '') {
			text = text + $(key + 'message').value.substring($(key + 'message').selectionStart, $(key + 'message').selectionEnd) + textend;
		}
		$(key + 'message').value = $(key + 'message').value.substr(0, $(key + 'message').selectionStart) + text + $(key + 'message').value.substr($(key + 'message').selectionEnd);
		if(!moveend) {
			$(key + 'message').selectionStart = opn + strlen(text) - endlen;
			$(key + 'message').selectionEnd = opn + strlen(text) - endlen;
		}
	} else if(document.selection && document.selection.createRange) {
		var sel = document.selection.createRange();
		if(textend != '') {
			text = text + sel.text + textend;
		}
		sel.text = text.replace(/\r?\n/g, '\r\n');
		if(!moveend) {
			sel.moveStart('character', -endlen);
			sel.moveEnd('character', -endlen);
		}
		sel.select();
	} else {
		$(key + 'message').value += text;
	}
	hideMenu();
}

function pmchecknew() {
	ajaxget('pm.php?checknewpm=' + Math.random(), 'pm_ntc', 'ajaxwaitid');
}

function pmviewnew() {
	if(!$('pm_ntc_menu')) {
		var div = document.createElement("div");
		div.id = 'pm_ntc_menu';
		div.style.display = 'none';
		$('append_parent').appendChild(div);
		div.innerHTML = '<div id="pm_ntc_view"></div>';
	}
	showMenu('pm_ntc');
	if($('pm_ntc_view').innerHTML == '') {
		ajaxget('pm.php?action=viewnew', 'pm_ntc_view', 'ajaxwaitid');
	}
}

function creditnoticewin() {
	if(!(creditnoticedata = getcookie('discuz_creditnotice'))) {
		return;
	}
	if(getcookie('discuz_creditnoticedisable')) {
		return;
	}
	creditnoticearray = creditnoticedata.split('D');
	if(creditnoticearray[9] != discuz_uid) {
		return;
	}
	creditnames = creditnotice.split(',');
	creditinfo = new Array();
	for(i in creditnames) {
		var e = creditnames[i].split('|');
		creditinfo[e[0]] = [e[1], e[2]];
	}
	s = '';
	for(i = 1;i <= 8;i++) {
		if(creditnoticearray[i] != 0 && creditinfo[i]) {
			s += '<span>' + creditinfo[i][0] + (creditnoticearray[i] > 0 ? '<em>+' : '<em class="desc">') + creditnoticearray[i] + '</em>' + creditinfo[i][1] + '</span>';
		}
	}
	setcookie('discuz_creditnotice', '', -2592000);
	if(s) {
		noticewin(s, 2000, 1);
	}
}

function noticewin(s, t, c) {
	c = !c ? '' : c;
	s = !c ? '<span style="font-style: normal;">'+s+'</span>' : s;
	s = '<table cellspacing="0" cellpadding="0" class="popupcredit"><tr><td class="pc_l">&nbsp;</td><td class="pc_c"><div class="pc_inner">' + s +
		(c ? '<a class="pc_btn" href="javascript:;" onclick="display(\'ntcwin\');setcookie(\'discuz_creditnoticedisable\', 1, 31536000);" title="不要再提示我"><img src="' + IMGDIR + '/popupcredit_btn.gif" alt="不要再提示我" /></a>' : '') +
		'</td><td class="pc_r">&nbsp;</td></tr></table>';
	if(!$('ntcwin')) {
		var div = document.createElement("div");
		div.id = 'ntcwin';
		div.style.display = 'none';
		div.style.position = 'absolute';
		div.style.zIndex = '100000';
		$('append_parent').appendChild(div);
	}
	$('ntcwin').innerHTML = s;
	$('ntcwin').style.display = '';
	$('ntcwin').style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=0)';
	$('ntcwin').style.opacity = 0;
	pbegin = document.documentElement.scrollTop + (document.documentElement.clientHeight / 2);
	pend = document.documentElement.scrollTop + (document.documentElement.clientHeight / 5);
	setTimeout(function () {noticewin_show(pbegin, pend, 0, t)}, 10);
	$('ntcwin').style.left = ((document.documentElement.clientWidth - $('ntcwin').clientWidth) / 2) + 'px';
	$('ntcwin').style.top = pbegin + 'px';
}

function noticewin_show(b, e, a, t) {
	step = (b - e) / 10;
	newp = (parseInt($('ntcwin').style.top) - step);
	if(newp > e) {
		$('ntcwin').style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + a + ')';
		$('ntcwin').style.opacity = a / 100;
		$('ntcwin').style.top = newp + 'px';
		setTimeout(function () {noticewin_show(b, e, a += 10, t)}, 10);
	} else {
		$('ntcwin').style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=100)';
		$('ntcwin').style.opacity = 1;
		setTimeout('display_opacity(\'ntcwin\', 100)', t);
	}
}

function showimmestatus(imme) {
	var lang = {'Online':'MSN 在线','Busy':'MSN 忙碌','Away':'MSN 离开','Offline':'MSN 脱机'};
	$('imme_status_' + imme.id.substr(0, imme.id.indexOf('@'))).innerHTML = lang[imme.statusText];
}

var discuz_uid = isUndefined(discuz_uid) ? 0 : discuz_uid;
var creditnotice = isUndefined(creditnotice) ? '' : creditnotice;
var cookiedomain = isUndefined(cookiedomain) ? '' : cookiedomain;
var cookiepath = isUndefined(cookiepath) ? '' : cookiepath;

if(typeof IN_ADMINCP == 'undefined') {
	if(discuz_uid && !getcookie('checkpm')) {
		_attachEvent(window, 'load', pmchecknew, document);
	}
	if(creditnotice != '' && getcookie('discuz_creditnotice') && !getcookie('discuz_creditnoticedisable')) {
		_attachEvent(window, 'load', function(){creditnoticewin()}, document);
	}
}