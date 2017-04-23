import javafx.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by Petean Mihai on 3/7/2017.
 */
public class ConsoleView {

    ClientThread clientThread;
    public ConsoleView() throws SQLException, IOException {
        clientThread = new ClientThread();
    }

    public void printMenu(){
        System.out.println("Options:");
        System.out.println("1)Add movie");
        System.out.println("2)Add client");
        System.out.println("3)See movies");
        System.out.println("4)See clients");
        System.out.println("5)Remove Movie");
        System.out.println("6)Remove Client");
        System.out.println("7)Add Rental");
        System.out.println("8)Remove Rental");
        System.out.println("9)See rentals");
        System.out.println("0)Exit");
    }

    public void addMovie(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Movie name:");
        String name = scanner.nextLine();
        System.out.println("Year of release:");
        int yearOfRelease = scanner.nextInt();
        clientThread.serverOut.write("addMovie;" + name + ";" + yearOfRelease + ";" + 0 + "\n");
    }

    public void addClient(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Client name:");
        String name = scanner.nextLine();
        clientThread.serverOut.write("addClient;" + name + "\n");

    }

    public void removeMovie(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Movie title: ");
        if(scanner.hasNextLine()) {
            String name = scanner.nextLine();
            clientThread.serverOut.write("removeMovie;" + name + "\n");
        }
    }

    public void removeClient(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Client name: ");
        if(scanner.hasNextLine()) {
            String name = scanner.nextLine();
            clientThread.serverOut.write("removeClient;" + name + "\n");
        }
    }

    public Pair<String, String> getInputRental(){
        Scanner scanner = new Scanner(System.in);
        String clientName;
        String movieName;

        System.out.println("Movie Title: ");
        movieName = scanner.nextLine();

        System.out.println("Client Name: ");
        clientName = scanner.nextLine();
        return new Pair<>(movieName, clientName);
    }

    public void addRental(){
        Pair<String, String> rentalData = getInputRental();
        //rentalData key = clientName; value = movieName
        clientThread.serverOut.write("addRental;" + rentalData.getKey() + ";" + rentalData.getValue() + "\n");
        clientThread.serverOut.flush();
    }

    public void disconnect(){
        clientThread.serverOut.write("disconnect\n");
        clientThread.serverOut.flush();
    }


    public void removeRental(){
        Pair<String, String> rentalData = getInputRental();
        //rentalData key = clientName; value = movieName
        clientThread.serverOut.write("removeRental;" + rentalData.getKey() +";"+ rentalData.getValue() + "\n");
        clientThread.serverOut.flush();
    }

    // TODO: 21-Mar-17 Pretty print for the entities
    public void listMovies() throws IOException {
        System.out.println("SENDING LIST MOVIES INSTRUCTION");
        clientThread.serverOut.write("listMovies\n");
        clientThread.serverOut.flush();
        String response = "";

        //awaiting server response
        while(!clientThread.serverIn.ready()){}
        while(clientThread.serverIn.ready())
            response += clientThread.serverIn.readLine() + "\n";

        System.out.print(response);
    }

    public void listClients() throws IOException {
        clientThread.serverOut.write("listClients\n");
        clientThread.serverOut.flush();
        String response = "";

        //awaiting server response
        while(!clientThread.serverIn.ready()){}
        while(clientThread.serverIn.ready())
            response += clientThread.serverIn.readLine() + "\n";

        System.out.print(response);
    }

    public void listRentals() throws IOException {
        clientThread.serverOut.write("listRentals\n");
        clientThread.serverOut.flush();
        String response = clientThread.serverIn.readLine();

        //awaiting server response
        while(!clientThread.serverIn.ready()){}
        while(clientThread.serverIn.ready())
            response += clientThread.serverIn.readLine() + "\n";

        System.out.print(response);
    }

    public void mainLoop() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int option = -1;
        while(option != 0){
            printMenu();
            if(scanner.hasNextInt())
                option = scanner.nextInt();
            else
                System.out.println("Invalid Option");
            switch(option){
                case 1: addMovie(); break;
                case 2: addClient(); break;
                case 3: listMovies(); break;
                case 4: listClients(); break;
                case 5: removeMovie(); break;
                case 6: removeClient(); break;
                case 7: addRental(); break;
                case 8: removeRental(); break;
                case 9: listRentals(); break;
                default: break;
            }
            scanner.nextLine();
        }
        disconnect();
    }
}
