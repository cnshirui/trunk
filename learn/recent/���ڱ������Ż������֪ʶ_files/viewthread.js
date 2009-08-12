/*
	[Discuz!] (C)2001-2009 Comsenz Inc.
	This is NOT a freeware, use is subject to license terms

	$Id: viewthread.js 17535 2009-01-20 05:12:20Z monkey $
*/

var replyreload = '';

function attachimgshow(pid) {
	aimgs = aimgcount[pid];
	aimgcomplete = 0;
	loadingcount = 0;
	for(i = 0;i < aimgs.length;i++) {
		obj = $('aimg_' + aimgs[i]);
		if(!obj) {
			break;
		}
		if(!obj.status) {
			obj.status = 1;
			obj.src = obj.getAttribute('file');
			loadingcount++;
		} else if(obj.status == 1) {
			if(obj.complete) {
				obj.status = 2;				
			} else {
				loadingcount++;				
			}
		} else if(obj.status == 2) {
			aimgcomplete++;
			if(obj.getAttribute('thumbImg')) {
				thumbImg(obj);
			}
		}
		if(loadingcount >= 10) {
			break;
		}
	}
	if(aimgcomplete < aimgs.length) {
		setTimeout("attachimgshow('" + pid + "')", 100);
	}
}

function attachimginfo(obj, infoobj, show, event) {
	objinfo = fetchOffset(obj);
	if(show) {
		$(infoobj).style.left = objinfo['left'] + 'px';
		$(infoobj).style.top = obj.offsetHeight < 40 ? (objinfo['top'] + obj.offsetHeight) + 'px' : objinfo['top'] + 'px';
		$(infoobj).style.display = '';
	} else {
		if(is_ie) {
			$(infoobj).style.display = 'none';
			return;
		} else {
			var mousex = document.body.scrollLeft + event.clientX;
			var mousey = document.documentElement.scrollTop + event.clientY;
			if(mousex < objinfo['left'] || mousex > objinfo['left'] + objinfo['width'] || mousey < objinfo['top'] || mousey > objinfo['top'] + objinfo['height']) {
				$(infoobj).style.display = 'none';
			}
		}
	}
}

function copycode(obj) {
	setcopy(is_ie ? obj.innerText.replace(/\r\n\r\n/g, '\r\n') : obj.textContent, '代码已复制到剪贴板');
}

function signature(obj) {
	if(obj.style.maxHeightIE != '') {
		var height = (obj.scrollHeight > parseInt(obj.style.maxHeightIE)) ? obj.style.maxHeightIE : obj.scrollHeight + 'px';
		if(obj.innerHTML.indexOf('<IMG ') == -1) {
			obj.style.maxHeightIE = '';
		}
		return height;
	}
}

function tagshow(event) {
	var obj = is_ie ? event.srcElement : event.target;
	obj.id = !obj.id ? 'tag_' + Math.random() : obj.id;
	ajaxmenu(event, obj.id, 0, '', 1, 3, 0);
	obj.onclick = null;
}

var zoomobj = Array();var zoomadjust;var zoomstatus = 1;

function zoom(obj, zimg) {
	if(!zoomstatus) {
		window.open(zimg, '', '');
		return;
	}
	if(!zimg) {
		zimg = obj.src;
	}
	if(!$('zoomimglayer_bg')) {
		div = document.createElement('div');div.id = 'zoomimglayer_bg';
		div.style.position = 'absolute';
		div.style.left = div.style.top = '0px';
		div.style.zIndex = '998';
		div.style.width = '100%';
		div.style.height = document.body.scrollHeight + 'px';
		div.style.backgroundColor = '#000';
		div.style.display = 'none';
		div.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=80,finishOpacity=100,style=0)';
		div.style.opacity = 0.8;
		$('append_parent').appendChild(div);
		div = document.createElement('div');
		div.id = 'zoomimglayer';
		div.style.position = 'absolute';
		div.style.padding = 0;
		$('append_parent').appendChild(div);
	}
	zoomobj['srcinfo'] = fetchOffset(obj);
	zoomobj['srcobj'] = obj;
	zoomobj['zimg'] = zimg;
	zoomobj['id'] = 'zoom_' + Math.random();
	$('zoomimglayer').setAttribute('pid', obj.getAttribute('pid'));
	$('zoomimglayer').style.display = '';
	$('zoomimglayer').style.left = zoomobj['srcinfo']['left'] + 'px';
	$('zoomimglayer').style.top = zoomobj['srcinfo']['top'] + 'px';
	$('zoomimglayer').style.width = zoomobj['srcobj'].width + 'px';
	$('zoomimglayer').style.height = zoomobj['srcobj'].height + 'px';
	$('zoomimglayer').style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=40)';
	$('zoomimglayer').style.opacity = 0.4;
	$('zoomimglayer').style.zIndex = '999';
	$('zoomimglayer').innerHTML = '<table width="100%" height="100%" cellspacing="0" cellpadding="0"><tr><td align="center" valign="middle"><img src="' + IMGDIR + '/loading.gif"></td></tr></table><div style="position:absolute;top:-100000px;display:none"><img id="' + zoomobj['id'] + '" src="' + zoomobj['zimg'] + '"></div>';
	setTimeout('zoomimgresize($(\'' + zoomobj['id'] + '\'))', 100);
	if(is_ie) {
		doane(event);
	}
}

