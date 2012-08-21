$(function () {
	
	/* $('body').layout({ applyDefaultStyles: true,
		closable:false, resizable:false, slidable:false,
		spacing_open:0
		}); */
	// Tabs
	var $tabs = $('#tabscollection1').tabs();
	
		

	$("#supercategoryform").validate({	
		submitHandler: function() {
				supercategoryListViewModel.save();
				$('#tabscollection1').tabs();
		},
		errorLabelContainer: "#messageBox",
		wrapper: "li",
		errorElement: "em",
		rules: {supercategoryName: { required: true, minlength: 5},supercategoryDesc: { 
				    	required:true,
				    	minlength:7
				  	}
				  	
				  }
			  	});
	
		
		
	$('#add').click(function(){
		$tabs.tabs('select',1);
		supercategoryListViewModel.supercategory(new supercategory());
		return false;
		
	});
	$("button").button();


	
});


function supercategory (id, supercategoryName,supercategoryDesc,categories) {
		this.id = id;
		this.supercategoryName =supercategoryName;
		this.supercategoryDesc = supercategoryDesc;
	 	this.categories = ko.observableArray(categories);
}
function category (catid, categoryName , categoryDesc) {
	this.catid = catid;
	this.categoryName =categoryName;
	this.categoryDesc = categoryDesc;
	this.isSelected = ko.observable(false);
}
var  supercategoryListViewModel = {
	supercategories: new ko.observableArray(),
	supercategory: ko.observable( new supercategory()),
	choosenCategories:ko.observableArray(),
	availableCategories :  new ko.observableArray(),
	query:  ko.observable(),
	search: function(){
		   var mydata = ko.toJS({query:this.query,action:"search"});
		  $.ajax({
		    	url:"/supercategory",
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
	        			supercategoryListViewModel.supercategories.removeAll();
	        			supercategoryListViewModel.supercategories.valueHasMutated();
	        			for (var i=0;i<data.length;i++){
	        				supercategorydata = new supercategory(data[i].id, data[i].supercategoryName, data[i].supercategoryDesc,data[i].categories,true);
	        				this.choosenCategory(supercategorydata.categories);
	        				this.supercategories.push( supercategorydata );
	        			}
	        		}
	            },
		  		error:function(jqxhr, e, errorText){
		  			alert("error : "+ errorText);
		  		}
	        });
			
	},
	
    edit: function(supercategory){
    	 supercategoryListViewModel.supercategory(supercategory);
    	 this.choosenCategories(supercategoryListViewModel.supercategory().categories);
    	return false;
    },
   	save:function() {
   		this.supercategory().categories(this.choosenCategories);
   	   var mydata = ko.toJSON({supercategory:this.supercategory,action:"PUT"});
	   alert(mydata);
	    $.ajax({
	    	url:"/supercategory",
            data: "JSON="+mydata+"&action=PUT",
            type: "post",
            success: function(result) { 
        		alert(result);
        		supercategoryListViewModel.supercategory(new supercategory());
                }
        });
    }  
 };



function loaddata(){
	 var mydata = ko.toJS({query:"",action:"search"}); 
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
	   			supercategoryListViewModel.availableCategories.removeAll();
	   			supercategoryListViewModel.availableCategories.valueHasMutated();
	   			for (var i=0;i<data.length;i++){
	   				categorydata = new category(data[i].id, data[i].categoryName, data[i].categoryDesc);
	   				supercategoryListViewModel.availableCategories.push( categorydata );
	   				
	   			}
	   		}
	       },
	 		error:function(jqxhr, e, errorText){
	 			alert("error : "+ errorText);
	 		}
	   });
 }
 
 
supercategoryListViewModel.gridViewModel = new ko.simpleGrid.viewModel({
    data: supercategoryListViewModel.supercategories,
    columns: [{headerText:"Edit",rowText:"",action:"Edit",rowLink:function(supercategory){$('#tabscollection1').tabs('select',1); supercategoryListViewModel.edit(supercategory)}},
        { headerText: "Delete",action:"Delete", rowText:"",rowLink:function(supercategory){ 
        	 var mydata = ko.toJS({id:supercategory.id,action:"delete"});
   		  $.ajax({
   		    	url:"/supercategory",
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
        { headerText: "Super Category Desc", action:"",rowText: "supercategoryDesc",rowLink:"" },
        { headerText: "Super Category Name", action:"",rowText: "supercategoryName",rowLink:"" },

     ],
    pageSize: 10
});
 
 
ko.applyBindings(supercategoryListViewModel);
loaddata(); 
