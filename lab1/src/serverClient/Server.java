import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Petean Mihai on 4/1/2017.
 */
public class Server {
        ServerSocket serverSocket;
        Controller controller;
        ExecutorService executorService;
        static int threadID = 0;

        public Server() throws IOException, SQLException {
            serverSocket = new ServerSocket(6969);
            executorService = Executors.newFixedThreadPool(10);
            controller = new Controller(true);
            System.out.print("SERVER IS RUNNING\n");
        }

        public Server(ServerSocket socket){
            serverSocket = socket;
        }



        //this will be the protocol to connect to a client
        public void acceptClient(Socket client) throws IOException, MovieException, RentalException, ClientException {
            System.out.println("ACCEPTING CLIENT " + Thread.currentThread().getId());
            PrintWriter clientOut = new PrintWriter(client.getOutputStream(), true);
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
            serveClient(clientIn, clientOut, client);
        }

        //this will make the server loop and wait for a client to connect
        public void awaitClient() throws IOException, MovieException, RentalException, ClientException {
            while(true){
                //System.out.println("AWAITING CLIENT");
                //Socket client = new Socket();
                Socket client = serverSocket.accept();
                executorService.submit(new ServiceRequest(client));
                //acceptClient(client);
            }
        }

        //this will make the server loop and wait for client instructions
        public void serveClient(BufferedReader clientIn, PrintWriter clientOut, Socket client) throws IOException, MovieException, ClientException, RentalException {
            //System.out.println("SERVING CLIENT");
            String instructionToServe = "";
            while(!instructionToServe.equals("disconnect")){
                instructionToServe = clientIn.readLine();
                System.out.println("RECEIVED INSTRUCTION FROM CLIENT " + Thread.currentThread().getId() + ": " + instructionToServe);
                //if no instructions
                if(instructionToServe.isEmpty())
                    continue;

                String[] segmentedInstruction = instructionToServe.split(";");
                switch(segmentedInstruction[0]) {
                    case "addMovie": addMovie(segmentedInstruction); break;
                    case "removeMovie": removeMovie(segmentedInstruction); break;
                    case "listMovies" : clientOut.write(listMovies()); clientOut.flush(); break;

                    case "addClient": addClient(segmentedInstruction); break;
                    case "removeClient": removeClient(segmentedInstruction); break;
                    case "listClients": clientOut.write(listClients()); clientOut.flush(); break;

                    case "addRental": addRental(segmentedInstruction); break;
                    case "removeRental": removeRental(segmentedInstruction); break;
                    case "listRentals": clientOut.write(listRentals()); clientOut.flush(); break;

                    case "disconnect": client.close(); break;
                }
            }
        }

    //movie repo business
    private void addMovie(String[] instruction) throws MovieException {
        int yearOfRelease = Integer.parseInt(instruction[2]);
        int rating = Integer.parseInt(instruction[3]);
        Movie newMovie = new Movie(instruction[1], rating, yearOfRelease);

        //rudimentary log, please write an actual logger
        System.out.println("ADDING MOVIE " + newMovie);

        controller.addMovie(newMovie);
    }

    private void removeMovie(String[] instruction) throws MovieException{
         controller.removeMovie(instruction[1]);
    }

    private String listMovies(){
        final String[] result = {new String()};
        //papa f.p. master would be proud
        controller.getMovies().forEach(movie -> result[0] += movie.toString() + "\n");
        return result[0];
    }

    //client repo business
    private void addClient(String[] instruction) throws ClientException {
        Client myClient = new Client(instruction[1]);
        controller.addClient(myClient);
    }

    private void removeClient(String[] instruction){
        controller.removeClient(instruction[1]);
    }

    private String listClients(){
        final String[] result = {new String()};
        controller.getClients().forEach(client -> result[0] += client.toString() + "\n");
        return result[0];
    }

    //rental repo business
    private void addRental(String[] instruction) throws RentalException {
        String movieName = instruction[1];
        String clientName = instruction[2];
        controller.addRental(movieName,clientName);
    }

    private void removeRental(String[] instruction){
        //it will be in the removal command so that's okay
        controller.removeRental(instruction[1],instruction[2]);
    }

    private String listRentals(){
        final String[] result = {new String()};
        controller.getRentals().forEach(rental -> result[0] += rental.toString() + "\n");
        return result[0];
    }

    class ServiceRequest implements Runnable{
        private Socket socket;

        public ServiceRequest(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                acceptClient(socket);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MovieException e) {
                e.printStackTrace();
            } catch (RentalException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }


}
