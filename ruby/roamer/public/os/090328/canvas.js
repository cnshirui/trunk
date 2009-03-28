var jsReady = false;

var usernames = {};
var thumbnails = {};
var selectedID = '';

function isReady() {
    return jsReady;
}

function sendToActionScript(value) {
    opensocial.flash.getFlash().sendToActionScript(value);
}

function getProfilePath() {
    // return "http://www.yiqi.com/home.php?tab=parlor&uid=";
    return "http://xiaonei.com/profile.do?id=";
}

function loadFriends(uid) {
    var req = opensocial.newDataRequest();

    if(uid == null) {
        // for viewer
        uid = "VIEWER";
    }
    else {
        // for other users
        var viewer = opensocial.newIdSpec({ "userId" : uid, "groupId" : "SELF" });
        req.add(req.newFetchPeopleRequest(viewer, {}), 'viewer');
        selectedID =  uid;
    }
    req.add(req.newFetchPersonRequest(opensocial.IdSpec.PersonId.VIEWER), 'viewer');

    var viewerFriends = opensocial.newIdSpec({ "userId" : uid, "groupId" : "FRIENDS" });
    var opt_params = {};
    opt_params[opensocial.DataRequest.PeopleRequestFields.MAX] = 1000;
    // TOP_FRIENDS,	NAME
    opt_params[opensocial.DataRequest.PeopleRequestFields.SORT_ORDER] = opensocial.DataRequest.SortOrder.NAME;
    // TOP_FRIENDS, ALL
    // opt_params[opensocial.DataRequest.PeopleRequestFields.FILTER] = opensocial.DataRequest.FilterType.ALL;
    req.add(req.newFetchPeopleRequest(viewerFriends, opt_params), 'viewerFriends');

    req.send(onLoadFriends);
}

function onLoadFriends(data) {
    var viewerFriends = data.get('viewerFriends').getData();

    var uid = "";
    if(selectedID) {
        // for other users
        uid = selectedID;
    }
    else {
        // for viewer
        var viewer = data.get('viewer').getData();
        uid = viewer.getId();
        usernames[uid] = viewer.getDisplayName();
        thumbnails[uid] = viewer.getField('thumbnailUrl');
    }

    // construct xml for flex component
    var index = 0;
    var friends_xml = "<person><id>" + uid + "</id><name>" + usernames[uid] + "</name><thumbnail>" + thumbnails[uid] + "</thumbnail><friends>";
    viewerFriends.each( function(person) {
        var id = person.getId();
        usernames[person.getId()] = person.getDisplayName();
        thumbnails[person.getId()] = person.getField('thumbnailUrl');

        if(id) {
            friends_xml += "<friend index='" + index.toString() + "'><id>" + id + "</id><name>" + usernames[id] + "</name><thumbnail>" + thumbnails[id] + "</thumbnail></friend>";
            index = index + 1;
        }
    });
    friends_xml += "</friends></person>";

    // prompt("All friends", friends_xml);
    sendToActionScript(friends_xml);
}


function init() {
    jsReady = true;

    // load spring graph
    var swf_path = "http://roamer.heroku.com/os/090328/" + "CampusRoamer.swf" + "?" + Math.random().toString();
    opensocial.flash.embedFlash( swf_path, "flash_container", "9",
     {
        width: "790",
        height: "470",
        quality: "high",
        wmode: "window",
        allowScriptAccess: "always"
     });    
// 790x470, or 850x600,
}

function reset() {
    usernames = {};
    thumbnails = {};
    selectedID = '';
}

