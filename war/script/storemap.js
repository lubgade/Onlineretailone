var map; 
var markers = []; 
var infoWindow; 
var locationSelect; 
var bounds;

function load() { 
	if (!google){ return }
	if (storeListViewModel.selectedStore()){
		$("#HomePage").fadeIn('slow');
		$("#HomePage").show();
		$("#searchStoresnearBy").hide();
		
	}
	
	map = new google.maps.Map(document.getElementById("map"), { 
	center: new google.maps.LatLng(12.92677,77.670893), 
	zoom: 14, 
	mapTypeId: 'roadmap', 
	mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU} 
	}); 
	bounds =  new google.maps.LatLngBounds(new google.maps.LatLng(12.92677,77.670893), new google.maps.LatLng(12.92677,77.670893)), 
	
	infoWindow = new google.maps.InfoWindow(); 
	
	locationSelect = $("#locationSelect"); 
	
	locationSelect.change (function() { 
		var markerNum = locationSelect.val(); 
		if (markerNum != "none"){ 
		google.maps.event.trigger(markers[markerNum], 'click'); 
		} 
	});
} 

$(function() { 
	$("#sortable").sortable({revert:true});
	$("#searchByAddressFields").button({icons:{secondary:"ui-icon-search"}});
	$("#searchButton").button({icons:{secondary:"ui-icon-search"}});
	
$("#searchtabs").tabs({select:function(event,ui){ 
		
		google.maps.event.trigger(map, 'resize');

	},
	show:function(event,ui){
		load();
	}
	
	
	});

	
	
	$("#searchButton").click(function(){
		searchLocations();
	});
	$("#searchByAddressFields").click(function(){
		storeListViewModel.searchByAddressFields();
		
	});
	
	
});





function searchLocations() { 
	var address = document.getElementById("addressInput").value; 
	var geocoder = new google.maps.Geocoder(); 
	geocoder.geocode({address: address}, function(results, status) { 
	if (status == google.maps.GeocoderStatus.OK) { 
	searchLocationsNear(results[0].geometry.location); 
	} else { 
	alert(address + ' not found'); 
	} 
	}); 
} 

function clearLocations() { 
	if ( infoWindow){
		infoWindow.close();
	}
	for (var i = 0; i < markers.length; i++) { 
	markers[i].setMap(null); 
	} 
	markers.length = 0; 
	
	locationSelect.html("");
	var option = document.createElement("option"); 
	option.value = "none"; 
	option.innerHTML = "See all results:"; 
	locationSelect.prepend(option); 
} 

function searchLocationsNear(center) { 
	clearLocations(); 
	
	var bounds = new google.maps.LatLngBounds(new google.maps.LatLng(12.92677,77.670893), new google.maps.LatLng(12.92677,77.670893)); 
	var radius = document.getElementById('radiusSelect').value; 
	var searchUrl = 'latitude=' + center.lat() + '&longitude=' + center.lng() + '&distance=' + radius+'&action=searchByAddress'; 
	alert(searchUrl);
	$.ajax({
		url:"/store",
		dataType: "json",
	    data: searchUrl,
	    type: "GET",
	    success: function(resp) {
	    	//alert(resp);
	    	if(resp){
				//getting the data from the response object
				data=resp.data;
			}
			if(data.length > 0){
				storeListViewModel.map(data);
				for (var i=0;i<data.length;i++){
					var name = data[i].storeName; 
					var address = data[i].addresses; 
				
					var distance = radius; 
					var latlng = new google.maps.LatLng( 
					parseFloat(address[0].lat), 
					parseFloat(address[0].lng)); 
					createOption(name, distance, i); 
					createMarker(latlng, name, address[0],data[i].id); 
					bounds.extend(latlng);
				}//end for
			}
			else {
				alert("No Stores found near by");
			 }
	    },
		error:function(jqhr,options, errorText ){
				errortxt = "Status:" + jqhr.status + " Options:" + options + " Error Text " + errorText;
				alert(errortxt);
			}
	});

	//map.fitBounds(bounds); 
	
	locationSelect.css("visibility","visible");
	
	locationSelect.change (function() { 
		var markerNum = locationSelect.val(); 
			if (markerNum != "none"){ 
				google.maps.event.trigger(markers[markerNum], 'click'); 
			}
		}); 
} //end search locations

$("#storeinfo").live("click",function(){

	$("#HomePage").fadeIn('slow');
	categoryListViewModel.catids.removeAll();
	categoryListViewModel.catids.valueHasMutated();
	categoryListViewModel.getchildcategories(storeListViewModel.selectedCategory());
	categoryListViewModel.catids.valueHasMutated();

	categoryListViewModel.getCategoryProducts(storeListViewModel.selectedCategory());
	storeListViewModel.saveSelectedStore();
	$("#searchStoresnearBy").dialog('close');

	
});

function createMarker(latlng, name, address,id) { 
	var html = "<b><a id='storeinfo'  href='#'>" + name + "</a></b> <br/><address>" + address.addressline1 +"<br/>";
	html +=    address.addressline2 + "<br/>"+  address.city + "<br/>" + address.state + "<br/>"+ address.country + "</address>";
	var marker = new google.maps.Marker({ 
	map: map, 
	position: latlng 
	}); 
	google.maps.event.addListener(marker, 'click', function() { 
	infoWindow.setContent(html); 
	infoWindow.open(map, marker); 
	}); 
	markers.push(marker); 
} 

