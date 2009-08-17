
//去掉字串左边的空格 
function lTrim(str) {
	if( str != null) {
		//如果字串左边第一个字符为空格
		if ( str.charAt(0) == " " || str.charAt(0) == "　") {
			str = str.substring(1, str.length);//将空格从字串中去掉
			//这一句也可改成 str = str.slice(1);
			str = lTrim(str);//递归调用
		}
	}
	return str;
}

//去掉字串右边的空格 
function rTrim(str) {
	if( str != null) {
		var iLength;
		iLength = str.length;
		//如果字串右边第一个字符为空格
		if ( str.charAt(iLength - 1) == " "  || str.charAt(0) == "　") {
			str = str.substring(0, iLength - 1);//将空格从字串中去掉
			//这一句也可改成 str = str.slice(0, iLength - 1);
			str = rTrim(str); //递归调用
		}
	}
	return str;
} 

//去掉字串两边的空格 
function trim(str) {
	return lTrim( rTrim(str) );
}

//判断是否为空值
function isEmpty(oldStr) {
	/*
	if( str == null || str == "" || trim(str) == "" ) {
		return true;
	}
	return false;
	*/
	var str = oldStr;
	var regex = /^\s+|　+$/;
	str = str.replace(regex,"");
	str = str.replace(regex,"");
	if (str == "" || str.length == 0)
		return true;
	return false;
}

//判断是否为是数字
function isPosInteger(number) {
	var regex = /^\d+$/;
	if (regex.test(number)){
		return true;
	}else{
		return false;
	}
}
	/*
	var str = number.toString();
	if( str.length > 9 ) return false;
	for( var i = 0; i < str.length; i++ ) {
		var c = str.charAt(i);
		if( i == 0 ) {
			if( c <= '0' || c > '9' ) {
				return false;
			}
		} else {
			if( c < '0' || c > '9' ) {
				return false;
			}
		}
	}
	return true;
	*/


//判断是否为有效电话号码
function isValidPhone(phone) {
	len = phone.length;
	pos_first = phone.indexOf("-");
	pos_last = phone.lastIndexOf("-");
	if( pos_first < 1 || pos_last == len -1 ) {
		return false;
	}
	for( var i = 0; i < len; i++ ) {
		var c = phone.charAt(i);
		if( c != "-" && (c < "0" || c > "9") ) {
			return false;
		}
	}
	return true;
}

//判断是否为有效email地址
function isValidEmailAddr(emailAddr) {
	len = emailAddr.length;
	pos_at_first = emailAddr.indexOf("@");
	pos_at_last = emailAddr.lastIndexOf("@");
	pos_dot_first = emailAddr.indexOf(".");
	pos_dot_last = emailAddr.lastIndexOf(".");
	if( pos_at_first != pos_at_last || pos_at_first < 1 || pos_at_last == len -1 || 
		pos_dot_first != pos_dot_last || pos_dot_first < 1 || pos_dot_last == len -1 || 
		pos_at_first > pos_dot_first ) {
		return false;
	}
	return true;
}

//判断是否为有效web地址
function isValidWebAddr(webAddr) {
	len = webAddr.length;
	pos_protocol_first = webAddr.indexOf("://");
	pos_dot_first = webAddr.indexOf(".");
	if( pos_protocol_first < 1 || pos_at_last == len -1 || 
		pos_dot_first < 1 || pos_dot_last == len -1 || 
		pos_protocol_first > pos_dot_first ) {
		return false;
	}
	return true;
}