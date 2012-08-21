<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="com.google.appengine.api.users.User" %> 
<%@ page import ="com.google.appengine.api.users.UserService" %>
<%@ page import ="com.google.appengine.api.users.UserServiceFactory" %>

<%
	String email="";
	UserService userService = UserServiceFactory.getUserService();         
	User user = userService.getCurrentUser();     
	if ( user == null){ 
								response.sendRedirect(userService.createLoginURL(request.getRequestURI()));         
    }
    else{
     email=user.getEmail();
     }
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/master.css" />
<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.16.custom.css" />
<script type="text/javascript"
	src="script/jquery-1.6.min.js"></script>
<script type="text/javascript"
	src='script/jquery-ui-1.8.16.custom.min.js'> </script>
	<script type='text/javascript' src='script/jquery.tmpl.js'></script>
<script type='text/javascript' src='script/knockout-2.0.0.js'></script>
<script type='text/javascript' src='script/jquery.validate.min.js'></script>
<script type='text/javascript' src='script/knockout.productGrid.js'></script>
<script type='text/javascript' src='script/knockout.simpleGrid.js'></script>
<script type='text/javascript' src='script/knockout.jqueryupdate.js'></script>

<script type='text/javascript' src='script/json2.js'></script>

<script type='text/javascript' src='script/master.js'></script>
<script type='text/javascript' src='script/category.js'></script>
<script type='text/javascript' src='script/product.js'></script>
<script type='text/javascript' src='script/cities.js'></script>

</head>

<body>

	
	<div id="top">
		
	<div id="maincontent">
	<div id="contenttop">		
	<div id="logo" class="ui-widget-header">Logo</div>
	<div id="top_info_left">
	<h4 class="disp">Secure Payment</h4>
	</div> <!--end of top_info_left-->
	<div id="top_info_rt" class="disp">
	<h4 class="disp"><%=email%></h4>
	<span>|</span>
	<a id="logout" class="ft_size" href="/masterindex.html">Logout</a>		
	</div> <!--end of top_info_rt-->
	</div> <!--end of contenttop-->
	<div class="wrap"></div>
