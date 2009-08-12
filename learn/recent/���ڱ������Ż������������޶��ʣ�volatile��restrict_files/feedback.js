/// <reference path="jquery-1.2.6-intellisense.js" />

$(document).ready(function() {
    LoadFeedback(CurrentEntryId, CurrentUserName);
    $("#MzImgExpPwd").attr("src","'ShowExPwd.aspx?temp='+ (new Date().getTime().toString(36)); return false;");
    $("#MzImgExpPwdA").bind("click","document.getElementById('MzImgExpPwd').src='ShowExPwd.aspx?temp='+ (new Date().getTime().toString(36)); return false;");
});

function getCookie(name) {
    var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    if (arr != null) {
        return unescape(arr[2]);
    }
    return null;
}
var usernameLogin = getCookie("activeUserName");
var isGuest = !usernameLogin || usernameLogin == "Guest";

String.prototype.format = function() {
    var str = this;
    for (var i = 0; i < arguments.length; i++) {
        var re = new RegExp('\\{' + (i) + '\\}', 'gm');
        str = str.replace(re, arguments[i]);
    }
    return str;
}

String.prototype.HtmlEncode = function() {
    return this.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/\n/g, '<br />').replace(/\r/g, '');
}

var JsonDateTime2String = function(JsongDateTimeString) {
    var datetimeString = JsongDateTimeString.replace('/', 'new ').replace('/', '');
    datetimeString = '{"DateCreated":' + datetimeString + '}';
    obj = eval('(' + datetimeString + ')');
    datetimeString = obj.DateCreated.toLocaleDateString() + ' ' + obj.DateCreated.toLocaleTimeString();
    return datetimeString;
}

var t_CommentHost = "http://comments.blog.csdn.net";
//{0} = 标题
//{1} = 文章链接
//{2} = 评论ID
//{3} = 评论用户名
//{4} = 评论时间
//{5} = 评论IP
//{6} = 评论内容
var bodyFeedbackDiv = "<dl class=\"question\"><dt><a id=\"{2}\" style=\"display: none\" title=\"permalink: {0}\" href=\"{1}#{2}\"></a><a href=\"http://hi.csdn.net/{3}\" target=\"_blank\" rel=\"nofollow\">{3}</a>&nbsp;发表于{4}&nbsp;&nbsp;<span style='display:none;'>IP:{5}</span><a href=\"mailto:yuexn@csdn.net?subject=Comment Report!!!&body=Author:{3} URL:{1}\">举报</a></dt><dd>{6}</dd></dl>";
var FeedbackSubmitDivLogin = "<fieldset><legend>给{0}的留言<dfn>只有注册用户才能发表评论！<a href=\"http://passport.csdn.net/member/UserLogin.aspx?from={1}\">登录</a><a href=\"http://passport.csdn.net/CSDNUserRegister.aspx\">注册</a></dfn></legend><em><img src=\"" + t_CommentHost + "/images/blog_commentnotice.gif\" alt=\"\" title=\"\" /></em><ul><li>姓&nbsp;&nbsp;&nbsp;名：<input type=\"text\" size=\"25\" /></li><li>校验码：<input type=\"text\" size=\"25\" /></li></ul><textarea cols=\"50\" rows=\"8\"></textarea><input type=\"button\" value=\"提交留言\" class=\"submitbtn\" /></fieldset>";
var FeedbackSubmitDiv = "<fieldset><legend发表评论:<ul><li>姓&nbsp;&nbsp;&nbsp;名：<input id=\"UserName\" readonly=\"readonly\" type=\"text\" value=\"{0}\" size=\"25\"/></li><li>校验码：<input id=\"ExPwd\" type=\"text\" size=\"25\" /><img id=\"MzImgExpPwd\" alt=\"\" src=\"" + t_CommentHost + "/AntiBotImage.ashx?entryId={1}\" /><a href=\"javascript:ChangeIdentifyingCode()\" id=\"MzImgExpPwdA\" onclick=\"\">重新获得验证码</a></li></ul><textarea cols=\"50\" rows=\"8\" id=\"content\"></textarea><input type=\"button\" value=\"提交留言\" class=\"submitbtn\" id=\"SubmitFeedback\" /></fieldset>";
//拼接一条评论正文Html
var buildContent = function(Title, SourceUrl, Id, Author, DateCreated, IpAddress, Body) {
    return bodyFeedbackDiv.format(Title, SourceUrl, Id, Author, JsonDateTime2String(DateCreated), IpAddress, Body);
}

