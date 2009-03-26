alert("hi");
var osdomain;
var uid;
var sid;
var tid;

var givenHugs = {};
var globalFriends = {};
var globalFriendsImgURL = {};

var numFriendPages;
var currentPage;

var currentFriendPage = 0;
var friendsPerPage = 18;
var totalFriends = 0;

var globalGiftImages = {};
var numGiftsPerPage = 24;

var selectedGiftId = 1;
var selectedGiftName = "";
var selectedGiftDisplayName = "";

var selectedFriendIds = {};

var owner;
var ownerId;
var viewer;
var viewerId;

function postActivity(fid) {
    var title = viewer.getDisplayName() + ' sent ' + globalFriends[fid] + ' ' + selectedGiftDisplayName;
    var body = globalFriends[fid] + ' received ' + '<img src="http://www.ticktag.com/freehug/images/' + globalGiftImages[selectedGiftId] + '">';
    var params = {};
    params[opensocial.Activity.Field.TITLE] = title;
    params[opensocial.Activity.Field.BODY] = body;
    var activity = opensocial.newActivity(params);
    opensocial.requestCreateActivity(activity, opensocial.CreateActivityPriority.HIGH, function() {});
}


function navigateToCanvas() {
    gadgets.views.requestNavigateTo(new gadgets.views.View("canvas"));
}

var init = function() {
    var env = opensocial.getEnvironment();

    try {
        var view = gadgets.views.getCurrentView();
        var viewName = view.getName();
    } catch (err) {};

    if( viewName == 'profile' ) {
        document.getElementById('navigation').innerHTML = '<div style="font-weight:bold;font-size:100%; color:33AA33;">Hug Your Loved Hug Your Friends Hug the World</div>';
        document.getElementById('main').innerHTML = '<a href="javascript:void();" onclick="navigateToCanvas();"><img style="border:0px;" src="http://www.ticktag.com/freehug/images/FreeHugAbout.jpg" /></a>';
    }
    else {
        osdomain = env.getDomain();
        getHome();
    }
}

function getSession() {
    if( owner && owner.isViewer() ) {
        var url = "http://www.sviral.com/orkut/freehug/initSession.php";
        var params = {};
        var postdata_array = {}; 
        postdata_array["t"] = "json";
        postdata_array["uid"] = viewer.getId();
        var postdata = gadgets.io.encodeValues(postdata_array);
        params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;
        params[gadgets.io.RequestParameters.POST_DATA]= postdata;
        params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.JSON;
        makeCachedRequest(url, setSession, params, 0); 
     }
}

function setSession(obj) {
//   uid = obj.data['uid'];
//   sid = obj.data['sid'];
//   tid = obj.data['token'];
}

function getHome() {
     // reset selected friends
     selectedFriendIds = {};

     loadGiftImages();
     loadUsers();
     loadFriends();

     var html = new Array();
     html.push('<div id="give">');
//     html.push('<form id="gift_form">');
     html.push('<div id="gifts"></div>');
     html.push('<div id="friends"></div>');
     html.push('<div id="actions"></div>');
//     html.push('</form>');
     html.push('</div>');
     document.getElementById('main').innerHTML = html.join('');
 
     loadNextButton();
}

function eventTrigger (e) {
     if (! e)
          e = event;
     return e.target || e.srcElement;
}

function selectGift (e) {
     var obj = eventTrigger (e);
     selectedGiftId = obj.value;
     selectedGiftName = getGiftName(selectedGiftId); 
     selectedGiftDisplayName = getGiftDisplayName(selectedGiftId); 
}

function getGiftName(gid) {
     selectedGiftName = globalGiftImages[gid];
}

function getGiftDisplayName(gid) {
     if( globalGiftImages[gid] ) {
     var gname= globalGiftImages[gid];
     var giftname = gname.split(".")[0];
     giftname = giftname.replace("_", " ");
     giftname = giftname.replace("_", " ");
     return giftname;
     }
     else {
         return "";
     }
}

function selectFriend (e) {
     var obj = eventTrigger (e);
     if( obj.checked == true ) {
         selectedFriendIds[obj.value] = 1;
     }
     else {
         selectedFriendIds[obj.value] = 0;
     }
}

