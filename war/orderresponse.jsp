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
<script type='text/javascript' src='script/knockout-1.3.0.latest.js'></script>
<script type='text/javascript' src='script/jquery.validate.min.js'></script>
<script type='text/javascript' src='script/knockout.productGrid.js'></script>
<script type='text/javascript' src='script/knockout.simpleGrid.js'></script>
<script type='text/javascript' src='script/knockout.jqueryupdate.js'></script>

<script type='text/javascript' src='script/json2.js'></script>
<script type="text/javascript" src="script/jquery-barcode-2.0.2.min.js"></script>  

<script type='text/javascript' src='script/master.js'></script>
<script type='text/javascript' src='script/category.js'></script>
<script type='text/javascript' src='script/product.js'></script>

<script type='text/javascript' src='script/menu.js'></script>
<script type="text/javascript" src='script/storemap.js'> </script>
<script type='text/javascript' src='script/cities.js'></script>

</head> 
<body>
 	
	<div id="top">
		<div class="maincontent" id="topmaincontent">
		<div id="rightlinks">
		<a id="contact" href="#">Contact Us</a>
		<span> | </span>
		<a id="home" href="/masterindex.jsp">Home</a>
		<span> | </span>
		<a id="account" href="#">Account</a>
		<span> | </span>
		<a id="login" href="#">Login</a>
		</div> <!--end of rightlinks-->
			
						
				</div> <!-- topmainContent -->
				<div class="wrap"></div>	
	</div> <!--end of top -->
	<div class="wrap"></div>
	
	
	<div id="responsecont" class="maincontent">
		<span style="font-size:15px;"><b>Your order has been successfully placed and is being processed..</b>Thank you for placing your order</span>
	    <h3>Your Order Summary</h3>
	    <p data-bind="with:orderViewModel.confirmedorder()">
	    	Order Channel : <span data-bind="text:orderNumber">
	    </p>
	    <div style="border:1px solid;height:10em">
	    	<div class="left">
	      <span data-bind="text:orderViewModel.confirmedorder().orderEmailAddress"></span>
	    <address data-bind="with:orderViewModel.confirmedorder()">	  
	    		    <span data-bind="text:orderShippingAddress.addressline1"></span> </br>
	    		    <span data-bind="text:orderShippingAddress.addressline2"></span> </br>
	    		    <span data-bind="text:orderShippingAddress.city"></span> </br>
	    		    <span data-bind="text:orderShippingAddress.state"></span> </br>
	    		    <span data-bind="text:orderShippingAddress.zip"></span> </br>
	    		    <span data-bind="text:orderShippingAddress.country"></span> </br>
	    		    <span data-bind="text:orderShippingAddress.phonenumber"></span> </br>
	    </address> 
	    </div>
	    <div class="right">
	    	Order Delivery Time: <span data-bind="text:orderViewModel.confirmedorder().selectedTmg"></span> </br>
	    	Payment Type: <span data-bind="text:orderViewModel.confirmedorder().orderPaymentType"></span>
	    </div>	
	    <div  id="barcodeTarget" class="right" ></div> 
	    	    		  
          </div>
    <div>      
	    <table width="100%" border="1">
	    	<thead>
	    		<tr>
	    			<th>Item Name</th>
	    			<th>Qty</th>
	    			
	    			<th>Discount</th>
	    			<th>You Pay</th>
	    		</tr>
	    	</thead>
	    		 <tbody data-bind="template:{name:'orderLineTemplate', foreach:orderViewModel.confirmedorder().orderLineItems}">
	    		
	    	</tbody> 
			 <tfoot align="right" >
			 	
	    		<tr>
	    			<td></td>
	    			<td></td>
	    			<td>Total Amount:</td><td data-bind="text:orderViewModel.confirmedorder().netAmount">
	    			</td>

	    			
	    		</tr>
	    		<tr>
	    			<td></td>
	    			<td></td>
	    			
	    			<td>Total Discount:</td><td data-bind="text:orderViewModel.confirmedorder().orderTotalDiscount">
	    			</td>
	    		</tr>
	    		<tr>
	    			<td></td>
	    			<td></td>
	    			
	    			<td>Shipping Charges:</td><td data-bind="text:orderViewModel.confirmedorder().shippingCharges">
	    			</td>
	    		</tr>
	    		<tr>
	    			<td></td>
	    			<td></td>
	    			
	    			<td>Net Amount:</td><td data-bind="text:orderViewModel.confirmedorder().orderTotalAmount"></td>
	    		</tr>
	    	</tfoot>

	  		</table>  
	  		</div>
			<script type="text/html" id="orderLineTemplate">
				<tr>
	    			<td data-bind="text:productName"> </td>
	    			<td data-bind="text:orderedQuantity"> </td>
	    			<td data-bind="text:adjustments"></td>
	    			<td data-bind="text:orderLineItemSubTotal"></td>
   
	    		</tr>
				
			</script>
		
			</div>
		
	
	<footer>
	<div id="bottom" >
	<div id="bottomcontent"><footer class="left ft_size ">©  2012 HappyShopping.com</footer>
	<div class="right"><a href="/masterindex.html" class="left ft_size">Home</a>
	<span>|</span></div></div>	
</div> <!--end of bottom how are tou-->
</footer>
</body>





</html>