<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>500 Internal Server Error</title>
</head>
<body>
    <h1>500 Internal Server Error</h1>
    <h2><%=exception%></h2>
    <p style="color: #7A7A7A; font-size: 9pt">
        <%
            // response.setStatus(HttpServletResponse.SC_OK);
            exception.printStackTrace(response.getWriter());
        %>
    </p>
</body>
</html>
