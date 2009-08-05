if(!(/jwebunit/).test(navigator.userAgent.toLowerCase())){
    AJS.toInit(function ($) {
        var attr = {
            cache_size: 30,
            max_length: 1,
            effect: "appear" // TODO: add "fade" effect
        };
        var dd,
            cache = {},
            cache_stack = [],
            timer;

        var hider = function (list) {
             $("a span", list).each(function () {
                var $a = $(this),
                    elpss = AJS("var", "&#8230;"),
                    elwidth = elpss[0].offsetWidth,
                    width = this.parentNode.parentNode.parentNode.parentNode.offsetWidth,
                    isLong = false,
                    rightPadding = 20; // add some padding so the ellipsis doesn't run over the edge of the box

                // get the hidden space name property from the span
                var spaceName = AJS.dropDown.getAdditionalPropertyValue($a, "spaceName");
                if (spaceName != null) {
                	spaceName = "(" + spaceName + ") ";
                } else {
                	spaceName = "";
                }
                
                AJS.dropDown.removeAllAdditionalProperties($a);
                
                this.realhtml = this.realhtml || $a.html();
                
                $a.html("<em>" + this.realhtml + "</em>");
                $a.append(elpss);
                
                $a.attr("title", spaceName + this.realhtml.replace(/<\/?strong>/gi, ""));
                
                this.elpss = elpss;
                $("em", $a).each(function () {
                    var $label = $(this);

                    $label.show();
                    if (this.offsetLeft + this.offsetWidth + elwidth > width - rightPadding) {

                        var childNodes = this.childNodes;
                        var success = false;

                        for (var j = childNodes.length - 1; j >= 0; j--) {
                            var childNode = childNodes[j];
                            var truncatedChars = 1;

                            var valueAttr = (childNode.nodeType == 3) ? "nodeValue" : "innerHTML";
                            var nodeText = childNode[valueAttr];

                            do {
                                if (truncatedChars <= nodeText.length) {
                                    childNode[valueAttr] = nodeText.substr(0, nodeText.length - truncatedChars++);
                                } else { // if we cannot fit even one character of the next word, then try truncating the node just previous to this
                                    break;
                                }
                            } while (this.offsetLeft + this.offsetWidth + elwidth > width - rightPadding)

                            if (truncatedChars <= nodeText.length) {
                                // we've managed truncate part of the word and fit it in
                                success = true;
                                break;
                            }
                        }

                        if (success) {
                            isLong = true;
                        } else {
                            $label.hide();
                        }
                    }
                });
                elpss[isLong ? "show" : "hide"]();
            });
        };

        var searchBox = $("#quick-search-query");
        if ($.browser.safari) {
            searchBox[0].type = "search";
            searchBox[0].setAttribute("results", 10);
            searchBox[0].setAttribute("placeholder", "search");
        }
        var jsonparser = function (json, resultStatus) {
            var hasErrors = json.statusMessage ? true : false; // right now, we are overloading the existance of a status message to imply an error
            var matches = hasErrors ? [[{html: json.statusMessage, className: "error"}]] : json.contentNameMatches;

            if (!hasErrors) {
                var query = json.query;
                if (!cache[query] && resultStatus == "success") {
                    cache[query] = json;
                    cache_stack.push(query);
                    if (cache_stack.length > attr.cache_size) {
                        delete cache[cache_stack.shift()];
                    }
                }
            }

            // do not update drop down for earlier requests. We are only interested in displaying the results for the most current search
            if (json.query != searchBox.val()) {
                return;
            }

            var old_dd = dd;
            dd = AJS.dropDown(matches)[0];
            dd.$.attr("id", "quick-nav-drop-down");

            dd.onhide = function (causer) {
                if (causer == "escape") {
                    searchBox.focus();
                }
            };
            var spans = $("span", dd.$);
            for (var i = 0, ii = spans.length - 1; i < ii; i++) {
                (function () {
                    var $this = $(this),
                    html = $this.html();
                    // highlight matching tokens
                    html = html.replace(new RegExp("(" + json.queryTokens.join("|") + ")", "gi"), "<strong>$1</strong>");
                    $this.html(html);
                }).call(spans[i]);
            }
            hider(dd.$);
            dd.hider = function () {
                hider(dd.$);
            };
            AJS.onTextResize(dd.hider);
            if (old_dd) {
                dd.show();
                dd.method = attr.effect;
                AJS.unbindTextResize(old_dd.hider);
                old_dd.$.remove();
            } else {
                dd.show(attr.effect);
            }
        };
        searchBox.oldval = searchBox.val();
        searchBox.keyup(function () {
            var val = searchBox.val();

            if (val != searchBox.oldval) {
                searchBox.oldval = val;
                if (!searchBox.hasClass("placeholded")) {
                    clearTimeout(timer);

                    if (AJS.params.quickNavEnabled && (/[\S]{2,}/).test(val)) {
                        if (cache[val]) {
                            jsonparser(cache[val]);
                        } else {
                            timer = setTimeout(function () { // delay sending a request to give the user a chance to finish typing their search term(s)
                                return AJS.$.ajax({
                                    type: "GET",
                                    url: contextPath + "/json/contentnamesearch.action?query=" + AJS.escape(val),
                                    data: null,
                                    success: jsonparser,
                                    dataType: "json",
                                    timeout: 5000,
                                    error: function ( xml, status, e ) { // ajax error handler
                                        if (status == "timeout") {
                                            jsonparser({statusMessage: "Timeout", query: val}, status);
                                        }
                                    }
                                });

                            }, 200);
                        }
                    } else {
                        dd && dd.hide();
                    }
                }
            }
        });
    });
}