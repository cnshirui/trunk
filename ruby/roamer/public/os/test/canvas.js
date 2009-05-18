var viewerFriends;       // Type：opensocial.Collection

function onLoadmore_f(vf) {
    var html = "";
    var isOwner = vf.isOwner();
    html += "isOwner: "+isOwner;
    html += "<br>";

    var isViewer = vf.isViewer();
    html += "isViewer: "+isViewer;
    html += "<br>";

    var displayName = vf.getDisplayName();
    html += "Username: "+displayName;
    html += "<br>";

    var nickName = vf.getDisplayName();
    html += "Nickname: "+nickName;
    html += "<br>";

    var id = vf.getField(opensocial.Person.Field.ID);
    html += "Opensocial-ID: "+id;
    html += "<br>";
    
    document.write(html);
    document.close();
}

/* Display the basic informaiton */
function showBasic() {
    var html = new Array();

    viewerFriends.each(
            function(friend) {
                html.push('<li>'+'<ul>'+'<a href="" onclick="onLoadmore_f('+friend.getId()+')" >' +'<img src='+friend.getField(opensocial.Person.Field.THUMBNAIL_URL)+'>'+friend.getDisplayName()+'</a>'+'</ul>'+'</li>');
            }
        );

    document.getElementById('friends').innerHTML = html.join('');
}

/* Send Opensocial API request. */
function reloadAll() {
    var req = new opensocial.DataRequest();//Create a Object"req" of class "opensocial.DataRequest".
    req.add(req.newFetchPeopleRequest('VIEWER_FRIENDS'), 'vf');
    req.send(onReloadAll);
}

/* Respond to requests */
function onReloadAll(dataResponse) {
    viewerFriends = dataResponse.get('vf').getData() || {};

    /* Show the data */
    showBasic();
}

/*Execute the request function when the application is finished loading.*/
function init() {
    reloadAll();
}

gadgets.util.registerOnLoadHandler(init);
