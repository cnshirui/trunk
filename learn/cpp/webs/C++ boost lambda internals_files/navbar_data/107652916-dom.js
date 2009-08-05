
// Commonly-used functions, reduced.

function d(s) {return document.getElementById(s);}
function dE(o,s) {return o.getElementsByTagName(s);}

/**
 * toggleDisplay()
 *
 * Will toggle the display property of the style object for any
 * DOM element or object that supports style as a property.
 *
 * Warning: This'll wreak havoc if applied to <TR> elements. Those
 * babies got different types "table-row" | "block" dependant on 
 * what browser's being used.
 *
 * Warning: Written in Texas.  Yeehaw.
 *
 * Typical usage:
 * toggleDisplay(document.getElementById("foo"));
 */
function toggleDisplay(o)
{  
  var display = getStyle(o, "display"); 
  
  if (o.style)
    o.style.display =
      (display != "none") ? "none" : getDisplayStyleByTagName(o);
}


function getDisplayStyleByTagName(o)
{
  var n = o.nodeName.toLowerCase(); 
  return (
          n == "span"
          || n == "img"
          || n == "a"
          ) ? "inline" : "block";
}


/**
 * hideElement()
 *
 * Hides an element from view.
 *
 * Typical usage:
 * hideElement(getElement("the-id-of-the-element"));
 */
function hideElement(o)
{
  if (o && o.style) o.style.display = "none";
}



/**
 * showElement()
 *
 * Shows an element that was hidden from view.
 *
 * Typical usage:
 * showElement(getElement("the-id-of-the-element"));
 */
function showElement(o)
{
  
  if (o && o.style) o.style.display = getDisplayStyleByTagName(o);
}


/**
 * getElement()
 *
 * Returns an element by its ID or shows an alert if it can't be found.
 *
 * Typical usage:
 * getElement("the-id-of-the-element");
 */
function getElement(id) {
  var e = d(id);
  if (!e) {
    alert("Cannot get element: " + id);
  }
  return e;
}

/**
 * setInnerHTML()
 *
 * Sets the innerHTML of an element or shows an alert if can't be set.
 *
 * Typical usage:
 * setInnerHTML("the-id-of-the-element");
 */
function setInnerHTML(id, html) {
  try {
    getElement(id).innerHTML = html;
  } catch (ex) {
    alert("Cannot set inner HTML: " + id);
  }
}


/**
 * setCssStyle()
 *
 * Sets the style of an element by its id or shows an alert if can't be set.
 *
 * Typical usage:
 * setCssStyle("the-id-of-the-element", "display", "block");
 */
function setCssStyle(id, name, value) {
  try {
    getElement(id).style[name] = value;
  } catch (ex) {
    alert("Cannot set style: " + id);
  }
}


/**
 * getStyle()
 *
 * Gets the computed style of any object.
 *
 * WARNING: Produces unexpected results in Safari.  To achieve best 
 * results, explicitly set the style property for that browser when the 
 * element is rendered.
 *
 * Typical usage:
 * getStyle(object, "display");
 */
function getStyle(el, style) {
  if (!document.getElementById || !el) return;
  
  if (document.defaultView
      && document.defaultView.getComputedStyle) {
      return document.defaultView.
        getComputedStyle(el, "").getPropertyValue(style);
  }  
  else if (el.currentStyle) {
    return el.currentStyle[style];
  }  
  else { 
    return el.style.display;
  }
}

/**
 * getStyleAttribute()
 *
 * Returns the style attribute of the specified node.
 */
function getStyleAttribute(node) {
  if (Detect.IE()) {
    return node.getAttribute('style').value;
  }else {
    return node.getAttribute('style');
  }
}


/*
 * showProps()
 *
 * Displays all the properties for a given element
 */
function showProps(o) {
	var s=""; for (var p in o) {
		s+=p+": "+o[p]+"\n<br />";
	}
	document.write(s);
}



function setIFrameEvent(iframe, eventName, func)
{
  if (document.all) {
    eval('getIFrameDocument(iframe).on' + eventName + ' = func;');
  } else {
    iframe.contentWindow.addEventListener(eventName, func, true);
  }
}

function setIFrameBody(iframe, strStyle, innerHtml) 
{
  if (!innerHtml) innerHtml = '';
  if (innerHtml == '' && Detect.IE()) {
    innerHtml = '<div></div>';
  }
  var doc = getIFrameDocument(iframe);
  doc.open();
  doc.write('<head></head><body style="' + strStyle + '">'
    + innerHtml + '</body>');
  doc.close();
}


function getIFrameDocument(iframe)
{
  if (Detect.IE()) {
    return iframe.document;
  } else {
    return iframe.contentDocument;
  }
}

function getIFrame(strId)
{
  if (Detect.IE()) {
    return document.frames[strId];
  } else {
    return document.getElementById(strId);
  }
}


function createElementandAppend(nodeName, strId, appendTo) {
  var el = document.createElement(nodeName);
  el.setAttribute("id", strId);
  if (appendTo) {
    appendTo.appendChild(el); 
  } else {
    document.body.appendChild(el); 
  }
  return el; 
}

function createElementandInsertBefore(nodeName, strId, appendTo, sibling) {
  var el = document.createElement(nodeName);
  el.setAttribute("id", strId);
  if (appendTo) {
    appendTo.insertBefore(el, sibling); 
  } else {
    document.body.insertBefore(el, sibling); 
  }
  return el; 
}


/**
* getXY()
 *
 * Returns the position of any element as an object.
 *
 * Typical usage:
 * var pos = getXY(object);
 * alert(pos.x + " " +pos.y);
 */
function getXY(el) {
  var x = el.offsetLeft;
  var y = el.offsetTop;
  if (el.offsetParent != null) {
    var pos = getXY(el.offsetParent);
    x += pos.x;
    y += pos.y;
  }
  return {x: x, y: y}
}

// The following 3 functions are taken from common.js
function hasClass(el, cl) {
  if (el == null || el.className == null) return false;
  var classes = el.className.split(" ");
  for (var i = 0; i < classes.length; i++) {
    if (classes[i] == cl) {
      return true;
    }
  }
  return false;
}

// Add a class to element
function addClass(el, cl) {
  if (hasClass(el, cl)) return;
  el.className += " " + cl;
} 

// Remove a class from an element
function removeClass(el, cl) {
  if (el.className == null) return;
  var classes = el.className.split(" ");
  var result = [];
  var changed = false;
  for (var i = 0; i < classes.length; i++) {
    if (classes[i] != cl) {
      if (classes[i]) { result.push(classes[i]); }
    } else {
      changed = true;
    } 
  } 
  if (changed) { el.className = result.join(" "); }
} 

function toggleClass(el, cl) {
  if (hasClass(el, cl)) {
    removeClass(el, cl);
  } else {
    addClass(el, cl);
  }
}


/* Constants for node types, since IE doesn't support Node.TEXT_NODE */
var TEXT_NODE = 3;
var ELEMENT_NODE = 1;