var t_CommentHandler = t_CommentHost + "/feedback.ashx?jsoncallback=?&{0}";
function LoadFeedback(EntryId, UserName) {
    var queryString = "action=get&entryId=" + EntryId + "&userName=" + UserName + "&d=" + Math.random();
    $.getJSON(t_CommentHandler.format(queryString), function(data) {
        $.each(data, function(i, domEle) {
            $('#commentslist').append(buildContent(domEle._title, domEle._sourceurl, domEle._id, domEle._author, domEle._datecreated, domEle.ipAddress, domEle._body));
        });
        $('.spacecommment').children().remove();
        if (isGuest) {
            $('.spacecommment').append(FeedbackSubmitDivLogin.format(UserName, location));
        }
        else {
            $('.spacecommment').append(FeedbackSubmitDiv.format(usernameLogin, EntryId));
            $("#SubmitFeedback").bind("click", submitContentFun);
        }
        LoadReply(EntryId, UserName);
    });
}
function LoadReply(EntryId, UserName) {
    var queryString = "action=getreply&entryId=" + EntryId + "&userName=" + UserName + "&d=" + Math.random();
    $.getJSON(t_CommentHandler.format(queryString), function(data) {
    $.each(data, function(i, domEle) {
            var feedbackId = domEle.FeedbackId;
            var feedbackUser = $('#' + feedbackId).next().text();
            $('#' + feedbackId).parent().parent().after(buildContent(domEle.Title, "", domEle.ReplyId, UserName, domEle.DateCreated, domEle.IpAddress, "Re " + feedbackUser + ": " + domEle.Body));
        });
    });
}

function ChangeIdentifyingCode()
{
    var url = $('#MzImgExpPwd').attr('src');
    if (!(/&d=[\d\.]+$/).test(url)) url += "&d=1";
    url = url.replace(/&d=[\d\.]+$/, "&d=" + Math.random())
    $('#MzImgExpPwd').attr('src', url);
}

var submitContentFun = function()
{
    var actionUserName = usernameLogin;
    var ownerUserName = CurrentUserName;
    var EntryId = CurrentEntryId;
    var Title = "Re:" + $('.title_txt').text().substr(1);
    var Content = $("#content").val();
    var ExPwd = $("#ExPwd").val();
    var url = encodeURIComponent(document.URL);
    if(url.lastIndexOf('#') != -1)
    {
        url = url.substr(0,url.lastIndexOf('#'));
    }
    var queryString = "action=post&url=" + url + "&entryId=" + EntryId + "&exPwd=" + ExPwd + "&userName=" + ownerUserName + "&title=" + escape(Title) + "&actionUserName=" + actionUserName + "&content=" + escape(Content) + "&d=" + Math.random();
    $("#SubmitFeedback").unbind("click");
    $("#SubmitFeedback").val("正在提交，请稍候...");
    $.getJSON(t_CommentHandler.format(queryString), function(response) {
        if (response.Status > 0) {
            $('#commentslist').append(bodyFeedbackDiv.format(Title, location, response.CommentId, actionUserName, new Date().toLocaleString(), response.ClientIp, Content.HtmlEncode()));
            $("#content").val("");
            $("#ExPwd").val("");
            ChangeIdentifyingCode();
        }
        else
        {
            alert(response.ErrorMsg);
        }
        $("#SubmitFeedback").bind("click",submitContentFun);
        $("#SubmitFeedback").val("提交");
    });
}