package com.example.ie_phase3;

import Tools.PairComparator.PairFIComparator;
import classes.IEMDB;
import classes.Movie;
import javafx.util.Pair;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "SortServlet", value = "/Sort")
public class SortServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case ("sort_by_imdb"): {
                request.setAttribute("moviesList", IEMDB.getInstance().getMovies());
                request.getRequestDispatcher("/movies.jsp").forward(request, response);
                break;
            }
            case ("sort_by_date"): {
                IEMDB iemdb = IEMDB.getInstance();
                iemdb.sortByDate();
                request.setAttribute("moviesList", iemdb.getMovies());
                request.getRequestDispatcher("/movies.jsp").forward(request, response);
            }
        }
    }
}

