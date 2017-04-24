/**
 * Created by Petean Mihai on 3/20/2017.
 */
public class MovieValidator {
    MovieValidator(){};
    /**
     * Validates a client entity
     *
     * @param movie
     *
     * @return a string that contains all the errors that occur with validating the given client
     *
     */
    public static String checkAttributes(Movie movie) {
        String pseudoError = "";
        if(movie == null)
            return "ERROR: Null movie!\n";
        pseudoError += (movie.getName().isEmpty()) ? "ERROR: Empty movie name!\n" : "";
        //pseudoError += (movie.getId() <= 0) ? "ERROR: Invalid movie ID\n" : "";
        pseudoError += (movie.getYearOfRelease() < 1900) ? "ERROR: Invalid year\n" : "";
        return pseudoError;
    }

    public static boolean validate(Movie movie) throws MovieException {
        String error = checkAttributes(movie);
        if(!error.isEmpty())
            throw new MovieException(error);
        return true;
    }
}
