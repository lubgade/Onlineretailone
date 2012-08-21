<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head id="script">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Retail Operations - Super Category Manager</title>
<link type="text/css" href="css/jquery-ui-1.8.16.custom.css"
	rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="css/master.css" />
<script type="text/javascript"
	src="script/jquery-1.6.min.js"></script>
<script type="text/javascript"
	src='script/jquery-ui-1.8.16.custom.min.js'> </script>
<script type='text/javascript' src='script/jquery.tmpl.js'></script>
<script type='text/javascript' src='script/knockout-1.3.0.latest.js'></script>
<script type='text/javascript' src='script/jquery.validate.min.js'></script>
<script type='text/javascript' src='script/knockout.simpleGrid.js'></script>
<script type='text/javascript' src='script/json2.js'></script>

  <script src="http://maps.google.com/maps/api/js?sensor=false" type="text/javascript"></script> 
  <script type='text/javascript' src='script/gmaps.js'></script>
	
<style>
ul {list-style: none;}
</style>
<script type="text/javascript">
$(function() {

$("#Menu").buttonset();

$("#store").click(function(){
 		$("#content").load("/storemaster.html");	
 
});
$("#category").click(function(){
	 $("#content").load("/categoryMaster.html");	
	});
$("#supercategory").click(function(){
	 $("#content").load("/supercategory.html");	
	});
$("#product").click(function(){
	 $("#content").load("/productMaster.html");	
	});
});
</script>
</head>
<body>
<div class="demo">
<form>
<div id="Menu">
<input class="radio" type="radio" name="radio" id="category"/><label for="category">Category</label>
<input class="radio" type="radio" name="radio" id="supercategory"/><label for="supercategory">Super Category</label>
<input class="radio" type="radio" name="radio" id="store"/><label for="store">Store</label>
<input class="radio" type="radio" name="radio" id="product"/><label for="product">Product</label>
</div>
</form>
</div>
<div id="content">

</div>

</body>
</html>