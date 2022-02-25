import IEMDB.Exception.InvalidCommandException;
import IEMDB.LoadBalancer;

import IEMDB.Movie.Actor;
import IEMDB.Movie.Movie;
import IEMDB.Movie.Rate;
import IEMDB.User.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Interface {
    public static void main(String[] args){
        PrintStream consoleOut = System.out;
        InputStream consoleIn = System.in;
        start(consoleIn, consoleOut);
    }

    public static void start(InputStream inputStream, PrintStream outputStream) {
        System.setOut(outputStream);
        LoadBalancer lB = new LoadBalancer();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try {
            while ((line = br.readLine()) != null) {
                try {
                    String[] input_parts = parseInput(line);
                    String command = input_parts[0];
                    String jsonData = "";
                    if (input_parts.length == 2) {
                        jsonData = input_parts[1];
                    }
                    runCommand(command, jsonData, lB);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void runCommand(String command, String jsonData, LoadBalancer loadBalancer) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            switch (command) {
                case "addActor": {
                    Actor actor = gson.fromJson(jsonData, Actor.class);
                    loadBalancer.addActor(actor);
                    printOutput(true, "Actor Added Successfully");
                    break;
                }
                case "addMovie": {
                    Movie movie = gson.fromJson(jsonData, Movie.class);
                    loadBalancer.addMovie(movie);
                    printOutput(true, "Movie Added Successfully");
                    break;
                }
                case "addUser": {
                    User user = gson.fromJson(jsonData, User.class);
                    loadBalancer.addUser(user);
                    printOutput(true, "User Added Successfully");
                    break;
                }
                case "addComment": {
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String userEmail = properties.getProperty("userEmail");
                    String movieId = properties.getProperty("movieId");
                    String text = properties.getProperty("text");
                    loadBalancer.addComment(userEmail, Integer.valueOf(movieId), text);
                    printOutput(true, "Comment Added Successfully");
                    break;
                }
                case "rateMovie": {
                    Rate rate = gson.fromJson(jsonData, Rate.class);
                    loadBalancer.addRateToMovie(rate);
                    printOutput(true, "Rating Done Successfully");
                    break;
                }
                case "voteComment": {
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String userEmail = properties.getProperty("userEmail");
                    String commentId = properties.getProperty("commentId");
                    String vote = properties.getProperty("vote");
                    loadBalancer.addVoteToComment(userEmail, Integer.valueOf(commentId), Integer.valueOf(vote));
                    printOutput(true, "Voting Done Successfully");
                    break;
                }
                case "addToWatchList": {
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String userEmail = properties.getProperty("userEmail");
                    String movieId = properties.getProperty("movieId");
                    loadBalancer.addMovieToWatchList(userEmail, Integer.valueOf(movieId));
                    printOutput(true, "Adding Movie to Watchlist Done Successfully");
                    break;
                }
                case "removeFromWatchList": {
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String userEmail = properties.getProperty("userEmail");
                    String movieId = properties.getProperty("movieId");
                    loadBalancer.removeMovieFromWatchList(userEmail, Integer.valueOf(movieId));
                    printOutput(true, "Removing Movie from Watchlist Done Successfully");
                    break;
                }
                case "getMoviesList": {
                    String data = loadBalancer.getMoviesList();
                    printOutput(true, data);
                    break;
                }
                case "getMovieById": {
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String movieId = properties.getProperty("movieId");
                    String data = loadBalancer.getMovieById(Integer.valueOf(movieId));
                    printOutput(true, data);
                    break;
                }
                case "getMoviesByGenre": {
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String genre = properties.getProperty("genre");
                    String data = loadBalancer.getMoviesByGenre(genre);
                    printOutput(true, data);
                    break;
                }
                case "getWatchList": {
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String userEmail = properties.getProperty("userEmail");
                    String data = loadBalancer.getWatchList(userEmail);
                    printOutput(true, data);
                    break;
                }
                default:
                    printOutput(false, new InvalidCommandException().getMessage());
            }
        } catch (Exception e) {
            printOutput(false, e.getMessage());
        }
    }

    private static String[] parseInput(String input) {
        return input.split(" ", 2);
    }

    private static void printOutput(Boolean flag, String data) {
        Map<String, String> elements = new HashMap<>();
        elements.put("success", flag.toString());
        elements.put("data", data);

        JSONObject json = new JSONObject(elements);
        System.out.println(json);
    }
}