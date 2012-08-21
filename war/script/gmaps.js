/*
 * Author : Leena Ubgade
 * Google Maps loading
 */

var map = null;
var prev_pin = null;
var geocoder;
var infowindow =null;
function onLoad() {
	if (typeof google === "undefined"){ 
	 return;
	}
	geocoder = new google.maps.Geocoder();
	    var latlng = new google.maps.LatLng(12.92677,77.670893);
	    var myOptions = {
	      zoom: 14,
	      center: latlng,
	      mapTypeId: google.maps.MapTypeId.ROADMAP
	    }
	 map = new google.maps.Map(document.getElementById("map"), myOptions);
	 
	google.maps.event.addListener(map, 'click', function(event) {
		addMarker(event.latLng);
 	   });
	

 }

function handleNoGeolocation(errorFlag) {
    if (errorFlag) {
      var content = 'Error: The Geolocation service failed.';
    } else {
      var content = 'Error: Your browser doesn\'t support geolocation.';
    }

    var options = {
      map: map,
      position: new google.maps.LatLng(12.92677,77.670893),
      content: content
    };

    infowindow = new google.maps.InfoWindow(options);
    map.setCenter(options.position);
  }

function addMarker(location){
		if (prev_pin) { 
			prev_pin.setMap(null); 
			prev_pin = null; 
		} 
		if (location) { 
			pin = new google.maps.Marker({position:location,map:map,animation:google.maps.Animation.BOUNCE}); 
			prev_pin = pin; 
 			latDiv = document.getElementById('lat');
			lngDiv = document.getElementById('lng');
			storeListViewModel.store().addresses[0].lat = location.lat();
			storeListViewModel.store().addresses[0].lng = location.lng();
			lngDiv.value = location.lng();
			latDiv.value = location.lat();
		} 
}
function geolocateme(){
	 // Try HTML5 geolocation
	
	if(navigator.geolocation) {
	      navigator.geolocation.getCurrentPosition(function(position) {
	        var pos = new google.maps.LatLng(position.coords.latitude,
	                                         position.coords.longitude);

	        infowindow = new google.maps.InfoWindow({
	          map: map,
	          position: pos,
	          content: 'We found your location'
	        });
	        addMarker(pos);	
	        map.setCenter(pos);
	      }, function() {
	        handleNoGeolocation(true);
	      });
	      return true;
	    } else {
	    	// Browser doesn't support Geolocation
	    	 
	    	handleNoGeolocation(false);
	    }
}

function lookupLocation(pos) {
if (pos){
	addMarker(pos);	
	infowindow = new google.maps.InfoWindow({
        map: map,
        position: pos,
        content: 'Currently mapped Location'
      });
    map.setCenter(pos);
}	  
else{
    var street = document.getElementById("addressline1").value ;
    var addressline2 =document.getElementById("addressline2").value ;
    var city = storeListViewModel.store().addresses[0].city;
    var state =document.getElementById("state").value;
    if (!city || !state || !addressline2 ) {
        alert("At least fill in the addressline1,addressline2 city and state,");
        return false;
     }
	if (prev_pin) { 
		prev_pin.setMap(null); 
		prev_pin = null; 
	}
	var htmlstatus = document.getElementById('status');
	var prev_status = htmlstatus.innerHTML;
	htmlstatus.innerHTML = "looking up address...please wait...";
	var address = street + "," + addressline2 + "," + city + "," + state;
    geocoder.geocode( { 'address': address}, function(results, status) {
    	
      if (status == google.maps.GeocoderStatus.OK) {
        map.setCenter(results[0].geometry.location);
        addMarker(results[0].geometry.location);
        htmlstatus.innerHTML =prev_status;
        return true;
      } else {
        alert("Geocode was not successful for the following reason: " + status);
      }
    });
	}
    
    return true;
  }
