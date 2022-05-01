package com.example.ie_phase3;

import classes.IEMDB;
import classes.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RemoveMovieFromWatchListServlet", value = "/RemoveMovie")
public class RemoveMovieFromWatchListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        int movieId = Integer.parseInt(request.getParameter("movie_id"));
        String userEmail = request.getParameter("user_email");

        User user = IEMDB.getInstance().getUserByEmail(userEmail);
        user.removeFromWatchList(movieId);

        response.sendRedirect("/watchlist.jsp");
    }
}