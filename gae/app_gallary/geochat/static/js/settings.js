/* settings.js */

var geocoder = null;
var validated = null;

function ValidateLocationCallback(latlng) {
  if (latlng) {
    validated = true;
  }
}

function ValidateLocation(location) {
  /*if (geocoder) {
    geocoder.getLatLng(location, ValidateLocationCallback);
  } else {
    alert('Can\'t validate address: Geocoder failed to load.');
    return false;
  }*/
}

window.onload = function() {
  if (GBrowserIsCompatible()) {
    geocoder = new GClientGeocoder();
  }
}

function IsValidLocation() {
/*  if (!validated) {
    alert('Can\'t validate address: Ensure that your home location is ' +
        'valid, or try again in a few seconds.');
  }
  return validated;*/
  if (geocoder) {
    geocoder.getLatLng(location, ValidateLocationCallback);
  } else {
    alert('Can\'t validate address: Geocoder failed to load.');
    return false;
  }
  while (validated == null) {
    // Wait.
  }
  if (validated) {
    return validated;
  } else {
    alert('Can\'t validate address: Ensure that your home location is ' +
        'valid, or try again in a few seconds.');
    validated = null;
    return false;
  }
}

window.onunload = function() {
  GUnload();
};


