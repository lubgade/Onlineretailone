<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%String selectedStore =  (String)session.getAttribute("SELECTEDSTORE");%>

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
<script src="http://maps.google.com/maps/api/js?sensor=false" 
type="text/javascript"></script> 

	<script type='text/javascript' src='script/jquery.tmpl.js'></script>
<script type='text/javascript' src='script/knockout-1.3.0.latest.js'></script>
<script type='text/javascript' src='script/jquery.validate.min.js'></script>
<script type='text/javascript' src='script/knockout.productGrid.js'></script>
<script type='text/javascript' src='script/knockout.simpleGrid.js'></script>
<script type='text/javascript' src='script/knockout.jqueryupdate.js'></script>

<script type='text/javascript' src='script/json2.js'></script>
<script type='text/javascript' src='script/master.js'></script>

<script type='text/javascript' src='script/category.js'></script>
<script type='text/javascript' src='script/product.js'></script>

<script type='text/javascript' src='script/menu.js'></script>
<script type="text/javascript" src='script/storemap.js'> </script>
<script type='text/javascript' src='script/cities.js'></script>

 
<style>
#map{
width:800px;
height:500px;
border:1px solid;
margin-left:20px;
}
.results{
width:15.5em;
border:1px solid blue;
}
.mainbox{
padding-left:10px;
padding-top:10px;
border:1px solid;
}
.ui-widget-content li{
list-style:none;
margin:0;
}
.resultList{
margin-left:0;
}
.searchBox{
border:1px solid blue;
padding-top:10px;
}

.fieldLabel{
display:inline-block;
width: 100px;
}
.addressline1,.citySelect,.localitySelect,.state{
width:220px;

}
</style>



</head>

<body >
	<%   if ( selectedStore == null){
	 %>
			
	<div id="searchStoresnearBy">
		<div id="searchtabs" class="left">						
			<ul>
				<li><a href="#main"> Search Store</a></li>
				<li><a href="#maptab">Store on Map</a></li>

			</ul>

		<div id="main" class="mainbox">

<div class="logo searchBox"> 
<fieldset>
<div>
<span class="fieldLabel ui-state-highlight">Address Line 1:</span>&nbsp;<span></span><input class="addressline1" data-bind="value:storeListViewModel.addressline1" type="text" id="addressline1" size="50"/>
</div>
<div>
<span class="fieldLabel ui-state-highlight">City:</span>
<select class="citySelect" id="citySelect" data-bind="options:citiesDB.cities(),optionsText:'name',value:storeListViewModel.city">
</select>
</div>

<div data-bind="with:storeListViewModel.city">
<span class="fieldLabel ui-state-highlight">Locality:</span>
<select class="localitySelect" id="localitySelect" data-bind="options:storelocations,optionsText:'name',optionsValue:'name',value:storeListViewModel.addressline2">
</select>
</div>
 <div data-bind="with:storeListViewModel.city">
<span class="fieldLabel ui-state-highlight">State:</span><span>&nbsp;</span><input class="state" class="ui-state-disabled" id="state" type="text" disabled="disabled" data-bind="value:state"/>
</div>
<div>
<Button id="searchByAddressFields">Search</Button>
</div>
</fieldset>
<fieldset>
Enter address to search stores near by:<br>
<input type="text" id="addressInput" size="50"/> 

<select id="radiusSelect"> 
<option value="2" selected>2km</option> 
<option value="5">5km</option> 
<option value="10">10km</option> 
</select> 
<Button id="searchButton"  >Search</Button> 
</fieldset>


</div> <!--  end of div SearchBox s-->
<div class="wrap"></div>
<div class="logo results">
<h3 class="ui-widget-header"> Search Results</h3>
<select id="locationSelect" style="width:100%;visibility:hidden">
</select>

<ul class="ui-widget-content resultList" id="sortable1" data-bind="template:{name:'storeListTemplate',foreach:storeListViewModel.stores()}">
	</ul>
	 

<script type="text/html" id="storeListTemplate">
<li class="ui-state-highlight">
<a  href="#" data-bind="click:function(){ storeListViewModel.higlightMarker(index,categoryId,id,storeName,$data);}, text:storeName"></a>
</li>
</script>
</div> 


<div class="wrap"></div>

</div> 
<div id="maptab">
<div  id="map" class="map logo"></div> 
</div>
		
		
		</div><!-- searchtabs -->

	</div> <!-- searchnearbystores div-->
	
<%}%>	
	
	<div  id="HomePage">
	
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
		<div id="search_div">							
		    <div id="searchinput_div">
			<input  id="search" placeholder="Search for Products" type="text" class="searchinput"/>
							<Button id="sbutton" ></Button>
			  </div>  <!--end of searchinput_div-->
			  
			<div id="cart">
				<img id="shoppingcart_icon" src="css/images/shop_cart.png"/>			
				<a id="shopcart" href="#">Cart(<span data-bind="text:shoppingCartListViewModel.totalQuantity"></span>)</a>				
		    </div> <!--end of cart-->   
			
						
		<div id="menu">		
			<div class="menu">
							<div class="menutop">See All Categories </div>

				 <div class="menuarrow">
					 <Button class="downbutton"></Button>
				</div>
			<div class="menulet" >		
				<ul class="browsecategorylist" data-bind="foreach:categoryListViewModel.catids">
				<div class="wrap"> </div>
				<li class="browsecategorytext">
				<div  class="menulink" > <a data-bind="text:$data.categoryName,click: function(data,event){categoryListViewModel.getCategoryProducts(id);$('#searchResults').hide();$('#content').hide();$('#productcontent').show();$('#products').show(); }" href="#"></a> </div>
				<div  class="browsecategoryblock" >
				<ul class="browsecategorylist" data-bind="foreach:childCategories">
				<li class="browsecategorytext">
					<a data-bind="text:categoryName"></a>	
				</li>
				</ul>
				</div>
				<div class="browsecategoryblockPart2">
				<ul class="browsecategorylist" data-bind="foreach:childCategories">
				<li class="browsecategorytext">
					<a data-bind="text:categoryName"></a>	
				</li>
				</ul>
				</div>
				</li>
			</ul>
				<div class="wrap"></div>
			</div> <!-- div class=menulet -->
			</div> <!-- div class=menu -->
	
		<div class="wrap"></div>
		</div> <!--end of menu-->	
	</div> <!--end ofsearch_div-->

		</div> <!-- topmainContent -->
				<div class="wrap"></div>	
		</div> <!--end of top-->
	<div class="wrap"></div>	
	

	
