<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Olympus VA PO Expire Menu</title>
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


<div style="padding-left:20px">
  <h3>FIS PO Expire Menu Page</h3>
</div>

<BR>

<h5>This page will provide access to the FIS VA PO Expired eports and data.<br>
Please select an action from the menu and provide the required
parameters if necessary.</h5>



<h5>Note: <font color="red">Requires Javascript to be enabled.</font> <BR>
The dropdown menus are dynamically loaded. You may need to reload 
to refresh the menus.
</h5>

<BR>


	<form name="actionform" method="post" action="vapoexpiredresults.jsp">

<table class="a" width="40%"  border="1" cellpadding="1" cellspacing="1">
  <tr> <th class="theader"> Olympus FIS Web Reports</th> </tr>
  <tr>
    <td class="table_cell">
    <!--  Inner Table -->
    <table class="a" width="100%"  border="1" cellpadding="1" cellspacing="1">
  <tr>
        <td width="40" valign="bottom">
        <b>Action:</b> 
        <select name="actiontype" onchange='ajaxFunction()' >
          <option value="0">Select Action</option>
          
          <option value="1">O'MARA, BRIAN</option>
          <option value="2">KUCHARUK, DEB</option>
           <option value="3">ALREAD, LEANGELA</option>
           <option value="5">WALL, LYNN</option>
		  <option value="6">ULIZZI, CHRIS</option>
           <option value="8">TAVACKOLI, MO</option>
            <option value="9">HOLLIDAY, DAVID</option>
            <option value="10">STERNBERG, KARLA</option>
           <option value="11">DAVIS, GREG</option>
            
        </select>
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
<h5>If you require access to the reports, please contact: John.Freeh@olympus.com</h5>
</body>
</html>