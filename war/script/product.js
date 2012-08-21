$(function () {
	
	$("#add").click(function (){
		productListViewModel.addprod();
	});

	$("#save").click(function () {

		$("#productForm").submit();
		
	});
	var $tabs = $('#tabscollection1').tabs();


	$("#nextPage").click(function(){
		productListViewModel.getNextPage();
	});
		
	$("#previousPage").click(function(){
		productListViewModel.getPreviousPage();
	});
	

	var cache={},lasttrx,prodcache={};
	$("#search").autocomplete({
		minLength:2,
		source: function( request, response ){
			$("#content").hide();
			$("productcontent").show();
			$("#products").hide();
			$("#searchResults").show();

		
			var term = request.term;
					if ( term in cache ) {
						response( cache[ term ] );
						productListViewModel.searchresults.removeAll();	
						if ( prodcache[term]){
							productListViewModel.searchresults(mapping.mapproducts(prodcache[term]));
							productListViewModel.searchresults.valueHasMutated();
						}
						if ( productListViewModel.searchresults().length > 0){
								productListViewModel.currentproduct(productListViewModel.searchresults()[0]);
						} 
						return;
					}
				   request.action="searchasutype";
	               lasttrx = $.getJSON( "/product", request, function( resp, status, xhr ) {
	              	if(resp)
	              	{ 
	              		data=resp.data;
	              	}
						cache[ term ] = $.map(data, function(item, i){return item.productName;});
						prodcache[term] = data;
						if ( xhr === lasttrx ) {
							var newData = $.map(data, function(item, i){return item.productName;});
							prodcache[term] = data;
							productListViewModel.searchresults(mapping.mapproducts(data));	
							productListViewModel.searchresults.valueHasMutated();
							if ( productListViewModel.searchresults().length > 0){
							productListViewModel.currentproduct(productListViewModel.searchresults()[0]);
							} 
							response( newData );
						}
						
		           });
	          }
	});






	$("#save").button({icons:{primary:"ui-icon-disk"},text:false});

	
			
	$("#productForm").validate({	
		
		submitHandler: function() {
			alert("saving");
				productListViewModel.saveprod();
				
		},
		errorLabelContainer: "#messageBox",
		wrapper: "li",
		errorElement: "em",
		rules:{
			 itemName:{
				 required:true, 
				 minlength:2
			 },
			 qty:{
				 required:true,
				 number:true
			 }
				
			
		}
	});
	
		
});

var mapping = {
		mapproducts : function(data){
			var products  = $.map(data, function(item, i){ 
			proddata = new product();
			proddata.id = item.id;
			proddata.productName  = item.productName;
			proddata.prodDesc = item.prodDesc;
			proddata.taxType = item.taxType;
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
			proddata.manufactDate = item.manufactDate;
			proddata.expiryDate = item.expiryDate;
			proddata.categoryMemberShips(item.categoryMemberShips);
			return proddata;
			});
			return products;
		}
};
function product() {
	var self =this;
	this.id=0,
	this.productName="Product Name";
    this.prodDesc="";
    this.taxType="";
    this.brandName="";
    this.manufactName="";
    this.mrp=0.01;
    this.UOM="";
    this.cost=0.01;
    this.sellPrice=0.01;
    this.smallImgUrl="";
    this.mdmImgUrl="";
    this.lrgImgUrl="";
    this.prodImgs=ko.observableArray([{img:""}]);
    this.manufactDate ="";
    this.expiryDate = "";
    this.categoryMemberShips = ko.observableArray([{categoryId:0}]);
    this.percentageSaving =  function(mrp,sellPrice){
    	var discount =  ((mrp -sellPrice)/mrp)*100	
    	return Math.round( discount.toFixed(2)) +"%";
    };
    this.addimages = function() {
    	this.prodImgs.push({img:""});
    }.bind(this);
    this.deleteimage = function(img){
      	 ko.utils.arrayRemoveItem(this.prodImgs, img);
      	 this.prodImgs.valueHasMutated();
    }.bind(this);
    this.producturl = function(url) {
    	var producturl = ""
    	if ( url){
    	 producturl =	"css/images/"+url;
    	}
    	return producturl;
    };
    this.currency ="Rs.";
   

};

