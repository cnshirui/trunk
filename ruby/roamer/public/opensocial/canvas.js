var jsReady = false;

var usernames = {};
var thumbnails = {};
var selectedID = '';

// rshi: set true when debug
var appDebugMode = false;
var appConfig = new Object();

function getServerURL() {
    return appConfig.serverURL;
}

function getProfilePath() {
    return appConfig.profilePath;
}

function shareApp() {
    //confirm("Are you sure to recommend this application to friends?");
    var opt_params = {"appId": appConfig.appID, "returnUrl": appConfig.appURL };
    opensocial.requestShareApp (null, null, callback, opt_params);
}

function callback(data) {
  if (data.hadError()) {
    alert("There was a problem:" + data.getErrorCode());
  } else {
    alert('callback ok!')
    output("Ok");
  }
}

function isReady() {
    // hide loading image
    $("#loading_div").slideUp("slow");
    
    return jsReady;
}

function init() {
    jsReady = true;

    appConfig.appVersion = 27;
    appConfig.profilePath = "http://xiaonei.com/profile.do?id=";

    if(appDebugMode) {
        // opensical test
        appConfig.appID = 25256;
        appConfig.appURL = "http://apps.renren.com/opensocial_test";
        appConfig.serverURL = "http://116.227.28.171:3000";
    }
    else {
        // campus roamer
        appConfig.appID = 24820;
        appConfig.appURL = "http://apps.renren.com/campus_roamer";
        appConfig.serverURL = "http://roamer.shirui.org";
    }

    // load spring graph
    var swf_path = appConfig.serverURL + "/opensocial/CampusRoamer.swf?22";
    opensocial.flash.embedFlash( swf_path, "flash_container", "9",
     {
        width: "780",
        height: "470",
        quality: "high",
        wmode: "window",
        allowScriptAccess: "always"
     });
}

function reset() {
    usernames = {};
    thumbnails = {};
    selectedID = '';
}

function sendToActionScript(value) {
    opensocial.flash.getFlash().sendToActionScript(value);
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