var zoomdragstart = new Array();
var zoomclick = 0;
function zoomdrag(e, op) {
	if(op == 1) {
		zoomclick = 1;
		zoomdragstart = is_ie ? [event.clientX, event.clientY] : [e.clientX, e.clientY];
		zoomdragstart[2] = parseInt($('zoomimglayer').style.left);
		zoomdragstart[3] = parseInt($('zoomimglayer').style.top);
		doane(e);
	} else if(op == 2 && zoomdragstart[0]) {
		zoomclick = 0;
		var zoomdragnow = is_ie ? [event.clientX, event.clientY] : [e.clientX, e.clientY];
		$('zoomimglayer').style.left = (zoomdragstart[2] + zoomdragnow[0] - zoomdragstart[0]) + 'px';
		$('zoomimglayer').style.top = (zoomdragstart[3] + zoomdragnow[1] - zoomdragstart[1]) + 'px';
		doane(e);
	} else if(op == 3) {
		if(zoomclick) zoomclose();
		zoomdragstart = [];
		doane(e);
	}
}

function zoomST(c) {
	if($('zoomimglayer').style.display == '') {
		$('zoomimglayer').style.left = (parseInt($('zoomimglayer').style.left) + zoomobj['x']) + 'px';
		$('zoomimglayer').style.top = (parseInt($('zoomimglayer').style.top) + zoomobj['y']) + 'px';
		$('zoomimglayer').style.width = (parseInt($('zoomimglayer').style.width) + zoomobj['w']) + 'px';
		$('zoomimglayer').style.height = (parseInt($('zoomimglayer').style.height) + zoomobj['h']) + 'px';
		c++;
		if(c <= 5) {
			setTimeout('zoomST(' + c + ')', 1);
		} else {
			$('zoomimglayer').style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=100,style=0)';
			$('zoomimglayer').style.opacity = 1;
			zoomadjust = 1;
			$('zoomimglayer').style.filter = '';
			$('zoomimglayer_bg').style.display = '';
			$('zoomimglayer').innerHTML = '<div class="zoominner"><p><span class="right"><a href="' + zoomobj['zimg'] + '" class="imglink" target="_blank" title="在新窗口打开">在新窗口打开</a><a href="javascipt:;" onclick="zoomimgadjust(event, 1)" class="imgadjust" title="实际大小">实际大小</a><a href="javascript:;" onclick="zoomclose()" class="imgclose" title="关闭">关闭</a></span>鼠标滚轮缩放图片</p><div id="zoomimgbox"><img id="zoomimg" style="cursor: move;" src="' + zoomobj['zimg'] + '" width="' + parseInt($('zoomimglayer').style.width) + '" height="' + parseInt($('zoomimglayer').style.height) + '"></div></div>';
			$('zoomimglayer').style.overflow = 'visible';
			$('zoomimglayer').style.width = (parseInt($('zoomimg').width < 300 ? 300 : parseInt($('zoomimg').width)) + 20) + 'px';
			$('zoomimglayer').style.height = (parseInt($('zoomimg').height) + 20) + 'px';
			if(is_ie){
				$('zoomimglayer').onmousewheel = zoomimgadjust;
			} else {
				$('zoomimglayer').addEventListener("DOMMouseScroll", zoomimgadjust, false);
			}
			$('zoomimgbox').onmousedown = function(event) {try{zoomdrag(event, 1);}catch(e){}};
			$('zoomimgbox').onmousemove = function(event) {try{zoomdrag(event, 2);}catch(e){}};
			$('zoomimgbox').onmouseup = function(event) {try{zoomdrag(event, 3);}catch(e){}};
		}
	}
}

