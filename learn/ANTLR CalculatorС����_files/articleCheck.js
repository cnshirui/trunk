
function checkArticleInfos() {
	var f = document.articleInfoForm;

	//f.subject.value = trim(f.subject.value);
	//alert(f.subject.value );
	if( isEmpty(f.subject.value) ) {
		alert("请输入标题!");
		f.subject.focus();
		return false;
	} else	if( f.subject.value.length > 50 ) {
		alert("标题过长，不能超过50个字符!");
		f.subject.focus();
		return false;
	}
	//alert(f.body.value);
	if( isEmpty(f.body.value) ) {
		alert("请输入内容!");
		//f.body.focus();
		return false;
	} else	if( f.body.value.length > 20000 ) {
		alert("内容过长，不能超过20000个字符!");
		//f.body.focus();
		return false;
	}
	if(!isPosInteger(f.divs_putKey.value)){ 
		alert("验证码输入错误！");
		return false;
	}
	return true;
}

function checkArticleInfo() {
	var f = document.articleInfoForm;

	//f.subject.value = trim(f.subject.value);
	//alert(f.subject.value );
	if( isEmpty(f.subject.value) ) {
		alert("请输入标题!");
		f.subject.focus();
		return false;
	} else	if( f.subject.value.length > 50 ) {
		alert("标题过长，不能超过50个字符!");
		f.subject.focus();
		return false;
	}
	//alert(f.body.value);
	if( isEmpty(f.body.value) ) {
		alert("请输入内容!");
		//f.body.focus();
		return false;
	} else	if( f.body.value.length > 20000 ) {
		alert("内容过长，不能超过20000个字符!");
		//f.body.focus();
		return false;
	}
	return true;
}

function checkBoardIdInfo() {
	var f = document.boardIdInfoForm;

	//f.newBoardId.value = trim(f.newBoardId.value);
	if( isEmpty(f.newBoardId.value) ) {
		alert("请输入版ID!");
		f.newBoardId.focus();
		return false;
	} else	if( f.newBoardId.value.length > 15 ) {
		alert("版ID过长，不能超过15个字符!");
		f.newBoardId.focus();
		return false;
	}
	return true;
}

function htmlA()
{
	var location = prompt("请输入链接的地址：","http://");
	if(location == null)	return;
	var info = prompt("请输入链接的说明文字：","");
	if(info == null)	return;
		articleInfoForm.body.value=articleInfoForm.body.value+"<a target=\"_blank\" href=\""+location+"\">"+info+"</a>";
}
function htmlIMG()
{
	var location = prompt("请输入图片的地址：","http://");
	if(location == null)	return;
	var info = prompt("请输入图片的说明文字：","");
	if(info == null)	return;
	articleInfoForm.body.value=articleInfoForm.body.value+"<img src=\""+location+"\" alt=\""+info+"\">";
}
function htmlFONT()
{
	var content = prompt("请输入文字的内容：","");
	if(content == null)	return;
	var color = prompt("请输入文字的颜色(RRGGBB)： (如红色为red或ff0000)","");
	if(color == null)	return;
	var size = prompt("请输入文字的尺寸(0-4)","4");
	if(size == null) return;
	var b = confirm("是否用粗体字？");
	if(b == null) return;
	if(size < 0) size=0;
	if(size > 4) size=4;
	if(b)
		articleInfoForm.body.value=articleInfoForm.body.value+"<font color=\""+color+"\" size=\""+size+"\"><b>"+content+"</b></font>";
	else
		articleInfoForm.body.value=articleInfoForm.body.value+"<font color=\""+color+"\" size=\""+size+"\">"+content+"</font>";
}

function reimgSize()
{
    for (i=0;i<document.images.length;i++)
    {
        if(document.images[i].width>634)
        {
            document.images[i].width=634;
        }
    }
}
