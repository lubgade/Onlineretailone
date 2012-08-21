<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/master.css" />
<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.16.custom.css" />
<link href="css/main-53.css"" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="script/jquery-1.6.min.js"></script>
<script type="text/javascript"
	src='script/jquery-ui-1.8.16.custom.min.js'> </script>
 <style>
	#feedback { font-size: 1.4em; }
	#selectable .ui-selecting { background: #FECA40; }
	#selectable .ui-selected { background: #F39814; color: white; }
	#selectable { list-style-type: none; margin: 0; padding: 0; }
	#selectable li { margin: 3px; padding: 1px; float: left; width: 200px; height: 200px; font-size: 1em; text-align: center; }
	</style>

<script type="text/javascript">
$.fx.speeds._default = 1000;
$(function() {
	
	$("button").button();
	$( "#selectable" ).selectable();
	$( "#dialog" ).dialog({
		autoOpen: false,
		show: "blind",
		hide: "explode",
		modal:"true",
		width:"400px"
	});

	$( "#opener" ).click(function() {
		$( "#dialog" ).dialog( "open" );
		return false;
	});
	
	
		
		function log( message ) {
			$( "<div/>" ).text( message ).prependTo( "#log" );
			$( "#log" ).scrollTop( 0 );
		}

		$( "#tabs" ).tabs({event: "mouseover"});
		$( ".tabs-bottom .ui-tabs-nav, .tabs-bottom .ui-tabs-nav > *" )
		.removeClass( "ui-corner-all ui-corner-top" )
		.addClass( "ui-corner-bottom" );
		
		$( "#searchinput" ).autocomplete({
			source: function( request, response ) {
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
				log( ui.item ?
					"Selected: " + ui.item.label :
					"Nothing selected, input was " + this.value);
			},
			open: function() {
				$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
			},
			close: function() {
				$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
			}
		});
	});
</script>
<style>

	
	

.ui-autocomplete-loading { background: white url('http://jqueryui.com/demos/autocomplete/images/ui-anim_basic_16x16.gif') right center no-repeat; }
	
	
	
</style>

<title>Online Groceries</title>


</head>
<body>
<div id="topheader" class="topheader"><a href="" class="anchor">Sign-In/Register</a>| 
<a id="opener" href="" class="anchor" >Shopping cart</a>| <a  href="" class="anchor">Contact us</a>   
</div>

<div id="wrap" class="wrap">
<div id ="header" class="header">
<div id="logo" class="logo" > <img class="logoimg" src="" /> </div>
<div id="searchbar" class="searchbar">
<input  type="search" id="searchinput"   />
 <button>Search</button>
</div>
<div id="flinks" class="flinks"> 
<a href="#">Some Links</a><a href="#">Some Links</a><a href="#">Some Links</a>
<a href="#">Some Links</a>
 </div>
</div>
<div class="wrap"></div>
<div id ="menu" class="menu">
	<ul>
			<li><a href="#"><img
					src="css/images/home_icon.png" alt="Home" />
			</a>
			</li>
			<li class=""><a title="Groceries" href="#">Groceries</a>
				<ul class="leftbutton ">
					<li class="two_column ">
						<div class="firstcol">
							<ol>
								<li><a title="RICE, ATTA, LENTILS & DALS" href="#">RICE, ATTA, LENTILS & DALS</a>
								</li>
								<li><a title="EDIBLE OILS" href="#">EDIBLE OILS</a>
								</li>
								<li><a href="#">COFFEE, TEA & COCOA</a>
								<ol>
									<li><a title="Coffee" href="#">Coffee</a>
									</li>
									<li><a title="Dairy Whitener" href="#">Dairy Whitener</a>
									</li>
									<li><a title="Tea" href="#">Tea</a>
									</li>
									<li><a title="Drinking Choclate" href="#">Drinking Choclate</a>
									</li>
								</ol>
					</li>
					</ol>
	</div>
	<div class="thirdcol">
		<ol>
			<li class="head">Popular Brands</li>
			<li><a Title="Shakti Bhog Atta" href="#">Shakti Bhog Atta</a>
			</li>
			<li><a Title="Kohinoor Basmati Rice" href="#">Kohinoor Basmati Rice</a>
			</li>
			<li><a Title="Nescafe" href="#">Nescafe</a>
			</li>
			<li><a Title="Fun Foods" href="#">Fun Foods</a>
			</li>
			<li><a Title="Kelloggs" href="#">Kelloggs</a>
			</li>
		</ol>
	</div>
	</li>
	</ul>
	</li>
	<li class=""><a title="Biscuits" href="#">Biscuits</a>
		<ul class="leftbutton ">
			<li class="two_column ">
				<div class="firstcol">
					<ol>
						<li><a title="Butter and Spreads" href="#">Butter and Spreads</a>
						</li>
						<li><a title="Jams" href="#">Jams</a>
						</li>
						<li><a href="#">Oils</a>
						<ol>
							<li><a href="#">Peanut Oil</a>
							</li>
							<li><a href="#">Olive Oil</a>
							</li>
							<li><a href="#">Pure Ghee</a>
							</li>
						</ol>
					</li>
					</ol>
			</div>
			<div class="thirdcol">
				<ol>
					<li class="head">Popular Brands</li>
					<li><a href="#">Amul</a>
					</li>
					<li><a href="#">Nestle</a>
					</li>
				</ol>
			</div>
	</li>
	</ul>
	</li>
	<li class="new"><a title="Cameras" href="#">SNACKS</a>
		<ul class="leftbutton ">
			<li class="three_column ">
				<div class="firstcol">
					<ol>
						<li><a title="Mobiles" href="#">Noodles</a>
						</li>
						<li><a title="Mobiles" href="#">Chips</a>
						</li>
						<li><a href="#">Sweets</a>
						 <img src="css/images/pnav_bullet.png" border="0" height="5" width="9" alt="" />
						<ol>
							<li><a href="#">Ras Gullas</a>
							</li>
							<li><a href="#">Malia Pedha</a>
							</li>
							<li><a href="#">Kaju Katli</a>
							</li>
						</ol>
			</li>
			</ol>
			</div>
			<div class="secondcol">
				<ol>
					<li><a title="" href="#">Mixture</a>
					</li>
					<li><a title="Mobiles" href="#">Sev</a>
					</li>
					<li><a href="#"> Haldiram</a>
					
					<ol>
											<li><a href="#">Samosa</a>
						</li>
						<li><a href="#">Bhel</a>
						</li>
						<li><a href="#">Pani Puri</a>
						</li>
					</ol>
	</li>
	</ol>
	</div>
	<div class="thirdcol">
		<ol>
			<li class="head">Popular Brands</li>
			<li><a href="#">Haldiram</a>
			</li>
			<li><a href="#">Maggi</a>
			</li>
		</ol>
	</div>
	</li>
	</ul>
	</li>


	</ul>
