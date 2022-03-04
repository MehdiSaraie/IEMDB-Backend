package Interface.InterfaceServer;

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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class InterfaceServer {


    private Javalin app;

    private IEMDB iemdb = new IEMDB();


    public IEMDB getiemdb() {
        return iemdb;
    }

    public void start(final String IEMDB_URI, final int port) {
        try {
            importDataFromWeb(IEMDB_URI);
            runServer(port);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void runServer(final int port) throws Exception {
        app = Javalin.create().start(port);
        app.get("/movies", ctx -> ctx.html(readResourceFile("movies.html")));
//        app.get("/hello/:name", ctx -> {
//            ctx.result("Hello: " + ctx.pathParam("name"));
//        });

//        app.get("restaurants/near/", ctx -> {
//            try {
//                ctx.html(generateGetNearRestaurantsPage());
//            }catch (Exception e){
//                System.out.println(e.getMessage());
//                ctx.status(502);
//            }
//        });
//
//        app.get("restaurants/:restaurantId", ctx -> {
//            try {
//                ctx.html(generateGetNearRestaurantPage(ctx.pathParam("restaurantId")));
//            }catch (RestaurantNotFoundException e) {
//                ctx.status(404).result("Not Found");
//            }catch (RestaurantIsNotNearUserException e){
//                ctx.status(403).result("Unauthorized");
//            }catch (Exception e){
//                System.out.println(e.getMessage());
//                ctx.status(502).result(":| " + e.getMessage());
//            }
//        });
//
//        app.get("profile/:email", ctx -> {
//            try {
//                ctx.html(generateGetUserProfile());
//            }catch (Exception e){
//                System.out.println(e.getMessage());
//                ctx.status(502).result(":| " + e.getMessage());
//            }
//        });
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
//
//
//        app.get("/getCart", ctx -> {
//            try {
//                ctx.html(generateUserCart());
//            } catch (Exception exception) {
//                System.out.println(exception.getMessage());
//            }
//        });
//
//        app.post("/finalize", ctx -> {
//            try {
//                iemdb.finalizeOrder();
//                ctx.html("order successfully finalized");
//            } catch (Exception exception) {
//                System.out.println(exception.getMessage());
//                ctx.html(exception.getMessage());
//                ctx.status(400);
//            }
//        });

    }

//    private String generateUserCart() throws Exception {
//       Cart cart = iemdb.getCart();
//       if (cart.getSize() == 0)
//           return "user cart is empty";
//       HashMap<String, String> context = new HashMap<>();
//       context.put("restaurantName", cart.getRestaurant().getName());
//       String userCartHtml = HTMLHandler.fillTemplate(readResourceFile("CartBefore.html"), context);
//       for (CartItem cartItem: cart.getCartItems()) {
//           context.clear();
//           context.put("foodName", cartItem.getFood().getName());
//           context.put("foodAmount", String.valueOf(cartItem.getQuantity()));
//           userCartHtml += HTMLHandler.fillTemplate(readResourceFile("CartItem.html"), context);
//       }
//       userCartHtml += readResourceFile("CartAfter.html");
//       return userCartHtml;
//    }
//
//    public String generateGetUserProfile() throws Exception {
//        User user = iemdb.getUser();
//        HashMap<String, String> context = new HashMap<>();
//        context.put("fullName", user.getFullName());
//        context.put("email", user.getEmail());
//        context.put("phoneNumber", user.getPhoneNumber());
//        context.put("credit", String.valueOf(user.getCredit()));
//        return HTMLHandler.fillTemplate(readResourceFile("user.html"), context);
//    }
//
//    public String generateGetNearRestaurantPage(String id) throws Exception{
//        Restaurant restaurant = iemdb.getNearRestaurantById(id);
//        HashMap<String, String> restaurantContext = new HashMap<>();
//        restaurantContext.put("id", restaurant.getId());
//        restaurantContext.put("logo", restaurant.getLogo());
//        restaurantContext.put("name", restaurant.getName());
//        restaurantContext.put("distance", Double.toString(restaurant.getDistanceFromLocation(new Location(0,0))));
//        restaurantContext.put("x", Double.toString(restaurant.getLocation().getX()));
//        restaurantContext.put("y", Double.toString(restaurant.getLocation().getY()));
//        String nearRestaurantHTML = HTMLHandler.fillTemplate(readResourceFile("restaurantBefore.html"), restaurantContext);
//
//        String menuItemHTML = readResourceFile("restaurantMenuItem.html");
//        for(Food food: restaurant.getMenu()){
//            HashMap<String, String> context = new HashMap<>();
//            context.put("restaurantId", restaurant.getId());
//            context.put("logo", food.getImage());
//            context.put("name", food.getName());
//            context.put("price", Double.toString(food.getPrice()));
////            context.put("description", restaurant.getDescription());
//            nearRestaurantHTML += HTMLHandler.fillTemplate(menuItemHTML, context);
//        }
//
//        nearRestaurantHTML += readResourceFile("restaurantAfter.html");
//        return nearRestaurantHTML;
//    }
//
//    public String generateGetNearRestaurantsPage() throws Exception{
//        String nearRestaurantsHTML = readResourceFile("restaurantsBefore.html");
//        List<Restaurant> nearRestaurants = iemdb.getNearRestaurants();
//        String restaurantItemHTML = readResourceFile("restaurantsItem.html");
//        int counter = 1;
//        for(Restaurant restaurant: nearRestaurants){
//            HashMap<String, String> context = new HashMap<>();
//            context.put("id", restaurant.getId());
//            context.put("logo", restaurant.getLogo());
//            context.put("name", restaurant.getName());
//            context.put("distance", Double.toString(restaurant.getDistanceFromLocation(new Location(0,0))));
//            context.put("description", restaurant.getPropertyOrDefaultValue("description", "nothing to show"));
//            nearRestaurantsHTML += HTMLHandler.fillTemplate(restaurantItemHTML, context);
//        }
//        nearRestaurantsHTML += readResourceFile("restaurantsAfter.html");
//        return nearRestaurantsHTML;
//    }
//
    private String readResourceFile(String fileName) throws Exception{
        File file = new File(Resources.getResource("templates/" + fileName).toURI());
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }
//
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
