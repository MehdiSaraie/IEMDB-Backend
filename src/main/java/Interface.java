import IEMDB.LoadBalancer;

import IEMDB.Movie.Actor;
import IEMDB.Movie.Comment;
import IEMDB.Movie.Movie;
import IEMDB.Movie.Rate;
import IEMDB.User.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
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
//                    if (input_parts.length != 2)
//                        throw new Exception("Error: Bad Format");
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
        final int RECOMMEND_COUNT = 3;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            switch (command) {
                case "alaki": {
                    Map<Integer, Integer> alaki = new HashMap<>();
                    if (alaki.containsKey(2)) {
                        alaki.put(2, 4);
                        Integer tempActor = alaki.get(2);
                    }
                    break;
                }
                case "addActor": {
                    System.out.println("Adding Actor");
                    Actor actor = gson.fromJson(jsonData, Actor.class);
                    loadBalancer.addActor(actor);
//                    printOutput(true, "Actor Added Successfully");
                    break;
                }
                case "addMovie": {
                    System.out.println("Adding Movie");
                    Movie movie = gson.fromJson(jsonData, Movie.class);
                    loadBalancer.addMovie(movie);
//                    printOutput(true, "Actor Added Successfully");
                    break;
                }
                case "addUser": {
                    System.out.println("Adding User");
                    User user = gson.fromJson(jsonData, User.class);
                    loadBalancer.addUser(user);
//                    printOutput(true, "Actor Added Successfully");
                    break;
                }
                case "addComment": {
                    System.out.println("Adding Comment");
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String userEmail = properties.getProperty("userEmail");
                    String movieId = properties.getProperty("movieId");
                    String text = properties.getProperty("text");
                    loadBalancer.addComment(userEmail, Integer.valueOf(movieId), text);
//                    printOutput(true, "Actor Added Successfully");
                    break;
                }
                case "rateMovie": {
                    System.out.println("Rating Movie");
                    Rate rate = gson.fromJson(jsonData, Rate.class);
                    loadBalancer.addRateToMovie(rate);
//                    printOutput(true, "Actor Added Successfully");
                    break;
                }
                case "voteComment": {
                    System.out.println("Voting Comment");
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String userEmail = properties.getProperty("userEmail");
                    String commentId = properties.getProperty("commentId");
                    String vote = properties.getProperty("vote");
                    loadBalancer.addVoteToComment(userEmail, Integer.valueOf(commentId), Integer.valueOf(vote));
//                    printOutput(true, "Actor Added Successfully");
                    break;
                }
                case "addToWatchList": {
                    System.out.println("Adding Movie to Watchlist");
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String userEmail = properties.getProperty("userEmail");
                    String movieId = properties.getProperty("movieId");
                    loadBalancer.addMovieToWatchList(userEmail, Integer.valueOf(movieId));
//                    printOutput(true, "Actor Added Successfully");
                    break;
                }
                case "removeFromWatchList": {
                    System.out.println("Removing Movie from Watchlist");
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String userEmail = properties.getProperty("userEmail");
                    String movieId = properties.getProperty("movieId");
                    loadBalancer.removeMovieFromWatchList(userEmail, Integer.valueOf(movieId));
//                    printOutput(true, "Actor Added Successfully");
                    break;
                }
                case "getMoviesList": {
                    System.out.println("Getting Movies List");
                    loadBalancer.getMoviesList();
                    break;
                }
                case "getMovieById": {
                    System.out.println("Getting Movie By Id");
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String movieId = properties.getProperty("movieId");
                    loadBalancer.getMovieById(Integer.valueOf(movieId));
                    break;
                }
                case "getMoviesByGenre": {
                    System.out.println("Getting Movies By Genre");
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String genre = properties.getProperty("genre");
                    loadBalancer.getMoviesByGenre(genre);
                    break;
                }
                case "getWatchList": {
                    System.out.println("Getting WatchList");
                    Properties properties = gson.fromJson(jsonData, Properties.class);
                    String userEmail = properties.getProperty("userEmail");
                    loadBalancer.getWatchList(userEmail);
                    break;
                }
                case "getCart":
                    System.out.println("Getting cart");
//                    System.out.println(mcZmo.getBriefCartJson());

                    break;
                default:
                    throw new Exception("Error: Bad Format");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String[] parseInput(String input) {
        return input.split(" ", 2);
    }

    private static void printOutput(Boolean flag, String command) {

    }
}
