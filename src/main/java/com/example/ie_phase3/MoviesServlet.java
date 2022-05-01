package com.example.ie_phase3;

import classes.IEMDB;
import classes.Movie;
import classes.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "MoviesServlet", value = "/movies")
public class MoviesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDB iemdb = IEMDB.getInstance();
        ArrayList<Movie> moviesList = iemdb.getMovies();
        request.setAttribute("moviesList",moviesList);
        request.getRequestDispatcher("/movies.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
