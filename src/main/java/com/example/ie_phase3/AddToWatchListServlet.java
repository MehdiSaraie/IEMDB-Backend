//package com.example.ie_phase3;
//
//import classes.CustomException;
//import classes.IEMDB;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebServlet(name = "AddToWatchListServlet", value = "/AddToWatchList")
//public class AddToWatchListServlet extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int movieId =  Integer.parseInt(request.getParameter("movie_id"));
//        String userEmail = request.getParameter("user_email");
//        try {
//            IEMDB.getInstance().addToWatchList(userEmail, movieId);
//        } catch (CustomException e) {
//            request.setAttribute("message","sorry, Age limit of this movie is greater than your age!");
//            request.getRequestDispatcher("/error.jsp").forward(request,response);
//        }
//        response.sendRedirect("/movie.jsp?movie_id=" + movieId);
//    }
//}
