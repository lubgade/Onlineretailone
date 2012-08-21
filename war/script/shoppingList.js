$(function () {
	
			
	$("#shoppingListForm").validate({	
		
		submitHandler: function() {
			alert("saving");
				shoppingListViewModel.saveList();
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

function shopList (id,listName) {
	this.id = id;
	this.listName =listName;
	
	this.items =  ko.observableArray([{id:0, itemName:"",qty:"0", uom:""}]);
	
	this.additem = function (){		
		this.items().push({id:0, itemName:"",qty:"0", uom:""});
		this.items.valueHasMutated();
	};
	this.deleteitem = function(item){
        ko.utils.arrayRemoveItem(this.items, item);
        this.items.valueHasMutated();
  	}
   this.availableUOMs=  ko.observableArray(['KG','Grams','Litre','Dozen','Piece','Pack']);
   this.makeeditable = false;
   this.visible = ko.observable(true);
}

var  shoppingListViewModel = {
		
lists: ko.observableArray([new shopList(0,"ListName")]),
shoppingList: ko.observable(new shopList(0,"ListName")),
query:  ko.observable(),
search: function(){
	   var mydata = ko.toJS({query:this.query,action:"search"});
	  $.ajax({
	    	url:"/shoppingList",
	    	dataType: "json",
            data: mydata,
            type: "GET",
            success: function(resp) {
            	//alert(resp);
            	if(resp){
        			//getting the data from the response object
        			data=resp.lists;
        		}
        		if(data.length > 0){
        			shoppingListViewModel.lists.removeAll();
        			shoppingListViewModel.lists.valueHasMutated();
        			for (var i=0;i<data.length;i++){
        				shoppingListdata = new shopList(data[i].id,data[i].listName);
        				shoppingListdata.items(data[i].items);
        				shoppingListViewModel.lists().push( shoppingListdata );
        				if(i==0)
        					{
        					shoppingListViewModel.shoppingList(shoppingListdata);
        					}
        				
        			}
        			
        			shoppingListViewModel.lists.valueHasMutated();

        		}
            },
	  		error:function(jqxhr, e, errorText){
	  			$("#messagebox").html("error : "+ errorText);
	  		}
        });
	
},
addList: function(){
	this.lists.push(new shopList(0,"ListName"));
	this.lists.valueHasMutated();
},
setCurrentList : function(elem){
	this.shoppingList(this.lists()[elem]);
},
edit: function(shoppingList){
	 shoppingListViewModel.shoppingList(shoppingList);
	return false;		
},
enableEdit:function(){
	this.shoppingList().makeeditable = true;
	this.lists.valueHasMutated();

},
disableEdit: function(){
	this.shoppingList().makeeditable = false;
	this.lists.valueHasMutated();
},
toggleEdit: function(toggle){
	 if ( this.shoppingList().makeeditable){
		 this.disableEdit();	
		 
	 }	else {
		 this.enableEdit();
		 
		 
	 }
},

deleteList: function(list){
	
	
	 ko.utils.arrayRemoveItem(this.lists, list);
     this.lists.valueHasMutated();
     var mydata = list.id;
     $.ajax({
     	url:"/shoppingList",
         data: "id="+mydata+"&action=delete",
         type: "post",
         success: function(result) { 
     		alert(result);
             }
     });
     
	
	
},
saveList:function() {
   var mydata = ko.toJSON({list:this.shoppingList,action:"PUT"});
   alert(mydata);
    $.ajax({
    	url:"/shoppingList",
        data: "JSON="+mydata+"&action=PUT",
        type: "post",
        success: function(result) { 
    		alert(result);
            }
    });
}  
};


shoppingListViewModel.gridViewModel = new ko.simpleGrid.viewModel({
data: shoppingListViewModel.lists,
columns: [{headerText:"Edit",rowText:"",action:"Edit",rowLink:function(shoppingList){shoppingListViewModel.edit(shoppingList);}},
    { headerText: "Delete",action:"Delete", rowText:"",rowLink:function(shoppingList){ 
    	 var mydata = ko.toJS({id:shoppingList.id,action:"delete"});
		  $.ajax({
		    	url:"/shoppingList",
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
    { headerText: "shoppingList Name", action:"",rowText: "listName",rowLink:"" }
 ],
pageSize: 10
});


ko.applyBindings(shoppingListViewModel);
shoppingListViewModel.search();