<div id="content">
	
</div> <!--  end of content div -->	


<div id="productcontent" class="maincontent">	
<!-- 
<ul>
	<li>
	<a href="products">By Category </a>
	</li>
	<li>
	<a href="#searchResults">Search Results </a>
	</li>
	</ul>  -->
<div id="searchResults" class="searchResults"  data-bind="productGrid: productListViewModel.gridViewModel"> </div>




<div id="products">
	<ul class="ui-widget-content">
		<li>&nbsp;
								<h3 style="text-align:center" data-bind="text:(storeListViewModel.selectedStoreObj())?storeListViewModel.selectedStoreObj().storeName:'Default Store'"></h3> 
		
			</li>
		<li>&nbsp;</li>
		<li>&nbsp;Store:<span data-bind="text:storeListViewModel.selectedStore()"></span></li>
	</ul>
			<div class="wrap"></div>
	
	
	
	<div class="pagination">
			<ul class="ui-widget-content">
			<li data-bind="visible:(productListViewModel.PAGE_NUMBER() > 1)">
				<a href="#" id="previousPage" >Previous</a>
			</li>
			<li>	
			<ul data-bind="foreach:productListViewModel.pageNumbers()">
				<li class="grey" data-bind="text:$data,visible:(productListViewModel.PAGE_NUMBER() == $data)">
				</li>
				<li data-bind="visible:(productListViewModel.PAGE_NUMBER() != $data), click:function(){ productListViewModel.getPage($data)}">
					<a class="mine-button ui-state-default" href="#" data-bind="text:$data"></a>		
				</li>
			</ul>		
			</li>
			<li data-bind="visible:productListViewModel.next_page_exists()">
				<a href="#"  id="nextPage">Next</a>
			</li>
			</ul>
			</div>

	
	
<ul id="productList" class="ui-widget-content" data-bind="foreach: {data:productListViewModel.products, afterRender:productListViewModel.jqueryuilogic}">
<li  data-bind="attr:{'id':id}" class="ui-widget-content">
<div>
 <img class="prodsmallimg"   data-bind="attr:{'src': producturl(smallImgUrl),'alt':productName }" /> 