var productListViewModel = {
		
		self:this,
		products:ko.observableArray([new product()]),
		currentproduct:ko.observable(new product()),
		query:ko.observable(),
		PAGE_NUMBER:ko.observable(0),
		pageNumbers:ko.observableArray(),
		next_page_exists:ko.observable(false),
		page:ko.observable("NEXT"),
		currentcategoryid: ko.observable(),
		searchresults: ko.observableArray(),
		getProductPage: function(catId){
			if ( catId){
			this.currentcategoryid( catId);
			}
			var mydata = ko.toJS({PAGE:this.page,PAGE_NUMBER:this.PAGE_NUMBER(),categoryId:this.currentcategoryid(),action:"getProductPage"});
			$.ajax({
        		url:"/product",      	
	    	dataType: "json",
            data: mydata,
            type: "GET",
            success: function(resp) {
            	if(resp){
        			data=resp.data;
        			productListViewModel.next_page_exists(resp.next_page_exists);
        		}
	        	if(data.length > 0){
	    			productListViewModel.products.removeAll();
	    			productListViewModel.products.valueHasMutated();
	    			productListViewModel.products(mapping.mapproducts(data));
					productListViewModel.products.valueHasMutated();
					if ( productListViewModel.products().length > 0){
					productListViewModel.currentproduct(productListViewModel.products()[0]);
					}
	        	}
	        },         
			error:function(jqxhr, e, errorText){
					alert(errorText);
		  			$("#messagebox").html("error : "+ errorText);
		  		}
	        });
			
		},
		getNextPage:function(catId,refreshSearch){
			if (catId){
				this.currentcategoryid(catId);
			}
			if ( refreshSearch ){
				this.PAGE_NUMBER(0);
				this.pageNumbers.removeAll();
			}
				
			this.page ="NEXT";
				
			this.PAGE_NUMBER(this.PAGE_NUMBER()+1);
			if (this.pageNumbers().indexOf(this.PAGE_NUMBER()) == -1){
				this.pageNumbers().push(this.PAGE_NUMBER());
				this.pageNumbers.valueHasMutated();
			}
			this.getProductPage(this.currentcategoryid());
		},
		getPreviousPage:function(){
			productListViewModel.page ="PREVIOUS";
			productListViewModel.PAGE_NUMBER(productListViewModel.PAGE_NUMBER()-1);
			productListViewModel.getProductPage(this.currentcategoryid());

		},
		getPage:function(i){
			productListViewModel.page ="PREVIOUS";
			productListViewModel.PAGE_NUMBER(i);
			productListViewModel.getProductPage(this.currentcategoryid());
		},
		search:function(){
        	var mydata = ko.toJS({query:this.query,action:"search"});
        	$.ajax({
        		url:"/product",      	
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
    			productListViewModel.products.removeAll();
    			productListViewModel.products.valueHasMutated();
    			for (var i=0;i<data.length;i++){
    				proddata = new product();
    				proddata.id=data[i].id;
    				proddata.productName=data[i].productName;
    				proddata.taxType=data[i].taxType;
    				proddata.prodDesc=data[i].prodDesc;
    				proddata.brandName=data[i].brandName;
    				proddata.manufactName=data[i].manufactName;
    				proddata.UOM=data[i].UOM;
    				proddata.mrp=data[i].mrp;
    				proddata.cost=data[i].cost;
    				proddata.sellPrice=data[i].sellPrice;
    				proddata.smallImgUrl=data[i].smallImgUrl;
    				proddata.mdmImgUrl=data[i].mdmImgUrl;
    				proddata.lrgImgUrl=data[i].lrgImgUrl;
    				proddata.prodImgs(data[i].prodImgs);
    				proddata.manufactDate = data[i].manufactDate;
    				proddata.expiryDate = data[i].expiryDate;
    				proddata.categoryMemberShips(data[i].categoryMemberShips);
				
    				productListViewModel.products().push( proddata );
    				if(i==0){
    					productListViewModel.currentproduct(proddata);
    				}
    				
    			}
    			
    			productListViewModel.products.valueHasMutated();

    		}
        },         
		
		error:function(jqxhr, e, errorText){
	  			$("#messagebox").html("error : "+ errorText);
	  		}
        });
		
        },
        setCurrentProd : function(elem){
        	this.currentproduct(this.products()[elem]);
        },  
        deleteProd: function(product){  	
       	 ko.utils.arrayRemoveItem(this.products, product);
            this.products.valueHasMutated();
            var mydata = product.id;
            $.ajax({
            	url:"/product",
                data: "id="+mydata+"&action=delete",
                type: "post",
                success: function(result) { 
            		alert(result);
                    }
            });
 
       },
       addprod: function(){
    	   this.products.push(new product());
    		this.products.valueHasMutated();
    	},
    	saveprod:function() {
    		productListViewModel.currentproduct().mrp = parseFloat(productListViewModel.currentproduct().mrp);
    		   var mydata = ko.toJSON({product:productListViewModel.currentproduct,action:"PUT"});
    		   alert(mydata);
    		    $.ajax({
    		    	url:"/product",
    		        data: "JSON="+mydata+"&action=PUT",
    		        type: "post",
    		        success: function(result) { 
    		    		alert(result);
    		            }
    		    });
    		}  
};

