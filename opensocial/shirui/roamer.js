document.write("<script   language=\"javascript\"   src=\"http://visapi-gadgets.googlecode.com/svn/trunk/pilesofmoney/pom.js\"></script>");
document.write("<script   language=\"javascript\"   src=\"http://www.google.com/jsapi\"></script>");

// ExternalInterface use
function thisMovie(movieName) {
    if(navigator.appName.indexOf("Microsoft") != -1) {
        return window[movieName];
    } else {
        return document[movieName];
    }
}

function sendToActionScript(value) {
    thisMovie("PersonDemo").sendToActionScript(value);
}

var usernames = {};
var thumbnails = {};
var selectedID = '';

function loadFriendsByUser(uid) {
    var req = opensocial.newDataRequest();

    if(uid.length == 0) {
        uid = "VIEWER";
    }
    else {
        var viewer = opensocial.newIdSpec({
            "userId" : uid,
            "groupId" : "SELF"
        });
        var opt_params = {};
        req.add(req.newFetchPeopleRequest(viewer, opt_params), 'viewer');
        selectedID =  uid;
    }
    req.add(req.newFetchPersonRequest(opensocial.IdSpec.PersonId.VIEWER), 'viewer');

    var viewerFriends = opensocial.newIdSpec({
        "userId" : uid,
        "groupId" : "FRIENDS"
    });
    var opt_params = {};
    opt_params[opensocial.DataRequest.PeopleRequestFields.MAX] = 20;
    req.add(req.newFetchPeopleRequest(viewerFriends, opt_params), 'viewerFriends');

    req.send(onLoadFriendsByUser);
}

function onLoadFriendsByUser(data) {
    var viewerFriends = data.get('viewerFriends').getData();

    var uid = "";
    if(selectedID) {
        uid = selectedID;
    }
    else {
        var viewer = data.get('viewer').getData();
        uid = viewer.getId();
        usernames[uid] = viewer.getDisplayName();
        thumbnails[uid] = viewer.getField('thumbnailUrl');
    }

    // show my or selected photo
    me = new Array();
    me.push('<div><b>', usernames[uid], '</b><br>');
    me.push('<img src="', thumbnails[uid], '" />');
    me.push('</div>');
    document.getElementById('me').innerHTML = me.join('');

    // show friends' photo
    html = new Array();
    html.push('<div style="float:left; margin:5px; padding:5px;">');

    viewerFriends.each(   function(person) {
        if (person.getId()) {
            html.push('<div style="float:left; width:50px; margin:5px; height:100px; padding:5px;">');
            html.push('<a href="#" onclick="loadFriendsByUser(' + person.getId() + '); return false;"><img src="', person.getField('thumbnailUrl'), '" border="0" /></a><br>');
            html.push(person.getDisplayName());
            html.push('</div>');
            usernames[person.getId()] = person.getDisplayName();
            thumbnails[person.getId()] = person.getField('thumbnailUrl');
        }
    });

    html.push('</div>');
    document.getElementById('friends').innerHTML = html.join('');

    // construct xml for flex component
    var friends_xml = "<person><id>" + uid + "</id><name>" + usernames[uid] + "</name><thumbnail>" + thumbnails[uid] + "</thumbnail><friends>";
    viewerFriends.each(   function(person) {
        var id = person.getId();
        if(id) {
            friends_xml += "<friend><id>" + id + "</id><name>" + usernames[id] + "</name><thumbnail>" + thumbnails[id] + "</thumbnail></friend>";
        }
    });
    friends_xml += "</friends></person>";

    sendToActionScript(friends_xml);
}

function init() {
    loadFriendsByUser("");

    // load spring graph
    var html = "<embed width='600' height='400' type='application/x-shockwave-flash' allowscriptaccess='sameDomain'";
    html += "id='flash_xcelsius_analytic_32Srwyarg2GyC9eDCAG608' wmode='transparent' version='true' flashvars=''";
    html += "src='http://shirui.googlecode.com/svn/trunk/opensocial/shirui/PersonDemo.swf'/>";
    document.getElementById('roamer').innerHTML = html;

    google.load("visualization", "1");
    gadgets.window.adjustHeight();
}

gadgets.util.registerOnLoadHandler(init);