function nextStep() {
     showFriends(0, 1);
     // emptying gift div
     var html = new Array();
     document.getElementById('gifts').innerHTML = html.join('');
     loadSubmitButton();
}

function getSentHugs() {
     var req = opensocial.newDataRequest();
     req.add(req.newFetchPersonRequest('VIEWER'), 'viewer');
     req.add(req.newFetchPersonAppDataRequest('VIEWER', 'hugs'), 'data');
     req.send(onLoadSentHugs);
}

function onLoadSentHugs(data) {
     var viewer = data.get('viewer').getData();
     var giftData = data.get('data').getData();
     showSentHugs(viewer, giftData, globalFriends);
}

function getRecvHugs() {
     var req = opensocial.newDataRequest();
     req.add(req.newFetchPersonRequest('VIEWER'), 'viewer');
     req.add(req.newFetchPersonAppDataRequest('VIEWER_FRIENDS', 'hugs'), 'viewerFriendData');
     req.send(onLoadRecvHugs);
}

function onLoadRecvHugs(data) {
     var viewer = data.get('viewer').getData();
     var giftData = data.get('viewerFriendData').getData();
     showReceivedHugs(viewer, giftData, globalFriends);
}

function loadGiftImages() {
     var url = "http://www.sviral.com/orkut/freehug/img_list.php";
     var params = {};
     var postdata_array = {}; 
     postdata_array["t"] = "json";
     var postdata = gadgets.io.encodeValues(postdata_array);
     params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;
     params[gadgets.io.RequestParameters.POST_DATA]= postdata;
     params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.JSON;
     makeCachedRequest(url, getGiftImages, params, 0); 
}

// Call the init function onLoad
//gadgets.util.registerOnLoadHandler(init);

/**
* Fetch the Viewer with an opensocial dataRequest
*/
function loadUsers() {
    var req = opensocial.newDataRequest();
    req.add(req.newFetchPersonRequest('OWNER'), 'owner');
    req.add(req.newFetchPersonRequest('VIEWER'), 'viewer');
    req.send(onLoadUsers);
}

function onLoadUsers(dataResponse) {
    owner = dataResponse.get('owner').getData();
    viewer = dataResponse.get('viewer').getData();
    try {
        ownerId = owner.getField(opensocial.Person.Field.ID);
        gender = viewer.getField(opensocial.Person.Field.GENDER);
        viewerId = viewer.getField(opensocial.Person.Field.ID);
    }
    catch(err) {
        viewerId = 0;
    }
}

/**
* Request an activity stream entry, with links to owner and viewer and application canvas page
*/
function createActivity(viewer, owner) {
    var activity = opensocial.newActivity("<a href='" + viewer.getField(opensocial.Person.Field.PROFILE_URL) + "'>" + viewer.getField(opensocial.Person.Field.NAME) + "</a> viewed <a href='" + owner.getField(opensocial.Person.Field.PROFILE_URL) + "'>" + owner.getField(opensocial.Person.Field.NAME) + "'s</a> journal with the <a href='/friend/apps/displayAppCanvas.do?appId=192&userid=" + owner.getField(opensocial.Person.Field.ID) + "'>MyJournal</a> application");
    activity.setField(opensocial.Activity.Field.STREAM_FAVICON_URL, 'http://images.hi5.com/images/icons/_/update_widget.png');

    var priority = opensocial.CreateActivityPriority['HIGH'];
    opensocial.requestCreateActivity(activity, priority);
}

function loadFriends() {
    var req = opensocial.newDataRequest();
    var opt = {};
    opt[opensocial.DataRequest.PeopleRequestFields.MAX] = 200;
    req.add(req.newFetchPersonRequest('VIEWER'), 'viewer');
    req.add(req.newFetchPeopleRequest('VIEWER_FRIENDS',opt), 'viewerFriends');
    req.send(onLoadFriends);
}
     
function onLoadFriends(data) {
    try {
        var viewer = data.get('viewer').getData();
        var viewerFriends = data.get('viewerFriends').getData();
    }
    catch(err) {
        return;
    }
    if(!viewer) return;

    totalFriends = viewerFriends.getTotalSize();

    if( totalFriends/friendsPerPage > Math.floor(totalFriends/friendsPerPage) ) {
        numFriendPages = Math.floor(totalFriends/friendsPerPage) + 1;
    }
    else {
        numFriendPages = Math.floor(totalFriends/friendsPerPage);
    }

    viewerFriends.each(function(person) {
        globalFriends[person.getId()] = person.getDisplayName();
        globalFriendsImgURL[person.getId()] = person.getField('thumbnailUrl');
    });
}

