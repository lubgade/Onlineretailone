<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/master.css" />
<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.16.custom.css" />
<script type="text/javascript"
	src="script/jquery-1.6.min.js"></script>
<script type="text/javascript"
	src='script/jquery-ui-1.8.16.custom.min.js'> </script>
	<script type='text/javascript' src='script/jquery.tmpl.js'></script>
<script type='text/javascript' src='script/knockout-1.3.0.latest.js'></script>
<script type='text/javascript' src='script/jquery.validate.min.js'></script>
<script type='text/javascript' src='script/knockout.jqueryupdate.js'></script>
<script type='text/javascript' src='script/profile.js'></script>

<script type='text/javascript' src='script/json2.js'></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create a new Profile</title>
</head>
<body>
<div id="Profile" >
<ul>
<li><a href="#ProfilePage1">Profile Page </a>
</li>
<li><a href="#ProfilePage2">Addresses, Phones and Email </a>
</li>
</ul>
<div id="ProfilePage1" >
<form>
<fieldset>
<label>Profile Name:</label><input data-bind=""  type="text"/>
<label>First Name:</label><input data-bind="" type="text"/>
<label>Last Name:</label><input data-bind="" type="text"/>
</fieldset>

<fieldset>
<Legend>Address:</Legend>
<label>Street Address Line 1:</label><input data-bind="" type="text"/>
<label>Street Address Line 2:</label><input data-bind="" type="text"/>
<label>City</label><input data-bind="" type="text"/>
<label>State</label><input data-bind="" type="text" />
<label>Country</label><input type="text" />
</fieldset>

<fieldset>
<label>Primary Phone:</label><input type="number"  data-bind="" type="text"/>
<label>Primary Email:</label><input type="email" data-bind="" type="text"/>
</fieldset>
</form>
</div><!--  Profile Page 1 -->
<div id="ProfilePage2" >
<form>
<div id="Addresses"> 
<div class="left" data-bind="foreach:profile.addresses" ><a  href="#" data-bind="addressLine1"></a></div>
</div> 
<div class="right" id="Address">
<fieldset>
<Legend>Address:</Legend>
<label>Street Address Line 1:</label><input data-bind="" type="text"/>
<label>Street Address Line 2:</label><input data-bind="" type="text"/>
<label>City</label><input data-bind="" type="text"/>
<label>State</label><input data-bind="" type="text" />
<label>Country</label><input type="text" />
</fieldset>
</div>
<div id="phones" data-bind="foeach:profile.phones"><input data-bind="value:profileViewModel.pToggle" type="checkbox"  id="phone" /><label  data-bind="text:$data" for="phone"></label>><input  type="number" data-bind="attri:{'id':$data},value=$data, visible:profileViewModel.pToggle"/></div>
<div id="emailAddress"  data-bind="foeach:profile.emailAddresses"><input type="checkbox"  data-bind="value:profileViewModel.eToggle" id="emailAddress" /> <label  data-bind="text:$data" for="emailAddress"><input  type="email" data-bind="attri:{'id':$data},value=$data,visible:profileViewModel.eToggle"/> </div>
</form>

</div><!--  end of Profile Page 2 -->
</div><!--  End profile Tab -->

</body>
</html>