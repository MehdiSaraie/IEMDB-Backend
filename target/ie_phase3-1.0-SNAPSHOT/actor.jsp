<%@ page import="classes.IEMDB" %>
<%@ page import="classes.Actor" %>
<%@ page import="classes.Movie" %><%--
  Created by IntelliJ IDEA.
  User: hadi
  Date: 13.03.22
  Time: 18:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Actor</title>
    <style>
        li, td, th {
            padding: 5px;
        }
    </style>
</head>
<body>
<%
    int actorId = Integer.parseInt(request.getParameter("actor_id"));
    Actor actor = IEMDB.getInstance().getActorById(actorId);
%>
<a href="/">Home</a>
<p id="email">
    Email:
    <%
        String userEmail = "";
        if (IEMDB.getInstance().getLoggedInUser().getEmail() == null)
            response.sendRedirect("/login.jsp");
        userEmail = IEMDB.getInstance().getLoggedInUser().getEmail();
    %>
    <%=userEmail%>
</p>
<ul>
    <li id="name">name:  <%= actor.getName() %>
    </li>
    <li id="birthDate">birthDate: <%=actor.getBirthDate()%>
    </li>
    <li id="nationality">nationality: <%=actor.getNationality()%>
    </li>
    <li id="tma">Total movies acted in: <%= actor.getActedMovie().size() %>
    </li>
</ul>
<table>
    <tr>
        <th>Movie</th>
        <th>imdb Rate</th>
        <th>rating</th>
        <th>page</th>
    </tr>
    <%
        for (Integer movieId : actor.getActedMovie()) {
            Movie movie = IEMDB.getInstance().getMovieById(movieId);
    %>
    <tr>
        <td><%= movie.getName()%></td>
        <td><%= movie.getImdbRate()%></td>
        <td><%= movie.getRating()%></td>
        <td><a href="/movie.jsp?movie_id=<%=movie.getId()%>">Link</a></td>
    </tr>
    <% } %>
</table>
</body>
</html>