function showFriends(page, all) {
    currentFriendPage = page;
    var selectAllFriends = all;

    var html = new Array();
    var pi = 0; 
    var userHtml;
  
    html.push('<div id="friendBox" style="margin:8px; padding:8px; float:left; width:777px; background-color:white; border: 1px solid #3B5998;">');
    html.push('<div id="friendbar" style="float:left; width:777px;">');
    html.push('<div style="margin:8px 20px 8px 0px; padding:0px; font-size:150%; font-weight:bold;">Step 2. Select Friends (Selecione Amigos)');

    if( numFriendPages > 1 ) {
        html.push('&nbsp; &nbsp; &nbsp; &nbsp;<span style="font-size:60%; font-weight:normal; color:#aaaacc;">more friends (mais amigos)</span>');
    }
    html.push('</div>');

    if( numFriendPages > 1 ) {
        html.push('<div style="width:777px; margin:0px; padding:0px; font-size:75%; font-weight:normal; color: #333388;">');
        for(pi = 0; pi < numFriendPages; pi++) {
            pj = pi + 1;
            if( currentFriendPage == pi ) {
            html.push('<div style="float:left; margin:3px; font-weight:bold;">' + pj + '</div>');
            }
            else {
                var tt = 0;
                html.push('<div style="float:left; margin:3px;"><a href="javascript:void(0);" onclick="showFriends(' + pi + ', ' + tt + ');">' + pj + '</a></div>');
            }
         }
         html.push('</div>');
    }
    html.push('</div>');

    pi = 0; 
    var startPage = currentFriendPage * friendsPerPage;
    var stopPage = (currentFriendPage + 1) * friendsPerPage;

    for(var uid in globalFriends) {
        if( pi >= startPage && pi < stopPage ) { 
            html.push('<div style="float:left; margin:5px; padding:5px; width:64px; height:100px;">');
            userHtml = '<img border="0" width="50" src="' + globalFriendsImgURL[uid] + '" width="50" height="50" /><br>';

            if( selectAllFriends == 1 ) {
                userHtml = userHtml + '<input type="checkbox" name="fid' + pi + '" value="' + uid + '" checked onclick="selectFriend(event);"><br>';
                selectedFriendIds[uid] = 1;
            }
            else if( selectAllFriends == -1 ) {
                userHtml = userHtml + '<input type="checkbox" name="fid' + pi + '" value="' + uid + '" onclick="selectFriend(event);"><br>';
                selectedFriendIds[uid] = 0;
            }
            else {
                if( selectedFriendIds[uid] ) {
                    userHtml = userHtml + '<input type="checkbox" name="fid' + pi + '" value="' + uid + '" checked onclick="selectFriend(event);"><br>';
                }
                else {
                     userHtml = userHtml + '<input type="checkbox" name="fid' + pi + '" value="' + uid + '" onclick="selectFriend(event);"><br>';
                }
            }
            userHtml = userHtml + '<span style="font-size:70%;">' + globalFriends[uid] + '</span>';
            html.push(userHtml);
            html.push('</div>');
        }
        pi++;
    };

    html.push('</div>');
    document.getElementById('friends').innerHTML = html.join('');
}

function loadNextButton() {
    var html = new Array();
    if( 1 || !owner || owner.isViewer() ) {
        html.push('<div id="submitBox" style="margin:8px; padding:8px; float:left; width:777px; background-color:white;">');
        html.push('<div id="submitbar" style="float:left; width:777px;">');
        html.push('<div style="float:left; margin:0px; padding:0px; font-size:130%; font-weight:bold;"><a style="margin:5px; padding:10px; font-size:120%; font-weight:bold; border:1px solid #FF0000; color:#FFFFFF; background-color:#3B3B98; " href="javascript:void()" onclick="nextStep();">Next (proxima)</a>');
        html.push('</div>');
        html.push('</div>');
        html.push('</div>');
    }
    else {
        html.push('<div style="float:left; width:700px; font-size:125%;font-weight:bold; color:#FF0000;">You have to install this app first before you can send hugs to friends! Click the link "add this app to your profile" below.</div>');
    }

    document.getElementById('actions').innerHTML = html.join('');
}

