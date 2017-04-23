import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created by Petean Mihai on 3/7/2017.
 */
public class Main {
    public static void main(String[] args) throws SQLException, IOException, MovieException, RentalException, ClientException {
        System.out.println("Type server to start server or become client...");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        if(choice.equals("server")){
            Server myServer = new Server();
            myServer.awaitClient();
        }
        else {
            ConsoleView myView = new ConsoleView();
            myView.mainLoop();
        }
        System.out.println("why?");

    }
}
