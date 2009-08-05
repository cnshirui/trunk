var MinPreHeight = 450;
var MinPreChars  = 750;

var PreOpen = new Image();
var PreClose = new Image();
var PreCopy = new Image();
PreOpen.src="/images/plus.gif";
PreClose.src = "/images/minus.gif";
PreCopy.src = "/images/copy_16.png";

function togglePre()
{
	var id = this.getAttribute("preid");
	var preelm = document.getElementById("pre" + id);
	var imgelm = document.getElementById("preimg" + id);
	var togelm = document.getElementById("precollapse" + id);

	if (preelm.style.display != 'none') 
	{
		if (document.all) togelm.innerText = " Expand code snippet";
		else {
			document.getElementById("premain" + id).className = "precollapse";
			togelm.firstChild.nodeValue = " Expand code snippet";
		}
		preelm.style.display = 'none';
		imgelm.setAttribute("src", PreOpen.src);
	}
	else 
	{
		if (document.all) togelm.innerText = " Collapse code snippet";
		else {	
			document.getElementById("premain" + id).className = "SmallText";
			togelm.firstChild.nodeValue = " Collapse code snippet";
		}
		preelm.style.display = 'block';
		imgelm.setAttribute("src", PreClose.src);
	}
}

function CopyCode(name) {
	if (!document.all && !window.netscape) return false;
	
	var id = this.getAttribute("preid");
	if (id < 0) return false;
	
	var elm = document.getElementById("pre" + id.toString());
	if (!elm) return false;
	
	var inputText;
	if(document.all) inputText = elm.innerText;
	else inputText = elm.textContent;
		
	if (window.clipboardData) window.clipboardData.setData("Text", inputText);
	else if (window.netscape) {
		
		var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
		if (!clip) return;
		
		var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
		if (!trans) return;
		trans.addDataFlavor('text/unicode');

		var str = new Object();
		var len = new Object();
		var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
		str.data = inputText;

		trans.setTransferData("text/unicode", str, inputText.length * 2);

		var clipid = Components.interfaces.nsIClipboard;
		if (!clip) return false;

		clip.setData(trans, null, clipid.kGlobalClipboard);
	}

	return false;
}

function InitTogglePre() {
	var canCopy = document.all || window.netscape;

	var articleText = document.getElementById("contentdiv");
	if (!articleText) return;
	var pres = articleText.getElementsByTagName("pre");

	if (window.netscape) {
		try { netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect'); }
		catch (e) {canCopy = false; }
	}
	
	for (var i=0; i<pres.length; i++)
	{	
		var parent = pres[i].parentNode;			
			
		/*var wrap = document.createElement("div");
		wrap.className = "no-vmads";
		parent.insertBefore(wrap, pres[i]);
		var node = parent.removeChild(pres[i]);
		wrap.appendChild(node);	*/
		/*
		if (pres[i].offsetHeight == 0)
		{	if (pres[i].innerText.length < MinPreChars) continue; }
		else if (pres[i].offsetHeight < MinPreHeight)
			continue;
		*/
		
		var main = document.createElement("div");
		main.style.width="100%";
		main.setAttribute("id", "premain" + i.toString());

		var elm = document.createElement("img");
		elm.setAttribute("id", "preimg" + i.toString());
		elm.setAttribute("src", PreClose.src);
		if (document.all) elm.style.cursor = "pointer";
		elm.setAttribute("height", 9);
		elm.setAttribute("width", 9);
		elm.setAttribute("preid", i);
		elm.onclick = togglePre;

		main.appendChild(elm);	
	
		elm = document.createElement("span");
		elm.setAttribute("id", "precollapse" + i.toString());

		if (document.all)
		{
			main.className = "precollapse";
			elm.innerText = " Collapse";
			elm.style.cursor = "pointer";
		}
		else
		{
			main.className = "SmallText";
			main.style.cursor = "pointer";
			elm.textContent = " Collapse";
		}
		
		elm.style.marginBottom = 0;
		elm.onclick = togglePre;
		elm.setAttribute("preid", i);
	
		main.appendChild(elm);

		if (canCopy) {
			elm = document.createElement("img");
			elm.setAttribute("src", PreCopy.src);
			elm.setAttribute("height", 16);
			elm.setAttribute("width", 16)
			elm.style.marginLeft = "35px";
			main.appendChild(elm);

			elm = document.createElement("a");
			main.className = "SmallText";

			if (document.all) elm.innerText = " Copy Code";
			else elm.textContent = " Copy Code";

			elm.href = '#';
			elm.setAttribute("preid", i);
			elm.onclick = CopyCode;
			main.appendChild(elm);
		}
		
		//wrap.setAttribute("id", "pre" + i.toString());
		//wrap.style.marginTop = 0;
		pres[i].setAttribute("id", "pre" + i.toString());
		pres[i].style.marginTop = 0;
		
		var parent = pres[i].parentNode;
		parent.insertBefore(main, pres[i]);	
	}
}

InitTogglePre();