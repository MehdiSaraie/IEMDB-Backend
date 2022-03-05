import IEMDB.Exception.*;
import IEMDB.IEMDB;
import IEMDB.Movie.*;
import IEMDB.User.User;

import Interface.InterfaceServer.InterfaceServer;
import org.junit.*;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserTest {
    IEMDB iemdb;
    @Before
    public void beforeTests() throws Exception {
        final String IEMDB_URI = "http://138.197.181.131:5000/api";
        InterfaceServer interfaceServer = new InterfaceServer();
        interfaceServer.importUserDataFromWeb(IEMDB_URI);
        interfaceServer.importActorDataFromWeb(IEMDB_URI);
        interfaceServer.importMovieDataFromWeb(IEMDB_URI);
        iemdb = interfaceServer.getiemdb();
    }

    @Test
    public void testGetWatchlistForSuccess() throws Exception {
        iemdb.addMovieToWatchList("sara@ut.ac.ir", 1);
        iemdb.addMovieToWatchList("sara@ut.ac.ir", 2);
        List<Movie> watchlist = iemdb.getWatchList("sara@ut.ac.ir");
        List<Integer> movieIds = new ArrayList<>();
        for (Movie movie : watchlist){
            movieIds.add(movie.getId());
        }
        assertEquals(Arrays.asList(1, 2), movieIds);
        iemdb.removeMovieFromWatchList("sara@ut.ac.ir", 2);
        watchlist = iemdb.getWatchList("sara@ut.ac.ir");
        movieIds = new ArrayList<>();
        for (Movie movie : watchlist){
            movieIds.add(movie.getId());
        }
        assertEquals(Arrays.asList(1), movieIds);
    }

    @Test (expected = UserNotFoundException.class)
    public void testGetWatchlistForUserNotFound() throws Exception {
        iemdb.getWatchList("sara2@ut.ac.ir");
    }

}