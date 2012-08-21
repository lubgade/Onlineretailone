<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
function <%=entity.getClass().getName()%> (){ 
	<#list enity.elements  as element>
	 this.${element} = "";
	 </#list>
}	

var ${entity.class.getName()}ViewModel = {
	${entity.class.getName()}s : ko.observableArray([new  ${entity.class.getName()}()]),
	current${entity.name}:ko.observable( new ${entity.class.getName()}),
	query:ko.observable(),
	

}