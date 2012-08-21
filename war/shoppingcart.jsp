<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="com.google.appengine.api.users.User" %> 
<%@ page import ="com.google.appengine.api.users.UserService" %>
<%@ page import ="com.google.appengine.api.users.UserServiceFactory" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	UserService userService = UserServiceFactory.getUserService();         
	User user = userService.getCurrentUser();     
	if ( user == null){ 
								response.sendRedirect(userService.createLoginURL(request.getRequestURI()));         
    }
%>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Shopping List</title>
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

<script type='text/javascript'>
	
	
</script>

<head>
	
	
</head>

<body>
	
	<div>
		<form>
		<table>
			<tr><th>Item Id</th><th>Item Name</th><th>Qty</th><th>Price</th><th>Subtotal</th>   
		  </tr>
		  <tbody data-bind='template:{name:"cartTemplate",data:}'  >
		  	
		  </tbody>
		</table>
		</form>
	</div>
	
</body>