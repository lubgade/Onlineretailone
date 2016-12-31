

$(function() {




		onLoad();
		// Tabs
		var $tabs = $('#tabscollection1').tabs();
		$tabs.bind("tabsselect", function (event, ui){
			if ( ui.panel.id == "CategoryMembership"){

				if (storeListViewModel.store().id){
					var id = storeListViewModel.store().categoryId;
					if (id){
						categoryListViewModel.getCategory(id);
					}

					categoryListViewModel.query("");
					categoryListViewModel.search();

				}else {
					alert("Please select a Store");
					return false;
				}



			}

		});

		$( "#sortable2" ).bind( "sortstop", function(event, ui) {
			$(ui.item).find("button").click();

		});

		$("#citySelect").change(function(){
			alert($("#citySelect").val());
		});
		$("#citySelect").click(function(){
			alert("clicked");
		});


		$("#sortable1,#sortable2").sortable({revert:'invalid', connectWith: ".connectedSortable",cursor:"move", helper:'clone'}).disableSelection();

		$("#storeform").validate({
			submitHandler : function() {
				storeListViewModel.save();
				$('#tabscollection1').tabs();
				return false;
			},
			errorLabelContainer : "#messageBox",
			wrapper : "li",
			errorElement : "em",
			rules : {
				storeName : {
					required : true,
					minlength : 5
				},
				storeDesc : {
					required : true,
					minlength : 7
				},
				managerName : {
					required : true,
					minlength : 5
				},
				lat: {
					required:true,
					minlength:5,
					messages: "please click geocode to get lat and lng"
				},
				lng: {
					required:true,
					minlength:5,
					messages: "please click geocode to get lat and lng"
				}
			}
		});

		
		$(".addButton").button({icons:{primary:"ui-icon-circle-plus"},text:false});
		$(".deleteButton").button({icons:{primary:"ui-icon-circle-close"},text:false});



		$(".geofield").autocomplete({
			source: function( request, response ) {
				console.log("testing" + request);

				$.ajax({
					url: "http://ws.geonames.org/searchJSON",
					dataType: "jsonp",
					data: {
						featureClass: "P",
						style: "full",
						maxRows: 12,
						name_startsWith: request.term
					},
					success: function( data ) {
						response( $.map( data.geonames, function( item ) {
							return {
								label: item.name + (item.adminName1 ? ", " + item.adminName1 : "") + ", " + item.countryName,
								value: item.name
							}
						}));
					}
				});
			},
			minLength: 2,
			select: function( event, ui ) {
				this.value = ui.item ? ui.item.label :this.value;
			},
			open: function() {
				$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
			},
			close: function() {
				$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
			}
		});

		$('#add').click(function() {
			$tabs.tabs('select', 1);

			storeListViewModel.store(new store());

			return false;

		});
		$('.geolocate').click(function() {
			geolocateme();
			$("#map").dialog("open");
			return false;
		});

		$('.geocodeAddress').click(function() {
			var position;
			if (lookupLocation(position)) {
				$("#map").dialog("open");
			}
			return false;
		});

		$('.locate').click(function() {
			var position;
			if (storeListViewModel.store().addresses[0].lat != 1.01) {
				var lat = storeListViewModel.store().addresses[0].lat;
				var lng = storeListViewModel.store().addresses[0].lng;
				position = new google.maps.LatLng(lat, lng);
			}
			if (lookupLocation(position)) {
				$("#map").dialog("open");
			}
			return false;
		});
		$("#map").dialog({
			autoOpen : false,
			resizeStop : function(event, ui) {
				google.maps.event.trigger(map, 'resize')
			},
			open : function(event, ui) {
				google.maps.event.trigger(map, 'resize');
			},
			close : function(event, ui) {
				if (infowindow != null){
					infowindow.close();
				}

				google.maps.event.trigger(map, 'resize');
			}
		});
		$("#map").dialog("option", "height", 530);
		$("#map").dialog("option", "width", 700);

		$("button").button();


		$( "#searchinput" ).autocomplete({
			source: function( request, response ) {
				$.ajax({
					url: "/store",
					dataType: "json",
					data: {
						query:request.term,
						action:"searchasyouType"
					},
					success: function( data ) {

						if (data.data.length > 0 ) {
						response( $.map(data.data, function( item ) {
							return {
								label: item.shingle,
								value: item.shingle
							}
						}));
						}
					},
					error:function(jqhr,options, errorText ){
				  			errortxt = "Status:" + jqhr.status + " Options:" + options + " Error Text " + errorText;
				  			storeListViewModel.status(errortxt);
				  	}
				});
			},
			minLength: 3,
			select: function( event, ui ) {
				storeListViewModel.query(this.value);
			},
			open: function() {
				$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
			},
			close: function() {
				$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
			}
		});


	});

	function store (id, storeName,storeDesc) {
			this.id = id;
			this.storeName =storeName;
			this.storeDesc = storeDesc;
			this.managerName="";
			this.emails = new ko.observableArray([{emailaddress:""}]);
			this.phones=  new ko.observableArray([{number:""}]);
			this.deliveryDistance="";
			this.addresses= [new address()];
			this.allowStorePayment=false;
			this.allowOrderModification=false;
			this.multipleStores=false;
			var edit =function (){
				return this;
			};
			this.categoryId = "";

	}