function loadSubmitButton() {
    var html = new Array();

    html.push('<div id="submitBox" style="margin:8px; padding:8px; float:left; width:777px; background-color:white; border: 1px solid #3B5898;">');
    html.push('<div style="float:right; margin:0px 20px; padding:0px; font-size:120%; font-weight:bold;"><input style="margin:0px; padding:2px; font-size:100%; font-weight:bold; border:1px solid #FF0000; color:#FFFFFF; background-color:#3B3B98;" type="submit" name="submit" value="   Send Hugs (envie abrazos)   " onclick="sendHugs();">');
    html.push('</div>');

    html.push('<div style="float:left; margin:8px 20px 8px 0px; padding:0px; font-size:80%; font-weight:normal;"><a href="javascript:void(0);" onclick="showFriends(currentFriendPage, 1);">Select all (Selecionne todas)</a>');
    html.push('</div>');

    html.push('<div style="float:left; margin:8px 20px 8px 0px; padding:0px; font-size:80%; font-weight:normal;"><a href="javascript:void(0);" onclick="showFriends(currentFriendPage, -1);">Clear (Claro)</a>');
    html.push('</div>');
    html.push('</div>');

    document.getElementById('actions').innerHTML = html.join('');
}

function showSentHugs(viewer, data, friends) {
    json = null;
    try {
        var json = data[viewer.getId()]['hugs'];
    }
    catch(e) {}

    if (!json) {
        givenHugs = {};
    }
    try {
        givenHugs = gadgets.json.parse(gadgets.util.unescapeString(json));
    } catch (e) {
        givenHugs = {};
    }

    var html = new Array();
    html.push('<div style="float:left; margin:0px; padding:2px; width:98%; border:0px solid #000;">');
    html.push('<div style="float:left; width:420px;">');
    var cnt = 0;
    for (i in givenHugs) {
         cnt++;
         if (+(i) > 0) {
             var j = givenHugs[i]-0;
             html.push('<div style="float:left; width:97%; border:1px solid #000;margin:2px;padding:2px;"><div style="float:left; color:#000; font-size:90%; margin:5px; padding:5px;"><br>You sent </div><div style="float:left; padding:5px; margin:5px;"><span style="color:#0000ff;">' + friends[i] + '</span><br><img src="' + globalFriendsImgURL[i] + '" /></div><div style="float:left; padding:5px; margin:5px; color:#000;"><br>a</div><div style="float:left; margin:5px; padding:5px; color:#CC8833;">' + getGiftDisplayName(j) + '<br><img src="http://www.ticktag.com/freehug/images/' + globalGiftImages[j] + '" /></div></div>');
         }
    }
    html.push('</div>');
    html.push('<div style="float:left; margin:0px; padding:2px; width:360px; border:0px solid #ccccff;">');
    html.push('</div>');
    if( cnt == 0 ) {
        html.push('<div style="float:left; font-size:150%; font-weight:bold; margin:10px; padding:8px; width:440px; border:0px solid #ccccff;">');
        html.push('You have not sent any hugs yet');
        html.push('</div>');
    }

    document.getElementById('main').innerHTML = html.join('');
}

