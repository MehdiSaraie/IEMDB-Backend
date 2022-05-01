package com.example.ie_phase3;

import classes.IEMDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AddCommentServlet", value = "/AddComment")
public class AddCommentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userEmail = request.getParameter("user_email");
        int movieId =  Integer.parseInt(request.getParameter("movie_id"));
        String text = request.getParameter("comment");
        IEMDB.getInstance().addComment(userEmail, movieId, text);
        response.sendRedirect("/movie.jsp?movie_id=" + movieId);
    }
}