function createOption(name, distance, num) { 
var option = document.createElement("option"); 
option.value = num; 
option.innerHTML = name + "(" + distance + ")"; 
locationSelect.prepend(option); 
} 

function store (id, storeName,storeDesc) {
	this.id = id;
	this.storeName =storeName;
	this.storeDesc = storeDesc;
	this.managerName="";
	this.emails = ko.observableArray([{emailaddress:""}]);
	this.phones=  ko.observableArray([{number:""}]);
	this.deliveryDistance="";
	this.addresses=[{addressline1:"", addressline2:"",city:"",state:"",zip:"", country:"",lat:1.01,lng:1.01 }];
	this.allowStorePayment=false; 
	this.allowOrderModification=false; 
	this.multipleStores=false;
	var edit =function (){
		return this;
	};
	this.categoryId = "";
	this.index =0;
	
}
var  storeListViewModel = {
		stores: new ko.observableArray(),
		store: ko.observable( new store()),
		locations:ko.observableArray([]),
		city:ko.observable(),
		addressline1:ko.observable(""),
		addressline2:ko.observable(""),
		selectedCategory:ko.observable(),
		selectedStore:ko.observable(),
		selectedStoreObj: new ko.observable(),
		higlightMarker: function(markerNum,aCategory,storeId,storeName,storeObj){
			if (markerNum != "none"){ 
				$("#searchtabs").tabs("select",1);
				google.maps.event.trigger(markers[markerNum], 'click');
				this.selectedStoreObj(storeObj);
				this.selectedCategory(aCategory);
				this.selectedStore(storeId);
				this.saveSelectedStore();
				if(typeof(Storage)!=="undefined"){
					localStorage.store = storeObj;
				}
			} 
		},
		saveSelectedStore: function(){
			
			var mydata = "action=setStoreForOrder&selectedStore="+this.selectedStore()
			$.ajax({
				url:"/store",
				dataType: "json",
			    data: mydata,
			    type: "POST",
			    success: function(data) {
			    	
			    },
			error:function(jqhr,options, errorText ){
				errortxt = "Status:" + jqhr.status + " Options:" + options + " Error Text " + errorText;
				alert(errortxt);
			}
			});
			    
			
		},
		getSelectedStoreFromSession: function(){
			
			var mydata = "action=getSelectedStoreFromSession"
			$.ajax({
				url:"/store",
				dataType: "json",
			    data: mydata,
			    type: "POST",
			    success: function(data) {
			    	if (data){
			    		storeListViewModel.selectedStoreObj(data);
			    		storeListViewModel.selectedStore(storeListViewModel.selectedStoreObj().id);
			    		storeListViewModel.selectedCategory(storeListViewModel.selectedStoreObj().categoryId);
						categoryListViewModel.catids.removeAll();
						categoryListViewModel.catids.valueHasMutated();
						categoryListViewModel.getchildcategories(storeListViewModel.selectedCategory());
						categoryListViewModel.catids.valueHasMutated();
						categoryListViewModel.getCategoryProducts(storeListViewModel.selectedCategory());

			    	}
			    },
			error:function(jqhr,options, errorText ){
				errortxt = "Status:" + jqhr.status + " Options:" + options + " Error Text " + errorText;
				alert(errortxt);
			}
			});

		},
		
		searchByAddressFields: function(){
			locationSelect = $("#locationSelect"); 

			clearLocations(); 
			var mydata =  ko.toJS({addressline1:this.addressline1,addressline2:this.addressline2,city:this.city().name,state:this.city().state,action:"searchByAddressFields"});
			$.ajax({
				url:"/store",
				dataType: "json",
			    data: mydata,
			    type: "GET",
			    success: function(resp) {
			    	//alert(resp);
			    	if(resp){
						//getting the data from the response object
						data=resp.data;
					}
					if(data.length > 0){
						storeListViewModel.map(data);
						for (var i=0;i<data.length;i++){
							var name = data[i].storeName; 
							var address = data[i].addresses; 
							var distance = 2; 
							var latlng = new google.maps.LatLng( 
							parseFloat(address[0].lat), 
							parseFloat(address[0].lng)); 
							createOption(name, distance, i); 
							createMarker(latlng, name, address[0]); 
							bounds.extend(latlng);
						}//end for
					}
					else {
						alert("No Stores found near by");
					 }
					locationSelect.css("visibility","visible");
					
					locationSelect.change (function() { 
						var markerNum = locationSelect.val(); 
						google.maps.event.trigger(markers[markerNum], 'click'); 
						}); 
			    },
				error:function(jqhr,options, errorText ){
						errortxt = "Status:" + jqhr.status + " Options:" + options + " Error Text " + errorText;
						alert(errortxt);
					}
			});
		},
		map: function(data){
			storeListViewModel.stores.removeAll();
			for (var i=0;i<data.length;i++){
				storedata = new store(data[i].id, data[i].storeName, data[i].storeDesc);
				storedata.managerName = data[i].managerName;
				storedata.deliveryDistance = data[i].deliveryDistance;
				storedata.emails(data[i].emails);
				storedata.phones(data[i].phones);
				storedata.addresses = data[i].addresses;
				storedata.allowStorePayment =data[i].allowStorePayment;
				storedata.allowOrderModification = data[i].allowOrderModification;
				storedata.multipleStores = data[i].multipleStores;
				storedata.categoryId = data[i].categoryId;
				storedata.index = i;
				storeListViewModel.stores.push( storedata );
			 }
		}
}
$(function(){
	 
	ko.applyBindings(storeListViewModel);
	storeListViewModel.getSelectedStoreFromSession();
	

});


