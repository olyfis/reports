<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <% 
  	String title =  "Olympus FIS OL Lease Rents Accrued Report"; 
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




<script language="javascript" type="text/javascript">
 
//Browser Support Code
function ajaxFunction(){
	var ajaxRequest;  // The variable that makes Ajax possible!
	
	try{
		// Opera 8.0+, Firefox, Safari
		ajaxRequest = new XMLHttpRequest();
	} catch (e){
		// Internet Explorer Browsers
		try{
			ajaxRequest = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try{
				ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e){
				// Something went wrong
				alert("Your browser broke!");
				return false;
			}
		}
	}
	// Create a function that will receive data sent from the server
	ajaxRequest.onreadystatechange = function(){
		if(ajaxRequest.readyState == 4){
			var ajaxDisplay = document.getElementById('ajaxDiv');
			ajaxDisplay.innerHTML = ajaxRequest.responseText;
		// document.actionform.actiontypeprogram.value = ajaxRequest.responseText;

		}
	}

//	var atype = document.actionform.getElementById('actiontype').value;
	var atype = document.actionform.actiontype.value;
	var date2 = document.actionform.startDate.value;
	//alert("atype=" + atype);
	
	//alert("date2=" + date2);
	//var queryString = "?atype=" + atype + "&wpm=" + wpm + "&ex=" + ex;
	var queryString = "/webreport/ajaxIL2.jsp?atype=" + atype + "&date2=" + date2;
	

	  
	//alert("QS=" + queryString);
	//ajaxRequest.open("GET", "ajax.jsp" + queryString, true);
	ajaxRequest.open("POST", queryString, true);
	ajaxRequest.send(); 
}

 
</script>





<!-- ********************************************************************************************************************************************************* -->

</head>
<body>
    
    
 <%@include  file="includes/header.html" %>

      
<!--   <img src="includes\images\logo.jpg" alt="logo"  height="100" width="225" align="right"> -->


<div style="padding-left:20px">
  <h3><%=title%></h3>
</div>

<BR>

<h5>This page will provide an on-demand Lease Rents Accrued Report.</h5>
<BR> <font color="red"> Note: Some queries take more time to run. Please be patient.</font>


 

<BR>

<!--  action = servlet to call: http://localhost:8181/webreport/olrent?id=2019-08-31   -->
	<form name="actionform" method="get" action="olrent">

<table class="a" width="40%"  border="1" cellpadding="1" cellspacing="1">
  <tr> <th class="theader"> <%=title%></th> </tr>
  <tr>
    <td class="table_cell">
    <!--  Inner Table -->
    <table class="a" width="100%"  border="1" cellpadding="1" cellspacing="1">
  <tr>
  
 
        <td width="40" valign="bottom"> <b>Select Date:</b> 
      <%  out.println("<input name=\"id\" id=\"date2\" type=\"text\" value=\"Click for Calendar\" onclick=\"pureJSCalendar.open('yyyy-MM-dd', 20, 30, 7, '2017-1-1', '2025-12-31', 'date2', 20)\"   />" );
  
     %> 
  
        
	</td>

    <td  valign="bottom" class="c">
	<div id='ajaxDiv'> </div>
	</td>
    <td> 
    <INPUT type="submit" value="Run">  
    </td>
    
   </tr></table>
    
    
    </td>
  </tr>
</table>


 </form>
<h5>If you require access to the reports, please contact: Michael.Janenko@olympus.com</h5>
</body>
</html>