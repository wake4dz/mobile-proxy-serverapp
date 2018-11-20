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
	</style>
</head>
<body>
	<br /> <br /> <br /> <br /> <br />
	<h1>The Mobile App Back-end Status Info</h1>
	
	<br /> <br /> <br />
	<%
		int index = 0;
		for (String message : LogUtil.getWelcomeMessages()) {
			if (index == 0 || (index == LogUtil.getWelcomeMessages().size() -1) ) {
				out.println("<h2>" + message + "<br /> </h2>");
			} else {
				out.println("<p>" + message + "<br /> </p>");
			}
			
			
			index++;
		}
	%>

</body>
</html>