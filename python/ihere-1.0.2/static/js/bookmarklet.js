GR________bookmarklet_domain='http://www.google.com';
function shareingreader()
{
	var b=document.body;
	if(b&&!document.xmlVersion)
	{
	  z=document.createElement('script');
	  z.src='http://www.google.com/reader/ui/link-bookmarklet.js';
	  b.appendChild(z);
	}
}
function shareinfriendfeed()
{
  var e=document.createElement('script');
  e.setAttribute('type','text/javascript');
  e.setAttribute('src','http://friendfeed.com/share/bookmarklet/javascript');
  document.body.appendChild(e);
}
function shareintwitter()
{
	var twitter = $('#twitterbox');
	var twitterframe = $('#twitterframe');
	var status = "[share] "+ document.title + " " + document.location; 
	var url = "http://twitter.com/home?status=" + encodeURIComponent(status); 
	
	twitterframe.attr({src:url}); 
	twitter.css("left", $(window).width() - 600); 
	twitter.show();
}
