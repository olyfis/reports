<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="java.net.InetAddress"%>
 

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Results</title>
</head>
<body>
<h4>Results Page</h4>




<%
	String at = request.getParameter("actiontype");
	String at2 = request.getParameter("getID");
    String hostname = InetAddress.getLocalHost().getHostName();
    //String hostname2 = "cvyhj3a27";
    String hostname2 = "localhost";
   //String id = request.getParameter("id");
	//out.println("atype=" + at);
	//System.out.println("***^^^*** AT=" + at + "--");
	if (at != null) {

		if (at.equals("1")) {	 
			//out.println("ID:" + id + "--");
			String redirectURL = "http://" + hostname  + ":8181/reports/poexpire?brch=" + at;
			response.sendRedirect(redirectURL);
		} else if (at.equals("2")) {	 
			String redirectURL = "http://" + hostname2  + ":8181/reports/poexpire?brch=" + at;
			response.sendRedirect(redirectURL);
		}  else if (at.equals("3")) {	 
			String redirectURL = "http://" + hostname2  + ":8181/reports/poexpire?brch=" + at;
			response.sendRedirect(redirectURL);
		}  else if (at.equals("5")) {	 
			String redirectURL = "http://" + hostname2  + ":8181/reports/poexpire?brch=" + at;
			response.sendRedirect(redirectURL);
		}  else if (at.equals("6")) {	 
			String redirectURL = "http://" + hostname2  + ":8181/reports/poexpire?brch=" + at;
			response.sendRedirect(redirectURL);
		}  else if (at.equals("8")) {	 
			String redirectURL = "http://" + hostname2  + ":8181/reports/poexpire?brch=" + at;
			response.sendRedirect(redirectURL);
		}  else if (at.equals("9")) {	 
			String redirectURL = "http://" + hostname2  + ":8181/reports/poexpire?brch=" + at;
			response.sendRedirect(redirectURL);
		}  else if (at.equals("10")) {	 
			String redirectURL = "http://" + hostname2  + ":8181/reports/poexpire?brch=" + at;
			response.sendRedirect(redirectURL);
		} else if (at.equals("11")) {	 
			String redirectURL = "http://" + hostname2  + ":8181/reports/poexpire?brch=" + at;
			response.sendRedirect(redirectURL);
		} 
		
		
		
		
	}
%>
 
 

</body>
</html>