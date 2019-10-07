<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.File"%>
<%@ page import="java.util.ArrayList"%>
 

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Olympus FIS Infolease Booking Report</title>

<style><%@include file="includes/css/reports.css"%></style>
<style><%@include file="includes/css/header.css"%></style>
<link rel="stylesheet" href="includes/css/calendar.css" />
<!-- ********************************************************************************************************************************************************* -->

<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>

<script>
	/*
$(function(){
          $("#dash").tablesorter({widgets: ['zebra']});
        });
	*/


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
<body style="font-family: sans-serif; color: black; font-size: 1;">
 <%@include  file="includes/header.html" %>
<div style="padding-left:20px">
  <h3>FIS Infolease Booking Report Page</h3>
</div> <BR>
<!-- ********************************************************************************************************************************************************* -->
<%!  
public String TableHeader(){
	String thead = null;
	String color1 = "#5DADE2";
	String style1 = "font-family: sans-serif; color: white;";	  
	//thead = "<tr bgcolor=" + color1 +  "style=" + style1 + ">";
	thead = "<tr class=\"b\" "  +  "style=" + style1 + ">";
	//thead += "<th class=\"b\">App Number</th>";
	thead += "<th>Contract ID</th>";
	thead += "<th class=\"c\">Customer Name</th>";
	thead += "<th>Contract Term</th>";
	thead += "<th>Date Commenced</th>";
	thead += "<th>Term Date</th>";
	thead += "<th>Total Service</th>";
	thead += "<th>Today's Date</th>";
	thead += "<th>Equip. City</th>";
	thead += "<th>Equip. State</th>";
	thead += "<th>Equip. Rep</th>";
	thead += "<th>SoldTo</th>";
	thead += "<th>Booking Date</th>";
	thead += "</tr>";	
	return thead;
} 
%>
<!-- ********************************************************************************************************************************************************* -->
<script>
/*
	var data = ${jsonStr}
	for (var i=0; i<data.length; i++) {
		document.write(data[i] + <BR>)
	}
*/
</script>
 
<%
String  s1  = (String) session.getAttribute("id"); 

//retrieve your list from the request, with casting 
ArrayList<String> list = new ArrayList<String>();
String rowColor = null;

list = (ArrayList<String>) session.getAttribute("strArr");
//out.println("listSize:" + list.size()   + "   ID:" + s1);

//out.println("listSize:" +  "   ID:" + s1 );
//System.out.println("******** arrSize=" + list.size() + " ---- ID:"  + s1);

	if (list.size() > 0 ) {
		String xDataItem = null;
		String color1 = "plum";
		String style1 = "font-family: sans-serif; color: white;";
		out.println("<table border=\"1\" class=\"a\"> ");
		 
		out.println("<tr bgcolor=" + color1 +  "style=" + style1 + ">" );
		out.println(TableHeader());
	 
		String rowEven = "#D7DBDD";
		String rowOdd = "AEB6BF";
		for(int i = 0; i < list.size(); i++) { 
	 
				rowColor = (i % 2 == 0) ? rowEven : rowOdd;
			out.println("<tr bgcolor=" + rowColor +  ">");
			xDataItem = list.get(i);
			//out.println( strArr  );
			//out.println( "<br>");
			String token_list[] = xDataItem.split(":");	
			 for(int x=0; x <token_list.length; x++) {
				 out.println("<td class=\"odd\">"   + token_list[x] + "</td>"  );			        
			}
			 out.println("</tr>");
		}	
		 
		out.println("</table> ");
		
	} else {
		out.println( "No data to display." + "<br>");
	}

%>
<!-- ********************************************************************************************************************************************************* -->	
</body>
</html>