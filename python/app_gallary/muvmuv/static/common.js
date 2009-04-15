var muvmuv = muvmuv || {};

muvmuv.getUrlArgs = function() {
  var args = new Object();    
  var params = window.location.href.split('?');

  if (params.length > 1) {
    params = params[1];
    var pairs = params.split("&");
    for ( var i = 0; i < pairs.length; i++) {
      var pos = pairs[i].indexOf('=');
      if ( pos == -1 ) continue;
      var argname = pairs[i].substring(0, pos);
      var value = pairs[i].substring(pos + 1);
      value = value.replace(/\+/g, " ");
      args[argname] = value;
    }
  }
  return args;
}

muvmuv.textTruncate = function(text, length) {
  if (text.length > length) {
    truncatedText = text.substring(0, length);
    truncatedText = truncatedText.replace(/\w+$/, '');
    truncatedText = [truncatedText, ' ...'].join('');  
    return truncatedText;
  } else {
    return text;
  }
}

muvmuv.getTitle = function() {
  return muvmuv.URL_ARGS.title;
};

muvmuv.getSortBy = function() {
  var sortby = muvmuv.URL_ARGS.sortby;

  if (!sortby) {
    sortby = 'release_date';
  }
  return sortby;
}

muvmuv.getCurrentPage = function() {
  var page = muvmuv.URL_ARGS.page;

  if (!page) {
    page = 1;
  }
  return parseInt(page);
}
