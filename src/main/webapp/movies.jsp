<%--
  Created by IntelliJ IDEA.
  User: hadi
  Date: 10.03.22
  Time: 16:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="classes.IEMDB" %>
<%@ page import="classes.Movie" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ArrayList<Movie> moviesList = (ArrayList<Movie>) request.getAttribute("moviesList");
%>
<html>
<head>
    <title>Movies</title>
</head>
<body>
    <a href="/index.jsp">Home</a>
    <p id="user_email">Email:
            <% if (IEMDB.getInstance().getLoggedInUser().getEmail() == null){
            response.sendRedirect("/login.jsp");
            }%>
            <%=IEMDB.getInstance().getLoggedInUser().getEmail()%>
    <p>
    <br><br>
    <form action="/Search" method="POST">
        <label>Search:</label>
        <input type="text" name="search" value="">
        <button type="submit" name="action" value="search">Search</button>
        <button type="submit" name="action" value="clear">Clear Search</button>
    </form>
    <br><br>
    <form action="/Sort" method="POST">
        <label>Sort By:</label>
        <button type="submit" name="action" value="sort_by_imdb">imdb Rate</button>
        <button type="submit" name="action" value="sort_by_date">releaseDate</button>
    </form>
    <br>
    <table>
        <tr>
            <th>name</th>
            <th>summary</th>
            <th>releaseDate</th>
            <th>director</th>
            <th>writers</th>
            <th>genres</th>
            <th>cast</th>
            <th>imdb Rate</th>
            <th>rating</th>
            <th>duration</th>
            <th>ageLimit</th>
            <th>Links</th>
        </tr>
        <%
//            ArrayList<Movie> movies = IEMDB.getInstance().getMovies();
            for (int i = 0; i<moviesList.size(); i++) {
                Movie movie = moviesList.get(i);
        %>
        <tr>
            <th><%= movie.getName() %></th>
            <th><%= movie.getSummary() %></th>
            <th><%= movie.getReleaseDate() %></th>
            <th><%= movie.getDirector() %></th>
            <th><%= movie.getWriters() %></th>
            <th><%= movie.getGenres() %></th>
            <th><%= movie.getCast() %></th>
            <th><%= movie.getImdbRate() %></th>
            <th><%= movie.getRating() %></th>
            <th><%= movie.getDuration() %></th>
            <th><%= movie.getAgeLimit() %></th>
            <td><a href="/movie.jsp?movie_id=<%= movie.getId() %>">Link</a></td>
        </tr>
        <%
            }
        %>
    </table>
</body>
</html>
