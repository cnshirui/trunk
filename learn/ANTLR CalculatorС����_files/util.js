var APP_DOMAIN = "club.163.com";
var DOMAIN = "163.com";
var isIE  = (navigator.appVersion.indexOf("MSIE") != -1) ? true : false;

function ajax_load(jsurl, callback){
	var myajax=new Ajax.Request(jsurl,{method:'get',parameters:'',requestHeaders:["If-Modified-Since","0"],onComplete:callback});
}

function setHomePageUtil(url){
	if(document.all){
		document.body.style.behavior="url(#default#homepage)";
		document.body.setHomePage(url);
	}else{
		alert("浏览器不支持，请手动设置为首页");
	}
	return false;
}

function createJST(jstId, jstContent){
	var textarea = document.createElement('textarea');
	textarea.value = jstContent;
	textarea.id = jstId;
	textarea.style.display = 'none';
	document.body.appendChild(textarea);
}

function createJSTAndParse(jstId, jstContent){
	createJST(jstId, jstContent);
	return TrimPath.parseDOMTemplate(jstId);
}

var Cookie = {
	set : function(name, value, expirationInDays, path, domain) {
		var cookie = escape(name) + "=" + escape(value);
		if (expirationInDays) {
			var date = new Date();
			date.setDate(date.getDate() + expirationInDays);
			cookie += "; expires=" + date.toGMTString();
		} 

		if (path) {
			cookie += ";path=" + path;
		}
		if (domain) {
			cookie += ";domain=" + domain;
		}
		
		document.cookie = cookie;

		if (value && (expirationInDays == undefined || expirationInDays > 0) && !this.get(name)) {
			return false;
		}
	},

	clear : function(name, path, ckDomain) {
		this.set(name, "", -1, path, ckDomain);
	},
	
	get : function(name) {
		var pattern = "(^|;)\\s*" + escape(name) + "=([^;]+)";
		var m = document.cookie.match(pattern);
		if (m && m[2]) {			
			return unescape(m[2]);
		}else{ 
			return null;
		}
	}
}   

/******************************获取对象的定位******************************/
//定位函数
function getBrowserPositionX(obj){
	var res = obj.offsetWidth + 5;
	while (obj != null){
		res += obj.offsetLeft;
		obj = obj.offsetParent;
	}
	return res;
}

function getBrowserPositionY(obj){
	var res = 0;
	while (obj != null){
		res += obj.offsetTop;
		obj = obj.offsetParent;
	}
	return res;
}

/******************************去除字符串的头尾空格******************************/
function Trim(TRIM_VALUE){
	if(TRIM_VALUE.length < 1){
		return"";
	}
	TRIM_VALUE = RTrim(TRIM_VALUE);
	TRIM_VALUE = LTrim(TRIM_VALUE);
	if(TRIM_VALUE==""){
		return "";
	}
	else{
		return TRIM_VALUE;
	}
} //End Function

function RTrim(VALUE){
	var w_space = String.fromCharCode(32);
	var v_length = VALUE.length;
	var strTemp = "";
	if(v_length < 0){
		return"";
	}
	var iTemp = v_length -1;

	while(iTemp > -1){
		if(VALUE.charAt(iTemp) == w_space){
		}
		else{
			strTemp = VALUE.substring(0,iTemp +1);
			break;
		}
		iTemp = iTemp-1;

	} //End While
	return strTemp;

} //End Function

function LTrim(VALUE){
	var w_space = String.fromCharCode(32);
	if(v_length < 1){
		return"";
	}
	var v_length = VALUE.length;
	var strTemp = "";

	var iTemp = 0;

	while(iTemp < v_length){
		if(VALUE.charAt(iTemp) == w_space){
		}
		else{
			strTemp = VALUE.substring(iTemp,v_length);
			break;
		}
		iTemp = iTemp + 1;
	} //End While
	return strTemp;
} //End Function

