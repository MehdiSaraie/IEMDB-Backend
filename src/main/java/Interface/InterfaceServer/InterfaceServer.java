package Interface.InterfaceServer;

import IEMDB.Exception.*;
import IEMDB.Movie.Rate;
import Interface.HTTPRequestHandler.HTTPRequestHandler;
import IEMDB.IEMDB;
import IEMDB.Movie.Actor;
import IEMDB.Movie.Comment;
import IEMDB.Movie.Movie;
import IEMDB.User.User;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.javalin.Javalin;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfaceServer {


    private Javalin app;

    private IEMDB iemdb = new IEMDB();

    private Map<String, Document> templates = new HashMap<>();

    public IEMDB getiemdb() {
        return iemdb;
    }

    public void start(final String IEMDB_URI, final int port) {
        try {
            importDataFromWeb(IEMDB_URI);
            loadTemplates();
            runServer(port);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadTemplates() throws Exception {
        templates.put("watchlist", readTemplateFile("watchlist.html"));
        templates.put("actor", readTemplateFile("actor.html"));
        templates.put("movie", readTemplateFile("movie.html"));
        templates.put("movies", readTemplateFile("movies.html"));
        templates.put("200", readTemplateFile("200.html"));
        templates.put("403", readTemplateFile("403.html"));
        templates.put("404", readTemplateFile("404.html"));

    }

    public void runServer(final int port) throws Exception {
        app = Javalin.create().start(port);
        app.get("/movies", ctx -> {
            try {
                ctx.html(generateMoviesPage(iemdb.getMoviesList()));
            }catch (ActorNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e) {
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

        app.get("movies/{movieId}", ctx -> {
            try {
                ctx.html(generateMoviePage(Integer.valueOf(ctx.pathParam("movieId"))));
            }catch (MovieNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (ActorNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

        app.get("actors/{actorId}", ctx -> {
            try {
                ctx.html(generateActorPage(Integer.valueOf(ctx.pathParam("actorId"))));
            }catch (ActorNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

        app.get("watchlist/{userId}", ctx -> {
            try {
                ctx.html(generateWatchListPage(ctx.pathParam("userId")));
            }catch (UserNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

        app.post("watchlist/{movieId}", ctx -> {
            try {
                iemdb.addMovieToWatchList(ctx.formParam("userId"), Integer.valueOf(ctx.pathParam("movieId")));
                ctx.html(templates.get("200").html()).status(200);
            }catch (MovieNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (UserNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

        app.get("watchlist/{userId}/{movieId}", ctx -> {
            try {
                iemdb.addMovieToWatchList(ctx.pathParam("userId"), Integer.valueOf(ctx.pathParam("movieId")));
                ctx.html(templates.get("200").html()).status(200);
            }catch (MovieNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (UserNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

        app.post("watchlist/{userId}/{movieId}", ctx -> {
            try {
                iemdb.addMovieToWatchList(ctx.pathParam("userId"), Integer.valueOf(ctx.pathParam("movieId")));
                ctx.html(templates.get("200").html()).status(200);
            }catch (MovieNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (UserNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

        app.get("rateMovie/{userId}/{movieId}/{rate}", ctx -> {
            try {
                iemdb.addRateToMovie(new Rate(
                        ctx.pathParam("userId"),
                        Integer.valueOf(ctx.pathParam("movieId")),
                        Integer.valueOf(ctx.pathParam("rate"))));
                ctx.html(templates.get("200").html()).status(200);
            }catch (MovieNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (UserNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

        app.post("rateMovie", ctx -> {
            try {
                iemdb.addRateToMovie(new Rate(
                        ctx.formParam("userId"),
                        Integer.valueOf(ctx.formParam("movieId")),
                        Integer.valueOf(ctx.formParam("rate"))));
                ctx.html(templates.get("200").html()).status(200);
            }catch (MovieNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (UserNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

        app.get("voteComment/{userId}/{commentId}/{rate}", ctx -> {
            try {
                iemdb.addVoteToComment(
                        ctx.pathParam("userId"),
                        Integer.valueOf(ctx.pathParam("commentId")),
                        Integer.valueOf(ctx.pathParam("rate")));
                ctx.html(templates.get("200").html()).status(200);
            }catch (CommentNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (UserNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

        app.post("voteComment", ctx -> {
            try {
                iemdb.addVoteToComment(
                        ctx.formParam("userId"),
                        Integer.valueOf(ctx.formParam("commentId")),
                        Integer.valueOf(ctx.formParam("rate")));
                ctx.html(templates.get("200").html()).status(200);
            }catch (CommentNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (UserNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

        app.post("removefromwatchlist", ctx -> {
            try {
                System.out.println(ctx.formParam("userId") + Integer.valueOf(ctx.formParam("movieId")));
                iemdb.removeMovieFromWatchList(ctx.formParam("userId"), Integer.valueOf(ctx.formParam("movieId")));
                ctx.html(templates.get("200").html()).status(200);
            }catch (MovieNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (UserNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

        app.get("/movies/search/{startYear}/{endYear}", ctx -> {
            try {
                ctx.html(generateMoviesPage(iemdb.getMoviesByReleaseDate(
                        Integer.valueOf(ctx.pathParam("startYear")), Integer.valueOf(ctx.pathParam("endYear"))
                )));
            }catch (ActorNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e) {
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

        app.get("/movies/search/{genre}", ctx -> {
            try {
                ctx.html(generateMoviesPage(iemdb.getMoviesByGenre(ctx.pathParam("genre"))));
            }catch (ActorNotFoundException e) {
                ctx.html(templates.get("404").html()).status(404);
            }catch (Exception e) {
                System.out.println(e.getMessage());
                ctx.html(templates.get("403").html()).status(403);
            }
        });

    }

    public String generateMoviesPage(List<Movie> movies) throws Exception{
        Document doc = templates.get("movies");
        Element table = doc.body().getElementsByTag("table").first();
        Element tbody = table.child(0);
        String movieRows = "";
        for (Movie movie : movies){
            List<String> cast = new ArrayList<>();
            for (Integer castId : movie.getCast()){
                cast.add(iemdb.findActor(castId).getName());
            }
            movieRows += "<tr>" +
                    "<td>" + movie.getName() + "</td>" +
                    "<td>" + movie.getSummary() + "</td>" +
                    "<td>" + movie.getReleaseDate() + "</td>" +
                    "<td>" + movie.getDirector() + "</td>" +
                    "<td>" + movie.getWriters() + "</td>" +
                    "<td>" + movie.getGenres() + "</td>" +
                    "<td>" + cast + "</td>" +
                    "<td>" + movie.getImdbRate() + "</td>" +
                    "<td>" + movie.getRating() + "</td>" +
                    "<td>" + movie.getDuration() + "</td>" +
                    "<td>" + movie.getAgeLimit() + "</td>" +
                    "<td><a href=/movies/" + movie.getId() + ">Link</a></td>" +
                    "</tr>";
        }
        table.html(tbody.child(0).outerHtml() + movieRows);
        return doc.html();
    }

    public String generateMoviePage(Integer movieId) throws Exception {
        Document doc = templates.get("movie");
        Movie movie = iemdb.findMovie(movieId);
        List<String> cast = new ArrayList<>();
        for (Integer castId : movie.getCast()){
            cast.add(iemdb.findActor(castId).getName());
        }
        doc.getElementById("name").html("name: " + movie.getName());
        doc.getElementById("summary").html("summary: " + movie.getSummary());
        doc.getElementById("releaseDate").html("releaseDate: " + movie.getReleaseDate());
        doc.getElementById("director").html("director: " + movie.getDirector());
        doc.getElementById("writers").html("writers: " + movie.getWriters().toString());
        doc.getElementById("genres").html("genres: " + movie.getGenres().toString());
        doc.getElementById("cast").html("cast: " + cast.toString());
        doc.getElementById("imdbRate").html("imdb Rate: " + movie.getImdbRate().toString());
        doc.getElementById("rating").html("rating: " + (movie.getRating() != null ? movie.getRating().toString() : "null"));
        doc.getElementById("duration").html("duration: " + movie.getDuration().toString());
        doc.getElementById("ageLimit").html("ageLimit: " + movie.getAgeLimit().toString());

        Element rateMovieForm = doc.body().getElementsByTag("form").get(0);
        rateMovieForm.attr("action", "/rateMovie");

        String rateMovieFormString =
                "<input id='form_movie_id' type='hidden' name='movieId' value='" + movie.getId() + "' />" +
                        "<label>Your ID(email):</label>" +
                        "<input type='text' name='userId' value='' />" +
                        "<label> Rate(between 1 and 10):</label>" +
                        "<input type='number' name='rate' value='' min='1' max='10' />" +
                        "<button type='submit'>Rate</button>";

        rateMovieForm.html(rateMovieFormString);

        Element watchlistForm = doc.body().getElementsByTag("form").get(1);
        watchlistForm.attr("action", "/watchlist/" + movie.getId().toString());

        String watchlistFormString =
                "<label>Your ID(email):</label>" +
                        "<input type='text' name='userId' value='' />" +
                        "<button type='submit'>Add to WatchList</button>";

        watchlistForm.html(watchlistFormString);

        Element table = doc.body().getElementsByTag("table").first();
        Element tbody = table.child(0);
        String commentRows = "";
        for (Comment comment : movie.getUserComments()){
            commentRows += "<tr>" +
                    "<td>" + iemdb.findUser(comment.getUserEmail()).getNickname() + "</td>" +
                    "<td>" + comment.getText() + "</td>" +
                    "<td>" +
                    "<form action='/voteComment' method='POST'>" +
                    "<label for=''>" + comment.getLikesCount() + "</label>" +
                    "<input id='form_comment_id' type='hidden' name='commentId' value='" + comment.getId() + "' />" +
                    "<input id='form_rate_id' type='hidden' name='rate' value='1' />" +
                    "<label>Your ID(email):</label>" +
                    "<input type='text' name='userId' value='' />" +
                    "<button type='submit'>like</button>" +
                    "</form>" +
                    "</td>" +
                    "<td>" +
                    "<form action='/voteComment' method='POST'>" +
                    "<label for=''>" + comment.getDislikesCount() + "</label>" +
                    "<input id='form_comment_id' type='hidden' name='commentId' value='" + comment.getId() + "' />" +
                    "<input id='form_rate_id' type='hidden' name='rate' value='-1' />" +
                    "<label>Your ID(email):</label>" +
                    "<input type='text' name='userId' value='' />" +
                    "<button type='submit'>dislike</button>" +
                    "</form>" +
                    "</td>" +
                    "</tr";
        }
        table.html(tbody.child(0).outerHtml() + commentRows);

        return doc.html();
    }

    private String generateWatchListPage(String userId) throws Exception {
        Document doc = templates.get("watchlist");
        User user = iemdb.findUser(userId);
        doc.getElementById("name").html("name: " + user.getName());
        doc.getElementById("nickname").html("nickname: " + user.getNickname());

        List<Movie> watchlist = iemdb.getWatchList(userId);

        Element table = doc.body().getElementsByTag("table").first();
        Element tbody = table.child(0);
        String movieRows = "";

        for (Movie movie : watchlist){
            movieRows += "<tr>" +
                    "<td>" + movie.getName() + "</td>" +
                    "<td>" + movie.getReleaseDate() + "</td>" +
                    "<td>" + movie.getDirector() + "</td>" +
                    "<td>" + movie.getGenres() + "</td>" +
                    "<td>" + movie.getImdbRate() + "</td>" +
                    "<td>" + movie.getRating() + "</td>" +
                    "<td>" + movie.getDuration() + "</td>" +
                    "<td><a href=/movies/" + movie.getId() + ">Link</a></td>" +
                    "<td>" +
                        "<form action='/removefromwatchlist' method='POST'>" +
                            "<input id='form_user_id' type='hidden' name='userId' value='" + userId + "' />" +
                            "<input id='form_movie_id' type='hidden' name='movieId' value='" + movie.getId() + "' />" +
                            "<button type='submit'>Remove</button>" +
                        "</form>" +
                    "</td>" +
                    "</tr";
        }
        table.html(tbody.child(0).outerHtml() + movieRows);

        return doc.html();
    }

    private String generateActorPage(Integer actorId) throws Exception {
        Document doc = templates.get("actor");
        Actor actor = iemdb.findActor(actorId);
        doc.getElementById("name").html("name: " + actor.getName());
        doc.getElementById("birthDate").html("birthDate: " + actor.getBirthDate());
        doc.getElementById("nationality").html("nationality: " + actor.getNationality());
        List<Movie> actorMovies = iemdb.getMoviesByActor(actorId);
        doc.getElementById("tma").html("Total movies acted in: " + actorMovies.size());


        Element table = doc.body().getElementsByTag("table").first();
        Element tbody = table.child(0);
        String movieRows = "";

        for (Movie movie : actorMovies){
            movieRows += "<tr>" +
                    "<td>" + movie.getName() + "</td>" +
                    "<td>" + movie.getImdbRate() + "</td>" +
                    "<td>" + movie.getRating() + "</td>" +
                    "<td><a href=/movies/" + movie.getId() + ">Link</a></td>" +
                    "</tr";
        }
        table.html(tbody.child(0).outerHtml() + movieRows);

        return doc.html();
    }

    private Document readTemplateFile(String fileName) throws Exception{
        File file = new File(Resources.getResource("templates/" + fileName).toURI());
        return Jsoup.parse(file, "UTF-8");
    }

    public void importDataFromWeb(String uri) throws Exception {
        importUserDataFromWeb(uri);
        importActorDataFromWeb(uri);
        importMovieDataFromWeb(uri);
        importCommentDataFromWeb(uri);
    }

    public void importUserDataFromWeb(String uri) throws Exception {
        String UsersJsonString = HTTPRequestHandler.getRequest(uri + "/users");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<User> users = gson.fromJson(UsersJsonString, new TypeToken<List<User>>() {
        }.getType());
        int counter = 1;
        for (User user : users) {
            System.out.println(counter + "----------------");
            counter++;
            try {
                iemdb.addUser(user);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void importActorDataFromWeb(String uri) throws Exception {
        String ActorsJsonString = HTTPRequestHandler.getRequest(uri + "/actors");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Actor> actors = gson.fromJson(ActorsJsonString, new TypeToken<List<Actor>>() {
        }.getType());
        int counter = 1;
        for (Actor actor : actors) {
            System.out.println(counter + "----------------");
            counter++;
            try {
                iemdb.addActor(actor);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void importMovieDataFromWeb(String uri) throws Exception {
        String MoviesJsonString = HTTPRequestHandler.getRequest(uri + "/movies");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Movie> movies = gson.fromJson(MoviesJsonString, new TypeToken<List<Movie>>() {
        }.getType());
        int counter = 1;
        for (Movie movie : movies) {
            System.out.println(counter + "----------------");
            counter++;
            try {
                iemdb.addMovie(movie);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void importCommentDataFromWeb(String uri) throws Exception {
        String CommentsJsonString = HTTPRequestHandler.getRequest(uri + "/comments");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Comment> comments = gson.fromJson(CommentsJsonString, new TypeToken<List<Comment>>() {
        }.getType());
        int counter = 1;
        for (Comment comment : comments) {
            System.out.println(counter + "----------------");
            counter++;
            try {
                iemdb.addComment(comment);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void stop() {
        app.stop();
    }
}
