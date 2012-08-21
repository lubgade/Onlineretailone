$(function(){
	
	//$("#productList").selectable();
	//$("#productList").draggable();

	$("#productList").sortable();
	/*$("#sortable").sortable();
	$("#sortable").draggable();*/
	
	
	
	$(function() {		
		
		$("#searchStoresnearBy").dialog({modal:true,hide:"explode",width:600,title:"Select nearest Store to Order"})
		
		$( "#tabs" ).tabs();	
		
				
	  $("#tabs").tabs("option","disabled",[1,2,3]);
		
	  $("#right_div_2").hide();
		$("#continue").click(function(){
           // alert("clicked");
			$("#tabs").tabs("option","disabled",[2,3]);
			$("#tabs").tabs("select",1);				
			$("#right_div_2").hide();
			
			$.ajax({
				url:"/order",
		    	dataType: "json",
	            data: {action:"getparty"},
	            type: "POST",
	            success: function(data) {
	            	if(data){
	            		partyobj = new party();
	            		if ( data.partyAddresses){
	            			partyobj.firstName(data.firstName);
	            			partyobj.lastName(data.lastName);
	            			partyobj.phones = data.phones;
	            			partyobj.partyType(data.partyType);
	            			partyobj.partyAddresses(data.partyAddresses);
	            			partyViewModel.currentparty(partyobj);
	            		}else {
	            			$("#l_div").hide();
	            			
	            		}
	            	}
	            },
	            error:function(jqxhr, e, errorText){
    	  			console.log("error : "+ errorText);
    	  		}  
   
	            
			});
			
			

		});

    
		$("#clktoselect").click(function(){
		
		});


		$("#savetocont").click(function(){
			
		$("#tabs").tabs("option","disabled",[3]);
		
		//$("#right_div_2").hide();
		  partyViewModel.address().city(partyViewModel.selectedcity().value);
		  partyViewModel.address().state(partyViewModel.selectedcity().state);
		  
		  var match = ko.utils.arrayFirst(partyViewModel.currentparty().partyAddresses(), function(item) {
			    return partyViewModel.address.addressline1 === item.addressline1;
			});

		  if (!match){
			  partyViewModel.currentparty().partyAddresses.push(partyViewModel.address());

		  }
		  partyViewModel.currentparty().partyAddresses.valueHasMutated();
		  var mydata = ko.toJSON(partyViewModel.currentparty());
		  $.ajax({
			url:"/order",
	    	dataType: "json",
	    	 data: "JSON="+mydata+"&action=createparty",
            type: "POST",
            success: function(data) {
      		  	partyViewModel.setShippingAddress(partyViewModel.address());
      		    partyViewModel.address(new address());
      		/* $("#addressForm").get(0).reset();*/
      		  	
      		  	      	},
           
            error:function(jqxhr, e, errorText){
	  			console.log("error : "+ errorText);
	  		}  
            	
            
		}); 

		 
		  $("#right_div_2").show();
		});

		$("#cont").click(function(){
			$("#tabs").tabs("enable",3);
			
			$("#tabs").tabs("select",3);
			$("#right_div_2").show();
						
		     $("#CODform").validate({
			 submitHandler : function() {
				 var challenge_field = Recaptcha.get_challenge();
				 var response_field = Recaptcha.get_response();
			
			if ( !response_field ){
				$("#messageBox").show();
				$("#messageBox").html("<li><em> Please complete the Captcha</em></li>");
				return false;
				
			}
			else
				{
				currentorder=new order();
				var $curTab = $('#subtabs').tabs();
				var selected = $curTab.tabs('option', 'selected');
				
				if(selected == 0)
					currentorder.orderPaymentType="CashOnDelivery";
				if(selected == 1)
					currentorder.orderPaymentType="NetBanking";
				
				currentorder.orderUserId=shoppingCartListViewModel.userId;
				currentorder.orderType="Delivery";
				currentorder.orderName="XYZ";
				currentorder.orderTotalAmount= shoppingCartListViewModel.amountPayable();
			    currentorder.orderShippingAddress=partyViewModel.shippingAddress(); 
			    currentorder.orderAdjustment = shoppingCartListViewModel.totalAdjustments();
			    currentorder.shippingCharges =  shoppingCartListViewModel.shippingAmt();
			    currentorder.netAmount = shoppingCartListViewModel.grandTotal();
			    currentorder.orderTotalDiscount=shoppingCartListViewModel.totalAdjustments();
			    currentorder.selectedTmg=orderViewModel.selectedtmg();
			    var mydata = ko.toJSON(currentorder);
			  //  console.log(mydata);
				$.ajax({
					url:"/order",
			    	dataType: "text",
			    	 data: "JSON="+mydata+"&action=createorder&recaptcha_challenge_field=" + challenge_field + "&recaptcha_response_field=" + response_field,
		            type: "POST",
		            success: function(resp) { 
				 		if(resp)
				 		
				 		{
				 			orderViewModel.confirmedorder(resp);
				 			if(typeof(Storage)!=="undefined"){
				 				localStorage.order =orderViewModel.confirmedorder();

				 			}

				 			$("#CODform").fadeOut();
				 			
				    		$("#messageBox").html("");
							$("#messageBox").fadeOut();
							
				 		}
							location.replace("/orderresponse.jsp");
							//$("#orderresp").show();
				    	             
				    	},
				         error: function(jqxhr, e, errorText){
				        	 if ( errorText === "RECAPCHA_VALIDATION_FAILED"){
						    		$("#messageBox").show();
									$("#messageBox").html("<li><em> Please complete the Captcha correctly</em></li>");
									Recaptcha.reload();
						    		return false;
						 		}
					  			console.log(e);
					  			console.log(jqxhr);
					  			console.log(errorText);
					  			return false;
					  		}  
		            
				});
				return false;			
				}
			 },
			
			
			 
			 errorLabelContainer : "#messageBox",
	    		wrapper : "li",
	    		errorElement : "em",
		
		});
		 
		}); 
		 

		$("#placeord").click(function(){
			
			
			
			
		//	location.replace("/orderresponse.html");
		});
		
	   
		
		
		
		$("#tabs").tabs({			
			select: function(event,ui){
				setTimeout(function(){
				if(ui.index==0){
					$("#tabs").tabs({disabled:[1,2,3]});
					$("#right_div_2").hide();
				}
				if(ui.index==1){
					//alert("hello");
					$("#tabs").tabs("option","disabled",[2,3]);
				}
				if(ui.index==2){
					$("#tabs").tabs("option","disabled",[3]);
				}
			
			},5);
			}
		});
			
		
	
	});
	

	
	//$("#right_div_2").hide();
	
$("#changeshipadd").click(function(){
	$("#tabs").tabs("select",1);				

});	
	


$("#contactdialog").dialog({
	autoOpen: false,
	show: "blind",
	modal: "true"	
});	
	
$("#contact").click(function() {
	$( "#contactdialog" ).dialog( "open" );
	return false;
});

$(".addtocartbutton").button({icons:{primary:"ui-icon-search"},text:false});

$("#sbutton").button({icons:{primary:"ui-icon-search"},text:false});

$("#btn").button({icons:{primary:"ui-icon-close"},text:false})

$(".addtocart").button({icons:{secondary:"ui-icon-circle-triangle-e"}})

$(".addtocart").click(function(){
	shoppingCartListViewModel.additems(productListViewModel.currentproduct());
	
});

$("#logindialog").dialog({
	autoOpen: false,	
	modal: "true",
	width: 500,
	buttons: {
		"Close Window": function(){
			$( this ).dialog( "close" );
		}
	}
	
});

$("#login").click(function(){
	$("#logindialog").dialog("open");
	return false;
});

$("#product_div").dialog({
	autoOpen: false,		
	modal: "false",
	width:600,
	buttons: {
		"Close Window": function(){
			$( this ).dialog( "close" );
		}
	}
});


$("#signupdialog").dialog({
	autoOpen: false,		
	modal: "true",
	width:500,
	buttons: {
		"Close Window": function(){
			$( this ).dialog( "close" );
		}
	}
});



$("#signup").click(function(){
	$( "#logindialog" ).dialog( "close" );
	$("#signupdialog").dialog("open");
	return false;
});

$("#shopcart").click(function(){
	$("#shopcartdialog").dialog("open");
	return false;
});

$("#shopcartdialog").dialog({
	autoOpen: false,	
	modal: "true",
	width: 500,
	buttons: {
		"Close & Continue Shopping": function(){
			$( this ).dialog( "close" );
		},
        "Place Order":function(){
        	location.replace("/order");
        	$("#right_div_2").hide();
        
        }
	}
	
});

$("#subtabs").tabs().addClass('ui-tabs-vertical ui-helper-clearfix');
$("#subtabs li").removeClass('ui-corner-top').addClass('ui-corner-left');

//$("#subtabs").tabs();



$("#account").click(function(){
	$("#searchResults").hide();
	$("#products").hide();
	$("#content").show();
	$("#content").load("/account.html");
});

$("#orderstatus").click(function(){	
	$("#rightacctcontent").load("/orderstatus.html");
});

$("#myorders").click(function(){
	$("#rightacctcontent").load("/myorders.html");
});

$("#personalinfo").click(function(){
	$("#rightacctcontent").load("/personalinfo.html");
});

$("#addresses").click(function(){
	$("#rightacctcontent").load("/addresses.html");
});

$("#product_div").hide();
$("#orderresponse").hide();
});