function zoomimgresize(obj) {
	if(!obj.complete) {
		setTimeout('zoomimgresize($(\'' + obj.id + '\'))', 100);
		return;
	}
	obj.parentNode.style.display = '';
	zoomobj['zimginfo'] = [obj.width, obj.height];
	var r = obj.width / obj.height;
	var w = document.body.clientWidth * 0.95;
	w = obj.width > w ? w : obj.width;
	var h = w / r;
	var clientHeight = document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight;
	var scrollTop = document.body.scrollTop ? document.body.scrollTop : document.documentElement.scrollTop;
	if(h > clientHeight) {
		h = clientHeight;
		w = h * r;
	}
	var l = (document.body.clientWidth - w) / 2;
	var t = h < clientHeight ? (clientHeight - h) / 2 : 0;
	t += + scrollTop;
	zoomobj['x'] = (l - zoomobj['srcinfo']['left']) / 5;
	zoomobj['y'] = (t - zoomobj['srcinfo']['top']) / 5;
	zoomobj['w'] = (w - zoomobj['srcobj'].width) / 5;
	zoomobj['h'] = (h - zoomobj['srcobj'].height) / 5;
	$('zoomimglayer').style.filter = '';
	$('zoomimglayer').innerHTML = '';
	setTimeout('zoomST(1)', 5);
}

function zoomimgadjust(e, a) {
	if(!a) {
		if(!e) e = window.event;
		if(e.altKey || e.shiftKey || e.ctrlKey) return;
		var l = parseInt($('zoomimglayer').style.left);
		var t = parseInt($('zoomimglayer').style.top);
		if(e.wheelDelta <= 0 || e.detail > 0) {
			if($('zoomimg').width <= 200 || $('zoomimg').height <= 200) {
				doane(e);return;
			}
			$('zoomimg').width -= zoomobj['zimginfo'][0] / 10;
			$('zoomimg').height -= zoomobj['zimginfo'][1] / 10;
			l += zoomobj['zimginfo'][0] / 20;
			t += zoomobj['zimginfo'][1] / 20;
		} else {
			if($('zoomimg').width >= zoomobj['zimginfo'][0]) {
				zoomimgadjust(e, 1);return;
			}
			$('zoomimg').width += zoomobj['zimginfo'][0] / 10;
			$('zoomimg').height += zoomobj['zimginfo'][1] / 10;
			l -= zoomobj['zimginfo'][0] / 20;
			t -= zoomobj['zimginfo'][1] / 20;
		}
	} else {
		var clientHeight = document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight;
		var scrollTop = document.body.scrollTop ? document.body.scrollTop : document.documentElement.scrollTop;
		$('zoomimg').width = zoomobj['zimginfo'][0];$('zoomimg').height = zoomobj['zimginfo'][1];
		var l = (document.body.clientWidth - $('zoomimg').clientWidth) / 2;l = l > 0 ? l : 0;
		var t = (clientHeight - $('zoomimg').clientHeight) / 2 + scrollTop;t = t > 0 ? t : 0;
	}
	$('zoomimglayer').style.left = l + 'px';
	$('zoomimglayer').style.top = t + 'px';
	$('zoomimglayer_bg').style.height = t + $('zoomimglayer').clientHeight > $('zoomimglayer_bg').clientHeight ? (t + $('zoomimglayer').clientHeight) + 'px' : $('zoomimglayer_bg').style.height;
	$('zoomimglayer').style.width = (parseInt($('zoomimg').width < 300 ? 300 : parseInt($('zoomimg').width)) + 20) + 'px';
	$('zoomimglayer').style.height = (parseInt($('zoomimg').height) + 20) + 'px';
	doane(e);
}

function zoomclose() {
	$('zoomimglayer').innerHTML = '';
	$('zoomimglayer').style.display = 'none';
	$('zoomimglayer_bg').style.display = 'none';
}

function v_onPlayStart(videoId, isAvailability, length) {
	ajaxget('video.php?action=updatevideoinfo&vid=' + videoId, '');
}

function parsetag(pid) {
	if(!$('postmessage_'+pid)) {
		return;
	}
	var havetag = false;
	var tagfindarray = new Array();
	var str = $('postmessage_'+pid).innerHTML.replace(/(^|>)([^<]+)(?=<|$)/ig, function($1, $2, $3, $4) {
		for(i in tagarray) {
			if(tagarray[i] && $3.indexOf(tagarray[i]) != -1) {
				havetag = true;
				$3 = $3.replace(tagarray[i], '<h_ ' + i + '>');
				tmp = $3.replace(/&[a-z]*?<h_ \d+>[a-z]*?;/ig, '');
				if(tmp != $3) {
					$3 = tmp;
				} else {
					tagfindarray[i] = tagarray[i];
					tagarray[i] = '';
				}
			}
		}
		return $2 + $3;
    	});
    	if(havetag) {
		$('postmessage_'+pid).innerHTML = str.replace(/<h_ (\d+)>/ig, function($1, $2) {
			return '<span href=\"tag.php?name=' + tagencarray[$2] + '\" onclick=\"tagshow(event)\" class=\"t_tag\">' + tagfindarray[$2] + '</span>';
	    	});
	}
}

