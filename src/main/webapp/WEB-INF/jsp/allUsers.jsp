<%@ page import="java.util.ArrayList" %>
<%@ page import="com.tromza.pokertds.domain.User" %><%--
  Created by IntelliJ IDEA.
  User: Viktar
  Date: 28/03/2023
  Time: 15:02
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% ArrayList <User> allUsers = (ArrayList<User>) request.getAttribute("allUsers");%>


<html>
<head>
    <title>All users</title>
</head>
<body>
<h1> Hello, there are all our Users!</h1>
<c:set var="user"/>
<c:forEach var="user" items="${allUsers}">
    <p>${user}</p>
</c:forEach>
</body>
</html>
