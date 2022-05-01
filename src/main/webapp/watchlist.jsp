<%--
  Created by IntelliJ IDEA.
  User: hadi
  Date: 10.03.22
  Time: 18:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="classes.IEMDB" %>
<%@ page import="classes.User" %>
<%@ page import="classes.Movie" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="javafx.util.Pair" %>
<%@ page import="Tools.PairComparator.PairFIComparator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <a href="/">Home</a>
    <p id="email">
        User Email:
        <%
            String userEmail = "";
            if (IEMDB.getInstance().getLoggedInUser() == null)
                response.sendRedirect("/login.jsp");
            else
                userEmail = IEMDB.getInstance().getLoggedInUser().getEmail();
        %>
        <%= userEmail %>
    </p>
    <% User user = IEMDB.getInstance().getLoggedInUser(); %>
    <ul>
        <li id="name">name: <%= user.getName()%> </li>
        <li id="nickname">nickname: @ <%= user.getNickname()%> </li>
    </ul>
    <h2>Watch List</h2>
    <table>
        <%
            if (user.getWatchList().size() > 0) {
        %>
        <tr>
            <th>Movie</th>
            <th>releaseDate</th>
            <th>director</th>
            <th>genres</th>
            <th>imdb Rate</th>
            <th>rating</th>
            <th>duration</th>
            <th></th>
            <th></th>
        </tr>
        <%
            }
            else {
        %>
        <p> your watchlist is empty! </p>
        <% } %>
        <%
            ArrayList<Movie> watchList = new ArrayList<>();
            for (Integer movieId : user.getWatchList())
                watchList.add(IEMDB.getInstance().getMovieById(movieId));
            for (int i = 0; i<watchList.size(); i++) {
                Movie movie = watchList.get(i);
        %>
        <tr>
            <th><%= movie.getName() %></th>
            <th><%= movie.getReleaseDate() %></th>
            <th><%= movie.getDirector() %></th>
            <th><%= movie.getGenres() %></th>
            <th><%= movie.getImdbRate() %></th>
            <th><%= movie.getRating() %></th>
            <th><%= movie.getDuration() %></th>
            <td><a href="/movies/<%= i+1 %>">Link</a></td>
            <td>
                <form action="/RemoveMovie" method="POST" >
                    <input id="form_movie_id" type="hidden" name="movie_id" value="<%= movie.getId() %>">
                    <input id="form_user_email" type="hidden" name="user_email" value="<%= user.getEmail() %>">
                    <button type="submit">Remove</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
    <h2>Recommendation List</h2>
    <%
        ArrayList<Pair<Float, Integer>> moviesScore = new ArrayList<>();
        for (Movie movie : IEMDB.getInstance().getMovies()) {
            if (user.getWatchList().contains(movie.getId()))
                continue;
            int genreSimilarity = 0;
            for (Movie movie2 : watchList)
                genreSimilarity += movie.getSimilarly(movie2);
            Float score = 3*genreSimilarity + movie.getImdbRate() + movie.getRating();
            moviesScore.add(new Pair<>(score, movie.getId()));
        }
        moviesScore.sort(new PairFIComparator());
    %>
    <table>
        <tr>
            <th>Movie</th>
            <th>imdb Rate</th>
            <th></th>
        </tr>
        <%
            int RECOMMENDATION_LIST_SIZE = 3;
            for (int i = 0; i<RECOMMENDATION_LIST_SIZE; i++) {
                int movieId = moviesScore.get(i).getValue();
                Movie movie = IEMDB.getInstance().getMovieById(movieId);
        %>
        <tr>
            <th><%= movie.getName() %></th>
            <th><%= movie.getImdbRate() %></th>
            <td><a href="/movie.jsp?movie_id=<%= movieId %>">Link</a></td>
        </tr>
        <% } %>
    </table>
</body>
</html>
