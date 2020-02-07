<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<% 
  	String title =  "FIS Asset Master Upload Report Page"; 
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> tokens = new ArrayList<String>();
	String formUrl =  (String) session.getAttribute("formUrl");
	
	String filePath = "C:\\Java_Dev\\props\\headers\\assetMasterHdr.txt";
	 ArrayList<String> headerArr = readHeader(filePath);
 
 
	list = (ArrayList<String>) session.getAttribute("strArr");
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
    
   <script>
  function openWin(myID) {
  
  
   myID2 = document.getElementById(b_app).value;

  alert("ID" + myID2);
  //window.open("http://cvyhj3a27:8181/fisAssetServlet/readxml?appID=" + myID2);
	}
	
	
	var call = function(id){
		var myID = document.getElementById(id).value;
		//alert("****** myID=" + myID + " ID=" + id);
		//window.open("http://cvyhj3a27:8181/fisAssetServlet/readxml?appID=
		window.open("http://localhost:8181/webreport/getquote?appKey=" + myID);
				
				
	}



	var getExcel = function(urlValue){
		var formUrl = document.getElementById(urlValue).value;
		//alert("SD=" + startDate + "****** formUrl=" + formUrl + " \n***** urlValue=" + urlValue);
		//alert("in Quote" + myID + " --- id=" +id);
		window.open( formUrl, 'popUpWindow','height=500,width=800,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no, status=yes' );
	
	}
	
	
	
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
			 header += "<th class=\"b3\" >" + dataArr.get(k) + " </th>";
			 
			
		}
	}
	return header;
	
}
/*************************************************************************************************************************************************************/

/*************************************************************************************************************************************************************/
public String  buildCells(JspWriter out, ArrayList<String> dataArr  ) throws IOException {
	String cells = "";
	String xDataItem = null;
	String color1 = "plum";
	String style1 = "font-family: sans-serif; color: white;";
	String rowEven = "#D7DBDD";
	String rowOdd = "#AEB6BF";
	String excel = null;
	String rowColor = null;
	

		if (dataArr.size() > 0) {
			for (int k = 0; k < dataArr.size(); k++) {

				rowColor = (k % 2 == 0) ? rowEven : rowOdd;
				//cells += "<tr class=\"alt-row\" bgcolor=" + rowColor + ">";
				cells = "<tr  bgcolor=" + rowColor + " >";
				out.println(cells);
				cells = "";
				xDataItem = dataArr.get(k);
				String token_list[] = xDataItem.split(";");
				for (int x = 0; x < token_list.length; x++) {
					//cells += "<td class=\"b3\">" + token_list[x] + "</td>";
					//cells += "<td class=\"b3\">" + token_list[x].trim().replaceAll("null", " ") + "</td>";
					
					cells += "<td class=\"b3\">" + token_list[x].trim().replaceAll("null", " ") + "</td>";
			 
					/*if (token_list[1].equals("3676")) {
						System.out.println("T=" + token_list[x].trim().replaceAll("null", " ") + "--");
					} */
					
					
					
				}
				cells += "</tr >";
				out.println(cells);
				cells = "";
			}
		}

		return cells;
	}%>




 
<%  
/*****************************************************************************************************************************************************************/



 
	
	//out.println("listSize=" + list.size());
	if (list.size() > 0) {
		
		%>
	
   
   
   
			<div style="height: 450px; overflow: auto;">
				
		
	
		<table class="nb" border="0" cellpadding="1" cellspacing="1">
  <tr class="nb">
    <td  class="nb" >   
    <form name="excelForm" enctype="multipart/form-data" method="get" action="<%=formUrl%> " \>
    <input type="hidden" value="excel"  name="excel" />
    <input type="submit" value="Save Excel File" class="btn" /> 
    </form>
	</td>   
   </tr></table><BR>

 
   
<!--  
<input id="search" type="text" placeholder="Enter Text to Filter...">
	 -->			
		

		<%  
		/**********************************************************************************************************************************************************/
		out.print("<BR><h5>Too many rows (" + list.size() + ")  returned. --  ");
		out.println("Download Excel file to view data.</h5><br>");
		
		// Output Table 
	
	 /*
		out.println("<table  border=\"1\"> <thead> <tr>");
		String header = buildHeader(out, headerArr); // build header from file
		out.println(header);
		out.println("</tr></thead>");
		out.println("<tbody id=\"report\">");
		String cells =  buildCells(out, list); // build data cells from file
		out.println(cells);
		out.println("</tbody></table>"); // Close Table
	
 */
		
	}
%>	
</div>
		

 



</body>
</html>