 
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<% 
  	String title =  "Olympus FIS Utilization Report Page"; 
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> tokens = new ArrayList<String>();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title><%=title%></title>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="includes/js/tableFilter.js"></script>
<style><%@include file="includes/css/reports.css"%></style>
<style><%@include file="includes/css/table.css"%></style>
<style><%@include file="includes/css/header.css"%></style>
<link rel="stylesheet" href="includes/css/calendar.css" />

<!-- ********************************************************************************************************************************************************* -->
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
<body>

<%!  

String formUrl = null;
/*************************************************************************************************************************************************************/
public ArrayList<String> readHeader(String filePath) throws IOException {
	
	ArrayList<String> strArr = new ArrayList<String>();
	String header = null;
	BufferedReader reader = null;
	StringBuilder sb = null;
	String line = null;
	try {
	 	reader = new BufferedReader(new FileReader(filePath));
    	 sb = new StringBuilder();
    
	} catch (FileNotFoundException fex) {
		fex.printStackTrace();	
	}
	try { 
	    while((line = reader.readLine())!= null){
	    	strArr.add(line);
	    }	   
		reader.close();
	
	} catch (IOException ioe) {
		ioe.printStackTrace();
	}
	
	return strArr;	
}
/*************************************************************************************************************************************************************/
public String  buildHeader( JspWriter out2, ArrayList<String> dataArr   ) throws IOException {
	
	String header = "";
	String style = "b3";
	if (dataArr.size() > 0) {
		for (int k = 0; k < dataArr.size(); k++) {
			
			if (k == 1) {
				style = "b3a";
			} else {
				style = "b3";
			}
			//header += "<th class=\"b3\" >" + dataArr.get(k) + " </th>";
			header += "<th class=\" " + style + "  \" >" + dataArr.get(k) + " </th>";
			
		}
	}
	return header;
	
}
/*************************************************************************************************************************************************************/
public String  buildCells( JspWriter out, ArrayList<String> dataArr  ) throws IOException {
	String cells = "";
	String xDataItem = null;
	String color1 = "plum";
	String style1 = "font-family: sans-serif; color: white;";
	String rowEven = "#D7DBDD";
	String rowOdd = "AEB6BF";
	String excel = null;
	String rowColor = null;
 	
	System.out.print("ARRSize=" + dataArr.size() );
	 
	if (dataArr.size() > 0) {
		for (int k = 0; k < dataArr.size(); k++) {
			rowColor = (k % 2 == 0) ? rowEven : rowOdd;
			cells +="<tr bgcolor=" + rowColor + ">";
			xDataItem = dataArr.get(k);
			String token_list[] = xDataItem.split(":");
			for (int x = 0; x < token_list.length; x++) {
				cells += "<td class=\"odd\">" + token_list[x] + "</td>";
				  
			}
			cells += "</tr >";
			
			 
		}
	}
	
	System.out.print(cells);
	return cells;
}

/*************************************************************************************************************************************************************/
public String TableHeader(){
	String thead = null;
	String color1 = "#5DADE2";
	String style1 = "font-family: sans-serif; color: white;";	  
	//thead = "<tr bgcolor=" + color1 +  "style=" + style1 + ">";
	 thead = "<tr class=\"a3a\" "  +  "style=" + style1 + ">";
	 
	//thead += "<th class=\"b\">App Number</th>";
	thead += "<th  class=\"a3a\" >Contract ID</th>";
	thead += "<th>Lead Bank</th>";
	thead += "<th class=\"c\">Customer Name</th>";
	thead += "<th>Agreement Number</th>";
	thead += "<th>Procs to Date</th>";
	thead += "<th>Prior Read Date</th>";	
	thead += "<th>Contracted Procs</th>";
	thead += "<th>Commencement</th>";
	thead += "<th>Booking Date</th>";
	thead += "<th>Term</th>";
	thead += "<th>FIS Territory</th>";
	thead += "<th>Months Elapsed</th>";
	thead += "<th>Percentage Term Elapsed</th>";
	thead += "<th>Proc Utilization</th>";
	thead += "<th>Utilization Compliance</th>";
 
	thead += "<th class=\"c\">Equip Rep</th>";
	thead += "<th class=\"c\">Equip RSD</th>";
	thead += "</tr>";	
	return thead;
}  
/*************************************************************************************************************************************************************/

%>

<div style="padding-left:20px">
<%@include  file="includes/header.html" %>
<h3><%=title%></h3>

<%  

list = (ArrayList<String>) session.getAttribute("strArr");
	//list2.add("xx");
	
	//out.println("listSize=" + list.size());
	if (list.size() > 0) {
		/**********************************************************************************************************************************************************/
		// Output Table 
 %>	
		<input id="search" type="text" placeholder="Enter Text to Filter...">
<h5></h5>

<%
	String cells = "";
String xDataItem = null;
String color1 = "plum";
String style1 = "font-family: sans-serif; color: white;";
String rowEven = "#D7DBDD";
String rowOdd = "AEB6BF";
String rowColor = null;


		out.println("<table class=\"tablesorter\" border=\"1\"> <thead> <tr>");
		//String header = buildHeader(out, headerArr); // build header from file
		//out.println(header);
		out.println(TableHeader());
		out.println("</tr></thead>");
		out.println("<tbody id=\"report\">");
		
		
		for (int i = 0; i < list.size(); i++) {
			rowColor = (i % 2 == 0) ? rowEven : rowOdd;
			out.println("<tr bgcolor=" + rowColor + ">");
	
			xDataItem = list.get(i);
			String token_list[] = xDataItem.split(":");
			for (int x = 0; x < token_list.length; x++) {
				out.println("<td class=\"odd\">" + token_list[x] + "</td>");
			}
			out.println("</tr>");
		}	

		 
		
		
		
		
		 //cells =  buildCells(out, list); // build data cells from file
		//out.println(cells);
		out.println("</tbody></table>"); // Close Table
		/**********************************************************************************************************************************************************/
	} else {
		out.println("No Asset data to display." + "<br>");

	}
%>




</body>
</html>