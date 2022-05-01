package com.example.ie_phase3;

import classes.IEMDB;
import classes.Movie;
import classes.User;
import javafx.util.Pair;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "RateMovieServlet", value = "/RateMovie")
public class RateMovieServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int rate = Integer.parseInt(request.getParameter("quantity"));
        int movieId =  Integer.parseInt(request.getParameter("movie_id"));
        String userEmail = request.getParameter("user_email");
        IEMDB.getInstance().rateMovie(userEmail, movieId, rate);
        response.sendRedirect("/movie.jsp?movie_id=" + movieId);
    }
}
