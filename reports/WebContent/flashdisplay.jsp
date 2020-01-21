<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<% 
  	String title =  "FIS Flash M-Q-Y Chart Report Page"; 
	ArrayList<String> list = new ArrayList<String>();
	/*
	ArrayList<String> tokens = new ArrayList<String>();
	String formUrl =  (String) session.getAttribute("formUrl");
	
	String filePath = "C:\\Java_Dev\\props\\headers\\olRent.txt";
	 ArrayList<String> headerArr = readHeader(filePath);
 
 
	list = (ArrayList<String>) session.getAttribute("strArr");
	*/
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title><%=title%></title>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="includes/js/tableFilter.js"></script>

<style><%@include file="includes/css/header.css"%></style>
 
    <link rel="stylesheet" href="includes/css/calendar.css" /> 
<style><%@include file="includes/css/table.css"%></style>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="includes/js/tableFilter.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="includes/js/chartjs/Chart.js"></script>
<script type="text/javascript" src="includes/js/chartjs/Chart.min.js"></script>
 
<script type="text/javascript" src="includes/js/chartjs/getappdataflash.js"></script>
<script type="text/javascript" src="includes/js/chartjs/getappdataflashyear.js"></script>
<style type="text/css">
 

#chart-containerFlash {
		width: 800px;
		height: auto;
	}
 
</style>
<!-- 

<style><%@include file="includes/css/table.css"%></style>

 

 -->



<!-- ******************************************************************************************************************************************************** -->
<style>

</style>
<!-- ******************************************************************************************************************************************************** -->
<script>

$(function() {

  // call the tablesorter plugin
  $("table").tablesorter({
    theme: 'blue',
    // initialize zebra striping of the table
    widgets: ["zebra"],
    // change the default striping class names
    // updated in v2.1 to use widgetOptions.zebra = ["even", "odd"]
    // widgetZebra: { css: [ "normal-row", "alt-row" ] } still works
    widgetOptions : {
      zebra : [ "normal-row", "alt-row" ]
    }
  });

});	
			
    </script>
    
  
</head>
<!-- ********************************************************************************************************************************************************* -->

<body>

<div style="padding-left:20px">
<%@include  file="includes/header.html" %>

<h3><%=title%></h3>

<%!/*****************************************************************************************************************************************************************/
//String formUrl = null;
/*************************************************************************************************************************************************************/
%>




 
<%  
/*****************************************************************************************************************************************************************/
		
%>
	
   
   
   
<div style="height: 600px; overflow: auto;">
				
<table border="2">
  <tr>
    <th class="b" >Flash M-Q Data Totals</th>  
  </tr>
  <tr>
    <td>
   		<div id="chart-containerFlash">
	 		<canvas id="mycanvasFlash" ></canvas>	
		</div>
    
    </td>
   </tr> 
   <tr> 
   <tr>
    <th class="b" >Flash Yearly Data Totals</th>  
  </tr>
   
 <td>
   		<div id="chart-containerFlashYear">
	 		<canvas id="mycanvasFlashYear" ></canvas>	
		</div>
    
    </td>

  </tr>
  
  
  
</table>
  
<BR>
 	
</div>
		

 



</body>
</html>