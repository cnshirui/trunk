AJS.toInit(function($) {
    $(".confluence-thumbnail-link").click(function(e) {
        /* the thumbnail javacode will produce a link with a class specifying the image size widthxheight */
        var size = this.className.match(/(^|\s)(\d+)x(\d+)(\s|$)/);
        window.open(this.href, "thumbnail-popup", "width=" + (+size[2] + 20) + ",height=" + (+size[3] + 20) + ",menubar=no,status=no,toolbar=no");
        return AJS.stopEvent(e);
    });
});