<div class="productPara">
<a href="#" class="productName" data-bind="text:productName,click:function(){shoppingCartListViewModel.showproduct($data);}"></a>
<p class="productDesc" data-bind="text:prodDesc"></p>
</div>
<div class="sellPrice"><span data-bind="text:currency"></span> <span data-bind="text:sellPrice"></span></div>
<div class="mrp" data-bind="visible:(mrp>sellPrice)"> <span data-bind="text:currency"></span><span data-bind="text:mrp"></span></div>
<div data-bind="visible: (mrp >sellPrice)"><p>You are saving <span data-bind="text:percentageSaving(mrp,sellPrice)"></span></p></div>
<Button class="addtocartbutton" data-bind="click:function(){shoppingCartListViewModel.additems($data);},jqueryupdate:{name:'button',arguments:{icons:{primary:'ui-icon-plus'},text:true}}" >Add</Button>

</div>
</li>
</ul>
	<div class="wrap"></div>
</div> <!-- end of products div -->

<div class="pagination">
			<ul class="ui-widget-content">
			<li data-bind="visible:(productListViewModel.PAGE_NUMBER() > 1)">
				<a href="#" id="previousPage" >Previous</a>
			</li>
			<li>	
			<ul data-bind="foreach:productListViewModel.pageNumbers()">
				<li class="grey" data-bind="text:$data,visible:(productListViewModel.PAGE_NUMBER() == $data)">
				</li>
				<li data-bind="visible:(productListViewModel.PAGE_NUMBER() != $data), click:function(){ productListViewModel.getPage($data)}">
					<a class="mine-button ui-state-default" href="#" data-bind="text:$data"></a>		
				</li>
			</ul>		
			</li>
			<li data-bind="visible:productListViewModel.next_page_exists()">
				<a href="#"  id="nextPage">Next</a>
			</li>
			</ul>
			</div>


<div  id="product_div">
	<div id="productcomplete">
	<div id="withcurrentproduct" data-bind="with:productListViewModel.currentproduct">
		<div class="left productimages">
		<div data-bind="if:(prodImgs().length > 0)" class="mainimg">
			<img class="large" data-bind="attr:{src:prodImgs()[0].img, alt:prodImgs()[0].img}" >
		</div>
		
		<div  class="thumbnails" data-bind="foreach:prodImgs" >
			<img class="thumbnail" data-bind="attr:{'src': img,'alt':img }" >
		</div>
		</div> <!-- images div -->
		
		<div class="left productInfo">
	    <span class="productName" data-bind="text:productName"></span> </br>
		<ul class="left productInfoList">
		<li><ul><li>Product Description:</li><li data-bind="text:prodDesc"></li></ul></li> 
		<li><ul><li>Brand Name:</li><li data-bind="text:brandName"></li> </ul></li> 
		<li><ul><li>Manufacturer:</li><li data-bind="text:manufactName"></li></ul></li>
		<li><ul><li>Unit of Measure:</li> <li data-bind="text:UOM"></li></ul></li>
		
		<li><ul><li>Price:</li><li class="mrp" data-bind="visible:(mrp >sellPrice)">
			<span data-bind="text:currency"></span><span  data-bind="text:mrp"></span>
		</li> 
		<li class="sellPrice">
			<span data-bind="text:currency"></span><span data-bind="text:sellPrice"></span></li></ul></li>
		<li><ul data-bind="visible:(mrp >sellPrice)"><li>Discount:</li><li data-bind="text:currency"></li><li  data-bind="text:(mrp-sellPrice)"> </li></ul> </li>
		<li><ul data-bind="visible:(mrp >sellPrice)"><li>Percentage Saving:</li><li id="circle"  data-bind="text:percentageSaving(mrp,sellPrice)">	</li></ul></li>
		
		</ul>
		
		</div><!-- end div productinfo -->
	</div>	<!-- End with current Product -->	
	<div class="left addtocartButtonarea">
		<div><strong>Free Shipping if total order amount is Rs. 200 or above.
Add Rs. 30 otherwise.</strong></div>
				<Button class="addtocart" >Buy This Now</Button><div style="display:inline"><span>with an option to pay
</span><p style="display:inline; font-weight:900">Cash on Delivery</p></div>
	</div>
	</div><!-- end complete product -->
	<div class="wrap"></div>	
</div> <!--end of product_div-->

