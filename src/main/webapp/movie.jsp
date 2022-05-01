<%@ page import="java.util.ArrayList" %>
<%@ page import="classes.*" %><%--
  Created by IntelliJ IDEA.
  User: hadi
  Date: 10.03.22
  Time: 21:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Movie</title>
</head>
<body>
<%
    int movieId = Integer.parseInt(request.getParameter("movie_id"));
    Movie movie = IEMDB.getInstance().getMovieById(movieId);
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
    <li id="name">name: <%= movie.getName()%>
    </li>
    <li id="summary">
        summary: <%= movie.getSummary() %>
    </li>
    <li id="releaseDate">releaseDate: <%= movie.getReleaseDate() %>
    </li>
    <li id="director">director: <%= movie.getDirector() %>
    </li>
    <li id="writers">writers: <%= movie.getWriters() %>
    </li>
    <li id="genres">genres: <%= movie.getGenres() %>
    </li>
    <li id="imdbRate">imdb Rate: <%= movie.getImdbRate() %>
    </li>
    <li id="rating">rating: <%= movie.getRating() %>
    </li>
    <li id="duration">duration: <%= movie.getDuration() %> minutes</li>
    <li id="ageLimit">ageLimit: <%= movie.getAgeLimit() %>
    </li>
</ul>
<h3>Cast</h3>
<table>
    <tr>
        <th>name</th>
        <th>age</th>
    </tr>
    <%
        for (Integer actorId : movie.getCast()) {
            Actor actor = IEMDB.getInstance().getActorById(actorId);
    %>
    <tr>
        <td><%=actor.getName()%>
        </td>
        <td><%=actor.calculateAge()%>
        </td>
        <td><a href="actor.jsp?actor_id=<%= actorId %>">Link</a></td>
    </tr>
    <% } %>
</table>
<br>
<form action="/RateMovie" method="POST">
    <label>Rate(between 1 and 10):</label>
    <input type="number" id="quantity" name="quantity" min="1" max="10">
    <input type="hidden" id="rate_movie_id" name="movie_id" value="<%=movieId%>">
    <input type="hidden" id="rate_user_email" name="user_email"
           value="<%=IEMDB.getInstance().getLoggedInUser().getEmail()%>">
    <button type="submit">rate</button>
</form>
<br>
<form action="/AddToWatchList" method="POST">
    <input type="hidden" id="add_movie_id" name="movie_id" value="<%=movieId%>">
    <input type="hidden" id="add_user_email" name="user_email"
           value="<%=IEMDB.getInstance().getLoggedInUser().getEmail()%>">
    <button type="submit">Add to WatchList</button>
</form>
<br/>
<table>
    <tr>
        <th>nickname</th>
        <th>comment</th>
        <th></th>
        <th></th>
    </tr>
    <%
        for (Comment comment : movie.getComments()) {
            User user = IEMDB.getInstance().getUserByEmail(comment.getUserEmail());

    %>
    <tr>
        <td>@<%= user.getNickname() %>
        </td>
        <td><%= comment.getText() %>
        </td>
        <td>
            <form action="/VoteComment" method="POST">
                <label><%= comment.getLike()%>
                </label>
                <input type="hidden" name="action" value="like">
                <input type="hidden" name="movie_id" value="<%= movie.getId() %>">
                <input type="hidden" name="comment_id" value="<%= comment.getId() %>">
                <input type="hidden" name="user_email" value="<%= user.getEmail() %>">
                <input type="hidden" name="vote" value="1">
                <button type="submit">like</button>
            </form>
        </td>
        <td>
            <form action="/VoteComment" method="POST">
                <label><%= comment.getDislike()%>
                </label>
                <input type="hidden" name="action" value="dislike">
                <input type="hidden" name="movie_id" value="<%= movie.getId() %>">
                <input type="hidden" name="comment_id" value="<%= comment.getId() %>">
                <input type="hidden" name="user_email" value="<%= user.getEmail() %>">
                <input type="hidden" name="vote" value="-1">
                <button type="submit">dislike</button>
            </form>
        </td>
    </tr>
</table>
<% }%>
<%
    User user = IEMDB.getInstance().getUserByEmail(userEmail);
%>
<br>
<form action="/AddComment" method="POST">
    <label>Your Comment:</label>
    <input type="text" name="comment" value="" required>
    <input type="hidden" name="action" value="comment">
    <input type="hidden" name="movie_id" value="<%= movie.getId() %>">
    <input type="hidden" name="user_email" value="<%= user.getEmail() %>">
    <button type="submit">Add Comment</button>
</form>
</body>
</html>
