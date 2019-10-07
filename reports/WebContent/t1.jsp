<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<% 
  	String title =  "FIS Evergreen Detail Report Page"; 
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> tokens = new ArrayList<String>();
	String formUrl =  (String) session.getAttribute("formUrl");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

 <table  border="0"  width="10%"    >
  <tr>
 
    <td  valign="bottom"  >
    <form name="excelForm" enctype="multipart/form-data" method="get" action="<%=formUrl%> " \> 
    <input type="submit" value="Save Excel File" class="btn" /> 
    </form> 
	</td> 
   </tr></table>
   
<BR>
<table class="a" width="40%"  border="1" cellpadding="1" cellspacing="1">
  <tr> <th class="theader"> <%=title%></th> </tr>
  <tr>
    <td class="table_cell">
    <!--  Inner Table -->
    <table class="a" width="90%"  border="1" cellpadding="1" cellspacing="1">
  <tr>

        <td width="40" valign="bottom" class="c">  		
   <input id="search" type="text" placeholder="Enter Text to Filter...">     
	</td>
    <td  valign="bottom" class="a">   
    <form name="excelForm" enctype="multipart/form-data" method="get" action="<%=formUrl%> " \>
    <input type="hidden" value="excel"  name="excel" />
    <input type="submit" value="Save Excel File" class="btn" /> 
    </form>
    <br> 
	</td>   
   </tr></table>
 
    </td>
  </tr>
</table>
</body>
</html>