/**
 * 
 */

$(function(){
$("#profile").tabs();	
	
	
});
var profile = function(){
	this.firstName= ko.observable();
	this.lastName= ko.observable();
	this.partyName = ko.obsevable();
	this.partyAddress =  ko.obsevable();
	this.addresses = ko.observableArray();
	this.emailAddresses = ko.observableArray();
	this.phones = ko.observableArray();
	this.userIds = ko.observableArray();
	this.userId =  ko.observable();
	this.addressLine1 = ko.observable();
	this.addressLine2 = ko.observable();
	this.city = ko.observable();
	this.state = ko.observable();
	this.country = ko.observable();
	
}

var profileVieModel = {
	profile = ko.observable( new profile),
	pToggle = ko.observable(false),
	tToggle = ko.observable(false),
		
		
}

$(function(){
ko.applyBindings(profileVieModel);
});