<div id="maindiv" class="main" >	
<div id="left_div" class="left">
	
	<div id="tabs" class="left">						
	
			<ul>							
				<li id="elogin"><a href="#tabs-1" class="ft_size">Email Login</a></li>
			<!--	<div id="nav_bar" class="left"> </div>	-->							
				<li id="shipAddress"><a href="#tabs-2" class="ft_size next1">Shipping Address</a></li>				
			<!--	<div id="nav_bar" class="left"></div> -->
				<li id="osummary"><a href="#tabs-3" class="ft_size">Order Summary</a></li>
			<!--	<div id="nav_bar" class="left"></div> -->
				<li id="payops"><a href="#tabs-4" class="ft_size">Payment Options</a></li>
				
			</ul>
			
	   <div id="tabs-1">
			  </br>  </br> </br>   
		 	<h4 class="disp ">Your Email Address:</h4>
		 	<span class="ft_size"><%=email%> (Not You <a id="logout" class="ft_size" href="/masterindex.html">Logout</a>)</span> </br>
		 	<span class="ft_size marg_left">Your order details will be sent to this email address</span> </br>	
		 	<Button id="continue" class="next1">Continue</Button>		 	
		</div> <!--end of tabs-1-->
		    
	<div id="tabs-2">
      	
	<div id="l_div" class="left">		 
		<span  class="ft_size"><b>Select from previous shipping Address</b>&nbsp;&nbsp;  <b>OR</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
	    </br>
	       
		<div  class="ft_size" id="addr">
		
				<ul  class="productInfoList ft_size" data-bind="template:{name:'addressTemplate',foreach:partyViewModel.currentparty().partyAddresses}">
		</ul>
		<script id="addressTemplate" type="text/html">
		<li  class="recipient ui-widget-content left">
        <address class="ft_size"> 
		<span data-bind="text:name"></span> </br>
		<span data-bind="text:addressline1"></span> </br>
 		<span data-bind="text:addressline2"></span> </br>
 		<span data-bind="text:city"></span> </br>
 		<span data-bind="text:state"></span> </br>
 		<span data-bind="text:zip"></span> </br>
 		<span data-bind="text:country"></span> </br>
 		<span data-bind="text:phonenumber"></span> </br>
        </address>
		<a href="#" class="" data-bind="click:function(){ partyViewModel.setShippingAddress($data)}" id="clktoselect" class="ft_size">Click To select</a>	

		</li>
		</script>
		
 	
		 	
		<br class="clear"/>	
		</div>  <!-- end of addr -->
	<!--  	<div id="mborder" class="left"> </div> -->
   </div> <!--end of l_div--> 
 
    
    	
	<div id="r_div" class="left">
	<span class="ft_size" ><b>Enter a new Shipping Address</b></span></br>	</br>
	 <div id="r_div_cont"> 
	 <form id="addressForm">
	 	
	    <fieldset>
	    <legend>Shipping Address</legend>
	    <Label  style="width:150px"  class="left ft_size">Recipents Name:<em>*</em></Label>
	   	<input data-bind="value:partyViewModel.address().name,valueUpdate:'afterkeydown'" class="right" style="width:200px" type="text" name="name" />  </br></br>
	   
	    <label style="width:150px" class="left ft_size">Address Line 1:<em>*</em></label>
	    <input required="required" class="required right ft_size" type="text" data-bind="value:partyViewModel.address().addressline1" style="width:200px"  name="addressline1" />  </br></br>
			    <label style="width:150px" class="left ft_size">Address Line 2:<em>*</em></label>
	    <input class="required right ft_size"  type="text" data-bind="value:partyViewModel.address().addressline2" style="width:200px" name="addressline2" />  </br></br>
		
	
	<!--    <div data-bind="with:partyViewModel.selectedcity">
	     <label class="left ft_size">Locality<em>*</em></label>	
	   <select class="right" id="localitySelect" data-bind="options:storelocations,optionsText:'name',optionsValue:'name',value:partyViewModel.address.addressline2">
		</select> 
	   </div> </br></br>-->
	    <label style="width:150px" class="left ft_size">City:<em>*</em></label>
	    <select class="right" id="citySelect" data-bind="options:citiesDB.cities(),optionsText:'name',value:partyViewModel.selectedcity"  style="width:200px" name="city" ></select>  </br></br>
		
	   <div data-bind="with:partyViewModel.selectedcity">
	    <label style="width:150px" class="left ft_size">State:<em>*</em></label>
	    <input class="ui-state-disabled ft_size" data-bind="value:state" style="width:200px" class="right"/>
		 </div>
		 </br></br>
	    <label style="width:150px" class="left ft_size">Country:</label><span class="right"><b >India</b><span style="font-size:9pt;font-type:italic">(service available only in India)</span> </span></br>	</br>	    
	    <label style="width:150px" class="left ft_size">Pincode:<em>*</em></label>
	    <input class="right" data-bind="value:partyViewModel.address().zip" style="width:200px;" type="text" name="pcode" size="40px"/> </br></br>	
	    <Label style="width:150px;" class="left ft_size">Phone Number:<em>*</em></label>
	    <input class="right" data-bind="value:partyViewModel.address().phonenumber" style="width:200px;" type="text" name="phone" size="40px"/> </br> </br></br>	
	  	</fieldset>
	    </form>
	    <Button id="savetocont" class="ft_size">Save & Continue</Button> 
	    <div data-bind="text:partyViewModel.currentparty().firstName()" ></div>
	 </div> <!--end of r_div_con-->
   </div> <!--end of r_div-->
 	</div> <!--end of tabs-2-->
 	
 	
 	<div id="tabs-3">
 	<div class="portlet-content" data-bind="simpleGrid: shoppingCartListViewModel.gridViewModel"> 	  
 	</div>
 	<h5>Select Date and Time Of Delivery:</h5> 
 	<h6>Pick A Date:</h6>
 	<select name="selDate" >
 	 
 	</select>
 	<h6>Pick A Time:</h6>
 	 <select name="selTime" data-bind="options: orderViewModel.availableTimings,value:orderViewModel.selectedtmg " >
 	  	</select>
 	<div>
 	</div>
 	<Button id="cont" class="ft_size right">Continue</Button>
 	</div> <!--end of tabs-3--> 
 	
 	<div id="tabs-4"> 	
 		
 		<h5 class="ft_size">Choose your mode of payment</h5>
 		
 	 <div id="subtabs">
 			
 		<ul>
		<li><a href="#subtabs-1">Cash-on-Delivery</a></li>
		 <li><a href="#subtabs-2">Net Banking</a></li>
		<!--<li><a href="#CC-tab">Credit Card</a></li>
        <li><a href="#DC-tab">Debit Card</a></li>    -->  
		</ul> 
		<div  id="subtabs-1" class="left">
		
		<form id="CODform" >	
		   	 <script type="text/javascript" src="http://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
          <script type="text/javascript">
          function showRecaptcha() {
            Recaptcha.create("6LdFP9ASAAAAANWGILg-yTxFLx2ur4LZDJHRyrdW", 'captchadiv', {
                tabindex: 1,
                theme: "clean"
            });
          }
         $(function(){
        	  showRecaptcha(); 
  
          });
          </script>
         
		<p class="ft_size"><label>Pay Using Cash-On-Delivery</label></p>
		</br>		
		<h5 class="ft_size left">Amount Payable On Delivery:</h5>
		<span data-bind="text:shoppingCartListViewModel.grandTotal" class="ft_size left"></span>
		</br>
		</br>
		<p class="ft_size">Cash on Delivery Order Verification:</p> </br>
		<p class="ft_size">Please type the characters you see below</p> </br>		
	<!--  	<h5 class="ft_size left">Type the text here:</h5> 
		<input type="text" class="left" name="ovtxt" size="10em"/>-->
		<div id="captchadiv"></div>  
		
		<input style="display:none" class="submit" type="submit" value="Submit"/> 
		</br></br> </br></br>
    <Button tabindex="2" id="placeord" class= "right ft_size" type="submit" >Place Order</Button>
		</form>
		<div id="messageBox"></div>		
		
		<div class="wrap"></div>
		 		
	    </div> <!--end of subtabs-1 -->
	    
		<div id="subtabs-2" class="left">
			
		<p class="ft_size">Pay Using Net Banking</p>
		</br>
		<p class="ft_size class="left">Choose your bank:</p>
	    </br>
	    <Button tabindex="0" id="placeord" class= "right ft_size" type="submit" >Place Order</Button>
	    <div class="wrap"></div>
				
		</div> <!--end of subtabs-2-->
		
     </div>	<!--end of subtabs-->

 	</div> <!--end of tabs-4--> 	
	</div> <!--end of tabs-->	
	</div> <!--end of left_div-->

	<div id="right_div" class="rt_div">
	    
		<h4>&nbsp;&nbsp;Order Summary</h4>
		 <hr/>
		<span class="left ft_size">Items:</span>
		<span data-bind="text:shoppingCartListViewModel.totalQuantity" class="ft_size left"></span>
		 </br>
		<span class="left ft_size">Sub Total:</span>
		<span data-bind="text:shoppingCartListViewModel.grandTotal" class="ft_size"></span>
		 </br>
		<span class="left ft_size">Shipping:</span> 
		<span data-bind="text:shoppingCartListViewModel.shippingAmt" class="ft_size"></span>
		</br>
		<span class="left ft_size">Amount Payable:</span><span data-bind="text:shoppingCartListViewModel.amountPayable"></span> </br>
	</div>	 <!--end of right_div-->

    <div id="right_div_2" class="rt_div">
	    <h4>Shipping Address</h4>
	    <a id="changeshipadd" href="#" >Change</a>
        <hr/>
        <div class="recipient" data-bind="with:partyViewModel.shippingAddress()">
	     		<address>
	     		<span data-bind="text:name"></span> </br>
	     		<span data-bind="text:addressline1"></span> </br>
	            <span data-bind="text:addressline2"></span> </br>
 		        <span data-bind="text:city"></span> </br>
 		        <span data-bind="text:state"></span> </br>
 		        <span data-bind="text:zip"></span> </br>
 		        <span data-bind="text:country"></span> </br>
                <span data-bind="text:phonenumber"></span> </br>
 		
        </address>
	    </div>
    
    </div>
	

<div class="wrap"></div>

<div class="left marg_left">
	<img id="lock" src="css/images/lock_closed.png"/>
	<span class="ft_size">Safe And Secure Shopping Guarantee</span>
</div> <!--end of lock-->
</div> <!--end of maindiv-->



<div class="wrap"></div>

</div>	<!-- end of maincontent -->
	</div><!--end of top-->

<div class="wrap"></div>

<div id="bottom" >
	
	<div id="bottomcontent"><footer class="left ft_size ">©  2012 HappyShopping.com</footer>
	<div class="right"><a href="/masterindex.html" class="left ft_size">Home</a>
	<span>|</span></div></div>	
</div> <!--end of bottom-->

<!--  <div id="orderresp" class="show_hide">
   <h2>Order Summary</h2>
</div> -->


</body>
</html>