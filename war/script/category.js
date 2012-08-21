/*
 * Author : Leena Ghanekar
 */

$(function () {
	
	/* $('body').layout({ applyDefaultStyles: true,
		closable:false, resizable:false, slidable:false,
		spacing_open:0
		}); */
	// Tabs
	
	
		
		$( ".column" ).sortable({
			connectWith: ".column"
		});

		$( ".portlet" ).addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
		.find( ".portlet-header" )
			.addClass( "ui-widget-header ui-corner-all" )
			.prepend( "<span class='ui-icon ui-icon-minusthick'></span>")
			.end()	
		.find( ".portlet-content" );
	
		$(".portlet-header .ui-icon").click(function() {
			$( this ).toggleClass( "ui-icon-minusthick" ).toggleClass( "ui-icon-plusthick" );
			$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).toggle();
			
		});
	
		
		$( ".column" ).disableSelection();

		var $tabs = $('#tabscollection1').tabs();
		
		$tabs.bind("tabsselect", function (event, ui){
			if ( ui.panel.id == "tabs-4"){
				if ( !categoryListViewModel.category().id){
					return
				}
				categoryListViewModel.getchildcategories(categoryListViewModel.category().id);

			}
			
			if ( ui.panel.id == "tabs-3") {
				if ( !categoryListViewModel.category().id){
					return
				}
				categoryListViewModel.getProductsByCategory(categoryListViewModel.category().id);

			}
		});
		
		
	$("#categoryform").validate({	
		submitHandler: function() {
				categoryListViewModel.save();
				$('#tabscollection1').tabs();
		},
		errorLabelContainer: "#messageBox",
		wrapper: "li",
		errorElement: "em",
		rules: {categoryName: { required: true, minlength: 2},categoryDesc: { 
				    	required:true,
				    	minlength:2
				  	}
				  	
				  }
			  	});
	
		
		
	$('#add').click(function(){
		$tabs.tabs('select',1);
		categoryListViewModel.category(new category());
		return false;
		
	});
		$("button").button();
	
	$("#savecategoryproducts").click(function(){
		categoryListViewModel.addproductstoCategory();
	
	});
	
	$("#savecategorycat").click(function(){
		categoryListViewModel.addCategoriestoCategory();
		
	});

	
});


function category (id,categoryName,categoryDesc) {
	this.id = id;
	this.categoryName =categoryName;
	this.categoryDesc = categoryDesc;
	/*this.subcategories =  new ko.observableArray([{subid:"", categoryName:"",categoryDesc:""}]);
	this.addCategory= function (){		
		this.subcategories().push({subid:"", categoryName:"",categoryDesc:""});
		this.subcategories.valueHasMutated();
	};
	this.deletesub = function(subcat){
        ko.utils.arrayRemoveItem(this.subcategories, subcat);
        this.subcategories.valueHasMutated();
 	};*/
	this.childCategories = new ko.observableArray([]);
	this.subCategories = new ko.observableArray([]);
	
}

