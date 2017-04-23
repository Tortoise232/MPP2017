/**
 * Created by Petean Mihai on 3/20/2017.
 */
public class ClientValidator {
    ClientValidator(){};
    /**
     * Validates a client entity
     *
     * @param client
     *
     * @return a string that contains all the errors that occur with validating the given client
     *
     */
    public static String checkAttributes(Client client) {
        String pseudoError = "";
        if(client == null)
            return "ERROR: Null client!\n";
        pseudoError += (client.getName().isEmpty()) ? "ERROR: Empty client name!\n" : "";
        //pseudoError += (client.getId() <= 0) ? "ERROR: Invalid client ID\n" : "";
        return pseudoError;
    }

    public static boolean validate(Client client) throws ClientException {
        String error = checkAttributes(client);
        if(!error.isEmpty())
            throw new ClientException(error);
        return true;
    }

}
