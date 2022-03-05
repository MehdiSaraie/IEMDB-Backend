import IEMDB.Exception.*;
import IEMDB.IEMDB;
import IEMDB.Movie.*;
import IEMDB.User.User;

import Interface.InterfaceServer.InterfaceServer;
import org.json.JSONObject;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

public class MovieTest {
    IEMDB iemdb;
    Movie movie;
    @Before
    public void beforeEachTest() throws Exception {
        final String IEMDB_URI = "http://138.197.181.131:5000/api";
        InterfaceServer interfaceServer = new InterfaceServer();
        interfaceServer.importUserDataFromWeb(IEMDB_URI);
        interfaceServer.importActorDataFromWeb(IEMDB_URI);
        interfaceServer.importMovieDataFromWeb(IEMDB_URI);
        iemdb = interfaceServer.getiemdb();
        movie = iemdb.findMovie(1);
    }

    @Test
    public void testRateMovieForSuccess() throws Exception {
        Rate rate1 = new Rate("sara@ut.ac.ir", 1, 5);
        iemdb.addRateToMovie(rate1);
        Rate rate2 = new Rate("sara@ut.ac.ir", 1, 7);
        iemdb.addRateToMovie(rate2);
        Rate rate3 = new Rate("sajjad@ut.ac.ir", 1, 3);
        iemdb.addRateToMovie(rate3);
        assertTrue(movie.getRatesByEmail().containsKey(rate2.getEmail()));
        assertEquals(rate2.getScore(), movie.getRatesByEmail().get(rate2.getEmail()).getScore());
        assertTrue(movie.getRatesByEmail().containsKey(rate3.getEmail()));
        assertEquals(rate3.getScore(), movie.getRatesByEmail().get(rate3.getEmail()).getScore());
        assertEquals(5, movie.getRating(), 0.001);
    }

    @Test (expected = UserNotFoundException.class)
    public void testRateMovieForUserNotFound() throws Exception {
        Rate rate = new Rate("sara2@ut.ac.ir", 1, 7);
        iemdb.addRateToMovie(rate);
        assertFalse(movie.getRatesByEmail().containsKey(rate.getEmail()));
        assertEquals(null, movie.getRating());
    }

    @Test (expected = MovieNotFoundException.class)
    public void testRateMovieForMovieNotFound() throws Exception {
        Rate rate = new Rate("sara@ut.ac.ir", 200, 7);
        iemdb.addRateToMovie(rate);
    }

    @Test (expected = InvalidRateScoreException.class)
    public void testRateMovieForInvalidScore() throws Exception {
        Rate rate = new Rate("sara@ut.ac.ir", 1, 11);
        iemdb.addRateToMovie(rate);
        assertFalse(movie.getRatesByEmail().containsKey(rate.getEmail()));
        assertEquals(null, movie.getRating());
    }


    @Test
    public void testGetMoviesByReleaseDateForSuccess() {
        Integer startYear = 2009;
        Integer endYear = 2010;
        List<Movie> movies = iemdb.getMoviesByReleaseDate(startYear, endYear);
        List<Integer> movieIds = new ArrayList<>();
        for (Movie movie : movies){
            movieIds.add(movie.getId());
        }
        assertEquals(Arrays.asList(9, 11), movieIds);
    }

    @Test (expected = NumberFormatException.class)
    public void testGetMoviesByReleaseDateForBadYear() {
        Integer startYear = Integer.valueOf("ali");
        Integer endYear = 2010;
        List<Movie> movies = iemdb.getMoviesByReleaseDate(startYear, endYear);
    }

}
