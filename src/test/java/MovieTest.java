import IEMDB.Exception.*;
import IEMDB.LoadBalancer;
import IEMDB.Movie.*;
import IEMDB.User.User;

import org.json.JSONObject;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MovieTest {
    static LoadBalancer lB = new LoadBalancer();
    static User user1, user2;
    static Actor actor1, actor2, actor3;
    static Movie movie;

    @BeforeClass
    public static void beforeTest() throws Exception {
        user1 = new User("sara@ut.ac.ir", "sara1234", "Sara", "sara", "1998-03-11");
        lB.addUser(user1);
        user2 = new User("sajjad@ut.ac.ir", "sajjad1234", "Sajjad", "sajjad", "2000-06-14");
        lB.addUser(user2);

        actor1 = new Actor(1, "Marlon Brando", "1924-04-03", "American");
        actor2 = new Actor(2, "Al Pacino", "1940-04-25", "American");
        actor3 = new Actor(3, "James Caan", "1940-03-26", "American");
        lB.addActor(actor1);
        lB.addActor(actor2);
        lB.addActor(actor3);
    }

    @Before
    public void addFreshMovie() {
        movie = new Movie(1, "The Godfather", "The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.", "1972-03-14", "Francis Ford Coppola", new ArrayList(Arrays.asList("Mario Puzo", "Francis Ford Coppola")), new ArrayList(Arrays.asList("Crime", "Drama")), new ArrayList(Arrays.asList(1, 2, 3)), 9.2, 175, 14);
        lB.addMovie(movie);
    }

    @Test
    public void testRateMovieForSuccess() throws Exception {
        Rate rate1 = new Rate("sara@ut.ac.ir", 1, 5);
        lB.addRateToMovie(rate1);
        Rate rate2 = new Rate("sara@ut.ac.ir", 1, 7);
        lB.addRateToMovie(rate2);
        Rate rate3 = new Rate("sajjad@ut.ac.ir", 1, 3);
        lB.addRateToMovie(rate3);
        assertTrue(movie.getRatesByEmail().containsKey(rate2.getEmail()));
        assertEquals(rate2.getScore(), movie.getRatesByEmail().get(rate2.getEmail()).getScore());
        assertTrue(movie.getRatesByEmail().containsKey(rate3.getEmail()));
        assertEquals(rate3.getScore(), movie.getRatesByEmail().get(rate3.getEmail()).getScore());
        assertEquals(5, movie.getRating(), 0.001);
    }

    @Test (expected = UserNotFoundException.class)
    public void testRateMovieForUserNotFound() throws Exception {
        Rate rate = new Rate("sara2@ut.ac.ir", 1, 7);
        lB.addRateToMovie(rate);
        assertFalse(movie.getRatesByEmail().containsKey(rate.getEmail()));
        assertEquals(null, movie.getRating());
    }

    @Test (expected = MovieNotFoundException.class)
    public void testRateMovieForMovieNotFound() throws Exception {
        Rate rate = new Rate("sara@ut.ac.ir", 2, 7);
        lB.addRateToMovie(rate);
    }

    @Test (expected = InvalidRateScoreException.class)
    public void testRateMovieForInvalidScore() throws Exception {
        Rate rate = new Rate("sara@ut.ac.ir", 1, 11);
        lB.addRateToMovie(rate);
        assertFalse(movie.getRatesByEmail().containsKey(rate.getEmail()));
        assertEquals(null, movie.getRating());
    }

    @Test
    public void testVoteCommentForSuccess() throws Exception {
        lB.addComment("sajjad@ut.ac.ir", 1, "Interesting Movie");
        lB.addVoteToComment("sara@ut.ac.ir", 1, -1);
        lB.addVoteToComment("sara@ut.ac.ir", 1, 1);
        assertTrue(lB.getComment(1).getVoters().containsKey("sara@ut.ac.ir"));
        assertEquals((Integer) 1, lB.getComment(1).getVoters().get("sara@ut.ac.ir"));
        assertEquals((Integer) 1, lB.getComment(1).getLikesCount());
        assertEquals((Integer) 0, lB.getComment(1).getDislikesCount());
        lB.removeComment(1);
    }

    @Test (expected = UserNotFoundException.class)
    public void testVoteCommentForUserNotFound() throws Exception {
        lB.addComment("sajjad@ut.ac.ir", 1, "Interesting Movie");
        lB.addVoteToComment("sara2@ut.ac.ir", 1, 1);
        assertFalse(lB.getComment(1).getVoters().containsKey("sara2@ut.ac.ir"));
        lB.removeComment(1);
    }

    @Test (expected = CommentNotFoundException.class)
    public void testVoteCommentForCommentNotFound() throws Exception {
        lB.addComment("sajjad@ut.ac.ir", 1, "Interesting Movie");
        lB.addVoteToComment("sara@ut.ac.ir", 2, 1);
        assertFalse(lB.getComment(1).getVoters().containsKey("sara@ut.ac.ir"));
        lB.removeComment(1);
    }

    @Test (expected = InvalidVoteValueException.class)
    public void testVoteCommentForInvalidVote() throws Exception {
        lB.addComment("sajjad@ut.ac.ir", 1, "Interesting Movie");
        lB.addVoteToComment("sara@ut.ac.ir", 1, 2);
        assertFalse(lB.getComment(1).getVoters().containsKey("sara@ut.ac.ir"));
        lB.removeComment(1);
    }

    @Test
    public void testGetMoviesByGenre() throws Exception{
        String movies = "[{\"director\":\"Francis Ford Coppola\",\"name\":\"The Godfather\",\"genre\":\"[Crime, Drama]\",\"movieId\":\"1\"}, {\"director\":\"Francis Ford Coppola\",\"name\":\"The Godfather\",\"genre\":\"[Crime, Drama]\",\"movieId\":\"1\"}, {\"director\":\"Francis Ford Coppola\",\"name\":\"The Godfather\",\"genre\":\"[Crime, Drama]\",\"movieId\":\"1\"}, {\"director\":\"Francis Ford Coppola\",\"name\":\"The Godfather\",\"genre\":\"[Crime, Drama]\",\"movieId\":\"1\"}, {\"director\":\"Francis Ford Coppola\",\"name\":\"The Godfather\",\"genre\":\"[Crime, Drama]\",\"movieId\":\"1\"}]";
        Map<String, String> elements = new HashMap<>();
        elements.put("MoviesListByGenre", movies);
        JSONObject expectedMovies = new JSONObject(elements);
        assertEquals(expectedMovies.toString(), lB.getMoviesByGenre("Drama"));
        assertTrue(true);
    }
}