function showReceivedHugs(viewer, data, friends) {
    var viewerId = viewer.getId();

    var html = new Array();
    html.push('<div style="float:left; margin:0px; padding:2px; width:98%; border:0px solid #000;">');
    html.push('<div style="float:left; width:420px;">');
    var cnt = 0;
    for (var p in friends) {
        if (p) {
            json = null;
            try {
              var json = data[p]['hugs'];
            }
            catch(e) {}

            var hugs = {}
            if (!json) {
                hugs = {};
            }
            try {
                hugs = gadgets.json.parse(gadgets.util.unescapeString(json));
            } catch (e) {
                hugs = {};
            }

            for (var i in hugs) {
                if (+(i) > 0 && i == viewerId) {
                    cnt++;
                    var j = hugs[i]-0;
//                    html.push('<div>You have received a ' + getGiftDisplayName(j) + ' <img src="http://www.ticktag.com/freehug/images/' + globalGiftImages[j] + '" /> from <span style="color:#0000ff;">' + friends[p] + '</span> <img src="' + globalFriendsImgURL[p] + '" /></div>');
                 if( globalFriendsImgURL[p] ) {
                    html.push('<div style="float:left; width:97%; border:1px solid #000;margin:2px;padding:2px;"><div style="float:left; color:#000; font-size:90%; margin:5px; padding:5px;"><br>You received a </div><div style="float:left; padding:5px; margin:5px;"><span style="color:#0000ff;">' + getGiftDisplayName(j) + '<br> <img src="http://www.ticktag.com/freehug/images/' + globalGiftImages[j] + '" /></div><div style="float:left; padding:5px; margin:5px; color:#000;"><br>from</div><div style="float:left; margin:5px; padding:5px; color:#33CC33;"' + friends[p] + '</span><br><img src="' + globalFriendsImgURL[p] + '" /></div></div>');
                 }
                }
            }
        }
    }
    if( cnt == 0 ) {
        html.push('<div style="float:left; font-size:150%; font-weight:bold; margin:10px; padding:8px; width:440px; border:0px solid #ccccff;">');
        html.push('You have not received any hugs yet');
        html.push('</div>');
    }

    html.push('</div>');
    html.push('<div style="float:left; margin:0px; padding:2px; width:360px; border:0px solid #ccccff;">');
    html.push('</div>');
    document.getElementById('main').innerHTML = html.join('');
}

function makeCachedRequest(url, response, params, refreshInterval) {
    var ts = new Date().getTime();
    var sep = "?";
    if (refreshInterval && refreshInterval > 0) {
        ts = Math.floor(ts / (refreshInterval * 1000));
    }
    if (url.indexOf("?") > -1) {
        sep = "&";
    }
    url = [ url, sep, "nocache=", ts ].join("");
    gadgets.io.makeRequest(url, response, params);
}

function getGiftImages(obj) {
    var i = 0;
    var imgname = "";
    var gname = "";

    for ( var k in obj.data ) {
        globalGiftImages[k] = obj.data[k];
    }
    showGiftImages(0);
}

function showGiftImages(page) {
    var html = new Array();
    var startPage = page*numGiftsPerPage;
    var stopPage = (page+1)*numGiftsPerPage;

    currentPage = page;

    html.push('<div id="giftBox" style="margin:8px; padding:8px; float:left; width:777px; background-color:white; border: 1px solid #3B5998;">');
    html.push('<div id="giftbar" style="float:left; width:777px;">');
    html.push('<div style="float:left; margin:5px 20px 5px 0px; padding:0px; font-size:150%; font-weight:bold;">Step 1. Select Hug (Selecione Abrazo)');
    html.push('</div>');

    html.push('<div style="float:left; margin:5px; padding:5px; font-size:80%; font-weight:normal; color:#333388;">more hugs (mais abrazos) &nbsp; ');
    if( currentPage == 0 ) {
        html.push('<span style="margin:5px; font-weight:bold;">1</span>');
    }
    else {
        html.push('<a style="margin:5px;" href="javascript:void(0);" onclick="showGiftImages(0);">1</a>');
    }
    if( currentPage == 1 ) {
        html.push('<span style="margin:5px; font-weight:bold;">2</span>');
    }
    else {
        html.push('<a style="margin:5px;" href="javascript:void(0);" onclick="showGiftImages(1);">2</a>');
    }
    if( currentPage == 2 ) {
        html.push('<span style="margin:5px; font-weight:bold;">3</span>');
    }
    else {
        html.push('<a style="margin:5px;" href="javascript:void(0);" onclick="showGiftImages(2);">3</a>');
    }
    if( currentPage == 3 ) {
        html.push('<span style="margin:5px; font-weight:bold;">4</span>');
    }
    else {
        html.push('<a style="margin:5px;" href="javascript:void(0);" onclick="showGiftImages(3);">4</a>');
    }
    html.push('</div>');
    html.push('</div>'); 

    html.push('<div style="width:777px;">'); 
    var i = 0;
    var imgname = "";
    var gname = "";

    for ( var k in globalGiftImages ) {
        if( i >= startPage && i < stopPage ) {
            html.push('<div style="float:left; width:88px; height:100px; margin:2px; padding:2px;">');
            html.push('<img id="' + i + '" style="border:0px" src="http://www.ticktag.com/freehug/images/' + globalGiftImages[k] + '" width="58" height="58"><br>');
            imgname = globalGiftImages[k];
            gname = imgname.split(".")[0];
            gname = gname.replace("_", " ");
            gname = gname.replace("_", " ");
            gname = gname.replace("_", " ");
            if( i == startPage ) {
                html.push('<input type="radio" name="gname" value="' + k + '" onclick="selectGift(event);" checked><br>');
                selectedGiftId = k;
                selectedGiftName = getGiftName(selectedGiftId); 
                selectedGiftDisplayName = getGiftDisplayName(selectedGiftId); 
            }
            else {
                html.push('<input type="radio" name="gname" value="' + k + '" onclick="selectGift(event);"><br>');
            }
            html.push('<div style="font-size:70%; font-weight:normal;">' + gname + '</div>');
            html.push('</div>');
        }
        i++;
    }
    html.push('</div>');
    html.push('</div>'); 
    document.getElementById('gifts').innerHTML = html.join('');
}