</div>
<div class="marginbot55"></div>

<div class="logo">
<div class="rightalign" id ="box">
<ol id="selectable">
	<li class="ui-state-default"><img size="width:30px,height:30px" src="css/images/SearchIcon.jpg"><div>Product1</div>
				<Button>Add to Cart</Button></li>
	<li class="ui-state-default"><img src="css/images/SearchIcon.jpg"><div>Product2</div>
				<Button>Add to Cart</Button></li>
	<li class="ui-state-default"><img src="css/images/SearchIcon.jpg"><div>Product3</div>
				<Button>Add to Cart</Button></li>
	<li class="ui-state-default"><img src="css/images/SearchIcon.jpg"><div>Product4</div>
				<Button>Add to Cart</Button></li>
	<li class="ui-state-default"><img src="css/images/SearchIcon.jpg"><div>Product5</div>
				<Button>Add to Cart</Button></li>
	<li class="ui-state-default"><img src="css/images/SearchIcon.jpg"><div>Product6</div>
				<Button>Add to Cart</Button></li>
	<li class="ui-state-default"><img src="css/images/SearchIcon.jpg"><div>Product7</div>
				<Button>Add to Cart</Button></li>
	<li class="ui-state-default"><img src="css/images/SearchIcon.jpg"><div>Product8</div>
				<Button>Add to Cart</Button></li>
	<li class="ui-state-default"><img src="css/images/SearchIcon.jpg"><div>Product9</div>
				<Button>Add to Cart</Button></li>
	<li class="ui-state-default"><img src="css/images/SearchIcon.jpg"><div>Product10</div>
				<Button>Add to Cart</Button></li>
	<li class="ui-state-default"><img src="css/images/SearchIcon.jpg"><div>Product11</div>
				<Button>Add to Cart</Button></li>
	<li class="ui-state-default"><img src="css/images/SearchIcon.jpg"><div>Product12</div>
				<Button>Add to Cart</Button></li>
</ol>
</div>
</div>
<div class="logo" id ="midbox"  >
<div id="tabs" class="tabs-bottom" >
	<ul>
		<li><a href="#tabs-1">Cooktop</a></li>
		<li><a href="#tabs-2">Micromax</a></li>
		<li><a href="#tabs-3">LCD TV</a></li>
	</ul>
	<div id="tabs-1">
	<img src="" />
	</div>
	<div id="tabs-2">
		<img src="" />
	</div>
	
	<div id="tabs-3">
		<img src="" />
	</div>
</div>
</div>
<div id ="box" class="box">

</div>

<div id ="main" class="main">

<div class="ui-widget" style="margin-top:2em; font-family:sans-serif;">
	Result:
	<div id="log" style="height: 200px; width: 300px; overflow: auto;" class="ui-widget-content"></div>
</div>

</div>
<br>

<div id ="footer" class="footer">
Footer
</div>
<div id ="footer2" class="footer">
Footer
</div>
</div>


<div id="dialog" title="Shopping Cart" class="dialog">
<table cellspacing="1">
<thead>
<th>Item </th>
<th>Description</th>
<th>Quantity</th>
<th>Price</th>
</thead>
<%for (int  i=1; i <=10; i++){ %>
<tr><td><%=i%> Phone </td><td><%=i%> Phone </td><td><%=i%> Phone </td><td><%=i%> Phone </td></tr>
<%} %>
</table>
<button>Continue Shopping</button>
<button>Pay now</button>

</div>

</body>
</html>