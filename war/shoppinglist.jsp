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
<script type='text/javascript' >
$(function() {
$("#button1").click(function () {

	shoppingListViewModel.addList();
	$("TextFieldList").show();
	
});

$("TextFieldList").hide();

$("#button2").click(function () {

	shoppingListViewModel.shoppingList().additem();
	
	
});

$("#button3").click(function () {

	$("#shoppingListForm").submit();
	
});

$("#search").autocomplete({
	source:function(request,response){
		var lists =shoppingListViewModel.lists(); 
		var  filteredList = $.ui.autocomplete.filter(
				$.map(shoppingListViewModel.lists(), function( item ) {
					return { 
							id : item.id,
							listName : item.listName,
							items: item.items,
							lable: item.id,
							value: item.listName,
					}
				}), request.term  );
		if(filteredList.length==0)
		{
			for (var i=0;i<shoppingListViewModel.lists().length;i++){
				shoppingListViewModel.lists()[i].visible(false);
			}
			shoppingListViewModel.lists.valueHasMutated();
		
		}
		if (request.term == "*"){ 
			for (var i=0;i<shoppingListViewModel.lists().length;i++){
				shoppingListViewModel.lists()[i].visible(true);
			}
			shoppingListViewModel.lists.valueHasMutated();
		}
		for (var i=0;i<shoppingListViewModel.lists().length;i++){
			for( var j=0; j< filteredList.length;j++){
				if (filteredList[j].id == shoppingListViewModel.lists()[i].id ){
					shoppingListViewModel.lists()[i].visible(true);
					break;
				}
				else {
					shoppingListViewModel.lists()[i].visible(false);
				}
			}
		}
		shoppingListViewModel.lists.valueHasMutated();
		//response(filteredList );
		
		
	}
		});

/*
 * $.map(shoppingListViewModel.lists(), function( item ) {
		var text = item.listName;
		if ( this.value && ( !request.term || matcher.test(text) ) )
		return {
			label: text.replace(
					new RegExp(
							"(?![^&;]+;)(?!<[^<>]*)(" +
							$.ui.autocomplete.escapeRegex(request.term) +
							")(?![^<>]*>)(?![^&;]+;)", "gi"
						), "$1" ),
			value: item.listName
		};
	})
 */

$("#button1").button({icons:{primary:"ui-icon-plus"},text:false});
$("#button2").button({icons:{primary:"ui-icon-plus"},text:false});
$("#button3").button({icons:{primary:"ui-icon-disk"},text:false});
$("#button4").button({icons:{primary:"ui-icon-disk"},text:false});

} );
</script>

</head>
<style>
	#div_1{
	width: 15em;
	float:left;
	border-style:groove;
	padding-right: 4px;
	margin-right: 10px;
	border-color : #8C2300;
	height:40em;	

	}
	.itemstable{
	border:solid 2px #8C2300;
	
	
	}
	.itemstable tr th {
	border :solid 2px #8C2300;
	font-size:0.75em;
	font-weight:600;
	background-color:#FFDD66
	
	}
	.itemstable tr td {	
		border :solid 2px #8C2300;
		background-color:#FFDD66
	
	}
	#messageBox{
	list-style:none;
	margin-right:2em;
	margin-top:1em;
	}
	#div_2{
	width: 25em;
	float:left;
	border-style:groove;
	background-color: #FFDD66;	
	border-color : #8C2300;
	height:40em;
	}	
	ul{
	list-style: none;
	padding:0px;
	
	}
	li{
		border-style:outset;
		text-align: left;
		background-color:#FFDD66; 
	}
	.hlink1{
	color:#FFFFFF
	}
	.hlink1:hover{
		color:#FFFFFF
	}
	li:hover{
	background-color:#663333	
	}
	#div_3{
	
	width:100%;
	background-color: #663333;
	border-style:none
	}
	
	.listname{
	font-family:"Franklin Gothic Heavy";
	font-size:larger;
	font-style:italic;
	font-weight:bold;
	text-align:justify;
	color: white; 
	 
		
	}
	#div_4{
		margin-top: 5px;
		border-style:groove;
		margin-bottom:0px;
		margin-left:6px;
		margin-right:2px;  
		padding:0px;
	}
	.currentlist{
	 	background-color:#285c00;	
	 
	}
	#button1{
	vertical-align:middle;
	}
	.search{
	width:16em;
	margin-left:1em;
	margin-right:auto;
	background:  url(css/images/search.png) top right no-repeat;
	float:left;
	}
	body{
	background-color : #FFFDD4;

	}
	.add{
	height:30px;
	width:30px
		}	
		
	</style>
	</head>
<body>

	<div  id="div_1">
	<div id="div_5"><div><input id="search"  class="search"  placeholder="Search" type="search"/></div>
	<div><Button id="button1" ></Button></div>
	</div>
	
	<div id="div_4">
	<ul data-bind='template:{name:"listTemplate",data:shoppingListViewModel}'>
	</ul>
	</div>
	</div><!--end of div_1-->	
	<div  id="div_2">
	<div id="div_3">
	
		<center><span data-bind="text:shoppingListViewModel.shoppingList().listName" class="listname"></span></center>
	
	<div style="float:right"> <Button id = "button3" ></Button>	<Button id="button2" ></Button>
	</div>	
		
	</div>
	
	<form id="shoppingListForm"  action="#" >
<table class="itemstable">
	<tr><th>Item Name</th><th>Quantity</th><th>UOM</th><th>Action</th>
	</tr>
	<tbody data-bind='template: { name: "shoppingTemplate", data:shoppingList}'></tbody>
	</table>
	</form>		
	</div>
		
	
	<script type="text/x-jquery-tmpl"  id="shoppingTemplate">
	{{each(i,item) items }}
	<tr><td><input name="itemName" data-bind="value:itemName" type="text" id="itemName" /> 
	</td>
	<td>
	<input name="qty" id="qty"  data-bind="value:qty"  />
	</td>
	<td>
	<select data-bind="options:availableUOMs, value:uom" ></select>
	</td>
	    <td>
		<Button  href="#" data-bind="click:function(){  shoppingListViewModel.shoppingList().deleteitem(item);}" id = "button4">Delete</Button>  
		</td> 
	
	</tr>
	{{/each}}
	</script>
	
	<script type="text/html" id="listTemplate">			
	{{each(j,list) lists() }}	
	<li  data-bind="css:{currentlist:list==shoppingListViewModel.shoppingList()}, visible:visible, click:function(){setCurrentList(j);}">
	
	<a href="#"  class="hlink1" data-bind="text:listName?	listName:'Edit', visible:!list.makeeditable, click:function() {setCurrentList(j);toggleEdit();}"></a>
	<input data-bind="value:listName, valueUpdate:'afterkeydown', visible:list.makeeditable, click:function(){setCurrentList(j);}" type="text" id="TextFieldList${j}" /> 
	<button  data-bind="click:function(){setCurrentList(j);toggleEdit();},visible:makeeditable">!</button>
   	<button  data-bind="click:function(){deleteList(list)},visible:makeeditable">x</button>

	</li>
    
	{{/each}}
	</script>
	
	

<script type='text/javascript' src='script/shoppingList.js'></script>
<div id="messageBox" class="messageBox"  >  </div>

</body>
</html>