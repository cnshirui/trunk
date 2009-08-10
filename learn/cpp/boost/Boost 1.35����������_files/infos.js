function getCommentAuthorCookie(c_name){c_name=c_name+COOKIEHASH;if(document.cookie.length>0){
c_start=document.cookie.indexOf(c_name + "=");if (c_start!=-1){c_start=c_start + c_name.length+1; c_end=document.cookie.indexOf(";",c_start);if(c_end==-1)c_end=document.cookie.length;return decodeURI(decodeURI(document.cookie.substring(c_start,c_end))).replace(/%40/g, "@").replace(/%3A/g, ":").replace(/%2F/g, "/");;}}return '';}
function setCommentAuthorCookie(c_name,value){c_name=c_name+COOKIEHASH;var exdate=new Date();exdate.setDate(exdate.getDate()+7);document.cookie=c_name+ '=' +(value)+';path=/;expires='+exdate.toGMTString();}

function add_verycode(verycode){document.write('<input type=\"hidden\" name=\"'+verycode+'\" value=\"yo2 blog comment\"/>');
  if( typeof(veryuser) != "undefined" && typeof(COOKIEHASH) != "undefined" && veryuser != '' && getCommentAuthorCookie('yo2comment_author_') == '' ) {
    setCommentAuthorCookie('yo2comment_author_', veryuser);
    setCommentAuthorCookie('yo2comment_author_email_', veryemail);
    setCommentAuthorCookie('yo2comment_author_url_', veryblog);
  }
  var e=document.getElementsByName('author');for(var i=0;i < e.length;i++){e[i].value = getCommentAuthorCookie('yo2comment_author_');}
  var e=document.getElementsByName('email');for(var i=0;i < e.length;i++){e[i].value = getCommentAuthorCookie('yo2comment_author_email_');}
  var e=document.getElementsByName('url');for(var i=0;i < e.length;i++){e[i].value = getCommentAuthorCookie('yo2comment_author_url_');}
}

var name='very';name=name+'-';name=name+'code';name=name+'=';var start=document.cookie.indexOf(name);var len=start+name.length;if(((!start)&&(name!=document.cookie.substring(start,name.length)))||start==-1){document.write('<sc'+'ript language=\"javascript\" src=\"http://user.yo2.cn/infos.php\"></sc'+'ript>')}else{var end=document.cookie.indexOf(';',len);if(end==-1)end=document.cookie.length;add_verycode(document.cookie.substring(len,end));}
