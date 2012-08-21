function ${entity.class.getName()} (){ 
	<#list enity.elements  as element>
	 this.${element} = "";
	 </#list>
}	

var ${entity.class.getName()}ViewModel = {
	${entity.class.getName()}s : ko.observableArray([new  ${entity.class.getName()}()]),
	current${entity.name}:ko.observable( new ${entity.class.getName()}),
	query:ko.observable(),
	

}