function stripData(content, noStripTags, additionBadTags){
	var badContent=["head","script","style","object","applet","noscript","frameset","noframes"];
	var badTag = ["form","meta","body","html","label","select","optgroup","option",
				"textarea","title","script", "xmp", "applet","embed","head","frameset",
				"iframe","noframes","noscript","object","style",
				"input","base","basefont","isindex","link","frame","param","xml","xss","st1:chsdate"];
    if (null!=additionBadTags && undefined!=additionBadTags){
        badTag=badTag.concat(additionBadTags);
    }
	var badAction = ["onabort", "onblur","onchange","onclick","ondblclick",
					"ondragdrop","onerror","onfocus","onkeydown","onkeypress",
					"onkeyup","onload","onmousedown","onmousemove","onmouseout",
					"onmouseover","onmouseup","onmove","onreset","onresize","onselect",
					"onsubmit","onunload","allowScriptAccess","disabled","id","name"];
	var badCss = ["position","javascript","vbscript","actionscript", "xmp", "activex"];
	var isHarm=false;
	var regStr;
	var reg = new RegExp("(&#)|(&%)","ig");	
	if(reg.test(content)){
		//isHarm = true;
		content = content.replace(reg,"&");			
	}
	delete reg;
	reg = new RegExp("(\t)","ig");
	if(reg.test(content)){
		//isHarm = true;
		content = content.replace(reg," ");
	}
	delete reg;
	regStr = "(<[^<>]*)(\r|\n)([^>]*>)";	
	reg = new RegExp(regStr,"ig");
	while(reg.test(content)){
		//isHarm = true;
		content = content.replace(reg,"$1 $3");
		delete reg;	
		reg = new RegExp(regStr,"ig");
	}
	delete reg;
	regStr = "(<[^<>]*)(\\\\|/\\*.*\\*/)([^>]*>)";
	reg = new RegExp(regStr,"ig");
	while(reg.test(content)){
		//isHarm = true;
		content = content.replace(reg,"$1$3");
		delete reg;	
		reg = new RegExp(regStr,"ig");
	}
	delete reg;		
	for(var i=0;i<badContent.length;i++){
		if (findNoStripTag(badContent[i],noStripTags))
			continue;
		regStr="<\\s*"+badContent[i]+"[^>]*>[\\s\\S]*?<\\s*/\\s*"+badContent[i]+"[^>]*>";
		reg = new RegExp(regStr,"ig");
		while(reg.test(content)){
			isHarm = true;
			content = content.replace(reg,"");
			delete reg;
			reg = new RegExp(regStr,"ig");
		}
		delete reg;				
	}
	for(var i=0;i<badTag.length;i++){
		if (findNoStripTag(badTag[i],noStripTags))
			continue;
		regStr="<\\s*[/\?]?\\s*"+badTag[i]+"[^>]*>";
		reg = new RegExp(regStr,"ig");

		while(reg.test(content)){
			isHarm = true;
			content = content.replace(reg,"");
			delete reg;
			reg = new RegExp(regStr,"ig");
		}
		delete reg;				
	}
	for(var i=0;i<badAction.length;i++){
		if (findNoStripTag(badAction[i],noStripTags))
			continue;
		regStr="(<[^<]*[\\s'\"])"+badAction[i]+"\\s*=\\s*['\"]?[^\\s'\">]*[\\s'\"]?([^>]*>)";
		reg = new RegExp(regStr,"ig");
		while(reg.test(content)){
			if(badAction[i]!="allowScriptAccess")
				isHarm = true;
			content = content.replace(reg,"$1$2");
			delete reg;
			reg = new RegExp(regStr,"ig");
		}
		delete reg;		
	}	
	reg = new RegExp("(<\\s*embed)([^>]*>)","ig");
	if(reg.test(content)){
		 content = content.replace(reg,"$1 allowScriptAccess=\"never\" $2");		
	}
	delete reg;
	for(var i=0;i<badCss.length;i++){
		if (findNoStripTag(badCss[i],noStripTags))
			continue;
		regStr="(<[^<]*)"+badCss[i]+"\\s*:\\s*[^\\s;\">]*([^>]*>)";
		reg = new RegExp(regStr,"ig");
		while(reg.test(content)){
			isHarm = true;
			content = content.replace(reg,"$1$2");
			delete reg;
			reg = new RegExp(regStr,"ig");
		}
		delete reg;		
	}
	regStr = "(<[^<]*)expression\\s*\\([^\\)]*\\)([^>]*>)";	
	reg = new RegExp(regStr,"ig");	
	while(reg.test(content)){
		isHarm = true;
		content = content.replace(reg,"$1$2");
		delete reg;
		reg = new RegExp(regStr,"ig");
	}
	delete reg;
	regStr = "(<[^<]*)url\\s*\\([^\\)]*\\.(js|do)\\s*\\)([^>]*>)";	
	reg = new RegExp(regStr,"ig");	
	while(reg.test(content)){
		isHarm = true;
		content = content.replace(reg,"$1$3");
		delete reg;
		reg = new RegExp(regStr,"ig");
	}
	delete reg;	
	regStr = "(<[^<]*[\\s'\"])src\\s*=\\s*['\"]?.*?\\.(js|do)(>)";	
	reg = new RegExp(regStr,"ig");	
	while(reg.test(content)){
		isHarm = true;
		content = content.replace(reg,"$1$3");
		delete reg;
		reg = new RegExp(regStr,"ig");
	}
	delete reg;
	regStr = "(<[^<]*[\\s'\"])src\\s*=\\s*['\"]?.*?\\.(js|do)[\\s'\"]([^>]*>)";	
	reg = new RegExp(regStr,"ig");	
	while(reg.test(content)){
		isHarm = true;
		content = content.replace(reg,"$1$3");
		delete reg;
		reg = new RegExp(regStr,"ig");
	}
	delete reg;	
	var retobj = {};
	retobj.content = content;
	retobj.isHarm = isHarm;
	return retobj;
}

function findNoStripTag(tag,tags){
	if(tags==null || tags=="")
		return false;
	for(var i=0;i<tags.length;i++){
		if(tags[i]==tag){
			return true;
		}
	}
	return false;
}

function addTagSlash(str){
	if(str==null)
		return str;
	return  str.replace(/(.)/ig,"\\\\?$1").replace("\\\\?","");
}
