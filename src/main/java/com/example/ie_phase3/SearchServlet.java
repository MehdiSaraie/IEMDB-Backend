package com.example.ie_phase3;

import classes.IEMDB;
import classes.Movie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "SearchServlet", value = "/Search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        IEMDB iemdb = IEMDB.getInstance();
        ArrayList<Movie> movies = iemdb.getMovies();
        switch (action) {
            case ("search"): {
                String searchValue = request.getParameter("search");
                ArrayList<Movie> selectedMovies = new ArrayList<>();
                for (Movie movie : movies) {
                    String movieName = movie.getName().toLowerCase();
                    if (movieName.contains(searchValue.toLowerCase()))
                        selectedMovies.add(movie);
                }
                request.setAttribute("moviesList", selectedMovies);
                request.getRequestDispatcher("/movies.jsp").forward(request,response);
                break;
            }
            case ("clear"): {
                request.setAttribute("moviesList", movies);
                request.getRequestDispatcher("/movies.jsp").forward(request,response);
            }
        }
    }
}