function setanswer(pid){
	if(confirm('您确认要把该回复选为“最佳答案”吗？')){
		if(is_ie) {
			doane(event);
		}
		$('modactions').action='misc.php?action=bestanswer&tid=' + tid + '&pid=' + pid + '&bestanswersubmit=yes';
		$('modactions').submit();
	}
}

var authort;
function showauthor(obj, id) {
	authort = setTimeout("$('" + id + "').style.display = 'block';", 500);
	if(!$(id).onmouseover) {
		$(id + '_ma').innerHTML = $(id + '_a').innerHTML;
		$(id).onmouseover = function() {
			$(id).style.display = 'block';
		}
		$(id).onmouseout = function() {
			$(id).style.display = 'none';
		}
	}
	if(!obj.onmouseout) {
		obj.onmouseout = function() {
			clearTimeout(authort);
		}
	}
}

function fastpostvalidate(theform) {
	s = '';
	if(theform.message.value == '' && theform.subject.value == '') {
		s = '请完成标题或内容栏。';
		theform.message.focus();
	} else if(mb_strlen(theform.subject.value) > 80) {
		s = '您的标题超过 80 个字符的限制。';
		theform.subject.focus();
	}
	if(!disablepostctrl && ((postminchars != 0 && mb_strlen(theform.message.value) < postminchars) || (postmaxchars != 0 && mb_strlen(theform.message.value) > postmaxchars))) {
		s = '您的帖子长度不符合要求。\n\n当前长度: ' + mb_strlen(theform.message.value) + ' ' + '字节\n系统限制: ' + postminchars + ' 到 ' + postmaxchars + ' 字节';
	}
	if(s) {
		$('fastpostreturn').className = 'onerror';
		$('fastpostreturn').innerHTML = s;
		$('fastpostsubmit').disabled = false;
		return false;
	}
	$('fastpostsubmit').disabled = true;
	theform.message.value = parseurl(theform.message.value);
	ajaxpost('fastpostform', 'fastpostreturn', 'fastpostreturn', 'onerror', $('replysubmit'));
	return false;
}

function fastpostappendreply() {
	newpos = fetchOffset($('post_new'));
	document.documentElement.scrollTop = newpos['top'];
	$('post_new').style.display = '';
	$('post_new').id = '';
	div = document.createElement('div');
	div.id = 'post_new';
	div.style.display = 'none';
	div.className = '';
	$('postlistreply').appendChild(div);
	$('fastpostsubmit').disabled = false;
	$('fastpostmessage').value = '';
	if($('secanswer3')) {
		$('checksecanswer3').innerHTML = '<img src="images/common/none.gif" width="17" height="17">';
		$('secanswer3').value = '';
		secclick3['secanswer3'] = 0;
	}
	if($('seccodeverify3')) {
		$('checkseccodeverify3').innerHTML = '<img src="images/common/none.gif" width="17" height="17">';
		$('seccodeverify3').value = '';
		secclick3['seccodeverify3'] = 0;
	}
	creditnoticewin();
}

function submithandle_fastpost(locationhref) {
	var pid = locationhref.lastIndexOf('#pid');
	if(pid != -1) {
		pid = locationhref.substr(pid + 4);
		ajaxget('viewthread.php?tid=' + tid + '&viewpid=' + pid, 'post_new', 'ajaxwaitid', '', null, 'fastpostappendreply()');
		if(replyreload) {
			var reloadpids = replyreload.split(',');
			for(i = 1;i < reloadpids.length;i++) {
				ajaxget('viewthread.php?tid=' + tid + '&viewpid=' + reloadpids[i], 'post_' + reloadpids[i]);
			}
		}
		$('fastpostreturn').className = '';
	} else {
		$('post_new').style.display = $('fastpostmessage').value = $('fastpostreturn').className = '';
		$('fastpostreturn').innerHTML = '本版回帖需要审核，您的帖子将在通过审核后显示';
	}
}

function messagehandle_fastpost() {
	$('fastpostsubmit').disabled = false;
}