var cartitem = function(product){
	this.productprice= ko.observable(0);
	
	if ( product ){
		this.productId = product.id;
		this.productName = product.productName;
		this.taxType = product.taxType;
		this.productprice= ko.observable(product.sellPrice);			
		this.mrp = product.mrp;		
		if(this.mrp > this.productprice()){
		    
			this.adjustments= this.mrp - this.productprice();
			}
		else {
			this.adjustments=0.0;
		}	
	}
	this.originaladj= ko.observable(0.00);

	this.quantity = ko.observable(1);
		
	this.subtotal = ko.dependentObservable(function(){ 
		var subt =(this.quantity() * this.productprice()); 
		return subt.toFixed(2);
	},this);

}

var party = function(){
	this.firstName = ko.observable("");
	this.lastName = ko.observable("");
	this.phones =  new ko.observableArray([]) ;
	this.partyType = ko.observable("customer");
	this.partyAddresses = new ko.observableArray([]);
	
}
var address  = function() {
    this.id="";
	this.addressline1 = ko.observable("");
	this.addressline2=ko.observable("");
	this.city=ko.observable("");
	this.state=ko.observable("");
	this.zip=ko.observable("");
	this.country=ko.observable("india");
	this.primaryAddress=ko.observable("0");
	this.name=ko.observable("");
	this.phonenumber = ko.observable("");
}

