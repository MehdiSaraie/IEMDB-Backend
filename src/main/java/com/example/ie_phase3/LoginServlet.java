//package com.example.ie_phase3;
//
//import classes.IEMDB;
//import classes.User;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebServlet(name = "LoginServlet", value = "/Login")
//public class LoginServlet extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("text/html");
//        String userEmail = request.getParameter("email");
//
//        User user = IEMDB.getInstance().getUserByEmail(userEmail);
//        if (user == null){
//            request.setAttribute("message","User Not Found");
//            request.getRequestDispatcher("/error.jsp").forward(request,response);
//        }else {
//            IEMDB.getInstance().setLoggedInUser(user);
//            response.sendRedirect("/");
//        }
//
//    }
//}