var  categoryListViewModel = {
categories: new ko.observableArray(),
catids : new ko.observableArray(),
currentsubcat: ko.observableArray(),
category: ko.observable( new category()),
storecategory:ko.observable(new category()),
query:  ko.observable(),
products: ko.observableArray(),
getCategoryProducts : function(category){
/*	console.log(productListViewModel.page);*/
/*	productListViewModel.page = "NEXT";
	productListViewModel.PAGE_NUMBER(1);
	productListViewModel.pageNumbers.removeAll();
	productListViewModel.pageNumbers().push(productListViewModel.PAGE_NUMBER());
	productListViewModel.getProductPage(category);*/
	productListViewModel.getNextPage(category,true);
	
},
getProductsByCategory:function(categoryID){
	var mydata = ko.toJS({id:categoryID,action:"getproducts"});
	 
	 $.ajax({
	    	url:"/category",
	    	dataType: "json",
           data: mydata,
           type: "GET",
           success: function(resp) {
           	//alert(resp);
           	if(resp){
       			//getting the data from the response object
       			data=resp.data;
       		}
           	
       		if(data.length > 0){
       			categoryListViewModel.products.removeAll();
       			categoryListViewModel.products.valueHasMutated();
       			categoryListViewModel.products($.map(data,function(item,i){
       			  proddata = new product();
       			  proddata.id = item.id;
       			  proddata.productName = item.productName;
       			  proddata.prodDesc = item.prodDesc;
       			  proddata.brandName=item.brandName;
	    			  proddata.manufactName=item.manufactName;
	    			  proddata.UOM=item.UOM;
	    			  proddata.mrp=item.mrp;
	    			  proddata.cost=item.cost;
	    			  proddata.sellPrice=item.sellPrice;
	    			  proddata.smallImgUrl=item.smallImgUrl;
	    			  proddata.mdmImgUrl=item.mdmImgUrl;
	    			  proddata.lrgImgUrl=item.lrgImgUrl;
	    			  proddata.prodImgs(item.prodImgs);
       			  return proddata;
       			}));
       			categoryListViewModel.products.valueHasMutated();
       		}else if ( data.length == 0){
       			categoryListViewModel.products.removeAll();
       			categoryListViewModel.products.valueHasMutated();
       		} 
       		
           },
	  		error:function(jqxhr, e, errorText){
	  			alert("error : "+ errorText);
	  		}
       });

},
getchildcategories:function(categoryID){
	var mydata = ko.toJS({id:categoryID,action:"getChildCategories"});
	$.ajax({
    	url:"/category",
    	dataType: "json",
        data: mydata,
        type: "GET",
        success: function(data) {
        	//alert(resp);
        	/*if(resp){
    			//getting the data from the response object
    			data=resp.data;
    		}
        	*/
    		if(data.length > 0){
    			var categoriesList =  $.map(data,function(item,i){
    			  catdata= new category(item.id, item.categoryName,item.categoryDesc);
    			  catdata.id = item.id;
    			  catdata.categoryName = item.categoryName;
    			  catdata.categoryDesc = item.categoryDesc;
    			//  catdata.subcategories(item.subCategories);
    			  catdata.childCategories(item.childCategories);
    			  return catdata;
    			});
    			categoryListViewModel.catids(categoriesList);
    			return categoriesList;
     		}
    		else if ( data.length == 0){
    			
    		} 
    		
        },
  		error:function(jqxhr, e, errorText){
  			alert("error : "+ errorText);
  		}
    });

},
getCategory: function(categoryId){
	   var mydata = ko.toJS({id:categoryId,action:"getCategory"});
	   $.ajax({
	    	url:"/category",
	    	dataType: "json",
           data: mydata,
           type: "GET",
           success: function(resp) {
           	if(resp){
       			//getting the data from the response object
       			data=resp.data;
       		}
       		if ( data){
       			categorydata = new category(data.id,data.categoryName, data.categoryDesc);
              //  categorydata.subcategories(data.subcategories);
                categoryListViewModel.storecategory(categorydata);
                return categorydata;
       		}
           },
	  		error:function(jqxhr, e, errorText){
	  			alert("error : "+ errorText);
	  		}
       });
	   
},
search: function(){
	   var mydata = ko.toJS({query:this.query,action:"search"});
	  $.ajax({
	    	url:"/category",
	    	dataType: "json",
            data: mydata,
            type: "GET",
            success: function(resp) {
            	//alert(resp);
            	console.log(resp);
            	if(resp){
        			//getting the data from the response object
        			data=resp.data;
        		}
        		if(data.length > 0){
        			categoryListViewModel.categories.removeAll();
        			categoryListViewModel.categories.valueHasMutated();
        			for (var i=0;i<data.length;i++){
        				categorydata = new category(data[i].id,data[i].categoryName, data[i].categoryDesc);
                       // categorydata.subcategories(data[i].subCategories);
                        categoryListViewModel.categories.push( categorydata );
        				
        			}
        		}
            },
	  		error:function(jqxhr, e, errorText){
	  			alert("error : "+ errorText);
	  		}
        });
	  
	
},

 edit: function(category){
	 categoryListViewModel.category(category);
	return false;
},
addCategoriestoCategory: function(){
	var categoryids =  $.map(this.catids(),function(item,i){return item.id});
	var cateids =categoryids.toString();
	 var mydata = ko.toJS({id:this.category().id,categoriesid:cateids,action:"addcategories"});
	 $.ajax({
	    	url:"/category",
	    	dataType: "text",
	        data: mydata,
	        type: "post",
	        success: function(result) { 
	    		alert(result);
	          },
	          error:function(e){
		  			alert("error : "+ e);
		  		}	
	    });
},
addproductstoCategory:function(){
	var prodids =  $.map(this.products(),function(item,i){return item.id});	
	var ids = prodids.toString();
	 var mydata = ko.toJS({id:this.category().id,productids:ids,action:"addproducts"});
	$.ajax({
	    	url:"/category",
	    	dataType: "text",
	        data: mydata,
	        type: "post",
	        success: function(result) { 
	    		alert(result);
	          },
	        error:function(e){
		  			alert("error : "+ e);
		  		}  
	    });
},
	save:function() {
   var mydata = ko.toJSON(this.category);
   console.log(mydata);
    $.ajax({
    	url:"/category",
        data: "JSON="+mydata+"&action=PUT",
        type: "post",
        success: function(result) { 
    		alert(result);
    		categoryListViewModel.category(new category());
            },
            error:function(e){
	  			alert("error : "+ e);
	  		}  
    });
}  
};
categoryListViewModel.gridViewModel = new ko.simpleGrid.viewModel({
	data: categoryListViewModel.categories,
	columns: [{headerText:"Edit",rowText:"",action:"Edit",rowLink:function(category){$('#tabscollection1').tabs('select',1); categoryListViewModel.edit(category)}},
	    { headerText: "Delete",action:"Delete", rowText:"",rowLink:function(category){ 
	    	 var mydata = ko.toJS({id:category.id,action:"delete"});
			  $.ajax({
			    	url:"/category",
			    	dataType: "text",
		            data: mydata,
		            type: "POST",	
		            success: function(resp) {
		            alert(resp);	
		            },
			  		error:function(e){
			  			alert("error : "+ e);
			  		}
		        });
			
	    	
	    } },
	    { headerText: "Category Desc", action:"",rowText: "categoryDesc",rowLink:"" },
	    { headerText: "Category Name", action:"",rowText: "categoryName",rowLink:"" },
	    { headerText: " Sub-Categories", action:"Sub-Categories",rowText:"",rowLink:function(category){
	    	categoryListViewModel.currentsubcat(category.subcategories);
	    } }

	/*    { headerText: " Sub-Category Name", action:"",rowText: "categoryName",rowLink:"" },
	    { headerText: "Sub-Category Desc", action:"",rowText: "categoryDesc",rowLink:"" },*/


	 ],
	pageSize: 10
	});



$(function () { 
	
ko.applyBindings(categoryListViewModel);

});