var order = function() {
	
	
	this.orderType="";
	this.orderName="";
	this.orderNumber="";
	this.orderNotes="";
	this.netAmount=0.0;
	this.orderStoreId=0.0;
	this.orderShipStatus="";
	this.orderTotalAmount=0;
	this.orderTotalDiscount=0;
	this.orderLineItems = new ko.observableArray([]);
	this.shippingCharges =0.0;
	this.orderShippingAddress=new address();
}

var paymentinfo = function(){
	this.paymentAmount=0;
	this.paymentType="";
	this.paymentCardLast4digits="";
	this.paymentStatus="";
	
}

var orderViewModel = {
		availableTimings:ko.observable(['9 Am - 12 PM','12 PM - 3 PM','3 PM - 6 PM','6 PM - 9 PM']),
		selectedtmg:ko.observable(),
		confirmedorder:  ko.observable(new order()),
		getOrderfromLocalStorage:function(){
			if(typeof(Storage)!=="undefined"){
				if ( localStorage.order ){
					var orderObj = JSON.parse(localStorage.order);	
					orderViewModel.confirmedorder(orderObj);
					console.log(orderViewModel.confirmedorder());
				}

 			}

		}
		
}

var partyViewModel = {
		currentparty:ko.observable(new party()),
		selectedcity:ko.observable(),
		address:ko.observable(new address()),
		phone:ko.observable(""),
		partyType:"CUSTOMTER",
		shippingAddress: ko.observable(new address()),
		setShippingAddress: function(address) {
			this.shippingAddress(address);
			$("#tabs").tabs("option","disabled",[3]);
	       	$("#tabs").tabs("select",2);
				$("#right_div_2").show();
		}
		
} 

