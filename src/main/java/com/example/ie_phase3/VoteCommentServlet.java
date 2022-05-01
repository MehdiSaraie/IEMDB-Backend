package com.example.ie_phase3;

import classes.IEMDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "VoteCommentServlet", value = "/VoteComment")
public class VoteCommentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int commentId =  Integer.parseInt(request.getParameter("comment_id"));
        String userEmail = request.getParameter("user_email");
        int vote = Integer.parseInt(request.getParameter("vote"));
        int movieId =  Integer.parseInt(request.getParameter("movie_id"));
        IEMDB.getInstance().voteComment(userEmail, commentId, vote);
        response.sendRedirect("/movie.jsp?movie_id=" + movieId);
    }
}
