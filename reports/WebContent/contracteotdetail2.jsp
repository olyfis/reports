<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
    <% 
  	String title =  "Olympus FIS FIS Contract EOT Report"; 
    String formUrl =  (String) session.getAttribute("formUrl");
    int lsz = 0;
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








<!-- ********************************************************************************************************************************************************* -->

</head>
<body>
    
    
 <%@include  file="includes/header.html" %>

      
<!--   <img src="includes\images\logo.jpg" alt="logo"  height="100" width="225" align="right"> -->


<div style="padding-left:20px">
  <h3><%=title%></h3>
</div>

<BR>

<h5>This page will provide an on-demand Contract EOT Report.  </h5>

  
 <%
 ArrayList<String> list = (ArrayList<String>) session.getAttribute("strArr");

lsz = list.size();
		
		
		
%>
 
<BR>
<%
      if (lsz > 50) 	{		
			out.println("<h6>Too many records to display: <BR>" +   lsz  +" records returned.</h6> ");	 
		}
      
      %>



<!--  action = servlet to call: http://localhost:8181/reports/olrent?id=2019-08-31   -->
	 

<table class="a" width="40%"  border="1" cellpadding="1" cellspacing="1">
  <tr> <th class="theader"> <%=title%></th> </tr>
  
   <%
      if (lsz > 50) 	{	
    	  out.println("<tr></tr>");
    	  out.println("<tr></tr>");
    	  out.println("<tr></tr>");
    	  out.println("<tr></tr>");
    	  
			//out.println("<h6>Too many records to display: <BR>" +   lsz  +" records returned.</h6> ");	 
		} else {
			
		}
      
      %>

  
  <tr>
    <td class="table_cell">

        <!-- ************************* Inner Table ****************************************************************************** -->
    <table class="a" width="100%"  border="1" cellpadding="1" cellspacing="1">
  <tr>
        <td width="40" valign="bottom" class="c">  
      
        
	</td>

    <td  valign="bottom" class="a">
    <form name="excelForm" enctype="multipart/form-data" method="get" action="<%=formUrl%> " \> 
    <input type="submit" value="Save Excel File" class="btn" /> 
    </form> 
	</td> 
   </tr></table>
    <!-- ****************************************************************************************************************************** -->
    
    </td>
  </tr>
</table>


 
<h5>If you require access to the reports, please contact: Michael.Janenko@olympus.com</h5>
</body>
</html>