package Interface.InterfaceServer;

import IEMDB.Exception.ActorNotFoundException;
import IEMDB.Exception.MovieNotFoundException;
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

        Element table = doc.body().getElementsByTag("table").first();
        Element tbody = table.child(0);
        String commentRows = "";
        for (Comment comment : movie.getUserComments()){
            commentRows += "<tr>" +
                    "<td>" + iemdb.findUser(comment.getUserEmail()).getNickname() + "</td>" +
                    "<td>" + comment.getText() + "</td>" +
                    "<td>" +
                        "<form action=\"\" method=\"POST\">" +
                            "<label for=\"\">" + comment.getLikesCount() + "</label>" +
                            "<input id=\"form_comment_id\" type=\"hidden\" name=\"comment_id\" value=" + comment.getLikesCount() + "/>" +
                            "<button type=\"submit\">like</button>" +
                        "</form>" +
                    "</td>" +
                    "<td>" +
                        "<form action=\"\" method=\"POST\">" +
                            "<label for=\"\">" + comment.getDislikesCount() + "</label>" +
                            "<input id=\"form_comment_id\" type=\"hidden\" name=\"comment_id\" value=" + comment.getDislikesCount() + "/>" +
                            "<button type=\"submit\">dislike</button>" +
                        "</form>" +
                    "</td>" +
                "</tr";
        }
        table.html(tbody.child(0).outerHtml() + commentRows);

        return doc.html();
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

//        app.get("/movies", ctx -> ctx.html(readTemplateFile("movies.html").html()));

//        app.get("restaurants/near/", ctx -> {
//            try {
//                ctx.html(generateGetNearRestaurantsPage());
//            }catch (Exception e){
//                System.out.println(e.getMessage());
//                ctx.status(502);
//            }
//        });

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
//
//        app.post("/charge", ctx -> {
//            try {
//                String creditValue = ctx.formParam("credit");
//                if (!creditValue.isEmpty())
//                    iemdb.chargeUserCredit(Double.parseDouble(creditValue));
//                ctx.result("user credit updated");
//            } catch (Exception exception) {
//                System.out.println(exception.getMessage());
//                ctx.result(exception.getMessage());
//            }
////            ctx.redirect("profile/ekhamespanah@yahoo.com");
//        });
//
//        app.post("/addToCart/:restaurantId", ctx -> {
//            String foodName = "", restaurantId = "";
//            try {
//                foodName = ctx.formParam("foodName");
//                restaurantId = ctx.pathParam("restaurantId");
//                iemdb.addToCartByRestaurantId(restaurantId, foodName);
//                ctx.status(200).result("Added to cart");
//                System.out.println(iemdb.getCart().getSize());
//            } catch (Exception exception) {
//                System.out.println(exception.getMessage());
//                ctx.status(403).result(exception.getMessage());
//            }
////            ctx.redirect("/restaurants/" + restaurantId);
//        });

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
