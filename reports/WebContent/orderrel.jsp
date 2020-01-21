<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="com.olympus.olyutil.Olyutil"%>
<% 
  	String title =  "Olympus FIS Order Released/Booking Report"; 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><%=title%></title>
<!--  <link href="includes/appstyle.css" rel="stylesheet" type="text/css" /> 

<style><%@include file="includes/css/reports.css"%></style>
<style><%@include file="includes/css/table.css"%></style>
-->
<style><%@include file="includes/css/header.css"%></style>
<style><%@include file="includes/css/menu.css"%></style>
 <link rel="stylesheet" href="includes/css/calendar.css" />
    <script type="text/javascript" src="includes/scripts/pureJSCalendar.js"></script>


 
</head>
<body>

    
 <%@include  file="includes/header.html" %>

 <%
	ArrayList<String> strArr = new ArrayList<String>();
 	ArrayList<String> strArrMod = new ArrayList<String>();
	strArr = (ArrayList<String>) session.getAttribute("strArr");
	strArrMod = (ArrayList<String>) session.getAttribute("strArrMod");
 	/*
	for (String str : strArrMod) {
		System.out.println(str);	
	}
 */
	
%>

<div style="padding-left:20px">
  <h3><%=title%></h3>
</div>

<BR>

<h5>This page will provide an on-demand Order Released/Booking Report.<BR> 
<BR> 
<font color="red"> Note: Some queries take more time to run. Please be patient.</font></h5>




<BR>
</body>
</html>