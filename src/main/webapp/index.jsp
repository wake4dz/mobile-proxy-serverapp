<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.wakefern.logging.LogUtil" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" href="style.css" />
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Mobile App's Back-end Release Info</title>
	<style>
	    p {
		    text-align: center;
		}
		
		#envVariable {
		  font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
		  border-collapse: collapse;
		  width: 700px;
		  margin-left: auto;
		  margin-right: auto;
		}
		
		#envVariable td, #envVariable th {
		  border: 1px solid #ddd;
		  padding: 8px;
		}
		
		#envVariable tr:nth-child(even){background-color: gray;}
		
		#envVariable tr:hover {background-color: #ddd;}
		
		#envVariable th {
		  padding-top: 12px;
		  padding-bottom: 12px;
		  text-align: Center;
		  background-color: #4CAF50;
		  color: white;
		}
	</style>
</head>
<body>
	<br /> <br /> <br /> <br /> <br />
	<h1>Server Status</h1>
	
	<br /> 
	<%
		for (String message : LogUtil.getWelcomeHtmlMessages()) {
				out.println(message);;
		}
	%>

</body>
</html>