productListViewModel.jqueryuilogic = function(elements){
	for (var i=0;i < elements.length;i++) {
		$(elements[i]).find('[class *= \'numberformat\']').blur(function(){
			   $(this).parseNumber({format:"####.00", locale:"in"});
			   $(this).formatNumber({format:"####.00", locale:"in"});
		});
		$(elements[i]).find('[class *= \'addimagesbutton\']').button({icons:{primary:"ui-icon-circle-plus"},text:false});
		$(elements[i]).find('[id *= \'manufactDate\']').datepicker({
			showOn: "both",
			buttonImage: "css/images/calendar.gif",
			buttonImageOnly: true,
			
		});
		
		$(elements[i]).find('[class *= \'cart\']').button({icons:{primary:"ui-icon-cart"},text:false});
		$(elements[i]).find('[id *= \'manufactDate\']').datepicker({
			showOn: "both",
			buttonImage: "css/images/calendar.gif",
			buttonImageOnly: true,
			
		});
		
		$(elements[i]).find('[id *= \'expiryDate\']').datepicker({
			showOn: "both",
			buttonImage: "css/images/calendar.gif",
			buttonImageOnly: true,
			
		});
	

	}
}

productListViewModel.gridViewModel = new ko.productGrid.viewModel({
	data: productListViewModel.searchresults,
	columns: [
	  	    { headerText: "img", action:"",rowText: function(product){return product.producturl(product.smallImgUrl)},rowLink:"" },
	    { headerText: "", action:"",rowText: "productName",rowLink:"" },
	    { headerText: "", action:"",rowText: "prodDesc",rowLink:"" },
	    { headerText: "", action:"",rowText: "sellPrice",rowLink:"" },
	    {headerText:"",rowText:"",action:"Buy Now",rowLink:function(product){shoppingCartListViewModel.additems(product)}},
	 ],
	pageSize: 6
	});

$(function(){
ko.applyBindings(productListViewModel);
//productListViewModel.search();
productListViewModel.getNextPage();

/*productListViewModel.page("NEXT");
productListViewModel.PAGE_NUMBER(1);
productListViewModel.getProductPage(); */
});