var shoppingCartListViewModel = {
		cartitems:ko.observableArray(),
		userId: ko.observable(),
		currentproduct: ko.observable(),
		additems:function(aProduct){
			var item  = new cartitem(aProduct);
			var items = $.map(this.cartitems(),function(a,i){ return a.productId;});
			if ( items.indexOf(item.productId) == -1){
				this.cartitems().push(item);
				this.cartitems.valueHasMutated();
			}
			else
			{	
			
				console.log();	
				var cItem  = this.cartitems()[items.indexOf(item.productId)];
				cItem.quantity(cItem.quantity()+1);
				if (cItem.originaladj() == 0.00){
					cItem.originaladj(cItem.adjustments);
				}
				cItem.adjustments=cItem.originaladj()*cItem.quantity();
				alert(cItem.adjustments);
		/*	    this.cartitems()[items.indexOf(item.productId)].quantity( this.cartitems()[items.indexOf(item.productId)].quantity()+1) ;*/
				

				 this.cartitems.valueHasMutated();
			}
			var mydata = ko.toJSON({items:this.cartitems,userId:"1"});
			/*console.log(mydata);*/
			$("#"+aProduct.id).effect('transfer',{ to: "#cart", className: 'ui-effects-transfer' },500, this.callback(aProduct.id));
			 $.ajax({
			 	url:"/cart",
			     data: "JSON="+mydata+"&action=PUT",
			     type: "post",
			     success: function(result) { 
			 		console.log(result);
			 		
			         },
			         error:function(e){
				  			alert("error : "+ e);
				  		}  
			});
},
callback:function(id){
			setTimeout(function(id) {
			$( "#"+id ).removeAttr( "style" ).hide().fadeIn();
		}, 1000 );
	
},

showproduct:function(product){
	productListViewModel.currentproduct(product);
	if(typeof(Storage)!=="undefined"){
		localStorage.currentproduct = product;
	}
	$("#product_div").dialog("open");
	
	
},

getcart:function(){
	   $.ajax({
	     	url:"/cart",
	    	dataType: "json",
	         data: "action=getcart",
	         type: "post",
	         success: function(resp) { 
	     		if ( resp){
	     			this.userId = resp.userId;
		     		if ( resp.items){
		     			if ( resp.items.length > 0){
		     				$.map(resp.items,function(item,index){
		     					cartobj = new cartitem();
		     					cartobj.productId = item.productId;
		     					cartobj.productName = item.productName;
		     					cartobj.quantity(item.quantity);
		     					cartobj.productprice(item.productprice);
		     					cartobj.mrp = item.mrp;
		     					cartobj.adjustments = item.adjustments;
		     					shoppingCartListViewModel.cartitems().push(cartobj);
		     					});
	     					shoppingCartListViewModel.cartitems.valueHasMutated();
	
		     			}
		     		}
	     		}
	     		
	     		},
	             error:function(jqxhr, e, errorText){
	    	  			console.log("error : "+ errorText);
	    	  		}  
	    });                 
},
removeitem:function(cartitem){                                                                                                                                                                                                                                                                                                             
	ko.utils.arrayRemoveItem(this.cartitems, cartitem);
    this.cartitems.valueHasMutated();
    var mydata = ko.toJSON({items:this.cartitems});
   /* console.log(mydata);*/
     $.ajax({
     	url:"/cart",
         data: "JSON="+mydata+"&action=PUT",
         type: "post",
         success: function(result) { 
     		console.log(result);
     		
             },
             error:function(e){
    	  			alert("error : "+ e);
    	  		}  
    });
}
}
shoppingCartListViewModel.grandTotal= new ko.dependentObservable(function(){        
	var total = 0;
    $.each(this.cartitems(), function(index, item) { total += item.quantity() * item.productprice() });
    return total.toFixed(2);
}, shoppingCartListViewModel);

shoppingCartListViewModel.totalAdjustments = new ko.dependentObservable(function(){
	var totaladj=0;
	$.each(this.cartitems(), function(index, item) { totaladj += item.adjustments});
    return totaladj;
},shoppingCartListViewModel);

shoppingCartListViewModel.totalQuantity= new ko.dependentObservable(function(){        
	var totalq = 0;
    $.each(this.cartitems(), function(index, item) { totalq += parseInt(item.quantity()) });
    return totalq;
}, shoppingCartListViewModel);

shoppingCartListViewModel.shippingAmt =  new ko.dependentObservable(function(){
	var shipamt = 0.00;
	if (this.grandTotal() > 200){
		return shipamt;
	}else {
		shipamt= 30.00; 
		return shipamt.toFixed(2);
	}
	return shipamt;
},shoppingCartListViewModel);

shoppingCartListViewModel.amountPayable =  new ko.dependentObservable(function(){
	var amountPayable;
	if (this.grandTotal()){
		amountPayable =(parseFloat(this.grandTotal()) + parseFloat(this.shippingAmt()));
	}
	return amountPayable.toFixed(2);
},shoppingCartListViewModel);




shoppingCartListViewModel.gridViewModel = new ko.simpleGrid.viewModel({
	data: shoppingCartListViewModel.cartitems,
	columns: [
	  	    {footerText:"", headerText: "Item Name", action:"",rowText: "productName",rowLink:"" },
	    { footerText:"",headerText: "Price", action:"",rowText: "productprice",rowLink:"" },
	    { footerText:"",headerText: "Quantity", action:"",rowLink:"",rowInput:"quantity" },
	    {footerText:"GrandTotal", headerText: "Sub-Total", action:"",rowText: "subtotal",rowLink:"" },
	    {footerText:function(){ return shoppingCartListViewModel.grandTotal},headerText:"",rowText:"",action:"Delete",rowLink:function(cartitem){shoppingCartListViewModel.removeitem(cartitem);}}
	 ],
	pageSize: 6
	});


$(function(){

ko.applyBindings(shoppingCartListViewModel);
shoppingCartListViewModel.getcart();
ko.applyBindings(partyViewModel);
ko.applyBindings(orderViewModel);
orderViewModel.getOrderfromLocalStorage();



});
