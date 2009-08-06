  google.load("search", "1");
  google.setOnLoadCallback(OnLoad);

  function OnLoad() {
    // Create a search control
    searchControl = new GSearchControl();

    // add a site-limited web search, with a custom label
    var siteSearch = new GwebSearch();
    siteSearch.setUserDefinedLabel("Sourcemaking.com");
    siteSearch.setSiteRestriction("sourcemaking.com");
    searchControl.addSearcher(siteSearch);

    // setting the draw mode for the Google search
    var drawOptions = new GdrawOptions();
    // use tabbed view
    drawOptions.setDrawMode(GSearchControl.DRAW_MODE_TABBED);
    // Draw the tabbed view in the content div
    searchControl.draw(document.getElementById("searchcontrol"), drawOptions);
    searchControl.setSearchStartingCallback(this, reportSearchStats);
    searchControl.setNoResultsString('Sorry, nothing found.');
    
    $("input.gsc-input").val(getkeywords());
  }

  function reportSearchStats(sc, searcher, query) {
    if (pageTracker != undefined) {
      pageTracker._trackEvent("Search", 'Site', query);
    }
  }

  function utf8_decode(utftext) {
    utftext = unescape(utftext);
    var string = "";
    var i = 0;
    var c = c1 = c2 = 0;

    while ( i < utftext.length ) {

      c = utftext.charCodeAt(i);

      if (c < 128) {
        string += String.fromCharCode(c);
        i++;
      }
      else if((c > 191) && (c < 224)) {
        c2 = utftext.charCodeAt(i+1);
        string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
        i += 2;
      }
      else {
        c2 = utftext.charCodeAt(i+1);
        c3 = utftext.charCodeAt(i+2);
        string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
        i += 3;
      }
    }

    return string;
  }

  function getkeywords() {
    var x = "";
    var r = document.referrer;
    if (r.search(/google/) != -1 || r.search(/ask.com/) != -1 || r.search(/search.msn.com/) != -1) {
      patt = new RegExp("q=(.*?)(&)");
      matches = patt.exec(r);
      if (matches[1] != undefined) {
        x = matches[1];
      }
      else {
        x = "";
      }
    }
    else if (r.search(/yahoo/) != -1) {
      patt = new RegExp("p=(.*?)(&)");
      matches = patt.exec(r);
      if (matches[1] != undefined) {
        x = matches[1];
      }
      else {
        x = "";
      }
    }
    x = x.replace(/\+/g, " ");
    x = x.replace(/(\s)*(site:)*(www\.)*sourcemaking(\.com)*/g, "");
    return utf8_decode(x);
  }