</div><!--  end of product Content -->

<div id="bottom" >
	
	<div id="bottomcontent"><footer class="left ft_size ">�  2012 HappyShopping.com</footer>
	<div class="right"><a href="/masterindex.jsp" class="left ft_size">Home</a>
	<span>|</span></div></div>	
</div> <!--end of bottom-->
	
 <div id="contactdialog" title="Contact Info">
 	<address>
 		Sobha Carnation,</br>
 		Sarjapura Outer Ring Road,</br>
 		Bellandur,</br>
 		Bangalore - 560103</br>
 		Phone : 080 - 25747688
 	</address>
 </div>	 <!--end of contactdialog-->
 
 <div id="logindialog" title = "Login" class="logindialog"> 
 	<form>
 	<div>	
 	<span class="left">Email:     </span>	
 	<input class="right" type="text" name="email" size="10em"/>  </br> </br>
 	<span class="left">Password:</span>
 	<input class="right" type="password" name="password" size="10em"/> </br> </br>
 	<button id="login" class="logindialog wrap">Login</button>
 	<a href="#">Forgot your password?</a> </br></br>
 	<span style="font:Lucida; font-size:larger; word-spacing:20px">OR</span> </br> 
 	<span>New User</span>
 	<button id="signup" class="logindialog">Sign Up</button>
 	</div>
 	<div class="left" style="width:200px" >
 		
 	</div>
 	</form>
 </div> <!--end of logindialog-->
   
 <div id="signupdialog" title="New User" class="logindialog">
 	<form>
 	<div class="left">	
 	<span class="left">Email</span> 
 	<input class="right" type="text" name="email" size="10em"/>  </br> </br>
 	<span class="left">Password:</span>
 	<input class="right" type="password" name="password" size="10em"/> </br> </br>
 	<span class="left">Repeat Password:</span>
 	<input class="right" type="password" name="password" size="10em"/> </br> </br>
 	<button id="signupnow" class="logindialog wrap">Sign Up Now</button>
 	</div>
 	<div class="left" style="width:200px" >
 		
 	</div>
 	</form>
 </div> <!--end of signupdialog-->
 
 
 <div id="orderresponse" >
 	
 	
 </div>  <!--end of orderresponse-->
 
 <script type="text/html" id="pagination">

	<a data-bind="click:function(){ productListViewModel.getPreviousPage(); },visible:PAGE_NUMBER() > 1" href="#">Previous</a>
	{{each(i) pageNumbers}}
	{{if PAGE_NUMBER() == i+1}}
		${i+1} 
	{{else}}
	<a data-bind="click:function(){ productListViewModel.getPage(i+1); }" href="#">${i+1}</a>
	{{/if}}
	{{/each}} 
	<a data-bind="click:function(){ productListViewModel.getNextPage(storeListViewModel.selectedCategory(),false); },visible:next_page_exists" href="#">Next</a>

	
</script>
	


 <div id="shopcartdialog" title="Cart" class="shocartdialog">
 	<div id="checklogin"></div>
 	<form>
 		<div  class="portlet-content" data-bind="simpleGrid: shoppingCartListViewModel.gridViewModel">
 		<!-- <table>
 			<thead>
 			<tr>
 			<th>Item Name</th>
 			<th>Price</th>
 			<th>Quantity</th>
 			<th>Sub-Total</th>
 			 <th></th>
 			 </tr>
 			</thead>
 			<tbody data-bind="foreach:{data:shoppingCartListViewModel.cartitems()}">
 			<tr>
 			<td><span data-bind="text:productName"></span></td>
 			<td><span data-bind="text:productprice"></span></td>
			<td><input class="number" type="number" data-bind="value:quantity"/></td>
 			<td><span data-bind="text:subtotal"></span></td>
 			<td><Button data-bind="jqueryupdate:{name:'button',arguments:{icons:{primary:'ui-icon-closethick'},text:false}}"></Button></td>
 			</tr>
 			</tbody>
  		</table>
  		<div class="gtotal">
  		<div class="right">
 			<div class="left ">Grand Total:</div><div class="left" data-bind="text:shoppingCartListViewModel.grandTotal"></div>
 		</div>
 		<div class="wrap"></div>
 		</div>
--> 		
 	</form>
 	
 </div> <!--end of shopcartdialog-->
 
</div><!-- Home page -->
 </body>
 </html>