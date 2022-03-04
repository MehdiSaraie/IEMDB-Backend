import IEMDB.Exception.*;
import IEMDB.IEMDB;
import IEMDB.Movie.*;
import IEMDB.User.User;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

public class UserTest {
    static IEMDB lB = new IEMDB();
    static User user;
    static Actor actor1, actor2, actor3;
    static Movie movie;

    @BeforeClass
    public static void beforeTest() throws Exception {
        actor1 = new Actor(1, "Marlon Brando", "1924-04-03", "American");
        actor2 = new Actor(2, "Al Pacino", "1940-04-25", "American");
        actor3 = new Actor(3, "James Caan", "1940-03-26", "American");
        lB.addActor(actor1);
        lB.addActor(actor2);
        lB.addActor(actor3);
    }

    @Before
    public void addFreshMovieAndUser() throws Exception {
        movie = new Movie(1, "The Godfather", "The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.", "1972-03-14", "Francis Ford Coppola", new ArrayList(Arrays.asList("Mario Puzo", "Francis Ford Coppola")), new ArrayList(Arrays.asList("Crime", "Drama")), new ArrayList(Arrays.asList(1, 2, 3)), 9.2, 175, 14);
        lB.addMovie(movie);
        user = new User("sara@ut.ac.ir", "sara1234", "Sara", "sara", "1998-03-11");
        lB.addUser(user);
    }

    @After
    public void removeUser() {
        lB.removeUser(user);
    }

    @Test
    public void testAddToWatchListForSuccess() throws Exception {
        lB.addMovieToWatchList("sara@ut.ac.ir", 1);
        assertTrue(user.getWatchList().contains(1));
    }

    @Test (expected = UserNotFoundException.class)
    public void testAddToWatchListForUserNotFound() throws Exception {
        lB.addMovieToWatchList("sara2@ut.ac.ir", 1);
    }

    @Test (expected = MovieNotFoundException.class)
    public void testAddToWatchListForMovieNotFound() throws Exception {
        lB.addMovieToWatchList("sara@ut.ac.ir", 2);
    }

    @Test (expected = MovieAlreadyExistsException.class)
    public void testAddToWatchListForMovieAlreadyExists() throws Exception {
        lB.addMovieToWatchList("sara@ut.ac.ir", 1);
        lB.addMovieToWatchList("sara@ut.ac.ir", 1);
    }

    @Test (expected = AgeLimitErrorException.class)
    public void testAddToWatchListForAgeLimitError() throws Exception {
        User user2 = new User("ali@ut.ac.ir", "ali1234", "Ali", "ali", "2009-03-11");
        lB.addUser(user2);
        lB.addMovieToWatchList("ali@ut.ac.ir", 1);
    }

}