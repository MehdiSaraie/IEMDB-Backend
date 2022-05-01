<%--
  Created by IntelliJ IDEA.
  User: hadi
  Date: 10.03.22
  Time: 14:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="classes.IEMDB" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>IEMDB</title>
</head>
<body>
    <ul>
        <li id="user_email">User Email:
                <%
                    String userEmail = "";
                    if (IEMDB.getInstance().getLoggedInUser() == null){
                        response.sendRedirect("/login.jsp");
                    } else
                        userEmail = IEMDB.getInstance().getLoggedInUser().getEmail();
                %>
                <%= userEmail %>
        </li>
        <li>
            <a href="/movies">Movies</a>
        </li>
        <li>
            <a href="/watchlist">Watch List</a>
        </li>
        <li>
            <a href="/logout">Log Out</a>
        </li>
    </ul>
</body>
</html>
