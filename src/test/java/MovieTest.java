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
    @Before
    public void beforeTests() throws Exception {
        final String IEMDB_URI = "http://138.197.181.131:5000/api";
        InterfaceServer interfaceServer = new InterfaceServer();
        interfaceServer.importActorDataFromWeb(IEMDB_URI);
        interfaceServer.importMovieDataFromWeb(IEMDB_URI);
        iemdb = interfaceServer.getiemdb();
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