function sendHugs() { 
    var html = new Array();
    var userHtml = "";
    html.push('<div id="giftBox" style="float:left; margin:8px; padding:8px; width:777px; background-color:white; border: 1px solid #3B5998;">');
    html.push('<div style="font-size:110%; font-weight:bold; color:#0000FF;">You have sent a <span style="color:#FF0000;">' + selectedGiftDisplayName + '</span> to these friends.</div>');
 
    html.push('<div style="float:left; font-size:90%; width:770px; font-weight:normal; color:#000000;">');
    for(var uid in selectedFriendIds) {
        if( selectedFriendIds[uid] == 1 ) {
            html.push('<div style="float:left; margin:5px; padding:5px; width:64px; height:100px;">');
            userHtml = '<img border="0" width="50" src="' + globalFriendsImgURL[uid] + '" width="50" height="50" /><br>';
            userHtml = userHtml + '<span style="font-size:70%;">' + globalFriends[uid] + '</span></div>';
            html.push(userHtml);
            givenHugs[uid] = selectedGiftId;
            postActivity(uid);
         }
    }
    html.push('</div>');

    html.push('<iframe src="http://www.google.com/custom?q=Find%20Love%20&sa=Search&client=pub-5412192092701230&forid=1&ie=UTF-8&oe=UTF-8&cof=GALT%3A%23008000%3BGL%3A1%3BDIV%3A%23336699%3BVLC%3A663399%3BAH%3Acenter%3BBGC%3AFFFFFF%3BLBGC%3A336699%3BALC%3A0000FF%3BLC%3A0000FF%3BT%3A000000%3BGFNT%3A0000FF%3BGIMP%3A0000FF%3BFORID%3A1%3B&hl=en" border="0" width="728" height="400" scrolling="no" frameborder="0"></iframe>');

    document.getElementById('main').innerHTML = html.join('');

    // store sent hugs into personal app data
    var json = gadgets.json.stringify(givenHugs);
    var req = opensocial.newDataRequest();
    req.add(req.newUpdatePersonAppDataRequest(opensocial.DataRequest.PersonId.VIEWER, 'hugs', json));
    req.send(noAction);
}

function noAction() {
}


function sendNotifications(notification, toIds) { 
    var params = new Object(); 
    params[opensocial.Message.Field.TYPE] = opensocial.Message.Type.NOTIFICATION; 
    var message = opensocial.newMessage(notification, params); 
    opensocial.requestSendMessage(toIds, message); 
}

function sendHugNotifications() { 
//function sendHugNotifications(hugName, toIds) { 
//    var viewerLink = viewer.getField(opensocial.Person.Field.PROFILE_URL); 
//    var notification = '<a href="' + viewerLink + '">' + viewer.getDisplayName() + '</a> sent you a ' + hugName; 
    var notification = 'sent gift';
    var toIds = new Array();
    var friend = "1469014301843241529";  
//var toIds = {}; 
toIds.push(friend); 
//    var toIds = new Array("1469014301843241529");
    sendNotifications(notification, toIds);
}
