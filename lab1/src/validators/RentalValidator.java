/**
 * Created by Petean Mihai on 3/20/2017.
 */
public class RentalValidator {
    RentalValidator(){};
    /**
     * Validates a client entity
     *
     * @param rental
     *
     * @return a string that contains all the errors that occur with validating the given client
     *
     */
    public static String checkAttributes(Rental rental) {
        String pseudoError = "";
        if(rental == null)
            return "ERROR: Null rental!\n";
        pseudoError += (rental.getMovie() <= 0) ? "ERROR: Invalid movie ID!\n" : "";
        pseudoError += (rental.getClient() <= 0) ? "ERROR: Invalid client ID!\n" : "";
        return pseudoError;
    }

    public static boolean validate(Rental rental) throws RentalException {
        String error = checkAttributes(rental);
        if(!error.isEmpty())
            throw new RentalException(error);
        return true;
    }
}