var address= function() {
	this.addressline1="";
	this.addressline2="";
	this.city="";
	this.state=ko.observable("");
	this.zip="560103";
	this.country="India";
	this.lat=1.01;
	this.lng=1.01;
};

function updateOptions(aCity,aAddress){
	 cityObj =	ko.utils.arrayFirst(citiesDB.cities(),function(item){
			if( item.name === aCity) return item;
	 });
	 if (cityObj){
		 locs =$.map(cityObj.storelocations,function(item){return item.name; });
		 storeListViewModel.locations(locs);
		 aAddress.state(cityObj.state);
	 }
}

var  storeListViewModel = {

		stores: new ko.observableArray(),
		status: ko.observable(),
		store:  ko.observable(new store()),
		locations:ko.observableArray([]),
		cityChanged: function(aAddress){
			updateOptions($("#citySelect").val(),aAddress);
			return true;
		},
		query:  ko.observable(),
		index: function(){
			  $.ajax({
			    	url:"/store",
			    	data:"action=index",
		            type: "post",
		            success: function(result) {
		        		storeListViewModel.status(result);
		            }
		        });
		},
		map: function(data){
			for (var i=0;i<data.length;i++){
				storedata = new store(data[i].id, data[i].storeName, data[i].storeDesc);
				storedata.managerName = data[i].managerName;
				storedata.deliveryDistance = data[i].deliveryDistance;
				storedata.emails(data[i].emails);
				storedata.phones(data[i].phones);
				storedata.addresses = $.map(data[i].addresses, function(item, i){
					addressObj = new address();
					addressObj.addressline1= item.addressline1;
					addressObj.addressline2= item.addressline2,
					addressObj.city =item.city;
					addressObj.state(item.state);
					addressObj.zip=item.zip;
					addressObj.country=item.country;
					addressObj.lat=item.lat;
					addressObj.lng=item.lng;
					return addressObj;
			    });
				storedata.allowStorePayment =data[i].allowStorePayment;
				storedata.allowOrderModification = data[i].allowOrderModification;
				storedata.multipleStores = data[i].multipleStores;
				storedata.categoryId = data[i].categoryId;


				storeListViewModel.stores.push( storedata );
			 }
		},
		searchusinglucene: function(){
			   var mydata = ko.toJS({query:this.query,action:"searchusinglucene"});
			   alert(mydata);
			  $.ajax({
			    	url:"/store",
			    	dataType: "json",
		            data: mydata,
		            type: "GET",
		            success: function(resp) {
		            	if(resp){
		        			//getting the data from the response object
		        			//storeListViewModel.status(resp);
		        			data=resp.data;

			        		if(data.length > 0){
			        			storeListViewModel.stores.removeAll();
			        			storeListViewModel.stores.valueHasMutated();
			        			storeListViewModel.map(data);
			        			storeListViewModel.status("");
		        		    }
			        		else {

			        			storeListViewModel.stores.removeAll();
				        		storeListViewModel.stores.valueHasMutated();
				        		storeListViewModel.status("No records to display");
				             }
		            	}else {
		            		storeListViewModel.stores.removeAll();
			        		storeListViewModel.stores.valueHasMutated();
		            		storeListViewModel.status("No records to display");
		            	}
		            },
		            error:function(jqhr,options, errorText ){
			  			errortxt = "Status:" + jqhr.status + " Options:" + options + " Error Text " + errorText;
			  			storeListViewModel.status(errortxt);
			  		}
		        });

		},

		search: function(){
			   var mydata = ko.toJS({query:this.query,action:"search"});

			  $.ajax({
			    	url:"/store",
			    	dataType: "json",
		            data: mydata,
		            type: "GET",
		            success: function(resp) {
		            	//alert(resp);
		            	var data;
		            	if(resp){
		        			//getting the data from the response object
		        			data=resp.data;
		        		}
		            	if (!data){ return;}
		        		if(data.length > 0){
		        			storeListViewModel.stores.removeAll();
		        			storeListViewModel.stores.valueHasMutated();
		        			storeListViewModel.map(data);
		  		   			storeListViewModel.status("");
		        		}else {

		        			storeListViewModel.stores.removeAll();
			        		storeListViewModel.stores.valueHasMutated();
			        		storeListViewModel.status("No records to display");
		        		 }
		            },
			  		error:function(jqhr,options, errorText ){
			  			errortxt = "Status:" + jqhr.status + " Options:" + options + " Error Text " + errorText;
			  			storeListViewModel.status(errortxt);
			  		}
		        });

		},
	 	addEmail: function(emails){
	 		emails.push({emailaddress:""});
	 	},
	 	addPhone: function(phones){
	 		phones.push({number:""});
	  	} ,
	 	removePhone: function (phone) {
	 		this.store().phones.remove(phone);
	    },
	    removeEmail: function (email) {

	    	this.store().emails.remove(email);
	    },
	    edit: function(storeobject){
	    	this.store(storeobject);
	    	updateOptions(this.store().addresses[0].city,this.store().addresses[0]);
	    	return false;
	    },
	    deletestore:function(store){
		    var id = store.id;
	    	$.ajax({
		    	url:"/store",
	            data: "id="+id+"&action=delete",
	            type: "post",
	            success: function(result) {
	            storeListViewModel.status("Store with Store id" + id + " deleted successfully");
	            },
	                error:function(jqhr,options, errorText ){
			  			errortxt = "Status:" + jqhr.status + " Options:" + options + " Error Text " + errorText;
			  			storeListViewModel.status(errortxt);
			  		}
	        });
	    },
	    makeStoreCategory: function(category,aStoreid){

	 	   var mydata = ko.toJS({storeid:aStoreid,categoryid:category.id,action:"makeStoreCategory"});
	 	  $.ajax({
		    	url:"/store",
		    	dataType: "text",
	           data: mydata,
	           type: "POST",
	           success: function(resp) {
	           	console.log(resp);
	           	if(resp){
	           		alert(resp);
	       		}

	           },
		  		error:function(jqxhr, e, errorText){
		  			alert("error : "+ errorText);
		  			console.log("jqxhr:"+jqxhr, " e:"+e + " errortext:"+errorText);
		  		}
	       });
	 	  categoryListViewModel.storecategory(category);
	    },
	   	save:function() {
		   var mydata = ko.toJSON({store:this.store,action:"PUT"});
		    $.ajax({
		    	url:"/store",
	            data: "JSON="+mydata+"&action=PUT",
	            type: "post",
	            success: function(result) {
	        		alert(result);
	        		storeListViewModel.store(new store());
	        		storeListViewModel.locations([]);
	                },
	                error:function(jqhr,options, errorText ){
			  			errortxt = "Status:" + jqhr.status + " Options:" + options + " Error Text " + errorText;
			  			storeListViewModel.status(errortxt);
			  		}
	        });
	    }
	 };

	storeListViewModel.gridViewModel = new ko.simpleGrid.viewModel({
	    data: storeListViewModel.stores,
	    columns: [{headerText:"Edit",action:"Edit", rowLink:function(storeObj){
	    	$('#tabscollection1').tabs('select',1);
	    	storeListViewModel.edit(storeObj);

	    }},
	              {headerText:"Delete",action:"Delete",rowText:"", rowLink:function(store){storeListViewModel.deletestore(store)}},
	        { headerText: "Store Name",action:"", rowText: "storeName",rowLink:"" },
	        { headerText: "Store Desc",action:"", rowText: "storeDesc",rowLink:"" },
	        { headerText: "Manager Name",action:"", rowText: "managerName",rowLink:"" },
	        { headerText: "Delivery Distance",action:"", rowText: "deliveryDistance",rowLink:"" },
	        { headerText: "Address Line1",action:"", rowText: function(storeObj){return storeObj.addresses[0].addressline1},rowLink:"" },
	        { headerText: "Address Line2",action:"", rowText: function(storeObj){return storeObj.addresses[0].addressline2},rowLink:"" },
	        { headerText: "City", action:"",rowText: function(storeObj){return storeObj.addresses[0].city;},rowLink:"" },
	        { headerText: "State",action:"", rowText: function(storeObj){return storeObj.addresses[0].state;},rowLink:"" },
	        { headerText: "Postal Code", action:"",rowText:function(storeObj){return storeObj.addresses[0].zip},rowLink:"" },
	        { headerText: "Country", action:"",rowText: function(storeObj){return storeObj.addresses[0].country},rowLink:"" },
	   	     { headerText: "Phone",action:"", rowText: function(storeObj){
	      		var phonedata ="";
	      		if ( storeObj.phones().length > 0){
		      		for (var i =0 ; i < storeObj.phones().length ; i++){
		      			phonedata = phonedata + storeObj.phones()[i].number + ",";
		      		}
	      		}
	      		phonedata = phonedata.substr(0,(phonedata.length-1));
	      		return phonedata;

	        },rowLink:"" },
	   	     { headerText: "CategoryId",action:"", rowText: function(storeObj){
		      		return storeObj.categoryId;

		        },rowLink:"" }

	     ],
	    pageSize: 10
	});

$(function(){

	ko.applyBindings(storeListViewModel);





});
